package cn.imsakura.mixin;

import cn.imsakura.type.CropNumResult;
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

import java.util.concurrent.ExecutionException;

@Mixin(CropBlock.class)
public abstract class CropMixin {

        @Inject(method = "randomTick", at = @At ("TAIL"))
        private void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) throws ExecutionException, InterruptedException {
            CropBlock cropBlock = (CropBlock) (Object) this;  // 将 this 强制转换为 CropBlock
            CropResult cropResult = SpecialUtils.checkCropBlockPositon(pos);
            if(!cropResult.flag)
            {
                return;
            }

            //作物是否成熟
            int age = state.get(CropBlock.AGE);
            if (age==cropBlock.getMaxAge()-1) {
                world.setBlockState(pos, cropBlock.withAge(cropBlock.getMaxAge()), Block.NOTIFY_LISTENERS);
                CropNumResult cropNumResult = SpecialUtils.countCrops(world,cropResult.cropMonitor);
                //只有当成熟的农作物占主体的1/2以上才发消息提示
                if(cropNumResult.agedCrops>=cropNumResult.allCrops/2) {
                    String msg = String.format("[Sakura] - 区域%s | 成熟状态:[%d/%d]", cropResult.cropMonitor.getName(), cropNumResult.agedCrops, cropNumResult.allCrops);
                    MessageUtils.broadcastMessage(world.getServer(), msg);
                }
            }
        }




}
