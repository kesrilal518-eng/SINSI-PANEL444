package com.sinsi.panel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinsi.panel.ui.components.SinsiCard
import com.sinsi.panel.ui.components.SectionHeader
import com.sinsi.panel.ui.theme.*
import com.sinsi.panel.viewmodel.MainViewModel

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    var showResetConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlackBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "SETTINGS", color = NeonPurple, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        SinsiCard {
            SectionHeader("APPEARANCE")
            Text(text = "Dark Mode", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Text(text = "Always enabled - optimized for gaming environments", color = TextMuted, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        SinsiCard {
            SectionHeader("DATA")
            Text(text = "Reset Saved Profiles", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Text(
                text = "This permanently clears all locally saved generation history.",
                color = TextMuted,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )
            if (showResetConfirm) {
                Text(text = "Are you sure? This cannot be undone.", color = AccentRed, fontSize = 13.sp, modifier = Modifier.padding(bottom = 8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { viewModel.clearAllProfiles(); showResetConfirm = false }, colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(10.dp)) {
                        Text("Confirm Reset", fontWeight = FontWeight.Bold)
                    }
                    Button(onClick = { showResetConfirm = false }, colors = ButtonDefaults.buttonColors(containerColor = CardBorder), shape = RoundedCornerShape(10.dp)) {
                        Text("Cancel")
                    }
                }
            } else {
                Button(onClick = { showResetConfirm = true }, colors = ButtonDefaults.buttonColors(containerColor = AccentOrange), shape = RoundedCornerShape(10.dp)) {
                    Text("Clear All Profiles", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        SinsiCard {
            SectionHeader("ABOUT SINSI PANEL")
            Text(text = "SINSI PANEL", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = "Version 1.0.0", color = TextSecondary, fontSize = 13.sp, modifier = Modifier.padding(top = 2.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "A device-based sensitivity recommendation tool. Generates customized starting settings based on your hardware and play style.",
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        SinsiCard {
            SectionHeader("PRIVACY")
            Text(
                text = "All data stays on your device. Profiles are stored locally using DataStore. No accounts, no cloud sync, and no tracking.",
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}
