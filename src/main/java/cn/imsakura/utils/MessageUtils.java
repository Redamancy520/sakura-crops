package cn.imsakura.utils;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class MessageUtils {

    public static void sendColoredMessage(ServerPlayerEntity player,String message){
        sendColoredMessage(player,message,Formatting.WHITE);
    }

    /**
     * 发送消息给玩家(单独)
     * @param player 玩家
     * @param message 消息
     * @param color 颜色
     */
    public static void sendColoredMessage(ServerPlayerEntity player, String message, Formatting color) {
        // 创建一个带有颜色的文本
        Text coloredText = Text.literal(message).formatted(color);

        // 发送消息给玩家
        player.sendMessage(coloredText, false); // 第二个参数为 true 时，消息会显示在聊天栏顶部
    }



    public static void broadcastMessage(MinecraftServer server, String message) {
        broadcastMessage(server,message,Formatting.WHITE);
    }

    /**
     * 广播消息(发送给所有在线玩家)
     * @param server 服务器
     * @param message 消息
     * @param color 颜色
     */
    public static void broadcastMessage(MinecraftServer server, String message, Formatting color) {
        // 创建彩色消息
        Text coloredText = Text.literal(message).formatted(color);

        // 遍历所有在线玩家
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            player.sendMessage(coloredText, false);
        }

        // 打印到服务器控制台
        server.sendMessage(Text.literal("[Server Broadcast]: ").append(coloredText));
    }

}
