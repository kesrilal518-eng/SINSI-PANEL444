package com.sinsi.panel.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinsi.panel.data.model.*
import com.sinsi.panel.ui.components.*
import com.sinsi.panel.ui.theme.*
import com.sinsi.panel.viewmodel.MainViewModel

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlackBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(text = "SINSI PANEL", color = NeonPurple, fontSize = 26.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "DEVICE-BASED SENSITIVITY GENERATOR", color = ElectricGreen, fontSize = 11.sp, fontWeight = FontWeight.Medium, letterSpacing = 1.5.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Find your recommended starting settings", color = TextSecondary, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        SinsiCard {
            SectionHeader("DEVICE DETAILS")
            Text(text = "Phone Model", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 6.dp))
            OutlinedTextField(
                value = state.phoneModel,
                onValueChange = { viewModel.updatePhoneModel(it) },
                placeholder = { Text("Example: Realme P4 Pro", color = TextMuted) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = NeonPurple,
                    unfocusedBorderColor = CardBorder,
                    cursorColor = NeonPurple,
                    focusedContainerColor = SurfaceDark,
                    unfocusedContainerColor = SurfaceDark
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))
            DropdownField(label = "RAM", selected = state.ram.display, options = RamOption.entries.map { it.display }, onSelect = { s -> RamOption.entries.find { it.display == s }?.let { viewModel.updateRam(it) } })
            Spacer(modifier = Modifier.height(12.dp))
            DropdownField(label = "Screen Size", selected = state.screenSize.display, options = ScreenSizeOption.entries.map { it.display }, onSelect = { s -> ScreenSizeOption.entries.find { it.display == s }?.let { viewModel.updateScreenSize(it) } })
            Spacer(modifier = Modifier.height(12.dp))
            DropdownField(label = "Refresh Rate", selected = state.refreshRate.display, options = RefreshRateOption.entries.map { it.display }, onSelect = { s -> RefreshRateOption.entries.find { it.display == s }?.let { viewModel.updateRefreshRate(it) } })
            Spacer(modifier = Modifier.height(12.dp))
            DropdownField(label = "Game FPS", selected = state.gameFps.display, options = GameFpsOption.entries.map { it.display }, onSelect = { s -> GameFpsOption.entries.find { it.display == s }?.let { viewModel.updateGameFps(it) } })
            Spacer(modifier = Modifier.height(12.dp))
            DropdownField(label = "Touch Response", selected = state.touchResponse.display, options = TouchResponseOption.entries.map { it.display }, onSelect = { s -> TouchResponseOption.entries.find { it.display == s }?.let { viewModel.updateTouchResponse(it) } })
            Spacer(modifier = Modifier.height(12.dp))
            DropdownField(label = "Play Style", selected = state.playStyle.display, options = PlayStyleOption.entries.map { it.display }, onSelect = { s -> PlayStyleOption.entries.find { it.display == s }?.let { viewModel.updatePlayStyle(it) } })
        }

        state.errorMessage?.let { msg ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = msg, color = AccentRed, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 4.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))
        GenerateButton(onClick = { viewModel.generate() }, isLoading = state.isGenerating)
        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(visible = state.showResult && state.result != null, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
            state.result?.let { result ->
                Column {
                    SinsiCard {
                        SectionHeader("DEVICE PROFILE")
                        Text(text = result.deviceLabel, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = result.profileSummary, color = TextSecondary, fontSize = 13.sp, modifier = Modifier.padding(top = 2.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    SinsiCard {
                        Text(text = "Recommended Starting Settings", color = ElectricGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))
                        ResultValueCard(icon = "G", title = "General Sensitivity", value = "${result.general}", accentColor = NeonPurple)
                        Spacer(modifier = Modifier.height(8.dp))
                        ResultValueCard(icon = "R", title = "Red Dot", value = "${result.redDot}", accentColor = AccentRed)
                        Spacer(modifier = Modifier.height(8.dp))
                        ResultValueCard(icon = "2", title = "2X Scope", value = "${result.scope2x}", accentColor = ElectricGreen)
                        Spacer(modifier = Modifier.height(8.dp))
                        ResultValueCard(icon = "4", title = "4X Scope", value = "${result.scope4x}", accentColor = ElectricGreen)
                        Spacer(modifier = Modifier.height(8.dp))
                        ResultValueCard(icon = "S", title = "Sniper Scope", value = "${result.sniper}", accentColor = NeonPurple)
                        Spacer(modifier = Modifier.height(8.dp))
                        ResultValueCard(icon = "F", title = "Free Look", value = "${result.freeLook}", accentColor = NeonPurple)
                        Spacer(modifier = Modifier.height(8.dp))
                        ResultValueCard(icon = "B", title = "Fire Button Size", value = "${result.fireButtonPercent}%", accentColor = AccentOrange)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    SinsiCard {
                        SectionHeader("DPI GUIDANCE")
                        Text(text = "Recommended range: ${result.dpiMin}-${result.dpiMax}", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Changing DPI is optional. Test your device default settings first. Device support varies.", color = TextMuted, fontSize = 12.sp, lineHeight = 17.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(onClick = { viewModel.copySettings(context) }, colors = ButtonDefaults.buttonColors(containerColor = NeonPurple), shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)) {
                            Text("COPY", fontWeight = FontWeight.Bold)
                        }
                        Button(onClick = { viewModel.shareSettings(context) }, colors = ButtonDefaults.buttonColors(containerColor = ElectricGreenDim), shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)) {
                            Text("SHARE", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Recommendations are starting points only. Always test in training mode and adjust to your comfort. Results are not guaranteed.",
            color = TextMuted,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Spacer(modifier = Modifier.height(100.dp))
    }
}
