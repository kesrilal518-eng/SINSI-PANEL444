package com.sinsi.panel.domain

import com.sinsi.panel.data.model.*

/**
 * Transparent, deterministic sensitivity recommendation engine.
 * Same inputs always produce the same outputs.
 * No claims of guaranteed results - these are starting points only.
 */
object SensitivityEngine {

    private const val BASE_GENERAL = 92
    private const val BASE_RED_DOT = 88
    private const val BASE_2X = 80
    private const val BASE_4X = 70
    private const val BASE_SNIPER = 48
    private const val BASE_FREE_LOOK = 70
    private const val BASE_FIRE_BUTTON = 52

    private const val MIN_SENS = 20
    private const val MAX_SENS = 200
    private const val MIN_FIRE = 30
    private const val MAX_FIRE = 100

    private val knownDevices = listOf(
        KnownDeviceProfile(
            keywords = listOf("realme", "p4", "pro"),
            baseOffsetGeneral = 2,
            baseOffsetRedDot = 1,
            baseOffsetScopes = 0,
            recommendedDpiMin = 420,
            recommendedDpiMax = 500
        ),
        KnownDeviceProfile(
            keywords = listOf("samsung", "s23", "s24", "s25"),
            baseOffsetGeneral = 1,
            baseOffsetRedDot = 2,
            baseOffsetScopes = 1,
            recommendedDpiMin = 400,
            recommendedDpiMax = 480
        ),
        KnownDeviceProfile(
            keywords = listOf("xiaomi", "redmi", "poco"),
            baseOffsetGeneral = 3,
            baseOffsetRedDot = 2,
            baseOffsetScopes = 1,
            recommendedDpiMin = 430,
            recommendedDpiMax = 510
        ),
        KnownDeviceProfile(
            keywords = listOf("oneplus", "nord"),
            baseOffsetGeneral = 1,
            baseOffsetRedDot = 1,
            baseOffsetScopes = 0,
            recommendedDpiMin = 410,
            recommendedDpiMax = 490
        ),
        KnownDeviceProfile(
            keywords = listOf("iqoo", "vivo"),
            baseOffsetGeneral = 2,
            baseOffsetRedDot = 2,
            baseOffsetScopes = 1,
            recommendedDpiMin = 420,
            recommendedDpiMax = 500
        ),
        KnownDeviceProfile(
            keywords = listOf("oppo", "find", "reno"),
            baseOffsetGeneral = 1,
            baseOffsetRedDot = 0,
            baseOffsetScopes = 0,
            recommendedDpiMin = 400,
            recommendedDpiMax = 480
        ),
        KnownDeviceProfile(
            keywords = listOf("pixel", "google"),
            baseOffsetGeneral = 0,
            baseOffsetRedDot = 0,
            baseOffsetScopes = -1,
            recommendedDpiMin = 390,
            recommendedDpiMax = 460
        ),
        KnownDeviceProfile(
            keywords = listOf("nothing", "phone"),
            baseOffsetGeneral = 1,
            baseOffsetRedDot = 1,
            baseOffsetScopes = 0,
            recommendedDpiMin = 400,
            recommendedDpiMax = 470
        )
    )

