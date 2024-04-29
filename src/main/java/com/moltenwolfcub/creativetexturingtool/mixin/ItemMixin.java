package com.moltenwolfcub.creativetexturingtool.mixin;

import com.moltenwolfcub.creativetexturingtool.DataSaver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void useInjector(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> info) {
        int maxReach = 100;

        if (!((Item)(Object)this instanceof BlockItem)) {
            return;
        }

        if(!((DataSaver)player).getPersistantData().getBoolean("active")){
            return;
        }

        BlockItem thisItem = (BlockItem)(Object)this;
        Vec3 eyePos = player.getEyePosition();
        Vec3 viewVec = player.getViewVector(0.0f);
        Vec3 maxReachPos = eyePos.add(viewVec.x*maxReach,viewVec.y*maxReach,viewVec.z*maxReach);

        HitResult hitResult = level.clip(new ClipContext(eyePos, maxReachPos, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, player));
        Vec3 blockLocation = hitResult.getLocation();
        BlockPos blockPos = new BlockPos((int)blockLocation.x, (int)blockLocation.y, (int)blockLocation.z);


        level.setBlock(blockPos, thisItem.getBlock().defaultBlockState(), 2);

        info.setReturnValue(InteractionResultHolder.sidedSuccess(player.getItemInHand(interactionHand), level.isClientSide()));
    }
}
