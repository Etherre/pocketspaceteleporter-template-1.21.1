package com.eetherrr.mods.pocketspaceteleporter.data;

import com.eetherrr.mods.pocketspaceteleporter.dimension.SpaceManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class SpaceDataStorage extends SavedData {
	public static SpaceDataStorage INSTANCE = new SpaceDataStorage();

	public static void init(DimensionDataStorage dimensionDataStorage) {
		if(dimensionDataStorage!=null) {
			SpaceDataStorage.INSTANCE = dimensionDataStorage.computeIfAbsent(new Factory<SpaceDataStorage>(SpaceDataStorage::new, SpaceDataStorage::load), "pocket_space_teleporter_data");
		}
	}

	public static SpaceDataStorage load(CompoundTag tag, HolderLookup.Provider registries) {
		if(tag.contains("SpaceManager", Tag.TAG_COMPOUND)) {
			SpaceManager.INSTANCE.deserializeNBT(registries, tag.getCompound("SpaceManager"));
		}
		return new SpaceDataStorage();
	}

	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
		CompoundTag compoundTag = new CompoundTag();
		compoundTag.put("SpaceManager", SpaceManager.INSTANCE.serializeNBT(provider));
		tag.put("pocket_space_teleporter_data", compoundTag);
		return tag;
	}
}
