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
	public static final int SPACING = 1024;
	private final ResourceLocation SPACE_PLATFORM = ResourceLocation.fromNamespaceAndPath(PocketSpaceTeleporter.MODID, "space_platform");

	public Vec3 findOrPlaceStructure(ServerPlayer serverPlayer, ServerLevel serverLevel) {
		Vec3 pos = SpaceDataStorage.INSTANCE.getPlayerPosition(serverPlayer.getUUID());
		if(pos!=null) {
			return pos;
		}else {
			return placeStructureForPlayer(serverPlayer, serverLevel);
		}
	}

	private Vec3 placeStructureForPlayer(ServerPlayer serverPlayer, ServerLevel serverLevel) {
		if(serverLevel==null) {
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
				if(isPositionFree(serverLevel, candidate)) {
					placeStructureAt(serverLevel, candidate, candidate);
					Vec3 pos = new Vec3(candidate.getX()+3.5, candidate.getY()+1, candidate.getZ()+3.5);
					SpaceDataStorage.INSTANCE.setPlayerPosition(serverPlayer.getUUID(), pos);
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
