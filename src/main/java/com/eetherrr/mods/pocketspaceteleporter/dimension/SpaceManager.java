package com.eetherrr.mods.pocketspaceteleporter.dimension;

import com.eetherrr.mods.pocketspaceteleporter.PocketSpaceTeleporter;
import com.eetherrr.mods.pocketspaceteleporter.data.PersistentDataManager;
import com.eetherrr.mods.pocketspaceteleporter.data.SpaceDataStorage;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
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
import org.jetbrains.annotations.UnknownNullability;

import java.util.UUID;

public class SpaceManager implements INBTSerializable<CompoundTag> {
	public static final SpaceManager INSTANCE = new SpaceManager();
	public static final ResourceKey<Level> POCKET_SPACE = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(PocketSpaceTeleporter.MODID, "pocket_space"));
	private final Object2ObjectOpenHashMap<UUID, Vec3> playerSpaceMap = new Object2ObjectOpenHashMap<>();

	@Override
	public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
		CompoundTag compoundTag = new CompoundTag();
		ListTag listTag = new ListTag();
		for(var entry : playerSpaceMap.entrySet()) {
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
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
		playerSpaceMap.clear();
		if(nbt.contains("position_mapping", Tag.TAG_LIST)) {
			ListTag listTag = nbt.getList("position_mapping", Tag.TAG_COMPOUND);
			for(int i = 0; i<listTag.size(); i++) {
				CompoundTag entry = listTag.getCompound(i);
				UUID uuid = entry.getUUID("uuid");
				CompoundTag posTag = entry.getCompound("pos");
				Vec3 pos = new Vec3(posTag.getDouble("x"), posTag.getDouble("y"), posTag.getDouble("z"));
				playerSpaceMap.put(uuid, pos);
			}
		}
	}

	public void enterPocketSpace(ServerPlayer serverPlayer, ServerLevel pocketLevel) {
		if(StructurePlacer.INSTANCE.findOrPlaceStructure(serverPlayer)) {
			PersistentDataManager.INSTANCE.savePlayerOriginPos(serverPlayer);
			Vec3 pos = findOrCreateSpawnPos(serverPlayer.getUUID());
			serverPlayer.changeDimension(new DimensionTransition(pocketLevel, pos, Vec3.ZERO, serverPlayer.getYRot(), serverPlayer.getXRot(), DimensionTransition.DO_NOTHING));
		}
	}

	public void returnFromPocket(ServerPlayer serverPlayer) {
		CompoundTag originPos = PersistentDataManager.INSTANCE.getPlayerOriginPos(serverPlayer);
		if(originPos!=null) {
			String dimNameSpace = originPos.getCompound("dim").getString("namespace");
			String dimPath = originPos.getCompound("dim").getString("path");
			ResourceKey<Level> originDim = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(dimNameSpace, dimPath));
			ServerLevel returnLevel = serverPlayer.getServer().getLevel(originDim);
			if(returnLevel!=null) {
				Vec3 returnPos = new Vec3(originPos.getDouble("x"), originPos.getDouble("y"), originPos.getDouble("z"));
				serverPlayer.changeDimension(new DimensionTransition(returnLevel, returnPos, Vec3.ZERO, serverPlayer.getYRot(), serverPlayer.getXRot(), DimensionTransition.DO_NOTHING));
				PersistentDataManager.INSTANCE.clearPlayerOriginPos(serverPlayer);
			}
		}
	}

	private Vec3 findOrCreateSpawnPos(UUID uuid) {
		if(playerSpaceMap.containsKey(uuid)) {
			return playerSpaceMap.get(uuid);
		}else {
			//todo:需先完成结构放置算法，再获取结构中心位置作为spawnPos
			Vec3 pos = new Vec3((playerSpaceMap.size()%10)*StructurePlacer.SPACING/2.0, 1, (playerSpaceMap.size()/10)*StructurePlacer.SPACING/2.0);
			playerSpaceMap.put(uuid, pos);
			SpaceDataStorage.INSTANCE.setDirty();
			return pos;
		}
	}
}
