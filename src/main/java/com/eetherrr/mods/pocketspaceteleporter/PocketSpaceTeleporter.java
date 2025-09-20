package com.eetherrr.mods.pocketspaceteleporter;

import com.eetherrr.mods.pocketspaceteleporter.Item.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

@Mod(PocketSpaceTeleporter.MODID)
public class PocketSpaceTeleporter {
	public static final String MODID = "pocket_space_teleporter";
	public static final Logger LOGGER = LogUtils.getLogger();

	public PocketSpaceTeleporter(IEventBus modEventBus, ModContainer modContainer) {
		ModItems.ITEMS.register(modEventBus);
		modEventBus.addListener(this::addCreative);
	}

	private void addCreative(BuildCreativeModeTabContentsEvent event) {
		if(event.getTabKey()==CreativeModeTabs.TOOLS_AND_UTILITIES) {
			event.accept(ModItems.SPACE_TELEPORTER);
		}
	}
}
