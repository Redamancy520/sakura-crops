package cn.imsakura.commands;


import cn.imsakura.Sakura;
import cn.imsakura.type.CropMonitor;
import cn.imsakura.type.CropNumResult;
import cn.imsakura.type.MyPosition;
import cn.imsakura.utils.MessageUtils;
import cn.imsakura.utils.MyFileUtils;
import cn.imsakura.utils.SpecialUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class CropCommand {

    private static Path ensureConfigFile(Path dir, String name) {
        try {
            // 如果目录不存在，创建目录
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            // 目标文件路径
            Path filePath = dir.resolve(name + ".json");

            // 如果文件不存在，创建空文件
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            // 返回文件路径
            return filePath;

        } catch (IOException e) {
            // 捕获异常并记录日志
            Sakura.LOGGER.error("无法确保文件存在: " + dir, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置农作物检测区域坐标1
     * @param context Context
     */
    public static void command_set1(CommandContext<ServerCommandSource> context){
        ServerPlayerEntity player = Objects.requireNonNull(context.getSource().getPlayer());

        MyPosition position = new MyPosition();
        position.x = String.valueOf(player.getX());
        position.y = String.valueOf(player.getY());
        position.z = String.valueOf(player.getZ());

        String name = StringArgumentType.getString(context, "name");

        Path configDir = FabricLoader.getInstance().getGameDir().resolve("config").resolve("Sakura");
        Path filePath = ensureConfigFile(configDir, name);

        Gson gson = new Gson();
        CropMonitor monitor;

        try {
            String json = MyFileUtils.readFromFile(filePath.toString());
            Files.delete(filePath);
            if(json==null||json.isEmpty())
            {
                monitor=new CropMonitor(name);
            }else {
                monitor = gson.fromJson(json, CropMonitor.class);
            }


        } catch (Exception e) {
            monitor = new CropMonitor(name); // 如果文件不存在或解析失败，创建新的对象
        }
        monitor.setName(name);
        monitor.setX1(position.x);
        monitor.setY1(position.y);
        monitor.setZ1(position.z);

        String updatedJson = gson.toJson(monitor);

        MyFileUtils.writeToFile(filePath.toString(), updatedJson);
        player.sendMessage(Text.literal("[Sakura] - 已保存当前坐标1"));
    }

    /**
     * 设置农作物检测区域坐标2
     * @param context Context
     */
    public static void command_set2(CommandContext<ServerCommandSource> context){
        ServerPlayerEntity player = Objects.requireNonNull(context.getSource().getPlayer());

        MyPosition position = new MyPosition();
        position.x = String.valueOf(player.getX());
        position.y = String.valueOf(player.getY());
        position.z = String.valueOf(player.getZ());

        String name = StringArgumentType.getString(context, "name");
        Path configDir = FabricLoader.getInstance().getGameDir().resolve("config").resolve("Sakura");
        Path filePath = ensureConfigFile(configDir, name);

        Gson gson = new Gson();
        CropMonitor monitor;

        try {
            String json = MyFileUtils.readFromFile(filePath.toString());
            Files.delete(filePath);
            if(json==null||json.isEmpty())
            {
                monitor=new CropMonitor(name);
            }else {
                monitor = gson.fromJson(json, CropMonitor.class);
            }
        } catch (Exception e) {
            monitor = new CropMonitor(name); // 如果文件不存在或解析失败，创建新的对象
        }

        monitor.setName(name);
        monitor.setX2(position.x);
        monitor.setY2(position.y);
        monitor.setZ2(position.z);

        String updatedJson = gson.toJson(monitor);
        MyFileUtils.writeToFile(filePath.toString(), updatedJson);
        player.sendMessage(Text.literal("[Sakura] - 已保存当前坐标2"));
    }

    /**
     * 列出所有农作物检测区域
     * @param context Context    
     */
    public static void command_list(CommandContext<ServerCommandSource> context){
        Path configDir = FabricLoader.getInstance().getGameDir().resolve("config").resolve("Sakura");
        FilenameFilter filenameFilter = (dir, name) -> name.toLowerCase().endsWith(".json");
        File folder = new File(configDir.toUri());
        File[] files;
        files=folder.listFiles(filenameFilter);
        Gson gson = new Gson();
        String json = "";
        ArrayList<String> list = new ArrayList<>();

        //从有效JSON文件中获取NameList
        if (files != null) {
            for(File file : files){
                try {
                    json = MyFileUtils.readFromFile(file.getPath());
                    CropMonitor cropMonitor =gson.fromJson(json, CropMonitor.class);
                    list.add(cropMonitor.getName());
                }catch (Exception ignored){
                }
            }
        }
        StringBuilder msg = new StringBuilder("[Sakura] - 全部区域:");
        for(String t : list){
            msg.append(String.format("[%s] ", t));
        }
        MessageUtils.sendColoredMessage(Objects.requireNonNull(context.getSource().getPlayer()),msg.toString(), Formatting.GREEN);
    }

    /**
     * 统计区域中的农作物数量以及成熟数量
     * @param context Context
     */
    public static void command_count(CommandContext<ServerCommandSource> context){
        Path configDir = FabricLoader.getInstance().getGameDir().resolve("config").resolve("Sakura");
        String name = StringArgumentType.getString(context,"name");
        Gson gson = new Gson();
        try{
            String json = MyFileUtils.readFromFile(configDir.resolve(name+".json").toString());
            if(json==null)
            {
                MessageUtils.sendColoredMessage(Objects.requireNonNull(context.getSource().getPlayer()),"[Sakura] - 该区域不存在",Formatting.RED);
                return;
            }
            CropMonitor monitor = gson.fromJson(json,CropMonitor.class);
            CropNumResult result = SpecialUtils.countCrops(context.getSource().getWorld(),monitor);
            String msg = String.format("[Sakura] - 该区域农作物成熟状态:[%d/%d]",result.agedCrops,result.allCrops);
            Objects.requireNonNull(context.getSource().getPlayer()).sendMessage(Text.literal(msg));

        }catch (JsonSyntaxException ex){
            MessageUtils.sendColoredMessage(Objects.requireNonNull(context.getSource().getPlayer()),"[Sakura] - 该区域JSON文件解析失败",Formatting.RED);
        }catch (Exception ex){
            MessageUtils.sendColoredMessage(Objects.requireNonNull(context.getSource().getPlayer()),"[Sakura] - 执行出错Error",Formatting.RED);
            ex.printStackTrace();
        }
    }


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(CommandManager.literal("sakura")
                .then(CommandManager.literal("crop")
                                //Command - set1
                                .then(CommandManager.literal("set1")
                                        .then(CommandManager.argument("name", StringArgumentType.string())
                                                .executes(context -> {
                                                    command_set1(context);
                                                    return 1;
                                        }))
                                )
                                //Command - set2
                                .then(CommandManager.literal("set2")
                                        .then(CommandManager.argument("name",StringArgumentType.string())
                                        .executes(context -> {
                                            command_set2(context);
                                            return 1;
                                        })
                                    )
                                )
                                //Command - list
                                .then(CommandManager.literal("list").executes(context -> {
                                    command_list(context);
                                    return 1;
                                }))
                                //Command - count
                                .then(CommandManager.literal("count")
                                    .then(CommandManager.argument("name",StringArgumentType.string()).executes(context -> {
                                        command_count(context);
                                        return 1;
                                    })))
                    )
        );
    }
}
