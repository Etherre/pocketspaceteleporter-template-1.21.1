package com.eetherrr.mods.pocketspaceteleporter.dimension;

import com.eetherrr.mods.pocketspaceteleporter.PocketSpaceTeleporter;
import com.eetherrr.mods.pocketspaceteleporter.data.OriginDataManager;
import com.eetherrr.mods.pocketspaceteleporter.data.SpaceDataStorage;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.UUID;

public class SpaceManager implements INBTSerializable<CompoundTag> {
	public static final SpaceManager INSTANCE = new SpaceManager();
	public static final ResourceKey<Level> POCKET_SPACE = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(PocketSpaceTeleporter.MODID, "pocket_space"));

	@Override
	public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
		CompoundTag compoundTag = new CompoundTag();
		ListTag listTag = new ListTag();
		for(var entry : SpaceDataStorage.INSTANCE.playerSpaceMap.entrySet()) {
			CompoundTag tagEntry = new CompoundTag();
			CompoundTag posTag = new CompoundTag();
			UUID key = entry.getKey();
			Vec3 value = entry.getValue();
			posTag.putDouble("x", value.x());
			posTag.putDouble("y", value.y());
			posTag.putDouble("z", value.z());
			tagEntry.put("pos", posTag);
			tagEntry.putUUID("uuid", key);
			listTag.add(tagEntry);
		}
		compoundTag.put("position_mapping", listTag);
		return compoundTag;
	}

	@Override
	public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
		//this.playerSpaceMap.clear();
		if(nbt.contains("position_mapping", Tag.TAG_LIST)) {
			ListTag listTag = nbt.getList("position_mapping", Tag.TAG_COMPOUND);
			for(int i = 0; i<listTag.size(); i++) {
				CompoundTag entry = listTag.getCompound(i);
				UUID uuid = entry.getUUID("uuid");
				CompoundTag posTag = entry.getCompound("pos");
				Vec3 pos = new Vec3(posTag.getDouble("x"), posTag.getDouble("y"), posTag.getDouble("z"));
				SpaceDataStorage.INSTANCE.playerSpaceMap.put(uuid, pos);
			}
		}
	}

	public void enterPocketSpace(ServerPlayer serverPlayer, ServerLevel serverLevel) {
		OriginDataManager.INSTANCE.savePlayerOriginPos(serverPlayer);
		Vec3 pos = StructurePlacer.INSTANCE.findOrPlaceStructure(serverPlayer, serverLevel);
		serverPlayer.changeDimension(new DimensionTransition(serverLevel, pos, Vec3.ZERO, serverPlayer.getYRot(), serverPlayer.getXRot(), DimensionTransition.DO_NOTHING));
	}

	public void returnFromPocket(ServerPlayer serverPlayer) {
		ServerLevel returnLevel = serverPlayer.getServer().getLevel(OriginDataManager.INSTANCE.getPlayerOriginDim(serverPlayer));
		Vec3 returnPos = OriginDataManager.INSTANCE.getPlayerOriginPos(serverPlayer);
		serverPlayer.changeDimension(new DimensionTransition(returnLevel, returnPos, Vec3.ZERO, serverPlayer.getYRot(), serverPlayer.getXRot(), DimensionTransition.DO_NOTHING));
		OriginDataManager.INSTANCE.clearPlayerOriginTag(serverPlayer);
	}
}
