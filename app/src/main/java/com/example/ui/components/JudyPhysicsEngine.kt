package com.example.ui.components

import android.opengl.Matrix
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Exact 30-bone skeleton layout baked into assets/models/judy.glb:
 *  - Bones 0..20: Core articulated body, ears, arms, wrists, legs, and feet
 *  - Bones 21..25: Dedicated Phase 2 secondary soft-body bones (synchronized across outfit + body)
 *  - Bones 26..29: Dedicated Finger & Thumb bones for natural Pixar hand & wrist articulation
 */
internal object JudyBones {
    const val COUNT = 30

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

    // Dedicated Phase 2 secondary soft-body bones (independent of Spine/Arms, synced on outfit + body)
    const val SOFT_CHEST_L = 21
    const val SOFT_CHEST_R = 22
    const val SOFT_HIP_L = 23
    const val SOFT_HIP_R = 24
    const val TAIL = 25

    // Dedicated Finger & Thumb articulation bones (children of Hand_L / Hand_R)
    const val FINGERS_L = 26
    const val THUMB_L = 27
    const val FINGERS_R = 28
    const val THUMB_R = 29

    val NAMES: Array<String> = arrayOf(
        "Hips", "Spine", "Chest", "Neck", "Head",
        "EarBase_L", "EarTip_L", "EarBase_R", "EarTip_R",
        "UpperArm_L", "ForeArm_L", "Hand_L",
        "UpperArm_R", "ForeArm_R", "Hand_R",
        "Thigh_L", "Shin_L", "Foot_L",
        "Thigh_R", "Shin_R", "Foot_R",
        "SoftChest_L", "SoftChest_R", "SoftHip_L", "SoftHip_R", "Tail",
        "Fingers_L", "Thumb_L", "Fingers_R", "Thumb_R"
    )

    // Exact leg geometry exported from the 30-bone GLB rig for analytical 2-bone foot-locking IK
    const val HIP_REST_Y = 0.430f
    const val THIGH_REST_X = 0.066f
    const val THIGH_REST_Y = 0.365f
    const val THIGH_REST_Z = -0.008f
    const val ANKLE_REST_Y = 0.065f
    const val ANKLE_REST_Z = -0.010f
    const val L1_THIGH = 0.1508642f
    const val L2_SHIN = 0.1510894f
    const val MAX_LEG_REACH = 0.995f * (L1_THIGH + L2_SHIN)
    const val MIN_LEG_REACH = 0.18f
}

/**
 * Exact 8 facial morph targets baked on meshes 2..9 in assets/models/judy.glb.
 */
internal object JudyMorphs {
    const val COUNT = 8

    const val BLINK_L = 0
    const val BLINK_R = 1
    const val MOUTH_OPEN = 2
    const val SMILE = 3
    const val BROW_UP = 4
    const val BROW_FOCUS = 5
    const val MOUTH_WIDE = 6
    const val MOUTH_ROUND = 7

    val NAMES: Array<String> = arrayOf(
        "blink_l", "blink_r", "mouth_open", "smile",
        "brow_up", "brow_focus", "mouth_wide", "mouth_round"
    )
}

/**
 * Varied, non-repeating playful & mood-aware tap reactions so tapping Judy never repeats
 * a single stiff pose.
 */
internal enum class TapReactionType(val durationSec: Float) {
    CRISP_HOP_SETTLE(0.74f),
    CHEEKY_WINK_TILT(0.95f),
    OFFICER_SALUTE(1.05f),
    DOUBLE_EAR_FLICK_PEEK(0.86f),
    COZY_STRETCH_SIGH(1.05f)
}

/**
 * Critically/under-damped 1D spring oscillator with strict dt/NaN/amplitude guards so
 * springs can never diverge on frame drops or resume from LocalMascotRenderMode.PAUSED.
 */
private class Spring1D(
    var value: Float = 0f,
    private val stiffness: Float,
    private val damping: Float,
    private val maxAbsValue: Float = 60f,
    private val maxAbsVelocity: Float = 420f
) {
    var velocity: Float = 0f

    fun step(target: Float, safeDt: Float): Float {
        if (!target.isFinite() || !value.isFinite() || !velocity.isFinite()) {
            value = 0f
            velocity = 0f
            return 0f
        }
        val force = (target - value) * stiffness - velocity * damping
        velocity = (velocity + force * safeDt).coerceIn(-maxAbsVelocity, maxAbsVelocity)
        value = (value + velocity * safeDt).coerceIn(-maxAbsValue, maxAbsValue)
        return value
    }

    fun addImpulse(dv: Float) {
        if (dv.isFinite()) {
            velocity = (velocity + dv).coerceIn(-maxAbsVelocity, maxAbsVelocity)
        }
    }
}

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
        val a = if (alpha.isFinite()) alpha.coerceIn(0f, 1f) else 0f
        pitch = sanitize(pitch + (target.pitch - pitch) * a)
        yaw = sanitize(yaw + (target.yaw - yaw) * a)
        roll = sanitize(roll + (target.roll - roll) * a)
        tx = sanitize(tx + (target.tx - tx) * a)
        ty = sanitize(ty + (target.ty - ty) * a)
        tz = sanitize(tz + (target.tz - tz) * a)
    }

    fun sanitizeAll() {
        pitch = sanitize(pitch)
        yaw = sanitize(yaw)
        roll = sanitize(roll)
        tx = sanitize(tx)
        ty = sanitize(ty)
        tz = sanitize(tz)
    }

    private fun sanitize(v: Float): Float = if (v.isFinite()) v else 0f
}

private data class LegIkSolution(
    var thighPitchDeg: Float = 0f,
    var thighYawDeg: Float = 0f,
    var thighRollDeg: Float = 0f,
    var shinPitchDeg: Float = 0f,
    var footPitchDeg: Float = 0f,
    var footRollDeg: Float = 0f
)

/**
 * Living Pixar-style procedural skeletal, facial & soft-body physics engine for 3D Judy:
 *  - Asymmetric contrapposto stance (weight-shifted hips, ~10° Thigh-yaw knee-over-toe turn-out,
 *    counter-tilted shoulders, dropped relaxed arms with soft inward wrist curl)
 *  - Varied mood-aware TapReactionDirector (snappy hop-settle, cheeky wink-tilt, Officer Hopps
 *    salute, double ear-flick peek, cozy stretch-sigh — zero repetitive wide-arm poses)
 *  - Living micro-acting: head-leads-torso gaze tracking, 3–5s random asymmetric ear twitches,
 *    and occasional speech-bubble downward glances when text appears
 *  - Calibrated multi-viseme lip-sync (0.35–0.65 conversational mouth_open, 0.85–1.0 excited peaks)
 *  - Expressive Phase 2 soft-body bounce (SoftChest_L/R, SoftHip_L/R, Tail) synced with outfit+body
 */
