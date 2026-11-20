package com.example.ui.components

import android.opengl.Matrix
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.sin

/**
 * Exact joint index layout matching the 21-bone skeleton baked into assets/models/judy.glb.
 */
internal object JudyBones {
    const val COUNT = 21

    const val HIPS = 0
    const val SPINE = 1
    const val CHEST = 2
    const val NECK = 3
    const val HEAD = 4
    const val EAR_BASE_L = 5
    const val EAR_TIP_L = 6
    const val EAR_BASE_R = 7
    const val EAR_TIP_R = 8
    const val UPPER_ARM_L = 9
    const val FORE_ARM_L = 10
    const val HAND_L = 11
    const val UPPER_ARM_R = 12
    const val FORE_ARM_R = 13
    const val HAND_R = 14
    const val THIGH_L = 15
    const val SHIN_L = 16
    const val FOOT_L = 17
    const val THIGH_R = 18
    const val SHIN_R = 19
    const val FOOT_R = 20
}

/**
 * Exact morph target index layout matching meshes 2..9 in assets/models/judy.glb.
 */
internal object JudyMorphs {
    const val COUNT = 6

    const val BLINK_L = 0
    const val BLINK_R = 1
    const val MOUTH_OPEN = 2
    const val SMILE = 3
    const val BROW_UP = 4
    const val BROW_FOCUS = 5
}

/**
 * Real-time damped harmonic spring oscillator for secondary physics
 * (bunny ears, squash-and-stretch, head follow-through, and momentum bounce).
 */
private class Spring1D(
    var value: Float = 0f,
    private val stiffness: Float,
    private val damping: Float
) {
    var velocity: Float = 0f

    fun step(target: Float, dt: Float, impulse: Float = 0f): Float {
        velocity += impulse
        val force = (target - value) * stiffness - velocity * damping
        velocity += force * dt
        value += velocity * dt
        return value
    }
}

/**
 * Per-bone Euler pose target (degrees) + optional local translation offset (meters).
 */
internal class BonePose {
    var pitch: Float = 0f
    var yaw: Float = 0f
    var roll: Float = 0f
    var tx: Float = 0f
    var ty: Float = 0f
    var tz: Float = 0f

    fun set(
        pitch: Float = 0f,
        yaw: Float = 0f,
        roll: Float = 0f,
        tx: Float = 0f,
        ty: Float = 0f,
        tz: Float = 0f
    ) {
        this.pitch = pitch
        this.yaw = yaw
        this.roll = roll
        this.tx = tx
        this.ty = ty
        this.tz = tz
    }

    fun blendToward(target: BonePose, alpha: Float) {
        pitch += (target.pitch - pitch) * alpha
        yaw += (target.yaw - yaw) * alpha
        roll += (target.roll - roll) * alpha
        tx += (target.tx - tx) * alpha
        ty += (target.ty - ty) * alpha
        tz += (target.tz - tz) * alpha
    }
}

/**
 * Complete procedural full-body skeleton + spring-physics + facial morph animation engine for Judy.
 * Replaces static A-pose with grounded stances, real knee-bend jumps, spring-physics bunny ears,
 * expressive hands/arms per emotion, independent winks, blinking, brow expressions, and lip-sync.
 */
internal class JudyPhysicsEngine {
    val poses = Array(JudyBones.COUNT) { BonePose() }
    private val targets = Array(JudyBones.COUNT) { BonePose() }
    val morphWeights = FloatArray(JudyMorphs.COUNT)

    // Root world transform outputs (applied to asset.root)
    var rootYawDeg: Float = 0f
    var rootPitchDeg: Float = 0f
    var rootRollDeg: Float = 0f
    var rootOffsetY: Float = 0f
    var rootScaleX: Float = 1f
    var rootScaleY: Float = 1f
    var rootScaleZ: Float = 1f

    // Secondary physics springs for tall bunny ears (base + floppy tip whip)
    private val earBaseLPitchSpring = Spring1D(0f, stiffness = 135f, damping = 11.5f)
    private val earBaseLRollSpring = Spring1D(0f, stiffness = 120f, damping = 10.5f)
    private val earTipLPitchSpring = Spring1D(0f, stiffness = 92f, damping = 7.8f)
    private val earTipLRollSpring = Spring1D(0f, stiffness = 86f, damping = 7.2f)

    private val earBaseRPitchSpring = Spring1D(0f, stiffness = 135f, damping = 11.5f)
    private val earBaseRRollSpring = Spring1D(0f, stiffness = 120f, damping = 10.5f)
    private val earTipRPitchSpring = Spring1D(0f, stiffness = 92f, damping = 7.8f)
    private val earTipRRollSpring = Spring1D(0f, stiffness = 86f, damping = 7.2f)

    // Secondary springs for body bounce, squash/stretch, and head follow-through
    private val squashSpring = Spring1D(0f, stiffness = 190f, damping = 13f)
    private val headFollowYaw = Spring1D(0f, stiffness = 90f, damping = 11f)
    private val torsoLagYaw = Spring1D(0f, stiffness = 75f, damping = 10f)

