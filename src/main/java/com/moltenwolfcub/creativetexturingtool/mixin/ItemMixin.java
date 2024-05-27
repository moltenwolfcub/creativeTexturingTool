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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    private static final int MAX_REACH = 100;

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void useInjector(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> info) {

        if (!player.isCreative()) {
            return;
        }

        Item This = player.getItemInHand(interactionHand).getItem();

        if (!(This instanceof BlockItem blockItem)) {
            return;
        }

        if(!((DataSaver)player).getPersistantData().getBoolean("active")){
            return;
        }

        Vec3 eyePos = player.getEyePosition();
        Vec3 viewVec = player.getViewVector(0.0f);
        Vec3 maxReachPos = eyePos.add(viewVec.x*MAX_REACH,viewVec.y*MAX_REACH,viewVec.z*MAX_REACH);

        BlockHitResult hitResult = level.clip(new ClipContext(eyePos, maxReachPos, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, player));
        BlockPos blockLocation = hitResult.getBlockPos();

        level.setBlock(blockLocation, blockItem.getBlock().defaultBlockState(), 2);

        info.setReturnValue(InteractionResultHolder.sidedSuccess(player.getItemInHand(interactionHand), level.isClientSide()));
    }
}
