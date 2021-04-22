package com.permadeathcore.Util.Item;

import com.permadeathcore.Main;
import com.permadeathcore.Util.Library.LeatherArmorBuilder;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class NetheriteArmor implements Listener {
   private static Color color = Color.fromRGB(6116957);
   private static String helmetName = Main.format("&5Netherite Helmet");
   private static String chestName = Main.format("&5Netherite Chestplate");
   private static String legName = Main.format("&5Netherite Leggings");
   private static String bootName = Main.format("&5Netherite Boots");

   public static ItemStack craftNetheriteHelmet() {
      ItemStack item = (new LeatherArmorBuilder(Material.LEATHER_HELMET, 1)).setColor(color).setDisplayName(helmetName).build();
      ItemMeta meta = item.getItemMeta();
      EquipmentSlot slot = EquipmentSlot.HEAD;
      AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 3.0D, Operation.ADD_NUMBER, slot);
      meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
      AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", 3.0D, Operation.ADD_NUMBER, slot);
      meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier2);
      meta.setUnbreakable(true);
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack craftNetheriteChest() {
      ItemStack item = (new LeatherArmorBuilder(Material.LEATHER_CHESTPLATE, 1)).setColor(color).setDisplayName(chestName).build();
      ItemMeta meta = item.getItemMeta();
      EquipmentSlot slot = EquipmentSlot.CHEST;
      AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 8.0D, Operation.ADD_NUMBER, slot);
      meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
      AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", 3.0D, Operation.ADD_NUMBER, slot);
      meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier2);
      meta.setUnbreakable(true);
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack craftNetheriteLegs() {
      ItemStack item = (new LeatherArmorBuilder(Material.LEATHER_LEGGINGS, 1)).setColor(color).setDisplayName(legName).build();
      ItemMeta meta = item.getItemMeta();
      EquipmentSlot slot = EquipmentSlot.LEGS;
      AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 6.0D, Operation.ADD_NUMBER, slot);
      meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
      AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", 3.0D, Operation.ADD_NUMBER, slot);
      meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier2);
      meta.setUnbreakable(true);
      item.setItemMeta(meta);
      return item;
   }

   public static ItemStack craftNetheriteBoots() {
      ItemStack item = (new LeatherArmorBuilder(Material.LEATHER_BOOTS, 1)).setColor(color).setDisplayName(bootName).build();
      ItemMeta meta = item.getItemMeta();
      EquipmentSlot slot = EquipmentSlot.FEET;
      AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.armor", 3.0D, Operation.ADD_NUMBER, slot);
      meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
      AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", 3.0D, Operation.ADD_NUMBER, slot);
      meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier2);
      meta.setUnbreakable(true);
      item.setItemMeta(meta);
      return item;
   }

   public static boolean isNetheritePiece(ItemStack s) {
      if (s == null) {
         return false;
      } else {
         return s.hasItemMeta() && s.getItemMeta().isUnbreakable() && ChatColor.stripColor(s.getItemMeta().getDisplayName()).startsWith("Netherite");
      }
   }

   public static boolean isInfernalPiece(ItemStack s) {
      if (s == null) {
         return false;
      } else {
         if (s.hasItemMeta()) {
            if (s.getType() == Material.ELYTRA && s.getItemMeta().getCustomModelData() == 1) {
               return true;
            }

            if (s.getItemMeta().isUnbreakable() && ChatColor.stripColor(s.getItemMeta().getDisplayName()).startsWith("Infernal")) {
               return true;
            }
         }

         return false;
      }
   }

   public static void setupHealth(Player p) {
      Double maxHealth = getAvaibleMaxHealth(p);
      p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
   }

   public static Double getAvaibleMaxHealth(Player p) {
      int currentNetheritePieces = 0;
      int currentInfernalPieces = 0;
      boolean doPlayerAteOne = p.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "hyper_one"), PersistentDataType.BYTE);
      boolean doPlayerAteTwo = p.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "hyper_two"), PersistentDataType.BYTE);
      ItemStack[] var5 = p.getInventory().getArmorContents();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         ItemStack contents = var5[var7];
         if (isNetheritePiece(contents)) {
            ++currentNetheritePieces;
         }

         if (isInfernalPiece(contents)) {
            ++currentInfernalPieces;
         }
      }

      Double maxHealth = 20.0D;
      if (doPlayerAteOne) {
         maxHealth = maxHealth + 4.0D;
      }

      if (doPlayerAteTwo) {
         maxHealth = maxHealth + 4.0D;
      }

      if (currentNetheritePieces >= 4) {
         maxHealth = maxHealth + 8.0D;
      }

      if (currentInfernalPieces >= 4) {
         maxHealth = maxHealth + 10.0D;
         p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 0));
      }

      if (Main.getInstance().getDays() >= 40L) {
         maxHealth = maxHealth - 8.0D;
         if (Main.getInstance().getDays() >= 60L) {
            maxHealth = maxHealth - 8.0D;
            boolean hasOrb = checkForOrb(p);
            if (!hasOrb) {
               maxHealth = maxHealth - 16.0D;
            }
         }
      }

      return Math.max(maxHealth, 1.0E-6D);
   }

   public static boolean checkForOrb(Player p) {
      if (Main.getInstance().getOrbEvent().isRunning()) {
         return true;
      } else {
         ItemStack[] var1 = p.getInventory().getContents();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            ItemStack stack = var1[var3];
            if (stack != null && stack.getItemMeta() != null && stack.getType() == Material.BROWN_DYE && stack.getItemMeta().isUnbreakable()) {
               return true;
            }
         }

         return false;
      }
   }
}
