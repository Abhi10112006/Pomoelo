package com.example.ui.components

import android.util.Log
import com.google.android.filament.Engine
import com.google.android.filament.IndirectLight
import com.google.android.filament.Texture
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

private const val IBL_TAG = "JudyStudioIbl"

/**
 * Crash-safe analytical Filament PBR studio Image-Based Lighting (IBL) rig:
 *  - High-resolution 128×128 (8 mip levels) pre-convolved specular radiance cubemap
 *  - Two crisp white Pixar studio softboxes positioned specifically on the upper-left and
 *    upper-right corneal reflection vectors for unmistakable jewel-like eye catchlights
 *  - Dual upper-back studio rim strips so Judy's ears, fur, and navy uniform pop cleanly
 *    off dark OLED backgrounds
 *  - Calibrated 3-band spherical harmonics irradiance for soft, non-muddy shadow fill
 *
 * Uploaded directly via Texture.setImage per mip level without GPU IBLPrefilterContext FBOs
 * or generateMipmaps(), preventing OpenGL ES 3.0 float-FBO aborts on mobile devices.
 */
internal class JudyStudioIbl private constructor(
    val indirectLight: IndirectLight,
    private val reflectionsTexture: Texture?
) {
    fun destroy(engine: Engine) {
        runCatching { engine.destroyIndirectLight(indirectLight) }
        reflectionsTexture?.let { tex ->
            runCatching { engine.destroyTexture(tex) }
        }
    }

    companion object {
        private const val BASE_SIZE = 128
        private const val MIP_LEVELS = 8 // 128, 64, 32, 16, 8, 4, 2, 1

        // Balanced bright-studio 3-band SH irradiance lifting dark navy pajamas off OLED black
        private fun studioSphericalHarmonics(): FloatArray = FloatArray(27).apply {
            // Band 0 (DC ambient studio fill)
            this[0] = 0.96f; this[1] = 0.95f; this[2] = 0.99f
            // Band 1 (Y up-down, Z front-back, X left-right directional softness)
            this[3] = 0.28f; this[4] = 0.28f; this[5] = 0.31f
            this[6] = 0.34f; this[7] = 0.34f; this[8] = 0.37f
            this[9] = 0.14f; this[10] = 0.14f; this[11] = 0.12f
        }

        fun create(engine: Engine): JudyStudioIbl {
            val sh = studioSphericalHarmonics()
            var cubemap: Texture? = null
            try {
                val useFloatHdr = Texture.isTextureFormatSupported(
                    engine,
                    Texture.InternalFormat.RGBA16F
                )
                val internalFormat = if (useFloatHdr) {
                    Texture.InternalFormat.RGBA16F
                } else {
                    Texture.InternalFormat.RGBA8
                }

                cubemap = Texture.Builder()
                    .width(BASE_SIZE)
                    .height(BASE_SIZE)
                    .levels(MIP_LEVELS)
                    .sampler(Texture.Sampler.SAMPLER_CUBEMAP)
                    .format(internalFormat)
                    .build(engine)

                // Corneal catchlight softboxes: aimed so upper-left (primary) & upper-right (secondary)
                // specular reflections sit crisply inside Judy's purple irises when facing the camera (+Z)
                val keyBoxDir = normalize3(-0.34f, 0.44f, 0.83f)
                val fillBoxDir = normalize3(0.46f, 0.34f, 0.82f)
                // Dual rear studio rim strips for clean 360° silhouette separation against dark OLED UI
                val rimLeftDir = normalize3(-0.60f, 0.50f, -0.62f)
                val rimRightDir = normalize3(0.60f, 0.46f, -0.65f)

                for (level in 0 until MIP_LEVELS) {
                    val dim = (BASE_SIZE shr level).coerceAtLeast(1)
                    val roughness = level.toFloat() / (MIP_LEVELS - 1).toFloat()
                    if (useFloatHdr) {
                        val faceBytes = dim * dim * 16 // RGBA FLOAT
                        val buf = buildCubemapLevelFloat(
                            dim, roughness, keyBoxDir, fillBoxDir, rimLeftDir, rimRightDir
                        )
                        val offsets = IntArray(6) { face -> face * faceBytes }
                        cubemap.setImage(
                            engine,
                            level,
                            Texture.PixelBufferDescriptor(buf, Texture.Format.RGBA, Texture.Type.FLOAT),
                            offsets
                        )
                    } else {
                        val faceBytes = dim * dim * 4 // RGBA UBYTE (RGBM)
                        val buf = buildCubemapLevelUByte(
                            dim, roughness, keyBoxDir, fillBoxDir, rimLeftDir, rimRightDir
                        )
                        val offsets = IntArray(6) { face -> face * faceBytes }
                        cubemap.setImage(
                            engine,
                            level,
                            Texture.PixelBufferDescriptor(buf, Texture.Format.RGBA, Texture.Type.UBYTE),
                            offsets
                        )
                    }
                }

                val indirectLight = IndirectLight.Builder()
                    .reflections(cubemap)
                    .irradiance(3, sh)
                    .intensity(42_000f)
                    .build(engine)

                return JudyStudioIbl(indirectLight, cubemap)
            } catch (t: Throwable) {
                Log.w(IBL_TAG, "Falling back to SH-only studio IndirectLight: ${t.message}")
                cubemap?.let { runCatching { engine.destroyTexture(it) } }
                val fallback = IndirectLight.Builder()
                    .irradiance(3, sh)
                    .intensity(44_000f)
                    .build(engine)
                return JudyStudioIbl(fallback, null)
            }
        }

        private fun buildCubemapLevelFloat(
            dim: Int,
            roughness: Float,
            keyBoxDir: FloatArray,
            fillBoxDir: FloatArray,
            rimLeftDir: FloatArray,
            rimRightDir: FloatArray
        ): ByteBuffer {
            val buf = ByteBuffer.allocateDirect(6 * dim * dim * 16).order(ByteOrder.nativeOrder())
            val floats = buf.asFloatBuffer()
            val rgb = FloatArray(3)
            for (face in 0 until 6) {
                for (y in 0 until dim) {
                    val v = ((y + 0.5f) / dim.toFloat()) * 2f - 1f
                    for (x in 0 until dim) {
                        val u = ((x + 0.5f) / dim.toFloat()) * 2f - 1f
                        sampleStudioDirection(
                            face, u, v, roughness,
                            keyBoxDir, fillBoxDir, rimLeftDir, rimRightDir,
                            rgb
                        )
                        floats.put(rgb[0])
                        floats.put(rgb[1])
                        floats.put(rgb[2])
                        floats.put(1.0f)
                    }
                }
            }
            buf.rewind()
            return buf
        }

        private fun buildCubemapLevelUByte(
            dim: Int,
            roughness: Float,
            keyBoxDir: FloatArray,
            fillBoxDir: FloatArray,
            rimLeftDir: FloatArray,
            rimRightDir: FloatArray
        ): ByteBuffer {
            val buf = ByteBuffer.allocateDirect(6 * dim * dim * 4).order(ByteOrder.nativeOrder())
            val rgb = FloatArray(3)
            for (face in 0 until 6) {
                for (y in 0 until dim) {
                    val v = ((y + 0.5f) / dim.toFloat()) * 2f - 1f
                    for (x in 0 until dim) {
                        val u = ((x + 0.5f) / dim.toFloat()) * 2f - 1f
                        sampleStudioDirection(
                            face, u, v, roughness,
                            keyBoxDir, fillBoxDir, rimLeftDir, rimRightDir,
                            rgb
                        )
                        // Filament decodes RGBA8 IBL reflections as RGBM (max range = 16.0)
                        val maxRgb = max(rgb[0], max(rgb[1], rgb[2]))
                        val mFloat = (maxRgb / 16.0f).coerceIn(1f / 255f, 1.0f)
                        val mByte = kotlin.math.ceil(mFloat * 255.0f).toInt().coerceIn(1, 255)
                        val scale = (mByte / 255.0f) * 16.0f
                        buf.put(toByte(rgb[0] / scale))
                        buf.put(toByte(rgb[1] / scale))
                        buf.put(toByte(rgb[2] / scale))
                        buf.put(mByte.toByte())
                    }
                }
            }
            buf.rewind()
            return buf
        }

        private fun toByte(value: Float): Byte =
            (value.coerceIn(0f, 1f) * 255f + 0.5f).toInt().coerceIn(0, 255).toByte()

        /**
         * Evaluates the pre-convolved analytical dual-catchlight softbox + dual rear rim-strip
         * studio environment for a given OpenGL cubemap face (0:+X, 1:-X, 2:+Y, 3:-Y, 4:+Z, 5:-Z).
         */
        private fun sampleStudioDirection(
            face: Int,
            u: Float,
            v: Float,
            roughness: Float,
            keyBoxDir: FloatArray,
            fillBoxDir: FloatArray,
            rimLeftDir: FloatArray,
            rimRightDir: FloatArray,
            outRgb: FloatArray
        ) {
            val rx: Float
            val ry: Float
            val rz: Float
            when (face) {
                0 -> { rx = 1f; ry = -v; rz = -u }     // +X
                1 -> { rx = -1f; ry = -v; rz = u }     // -X
                2 -> { rx = u; ry = 1f; rz = v }       // +Y
                3 -> { rx = u; ry = -1f; rz = -v }     // -Y
                4 -> { rx = u; ry = -v; rz = 1f }      // +Z
                else -> { rx = -u; ry = -v; rz = -1f } // -Z
            }
            val invLen = 1f / sqrt(rx * rx + ry * ry + rz * rz).coerceAtLeast(1e-6f)
            val dx = rx * invLen
            val dy = ry * invLen
            val dz = rz * invLen

            val r2 = roughness * roughness
            val broaden = 1f + r2 * 22f

            // Bright neutral studio dome gradient with soft upward skylight & clean floor bounce
            val skyT = ((dy + 1f) * 0.5f).coerceIn(0f, 1f)
            var r = 0.26f + 0.34f * skyT
            var g = 0.27f + 0.35f * skyT
            var b = 0.31f + 0.40f * skyT

            // 1. Primary upper-left crisp white studio softbox (primary jewel eye catchlight at mip 0)
            val dKey = max(0f, dx * keyBoxDir[0] + dy * keyBoxDir[1] + dz * keyBoxDir[2])
            val keySharpExp = max(2.0f, 96f / broaden)
            val keyAtten = 1f / (1f + r2 * 4.5f)
            val keySpark = dKey.pow(keySharpExp) * (15.5f * keyAtten) +
                exp(-((1f - dKey) * (16f / (1f + r2 * 6f)))) * (1.35f * keyAtten)
            r += keySpark * 1.02f
            g += keySpark * 1.00f
            b += keySpark * 0.98f

            // 2. Secondary upper-right studio softbox (smaller crisp secondary eye catchlight + wrap)
            val dFill = max(0f, dx * fillBoxDir[0] + dy * fillBoxDir[1] + dz * fillBoxDir[2])
            val fillSharpExp = max(2.0f, 128f / broaden)
            val fillAtten = 1f / (1f + r2 * 3.8f)
            val fillSpark = dFill.pow(fillSharpExp) * (10.5f * fillAtten) +
                dFill.pow(max(1.5f, 8f / (1f + r2 * 3.5f))) * 0.55f
            r += fillSpark * 0.96f
            g += fillSpark * 0.99f
            b += fillSpark * 1.06f

            // 3. Dual upper-back studio rim strips (crisp luminous separation on BOTH sides of silhouette)
            val dRimL = max(0f, dx * rimLeftDir[0] + dy * rimLeftDir[1] + dz * rimLeftDir[2])
            val dRimR = max(0f, dx * rimRightDir[0] + dy * rimRightDir[1] + dz * rimRightDir[2])
            val rimExp = max(1.8f, 22f / broaden)
            val rimAtten = 1f / (1f + r2 * 3.0f)
            val rimL = dRimL.pow(rimExp) * (5.6f * rimAtten)
            val rimR = dRimR.pow(rimExp) * (4.8f * rimAtten)
            r += (rimL + rimR) * 0.95f
            g += (rimL + rimR) * 0.99f
            b += (rimL + rimR) * 1.08f

            outRgb[0] = r
            outRgb[1] = g
            outRgb[2] = b
        }

        private fun normalize3(x: Float, y: Float, z: Float): FloatArray {
            val len = sqrt(x * x + y * y + z * z).coerceAtLeast(1e-6f)
            return floatArrayOf(x / len, y / len, z / len)
        }
    }
}
