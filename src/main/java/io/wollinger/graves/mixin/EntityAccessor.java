package io.wollinger.graves.mixin;

import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("customData")
    NbtComponent getCustomData();

    @Accessor("customData")
    void setCustomData(NbtComponent customData);
}
