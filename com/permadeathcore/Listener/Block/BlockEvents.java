package com.permadeathcore.Listener.Block;

import com.permadeathcore.Main;
import com.permadeathcore.Util.Item.PermaDeathItems;
import com.permadeathcore.Util.Manager.Data.EndDataManager;
import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;

public class BlockEvents implements Listener {
   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onBlockBurn(BlockBurnEvent e) {
      if (Main.getInstance().getEndData() != null && Main.getInstance().getDays() >= 30L) {
         EndDataManager ma = Main.getInstance().getEndData();
         if (ma.getConfig().contains("RegenZoneLocation")) {
            Location loc = this.buildLocation(ma.getConfig().getString("RegenZoneLocation"));
            if (e.getBlock().getWorld().getName().equalsIgnoreCase(loc.getWorld().getName()) && e.getBlock().getLocation().distance(loc) <= 10.0D) {
               e.setCancelled(true);
            }
         }
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onBlockExplode(EntityExplodeEvent e) {
      if (Main.getInstance().getEndData() != null) {
         EndDataManager ma = Main.getInstance().getEndData();
         if (ma.getConfig().contains("RegenZoneLocation")) {
            Location loc = this.buildLocation(ma.getConfig().getString("RegenZoneLocation"));
            Iterator var4 = e.blockList().iterator();

            while(var4.hasNext()) {
               Block b = (Block)var4.next();
               if (b.getWorld().getName().equalsIgnoreCase(loc.getWorld().getName()) && b.getLocation().distance(loc) <= 10.0D) {
                  e.setCancelled(true);
               }
            }
         }
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onBlockCombust(BlockIgniteEvent e) {
      if (Main.getInstance().getEndData() != null && Main.getInstance().getDays() >= 30L) {
         EndDataManager ma = Main.getInstance().getEndData();
         if (ma.getConfig().contains("RegenZoneLocation")) {
            Location loc = this.buildLocation(ma.getConfig().getString("RegenZoneLocation"));
            if (e.getBlock().getWorld().getName().equalsIgnoreCase(loc.getWorld().getName()) && e.getBlock().getLocation().distance(loc) <= 3.0D) {
               e.setCancelled(true);
            }
         }
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onBlockPlace(BlockPlaceEvent e) {
      if (Main.getInstance().getEndData() != null && Main.getInstance().getDays() >= 30L) {
         EndDataManager ma = Main.getInstance().getEndData();
         if (ma.getConfig().contains("RegenZoneLocation")) {
            Location loc = this.buildLocation(ma.getConfig().getString("RegenZoneLocation"));
            if (e.getBlock().getWorld().getName().equalsIgnoreCase(loc.getWorld().getName()) && e.getBlock().getLocation().distance(loc) <= 3.0D) {
               e.setCancelled(true);
               Player var10000 = e.getPlayer();
               Main.getInstance();
               var10000.sendMessage(Main.format("&cNo puedes colocar bloques cerca de la Zona de Regeneración."));
            }
         }
      }

   }

   @EventHandler
   public void onBreak(BlockBreakEvent e) {
      if (Main.getInstance().getEndData() != null && Main.getInstance().getDays() >= 30L) {
         EndDataManager ma = Main.getInstance().getEndData();
         if (ma.getConfig().contains("RegenZoneLocation")) {
            Location loc = this.buildLocation(ma.getConfig().getString("RegenZoneLocation"));
            if (e.getBlock().getWorld().getName().equalsIgnoreCase(loc.getWorld().getName()) && e.getBlock().getLocation().distance(loc) <= 4.0D) {
               e.setCancelled(true);
               Player var10000 = e.getPlayer();
               Main.getInstance();
               var10000.sendMessage(Main.format("&cNo puedes romper bloques cerca de la Zona de Regeneración."));
            }
         }
      }

      if (Main.getInstance().getDays() >= 50L) {
         ArrayList<ItemStack> items = new ArrayList();
         items.add(PermaDeathItems.createNetheriteAxe());
         items.add(PermaDeathItems.createNetheriteShovel());
         items.add(PermaDeathItems.createNetheriteSword());
         items.add(PermaDeathItems.createNetheritePickaxe());
         items.add(PermaDeathItems.createNetheriteHoe());
         boolean damage = true;
         if (e.getPlayer().getInventory().getItemInMainHand() != null) {
            Iterator var4 = items.iterator();

            while(var4.hasNext()) {
               ItemStack s = (ItemStack)var4.next();
               ItemStack i = e.getPlayer().getInventory().getItemInMainHand();
               if (i.getType() == s.getType() && i.getItemMeta().isUnbreakable() && s.getItemMeta().isUnbreakable()) {
                  damage = false;
               }
            }
         }

         if (damage) {
            if (Main.instance.getDays() < 60L) {
               e.getPlayer().damage(1.0D);
            } else {
               e.getPlayer().damage(16.0D);
            }
         }
      }

   }

   @EventHandler
   public void onBucketFill(PlayerBucketFillEvent e) {
      if (Main.getInstance().getDays() >= 50L) {
         e.setCancelled(true);
      }

   }

   @EventHandler
   public void onBlockFurnace(FurnaceSmeltEvent e) {
      if (Main.getInstance().getDays() >= 50L && e.getResult() != null) {
         ItemStack resu;
         if (e.getResult().getType() == Material.IRON_INGOT) {
            resu = e.getResult();
            resu.setType(Material.IRON_NUGGET);
            e.setResult(resu);
         }

         if (e.getResult().getType() == Material.GOLD_INGOT) {
            resu = e.getResult();
            resu.setType(Material.GOLD_NUGGET);
            e.setResult(resu);
         }
      }

   }

   private Location buildLocation(String s) {
      String[] split = s.split(";");
      Double x = Double.valueOf(split[0]);
      Double y = Double.valueOf(split[1]);
      Double z = Double.valueOf(split[2]);
      World w = Bukkit.getWorld(split[3]);
      return new Location(w, x, y, z);
   }
}
