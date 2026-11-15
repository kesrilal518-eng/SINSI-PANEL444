package com.sinsi.panel.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sinsi.panel.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

private val Context.profileDataStore: DataStore<Preferences> by preferencesDataStore(name = "sinsi_profiles")

class ProfileDataStore(private val context: Context) {

    private val profilesKey = stringPreferencesKey("saved_profiles_json")

    val profilesFlow: Flow<List<SavedProfile>> = context.profileDataStore.data.map { prefs ->
        parseProfiles(prefs[profilesKey] ?: "[]")
    }

    suspend fun saveProfile(input: DeviceInput, result: SensitivityResult) {
        context.profileDataStore.edit { prefs ->
            val current = parseProfiles(prefs[profilesKey] ?: "[]")
            val newProfile = SavedProfile(
                id = UUID.randomUUID().toString(),
                timestamp = System.currentTimeMillis(),
                deviceInput = input,
                result = result
            )
            prefs[profilesKey] = serializeProfiles((listOf(newProfile) + current).take(20))
        }
    }

    suspend fun clearAll() {
        context.profileDataStore.edit { prefs -> prefs[profilesKey] = "[]" }
    }

    private fun serializeProfiles(list: List<SavedProfile>): String {
        val array = JSONArray()
        list.forEach { profile ->
            array.put(JSONObject().apply {
                put("id", profile.id)
                put("timestamp", profile.timestamp)
                put("phoneModel", profile.deviceInput.phoneModel)
                put("ram", profile.deviceInput.ram.name)
                put("screenSize", profile.deviceInput.screenSize.name)
                put("refreshRate", profile.deviceInput.refreshRate.name)
                put("gameFps", profile.deviceInput.gameFps.name)
                put("touchResponse", profile.deviceInput.touchResponse.name)
                put("playStyle", profile.deviceInput.playStyle.name)
                put("general", profile.result.general)
                put("redDot", profile.result.redDot)
                put("scope2x", profile.result.scope2x)
                put("scope4x", profile.result.scope4x)
                put("sniper", profile.result.sniper)
                put("freeLook", profile.result.freeLook)
                put("fireButton", profile.result.fireButtonPercent)
                put("dpiMin", profile.result.dpiMin)
                put("dpiMax", profile.result.dpiMax)
                put("deviceLabel", profile.result.deviceLabel)
                put("profileSummary", profile.result.profileSummary)
            })
        }
        return array.toString()
    }

    private fun parseProfiles(json: String): List<SavedProfile> {
        return try {
            val array = JSONArray(json)
            (0 until array.length()).mapNotNull { i ->
                val obj = array.getJSONObject(i)
                SavedProfile(
                    id = obj.getString("id"),
                    timestamp = obj.getLong("timestamp"),
                    deviceInput = DeviceInput(
                        phoneModel = obj.optString("phoneModel", ""),
                        ram = runCatching { RamOption.valueOf(obj.getString("ram")) }.getOrDefault(RamOption.GB_8),
                        screenSize = runCatching { ScreenSizeOption.valueOf(obj.getString("screenSize")) }.getOrDefault(ScreenSizeOption.MEDIUM),
                        refreshRate = runCatching { RefreshRateOption.valueOf(obj.getString("refreshRate")) }.getOrDefault(RefreshRateOption.HZ_120),
                        gameFps = runCatching { GameFpsOption.valueOf(obj.getString("gameFps")) }.getOrDefault(GameFpsOption.FPS_90),
                        touchResponse = runCatching { TouchResponseOption.valueOf(obj.getString("touchResponse")) }.getOrDefault(TouchResponseOption.MEDIUM),
                        playStyle = runCatching { PlayStyleOption.valueOf(obj.getString("playStyle")) }.getOrDefault(PlayStyleOption.BALANCED)
                    ),
                    result = SensitivityResult(
                        general = obj.getInt("general"),
                        redDot = obj.getInt("redDot"),
                        scope2x = obj.getInt("scope2x"),
                        scope4x = obj.getInt("scope4x"),
                        sniper = obj.getInt("sniper"),
                        freeLook = obj.getInt("freeLook"),
                        fireButtonPercent = obj.getInt("fireButton"),
                        dpiMin = obj.optInt("dpiMin", 400),
                        dpiMax = obj.optInt("dpiMax", 480),
                        deviceLabel = obj.optString("deviceLabel", ""),
                        profileSummary = obj.optString("profileSummary", "")
                    )
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }
}
