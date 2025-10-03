package io.wollinger.graves.config

import kotlinx.serialization.Serializable

@Serializable
data class ModConfig(
    val global_setting: GraveSetting = GraveSetting(),
    val player_settings: HashMap<String, GraveSetting> = hashMapOf(
        "6694e431-4128-4bce-95f0-f7b359e8de14" to GraveSetting(
            grave_label = "A rodents Grave (%PLAYER_NAME%).",
            grave_lifetime = 20 * 60 * 60,
            locked_to_owner = true
        )
    )
)

@Serializable
data class GraveSetting(
    val grave_label: String = "%PLAYER_NAME%'s Grave",
    val grave_lifetime: Int = 20 * 60 * 20,
    val locked_to_owner: Boolean = true
)
