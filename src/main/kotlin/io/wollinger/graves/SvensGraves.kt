package io.wollinger.graves

import io.wollinger.graves.config.ConfigManager
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.minecraft.entity.decoration.InteractionEntity
import net.minecraft.item.Items
import net.minecraft.nbt.NbtList
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import org.slf4j.LoggerFactory
import java.io.File

object SvensGraves : ModInitializer {
    private val logger = LoggerFactory.getLogger("svens-graves")

	override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        ConfigManager

		UseEntityCallback.EVENT.register { player, world, hand, entity, hitResult ->
			if(entity is InteractionEntity && GraveUtils.isGrave(entity)) {
				GraveUtils.interactGrave(player, entity)
                ActionResult.SUCCESS
			}
			ActionResult.PASS
		}

        ServerLivingEntityEvents.ALLOW_DEATH.register { entity, damageSource, damageAmount ->
            if(entity !is ServerPlayerEntity) return@register true

            val graveItems = NbtList()
            entity.inventory.filter { itemStack -> itemStack.item != Items.AIR }.forEach { itemStack ->
                val nbtData = ItemSerializer.serialize(itemStack)
                graveItems.add(nbtData)
            }
            entity.inventory.clear()
            GraveUtils.spawnGrave(entity, entity.blockPos, entity.world, graveItems)
            entity.sendMessage(Text.literal("Oh no! You died! Your Grave will be around ${entity.blockPos.toShortString()}"))
            true
        }
	}
}