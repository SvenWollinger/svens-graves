package io.wollinger.graves.config

import kotlinx.serialization.Serializable

@Serializable
data class ModConfig(
    var global_setting: GraveSetting = GraveSetting(),
    var player_settings: HashMap<String, GraveSetting> = hashMapOf(
        Pair("6694e431-4128-4bce-95f0-f7b359e8de14", GraveSetting("A rodents Grave (%PLAYER_NAME%).", 20 * 60 * 60, true))
    )
)

@Serializable
data class GraveSetting(
    var grave_label: String = "%PLAYER_NAME%'s Grave",
    var grave_lifetime: Int = 20 * 60 * 20,
    var locked_to_owner: Boolean = true
)
