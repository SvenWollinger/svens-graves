package io.wollinger.graves.mixin;

import io.wollinger.graves.EntityTickHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.InteractionEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InteractionEntity.class)
public abstract class EntityTickMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (!entity.getWorld().isClient) {
            EntityTickHelper.INSTANCE.tickGrave(entity);
        }
    }

}