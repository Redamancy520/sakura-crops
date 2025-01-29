package cn.imsakura.mixin;

import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class ExampleMixin {
	@Shadow @Final private static Logger LOGGER;

	@Shadow public abstract void setMotd(String motd);

	@Inject(at = @At("HEAD"), method = "loadWorld")
	private void init(CallbackInfo info) {
			LOGGER.info("原神启动！！！！！！！！！！！");
			this.setMotd("你说的对但是明日方舟是一款由....");
	}
}

