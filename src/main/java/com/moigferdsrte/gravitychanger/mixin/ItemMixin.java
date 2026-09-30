package com.moigferdsrte.gravitychanger.mixin;

import com.moigferdsrte.gravitychanger.util.GravityDirectionUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
// Directional POV fix for item raycasts.
public abstract class ItemMixin {
    @Inject(
        method = "getPlayerPOVHitResult",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void gravitychanger$directionalPlayerPOVHitResult(
        final net.minecraft.world.level.Level level,
        final Player player,
        final ClipContext.Fluid fluidMode,
        final CallbackInfoReturnable<BlockHitResult> cir
    ) {
        Direction gravityDirection = GravityDirectionUtil.getGravityDirection(player);
        if (gravityDirection == Direction.DOWN) {
            return;
        }

        Vec3 start = player.getEyePosition();
        Vec3 end = start.add(player.getViewVector(1.0F).scale(player.blockInteractionRange()));
        cir.setReturnValue(level.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, fluidMode, player)));
    }
}
