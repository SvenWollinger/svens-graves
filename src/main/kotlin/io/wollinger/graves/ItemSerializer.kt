package io.wollinger.graves

import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtOps

object ItemSerializer {
    val SERIALIZE_OPS: DynamicOps<NbtCompound> = NbtOps.INSTANCE as DynamicOps<NbtCompound>
    fun serialize(itemStack: ItemStack): NbtCompound {
        val ops: DynamicOps<NbtCompound> = NbtOps.INSTANCE as DynamicOps<NbtCompound>
        val result: DataResult<NbtCompound> = ItemStack.CODEC.encodeStart(ops, itemStack)
        return result.result().orElseThrow { IllegalStateException("Failed to encode ItemStack") }
    }

    fun deserialize(nbtCompound: NbtElement): ItemStack {
        val ops: DynamicOps<NbtElement> = NbtOps.INSTANCE as DynamicOps<NbtElement>
        val result: DataResult<ItemStack> = ItemStack.CODEC.parse(ops, nbtCompound)
        return result.result().orElseThrow { IllegalStateException("Failed to decode ItemStack") }
    }
}