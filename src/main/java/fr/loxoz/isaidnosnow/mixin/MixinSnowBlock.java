package fr.loxoz.isaidnosnow.mixin;

import fr.loxoz.isaidnosnow.ISaidNoSnow;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowBlock.class)
public abstract class MixinSnowBlock extends Block {
    public MixinSnowBlock(Settings settings) {
        super(settings);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        if (ISaidNoSnow.INSTANCE != null && ISaidNoSnow.INSTANCE.isEnabled() && state.get(SnowBlock.LAYERS) == 1)
            return BlockRenderType.INVISIBLE;
        return super.getRenderType(state);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static AbstractBlock.Settings injected(AbstractBlock.Settings settings) {
        return settings.nonOpaque();
    }

    @Inject(method = "getOutlineShape", at = @At("HEAD"), cancellable = true)
    private void onGetOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (ISaidNoSnow.INSTANCE != null && ISaidNoSnow.INSTANCE.isEnabled() && state.get(SnowBlock.LAYERS) == 1)
            cir.setReturnValue(VoxelShapes.empty());
    }
}
