package com.permadeathcore.Util.Item;

import com.permadeathcore.Main;
import com.permadeathcore.Util.Library.LeatherArmorBuilder;
import java.util.UUID;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class InfernalNetherite implements Listener {
   private static Color color = Color.fromRGB(16711680);
   private static String helmetName = Main.format("&5Infernal Netherite Helmet");
   private static String chestName = Main.format("&5Infernal Netherite Chestplate");
   private static String legName = Main.format("&5Infernal Netherite Leggings");
   private static String bootName = Main.format("&5Infernal Netherite Boots");

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
      ItemStack item = (new LeatherArmorBuilder(Material.LEATHER_BOOTS, 1)).setColor(Color.fromRGB(11277847)).setColor(color).setDisplayName(bootName).build();
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
}
