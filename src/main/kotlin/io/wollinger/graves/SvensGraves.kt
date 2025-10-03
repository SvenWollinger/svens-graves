package io.wollinger.graves

import io.wollinger.graves.config.ConfigManager
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.entity.decoration.InteractionEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.Items
import net.minecraft.nbt.NbtList
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.HopperScreenHandler
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

        UseItemCallback.EVENT.register { player, v2, v3 ->
            val nbtString = ItemSerializer.serialize(player.inventory.selectedStack).toString()
            player.sendMessage(Text.literal(nbtString), false)
            ActionResult.PASS
        }

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
            val spawnResult = GraveUtils.spawnGrave(entity, entity.blockPos, entity.world, graveItems)

            val deathMessage = ConfigManager.langConfig.death_message
                .replace("%GRAVE_POSITION%", entity.blockPos.toShortString())
                .replace("%GRAVE_OPEN_TIME%", Utils.formatTime((spawnResult.graveTicks / 20) + 1))

            entity.sendMessage(Text.literal(deathMessage))
            true
        }
	}
}