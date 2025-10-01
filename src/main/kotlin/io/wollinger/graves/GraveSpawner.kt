package io.wollinger.graves

import io.wollinger.graves.mixin.EntityAccessor
import net.minecraft.block.Blocks
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.component.ComponentType
import net.minecraft.component.type.NbtComponent
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.decoration.DisplayEntity
import net.minecraft.entity.decoration.InteractionEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.storage.NbtReadView
import net.minecraft.storage.ReadView
import net.minecraft.storage.WriteView
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.math.AffineTransformation
import net.minecraft.util.math.AffineTransformations
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import org.joml.Vector3d
import org.joml.Vector3f
import java.util.UUID

object GraveSpawner {
    fun spawnGrave(player: PlayerEntity, location: BlockPos, world: World, content: NbtList) {
        val chestEntity = DisplayEntity.BlockDisplayEntity(EntityType.BLOCK_DISPLAY, world)
        val interactionEntity = InteractionEntity(EntityType.INTERACTION, world)

        //Position
        chestEntity.setPosition(location.toCenterPos())

        //Block State
        chestEntity.blockState = Blocks.CHEST.defaultState

        //Bounding Box (Doesn't work?)
        chestEntity.boundingBox = Box.of(location.toCenterPos(), 2.0, 2.0, 2.0)

        //Display Name
        chestEntity.customName = Text.literal("${player.name.literalString}'s Grave")
        chestEntity.isCustomNameVisible = true

        //Visual Position & Scaling
        val visualPosition = Vector3f(-0.4f, -0.5f, -0.4f)
        val visualScale = Vector3f(0.8f, 0.8f, 0.8f)
        val transformation = AffineTransformation(visualPosition, null, visualScale, null)
        chestEntity.setTransformation(transformation)

        EntityAccessorHelper.writeString(chestEntity, CustomDataKeys.GRAVE_CONNECTION_INTERACTION_UUID, interactionEntity.uuidAsString)

        world.spawnEntity(chestEntity)

        interactionEntity.setPosition(location.toCenterPos().subtract(0.0, 0.5, 0.0))
        interactionEntity.interactionWidth = 0.7f
        interactionEntity.interactionHeight = 0.7f
        EntityAccessorHelper.writeString(interactionEntity, CustomDataKeys.GRAVE_OWNER_UUID, player.uuidAsString)
        EntityAccessorHelper.writeString(interactionEntity, CustomDataKeys.GRAVE_CONNECTION_BLOCK_DISPLAY_UUID, chestEntity.uuidAsString)
        EntityAccessorHelper.writeInt(interactionEntity, CustomDataKeys.GRAVE_HEALTH, 300)
        EntityAccessorHelper.writeNbtList(interactionEntity, CustomDataKeys.GRAVE_CONTENTS, content)
        world.spawnEntity(interactionEntity)
    }

    fun killGrave(player: PlayerEntity?, entity: Entity) {
        val ownerUUID = EntityAccessorHelper.readString(entity, CustomDataKeys.GRAVE_OWNER_UUID)!!.toUUID()
        if(player != null && ownerUUID != player.uuid) {
            player?.sendMessage(Text.literal("This is not your grave!"), false)
            return
        }
        val world = entity.world!!
        val owner = world.server!!.playerManager.getPlayer(ownerUUID)

        val blockDisplayUUID = EntityAccessorHelper.readString(entity, CustomDataKeys.GRAVE_CONNECTION_BLOCK_DISPLAY_UUID)
        val blockDisplay = world.getEntity(UUID.fromString(blockDisplayUUID))

        EntityAccessorHelper.readNbtList(entity, CustomDataKeys.GRAVE_CONTENTS)?.forEach { nbtElement ->
            val item = ItemSerializer.deserialize(nbtElement)
            val itemEntity = ItemEntity(world, entity.pos.x, entity.pos.y, entity.pos.z, item)
            world.spawnEntity(itemEntity)
        }

        blockDisplay?.remove(Entity.RemovalReason.DISCARDED)
        entity.remove(Entity.RemovalReason.DISCARDED)

        player?.sendMessage(Text.literal("This grave is from ${owner!!.name.literalString}"), false)
    }
}