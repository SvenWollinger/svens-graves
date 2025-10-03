package io.wollinger.graves.config

import kotlinx.serialization.Serializable

@Serializable
data class LangConfig(
    val death_message: String = "You died! Your grave will be at %GRAVE_POSITION% and will open in %GRAVE_OPEN_TIME%.",
    val not_owner_message: String = "This grave does not belong to you! It belongs to %GRAVE_OWNER%.",
    val not_owner_message_unknown: String = "This grave does not belong to you!"
)
