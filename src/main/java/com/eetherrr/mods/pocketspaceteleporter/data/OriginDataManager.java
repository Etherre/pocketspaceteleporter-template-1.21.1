package com.eetherrr.mods.pocketspaceteleporter.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class OriginDataManager {
	public static final OriginDataManager INSTANCE = new OriginDataManager();

	public void savePlayerOriginPos(ServerPlayer serverPlayer) {
		CompoundTag pData = serverPlayer.getPersistentData();
		if(!pData.contains("pst_origin_data", Tag.TAG_COMPOUND)) {
			CompoundTag originTag = new CompoundTag();
			CompoundTag dimTag = new CompoundTag();
			dimTag.putString("namespace", serverPlayer.level().dimension().location().getNamespace());
			dimTag.putString("path", serverPlayer.level().dimension().location().getPath());
			originTag.put("dim", dimTag);
			originTag.putDouble("x", serverPlayer.position().x());
			originTag.putDouble("y", serverPlayer.position().y());
			originTag.putDouble("z", serverPlayer.position().z());
			pData.put("pst_origin_data", originTag);
		}
	}

	public CompoundTag getPlayerOriginTag(ServerPlayer serverPlayer) {
		CompoundTag pData = serverPlayer.getPersistentData();
		if(pData.contains("pst_origin_data", Tag.TAG_COMPOUND)) {
			return pData.getCompound("pst_origin_data");
		}else {
			return null;
		}
	}

	public void clearPlayerOriginTag(ServerPlayer serverPlayer) {
		CompoundTag pData = serverPlayer.getPersistentData();
		if(pData.contains("pst_origin_data", Tag.TAG_COMPOUND)) {
			pData.remove("pst_origin_data");
		}
	}

	public ResourceKey<Level> getPlayerOriginDim(ServerPlayer serverPlayer) {
		CompoundTag originTag = getPlayerOriginTag(serverPlayer);
		if(originTag.contains("dim", Tag.TAG_COMPOUND)) {
			CompoundTag dimTag = originTag.getCompound("dim");
			return ResourceKey.create(Registries.DIMENSION, net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(dimTag.getString("namespace"), dimTag.getString("path")));
		}else {
			return null;
		}
	}

	public Vec3 getPlayerOriginPos(ServerPlayer serverPlayer) {
		CompoundTag originTag = getPlayerOriginTag(serverPlayer);
		return new Vec3(originTag.getDouble("x"), originTag.getDouble("y"), originTag.getDouble("z"));
	}
}
