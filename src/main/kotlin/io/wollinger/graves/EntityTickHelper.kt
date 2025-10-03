package io.wollinger.graves

import net.minecraft.entity.Entity
import net.minecraft.entity.decoration.InteractionEntity
import net.minecraft.text.Text

object EntityTickHelper {
    fun tickGrave(entity: Entity) {
        //Entity is either not an InteractionEntity or not a Grave. Return early if not an InteractionEntity
        if(entity !is InteractionEntity || !GraveUtils.isGrave(entity)) {
            return
        }
        var health = EntityAccessorHelper.readInt(entity, CustomDataKeys.GRAVE_HEALTH) ?: return
        health -= 1
        if(health < 0) {
            GraveUtils.openGrave(grave = entity)
        } else {
            EntityAccessorHelper.writeInt(entity = entity, key = CustomDataKeys.GRAVE_HEALTH, data = health)
            entity.customName = Text.literal(Utils.formatTime((health / 20) + 1))
        }
    }
}