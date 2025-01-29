package cn.imsakura.commands;

import cn.imsakura.Sakura;
import cn.imsakura.utils.MessageUtils;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;

import java.util.List;
import java.util.Objects;

public class ItemCleanner {

    public static void cleanAllDroppedItems(ServerWorld world) {

        // 创建覆盖整个世界的 Box
        Box worldBox = new Box(
                //[Warning] - 别写Integer.MAX 之类的 莫名其妙就不生效
                  -11451455, -60, -11451455,
                11451455, 1145, 11451455
        );

        // 获取世界范围内的所有掉落物
        List<ItemEntity> items = world.getEntitiesByType(EntityType.ITEM, worldBox, entity -> true);
        MessageUtils.broadcastMessage(world.getServer(),"[Sakura] - 发现"+ items.size()+ "个掉落物");
        // 移除所有掉落物
        for (ItemEntity item : items) {
            item.remove(Entity.RemovalReason.KILLED);
        }
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(CommandManager.literal("cleanup").executes(context -> {
            ServerWorld world = Objects.requireNonNull(context.getSource().getPlayer()).getServerWorld();
            cleanAllDroppedItems(world);
            MessageUtils.broadcastMessage(context.getSource().getServer(),"[Sakura] - 已清理掉落物");
            return 1;
        }));
    }
}