    fun generate(input: DeviceInput): SensitivityResult {
        val normalizedName = input.phoneModel.trim().lowercase()
        val matchedProfile = findKnownProfile(normalizedName)

        var general = BASE_GENERAL
        var redDot = BASE_RED_DOT
        var scope2x = BASE_2X
        var scope4x = BASE_4X
        var sniper = BASE_SNIPER
        var freeLook = BASE_FREE_LOOK
        var fireButton = BASE_FIRE_BUTTON
        var dpiMin = 400
        var dpiMax = 480

        matchedProfile?.let { profile ->
            general += profile.baseOffsetGeneral
            redDot += profile.baseOffsetRedDot
            scope2x += profile.baseOffsetScopes
            scope4x += profile.baseOffsetScopes
            sniper += profile.baseOffsetScopes / 2
            dpiMin = profile.recommendedDpiMin
            dpiMax = profile.recommendedDpiMax
        }

        when (input.ram) {
            RamOption.GB_4 -> { general -= 4; redDot -= 3; fireButton -= 2 }
            RamOption.GB_6 -> { general -= 2; redDot -= 1 }
            RamOption.GB_8 -> { }
            RamOption.GB_12 -> { general += 2; redDot += 1 }
            RamOption.GB_16, RamOption.GB_24 -> { general += 3; redDot += 2; freeLook += 2 }
        }

        when (input.refreshRate) {
            RefreshRateOption.HZ_60 -> { general -= 3; redDot -= 2 }
            RefreshRateOption.HZ_90 -> { general -= 1 }
            RefreshRateOption.HZ_120 -> { }
            RefreshRateOption.HZ_144 -> { general += 3; redDot += 2; freeLook += 2 }
        }

        when (input.gameFps) {
            GameFpsOption.FPS_30 -> { general -= 5; redDot -= 4; scope2x -= 3 }
            GameFpsOption.FPS_60 -> { general -= 2; redDot -= 1 }
            GameFpsOption.FPS_90 -> { }
            GameFpsOption.FPS_120 -> { general += 2; redDot += 1 }
            GameFpsOption.FPS_144 -> { general += 4; redDot += 3; freeLook += 2 }
        }

        when (input.screenSize) {
            ScreenSizeOption.SMALL -> { general += 3; redDot += 2; fireButton -= 4 }
            ScreenSizeOption.MEDIUM -> { }
            ScreenSizeOption.LARGE -> { general -= 2; redDot -= 1; fireButton += 5 }
        }

        when (input.touchResponse) {
            TouchResponseOption.LOW -> { general -= 3; redDot -= 2; freeLook -= 2 }
            TouchResponseOption.MEDIUM -> { }
            TouchResponseOption.HIGH -> { general += 3; redDot += 2; freeLook += 3 }
        }

        when (input.playStyle) {
            PlayStyleOption.BALANCED -> { }
            PlayStyleOption.RUSH -> { general += 5; redDot += 4; scope2x += 2; freeLook += 3; fireButton += 2 }
            PlayStyleOption.DRAG -> { general += 4; redDot += 3; scope2x += 1; freeLook += 2 }
            PlayStyleOption.PRECISION -> { general -= 3; redDot -= 2; scope4x += 3; sniper += 4; freeLook -= 1 }
        }

        general = general.coerceIn(MIN_SENS, MAX_SENS)
        redDot = redDot.coerceIn(MIN_SENS, MAX_SENS)
        scope2x = scope2x.coerceIn(MIN_SENS, MAX_SENS)
        scope4x = scope4x.coerceIn(MIN_SENS, MAX_SENS)
        sniper = sniper.coerceIn(MIN_SENS, MAX_SENS)
        freeLook = freeLook.coerceIn(MIN_SENS, MAX_SENS)
        fireButton = fireButton.coerceIn(MIN_FIRE, MAX_FIRE)

        val displayName = input.phoneModel.trim().ifBlank { "Unknown Device" }
        val summary = buildString {
            append(input.ram.display)
            append(" - ")
            append(input.refreshRate.display)
            append(" - ")
            append(input.gameFps.display)
        }

        return SensitivityResult(
            general = general,
            redDot = redDot,
            scope2x = scope2x,
            scope4x = scope4x,
            sniper = sniper,
            freeLook = freeLook,
            fireButtonPercent = fireButton,
            dpiMin = dpiMin,
            dpiMax = dpiMax,
            deviceLabel = displayName,
            profileSummary = summary
        )
    }

    private fun findKnownProfile(normalizedName: String): KnownDeviceProfile? {
        if (normalizedName.isBlank()) return null
        return knownDevices.firstOrNull { profile ->
            profile.keywords.all { keyword -> normalizedName.contains(keyword) } ||
                profile.keywords.any { keyword -> normalizedName.contains(keyword) && keyword.length > 3 }
        }
    }
}
