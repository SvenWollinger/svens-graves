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
    private val modConfigFolder = File(configFolder, "svens-graves").apply { mkdirs() }
    private val modConfigFile = File(modConfigFolder, "svens-graves.json")
    private val langConfigFile = File(modConfigFolder, "svens-graves-lang.json")

    var config: ModConfig = if(modConfigFile.exists()) json.decodeFromString(modConfigFile.readText()) else ModConfig()
    var langConfig: LangConfig = if(langConfigFile.exists()) json.decodeFromString(langConfigFile.readText()) else LangConfig()

    init {
        //Write defaults or new missing values
        modConfigFile.writeText(json.encodeToString(config))
        langConfigFile.writeText(json.encodeToString(langConfig))
    }

    fun getConfigSetting(player: PlayerEntity): GraveSetting {
        return config.player_settings[player.uuidAsString] ?: config.global_setting
    }
}