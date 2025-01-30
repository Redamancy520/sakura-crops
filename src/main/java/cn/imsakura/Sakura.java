package cn.imsakura;

import cn.imsakura.commands.CropCommand;
import cn.imsakura.commands.ItemCleanner;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sakura implements ModInitializer {
	public static final String MOD_ID = "sakura";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {


		LOGGER.info("[Sakura] - Start to load");//Register Commands
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			ItemCleanner.register(dispatcher);
		});

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			CropCommand.register(dispatcher);
		});
		LOGGER.info("[Sakura] - Register commands successfully.");

	}
}