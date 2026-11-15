package com.sinsi.panel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sinsi.panel.data.model.SavedProfile
import com.sinsi.panel.ui.components.SinsiCard
import com.sinsi.panel.ui.theme.*
import com.sinsi.panel.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfilesScreen(viewModel: MainViewModel) {
    val profiles by viewModel.savedProfiles.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(BlackBackground).padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "SAVED PROFILES", color = NeonPurple, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(text = "Your recent sensitivity generations", color = TextSecondary, fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp, bottom = 16.dp))

        if (profiles.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(top = 60.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "No profiles yet", color = TextSecondary, fontSize = 16.sp)
                    Text(text = "Generate settings on the Home tab", color = TextMuted, fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 100.dp)) {
                items(profiles, key = { it.id }) { profile -> ProfileCard(profile) }
            }
        }
    }
}

@Composable
private fun ProfileCard(profile: SavedProfile) {
    val dateStr = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(profile.timestamp))
    SinsiCard {
        Text(text = profile.result.deviceLabel, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(text = profile.result.profileSummary, color = TextSecondary, fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
        Text(text = dateStr, color = TextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp, bottom = 10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            MiniStat("GEN", "${profile.result.general}")
            MiniStat("RD", "${profile.result.redDot}")
            MiniStat("2X", "${profile.result.scope2x}")
            MiniStat("4X", "${profile.result.scope4x}")
            MiniStat("SNP", "${profile.result.sniper}")
            MiniStat("FL", "${profile.result.freeLook}")
        }
    }
}

@Composable
private fun MiniStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = TextMuted, fontSize = 10.sp)
        Text(text = value, color = ElectricGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
