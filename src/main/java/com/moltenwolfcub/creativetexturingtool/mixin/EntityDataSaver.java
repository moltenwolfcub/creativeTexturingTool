package com.moltenwolfcub.creativetexturingtool.mixin;

import com.moltenwolfcub.creativetexturingtool.CreativeTexturingTool;
import com.moltenwolfcub.creativetexturingtool.DataSaver;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityDataSaver implements DataSaver {
    @Unique
    private CompoundTag persistantData;

    @Inject(method = "saveWithoutId", at = @At("HEAD"))
    protected void WriteMethodInject(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> info) {
        if(persistantData != null) {
            nbt.put(CreativeTexturingTool.MODID+ ".painting_data", persistantData);
        }
    }

    @Inject(method = "load", at = @At("HEAD"))
    protected void ReadMethodInject(CompoundTag nbt, CallbackInfo info) {
        if(nbt.contains(CreativeTexturingTool.MODID+ ".painting_data", 10)) {
            persistantData = nbt.getCompound(CreativeTexturingTool.MODID+ ".painting_data");
        }
    }

    @Override
    public CompoundTag getPersistantData() {
        if(this.persistantData == null) {
            this.persistantData = new CompoundTag();
        }
        return this.persistantData;
    }
}
