package org.smbarbour.nei.forestry;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.print.attribute.standard.OutputDeviceAssigned;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import forestry.core.utils.ShapedRecipeCustom;

public class ForestryRecipeHandler extends ShapedRecipeHandler {

	@Override
	public String getRecipeName() {
		return "Shaped Forestry";
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if ((outputId.equals("crafting")) && (getClass() == ForestryRecipeHandler.class)) {
			List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
			for(IRecipe irecipe : allrecipes)
			{
				if (irecipe instanceof ShapedRecipeCustom) {
					ShapedRecipeCustom customRecipe = (ShapedRecipeCustom)irecipe;
					NEIForestry.logger.log(Level.FINE, "Recipe size: " + irecipe.getRecipeSize());
					CachedForestryRecipe recipe = new CachedForestryRecipe(customRecipe);
					this.arecipes.add(recipe);
				}
			}
		} else {
			super.loadCraftingRecipes(outputId, results);
		}
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result) {
		List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
		for(IRecipe irecipe : allrecipes)
		{
			if (irecipe instanceof ShapedRecipeCustom) {
				ShapedRecipeCustom customRecipe = (ShapedRecipeCustom)irecipe;
				CachedForestryRecipe recipe = new CachedForestryRecipe(customRecipe);
				if (NEIClientUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
					this.arecipes.add(recipe);
				}
			}
		}
	}
	
	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
		for(IRecipe irecipe : allrecipes)
		{
			if (irecipe instanceof ShapedRecipeCustom) {
				ShapedRecipeCustom customRecipe = (ShapedRecipeCustom)irecipe;
				CachedForestryRecipe recipe = new CachedForestryRecipe(customRecipe);
				if (recipe.contains(recipe.ingredients, ingredient)) {
					recipe.setIngredientPermutation(recipe.ingredients, ingredient);
					this.arecipes.add(recipe);
				}
			}
		}
	}
	
	public class CachedForestryRecipe extends CachedRecipe {

		public int xproduct = 119;
		public int yproduct = 24;
		public ArrayList<PositionedStack> ingredients;
		public PositionedStack product;
		
		public CachedForestryRecipe(ShapedRecipeCustom recipe) {
			super();
			ingredients = new ArrayList<PositionedStack>();
			setIngredients(recipe);
			NEIForestry.logger.log(Level.FINE, (this.product.item.getItemName() + ": " + ingredients.size() + " ingredients"));
		}

		public void setIngredients(ShapedRecipeCustom recipe)
		{
			try
			{
				int width = ((Integer)ObfuscationReflectionHelper.getPrivateValue(ShapedRecipeCustom.class, recipe, 0)).intValue();
				int height = ((Integer)ObfuscationReflectionHelper.getPrivateValue(ShapedRecipeCustom.class, recipe, 1)).intValue();
				Object[] items = (Object[])ObfuscationReflectionHelper.getPrivateValue(ShapedRecipeCustom.class, recipe, 2);
				this.product = new PositionedStack((ItemStack)ObfuscationReflectionHelper.getPrivateValue(ShapedRecipeCustom.class, recipe, 3), this.xproduct, this.yproduct);
				setIngredients(width, height, items);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				return;
			}
		}

		public void setIngredients(int width, int height, Object[] items) {
			for (int x = 0; x < width; x++)
			{
				for (int y = 0; y < height; y++)
				{
					if (items[(y * width + x)] != null)
					{
						PositionedStack stack = new PositionedStack(items[(y * width + x)], 25 + x * 18, 6 + y * 18);
						stack.setMaxSize(1);
						this.ingredients.add(stack);
					}
				}
			}
		}

		@Override
		public PositionedStack getResult() {
			return this.product;
		}

		public ArrayList<PositionedStack> getIngredients()
		{
			return getCycledIngredients(ForestryRecipeHandler.this.cycleticks / 20, this.ingredients);
		}
	}
}