    // Smooth blended channels
    private var smoothSpeak = 0f
    private var smoothSmile = 0.40f
    private var smoothRelaxLids = 0f
    private var smoothBrowUp = 0f
    private var smoothBrowFocus = 0f
    private var prevExpression: MascotExpression? = null
    private var moodChangeElapsed = 99f
    private var prevJumpHeight = 0f
    private var prevJumpVel = 0f
    private var initializedPose = false

    // Scratch matrices for zero-GC frame updates
    private val scratchRot = FloatArray(16)
    private val scratchOut = FloatArray(16)

    fun triggerTapImpulse() {
        // Elastic impulse into ears & body squash when user taps Judy
        earBaseLPitchSpring.velocity -= 190f
        earBaseRPitchSpring.velocity -= 170f
        earTipLPitchSpring.velocity -= 260f
        earTipRPitchSpring.velocity -= 240f
        squashSpring.velocity -= 0.75f
    }

    fun step(
        dt: Float,
        elapsedSec: Float,
        expression: MascotExpression,
        isBreathing: Boolean,
        isSpeaking: Boolean,
        tapElapsedSec: Float,
        userYawDeg: Float,
        userPitchDeg: Float,
        userYawVelocityDeg: Float
    ) {
        if (prevExpression != expression) {
            if (prevExpression != null) {
                moodChangeElapsed = 0f
                // Subtle ear perk & body pop on emotion switch
                earBaseLPitchSpring.velocity -= 110f
                earBaseRPitchSpring.velocity -= 95f
                earTipLPitchSpring.velocity -= 140f
                earTipRPitchSpring.velocity -= 125f
                squashSpring.velocity += 0.45f
            }
            prevExpression = expression
        } else {
            moodChangeElapsed = (moodChangeElapsed + dt).coerceAtMost(99f)
        }

        val poseBlend = if (!initializedPose) 1f else (1f - exp(-10.5f * dt))
        val facialBlend = if (!initializedPose) 1f else (1f - exp(-9.5f * dt))
        initializedPose = true

        smoothSpeak = lerp(smoothSpeak, if (isSpeaking) 1f else 0f, facialBlend)

        // Reset targets
        for (i in 0 until JudyBones.COUNT) targets[i].set()

        // --- 1. REAL 3-PHASE ATHLETIC BUNNY JUMP PHYSICS (tap jump + periodic EXCITED hop) ---
        val tapJumpDur = 0.78f
        val isTapJump = tapElapsedSec in 0f..tapJumpDur
        val isTapWaving = tapElapsedSec in 0f..1.85f

        val autoHopPeriod = 3.1f
        val autoHopPhase = elapsedSec % autoHopPeriod
        val isAutoHop = (expression == MascotExpression.EXCITED && !isTapJump && autoHopPhase in 0.55f..1.30f)

        var jumpHeight = 0f
        var squatFactor = 0f      // 0..1 grounded knee squat (anticipation & landing)
        var airTuckFactor = 0f    // 0..1 mid-air knee tuck & arm fling
        var jumpSpinDeg = 0f
        var targetSquash = 0f

        if (isTapJump) {
            when {
                tapElapsedSec < 0.13f -> {
                    // Phase 1: Deep anticipation squat (feet stay planted on ground!)
                    val p = tapElapsedSec / 0.13f
                    val ease = sin(p * (PI.toFloat() * 0.5f))
                    squatFactor = ease * 0.92f
                    targetSquash = -0.075f * ease
                }
                tapElapsedSec < 0.56f -> {
                    // Phase 2: Explosive airborne arc
                    val p = (tapElapsedSec - 0.13f) / 0.43f
                    val arc = sin(p * PI.toFloat())
                    jumpHeight = arc * 0.22f
                    airTuckFactor = arc
                    jumpSpinDeg = sin(p * PI.toFloat()) * 16f
                    targetSquash = (1f - abs(p - 0.5f) * 2f) * 0.065f
                }
                else -> {
                    // Phase 3: Cushioned knee-bend landing & spring settle
                    val p = (tapElapsedSec - 0.56f) / 0.22f
                    val absorb = sin(p * PI.toFloat())
                    squatFactor = absorb * 0.68f
                    targetSquash = -0.065f * absorb
                }
            }
        } else if (isAutoHop) {
            val t = autoHopPhase - 0.55f // 0..0.75s
            when {
                t < 0.12f -> {
                    val ease = sin((t / 0.12f) * (PI.toFloat() * 0.5f))
                    squatFactor = ease * 0.55f
                    targetSquash = -0.045f * ease
                }
                t < 0.55f -> {
                    val p = (t - 0.12f) / 0.43f
                    val arc = sin(p * PI.toFloat())
                    jumpHeight = arc * 0.13f
                    airTuckFactor = arc * 0.65f
                    targetSquash = (1f - abs(p - 0.5f) * 2f) * 0.04f
                }
                else -> {
                    val p = (t - 0.55f) / 0.20f
                    val absorb = sin(p * PI.toFloat())
                    squatFactor = absorb * 0.42f
                    targetSquash = -0.04f * absorb
                }
            }
        }

        // Compute vertical velocity & acceleration for realistic ear whip physics!
        val jumpVel = (jumpHeight - prevJumpHeight) / dt
        val jumpAccel = ((jumpVel - prevJumpVel) / dt).coerceIn(-25f, 25f)
        prevJumpHeight = jumpHeight
        prevJumpVel = jumpVel

        val squashVal = squashSpring.step(targetSquash, dt)
        val breathPhase = elapsedSec * (if (expression == MascotExpression.RELAX) 1.25f else 2.15f)
        val breath = if (isBreathing) sin(breathPhase) else 0f

        // --- 2. WINK, BLINK, AND SPEAKING CADENCE ---
        val tapWink = if (tapElapsedSec in 0.08f..0.74f) {
            sin(((tapElapsedSec - 0.08f) / 0.66f) * PI.toFloat())
        } else 0f

        val winkCycle = elapsedSec % 4.4f
        val moodWink = if (
            (expression == MascotExpression.HAPPY || expression == MascotExpression.CURIOUS || expression == MascotExpression.EXCITED) &&
            winkCycle in 1.5f..2.18f
        ) {
            sin(((winkCycle - 1.5f) / 0.68f) * PI.toFloat())
        } else 0f
        val winkWeight = max(tapWink, moodWink).coerceIn(0f, 1f)

        val blinkCycle = elapsedSec % 3.6f
        val naturalBlink = when {
            blinkCycle in 3.22f..3.38f -> sin(((blinkCycle - 3.22f) / 0.16f) * PI.toFloat())
            blinkCycle in 3.44f..3.58f && (elapsedSec.toInt() % 3 == 0) ->
                sin(((blinkCycle - 3.44f) / 0.14f) * PI.toFloat())
            else -> 0f
        }

        // Multi-harmonic syllabic speech envelope (realistic phrases with micro-pauses)
        val phraseGate = (0.55f + 0.45f * sin(elapsedSec * 3.1f)).coerceIn(0f, 1f)
        val syllableWave = (abs(sin(elapsedSec * 12.8f)) * 0.62f +
            abs(sin(elapsedSec * 20.4f + 0.7f)) * 0.38f) * phraseGate
        val mouthOpenWeight = (syllableWave * 0.92f + 0.06f) * smoothSpeak

        // --- 3. FULL-BODY EXPRESSIVE POSES PER EMOTION (NEVER A-POSE!) ---
        // Note on coordinate convention in judy.glb (Y-up, +Z forward, +X is Judy's Left):
        //  - Arms start at ~40° below horizontal:
        //      UpperArm_L (+X): roll = -42° drops arm naturally down by her side; roll = +55° raises arm high to wave!
        //      UpperArm_R (-X): roll = +42° drops arm naturally down by her side; roll = -55° raises arm high!
        //      UpperArm_L yaw < 0 brings arm forward; UpperArm_R yaw > 0 brings arm forward.
        //  - Legs (pointing straight down -Y):
        //      Thigh pitch < 0 lifts knee FORWARD (+Z).
        //      Shin pitch > 0 bends lower leg BACKWARD (-Z) at the knee.
        //      Foot pitch < 0 flexes toes UP to stay flat during squat; Foot pitch > 0 points toes down in air.
        val sway = sin(elapsedSec * 1.45f)
        val swaySlow = sin(elapsedSec * 0.95f)
        val upbeatBounce = abs(sin(elapsedSec * 4.2f))

        // Base grounded hip & leg stance
        var hipDropY = -squatFactor * 0.078f
        var hipShiftX = 0f
        var hipPitch = squatFactor * 14f - airTuckFactor * 8f
        var hipYaw = 0f
        var hipRoll = 0f

        var thighLPitch = -squatFactor * 36f - airTuckFactor * 32f
        var shinLPitch = squatFactor * 70f + airTuckFactor * 58f
        var footLPitch = -squatFactor * 34f + airTuckFactor * 24f

        var thighRPitch = -squatFactor * 36f - airTuckFactor * 24f
        var shinRPitch = squatFactor * 70f + airTuckFactor * 48f
        var footRPitch = -squatFactor * 34f + airTuckFactor * 20f

        var spinePitch = 0f
        var spineYaw = 0f
        var spineRoll = 0f
        var chestPitch = -breath * 2.2f
        var chestYaw = 0f
        var chestRoll = 0f

        var headPitch = 0f
        var headYaw = 0f
        var headRoll = 0f

        // Natural relaxed arms-down baseline (NO A-pose!)
        var uaLPitch = 4f; var uaLYaw = -10f; var uaLRoll = -42f
        var faLPitch = 0f; var faLYaw = -18f; var faLRoll = 12f
        var handLPitch = 0f; var handLYaw = 0f; var handLRoll = 6f

        var uaRPitch = 4f; var uaRYaw = 10f; var uaRRoll = 42f
        var faRPitch = 0f; var faRYaw = 18f; var faRRoll = -12f
        var handRPitch = 0f; var handRYaw = 0f; var handRRoll = -6f

        // Ear rest targets (pitch < 0 perks ears forward, pitch > 0 relaxes back, roll tilts outward/inward)
        var earTargetLPitch = -4f
        var earTargetLRoll = -4f
        var earTargetRPitch = -4f
        var earTargetRRoll = 4f

        var targetSmile = 0.40f
        var targetRelaxLids = 0f
        var targetBrowUp = 0f
        var targetBrowFocus = 0f

        when (expression) {
            MascotExpression.IDLE -> {
                // Relaxed weight shift, gentle breathing, occasional cute foot tap & curious glance
                hipShiftX = swaySlow * 0.014f
                hipRoll = swaySlow * 3.5f
                hipYaw = sin(elapsedSec * 0.8f) * 4f
                spineRoll = -swaySlow * 2.5f
                chestRoll = -swaySlow * 1.8f

                // Natural relaxed stance with slight knee micro-flex + playful right-foot tap every 5s
                val tapFootCycle = elapsedSec % 5.2f
                val footTap = if (tapFootCycle in 3.6f..4.5f) {
                    abs(sin((tapFootCycle - 3.6f) * 14f)) * 14f
                } else 0f
                thighLPitch -= 4f + max(0f, swaySlow) * 3f
                shinLPitch += 8f + max(0f, swaySlow) * 6f
                footLPitch -= 4f
                thighRPitch -= 5f + max(0f, -swaySlow) * 3f
                shinRPitch += 10f + max(0f, -swaySlow) * 6f
                footRPitch -= 5f + footTap

                headYaw = sin(elapsedSec * 0.72f) * 9f
                headPitch = sin(elapsedSec * 1.1f) * 3.5f
                headRoll = sin(elapsedSec * 0.65f) * 4.5f

                // Arms hanging comfortably at sides with soft breathing swing
                uaLRoll = -41f + breath * 2.5f
                uaLYaw = -10f + swaySlow * 4f
                faLYaw = -20f
                faLRoll = 14f

                uaRRoll = 41f - breath * 2.5f
                uaRYaw = 10f - swaySlow * 4f
                faRYaw = 20f
                faRRoll = -14f

                earTargetLPitch = -4f + breath * 3f
                earTargetRPitch = -4f + breath * 3f
                earTargetLRoll = -5f + swaySlow * 4f
                earTargetRRoll = 5f + swaySlow * 4f
                targetSmile = 0.42f
            }

            MascotExpression.HAPPY -> {
                // Bouncy joyful groove, cheerful left-hand greeting wave, big bright smile & playful winks!
                val bounce = upbeatBounce
                hipDropY -= (1f - bounce) * 0.022f
                hipRoll = sin(elapsedSec * 2.6f) * 5.5f
                hipYaw = sin(elapsedSec * 2.6f) * 7f
                spineRoll = -hipRoll * 0.45f
                chestPitch += -4f + bounce * 3f
                chestYaw = sin(elapsedSec * 2.6f + 0.4f) * 5f

                // Bouncy knees on the beat
                val kneeBounce = (1f - bounce) * 12f
                thighLPitch -= kneeBounce
                shinLPitch += kneeBounce * 2f
                footLPitch -= kneeBounce
                thighRPitch -= kneeBounce * 0.9f
                shinRPitch += kneeBounce * 1.8f
                footRPitch -= kneeBounce * 0.9f

                headYaw = sin(elapsedSec * 2.2f) * 8f
                headPitch = -3f + bounce * 4f
                headRoll = sin(elapsedSec * 2.6f) * 6f + winkWeight * 9f

                // Left arm raised in a friendly warm wave!
                val waveOsc = sin(elapsedSec * 8.2f)
                uaLRoll = 42f
                uaLYaw = -24f
                uaLPitch = -8f
                faLRoll = 56f + waveOsc * 16f
                faLYaw = -24f
                handLRoll = waveOsc * 26f
                handLPitch = cos(elapsedSec * 8.2f) * 8f

                // Right arm cheerful resting swing near hip
                uaRRoll = 34f - bounce * 8f
                uaRYaw = 14f + sin(elapsedSec * 2.6f) * 8f
                faRRoll = -26f
                faRYaw = 28f
                handRRoll = -10f + sin(elapsedSec * 4.2f) * 8f

                earTargetLPitch = -10f - bounce * 5f
                earTargetRPitch = -10f - bounce * 5f
                earTargetLRoll = -8f - bounce * 6f
                earTargetRRoll = 8f + bounce * 6f

                targetSmile = 0.90f
                targetBrowUp = 0.35f
            }

            MascotExpression.EXCITED -> {
                // High-energy celebration! Big enthusiastic wave, bouncy knees, celebratory hops, sparkly grin!
                val energyOsc = sin(elapsedSec * 10.5f)
                val bodyGroove = sin(elapsedSec * 3.8f)
                hipDropY -= abs(bodyGroove) * 0.025f
                hipRoll = bodyGroove * 7f
                hipYaw = bodyGroove * 9f
                chestPitch += -6f
                chestRoll = -bodyGroove * 4f

                val kneeGroove = abs(bodyGroove) * 14f
                thighLPitch -= kneeGroove
                shinLPitch += kneeGroove * 2f
                footLPitch -= kneeGroove
                thighRPitch -= kneeGroove
                shinRPitch += kneeGroove * 2f
                footRPitch -= kneeGroove

                headPitch = -5f + sin(elapsedSec * 7.6f) * 4f
                headYaw = bodyGroove * 9f
                headRoll = -bodyGroove * 6f + winkWeight * 10f

                // Left arm waving high & joyfully!
                uaLRoll = 58f
                uaLYaw = -26f
                uaLPitch = -10f
                faLRoll = 64f + energyOsc * 22f
                faLYaw = -22f
                handLRoll = energyOsc * 32f
                handLPitch = cos(elapsedSec * 10.5f) * 12f

                // Right arm upbeat celebration pump!
                uaRRoll = -28f - abs(bodyGroove) * 16f
                uaRYaw = 26f
                uaRPitch = -6f
                faRRoll = -52f + sin(elapsedSec * 7.6f) * 16f
                faRYaw = 30f
                handRRoll = -sin(elapsedSec * 7.6f) * 20f

                earTargetLPitch = -14f
                earTargetRPitch = -14f
                earTargetLRoll = -12f
                earTargetRRoll = 12f

                targetSmile = 0.98f
                targetBrowUp = 0.85f
            }

            MascotExpression.FOCUS -> {
                // Confident Officer Hopps "Locked In" pose: HANDS ON HIPS, leaned slightly forward, determined brows!
                hipShiftX = swaySlow * 0.006f
                hipYaw = -6f
                spinePitch = 6f
                chestPitch += 4f
                chestYaw = 6f

                // Confident grounded athletic stance (one knee slightly forward)
                thighLPitch -= 7f
                shinLPitch += 12f
                footLPitch -= 5f
                thighRPitch -= 3f
                shinRPitch += 6f
                footRPitch -= 3f

                headPitch = -5f + breath * 1.5f
                headYaw = sin(elapsedSec * 0.9f) * 3.5f
                headRoll = -2f

                // Both hands planted confidently on her waist/hips!
                uaLRoll = -16f
                uaLYaw = -6f
                uaLPitch = 6f
                faLRoll = -62f
                faLYaw = -32f
                faLPitch = 8f
                handLRoll = 26f
                handLYaw = -14f

                uaRRoll = 16f
                uaRYaw = 6f
                uaRPitch = 6f
                faRRoll = 62f
                faRYaw = 32f
                faRPitch = 8f
                handRRoll = -26f
                handRYaw = 14f

                // Alert perked-forward bunny radar ears!
                earTargetLPitch = -16f
                earTargetRPitch = -16f
                earTargetLRoll = -2f
                earTargetRRoll = 2f

                targetSmile = 0.16f
                targetBrowFocus = 0.88f
            }

            MascotExpression.RELAX -> {
                // Cozy pajama chill: slow deep breathing, soft half-closed eyes, gently drooped ears, warm sway!
                val cozySway = sin(elapsedSec * 0.95f)
                hipShiftX = cozySway * 0.020f
                hipRoll = cozySway * 4.5f
                spinePitch = -3f + breath * 2f
                spineRoll = -cozySway * 3f
                chestPitch += -2f - breath * 2.5f

                // Soft relaxed knees
                thighLPitch -= 6f + max(0f, cozySway) * 4f
                shinLPitch += 12f + max(0f, cozySway) * 8f
                footLPitch -= 6f
                thighRPitch -= 6f + max(0f, -cozySway) * 4f
                shinRPitch += 12f + max(0f, -cozySway) * 8f
                footRPitch -= 6f

                headPitch = 5f + breath * 2.5f
                headYaw = cozySway * 7f
                headRoll = -8.5f + cozySway * 3.5f

                // Soft cozy arms resting gently down/forward
                uaLRoll = -44f + cozySway * 3f
                uaLYaw = -16f
                faLRoll = 18f
                faLYaw = -26f
                handLRoll = 10f

                uaRRoll = 44f + cozySway * 3f
                uaRYaw = 16f
                faRRoll = -18f
                faRYaw = 26f
                handRRoll = -10f

                // Softly relaxed drooping bunny ears (signature cozy bunny look!)
                earTargetLPitch = 20f + breath * 4f
                earTargetRPitch = 22f + breath * 4f
                earTargetLRoll = -16f + cozySway * 4f
                earTargetRRoll = 16f + cozySway * 4f

                targetSmile = 0.68f
                targetRelaxLids = 0.44f
            }

            MascotExpression.CURIOUS -> {
                // Adorable inquisitive thinking pose: RIGHT PAW AT CHIN, head tilted 15°, one ear folded sideways!
                hipShiftX = -0.016f + swaySlow * 0.008f
                hipRoll = -4f
                hipYaw = 7f
                spineRoll = 5f
                chestPitch += 3f
                chestRoll = 4f

                thighLPitch -= 8f
                shinLPitch += 14f
                footLPitch -= 6f
                thighRPitch -= 4f
                shinRPitch += 8f
                footRPitch -= 4f

                headPitch = -3f + sin(elapsedSec * 1.4f) * 3f
                headYaw = -6f + sin(elapsedSec * 1.1f) * 6f
                headRoll = 14.5f + sin(elapsedSec * 1.4f) * 3f + winkWeight * 5f

                // Left hand resting casually on hip
                uaLRoll = -20f
                uaLYaw = -8f
                faLRoll = -56f
                faLYaw = -28f
                handLRoll = 22f

                // Right arm lifted thoughtfully with paw resting right beneath her chin!
                uaRRoll = -12f
                uaRYaw = 48f
                uaRPitch = -14f
                faRRoll = -76f
                faRYaw = 54f
                faRPitch = -12f
                handRRoll = -18f + sin(elapsedSec * 2.8f) * 6f
                handRPitch = 14f

                // Classic cute asymmetric curious bunny ears: left perked up, right folded playfully sideways!
                earTargetLPitch = -14f
                earTargetLRoll = -4f
                earTargetRPitch = 18f + sin(elapsedSec * 2.1f) * 5f
                earTargetRRoll = 34f + sin(elapsedSec * 1.6f) * 5f

                targetSmile = 0.52f
                targetBrowUp = 0.78f
            }
        }

        // --- 4. OVERLAY TAP WAVE & JUMP CELEBRATION FLING ---
        if (isTapWaving && expression != MascotExpression.EXCITED && expression != MascotExpression.HAPPY) {
            val waveFade = when {
                tapElapsedSec < 0.18f -> tapElapsedSec / 0.18f
                tapElapsedSec > 1.45f -> ((1.85f - tapElapsedSec) / 0.40f).coerceIn(0f, 1f)
                else -> 1f
            }
            val waveOsc = sin(elapsedSec * 11.0f)
            uaLRoll = lerp(uaLRoll, 52f, waveFade)
            uaLYaw = lerp(uaLYaw, -25f, waveFade)
            uaLPitch = lerp(uaLPitch, -8f, waveFade)
            faLRoll = lerp(faLRoll, 60f + waveOsc * 20f, waveFade)
            faLYaw = lerp(faLYaw, -24f, waveFade)
            handLRoll = lerp(handLRoll, waveOsc * 30f, waveFade)
        }

        // Jump celebration arm fling (both arms rise dynamically when airborne!)
        if (airTuckFactor > 0.01f) {
            uaLRoll = lerp(uaLRoll, 64f, airTuckFactor * 0.85f)
            uaRRoll = lerp(uaRRoll, -64f, airTuckFactor * 0.85f)
            faLRoll = lerp(faLRoll, 26f, airTuckFactor * 0.65f)
            faRRoll = lerp(faRRoll, -26f, airTuckFactor * 0.65f)
            headPitch -= airTuckFactor * 10f
        }
        // Squat anticipation swings arms slightly back before takeoff
        if (squatFactor > 0.01f) {
            uaLYaw += squatFactor * 18f
            uaRYaw -= squatFactor * 18f
            spinePitch += squatFactor * 12f
        }

        // --- 5. OVERLAY EXPRESSIVE CONVERSATIONAL SPEAKING GESTURES ---
        if (smoothSpeak > 0.01f) {
            val speakBeatA = sin(elapsedSec * 5.8f)
            val speakBeatB = sin(elapsedSec * 5.8f + 2.1f)
            val emphasis = abs(sin(elapsedSec * 3.4f))

            // Natural conversational head nods & brow emphasis
            headPitch += sin(elapsedSec * 11.2f) * 5.8f * smoothSpeak
            headYaw += sin(elapsedSec * 4.4f) * 6.5f * smoothSpeak
            headRoll += sin(elapsedSec * 6.0f) * 4.2f * smoothSpeak
            chestPitch += sin(elapsedSec * 5.8f) * 2.6f * smoothSpeak
            chestYaw += sin(elapsedSec * 4.4f) * 3.8f * smoothSpeak

            // Expressive alternating hand gestures while explaining/talking!
            val talkArmL = max(0f, speakBeatA) * smoothSpeak
            val talkArmR = max(0f, speakBeatB) * smoothSpeak

            // Bring forearms up & forward gently to gesture as she talks
            if (expression != MascotExpression.EXCITED && !isTapWaving) {
                uaLRoll = lerp(uaLRoll, -22f + talkArmL * 24f, smoothSpeak * 0.72f)
                uaLYaw = lerp(uaLYaw, -26f - talkArmL * 14f, smoothSpeak * 0.72f)
                faLRoll = lerp(faLRoll, 34f + talkArmL * 22f, smoothSpeak * 0.75f)
                faLYaw = lerp(faLYaw, -32f, smoothSpeak * 0.75f)
                handLRoll += speakBeatA * 18f * smoothSpeak
                handLPitch += emphasis * 12f * smoothSpeak
            }
            if (expression != MascotExpression.CURIOUS) {
                uaRRoll = lerp(uaRRoll, 22f - talkArmR * 24f, smoothSpeak * 0.72f)
                uaRYaw = lerp(uaRYaw, 26f + talkArmR * 14f, smoothSpeak * 0.72f)
                faRRoll = lerp(faRRoll, -34f - talkArmR * 22f, smoothSpeak * 0.75f)
                faRYaw = lerp(faRYaw, 32f, smoothSpeak * 0.75f)
                handRRoll += speakBeatB * 18f * smoothSpeak
                handRPitch += emphasis * 12f * smoothSpeak
            }

            // Lively ear bobs synchronized to speech beats
            earTargetLPitch += sin(elapsedSec * 6.4f) * 6f * smoothSpeak
            earTargetRPitch += sin(elapsedSec * 6.4f + 0.55f) * 6f * smoothSpeak
            targetBrowUp = max(targetBrowUp, emphasis * 0.45f * smoothSpeak)
        }

        // --- 6. REAL SPRING-DAMPER SECONDARY PHYSICS FOR EARS & TORSO FOLLOW-THROUGH ---
        // Spin inertia makes her ears & torso trail naturally when user drags Judy!
        val spinLag = (-userYawVelocityDeg * 0.085f).coerceIn(-24f, 24f)
        val lagTorso = torsoLagYaw.step(spinLag * 0.35f, dt)
        val lagHead = headFollowYaw.step(spinLag * 0.65f, dt)

        // Vertical jump acceleration whips the bunny ears backward on takeoff and forward on landing!
        val earJumpImpulse = (jumpAccel * 1.45f - jumpVel * 14f).coerceIn(-38f, 38f)

        val earBaseLP = earBaseLPitchSpring.step(earTargetLPitch + earJumpImpulse * 0.65f, dt)
        val earBaseLR = earBaseLRollSpring.step(earTargetLRoll + spinLag * 0.45f, dt)
        val earTipLP = earTipLPitchSpring.step(earBaseLP * 0.58f + earJumpImpulse * 0.85f, dt)
        val earTipLR = earTipLRollSpring.step(earBaseLR * 0.55f + spinLag * 0.70f, dt)

        val earBaseRP = earBaseRPitchSpring.step(earTargetRPitch + earJumpImpulse * 0.65f, dt)
        val earBaseRR = earBaseRRollSpring.step(earTargetRRoll + spinLag * 0.45f, dt)
        val earTipRP = earTipRPitchSpring.step(earBaseRP * 0.58f + earJumpImpulse * 0.85f, dt)
        val earTipRR = earTipRRollSpring.step(earBaseRR * 0.55f + spinLag * 0.70f, dt)

        // --- 7. POPULATE ALL 21 JOINT TARGETS & SMOOTHLY INTERPOLATE ---
        targets[JudyBones.HIPS].set(pitch = hipPitch, yaw = hipYaw, roll = hipRoll, tx = hipShiftX, ty = hipDropY)
        targets[JudyBones.SPINE].set(pitch = spinePitch, yaw = spineYaw + lagTorso * 0.4f, roll = spineRoll)
        targets[JudyBones.CHEST].set(pitch = chestPitch, yaw = chestYaw + lagTorso * 0.6f, roll = chestRoll)
        targets[JudyBones.NECK].set(pitch = headPitch * 0.35f, yaw = (headYaw + lagHead) * 0.32f, roll = headRoll * 0.32f)
        targets[JudyBones.HEAD].set(pitch = headPitch * 0.68f, yaw = (headYaw + lagHead) * 0.68f, roll = headRoll * 0.68f)

        targets[JudyBones.EAR_BASE_L].set(pitch = earBaseLP.coerceIn(-40f, 48f), roll = earBaseLR.coerceIn(-45f, 35f))
        targets[JudyBones.EAR_TIP_L].set(pitch = earTipLP.coerceIn(-38f, 52f), roll = earTipLR.coerceIn(-40f, 35f))
        targets[JudyBones.EAR_BASE_R].set(pitch = earBaseRP.coerceIn(-40f, 48f), roll = earBaseRR.coerceIn(-35f, 45f))
        targets[JudyBones.EAR_TIP_R].set(pitch = earTipRP.coerceIn(-38f, 52f), roll = earTipRR.coerceIn(-35f, 40f))

        targets[JudyBones.UPPER_ARM_L].set(pitch = uaLPitch, yaw = uaLYaw, roll = uaLRoll)
        targets[JudyBones.FORE_ARM_L].set(pitch = faLPitch, yaw = faLYaw, roll = faLRoll)
        targets[JudyBones.HAND_L].set(pitch = handLPitch, yaw = handLYaw, roll = handLRoll)

        targets[JudyBones.UPPER_ARM_R].set(pitch = uaRPitch, yaw = uaRYaw, roll = uaRRoll)
        targets[JudyBones.FORE_ARM_R].set(pitch = faRPitch, yaw = faRYaw, roll = faRRoll)
        targets[JudyBones.HAND_R].set(pitch = handRPitch, yaw = handRYaw, roll = handRRoll)

        targets[JudyBones.THIGH_L].set(pitch = thighLPitch)
        targets[JudyBones.SHIN_L].set(pitch = shinLPitch)
        targets[JudyBones.FOOT_L].set(pitch = footLPitch)

        targets[JudyBones.THIGH_R].set(pitch = thighRPitch)
        targets[JudyBones.SHIN_R].set(pitch = shinRPitch)
        targets[JudyBones.FOOT_R].set(pitch = footRPitch)

        for (i in 0 until JudyBones.COUNT) {
            // Ears already run through real spring-damper physics; apply them directly for crisp physics!
            if (i in JudyBones.EAR_BASE_L..JudyBones.EAR_TIP_R) {
                poses[i].set(
                    pitch = targets[i].pitch,
                    yaw = targets[i].yaw,
                    roll = targets[i].roll
                )
            } else {
                poses[i].blendToward(targets[i], poseBlend)
            }
        }

        // --- 8. SMOOTHLY UPDATE ALL 6 FACIAL MORPH WEIGHTS ---
        smoothSmile = lerp(smoothSmile, targetSmile, facialBlend)
        smoothRelaxLids = lerp(smoothRelaxLids, targetRelaxLids, facialBlend)
        smoothBrowUp = lerp(smoothBrowUp, targetBrowUp, facialBlend)
        smoothBrowFocus = lerp(smoothBrowFocus, targetBrowFocus, facialBlend)

        morphWeights[JudyMorphs.BLINK_L] = max(max(naturalBlink, smoothRelaxLids), winkWeight * 0.96f).coerceIn(0f, 1f)
        morphWeights[JudyMorphs.BLINK_R] = max(naturalBlink, smoothRelaxLids).coerceIn(0f, 1f)
        morphWeights[JudyMorphs.MOUTH_OPEN] = mouthOpenWeight.coerceIn(0f, 1f)
        morphWeights[JudyMorphs.SMILE] = (smoothSmile + winkWeight * 0.22f).coerceIn(0f, 1f)
        morphWeights[JudyMorphs.BROW_UP] = smoothBrowUp.coerceIn(0f, 1f)
        morphWeights[JudyMorphs.BROW_FOCUS] = smoothBrowFocus.coerceIn(0f, 1f)

        // --- 9. ROOT TRANSFORM (GLOBAL ORBIT + JUMP ARC + ELASTIC SQUASH-STRETCH) ---
        val moodPop = if (moodChangeElapsed in 0f..0.38f) {
            sin((moodChangeElapsed / 0.38f) * PI.toFloat()) * 0.045f
        } else 0f

        rootOffsetY = jumpHeight + moodPop
        rootYawDeg = userYawDeg + jumpSpinDeg
        rootPitchDeg = userPitchDeg
        rootRollDeg = 0f

        val breathScale = if (isBreathing) breath * 0.008f else 0f
        rootScaleX = 1f + breathScale - squashVal * 0.55f
        rootScaleY = 1f + breathScale + squashVal
        rootScaleZ = 1f + breathScale - squashVal * 0.55f
    }

    /**
     * Composes a bone's rest local matrix with its animated local offset + YXZ Euler rotation.
     */
    fun composeBoneMatrix(restLocalMatrix: FloatArray, boneIndex: Int): FloatArray {
        val p = poses[boneIndex]
        Matrix.setIdentityM(scratchRot, 0)
        if (p.tx != 0f || p.ty != 0f || p.tz != 0f) {
            Matrix.translateM(scratchRot, 0, p.tx, p.ty, p.tz)
        }
        if (p.yaw != 0f) Matrix.rotateM(scratchRot, 0, p.yaw, 0f, 1f, 0f)
        if (p.pitch != 0f) Matrix.rotateM(scratchRot, 0, p.pitch, 1f, 0f, 0f)
        if (p.roll != 0f) Matrix.rotateM(scratchRot, 0, p.roll, 0f, 0f, 1f)
        Matrix.multiplyMM(scratchOut, 0, restLocalMatrix, 0, scratchRot, 0)
        return scratchOut
    }

    private fun lerp(a: Float, b: Float, t: Float): Float = a + (b - a) * t
}
