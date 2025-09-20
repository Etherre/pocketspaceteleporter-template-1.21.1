package com.eetherrr.mods.pocketspaceteleporter.event;

import com.eetherrr.mods.pocketspaceteleporter.data.SpaceDataStorage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

public class LevelLoadEvent {
	@SubscribeEvent
	public static void onLevelLoad(LevelEvent.Load event) {
		if(event.getLevel() instanceof ServerLevel serverLevel && serverLevel.dimension()==Level.OVERWORLD) {
			// Initialize SpaceDataStorage when the level loads
			SpaceDataStorage.init(serverLevel.getDataStorage());
		}
	}
}
