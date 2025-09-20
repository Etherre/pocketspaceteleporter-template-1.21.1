package com.eetherrr.mods.pocketspaceteleporter.dimension;

import com.eetherrr.mods.pocketspaceteleporter.PocketSpaceTeleporter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class StructurePlacer {
	public static final StructurePlacer INSTANCE = new StructurePlacer();
	public static final int SPACING = 256;
	private final ResourceLocation SPACE_PLATFORM = ResourceLocation.fromNamespaceAndPath(PocketSpaceTeleporter.MODID, "space_platform");

	public boolean findOrPlaceStructure(ServerPlayer serverPlayer) {
		//todo:结构生成算法
		//		ServerLevel serverLevel = player.serverLevel();
		//		var structurePos = structurePosForPlayer(player);
		//		var pocketLevel = serverLevel.getServer().getLevel(POCKET_DIMENSION);
		//		BlockState blockState = pocketLevel.getBlockState(structurePos);
		//		if(blockState.isAir() && !blockState.is(Blocks.BARRIER)) {
		//			var structureTemplateManager = pocketLevel.getStructureManager();
		//			var structureTemplate = structureTemplateManager.getOrCreate(POCKET_ROOM_STRUCTURE);
		//			var placementSettings = (new StructurePlaceSettings()).setMirror(Mirror.NONE)
		//					                        .setRotation(Rotation.NONE)
		//					                        .setIgnoreEntities(true)
		//					                        .addProcessor(new ClearPortalFrameDataProcessor());
		//			structureTemplate.placeInWorld(pocketLevel, structurePos, structurePos, placementSettings, pocketLevel.getRandom(), 2);
		//			return true;
		//		}
		return true;
	}
}