internal class JudyPhysicsEngine(
    var softBodyIntensity: Float = 1.0f
) {
    val poses = Array(JudyBones.COUNT) { BonePose() }
    private val targets = Array(JudyBones.COUNT) { BonePose() }
    val morphWeights = FloatArray(JudyMorphs.COUNT)

    // Root transform outputs (horizontal-only 360° turntable keeps feet flat on floor plane)
    var rootYawDeg: Float = 0f
    var rootOffsetY: Float = 0.009f
    var rootScaleX: Float = 1f
    var rootScaleY: Float = 1f
    var rootScaleZ: Float = 1f
    var groundedHipShiftX: Float = 0f

    // Critically-damped bunny ear springs (lively flicks + natural follow-through)
    private val earBaseLPitch = Spring1D(0f, stiffness = 150f, damping = 16.5f, maxAbsValue = 18f)
    private val earBaseLRoll = Spring1D(0f, stiffness = 138f, damping = 16f, maxAbsValue = 16f)
    private val earTipLPitch = Spring1D(0f, stiffness = 115f, damping = 14f, maxAbsValue = 15f)
    private val earTipLRoll = Spring1D(0f, stiffness = 108f, damping = 13.5f, maxAbsValue = 12f)

    private val earBaseRPitch = Spring1D(0f, stiffness = 150f, damping = 16.5f, maxAbsValue = 18f)
    private val earBaseRRoll = Spring1D(0f, stiffness = 138f, damping = 16f, maxAbsValue = 16f)
    private val earTipRPitch = Spring1D(0f, stiffness = 115f, damping = 14f, maxAbsValue = 15f)
    private val earTipRRoll = Spring1D(0f, stiffness = 108f, damping = 13.5f, maxAbsValue = 12f)

    // Spring-driven wrist follow-through & organic finger/thumb curl (Pixar overlapping action)
    private val handLPitchSpring = Spring1D(-8f, stiffness = 155f, damping = 17f, maxAbsValue = 38f)
    private val handLRollSpring = Spring1D(-10f, stiffness = 145f, damping = 16f, maxAbsValue = 34f)
    private val handLYawSpring = Spring1D(-6f, stiffness = 145f, damping = 17f, maxAbsValue = 28f)
    private val fingersLCurlSpring = Spring1D(26f, stiffness = 135f, damping = 16f, maxAbsValue = 44f)
    private val thumbLCurlSpring = Spring1D(16f, stiffness = 140f, damping = 17f, maxAbsValue = 34f)

    private val handRPitchSpring = Spring1D(-8f, stiffness = 155f, damping = 17f, maxAbsValue = 38f)
    private val handRRollSpring = Spring1D(10f, stiffness = 145f, damping = 16f, maxAbsValue = 34f)
    private val handRYawSpring = Spring1D(6f, stiffness = 145f, damping = 17f, maxAbsValue = 28f)
    private val fingersRCurlSpring = Spring1D(26f, stiffness = 135f, damping = 16f, maxAbsValue = 44f)
    private val thumbRCurlSpring = Spring1D(16f, stiffness = 140f, damping = 17f, maxAbsValue = 34f)

    // Head-leads-torso gaze springs (head tracks briskly while chest/spine follow ~0.28s behind!)
    private val headGazeYawSpring = Spring1D(0f, stiffness = 74f, damping = 13.5f, maxAbsValue = 14f)
    private val headGazePitchSpring = Spring1D(0f, stiffness = 68f, damping = 13.0f, maxAbsValue = 12f)
    private val torsoFollowYawSpring = Spring1D(0f, stiffness = 24f, damping = 8.8f, maxAbsValue = 9f)
    private val torsoFollowRollSpring = Spring1D(0f, stiffness = 28f, damping = 9.5f, maxAbsValue = 6f)

    // Secondary springs for elastic squash/stretch & inertial turntable lag
    private val squashSpring = Spring1D(0f, stiffness = 185f, damping = 19f, maxAbsValue = 0.055f, maxAbsVelocity = 3f)
    private val headLagYaw = Spring1D(0f, stiffness = 95f, damping = 15f, maxAbsValue = 14f)
    private val torsoLagYaw = Spring1D(0f, stiffness = 85f, damping = 14f, maxAbsValue = 8f)

    // Phase 2 expressive lively 2D soft-body springs (SoftChest_L/R, SoftHip_L/R, Tail)
    // Slightly underdamped (damping ~8.8..9.6) so chest & hips show genuine weighted bounce & settle!
    private val softChestLY = Spring1D(0f, stiffness = 158f, damping = 8.9f, maxAbsValue = 0.020f, maxAbsVelocity = 0.92f)
    private val softChestRY = Spring1D(0f, stiffness = 150f, damping = 8.7f, maxAbsValue = 0.020f, maxAbsVelocity = 0.92f)
    private val softChestLX = Spring1D(0f, stiffness = 136f, damping = 9.6f, maxAbsValue = 0.011f, maxAbsVelocity = 0.56f)
    private val softChestRX = Spring1D(0f, stiffness = 132f, damping = 9.4f, maxAbsValue = 0.011f, maxAbsVelocity = 0.56f)

    private val softHipLY = Spring1D(0f, stiffness = 142f, damping = 9.1f, maxAbsValue = 0.017f, maxAbsVelocity = 0.78f)
    private val softHipRY = Spring1D(0f, stiffness = 138f, damping = 9.0f, maxAbsValue = 0.017f, maxAbsVelocity = 0.78f)
    private val softHipLX = Spring1D(0f, stiffness = 128f, damping = 9.5f, maxAbsValue = 0.012f, maxAbsVelocity = 0.56f)
    private val softHipRX = Spring1D(0f, stiffness = 128f, damping = 9.5f, maxAbsValue = 0.012f, maxAbsVelocity = 0.56f)

    private val tailPitchSpring = Spring1D(0f, stiffness = 148f, damping = 10.5f, maxAbsValue = 14f, maxAbsVelocity = 240f)
    private val tailYawSpring = Spring1D(0f, stiffness = 128f, damping = 9.8f, maxAbsValue = 16f, maxAbsVelocity = 250f)

    // Tap Reaction Director state
    private var activeTapReaction: TapReactionType = TapReactionType.CRISP_HOP_SETTLE
    private var tapCounter: Int = 0
    private var lastTapJumpLandFired: Boolean = false

    // Living micro-acting timers (random 3-5s asymmetric ear twitch + speech-bubble glances)
    private var nextEarTwitchTimerSec: Float = 2.6f
    private var earTwitchSeed: Int = 17
    private var wasSpeaking: Boolean = false
    private var speechGlanceTimerSec: Float = 99f
    private var nextSpeechGlanceCooldownSec: Float = 0f
    private var prevSyllablePeak: Float = 0f

    // Smoothed facial & blend states
    private var smoothSpeak = 0f
    private var smoothSmile = 0.38f
    private var smoothRelaxLids = 0f
    private var smoothBrowUp = 0f
    private var smoothBrowFocus = 0f
    private var smoothMouthOpen = 0f
    private var smoothMouthWide = 0f
    private var smoothMouthRound = 0f
    private var transitionBlinkTimer = 99f
    private var prevExpression: MascotExpression? = null
    private var prevJumpHeight = 0f
    private var prevJumpVel = 0f
    private var prevHipDropY = 0f
    private var prevHipDropVel = 0f
    private var prevHipShiftX = 0f
    private var prevHipShiftVel = 0f
    private var prevSmoothedFaLRoll = 0f
    private var prevSmoothedFaRRoll = 0f
    private var initializedPose = false

    // Scratch buffers for zero-GC per-frame updates
    private val scratchRot = FloatArray(16)
    private val scratchOut = FloatArray(16)
    private val legIkL = LegIkSolution()
    private val legIkR = LegIkSolution()

    /**
     * Picks a varied, non-repeating playful or mood-aware tap reaction and fires tailored
     * ear, wrist, squash, and soft-body chest/hip impulses.
     */
    fun triggerTapReaction(expression: MascotExpression) {
        tapCounter++
        lastTapJumpLandFired = false

        val pool = when (expression) {
            MascotExpression.FOCUS -> listOf(
                TapReactionType.OFFICER_SALUTE,
                TapReactionType.DOUBLE_EAR_FLICK_PEEK,
                TapReactionType.CRISP_HOP_SETTLE,
                TapReactionType.CHEEKY_WINK_TILT
            )
            MascotExpression.RELAX -> listOf(
                TapReactionType.COZY_STRETCH_SIGH,
                TapReactionType.CHEEKY_WINK_TILT,
                TapReactionType.DOUBLE_EAR_FLICK_PEEK,
                TapReactionType.CRISP_HOP_SETTLE
            )
            MascotExpression.CURIOUS -> listOf(
                TapReactionType.DOUBLE_EAR_FLICK_PEEK,
                TapReactionType.CHEEKY_WINK_TILT,
                TapReactionType.OFFICER_SALUTE,
                TapReactionType.CRISP_HOP_SETTLE
            )
            MascotExpression.EXCITED -> listOf(
                TapReactionType.CRISP_HOP_SETTLE,
                TapReactionType.CHEEKY_WINK_TILT,
                TapReactionType.OFFICER_SALUTE,
                TapReactionType.DOUBLE_EAR_FLICK_PEEK
            )
            MascotExpression.HAPPY -> listOf(
                TapReactionType.CHEEKY_WINK_TILT,
                TapReactionType.CRISP_HOP_SETTLE,
                TapReactionType.DOUBLE_EAR_FLICK_PEEK,
                TapReactionType.OFFICER_SALUTE
            )
            MascotExpression.IDLE -> listOf(
                TapReactionType.CHEEKY_WINK_TILT,
                TapReactionType.CRISP_HOP_SETTLE,
                TapReactionType.OFFICER_SALUTE,
                TapReactionType.DOUBLE_EAR_FLICK_PEEK
            )
        }

        // Pseudo-random non-repeating selection weighted by tap count & mood
        val hash = ((tapCounter * 31 + expression.ordinal * 7 + earTwitchSeed * 13) and 0x7FFFFFFF)
        var picked = pool[hash % pool.size]
        if (picked == activeTapReaction && pool.size > 1) {
            picked = pool[(hash + 1) % pool.size]
        }
        activeTapReaction = picked
        earTwitchSeed = (earTwitchSeed * 1103515245 + 12345) and 0x7FFFFFFF

        val sb = softBodyIntensity.coerceIn(0f, 1f)
        when (picked) {
            TapReactionType.CRISP_HOP_SETTLE -> {
                earBaseLPitch.addImpulse(-72f)
                earBaseRPitch.addImpulse(-66f)
                earTipLPitch.addImpulse(-90f)
                earTipRPitch.addImpulse(-82f)
                squashSpring.addImpulse(-0.34f)
                if (sb > 0.001f) {
                    softChestLY.addImpulse(-0.30f * sb)
                    softChestRY.addImpulse(-0.28f * sb)
                    softHipLY.addImpulse(-0.24f * sb)
                    softHipRY.addImpulse(-0.24f * sb)
                    tailPitchSpring.addImpulse(-135f * sb)
                    tailYawSpring.addImpulse(150f * sb)
                }
            }
            TapReactionType.CHEEKY_WINK_TILT -> {
                earBaseLPitch.addImpulse(-55f)
                earBaseRPitch.addImpulse(35f)
                earTipLPitch.addImpulse(-75f)
                handLRollSpring.addImpulse(85f)
                fingersLCurlSpring.addImpulse(-90f)
                if (sb > 0.001f) {
                    softChestLY.addImpulse(0.22f * sb)
                    softChestRY.addImpulse(0.18f * sb)
                    softChestLX.addImpulse(0.14f * sb)
                    softHipLX.addImpulse(-0.18f * sb)
                    softHipRX.addImpulse(-0.18f * sb)
                    tailYawSpring.addImpulse(165f * sb)
                }
            }
            TapReactionType.OFFICER_SALUTE -> {
                earBaseLPitch.addImpulse(-85f)
                earBaseRPitch.addImpulse(-85f)
                earTipLPitch.addImpulse(-65f)
                earTipRPitch.addImpulse(-65f)
                handRPitchSpring.addImpulse(-60f)
                if (sb > 0.001f) {
                    softChestLY.addImpulse(0.24f * sb)
                    softChestRY.addImpulse(0.26f * sb)
                    softHipLY.addImpulse(0.14f * sb)
                    softHipRY.addImpulse(0.14f * sb)
                    tailPitchSpring.addImpulse(-95f * sb)
                }
            }
            TapReactionType.DOUBLE_EAR_FLICK_PEEK -> {
                earBaseLPitch.addImpulse(-115f)
                earTipLPitch.addImpulse(-140f)
                earBaseRRoll.addImpulse(95f)
                earTipRRoll.addImpulse(110f)
                if (sb > 0.001f) {
                    softChestLY.addImpulse(0.20f * sb)
                    softChestRY.addImpulse(-0.18f * sb)
                    softHipLX.addImpulse(0.15f * sb)
                    softHipRX.addImpulse(0.15f * sb)
                    tailYawSpring.addImpulse(-155f * sb)
                }
            }
            TapReactionType.COZY_STRETCH_SIGH -> {
                earBaseLPitch.addImpulse(45f)
                earBaseRPitch.addImpulse(48f)
                squashSpring.addImpulse(0.18f)
                if (sb > 0.001f) {
                    softChestLY.addImpulse(0.22f * sb)
                    softChestRY.addImpulse(0.22f * sb)
                    softHipLY.addImpulse(-0.14f * sb)
                    softHipRY.addImpulse(-0.14f * sb)
                    tailPitchSpring.addImpulse(75f * sb)
                }
            }
        }
    }

    fun step(
        dt: Float,
        elapsedSec: Float,
        expression: MascotExpression,
        isBreathing: Boolean,
        isSpeaking: Boolean,
        tapElapsedSec: Float,
        userYawDeg: Float,
        userYawVelocityDeg: Float
    ) {
        // Strict dt & input sanitization so NaN or resume spikes can never destabilize springs
        val safeDt = if (!dt.isFinite() || dt <= 0f) 0.016f else dt.coerceIn(0.001f, 0.033f)
        val safeTime = if (elapsedSec.isFinite() && elapsedSec >= 0f) elapsedSec else 0f
        val safeTapElapsed = if (tapElapsedSec.isFinite() && tapElapsedSec >= 0f) tapElapsedSec else 99f
        val safeUserYaw = if (userYawDeg.isFinite()) userYawDeg else 0f
        val safeUserYawVel = if (userYawVelocityDeg.isFinite()) userYawVelocityDeg.coerceIn(-450f, 450f) else 0f

        val sb = softBodyIntensity.coerceIn(0f, 1f)

        if (prevExpression != expression) {
            if (prevExpression != null) {
                earBaseLPitch.addImpulse(-36f)
                earBaseRPitch.addImpulse(-28f)
                earTipLPitch.addImpulse(-46f)
                earTipRPitch.addImpulse(-38f)
                handLPitchSpring.addImpulse(34f)
                handRPitchSpring.addImpulse(34f)
                squashSpring.addImpulse(0.15f)
                if (sb > 0.001f) {
                    softChestLY.addImpulse(0.14f * sb)
                    softChestRY.addImpulse(0.13f * sb)
                    softHipLY.addImpulse(0.10f * sb)
                    softHipRY.addImpulse(0.10f * sb)
                    tailYawSpring.addImpulse(85f * sb)
                }
                // Classic Pixar realism: subtle 0.16s transition blink on emotional shift
                transitionBlinkTimer = 0f
            }
            prevExpression = expression
        }
        if (transitionBlinkTimer < 1f) {
            transitionBlinkTimer += safeDt
        }

        // --- LIVING MICRO-ACTING 1: Random 3–5s Crisp Asymmetric Ear Twitch ---
        nextEarTwitchTimerSec -= safeDt
        if (nextEarTwitchTimerSec <= 0f) {
            earTwitchSeed = (earTwitchSeed * 1103515245 + 12345) and 0x7FFFFFFF
            val rand01 = ((earTwitchSeed ushr 16) and 0x7FFF) / 32767f
            nextEarTwitchTimerSec = 3.0f + rand01 * 2.0f // 3.0s..5.0s natural interval
            val twitchLeft = (earTwitchSeed and 1) == 0
            val strength = 0.85f + (((earTwitchSeed ushr 8) and 0xFF) / 255f) * 0.45f
            if (twitchLeft) {
                earBaseLPitch.addImpulse(-105f * strength)
                earBaseLRoll.addImpulse(-65f * strength)
                earTipLPitch.addImpulse(-135f * strength)
            } else {
                earBaseRPitch.addImpulse(-105f * strength)
                earBaseRRoll.addImpulse(65f * strength)
                earTipRPitch.addImpulse(-135f * strength)
            }
        }

        // --- LIVING MICRO-ACTING 2: Occasional Speech-Bubble Glance Down -> Back Up ---
        if (nextSpeechGlanceCooldownSec > 0f) {
            nextSpeechGlanceCooldownSec -= safeDt
        }
        if (isSpeaking && !wasSpeaking && nextSpeechGlanceCooldownSec <= 0f) {
            speechGlanceTimerSec = 0f
            nextSpeechGlanceCooldownSec = 4.2f
        } else if (isSpeaking && speechGlanceTimerSec > 1.5f && nextSpeechGlanceCooldownSec <= 0f) {
            speechGlanceTimerSec = 0f
            nextSpeechGlanceCooldownSec = 5.0f
        }
        wasSpeaking = isSpeaking
        if (speechGlanceTimerSec < 2.0f) {
            speechGlanceTimerSec += safeDt
        }
        val speechBubbleGlanceDown = if (speechGlanceTimerSec in 0.14f..1.04f) {
            // Smooth glance down toward the speech bubble beneath her, then back up to the user
            sin(((speechGlanceTimerSec - 0.14f) / 0.90f) * PI.toFloat())
        } else 0f

        val poseBlend = if (!initializedPose) 1f else (1f - exp(-8.2f * safeDt))
        val facialBlend = if (!initializedPose) 1f else (1f - exp(-10.5f * safeDt))
        val lipBlend = if (!initializedPose) 1f else (1f - exp(-22.0f * safeDt))
        initializedPose = true

        smoothSpeak = lerp(smoothSpeak, if (isSpeaking) 1f else 0f, facialBlend)
        for (i in 0 until JudyBones.COUNT) targets[i].set()

        // --- 1. TAP REACTION TIMELINE & GROUNDED HOP CURVES ---
        val tapDur = activeTapReaction.durationSec
        val isTapActive = safeTapElapsed in 0f..tapDur
        val isTapHop = isTapActive && activeTapReaction == TapReactionType.CRISP_HOP_SETTLE

        val autoHopPeriod = 4.6f
        val autoHopPhase = safeTime % autoHopPeriod
        val isAutoHop = (expression == MascotExpression.EXCITED && !isTapActive && autoHopPhase in 0.9f..1.54f)

        var jumpHeight = 0f
        var squatDepth = 0f
        var airTuckFactor = 0f
        var targetSquash = 0f
        var jumpSpinDeg = 0f

        if (isTapHop) {
            when {
                // Snappy 0.11s anticipation dip
                safeTapElapsed < 0.11f -> {
                    val p = safeTapElapsed / 0.11f
                    val ease = sin(p * (PI.toFloat() * 0.5f))
                    squatDepth = 0.026f * ease
                    targetSquash = -0.028f * ease
                }
                // Crisp 0.34s hop (0.078m peak fits comfortably inside zoomed closer framing!)
                safeTapElapsed < 0.45f -> {
                    val p = (safeTapElapsed - 0.11f) / 0.34f
                    val arc = sin(p * PI.toFloat())
                    jumpHeight = arc * 0.078f
                    airTuckFactor = arc
                    jumpSpinDeg = sin(p * PI.toFloat()) * 5.5f
                    targetSquash = (1f - abs(p - 0.5f) * 2f) * 0.024f
                }
                // Weighted 0.29s landing cushion & soft-body bounce settle
                else -> {
                    if (!lastTapJumpLandFired) {
                        lastTapJumpLandFired = true
                        if (sb > 0.001f) {
                            softChestLY.addImpulse(-0.34f * sb)
                            softChestRY.addImpulse(-0.32f * sb)
                            softHipLY.addImpulse(-0.28f * sb)
                            softHipRY.addImpulse(-0.28f * sb)
                            tailPitchSpring.addImpulse(115f * sb)
                        }
                    }
                    val p = ((safeTapElapsed - 0.45f) / 0.29f).coerceIn(0f, 1f)
                    val absorb = sin(p * PI.toFloat()) * exp(-1.6f * p)
                    squatDepth = 0.022f * absorb
                    targetSquash = -0.024f * absorb
                }
            }
        } else if (isAutoHop) {
            val t = autoHopPhase - 0.9f // 0..0.64s
            when {
                t < 0.11f -> {
                    val ease = sin((t / 0.11f) * (PI.toFloat() * 0.5f))
                    squatDepth = 0.017f * ease
                    targetSquash = -0.018f * ease
                }
                t < 0.45f -> {
                    val p = (t - 0.11f) / 0.34f
                    val arc = sin(p * PI.toFloat())
                    jumpHeight = arc * 0.064f
                    airTuckFactor = arc * 0.65f
                    targetSquash = (1f - abs(p - 0.5f) * 2f) * 0.019f
                }
                else -> {
                    val p = ((t - 0.45f) / 0.19f).coerceIn(0f, 1f)
                    val absorb = sin(p * PI.toFloat())
                    squatDepth = 0.014f * absorb
                    targetSquash = -0.016f * absorb
                }
            }
        }

        val jumpVel = ((jumpHeight - prevJumpHeight) / safeDt).coerceIn(-4f, 4f)
        val jumpAccel = ((jumpVel - prevJumpVel) / safeDt).coerceIn(-20f, 20f)
        prevJumpHeight = jumpHeight
        prevJumpVel = jumpVel

        val squashVal = squashSpring.step(targetSquash, safeDt)
        val breathRate = if (expression == MascotExpression.RELAX) 1.24f else 1.82f
        val breathDepthMod = 0.86f + 0.14f * sin(safeTime * 0.37f + 0.9f)
        val breath = if (isBreathing) sin(safeTime * breathRate) * breathDepthMod else 0f
        val breathLag = if (isBreathing) sin(safeTime * breathRate - 0.58f) * breathDepthMod else 0f

        // --- 2. FACIAL TIMERS (BLINK, WINK, CALIBRATED CONVERSATIONAL LIP-SYNC) ---
        val tapWink = if (isTapActive && (activeTapReaction == TapReactionType.CHEEKY_WINK_TILT || activeTapReaction == TapReactionType.CRISP_HOP_SETTLE)) {
            if (safeTapElapsed in 0.08f..0.64f) {
                sin(((safeTapElapsed - 0.08f) / 0.56f) * PI.toFloat())
            } else 0f
        } else 0f

        val winkCycle = safeTime % 6.2f
        val moodWink = if (
            (expression == MascotExpression.HAPPY || expression == MascotExpression.CURIOUS) &&
            !isTapActive && winkCycle in 2.4f..2.94f
        ) {
            sin(((winkCycle - 2.4f) / 0.54f) * PI.toFloat())
        } else 0f
        val winkWeight = max(tapWink, moodWink).coerceIn(0f, 1f)

        val transitionBlink = if (transitionBlinkTimer in 0f..0.16f) {
            sin((transitionBlinkTimer / 0.16f) * PI.toFloat()) * 0.96f
        } else 0f

        val blinkCycle = safeTime % 3.85f
        val periodicBlink = when {
            blinkCycle in 3.42f..3.58f -> sin(((blinkCycle - 3.42f) / 0.16f) * PI.toFloat())
            blinkCycle in 3.64f..3.78f && (safeTime.toInt() % 3 == 0) ->
                sin(((blinkCycle - 3.64f) / 0.14f) * PI.toFloat())
            else -> 0f
        }
        val naturalBlink = max(periodicBlink, transitionBlink).coerceIn(0f, 1f)

        // Calibrated conversational speech cadence:
        // GLB mouth_open max is ~33.8mm (+8.2mm upper lip). Keep normal speech in 0.35–0.65 (~14–26mm)
        // and reserve 0.85–0.98 for excited/laugh/hop bursts so she speaks naturally without yelling!
        val wordBurst = (0.58f + 0.32f * sin(safeTime * 2.75f) + 0.16f * cos(safeTime * 1.23f + 0.6f)).coerceIn(0f, 1f)
        val consonantSignal = sin(safeTime * 10.6f + 0.80f * sin(safeTime * 3.3f))
        val consonantClosure = lerp(0.14f, 1.0f, smoothstep(-0.75f, -0.30f, consonantSignal))
        val rawSyllable = (abs(sin(safeTime * 9.0f)) * 0.54f +
            abs(sin(safeTime * 14.4f + 0.7f)) * 0.28f +
            abs(sin(safeTime * 5.8f + 1.9f)) * 0.18f)
        val syllableWave = (rawSyllable * wordBurst * consonantClosure).coerceIn(0f, 1f)

        // Fire crisp micro-impulse into chest soft-body on strong syllable onsets for lively talk bounce!
        if (smoothSpeak > 0.25f && sb > 0.001f && syllableWave > 0.58f && prevSyllablePeak <= 0.58f) {
            softChestLY.addImpulse(0.135f * sb * smoothSpeak)
            softChestRY.addImpulse(0.125f * sb * smoothSpeak)
        }
        prevSyllablePeak = syllableWave

        val vowelSelector = sin(safeTime * 4.2f + 0.55f * cos(safeTime * 1.95f))
        val excitedLaughPeak = if (expression == MascotExpression.EXCITED || isTapHop) {
            (0.28f * smoothstep(0.60f, 0.95f, syllableWave) + (if (isTapHop) airTuckFactor * 0.36f else 0f))
        } else 0f
        val conversationalOpen = (0.12f * wordBurst + syllableWave * 0.54f) * smoothSpeak
        val targetMouthOpen = (conversationalOpen + excitedLaughPeak).coerceIn(0f, 0.96f)
        val targetMouthWide = (smoothstep(0.02f, 0.80f, vowelSelector) * (0.34f + 0.66f * syllableWave) * 0.82f * smoothSpeak).coerceIn(0f, 0.88f)
        val targetMouthRound = (smoothstep(0.02f, 0.80f, -vowelSelector) * (0.32f + 0.68f * syllableWave) * 0.84f * smoothSpeak).coerceIn(0f, 0.90f)

        // --- 3. LIVING GAZE (HEAD LEADS FIRST, TORSO FOLLOWS BEHIND) ---
        val gazeTargetYaw = (sin(safeTime * 0.43f) * 7.2f + cos(safeTime * 0.23f + 1.1f) * 4.4f) *
            (if (expression == MascotExpression.FOCUS) 0.42f else 1.0f)
        val gazeTargetPitch = (sin(safeTime * 0.61f + 0.5f) * 2.6f) + speechBubbleGlanceDown * 9.5f
        val leadHeadYaw = headGazeYawSpring.step(gazeTargetYaw, safeDt)
        val leadHeadPitch = headGazePitchSpring.step(gazeTargetPitch, safeDt)
        // Torso tracks head gaze with slow spring (~0.28s follow-through lag so head ALWAYS leads!)
        val followTorsoYaw = torsoFollowYawSpring.step(leadHeadYaw * 0.46f, safeDt)
        val followTorsoRoll = torsoFollowRollSpring.step(-leadHeadYaw * 0.20f, safeDt)

        val swayBase = sin(safeTime * 0.81f) * 0.74f + sin(safeTime * 0.47f + 1.2f) * 0.26f
        val swaySpine = sin(safeTime * 0.81f - 0.38f) * 0.74f + sin(safeTime * 0.47f + 0.82f) * 0.26f
        val swayHead = sin(safeTime * 0.81f - 0.76f) * 0.74f + sin(safeTime * 0.47f + 0.44f) * 0.26f
        val fingerDriftL = sin(safeTime * 1.17f + 0.4f) * 0.6f + sin(safeTime * 0.63f) * 0.4f
        val fingerDriftR = sin(safeTime * 1.09f + 1.8f) * 0.6f + sin(safeTime * 0.59f + 1.1f) * 0.4f
        val bounceBeat = abs(sin(safeTime * 3.3f))

        // Contrapposto stance defaults: asymmetric weight on left leg, right thigh turned out ~10.5°
        // (so right knee bends over turned-out toes with zero ankle twist!)
        var hipShiftX = 0.014f
        var hipDropY = -squatDepth - 0.008f
        var hipShiftZ = 0f
        var hipPitch = squatDepth * 210f - airTuckFactor * 5f
        var hipYaw = -4.2f
        var hipRoll = -2.6f
        var thighYawL = 2.0f
        var thighYawR = -10.5f // Right leg naturally angled out ~10.5° from hip socket (knee over toes!)
        var ankleOffsetZL = 0f
        var ankleOffsetZR = 0.018f // Slight forward relaxed right foot for soft natural knee bend

        var spinePitch = 0f
        var spineYaw = followTorsoYaw * 0.42f
        var spineRoll = 2.2f + followTorsoRoll * 0.45f
        var chestPitch = -breath * 1.8f
        var chestYaw = 3.8f + followTorsoYaw * 0.58f
        var chestRoll = 1.8f + followTorsoRoll * 0.55f

        var headPitch = leadHeadPitch + breathLag * 1.2f
        var headYaw = leadHeadYaw
        var headRoll = swayHead * 2.4f + speechBubbleGlanceDown * 2.8f

        // FK-verified dropped, relaxed shoulders + elbows close to ribcage + soft inward wrist curl!
        var uaLPitch = 2f; var uaLYaw = -6f; var uaLRoll = -41f
        var faLPitch = -12f; var faLYaw = -18f; var faLRoll = 8f
        var handLPitch = -8f; var handLYaw = -6f; var handLRoll = -10f
        var fingerCurlL = 26f; var thumbCurlL = 16f

        var uaRPitch = 2f; var uaRYaw = 6f; var uaRRoll = 39f
        var faRPitch = -12f; var faRYaw = 18f; var faRRoll = -8f
        var handRPitch = -8f; var handRYaw = 6f; var handRRoll = 10f
        var fingerCurlR = 26f; var thumbCurlR = 16f

        // Natural Pixar bunny ear rest targets
        var earTargetLPitch = -3f
        var earTargetLRoll = -3f
        var earTargetRPitch = -3f
        var earTargetRRoll = 3f

        var targetSmile = 0.40f
        var targetRelaxLids = 0f
        var targetBrowUp = 0f
        var targetBrowFocus = 0f

        when (expression) {
            MascotExpression.IDLE -> {
                // Classic Pixar contrapposto idle: clear weight on left hip, soft bend in right knee,
                // counter-tilted shoulders, and softly asymmetric relaxed arms beside her ribcage
                hipShiftX = 0.015f + swayBase * 0.007f
                hipDropY -= 0.003f
                hipRoll = -2.8f + swayBase * 1.6f
                hipYaw = -4.5f + sin(safeTime * 0.59f) * 2.0f
                spineRoll = 2.4f - swaySpine * 1.4f + followTorsoRoll * 0.45f
                chestRoll = 2.0f - swaySpine * 1.2f + followTorsoRoll * 0.55f
                chestYaw = 4.2f + followTorsoYaw * 0.60f

                headRoll = -2.2f + swayHead * 3.0f + speechBubbleGlanceDown * 2.8f

                // Left arm rests slightly closer to supporting hip; right arm slightly softer/lower
                uaLPitch = 2f - breath * 1.5f
                uaLRoll = -42f + breath * 1.8f
                uaLYaw = -6f + swaySpine * 2.4f
                faLPitch = -14f - breathLag * 2.5f
                faLYaw = -18f - breathLag * 1.8f
                faLRoll = 9f + breathLag * 2.0f
                handLPitch = -8f - breathLag * 3.5f + fingerDriftL * 2.6f
                handLRoll = -11f + swayHead * 4.0f
                handLYaw = -6f + fingerDriftL * 3.2f
                fingerCurlL = 27f - breathLag * 4.5f + fingerDriftL * 4.0f
                thumbCurlL = 17f - breathLag * 2.5f + fingerDriftL * 2.2f

                uaRPitch = 4f - breath * 1.4f
                uaRRoll = 39f - breath * 1.8f
                uaRYaw = 8f - swaySpine * 2.4f
                faRPitch = -11f - breathLag * 2.2f
                faRYaw = 17f + breathLag * 1.8f
                faRRoll = -8f - breathLag * 2.0f
                handRPitch = -7f - breathLag * 3.0f + fingerDriftR * 2.6f
                handRRoll = 11f + swayHead * 4.0f
                handRYaw = 6f - fingerDriftR * 3.2f
                fingerCurlR = 24f - breathLag * 4.2f + fingerDriftR * 3.8f
                thumbCurlR = 15f - breathLag * 2.2f + fingerDriftR * 2.2f

                earTargetLPitch = -2f + breathLag * 2.2f
                earTargetRPitch = -1f + breathLag * 2.0f
                earTargetLRoll = -4f + swayHead * 2.6f
                earTargetRRoll = 3f + swayHead * 2.4f
                targetSmile = 0.42f
            }

            MascotExpression.HAPPY -> {
                val groove = sin(safeTime * 2.15f)
                val grooveLag = sin(safeTime * 2.15f - 0.40f)
                hipShiftX = 0.008f + groove * 0.011f
                hipDropY -= 0.004f + (1f - bounceBeat) * 0.011f
                hipRoll = -1.6f + groove * 2.5f
                hipYaw = -2.0f + groove * 4.4f
                thighYawL = 3.5f
                thighYawR = -9.5f
                ankleOffsetZR = 0.014f
                spineRoll = 1.4f - grooveLag * 1.5f + followTorsoRoll * 0.4f
                chestPitch += -2.5f + bounceBeat * 2.2f
                chestYaw = 2.0f + grooveLag * 3.8f + followTorsoYaw * 0.5f

                headYaw = leadHeadYaw * 0.75f + sin(safeTime * 1.65f) * 4.5f
                headPitch = -2f + bounceBeat * 2.4f + speechBubbleGlanceDown * 9.0f
                headRoll = grooveLag * 4.0f + winkWeight * 6.5f

                // Warm Pixar palm-forward greeting wave (left) + buoyant relaxed right arm
                val waveForearm = sin(safeTime * 6.4f)
                val waveWrist = sin(safeTime * 6.4f - 0.72f)
                val waveFinger = sin(safeTime * 6.4f - 1.25f)
                uaLPitch = -14f + sin(safeTime * 3.2f) * 2.0f
                uaLYaw = -16f
                uaLRoll = 30f + sin(safeTime * 3.2f) * 2.5f
                faLPitch = -32f + waveForearm * 4.0f
                faLYaw = -8f
                faLRoll = 52f + waveForearm * 10f
                handLPitch = -26f + waveWrist * 6f
                handLYaw = 14f + waveWrist * 4f
                handLRoll = 10f + waveWrist * 18f
                fingerCurlL = -3f + waveFinger * 6f
                thumbCurlL = 4f + waveFinger * 3f

                // Right arm tucked softly near hip (not splayed!)
                uaRPitch = -2f - bounceBeat * 3f
                uaRYaw = 10f
                uaRRoll = 36f - bounceBeat * 4f
                faRPitch = -16f - bounceBeat * 4f
                faRYaw = 18f
                faRRoll = -12f - bounceBeat * 3f
                handRPitch = -10f - bounceBeat * 5f + fingerDriftR * 2f
                handRYaw = 6f
                handRRoll = 10f + grooveLag * 5f
                fingerCurlR = 20f - bounceBeat * 6f + fingerDriftR * 3f
                thumbCurlR = 13f - bounceBeat * 3f

                earTargetLPitch = -6f - bounceBeat * 2.4f
                earTargetRPitch = -6f - bounceBeat * 2.4f
                earTargetLRoll = -5f - bounceBeat * 2.8f
                earTargetRRoll = 5f + bounceBeat * 2.8f

                targetSmile = 0.86f
                targetBrowUp = 0.34f
            }

            MascotExpression.EXCITED -> {
                val groove = sin(safeTime * 3.15f)
                val grooveLag = sin(safeTime * 3.15f - 0.45f)
                val waveForearm = sin(safeTime * 8.0f)
                val waveWrist = sin(safeTime * 8.0f - 0.78f)
                val waveFinger = sin(safeTime * 8.0f - 1.30f)

                hipShiftX = groove * 0.012f
                hipDropY -= 0.006f + abs(groove) * 0.012f
                hipRoll = groove * 2.6f
                hipYaw = groove * 5.2f
                thighYawL = 5.0f
                thighYawR = -7.0f
                ankleOffsetZR = 0.010f
                spineRoll = -grooveLag * 1.6f
                chestPitch += -3.5f
                chestRoll = -grooveLag * 2.4f
                chestYaw = grooveLag * 4.0f + followTorsoYaw * 0.45f

                headPitch = -3.5f + sin(safeTime * 6.1f) * 2.6f + speechBubbleGlanceDown * 8.0f
                headYaw = leadHeadYaw * 0.65f + grooveLag * 5.5f
                headRoll = -grooveLag * 3.8f + winkWeight * 7.0f

                // Cheerful high palm-forward wave + enthusiastic mirrored cheer
                uaLPitch = -16f
                uaLYaw = -18f
                uaLRoll = 36f
                faLPitch = -34f + waveForearm * 4f
                faLYaw = -6f
                faLRoll = 55f + waveForearm * 10f
                handLPitch = -28f + waveWrist * 6f
                handLYaw = 16f + waveWrist * 4f
                handLRoll = 12f + waveWrist * 18f
                fingerCurlL = -6f + waveFinger * 6f
                thumbCurlL = 2f + waveFinger * 4f

                val rBeat = sin(safeTime * 6.1f)
                val rWrist = sin(safeTime * 6.1f - 0.75f)
                uaRPitch = -14f
                uaRYaw = 18f
                uaRRoll = 14f - abs(groove) * 10f
                faRPitch = -30f - rBeat * 4f
                faRYaw = 18f
                faRRoll = -44f + rBeat * 10f
                handRPitch = -24f + rWrist * 6f
                handRYaw = -12f
                handRRoll = -10f - rWrist * 16f
                fingerCurlR = 2f - rWrist * 8f
                thumbCurlR = 6f - rWrist * 4f

                earTargetLPitch = -9f
                earTargetRPitch = -9f
                earTargetLRoll = -7f
                earTargetRRoll = 7f

                targetSmile = 0.94f
                targetBrowUp = 0.72f
            }

            MascotExpression.FOCUS -> {
                // Determined Officer Hopps athletic ready stance: slight weight forward, knees aligned
                hipShiftX = 0.008f + swayBase * 0.004f
                hipDropY -= 0.006f
                hipRoll = -1.4f
                hipYaw = -6.0f
                thighYawL = 3.0f
                thighYawR = -11.0f
                ankleOffsetZR = 0.020f
                spinePitch = 4.5f
                spineRoll = 1.2f
                chestPitch += 3.0f
                chestYaw = 6.0f + followTorsoYaw * 0.45f
                chestRoll = 1.0f

                headPitch = -3.5f + breathLag * 1.0f + speechBubbleGlanceDown * 8.5f
                headYaw = leadHeadYaw * 0.55f
                headRoll = -1.8f

                // Arms held compactly forward at waist (zero outward doll splay!)
                uaLPitch = -8f
                uaLYaw = -16f
                uaLRoll = -34f
                faLPitch = -24f
                faLYaw = -26f
                faLRoll = 18f
                handLPitch = -12f + breathLag * 2.5f
                handLYaw = -6f
                handLRoll = 4f
                fingerCurlL = 32f + breathLag * 3.0f
                thumbCurlL = 22f

                uaRPitch = -8f
                uaRYaw = 16f
                uaRRoll = 34f
                faRPitch = -24f
                faRYaw = 26f
                faRRoll = -18f
                handRPitch = -12f + breathLag * 2.5f
                handRYaw = 6f
                handRRoll = -4f
                fingerCurlR = 32f + breathLag * 3.0f
                thumbCurlR = 22f

                earTargetLPitch = -11f
                earTargetRPitch = -11f
                earTargetLRoll = -2f
                earTargetRRoll = 2f

                targetSmile = 0.18f
                targetBrowFocus = 0.86f
            }

            MascotExpression.RELAX -> {
                val cozySway = sin(safeTime * 0.79f) * 0.78f + sin(safeTime * 0.43f + 0.8f) * 0.22f
                val cozyLag = sin(safeTime * 0.79f - 0.50f) * 0.78f + sin(safeTime * 0.43f + 0.3f) * 0.22f
                hipShiftX = 0.016f + cozySway * 0.008f
                hipDropY -= 0.004f
                hipRoll = -3.0f + cozySway * 1.8f
                hipYaw = -3.8f
                thighYawL = 2.0f
                thighYawR = -11.5f
                ankleOffsetZR = 0.020f
                spinePitch = -2.0f + breath * 1.5f
                spineRoll = 2.6f - cozyLag * 1.6f
                chestPitch += -1.5f - breath * 1.8f
                chestRoll = 2.2f - cozyLag * 1.2f
                chestYaw = 3.5f + followTorsoYaw * 0.5f

                headPitch = 3.5f + breathLag * 1.8f + speechBubbleGlanceDown * 7.5f
                headYaw = leadHeadYaw * 0.85f + cozyLag * 3.5f
                headRoll = -6.5f + cozyLag * 2.4f

                // Extra-dropped cozy shoulders with softly draped hands beside hips
                uaLPitch = 4f
                uaLYaw = -8f
                uaLRoll = -44f + cozySway * 1.8f
                faLPitch = -11f
                faLYaw = -16f
                faLRoll = 7f
                handLPitch = -6f + breathLag * 4.0f + fingerDriftL * 2f
                handLYaw = -5f
                handLRoll = -12f + cozyLag * 4.0f
                fingerCurlL = 20f - breathLag * 4.0f + fingerDriftL * 2.5f
                thumbCurlL = 12f

                uaRPitch = 5f
                uaRYaw = 8f
                uaRRoll = 43f + cozySway * 1.8f
                faRPitch = -10f
                faRYaw = 16f
                faRRoll = -7f
                handRPitch = -6f + breathLag * 4.0f + fingerDriftR * 2f
                handRYaw = 5f
                handRRoll = 12f + cozyLag * 4.0f
                fingerCurlR = 20f - breathLag * 4.0f + fingerDriftR * 2.5f
                thumbCurlR = 12f

                earTargetLPitch = 10f + breathLag * 2.2f
                earTargetRPitch = 11f + breathLag * 2.2f
                earTargetLRoll = -8f + cozyLag * 2.5f
                earTargetRRoll = 8f + cozyLag * 2.5f

                targetSmile = 0.64f
                targetRelaxLids = 0.40f
            }

            MascotExpression.CURIOUS -> {
                // Mirrored contrapposto shift onto right leg with inquisitive head tilt & lifted hand
                hipShiftX = -0.014f + swayBase * 0.005f
                hipDropY -= 0.004f
                hipRoll = 2.8f
                hipYaw = 5.5f
                thighYawL = 10.5f // Left leg turned out cleanly over toes when weight shifts right!
                thighYawR = -2.0f
                ankleOffsetZL = 0.018f
                ankleOffsetZR = 0f
                spineRoll = -2.6f
                chestPitch += 2.0f
                chestRoll = -2.2f
                chestYaw = -4.5f + followTorsoYaw * 0.55f

                headPitch = -2.0f + sin(safeTime * 1.25f) * 2.0f + speechBubbleGlanceDown * 8.5f
                headYaw = -4.5f + leadHeadYaw * 0.8f
                headRoll = 10.8f + sin(safeTime * 1.25f) * 2.2f + winkWeight * 4.0f

                // Left arm dropped & comfortably relaxed beside hip
                uaLPitch = 3f
                uaLYaw = -8f
                uaLRoll = -41f
                faLPitch = -13f
                faLYaw = -16f
                faLRoll = 9f
                handLPitch = -8f + breathLag * 3.5f + fingerDriftL * 2f
                handLYaw = -5f
                handLRoll = -10f
                fingerCurlL = 24f + fingerDriftL * 3f
                thumbCurlL = 15f

                // Inquisitive right hand lifted cleanly near chin/cheek (zero clipping!)
                val thinkOsc = sin(safeTime * 2.15f)
                val thinkLag = sin(safeTime * 2.15f - 0.65f)
                uaRPitch = -18f
                uaRYaw = 22f
                uaRRoll = -8f
                faRPitch = -34f + thinkOsc * 3.0f
                faRYaw = 30f
                faRRoll = -48f + thinkOsc * 4.0f
                handRPitch = -28f + thinkLag * 5.0f
                handRYaw = -14f
                handRRoll = 6f + thinkLag * 7.0f
                fingerCurlR = 14f + thinkLag * 6.0f
                thumbCurlR = 16f - thinkLag * 4.0f

                earTargetLPitch = -9f
                earTargetLRoll = -3f
                earTargetRPitch = 9f + sin(safeTime * 1.85f) * 2.4f
                earTargetRRoll = 11f + sin(safeTime * 1.45f) * 2.4f

                targetSmile = 0.52f
                targetBrowUp = 0.74f
            }
        }

        // --- 4. VARIED MOOD-AWARE TAP REACTION OVERLAYS (SNAPPY, NON-REPETITIVE!) ---
        var tapCozyEyes = 0f
        var tapArmOverrideL = 0f
        var tapArmOverrideR = 0f
        if (isTapActive) {
            val env = smoothTapEnvelope(safeTapElapsed, tapDur, attack = 0.14f, release = 0.28f)
            when (activeTapReaction) {
                TapReactionType.CRISP_HOP_SETTLE -> {
                    // Snappy compact bunny hop: elbows tucked in, quick joyous smile & ear spring!
                    if (airTuckFactor > 0.01f) {
                        uaLPitch = lerp(uaLPitch, -12f, airTuckFactor * 0.60f)
                        uaRPitch = lerp(uaRPitch, -12f, airTuckFactor * 0.60f)
                        uaLRoll = lerp(uaLRoll, -14f, airTuckFactor * 0.65f)
                        uaRRoll = lerp(uaRRoll, 14f, airTuckFactor * 0.65f)
                        faLPitch = lerp(faLPitch, -28f, airTuckFactor * 0.65f)
                        faRPitch = lerp(faRPitch, -28f, airTuckFactor * 0.65f)
                        faLRoll = lerp(faLRoll, 26f, airTuckFactor * 0.55f)
                        faRRoll = lerp(faRRoll, -26f, airTuckFactor * 0.55f)
                        handLPitch = lerp(handLPitch, -20f, airTuckFactor * 0.65f)
                        handRPitch = lerp(handRPitch, -20f, airTuckFactor * 0.65f)
                        fingerCurlL = lerp(fingerCurlL, 6f, airTuckFactor * 0.7f)
                        fingerCurlR = lerp(fingerCurlR, 6f, airTuckFactor * 0.7f)
                        headPitch -= airTuckFactor * 5.0f
                        targetSmile = max(targetSmile, 0.90f * airTuckFactor)
                        targetBrowUp = max(targetBrowUp, 0.55f * airTuckFactor)
                    }
                    if (squatDepth > 0.002f) {
                        val sqRatio = (squatDepth / 0.026f).coerceIn(0f, 1f)
                        uaLYaw += sqRatio * 7f
                        uaRYaw -= sqRatio * 7f
                        spinePitch += sqRatio * 6f
                    }
                }

                TapReactionType.CHEEKY_WINK_TILT -> {
                    // Playful cheeky wink + 3/4 head tilt + sassy hip pop + quick cute left-hand wiggle
                    val wiggle = sin(safeTapElapsed * 15f)
                    hipShiftX = lerp(hipShiftX, 0.020f, env)
                    hipRoll = lerp(hipRoll, -3.5f, env)
                    chestRoll = lerp(chestRoll, 3.2f, env)
                    headRoll = lerp(headRoll, 10.5f, env)
                    headYaw = lerp(headYaw, -6.5f, env)
                    headPitch = lerp(headPitch, -3.0f, env)

                    tapArmOverrideL = env
                    uaLPitch = lerp(uaLPitch, -14f, env)
                    uaLYaw = lerp(uaLYaw, -16f, env)
                    uaLRoll = lerp(uaLRoll, 24f, env)
                    faLPitch = lerp(faLPitch, -32f + wiggle * 4f, env)
                    faLYaw = lerp(faLYaw, -10f, env)
                    faLRoll = lerp(faLRoll, 52f + wiggle * 8f, env)
                    handLPitch = lerp(handLPitch, -26f, env)
                    handLYaw = lerp(handLYaw, 14f, env)
                    handLRoll = lerp(handLRoll, 12f + wiggle * 16f, env)
                    fingerCurlL = lerp(fingerCurlL, -2f + wiggle * 5f, env)
                    thumbCurlL = lerp(thumbCurlL, 4f, env)

                    targetSmile = max(targetSmile, 0.92f * env)
                    targetBrowUp = max(targetBrowUp, 0.45f * env)
                }

                TapReactionType.OFFICER_SALUTE -> {
                    // FK-verified Officer Hopps right-hand brow salute + tall attentive posture!
                    hipShiftX = lerp(hipShiftX, 0.004f, env)
                    spinePitch = lerp(spinePitch, -2.5f, env)
                    chestPitch = lerp(chestPitch, -3.0f, env)
                    headPitch = lerp(headPitch, -3.0f, env)
                    headRoll = lerp(headRoll, -3.5f, env)
                    headYaw = lerp(headYaw, 4.0f, env)

                    tapArmOverrideR = env
                    uaRPitch = lerp(uaRPitch, -24f, env)
                    uaRYaw = lerp(uaRYaw, 26f, env)
                    uaRRoll = lerp(uaRRoll, -22f, env)
                    faRPitch = lerp(faRPitch, -34f, env)
                    faRYaw = lerp(faRYaw, 74f, env)
                    faRRoll = lerp(faRRoll, -90f, env)
                    handRPitch = lerp(handRPitch, -10f, env)
                    handRYaw = lerp(handRYaw, 10f, env)
                    handRRoll = lerp(handRRoll, -8f, env)
                    fingerCurlR = lerp(fingerCurlR, -4f, env) // Crisp straight salute fingers!
                    thumbCurlR = lerp(thumbCurlR, 6f, env)

                    earTargetLPitch = lerp(earTargetLPitch, -12f, env)
                    earTargetRPitch = lerp(earTargetRPitch, -12f, env)
                    targetSmile = max(targetSmile, 0.72f * env)
                    targetBrowUp = max(targetBrowUp, 0.35f * env)
                }

                TapReactionType.DOUBLE_EAR_FLICK_PEEK -> {
                    // Quick playful head-bob + double asymmetric ear-flick + cheerful shoulder pop
                    val bob = sin((safeTapElapsed / tapDur) * PI.toFloat() * 2f)
                    headYaw += bob * 7.5f * env
                    headRoll -= bob * 6.0f * env
                    headPitch -= abs(bob) * 4.5f * env
                    chestYaw -= bob * 3.5f * env
                    uaLRoll += env * 9f
                    uaRRoll -= env * 9f
                    earTargetLPitch -= (4f + bob * 8f) * env
                    earTargetRPitch -= (4f - bob * 8f) * env
                    earTargetLRoll -= 6f * env
                    earTargetRRoll += 6f * env
                    targetSmile = max(targetSmile, 0.80f * env)
                    targetBrowUp = max(targetBrowUp, 0.65f * env)
                }

                TapReactionType.COZY_STRETCH_SIGH -> {
                    // Cozy gentle inhale stretch + contented closed-eye smile + soft ear droop
                    spinePitch -= 4.5f * env
                    chestPitch -= 4.0f * env
                    headPitch += 3.5f * env
                    headRoll -= 6.5f * env
                    uaLRoll += env * 12f
                    uaRRoll -= env * 12f
                    earTargetLPitch += 9f * env
                    earTargetRPitch += 10f * env
                    tapCozyEyes = 0.85f * env
                    targetSmile = max(targetSmile, 0.82f * env)
                }
            }
        }

        // --- 5. CONVERSATIONAL SPEAKING GESTURES (NATURAL PALM-OPEN PHRASING + BROW EMPHASIS) ---
        if (smoothSpeak > 0.01f) {
            val speakBeatA = sin(safeTime * 4.4f)
            val speakBeatALag = sin(safeTime * 4.4f - 0.65f)
            val speakBeatB = sin(safeTime * 4.4f + 2.1f)
            val speakBeatBLag = sin(safeTime * 4.4f + 1.45f)
            val emphasis = abs(sin(safeTime * 2.8f))

            // Natural conversational head nods & brow emphasis synced to syllable energy
            headPitch += (sin(safeTime * 7.4f) * 2.6f + syllableWave * 2.8f) * smoothSpeak
            headYaw += sin(safeTime * 3.2f) * 4.2f * smoothSpeak
            headRoll += sin(safeTime * 3.9f) * 2.8f * smoothSpeak
            chestPitch += (sin(safeTime * 4.4f) * 1.8f + syllableWave * 1.4f) * smoothSpeak
            chestYaw += sin(safeTime * 3.2f - 0.35f) * 2.4f * smoothSpeak

            val talkArmL = max(0f, speakBeatA) * smoothSpeak
            val talkArmR = max(0f, speakBeatB) * smoothSpeak
            val leftSpeakBlend = smoothSpeak * (1f - tapArmOverrideL).coerceIn(0f, 1f)
            val rightSpeakBlend = smoothSpeak * (1f - tapArmOverrideR).coerceIn(0f, 1f)

            if (expression != MascotExpression.EXCITED && expression != MascotExpression.HAPPY && leftSpeakBlend > 0.01f) {
                uaLPitch = lerp(uaLPitch, -10f - talkArmL * 6f, leftSpeakBlend * 0.72f)
                uaLYaw = lerp(uaLYaw, -15f - talkArmL * 4f, leftSpeakBlend * 0.72f)
                uaLRoll = lerp(uaLRoll, -28f + talkArmL * 12f, leftSpeakBlend * 0.72f)
                faLPitch = lerp(faLPitch, -26f - talkArmL * 8f, leftSpeakBlend * 0.76f)
                faLYaw = lerp(faLYaw, -24f, leftSpeakBlend * 0.76f)
                faLRoll = lerp(faLRoll, 24f + talkArmL * 10f, leftSpeakBlend * 0.76f)
                handLPitch = lerp(handLPitch, -20f - talkArmL * 10f + speakBeatALag * 5f, leftSpeakBlend * 0.84f)
                handLYaw = lerp(handLYaw, 10f + talkArmL * 6f, leftSpeakBlend * 0.80f)
                handLRoll = lerp(handLRoll, 8f + speakBeatALag * 10f, leftSpeakBlend * 0.84f)
                fingerCurlL = lerp(fingerCurlL, 16f - (talkArmL * 18f + syllableWave * 6f), leftSpeakBlend * 0.84f)
                thumbCurlL = lerp(thumbCurlL, 10f - talkArmL * 7f, leftSpeakBlend * 0.80f)
            }
            if (expression != MascotExpression.CURIOUS && expression != MascotExpression.EXCITED && rightSpeakBlend > 0.01f) {
                uaRPitch = lerp(uaRPitch, -10f - talkArmR * 6f, rightSpeakBlend * 0.72f)
                uaRYaw = lerp(uaRYaw, 15f + talkArmR * 4f, rightSpeakBlend * 0.72f)
                uaRRoll = lerp(uaRRoll, 28f - talkArmR * 12f, rightSpeakBlend * 0.72f)
                faRPitch = lerp(faRPitch, -26f - talkArmR * 8f, rightSpeakBlend * 0.76f)
                faRYaw = lerp(faRYaw, 24f, rightSpeakBlend * 0.76f)
                faRRoll = lerp(faRRoll, -24f - talkArmR * 10f, rightSpeakBlend * 0.76f)
                handRPitch = lerp(handRPitch, -20f - talkArmR * 10f + speakBeatBLag * 5f, rightSpeakBlend * 0.84f)
                handRYaw = lerp(handRYaw, -10f - talkArmR * 6f, rightSpeakBlend * 0.80f)
                handRRoll = lerp(handRRoll, -8f - speakBeatBLag * 10f, rightSpeakBlend * 0.84f)
                fingerCurlR = lerp(fingerCurlR, 16f - (talkArmR * 18f + syllableWave * 6f), rightSpeakBlend * 0.84f)
                thumbCurlR = lerp(thumbCurlR, 10f - talkArmR * 7f, rightSpeakBlend * 0.80f)
            }

            earTargetLPitch += sin(safeTime * 5.2f) * 3.0f * smoothSpeak
            earTargetRPitch += sin(safeTime * 5.2f + 0.55f) * 3.0f * smoothSpeak
            targetBrowUp = max(targetBrowUp, (0.20f + emphasis * 0.44f) * smoothSpeak)
        }

        // --- 6. ANALYTICAL 2-BONE FOOT-LOCKING LEG IK (WITH KNEE-OVER-TOE THIGH YAW!) ---
        val groundedIkWeight = (1f - airTuckFactor * 1.35f).coerceIn(0f, 1f)
        solveGroundedLegIK(
            side = +1f,
            hipTx = hipShiftX,
            hipTy = hipDropY,
            hipTz = hipShiftZ,
            hipPitchDeg = hipPitch,
            hipRollDeg = hipRoll,
            thighYawDeg = thighYawL,
            ankleOffsetZ = ankleOffsetZL,
            out = legIkL
        )
        solveGroundedLegIK(
            side = -1f,
            hipTx = hipShiftX,
            hipTy = hipDropY,
            hipTz = hipShiftZ,
            hipPitchDeg = hipPitch,
            hipRollDeg = hipRoll,
            thighYawDeg = thighYawR,
            ankleOffsetZ = ankleOffsetZR,
            out = legIkR
        )

        val airThighL = -airTuckFactor * 18f
        val airShinL = airTuckFactor * 30f
        val airFootL = airTuckFactor * 14f

        val airThighR = -airTuckFactor * 14f
        val airShinR = airTuckFactor * 24f
        val airFootR = airTuckFactor * 12f

        val finalThighLPitch = lerp(airThighL, legIkL.thighPitchDeg, groundedIkWeight).coerceIn(-28f, 14f)
        val finalThighLYaw = lerp(0f, legIkL.thighYawDeg, groundedIkWeight).coerceIn(-14f, 14f)
        val finalThighLRoll = lerp(0f, legIkL.thighRollDeg, groundedIkWeight).coerceIn(-10f, 10f)
        val finalShinLPitch = lerp(airShinL, legIkL.shinPitchDeg, groundedIkWeight).coerceIn(0f, 34f)
        val finalFootLPitch = lerp(airFootL, legIkL.footPitchDeg, groundedIkWeight).coerceIn(-22f, 18f)
        val finalFootLRoll = lerp(0f, legIkL.footRollDeg, groundedIkWeight).coerceIn(-10f, 10f)

        val finalThighRPitch = lerp(airThighR, legIkR.thighPitchDeg, groundedIkWeight).coerceIn(-28f, 14f)
        val finalThighRYaw = lerp(0f, legIkR.thighYawDeg, groundedIkWeight).coerceIn(-14f, 14f)
        val finalThighRRoll = lerp(0f, legIkR.thighRollDeg, groundedIkWeight).coerceIn(-10f, 10f)
        val finalShinRPitch = lerp(airShinR, legIkR.shinPitchDeg, groundedIkWeight).coerceIn(0f, 34f)
        val finalFootRPitch = lerp(airFootR, legIkR.footPitchDeg, groundedIkWeight).coerceIn(-22f, 18f)
        val finalFootRRoll = lerp(0f, legIkR.footRollDeg, groundedIkWeight).coerceIn(-10f, 10f)

        // --- 7. CRITICALLY-DAMPED EAR, WRIST, FINGER & INERTIAL SPRINGS ---
        val spinLag = (-safeUserYawVel * 0.044f).coerceIn(-13f, 13f)
        val lagTorso = torsoLagYaw.step(spinLag * 0.36f, safeDt)
        val lagHead = headLagYaw.step(spinLag * 0.62f, safeDt)
        val earJumpDrive = (jumpAccel * 0.50f - jumpVel * 4.5f).coerceIn(-14f, 14f)

        val ebLP = earBaseLPitch.step((earTargetLPitch + earJumpDrive * 0.55f).coerceIn(-16f, 16f), safeDt)
        val ebLR = earBaseLRoll.step((earTargetLRoll + spinLag * 0.35f).coerceIn(-14f, 14f), safeDt)
        val etLP = earTipLPitch.step((ebLP * 0.45f + earJumpDrive * 0.45f).coerceIn(-13f, 13f), safeDt)
        val etLR = earTipLRoll.step((ebLR * 0.40f + spinLag * 0.40f).coerceIn(-10f, 10f), safeDt)

        val ebRP = earBaseRPitch.step((earTargetRPitch + earJumpDrive * 0.55f).coerceIn(-16f, 16f), safeDt)
        val ebRR = earBaseRRoll.step((earTargetRRoll + spinLag * 0.35f).coerceIn(-14f, 14f), safeDt)
        val etRP = earTipRPitch.step((ebRP * 0.45f + earJumpDrive * 0.45f).coerceIn(-13f, 13f), safeDt)
        val etRR = earTipRRoll.step((ebRR * 0.40f + spinLag * 0.40f).coerceIn(-10f, 10f), safeDt)

        val curSmoothedFaL = lerp(poses[JudyBones.FORE_ARM_L].roll, faLRoll.coerceIn(-24f, 66f), poseBlend)
        val curSmoothedFaR = lerp(poses[JudyBones.FORE_ARM_R].roll, faRRoll.coerceIn(-96f, 24f), poseBlend)
        val faLVel = ((curSmoothedFaL - prevSmoothedFaLRoll) / safeDt).coerceIn(-140f, 140f)
        val faRVel = ((curSmoothedFaR - prevSmoothedFaRRoll) / safeDt).coerceIn(-140f, 140f)
        prevSmoothedFaLRoll = curSmoothedFaL
        prevSmoothedFaRRoll = curSmoothedFaR

        val hLP = handLPitchSpring.step(handLPitch.coerceIn(-36f, 22f), safeDt)
        val hLR = handLRollSpring.step((handLRoll - faLVel * 0.045f).coerceIn(-32f, 32f), safeDt)
        val hLY = handLYawSpring.step(handLYaw.coerceIn(-24f, 24f), safeDt)
        val fgL = fingersLCurlSpring.step(fingerCurlL.coerceIn(-12f, 40f), safeDt)
        val thL = thumbLCurlSpring.step(thumbCurlL.coerceIn(-8f, 30f), safeDt)

        val hRP = handRPitchSpring.step(handRPitch.coerceIn(-36f, 22f), safeDt)
        val hRR = handRRollSpring.step((handRRoll - faRVel * 0.045f).coerceIn(-32f, 32f), safeDt)
        val hRY = handRYawSpring.step(handRYaw.coerceIn(-24f, 24f), safeDt)
        val fgR = fingersRCurlSpring.step(fingerCurlR.coerceIn(-12f, 40f), safeDt)
        val thR = thumbRCurlSpring.step(thumbCurlR.coerceIn(-8f, 30f), safeDt)

        // --- 8. POPULATE ALL 21 CORE JOINTS + 4 FINGER/THUMB JOINTS ---
        groundedHipShiftX = hipShiftX.coerceIn(-0.022f, 0.022f)
        targets[JudyBones.HIPS].set(
            pitch = hipPitch.coerceIn(-10f, 12f),
            yaw = hipYaw.coerceIn(-10f, 10f),
            roll = hipRoll.coerceIn(-5f, 5f),
            tx = groundedHipShiftX,
            ty = hipDropY.coerceIn(-0.038f, 0.004f),
            tz = hipShiftZ
        )
        targets[JudyBones.SPINE].set(
            pitch = spinePitch.coerceIn(-9f, 10f),
            yaw = (spineYaw + lagTorso * 0.4f).coerceIn(-10f, 10f),
            roll = spineRoll.coerceIn(-7f, 7f)
        )
        targets[JudyBones.CHEST].set(
            pitch = chestPitch.coerceIn(-9f, 10f),
            yaw = (chestYaw + lagTorso * 0.6f).coerceIn(-11f, 11f),
            roll = chestRoll.coerceIn(-7f, 7f)
        )
        targets[JudyBones.NECK].set(
            pitch = (headPitch * 0.34f).coerceIn(-7f, 7f),
            yaw = ((headYaw + lagHead) * 0.34f).coerceIn(-9f, 9f),
            roll = (headRoll * 0.34f).coerceIn(-6f, 6f)
        )
        targets[JudyBones.HEAD].set(
            pitch = (headPitch * 0.66f).coerceIn(-14f, 14f),
            yaw = ((headYaw + lagHead) * 0.66f).coerceIn(-18f, 18f),
            roll = (headRoll * 0.66f).coerceIn(-13f, 13f)
        )

        targets[JudyBones.EAR_BASE_L].set(pitch = ebLP.coerceIn(-16f, 16f), roll = ebLR.coerceIn(-14f, 14f))
        targets[JudyBones.EAR_TIP_L].set(pitch = etLP.coerceIn(-13f, 13f), roll = etLR.coerceIn(-10f, 10f))
        targets[JudyBones.EAR_BASE_R].set(pitch = ebRP.coerceIn(-16f, 16f), roll = ebRR.coerceIn(-14f, 14f))
        targets[JudyBones.EAR_TIP_R].set(pitch = etRP.coerceIn(-13f, 13f), roll = etRR.coerceIn(-10f, 10f))

        targets[JudyBones.UPPER_ARM_L].set(
            pitch = uaLPitch.coerceIn(-28f, 14f),
            yaw = uaLYaw.coerceIn(-28f, 24f),
            roll = uaLRoll.coerceIn(-48f, 44f)
        )
        targets[JudyBones.FORE_ARM_L].set(
            pitch = faLPitch.coerceIn(-40f, 10f),
            yaw = faLYaw.coerceIn(-34f, 20f),
            roll = faLRoll.coerceIn(-24f, 66f)
        )
        targets[JudyBones.HAND_L].set(
            pitch = hLP.coerceIn(-36f, 22f),
            yaw = hLY.coerceIn(-24f, 24f),
            roll = hLR.coerceIn(-32f, 32f)
        )
        targets[JudyBones.FINGERS_L].set(
            pitch = (fgL * 0.22f).coerceIn(-8f, 14f),
            yaw = (-fgL * 0.30f).coerceIn(-16f, 8f),
            roll = (-fgL).coerceIn(-40f, 14f)
        )
        targets[JudyBones.THUMB_L].set(
            pitch = (thL * 0.55f).coerceIn(-8f, 20f),
            yaw = (-thL * 0.85f).coerceIn(-28f, 10f),
            roll = (-thL * 0.35f).coerceIn(-14f, 8f)
        )

        targets[JudyBones.UPPER_ARM_R].set(
            pitch = uaRPitch.coerceIn(-28f, 14f),
            yaw = uaRYaw.coerceIn(-24f, 30f),
            roll = uaRRoll.coerceIn(-44f, 48f)
        )
        targets[JudyBones.FORE_ARM_R].set(
            pitch = faRPitch.coerceIn(-42f, 10f),
            yaw = faRYaw.coerceIn(-20f, 78f),
            roll = faRRoll.coerceIn(-96f, 24f)
        )
        targets[JudyBones.HAND_R].set(
            pitch = hRP.coerceIn(-36f, 22f),
            yaw = hRY.coerceIn(-24f, 24f),
            roll = hRR.coerceIn(-32f, 32f)
        )
        targets[JudyBones.FINGERS_R].set(
            pitch = (fgR * 0.22f).coerceIn(-8f, 14f),
            yaw = (fgR * 0.30f).coerceIn(-16f, 16f),
            roll = (fgR).coerceIn(-14f, 40f)
        )
        targets[JudyBones.THUMB_R].set(
            pitch = (thR * 0.55f).coerceIn(-8f, 20f),
            yaw = (thR * 0.85f).coerceIn(-10f, 28f),
            roll = (thR * 0.35f).coerceIn(-8f, 14f)
        )

        // Apply Thigh yaw so the knee bends anatomically over the turned-out toes!
        targets[JudyBones.THIGH_L].set(pitch = finalThighLPitch, yaw = finalThighLYaw, roll = finalThighLRoll)
        targets[JudyBones.SHIN_L].set(pitch = finalShinLPitch)
        targets[JudyBones.FOOT_L].set(pitch = finalFootLPitch, roll = finalFootLRoll)

        targets[JudyBones.THIGH_R].set(pitch = finalThighRPitch, yaw = finalThighRYaw, roll = finalThighRRoll)
        targets[JudyBones.SHIN_R].set(pitch = finalShinRPitch)
        targets[JudyBones.FOOT_R].set(pitch = finalFootRPitch, roll = finalFootRRoll)

        // --- 9. PHASE 2 EXPRESSIVE LIVELY SOFT-BODY BOUNCE (CHEST, HIPS & TAIL) ---
        val hipDropVel = ((hipDropY - prevHipDropY) / safeDt).coerceIn(-2.0f, 2.0f)
        val hipDropAccel = ((hipDropVel - prevHipDropVel) / safeDt).coerceIn(-16f, 16f)
        prevHipDropY = hipDropY
        prevHipDropVel = hipDropVel

        val hipShiftVel = ((hipShiftX - prevHipShiftX) / safeDt).coerceIn(-2.0f, 2.0f)
        val hipShiftAccel = ((hipShiftVel - prevHipShiftVel) / safeDt).coerceIn(-14f, 14f)
        prevHipShiftX = hipShiftX
        prevHipShiftVel = hipShiftVel

        if (sb > 0.001f) {
            val torsoAccelY = (jumpAccel + hipDropAccel * 1.15f).coerceIn(-22f, 22f)
            val verticalBounceDrive = (-torsoAccelY * 0.00145f).coerceIn(-0.017f, 0.017f)
            val lateralInertiaDrive = ((-hipShiftAccel * 0.00105f) + (spinLag * 0.00068f)).coerceIn(-0.010f, 0.010f)

            // Prominent speech syllable bounce + breathing lift on SoftChest_L/R (with organic L/R micro-phase)
            val speechPulseL = (syllableWave * 0.0125f * smoothSpeak) +
                (sin(safeTime * 7.8f) * 0.0024f * smoothSpeak) + (breath * 0.0046f)
            val speechPulseR = (syllableWave * 0.0118f * smoothSpeak) +
                (sin(safeTime * 7.8f - 0.35f) * 0.0024f * smoothSpeak) + (breath * 0.0044f)
            // Subtle hip rhythmic follow-through during talking & upbeat grooves
            val hipSpeakBounce = (syllableWave * 0.0048f * smoothSpeak) + (breath * 0.0018f)

            val chestLY = softChestLY.step((verticalBounceDrive + speechPulseL) * sb, safeDt)
            val chestRY = softChestRY.step((verticalBounceDrive * 0.96f + speechPulseR) * sb, safeDt)
            val chestLX = softChestLX.step((lateralInertiaDrive + sin(safeTime * 4.4f) * 0.0022f * smoothSpeak) * sb, safeDt)
            val chestRX = softChestRX.step((lateralInertiaDrive * 0.94f + sin(safeTime * 4.4f - 0.3f) * 0.0022f * smoothSpeak) * sb, safeDt)

            val hipLY = softHipLY.step((verticalBounceDrive * 1.08f + hipSpeakBounce) * sb, safeDt)
            val hipRY = softHipRY.step((verticalBounceDrive * 1.04f + hipSpeakBounce * 0.94f) * sb, safeDt)
            val hipLX = softHipLX.step((lateralInertiaDrive * 1.15f) * sb, safeDt)
            val hipRX = softHipRX.step((lateralInertiaDrive * 1.15f) * sb, safeDt)

            // Playful bunny-tail bounce & happy/excited wag + 360° spin lag
            val happyWag = when (expression) {
                MascotExpression.EXCITED -> sin(safeTime * 11.0f) * 8.5f
                MascotExpression.HAPPY -> sin(safeTime * 7.4f) * 5.6f
                else -> sin(safeTime * 2.2f) * 1.6f
            }
            val tailP = tailPitchSpring.step((torsoAccelY * 0.62f + breath * 2.4f + syllableWave * 2.2f * smoothSpeak) * sb, safeDt)
            val tailY = tailYawSpring.step((spinLag * 0.92f + happyWag) * sb, safeDt)

            targets[JudyBones.SOFT_CHEST_L].set(
                pitch = (-chestLY * 640f).coerceIn(-12.0f, 12.0f),
                roll = (-chestLX * 580f).coerceIn(-8.0f, 8.0f),
                tx = chestLX,
                ty = chestLY
            )
            targets[JudyBones.SOFT_CHEST_R].set(
                pitch = (-chestRY * 640f).coerceIn(-12.0f, 12.0f),
                roll = (-chestRX * 580f).coerceIn(-8.0f, 8.0f),
                tx = chestRX,
                ty = chestRY
            )
            targets[JudyBones.SOFT_HIP_L].set(
                pitch = (-hipLY * 500f).coerceIn(-9.5f, 9.5f),
                roll = (-hipLX * 620f).coerceIn(-9.5f, 9.5f),
                tx = hipLX,
                ty = hipLY
            )
            targets[JudyBones.SOFT_HIP_R].set(
                pitch = (-hipRY * 500f).coerceIn(-9.5f, 9.5f),
                roll = (-hipRX * 620f).coerceIn(-9.5f, 9.5f),
                tx = hipRX,
                ty = hipRY
            )
            targets[JudyBones.TAIL].set(
                pitch = tailP.coerceIn(-13f, 13f),
                yaw = tailY.coerceIn(-15f, 15f),
                ty = (verticalBounceDrive * 0.85f * sb).coerceIn(-0.010f, 0.010f)
            )
        } else {
            for (i in JudyBones.SOFT_CHEST_L..JudyBones.TAIL) {
                targets[i].set()
                poses[i].set()
            }
        }

        // Smoothly apply pose targets (spring-driven ears, wrists, fingers & Phase 2 soft-body track crisply!)
        for (i in 0 until JudyBones.COUNT) {
            when {
                i in JudyBones.EAR_BASE_L..JudyBones.EAR_TIP_R ||
                i == JudyBones.HAND_L || i == JudyBones.HAND_R ||
                i in JudyBones.FINGERS_L..JudyBones.THUMB_R -> {
                    poses[i].set(pitch = targets[i].pitch, yaw = targets[i].yaw, roll = targets[i].roll)
                }
                i in JudyBones.THIGH_L..JudyBones.FOOT_R -> {
                    val legBlend = if (groundedIkWeight > 0.8f) 1f - exp(-18f * safeDt) else poseBlend
                    poses[i].blendToward(targets[i], legBlend)
                }
                i in JudyBones.SOFT_CHEST_L..JudyBones.TAIL -> {
                    poses[i].set(
                        pitch = targets[i].pitch,
                        yaw = targets[i].yaw,
                        roll = targets[i].roll,
                        tx = targets[i].tx,
                        ty = targets[i].ty
                    )
                }
                else -> poses[i].blendToward(targets[i], poseBlend)
            }
            poses[i].sanitizeAll()
        }

        // --- 10. SMOOTHLY UPDATE ALL 8 AMPLIFIED FACIAL MORPH WEIGHTS ---
        smoothSmile = lerp(smoothSmile, targetSmile, facialBlend)
        smoothRelaxLids = lerp(smoothRelaxLids, max(targetRelaxLids, tapCozyEyes), facialBlend)
        smoothBrowUp = lerp(smoothBrowUp, targetBrowUp, facialBlend)
        smoothBrowFocus = lerp(smoothBrowFocus, targetBrowFocus, facialBlend)

        smoothMouthOpen = lerp(smoothMouthOpen, targetMouthOpen, lipBlend)
        smoothMouthWide = lerp(smoothMouthWide, targetMouthWide, lipBlend)
        smoothMouthRound = lerp(smoothMouthRound, targetMouthRound, lipBlend)

        morphWeights[JudyMorphs.BLINK_L] = safeUnit(max(max(naturalBlink, smoothRelaxLids), winkWeight * 0.96f))
        morphWeights[JudyMorphs.BLINK_R] = safeUnit(max(naturalBlink, smoothRelaxLids))
        morphWeights[JudyMorphs.MOUTH_OPEN] = safeUnit(smoothMouthOpen)
        morphWeights[JudyMorphs.SMILE] = safeUnit(smoothSmile + winkWeight * 0.16f - smoothMouthRound * 0.22f)
        morphWeights[JudyMorphs.BROW_UP] = safeUnit(smoothBrowUp)
        morphWeights[JudyMorphs.BROW_FOCUS] = safeUnit(smoothBrowFocus)
        morphWeights[JudyMorphs.MOUTH_WIDE] = safeUnit(smoothMouthWide)
        morphWeights[JudyMorphs.MOUTH_ROUND] = safeUnit(smoothMouthRound)

        // --- 11. ROOT TURNTABLE TRANSFORM (HORIZONTAL-ONLY 360° SPIN, SOLES PLANTED AT y = 0.000m) ---
        rootOffsetY = if (jumpHeight.isFinite()) (jumpHeight + 0.009f).coerceIn(0f, 0.24f) else 0.009f
        rootYawDeg = if ((safeUserYaw + jumpSpinDeg).isFinite()) safeUserYaw + jumpSpinDeg else 0f

        val breathScale = if (isBreathing) breath * 0.005f else 0f
        rootScaleX = (1f + breathScale - squashVal * 0.45f).coerceIn(0.92f, 1.08f)
        rootScaleY = (1f + breathScale + squashVal).coerceIn(0.92f, 1.08f)
        rootScaleZ = (1f + breathScale - squashVal * 0.45f).coerceIn(0.92f, 1.08f)
    }

    /**
     * Analytical Law-of-Cosines 2-bone Leg IK solver using the exact exported thigh (L1) and
     * shin (L2) lengths. Applies contrapposto turn-out at the Thigh yaw joint so the knee bends
     * anatomically over the turned-out toes while keeping both soles pinned flat to y = 0.
     */
    private fun solveGroundedLegIK(
        side: Float,
        hipTx: Float,
        hipTy: Float,
        hipTz: Float,
        hipPitchDeg: Float,
        hipRollDeg: Float,
        thighYawDeg: Float,
        ankleOffsetZ: Float,
        out: LegIkSolution
    ) {
        val hipRollRad = hipRollDeg * (PI.toFloat() / 180f)
        val localSocketX = side * JudyBones.THIGH_REST_X
        val localSocketY = JudyBones.THIGH_REST_Y - JudyBones.HIP_REST_Y

        val rotSocketX = localSocketX * cos(hipRollRad) - localSocketY * sin(hipRollRad)
        val rotSocketY = localSocketX * sin(hipRollRad) + localSocketY * cos(hipRollRad)

        val socketX = hipTx + rotSocketX
        val socketY = JudyBones.HIP_REST_Y + hipTy + rotSocketY
        val socketZ = JudyBones.THIGH_REST_Z + hipTz

        val targetX = side * JudyBones.THIGH_REST_X
        val targetY = JudyBones.ANKLE_REST_Y
        val targetZ = JudyBones.ANKLE_REST_Z + ankleOffsetZ

        val dx = targetX - socketX
        val dy = targetY - socketY
        val dz = targetZ - socketZ

        val dist = sqrt(dx * dx + dy * dy + dz * dz)
            .coerceIn(JudyBones.MIN_LEG_REACH, JudyBones.MAX_LEG_REACH)

        val l1 = JudyBones.L1_THIGH
        val l2 = JudyBones.L2_SHIN

        val cosKnee = ((l1 * l1 + l2 * l2 - dist * dist) / (2f * l1 * l2)).coerceIn(-1f, 1f)
        val interiorKneeRad = acos(cosKnee)
        val kneeBendRad = (PI.toFloat() - interiorKneeRad).coerceIn(0f, 0.48f)

        val cosAlpha = ((l1 * l1 + dist * dist - l2 * l2) / (2f * l1 * dist)).coerceIn(-1f, 1f)
        val alphaRad = acos(cosAlpha)

        // Transform sagittal/lateral aim into the turned-out thigh's local yaw frame
        val yawRad = thighYawDeg * (PI.toFloat() / 180f)
        val localDx = dx * cos(yawRad) - dz * sin(yawRad)
        val localDz = dx * sin(yawRad) + dz * cos(yawRad)

        val aimPitchRad = atan2(localDz, -dy)
        val aimRollRad = atan2(localDx, -dy)

        val radToDeg = 180f / PI.toFloat()
        val kneeDeg = kneeBendRad * radToDeg

        val thighPitch = -(alphaRad + aimPitchRad) * radToDeg - hipPitchDeg
        val thighRoll = aimRollRad * radToDeg - hipRollDeg
        val shinPitch = kneeDeg
        val footPitch = -(hipPitchDeg + thighPitch + shinPitch)
        val footRoll = -(hipRollDeg + thighRoll)

        out.thighPitchDeg = thighPitch
        out.thighYawDeg = thighYawDeg
        out.thighRollDeg = thighRoll
        out.shinPitchDeg = shinPitch
        out.footPitchDeg = footPitch
        out.footRollDeg = footRoll
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

    private fun smoothTapEnvelope(t: Float, duration: Float, attack: Float, release: Float): Float {
        if (t <= 0f || t >= duration) return 0f
        val up = smoothstep(0f, attack, t)
        val down = 1f - smoothstep(duration - release, duration, t)
        return (up * down).coerceIn(0f, 1f)
    }

    private fun lerp(a: Float, b: Float, t: Float): Float = a + (b - a) * t

    private fun smoothstep(edge0: Float, edge1: Float, x: Float): Float {
        val t = ((x - edge0) / (edge1 - edge0)).coerceIn(0f, 1f)
        return t * t * (3f - 2f * t)
    }

    private fun safeUnit(v: Float): Float = if (v.isFinite()) v.coerceIn(0f, 1f) else 0f
}
