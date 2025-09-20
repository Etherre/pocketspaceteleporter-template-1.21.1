package com.eetherrr.mods.pocketspaceteleporter.Item;

import com.eetherrr.mods.pocketspaceteleporter.PocketSpaceTeleporter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PocketSpaceTeleporter.MODID);

	public static final DeferredItem<Item> SPACE_TELEPORTER = registerItem(
			"space_teleporter", ()->new TeleporterItem(new Item.Properties().stacksTo(1)
					                                           .rarity(Rarity.EPIC))
	);

	public static DeferredItem<Item> registerItem(String name, Supplier<Item> itemSupplier) {
		return ITEMS.register(name, itemSupplier);
	}
}
