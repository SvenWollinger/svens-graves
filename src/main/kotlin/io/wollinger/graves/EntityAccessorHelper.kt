package io.wollinger.graves

import io.wollinger.graves.mixin.EntityAccessor
import net.minecraft.component.type.NbtComponent
import net.minecraft.entity.Entity
import net.minecraft.nbt.NbtList
import kotlin.jvm.optionals.getOrNull

object EntityAccessorHelper {
    private fun createKey(key: String) = "SVENS_GRAVES_$key"

    fun writeString(entity: Entity, key: String, data: String) {
        val accessor = entity as EntityAccessor
        val nbt = accessor.customData.copyNbt()
        nbt.putString(createKey(key), data)
        accessor.customData = NbtComponent.of(nbt)
    }

    fun writeInt(entity: Entity, key: String, data: Int) {
        val accessor = entity as EntityAccessor
        val nbt = accessor.customData.copyNbt()
        nbt.putInt(createKey(key), data)
        accessor.customData = NbtComponent.of(nbt)
    }

    fun writeNbtList(entity: Entity, key: String, data: NbtList) {
        val accessor = entity as EntityAccessor
        val nbt = accessor.customData.copyNbt()
        nbt.put(createKey(key), data)
        accessor.customData = NbtComponent.of(nbt)
    }

    fun readString(entity: Entity, key: String): String? {
        val accessor = entity as EntityAccessor
        return accessor.customData.copyNbt().getString(createKey(key)).getOrNull()
    }

    fun readInt(entity: Entity, key: String): Int? {
        val accessor = entity as EntityAccessor
        return accessor.customData.copyNbt().getInt(createKey(key)).getOrNull()
    }

    fun readNbtList(entity: Entity, key: String): NbtList? {
        val accessor = entity as EntityAccessor
        return accessor.customData.copyNbt().getList(createKey(key)).getOrNull()
    }
}