package com.eetherrr.mods.pocketspaceteleporter.data;

import com.eetherrr.mods.pocketspaceteleporter.dimension.SpaceManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SpaceDataStorage extends SavedData {
	public static SpaceDataStorage INSTANCE;
	public final Object2ObjectOpenHashMap<UUID, Vec3> playerSpaceMap = new Object2ObjectOpenHashMap<>();

	public static void init(DimensionDataStorage dimensionDataStorage) {
		if(dimensionDataStorage!=null) {
			SpaceDataStorage.INSTANCE = dimensionDataStorage.computeIfAbsent(new Factory<>(SpaceDataStorage::new, SpaceDataStorage::load), "pocket_space_teleporter_data");
		}
	}

	public static SpaceDataStorage load(CompoundTag tag, HolderLookup.Provider registries) {
		if(tag.contains("SpaceManager", Tag.TAG_COMPOUND)) {
			SpaceManager.INSTANCE.deserializeNBT(registries, tag.getCompound("SpaceManager"));
		}
		return new SpaceDataStorage();
	}

	@Override
	public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.@NotNull Provider provider) {
		tag.put("SpaceManager", SpaceManager.INSTANCE.serializeNBT(provider));
		return tag;
	}

	public void clearPlayerSpaceMap() {
		this.playerSpaceMap.clear();
		SpaceDataStorage.INSTANCE.setDirty();
	}

	public Vec3 getPlayerPosition(UUID uuid) {
		return this.playerSpaceMap.get(uuid);
	}

	public void setPlayerPosition(UUID uuid, Vec3 pos) {
		this.playerSpaceMap.put(uuid, pos);
		SpaceDataStorage.INSTANCE.setDirty();
	}
}
