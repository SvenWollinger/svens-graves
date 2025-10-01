package io.wollinger.graves.config

import kotlinx.serialization.json.Json
import net.minecraft.entity.player.PlayerEntity
import java.io.File

object ConfigManager {
    private val json = Json {
        encodeDefaults = true
        prettyPrint = true
    }
    private val configFolder = File("config").apply { mkdirs() }
    private val modConfigFile = File(configFolder, "svens-graves.json")

    var config: ModConfig

    init {
        if(modConfigFile.exists()) {
            config = json.decodeFromString(modConfigFile.readText())
        } else {
            config = ModConfig()
            val encoded = json.encodeToString(config)
            modConfigFile.writeText(encoded)
        }
    }

    fun getConfigSetting(player: PlayerEntity): GraveSetting {
        return config.player_settings[player.uuidAsString] ?: config.global_setting
    }
}