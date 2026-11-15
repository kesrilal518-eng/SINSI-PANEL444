package com.sinsi.panel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinsi.panel.ui.components.SinsiCard
import com.sinsi.panel.ui.theme.*

@Composable
fun TipsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlackBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "TIPS & GUIDANCE", color = NeonPurple, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "Improve your aim with smart practice",
            color = TextSecondary,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )
        TipCard(
            title = "Aim Practice",
            body = "Sensitivity should always be tested in training mode before ranked matches. Adjust in small steps until your aim feels natural and consistent."
        )
        Spacer(modifier = Modifier.height(12.dp))
        TipCard(
            title = "Drag Technique",
            body = "Use short, controlled upward finger movements rather than long swipes to maintain accuracy while spraying."
        )
        Spacer(modifier = Modifier.height(12.dp))
        TipCard(
            title = "Finding Your Settings",
            body = "Change only a few values at a time. If General feels high, lower it by 3-5 points and re-test."
        )
        Spacer(modifier = Modifier.height(12.dp))
        TipCard(
            title = "Performance",
            body = "Use stable graphics and FPS settings that your device can maintain without dropping frames."
        )
        Spacer(modifier = Modifier.height(12.dp))
        TipCard(
            title = "DPI Notes",
            body = "Changing system DPI is optional. Always test your device default settings first. Device support varies."
        )
        Spacer(modifier = Modifier.height(20.dp))
        SinsiCard {
            Text(text = "Important Reminder", color = AccentOrange, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "These recommendations are starting points only. No sensitivity setting guarantees headshots or wins. Adjust everything to what feels right for you.",
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun TipCard(title: String, body: String) {
    SinsiCard {
        Text(text = title, color = ElectricGreen, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = body, color = TextSecondary, fontSize = 13.sp, lineHeight = 19.sp)
    }
}
