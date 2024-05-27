package com.moltenwolfcub.creativetexturingtool.mixin;

import com.moltenwolfcub.creativetexturingtool.DataSaver;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin extends Item {
    public BlockItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void placeInjector(BlockPlaceContext blockPlaceContext, CallbackInfoReturnable<InteractionResult> info) {
        Player player = blockPlaceContext.getPlayer();
        Level level = blockPlaceContext.getLevel();
        InteractionHand hand = blockPlaceContext.getHand();

        if (!player.isCreative()) {
            return;
        }

        if(((DataSaver)player).getPersistantData().getBoolean("active")){
            info.setReturnValue(this.use(level, player, hand).getResult());
        }
    }
}
