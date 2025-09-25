package com.eetherrr.mods.pocketspaceteleporter.datagen;

import com.eetherrr.mods.pocketspaceteleporter.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

	public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SPACE_TELEPORTER)
				.pattern(" A ")
				.pattern(" B ")
				.pattern(" C ")
				.define('A', Items.ECHO_SHARD)
				.define('B', Items.ENDER_EYE)
				.define('C', Items.AMETHYST_SHARD)
				.unlockedBy(getHasName(Items.ECHO_SHARD), has(Items.ECHO_SHARD))
				.save(recipeOutput);
	}
}
