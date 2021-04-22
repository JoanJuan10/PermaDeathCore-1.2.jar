package com.permadeathcore.Util.Manager;

import com.permadeathcore.Main;
import com.permadeathcore.Util.Item.InfernalNetherite;
import com.permadeathcore.Util.Item.PermaDeathItems;
import com.permadeathcore.Util.Library.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class RecipeManager {
   private Main instance;

   public RecipeManager(Main instance) {
      this.instance = instance;
   }

   public void registerRecipes() {
      this.registerHyperGAP();
      this.registerSuperGAP();
      this.registerShulkerUnCraft();
      this.registerEndRel();
   }

   public void registerD50Recipes() {
      this.registerINH();
      this.registerINC();
      this.registerINL();
      this.registerINB();
   }

   public void registerD60Recipes() {
      this.registerIE();
      this.registerLifeOrb();
      this.registerBeginningRelic();
   }

   private void registerBeginningRelic() {
      ItemStack s = PermaDeathItems.createBeginningRelic();
      NamespacedKey key = new NamespacedKey(this.instance, "beginning_relic");
      ShapedRecipe recipe = new ShapedRecipe(key, s);
      recipe.shape(new String[]{"SBS", "BDB", "SBS"});
      recipe.setIngredient('B', Material.DIAMOND_BLOCK);
      recipe.setIngredient('D', Material.LIGHT_BLUE_DYE);
      recipe.setIngredient('S', Material.SHULKER_SHELL);
      this.instance.getServer().addRecipe(recipe);
   }

   private void registerIE() {
      ItemStack s = PermaDeathItems.crearElytraInfernal();
      NamespacedKey key = new NamespacedKey(this.instance, "infernal_elytra");
      ShapedRecipe recipe = new ShapedRecipe(key, s);
      recipe.shape(new String[]{"III", "IPI", "III"});
      recipe.setIngredient('I', Material.DIAMOND);
      recipe.setIngredient('P', Material.ELYTRA);
      this.instance.getServer().addRecipe(recipe);
   }

   private void registerINH() {
      ItemStack s = InfernalNetherite.craftNetheriteHelmet();
      NamespacedKey key = new NamespacedKey(this.instance, "infernal_helmet");
      ShapedRecipe recipe = new ShapedRecipe(key, s);
      recipe.shape(new String[]{" I ", "IPI", " I "});
      recipe.setIngredient('I', Material.DIAMOND);
      recipe.setIngredient('P', Material.LEATHER_HELMET);
      this.instance.getServer().addRecipe(recipe);
   }

   private void registerINC() {
      ItemStack s = InfernalNetherite.craftNetheriteChest();
      NamespacedKey key = new NamespacedKey(this.instance, "infernal_chestplate");
      ShapedRecipe recipe = new ShapedRecipe(key, s);
      recipe.shape(new String[]{" I ", "IPI", " I "});
      recipe.setIngredient('I', Material.DIAMOND);
      recipe.setIngredient('P', Material.LEATHER_CHESTPLATE);
      this.instance.getServer().addRecipe(recipe);
   }

   private void registerINL() {
      ItemStack s = InfernalNetherite.craftNetheriteLegs();
      NamespacedKey key = new NamespacedKey(this.instance, "infernal_leggings");
      ShapedRecipe recipe = new ShapedRecipe(key, s);
      recipe.shape(new String[]{" I ", "IPI", " I "});
      recipe.setIngredient('I', Material.DIAMOND);
      recipe.setIngredient('P', Material.LEATHER_LEGGINGS);
      this.instance.getServer().addRecipe(recipe);
   }

   private void registerINB() {
      ItemStack s = InfernalNetherite.craftNetheriteBoots();
      NamespacedKey key = new NamespacedKey(this.instance, "infernal_boots");
      ShapedRecipe recipe = new ShapedRecipe(key, s);
      recipe.shape(new String[]{" I ", "IPI", " I "});
      recipe.setIngredient('I', Material.DIAMOND);
      recipe.setIngredient('P', Material.LEATHER_BOOTS);
      this.instance.getServer().addRecipe(recipe);
   }

   private void registerEndRel() {
      ItemStack s = PermaDeathItems.crearReliquia();
      ItemMeta meta = s.getItemMeta();
      meta.setUnbreakable(true);
      s.setItemMeta(meta);
      NamespacedKey key = new NamespacedKey(this.instance, "end_relic");
      ShapedRecipe recipe = new ShapedRecipe(key, s);
      recipe.shape(new String[]{" S ", " D ", " S "});
      recipe.setIngredient('S', Material.SHULKER_SHELL);
      recipe.setIngredient('D', Material.DIAMOND_BLOCK);
      this.instance.getServer().addRecipe(recipe);
   }

   private void registerShulkerUnCraft() {
      Material[] var1 = Material.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Material m = var1[var3];
         if (m.name().toLowerCase().contains("shulker_box")) {
            Bukkit.addRecipe((new ShapelessRecipe(new NamespacedKey(this.instance, m.name() + "_uncraft"), new ItemStack(Material.SHULKER_SHELL, 2))).addIngredient(m));
         }
      }

   }

   private void registerHyperGAP() {
      ItemBuilder var10000 = new ItemBuilder(Material.GOLDEN_APPLE, 1);
      Main var10001 = this.instance;
      ItemStack s = var10000.setDisplayName(Main.format("&6Hyper Golden Apple +")).addEnchant(Enchantment.ARROW_INFINITE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build();
      String id = "hyper_golden_apple";
      NamespacedKey key = new NamespacedKey(this.instance, id);
      ShapedRecipe recipe = new ShapedRecipe(key, s);
      recipe.shape(new String[]{"GGG", "GAG", "GGG"});
      recipe.setIngredient('G', Material.GOLD_BLOCK);
      recipe.setIngredient('A', Material.GOLDEN_APPLE);

      try {
         this.instance.getServer().addRecipe(recipe);
      } catch (Exception var6) {
      }

   }

   private void registerSuperGAP() {
      ItemBuilder var10000 = new ItemBuilder(Material.GOLDEN_APPLE, 1);
      Main var10001 = this.instance;
      ItemStack s = var10000.setDisplayName(Main.format("&6Super Golden Apple +")).addEnchant(Enchantment.ARROW_INFINITE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build();
      NamespacedKey key = new NamespacedKey(this.instance, "super_golden_apple");
      ShapedRecipe recipe = new ShapedRecipe(key, s);
      recipe.shape(new String[]{"GGG", "GAG", "GGG"});
      recipe.setIngredient('G', Material.GOLD_INGOT);
      recipe.setIngredient('A', Material.GOLDEN_APPLE);
      this.instance.getServer().addRecipe(recipe);
   }

   private void registerLifeOrb() {
      ItemStack s = PermaDeathItems.createLifeOrb();
      NamespacedKey key = new NamespacedKey(this.instance, "PERMADEATHCORE_LIFO");
      ShapedRecipe recipe = new ShapedRecipe(key, s);
      recipe.shape(new String[]{"DGB", "RSE", "NOL"});
      recipe.setIngredient('D', Material.DIAMOND);
      recipe.setIngredient('G', Material.GOLD_INGOT);
      recipe.setIngredient('B', Material.BONE_BLOCK);
      recipe.setIngredient('R', Material.BLAZE_ROD);
      recipe.setIngredient('S', Material.HEART_OF_THE_SEA);
      recipe.setIngredient('E', Material.END_STONE);
      recipe.setIngredient('N', Material.NETHER_BRICKS);
      recipe.setIngredient('O', Material.OBSIDIAN);
      recipe.setIngredient('L', Material.LAPIS_BLOCK);
      this.instance.getServer().addRecipe(recipe);
   }
}
