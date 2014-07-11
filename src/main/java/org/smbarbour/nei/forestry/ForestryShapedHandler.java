package org.smbarbour.nei.forestry;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import forestry.core.utils.ShapedRecipeCustom;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;
import java.util.List;

public class ForestryShapedHandler extends ShapedRecipeHandler {

	@Override
	public String getRecipeName() {
		return "Shaped Forestry";
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if ((outputId.equals("crafting")) && (getClass() == ForestryShapedHandler.class)) {
			List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
			for(IRecipe irecipe : allrecipes)
			{
				if (irecipe instanceof ShapedRecipeCustom) {
					ShapedRecipeCustom customRecipe = (ShapedRecipeCustom)irecipe;
					CachedShapedRecipe recipe = new CachedShapedRecipe(customRecipe);
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
				CachedShapedRecipe recipe = new CachedShapedRecipe(customRecipe);
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
				CachedShapedRecipe recipe = new CachedShapedRecipe(customRecipe);
				if (recipe.contains(recipe.ingredients, ingredient)) {
					recipe.setIngredientPermutation(recipe.ingredients, ingredient);
					this.arecipes.add(recipe);
				}
			}
		}
	}
	
	public class CachedShapedRecipe extends CachedRecipe {

		public int xproduct = 119;
		public int yproduct = 24;
		public ArrayList<PositionedStack> ingredients;
		public PositionedStack product;
		
		public CachedShapedRecipe(ShapedRecipeCustom recipe) {
			super();
			ingredients = new ArrayList<PositionedStack>();
			setIngredients(recipe);
		}

		public void setIngredients(ShapedRecipeCustom recipe)
		{
			try
			{
				int width = ObfuscationReflectionHelper.getPrivateValue(ShapedRecipeCustom.class, recipe, 0);
				int height = ObfuscationReflectionHelper.getPrivateValue(ShapedRecipeCustom.class, recipe, 1);
				Object[] items = recipe.getIngredients();
				this.product = new PositionedStack(recipe.getRecipeOutput(), this.xproduct, this.yproduct);
				setIngredients(width, height, items);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
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

		public List<PositionedStack> getIngredients()
		{
			return getCycledIngredients(ForestryShapedHandler.this.cycleticks / 20, this.ingredients);
		}
	}
}
