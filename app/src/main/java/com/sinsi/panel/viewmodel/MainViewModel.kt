package com.sinsi.panel.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sinsi.panel.data.local.ProfileDataStore
import com.sinsi.panel.data.model.*
import com.sinsi.panel.domain.SensitivityEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val phoneModel: String = "",
    val ram: RamOption = RamOption.GB_8,
    val screenSize: ScreenSizeOption = ScreenSizeOption.MEDIUM,
    val refreshRate: RefreshRateOption = RefreshRateOption.HZ_120,
    val gameFps: GameFpsOption = GameFpsOption.FPS_90,
    val touchResponse: TouchResponseOption = TouchResponseOption.MEDIUM,
    val playStyle: PlayStyleOption = PlayStyleOption.BALANCED,
    val result: SensitivityResult? = null,
    val isGenerating: Boolean = false,
    val showResult: Boolean = false,
    val errorMessage: String? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = ProfileDataStore(application)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val savedProfiles: StateFlow<List<SavedProfile>> = dataStore.profilesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updatePhoneModel(value: String) {
        _uiState.value = _uiState.value.copy(phoneModel = value, errorMessage = null)
    }

    fun updateRam(value: RamOption) {
        _uiState.value = _uiState.value.copy(ram = value)
    }

    fun updateScreenSize(value: ScreenSizeOption) {
        _uiState.value = _uiState.value.copy(screenSize = value)
    }

    fun updateRefreshRate(value: RefreshRateOption) {
        _uiState.value = _uiState.value.copy(refreshRate = value)
    }

    fun updateGameFps(value: GameFpsOption) {
        _uiState.value = _uiState.value.copy(gameFps = value)
    }

    fun updateTouchResponse(value: TouchResponseOption) {
        _uiState.value = _uiState.value.copy(touchResponse = value)
    }

    fun updatePlayStyle(value: PlayStyleOption) {
        _uiState.value = _uiState.value.copy(playStyle = value)
    }

    fun generate() {
        val state = _uiState.value
        if (state.phoneModel.trim().isEmpty()) {
            _uiState.value = state.copy(errorMessage = "Please enter your phone model")
            return
        }

        _uiState.value = state.copy(isGenerating = true, errorMessage = null)

        viewModelScope.launch {
            val input = DeviceInput(
                phoneModel = state.phoneModel.trim(),
                ram = state.ram,
                screenSize = state.screenSize,
                refreshRate = state.refreshRate,
                gameFps = state.gameFps,
                touchResponse = state.touchResponse,
                playStyle = state.playStyle
            )
            val result = SensitivityEngine.generate(input)
            dataStore.saveProfile(input, result)
            _uiState.value = _uiState.value.copy(
                result = result,
                isGenerating = false,
                showResult = true
            )
        }
    }

    fun hideResult() {
        _uiState.value = _uiState.value.copy(showResult = false)
    }

    fun copySettings(context: Context) {
        val result = _uiState.value.result ?: return
        val text = buildShareText(result)
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("SINSI PANEL Settings", text))
    }

    fun shareSettings(context: Context) {
        val result = _uiState.value.result ?: return
        val text = buildShareText(result)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "SINSI PANEL Settings")
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(intent, "Share Settings"))
    }

    fun clearAllProfiles() {
        viewModelScope.launch {
            dataStore.clearAll()
        }
    }

    private fun buildShareText(result: SensitivityResult): String {
        return buildString {
            appendLine("SINSI PANEL SETTINGS")
            appendLine()
            appendLine("Device: ${result.deviceLabel}")
            appendLine(result.profileSummary)
            appendLine()
            appendLine("General: ${result.general}")
            appendLine("Red Dot: ${result.redDot}")
            appendLine("2X Scope: ${result.scope2x}")
            appendLine("4X Scope: ${result.scope4x}")
            appendLine("Sniper: ${result.sniper}")
            appendLine("Free Look: ${result.freeLook}")
            appendLine("Fire Button: ${result.fireButtonPercent}%")
            appendLine()
            appendLine("DPI Guidance: ${result.dpiMin}-${result.dpiMax}")
            appendLine()
            appendLine("Generated as recommended starting settings.")
            appendLine("Test and adjust to your comfort.")
        }
    }
}
