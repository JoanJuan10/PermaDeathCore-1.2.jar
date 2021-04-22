package com.permadeathcore.Listener.Player;

import com.permadeathcore.Main;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class AnvilListener implements Listener {
   private final Main plugin;
   private String helmetName;
   private String chestName;
   private String legName;
   private String bootName;

   public AnvilListener(Main instance) {
      this.plugin = instance;
      this.helmetName = Main.format("&5Netherite Helmet");
      this.chestName = Main.format("&5Netherite Chestplate");
      this.legName = Main.format("&5Netherite Leggings");
      this.bootName = Main.format("&5Netherite Boots");
   }

   @EventHandler
   public void onAnvil(InventoryClickEvent e) {
      if (e.getCurrentItem() != null) {
         if (e.getCurrentItem().getType() != Material.AIR) {
            Main var10000;
            LeatherArmorMeta meta;
            ItemStack item;
            String name;
            Material type;
            if (e.getInventory().getType() == InventoryType.ANVIL && e.getSlotType() == SlotType.RESULT && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().hasDisplayName()) {
               if (e.getCurrentItem().getType().name().toLowerCase().contains("diamond_") && e.getCurrentItem().getItemMeta().isUnbreakable()) {
                  String name = "";
                  Material type = e.getCurrentItem().getType();
                  if (type == Material.DIAMOND_SWORD) {
                     name = "Espada de Netherite";
                  } else if (type == Material.DIAMOND_PICKAXE) {
                     name = "Pico de Netherite";
                  } else if (type == Material.DIAMOND_AXE) {
                     name = "Hacha de Netherite";
                  } else if (type == Material.DIAMOND_HOE) {
                     name = "Azada de Netherite";
                  } else if (type == Material.DIAMOND_SHOVEL) {
                     name = "Pala de Netherite";
                  }

                  if (!name.isEmpty()) {
                     ItemMeta meta = e.getCurrentItem().getItemMeta();
                     Main var10001 = this.plugin;
                     meta.setDisplayName(Main.format("&6" + name));
                     e.getCurrentItem().setItemMeta(meta);
                  }
               }

               if (e.getCurrentItem().getType() == Material.LEATHER_HELMET || e.getCurrentItem().getType() == Material.LEATHER_CHESTPLATE || e.getCurrentItem().getType() == Material.LEATHER_LEGGINGS || e.getCurrentItem().getType() == Material.LEATHER_BOOTS) {
                  meta = (LeatherArmorMeta)e.getCurrentItem().getItemMeta();
                  item = e.getCurrentItem();
                  name = "";
                  type = item.getType();
                  if (meta.isUnbreakable() && type == Material.LEATHER_BOOTS) {
                     name = this.bootName;
                  } else if (meta.isUnbreakable() && type == Material.LEATHER_HELMET) {
                     name = this.helmetName;
                  } else if (meta.isUnbreakable() && type == Material.LEATHER_CHESTPLATE) {
                     name = this.chestName;
                  } else if (meta.isUnbreakable() && type == Material.LEATHER_LEGGINGS) {
                     name = this.legName;
                  }

                  if ((meta.getColor().equals(Color.fromRGB(16711680)) || meta.getColor() == Color.fromRGB(16711680)) && !name.isEmpty()) {
                     var10000 = this.plugin;
                     name = Main.format("&5Infernal " + ChatColor.stripColor(name));
                  }

                  if (!name.isEmpty()) {
                     meta.setDisplayName(name);
                     e.getCurrentItem().setItemMeta(meta);
                  }
               }
            } else if ((e.getCurrentItem().getType() == Material.LEATHER_HELMET || e.getCurrentItem().getType() == Material.LEATHER_CHESTPLATE || e.getCurrentItem().getType() == Material.LEATHER_LEGGINGS || e.getCurrentItem().getType() == Material.LEATHER_BOOTS) && e.getCurrentItem().getItemMeta().isUnbreakable()) {
               meta = (LeatherArmorMeta)e.getCurrentItem().getItemMeta();
               item = e.getCurrentItem();
               name = "";
               type = item.getType();
               if (meta.isUnbreakable() && type == Material.LEATHER_BOOTS) {
                  name = this.bootName;
               } else if (meta.isUnbreakable() && type == Material.LEATHER_HELMET) {
                  name = this.helmetName;
               } else if (meta.isUnbreakable() && type == Material.LEATHER_CHESTPLATE) {
                  name = this.chestName;
               } else if (meta.isUnbreakable() && type == Material.LEATHER_LEGGINGS) {
                  name = this.legName;
               }

               if ((meta.getColor().equals(Color.fromRGB(16711680)) || meta.getColor() == Color.fromRGB(16711680)) && !name.isEmpty()) {
                  var10000 = this.plugin;
                  name = Main.format("&5Infernal " + ChatColor.stripColor(name));
               }

               if (!name.isEmpty()) {
                  meta.setDisplayName(name);
                  e.getCurrentItem().setItemMeta(meta);
               }
            }

         }
      }
   }
}
