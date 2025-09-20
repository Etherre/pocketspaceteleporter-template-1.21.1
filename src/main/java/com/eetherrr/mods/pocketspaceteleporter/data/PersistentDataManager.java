package com.eetherrr.mods.pocketspaceteleporter.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;

public class PersistentDataManager {
	public static final PersistentDataManager INSTANCE = new PersistentDataManager();

	public void savePlayerOriginPos(ServerPlayer serverPlayer) {
		CompoundTag pData = serverPlayer.getPersistentData();
		if(!pData.contains("pocket_space_teleporter_origin", Tag.TAG_COMPOUND)) {
			CompoundTag originTag = new CompoundTag();
			CompoundTag dimTag = new CompoundTag();
			dimTag.putString("namespace", serverPlayer.level().dimension().location().getNamespace());
			dimTag.putString("path", serverPlayer.level().dimension().location().getPath());
			originTag.put("dim", dimTag);
			originTag.putDouble("x", serverPlayer.position().x());
			originTag.putDouble("y", serverPlayer.position().y());
			originTag.putDouble("z", serverPlayer.position().z());
			pData.put("pocket_space_teleporter_origin", originTag);
		}
	}

	public CompoundTag getPlayerOriginPos(ServerPlayer serverPlayer) {
		CompoundTag pData = serverPlayer.getPersistentData();
		if(pData.contains("pocket_space_teleporter_origin", Tag.TAG_COMPOUND)) {
			return pData.getCompound("pocket_space_teleporter_origin");
		}
		return null;
	}

	public void clearPlayerOriginPos(ServerPlayer serverPlayer) {
		CompoundTag pData = serverPlayer.getPersistentData();
		if(pData.contains("pocket_space_teleporter_origin", Tag.TAG_COMPOUND)) {
			pData.remove("pocket_space_teleporter_origin");
		}
	}
}
