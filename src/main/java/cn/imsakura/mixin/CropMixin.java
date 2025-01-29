package cn.imsakura.mixin;

import cn.imsakura.type.CropResult;
import cn.imsakura.utils.MessageUtils;
import cn.imsakura.utils.SpecialUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CropBlock.class)
public abstract class CropMixin {

        @Inject(method = "randomTick", at = @At ("TAIL"))
        private void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
            CropBlock cropBlock = (CropBlock) (Object) this;  // 将 this 强制转换为 CropBlock
            CropResult cropResult = SpecialUtils.checkCropBlockPositon(pos);
            if(!cropResult.flag)
            {
               // System.out.println("[Sakura] - Out of limit-field");
                return;
            }

            //作物是否成熟
            int age = state.get(CropBlock.AGE);
            if (age==cropBlock.getMaxAge()-1) {
                world.setBlockState(pos, cropBlock.withAge(cropBlock.getMaxAge()), Block.NOTIFY_LISTENERS);
                //MessageUtils.broadcastMessage(world.getServer(),"[Sakura] - 区域"+ cropResult.cropMonitor.getName() + "的一个作物成熟了");
            }
        }




}
