package io.wollinger.graves

import io.wollinger.graves.mixin.EntityAccessor
import net.minecraft.component.type.NbtComponent
import net.minecraft.entity.Entity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import kotlin.jvm.optionals.getOrNull

object EntityAccessorHelper {
    private fun createKey(key: String) = "SVENS_GRAVES_$key"

    fun getEditableNbt(entity: Entity, action: (NbtCompound) -> Unit) {
        val accessor = entity as EntityAccessor
        val nbt = accessor.customData.copyNbt()
        action.invoke(nbt)
        accessor.customData = NbtComponent.of(nbt)
    }

    fun getNbt(entity: Entity) = (entity as EntityAccessor).customData.copyNbt()

    fun writeString(entity: Entity, key: String, data: String) {
        getEditableNbt(entity) { nbt ->
            nbt.putString(createKey(key), data)
        }
    }

    fun writeInt(entity: Entity, key: String, data: Int) {
        getEditableNbt(entity) { nbt ->
            nbt.putInt(createKey(key), data)
        }
    }

    fun writeNbtList(entity: Entity, key: String, data: NbtList) {
        getEditableNbt(entity) { nbt ->
            nbt.put(createKey(key), data)
        }
    }

    fun readString(entity: Entity, key: String): String? {
        return getNbt(entity).getString(createKey(key)).getOrNull()
    }

    fun readInt(entity: Entity, key: String): Int? {
        return getNbt(entity).getInt(createKey(key)).getOrNull()
    }

    fun readNbtList(entity: Entity, key: String): NbtList? {
        return getNbt(entity).getList(createKey(key)).getOrNull()
    }
}