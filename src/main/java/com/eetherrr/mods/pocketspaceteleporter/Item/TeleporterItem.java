package com.eetherrr.mods.pocketspaceteleporter.Item;

import com.eetherrr.mods.pocketspaceteleporter.dimension.SpaceManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TeleporterItem extends Item {
	public TeleporterItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		ItemStack teleporter = player.getItemInHand(usedHand);
		if(player instanceof ServerPlayer serverPlayer) {
			ServerLevel pocketLevel = serverPlayer.getServer().getLevel(SpaceManager.POCKET_SPACE);
			if(level.dimension().equals(SpaceManager.POCKET_SPACE)) {
				SpaceManager.INSTANCE.returnFromPocket(serverPlayer);
			}else {
				SpaceManager.INSTANCE.enterPocketSpace(serverPlayer, pocketLevel);
			}
		}
		player.getCooldowns().addCooldown(this, 60);
		return InteractionResultHolder.sidedSuccess(teleporter, level.isClientSide());
	}
}
