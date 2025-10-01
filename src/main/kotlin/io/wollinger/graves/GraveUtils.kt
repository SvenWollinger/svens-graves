package io.wollinger.graves

import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.decoration.DisplayEntity
import net.minecraft.entity.decoration.InteractionEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtList
import net.minecraft.text.Text
import net.minecraft.util.math.AffineTransformation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.world.World
import org.joml.Vector3f
import java.util.UUID

object GraveUtils {
    fun spawnGrave(player: PlayerEntity, location: BlockPos, world: World, content: NbtList) {
        val chestEntity = DisplayEntity.BlockDisplayEntity(EntityType.BLOCK_DISPLAY, world)
        val interactionEntity = InteractionEntity(EntityType.INTERACTION, world)

        //Position
        chestEntity.setPosition(location.toCenterPos())

        //Block State
        chestEntity.blockState = Blocks.CHEST.defaultState

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

    fun getOwnerUUID(grave: Entity): UUID? {
        return EntityAccessorHelper.readString(grave, CustomDataKeys.GRAVE_OWNER_UUID)?.toUUID()
    }

    fun isGrave(entity: Entity): Boolean {
        return getOwnerUUID(entity) != null
    }

    fun isOwner(player: PlayerEntity, grave: Entity): Boolean {
        return getOwnerUUID(grave) == player.uuid
    }

    fun interactGrave(player: PlayerEntity, grave: Entity) {
        if(!isGrave(grave)) return
        if(!isOwner(player, grave)) {
            player.sendMessage(Text.literal("This is not your grave!"), false)
            return
        }
        openGrave(grave)
    }

    fun openGrave(grave: Entity) {
        val world = grave.world!!
        val blockDisplayUUID = EntityAccessorHelper.readString(grave, CustomDataKeys.GRAVE_CONNECTION_BLOCK_DISPLAY_UUID)
        val blockDisplay = world.getEntity(UUID.fromString(blockDisplayUUID))

        EntityAccessorHelper.readNbtList(grave, CustomDataKeys.GRAVE_CONTENTS)?.forEach { nbtElement ->
            val item = ItemSerializer.deserialize(nbtElement)
            val itemEntity = ItemEntity(world, grave.pos.x, grave.pos.y, grave.pos.z, item)
            world.spawnEntity(itemEntity)
        }

        blockDisplay?.remove(Entity.RemovalReason.DISCARDED)
        grave.remove(Entity.RemovalReason.DISCARDED)
    }
}