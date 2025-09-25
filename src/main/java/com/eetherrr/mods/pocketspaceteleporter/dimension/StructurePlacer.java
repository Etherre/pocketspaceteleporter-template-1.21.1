package com.eetherrr.mods.pocketspaceteleporter.dimension;

import com.eetherrr.mods.pocketspaceteleporter.PocketSpaceTeleporter;
import com.eetherrr.mods.pocketspaceteleporter.data.SpaceDataStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;

public class StructurePlacer {
	public static final StructurePlacer INSTANCE = new StructurePlacer();
	public static final int SPACING = 256;
	private final ResourceLocation SPACE_PLATFORM = ResourceLocation.fromNamespaceAndPath(PocketSpaceTeleporter.MODID, "space_platform");

	public Vec3 findOrPlaceStructure(ServerPlayer serverPlayer) {
		if(SpaceManager.INSTANCE.playerSpaceMap.containsKey(serverPlayer.getUUID())) {
			return SpaceManager.INSTANCE.playerSpaceMap.get(serverPlayer.getUUID());
		}else {
			Vec3 playerSpawnPos = placeStructureForPlayer(serverPlayer);
			return playerSpawnPos;
		}
	}

	private Vec3 placeStructureForPlayer(ServerPlayer serverPlayer) {
		ServerLevel pocketLevel = serverPlayer.getServer().getLevel(SpaceManager.POCKET_SPACE);
		if(pocketLevel==null) {
			return null;
		}

		int baseY = 0;
		BlockPos center = new BlockPos(0, baseY, 0);

		int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
		int step = 0;

		while(true) {
			for(int[] dir : directions) {
				int dx = dir[0]*step*SPACING;
				int dz = dir[1]*step*SPACING;
				BlockPos candidate = center.offset(dx, 0, dz);
				if(isPositionFree(pocketLevel, candidate)) {
					placeStructureAt(pocketLevel, candidate, candidate);
					Vec3 pos = new Vec3(candidate.getX()+5, candidate.getY()+1, candidate.getZ()+5);
					SpaceManager.INSTANCE.playerSpaceMap.put(serverPlayer.getUUID(), pos);
					SpaceDataStorage.INSTANCE.setDirty();
					return pos;
				}
			}
			step++;
		}
	}

	private boolean isPositionFree(ServerLevel level, BlockPos pos) {
		return level.isEmptyBlock(pos);
	}

	private void placeStructureAt(ServerLevel level, BlockPos offset, BlockPos pos) {
		StructureTemplate structureTemplate = level.getStructureManager().getOrCreate(SPACE_PLATFORM);
		StructurePlaceSettings placementSettings = new StructurePlaceSettings().setMirror(Mirror.NONE)
				                                           .setRotation(Rotation.NONE)
				                                           .setIgnoreEntities(true);
		structureTemplate.placeInWorld(level, offset, pos, placementSettings, level.getRandom(), 2);
	}
}
