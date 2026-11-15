package com.sinsi.panel.data.model

/**
 * All data models for SINSI PANEL.
 * Completely original implementation.
 */

enum class RamOption(val display: String, val valueGb: Int) {
    GB_4("4 GB", 4),
    GB_6("6 GB", 6),
    GB_8("8 GB", 8),
    GB_12("12 GB", 12),
    GB_16("16 GB", 16),
    GB_24("24 GB", 24)
}

enum class ScreenSizeOption(val display: String) {
    SMALL("Small (Below 6.3 inch)"),
    MEDIUM("Medium (6.3-6.7 inch)"),
    LARGE("Large (Above 6.7 inch)")
}

enum class RefreshRateOption(val display: String, val hz: Int) {
    HZ_60("60 Hz", 60),
    HZ_90("90 Hz", 90),
    HZ_120("120 Hz", 120),
    HZ_144("144 Hz", 144)
}

enum class GameFpsOption(val display: String, val fps: Int) {
    FPS_30("30 FPS", 30),
    FPS_60("60 FPS", 60),
    FPS_90("90 FPS", 90),
    FPS_120("120 FPS", 120),
    FPS_144("144 FPS", 144)
}

enum class TouchResponseOption(val display: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High")
}

enum class PlayStyleOption(val display: String) {
    BALANCED("Balanced"),
    RUSH("Rush"),
    DRAG("Drag"),
    PRECISION("Precision")
}

data class DeviceInput(
    val phoneModel: String = "",
    val ram: RamOption = RamOption.GB_8,
    val screenSize: ScreenSizeOption = ScreenSizeOption.MEDIUM,
    val refreshRate: RefreshRateOption = RefreshRateOption.HZ_120,
    val gameFps: GameFpsOption = GameFpsOption.FPS_90,
    val touchResponse: TouchResponseOption = TouchResponseOption.MEDIUM,
    val playStyle: PlayStyleOption = PlayStyleOption.BALANCED
)

data class SensitivityResult(
    val general: Int,
    val redDot: Int,
    val scope2x: Int,
    val scope4x: Int,
    val sniper: Int,
    val freeLook: Int,
    val fireButtonPercent: Int,
    val dpiMin: Int,
    val dpiMax: Int,
    val deviceLabel: String,
    val profileSummary: String
)

data class SavedProfile(
    val id: String,
    val timestamp: Long,
    val deviceInput: DeviceInput,
    val result: SensitivityResult
)

data class KnownDeviceProfile(
    val keywords: List<String>,
    val baseOffsetGeneral: Int = 0,
    val baseOffsetRedDot: Int = 0,
    val baseOffsetScopes: Int = 0,
    val recommendedDpiMin: Int = 400,
    val recommendedDpiMax: Int = 480
)
