package cn.imsakura.utils;

import cn.imsakura.type.CropMonitor;
import cn.imsakura.type.CropNumResult;
import cn.imsakura.type.CropResult;
import cn.imsakura.type.MyPosition;
import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.CropBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpecialUtils {

    private void test(){checkCropBlockPositon(null);}



      /**
     * 统计指定区域的农作物数量
     * @param world 世界
     * @param monitor Monitor
     * @return 数量
     * @throws ExecutionException ex
     * @throws InterruptedException ex
     */
    public static CropNumResult countCrops(ServerWorld world, CropMonitor monitor) throws ExecutionException, InterruptedException {
        CropNumResult result = new CropNumResult();
        int x,y,z;
        x= (int) Double.parseDouble(monitor.getX1());
        y= (int) Double.parseDouble(monitor.getY1());
        z= (int) Double.parseDouble(monitor.getZ1());
        BlockPos p1 = new BlockPos(x,y,z);
        x= (int) Double.parseDouble(monitor.getX2());
        y= (int) Double.parseDouble(monitor.getY2());
        z= (int) Double.parseDouble(monitor.getZ2());
        BlockPos p2 = new BlockPos(x,y,z);
        result = countCrops(world,p1,p2);

        return result;
    }

    public static CropNumResult countCrops(ServerWorld world, BlockPos start, BlockPos end) {
        int totalCrops = 0;
        int maturedCrops = 0;

        // 遍历区域内的所有方块
        for (int x = start.getX(); x <= end.getX(); x++) {
            for (int y = start.getY(); y <= end.getY(); y++) {
                for (int z = start.getZ(); z <= end.getZ(); z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);

                    // 获取当前方块的状态
                    if (world.getBlockState(currentPos).getBlock() instanceof CropBlock cropBlock) {
                        totalCrops++; // 增加农作物计数

                        // 判断是否成熟
                        int age = world.getBlockState(currentPos).get(CropBlock.AGE);
                        if (age == cropBlock.getMaxAge()) {
                            maturedCrops++; // 增加成熟计数
                        }
                    }
                }
            }
        }

        // 输出统计结果
        System.out.println("Total Crops: " + totalCrops);
        System.out.println("Matured Crops: " + maturedCrops);
        return new CropNumResult(totalCrops,maturedCrops);
    }

    public static CropResult checkCropBlockPositon(BlockPos pos){

        Path configDir = FabricLoader.getInstance().getGameDir().resolve("config").resolve("Sakura");
        File folder = new File(configDir.toUri());
        if(!folder.isDirectory()||!folder.exists())return new CropResult(null,false);

        FilenameFilter filenameFilter = (dir, name) -> name.toLowerCase().endsWith(".json");
        File[] files = folder.listFiles(filenameFilter);
        if (files != null && files.length == 0) return new CropResult(null,false);
        if (files != null) {
            for(File file : files){
                String json = MyFileUtils.readFromFile(file.getPath());
                Gson gson = new Gson();
                try {
                    CropMonitor cropMonitor = gson.fromJson(json, CropMonitor.class);
                    //Get From Json File
                    int x1 = (int) Double.parseDouble(cropMonitor.getX1());
                    int y1 = (int) Double.parseDouble(cropMonitor.getY1());
                    int z1 = (int) Double.parseDouble(cropMonitor.getZ1());

                    int x2 = (int) Double.parseDouble(cropMonitor.getX2());
                    int y2 = (int) Double.parseDouble(cropMonitor.getY2());
                    int z2 = (int) Double.parseDouble(cropMonitor.getZ2());

                    boolean f1,f2,f3 = false;
                    f1 = pos.getX()>=Integer.min(x1,x2)&&pos.getX()<=Integer.max(x1,x2);
                    f2 = pos.getY()>=Integer.min(y1,y2)-2&&pos.getX()<=Integer.max(y1,y2)+2;
                    f3 = pos.getZ()>=Integer.min(z1,z2)&&pos.getX()<=Integer.max(z1,z2);
                 //   System.out.println("pos="+pos+"| check"+f1+f2+f3+"|"+new MyPosition(x1,y1,z1));
                    if(f1&&f2&&f3)
                        return new CropResult(cropMonitor,true);

                }catch (Exception e){
                    e.printStackTrace();
                    return new CropResult(null,false);
                }

            }
        }

        return new CropResult(null,false);
    }

}
