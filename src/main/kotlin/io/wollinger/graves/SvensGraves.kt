package io.wollinger.graves

import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.JsonOps
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.minecraft.component.type.NbtComponent
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.decoration.InteractionEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtHelper
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtOps
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import org.slf4j.LoggerFactory
import java.util.UUID

object SvensGraves : ModInitializer {
    private val logger = LoggerFactory.getLogger("svens-graves")

	val MOD_ID = "svens-graves"

	override fun onInitialize() {
		UseEntityCallback.EVENT.register { player, world, hand, entity, hitResult ->
			if(entity is InteractionEntity) {
				GraveSpawner.killGrave(player, entity)


			}
			ActionResult.SUCCESS
		}

		ServerLivingEntityEvents.ALLOW_DEATH.register(object: ServerLivingEntityEvents.AllowDeath {
			override fun allowDeath(
				entity: LivingEntity?,
				damageSource: DamageSource?,
				damageAmount: Float
			): Boolean {
				if(entity !is ServerPlayerEntity) return true
				println("Death Event! Entity: $entity, Damage Source: $damageSource, Damage Amount: $damageAmount")

				val inventoryStuff = NbtList()

				entity.inventory.filter { itemStack -> itemStack.item != Items.AIR }.forEach { itemStack ->
					val nbtData = ItemSerializer.serialize(itemStack)
					inventoryStuff.add(nbtData)
				}
				entity.inventory.clear()
				GraveSpawner.spawnGrave(entity, entity.blockPos, entity.world, inventoryStuff)
				entity.sendMessage(Text.literal("Oh no! You died! Your Grave will be around ${entity.blockPos.toShortString()}"))
				return true
			}

		})
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger.info("Hello Fabric world!")
	}
}