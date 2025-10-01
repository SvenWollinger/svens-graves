package io.wollinger.graves

import net.minecraft.entity.Entity
import net.minecraft.entity.decoration.InteractionEntity
import net.minecraft.text.Text

object EntityTickHelper {
    fun tick(entity: Entity) {
        if(entity !is InteractionEntity || !GraveUtils.isGrave(entity)) {
            return
        }
        var health = EntityAccessorHelper.readInt(entity, CustomDataKeys.GRAVE_HEALTH) ?: return
        health -= 1
        if(health < 0) {
            GraveUtils.openGrave(grave = entity)
        } else {
            entity.customName = Text.literal("${(health / 20) + 1}s")
        }
        EntityAccessorHelper.writeInt(entity = entity, key = CustomDataKeys.GRAVE_HEALTH, data = health)
    }
}