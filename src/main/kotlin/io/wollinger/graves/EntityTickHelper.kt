package io.wollinger.graves

import net.minecraft.entity.Entity
import net.minecraft.entity.decoration.InteractionEntity
import net.minecraft.text.Text

object EntityTickHelper {
    fun tick(entity: Entity) {
        if(entity !is InteractionEntity) {
            return
        }
        var health = EntityAccessorHelper.readInt(entity, CustomDataKeys.GRAVE_HEALTH) ?: return
        health -= 1
        if(health < 0) {
            GraveSpawner.killGrave(null, entity)
        } else {
            entity.customName = Text.literal(health.toString())
        }
        EntityAccessorHelper.writeInt(entity, CustomDataKeys.GRAVE_HEALTH, health)
    }
}