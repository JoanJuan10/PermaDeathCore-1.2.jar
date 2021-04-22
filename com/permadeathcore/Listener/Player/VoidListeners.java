package com.permadeathcore.Listener.Player;

import com.permadeathcore.Main;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VoidListeners implements Listener {
   private Main main;
   private ArrayList<Entity> gatosSupernova = new ArrayList();

   public VoidListeners(Main main) {
      this.main = main;
   }

   @EventHandler
   public void onDrop(PlayerDropItemEvent e) {
      if (!e.isCancelled()) {
         if (e.getItemDrop().getItemStack() != null && e.getItemDrop().getItemStack().getType() == Material.STRUCTURE_VOID) {
            e.setCancelled(true);
         }

      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onClickVoid(InventoryClickEvent e) {
      if (!e.isCancelled()) {
         if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.STRUCTURE_VOID) {
            e.setCancelled(true);
            if (e.getClick() == ClickType.NUMBER_KEY) {
               e.getInventory().remove(Material.STRUCTURE_VOID);
            }
         }

         if (e.getCursor() != null && e.getCursor().getType() == Material.STRUCTURE_VOID) {
            e.setCancelled(true);
         }

      }
   }

   @EventHandler
   public void onItemCraft(PrepareItemCraftEvent e) {
      if (e.getInventory() != null && e.getInventory().getResult() != null && (e.getInventory().getResult().getType() == Material.TORCH || e.getInventory().getResult().getType() == Material.REDSTONE_TORCH)) {
         e.getInventory().setResult((ItemStack)null);
      }

   }

   @EventHandler
   public void onPlace(BlockPlaceEvent e) {
      if (e.getBlock().getType() == Material.STRUCTURE_VOID) {
         e.setCancelled(true);
      }

   }

   @EventHandler
   public void onSwap(PlayerSwapHandItemsEvent e) {
      if (!e.isCancelled()) {
         if (e.getOffHandItem() != null && e.getOffHandItem().getType() == Material.STRUCTURE_VOID) {
            e.setCancelled(true);
         }

         if (e.getMainHandItem() != null && e.getMainHandItem().getType() == Material.STRUCTURE_VOID) {
            e.setCancelled(true);
         }

      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onMoveItem(InventoryMoveItemEvent e) {
      if (!e.isCancelled()) {
         if (e.getItem() != null && e.getItem().getType() == Material.STRUCTURE_VOID) {
            e.setCancelled(true);
         }

      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onPickup(InventoryPickupItemEvent e) {
      if (!e.isCancelled()) {
         if (e.getItem().getItemStack() != null && e.getItem().getItemStack().getType() == Material.STRUCTURE_VOID) {
            e.setCancelled(true);
         }

      }
   }

   @EventHandler
   public void onWitchThrow(ProjectileLaunchEvent e) {
      if (this.main.getDays() >= 40L) {
         if (e.getEntity().getShooter() instanceof Witch && e.getEntity() instanceof ThrownPotion) {
            ThrownPotion potion = (ThrownPotion)e.getEntity();
            int prob = (new Random()).nextInt(2) + 1;
            ItemStack s;
            PotionMeta meta;
            Iterator var6;
            PotionEffect effect;
            if (prob == 1) {
               s = new ItemStack(Material.SPLASH_POTION);
               meta = (PotionMeta)s.getItemMeta();
               if (!meta.getCustomEffects().isEmpty() || meta.getCustomEffects().size() >= 1) {
                  var6 = meta.getCustomEffects().iterator();

                  while(var6.hasNext()) {
                     effect = (PotionEffect)var6.next();
                     meta.removeCustomEffect(effect.getType());
                  }
               }

               meta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 20, 3), true);
               s.setItemMeta(meta);
               potion.setItem(s);
            } else if (prob == 2) {
               int min = 300;
               ItemStack s = new ItemStack(Material.SPLASH_POTION);
               PotionMeta meta = (PotionMeta)s.getItemMeta();
               if (!meta.getCustomEffects().isEmpty() || meta.getCustomEffects().size() >= 1) {
                  Iterator var12 = meta.getCustomEffects().iterator();

                  while(var12.hasNext()) {
                     PotionEffect effect = (PotionEffect)var12.next();
                     meta.removeCustomEffect(effect.getType());
                  }
               }

               meta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, min * 20, 2), true);
               s.setItemMeta(meta);
               potion.setItem(s);
            } else {
               s = new ItemStack(Material.SPLASH_POTION);
               meta = (PotionMeta)s.getItemMeta();
               if (!meta.getCustomEffects().isEmpty() || meta.getCustomEffects().size() >= 1) {
                  var6 = meta.getCustomEffects().iterator();

                  while(var6.hasNext()) {
                     effect = (PotionEffect)var6.next();
                     meta.removeCustomEffect(effect.getType());
                  }
               }

               meta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 400, 4), true);
               s.setItemMeta(meta);
               potion.setItem(s);
            }
         }

      }
   }

   @EventHandler
   public void onDrag(InventoryDragEvent e) {
      if (!e.getNewItems().isEmpty()) {
         Iterator var2 = e.getNewItems().keySet().iterator();

         while(var2.hasNext()) {
            int i = (Integer)var2.next();
            ItemStack s = (ItemStack)e.getNewItems().get(i);
            if (s != null && s.getType() == Material.STRUCTURE_VOID) {
               e.getInventory().removeItem(new ItemStack[]{s});
            }
         }
      }

   }

   @EventHandler
   public void onIntWithEndRelic(PlayerInteractEvent e) {
      if (e.getPlayer().getInventory().getItemInMainHand() != null && this.esReliquia(e.getPlayer(), e.getPlayer().getInventory().getItemInMainHand())) {
         e.setCancelled(true);
      }

      if (e.getPlayer().getInventory().getItemInOffHand() != null && this.esReliquia(e.getPlayer(), e.getPlayer().getInventory().getItemInOffHand())) {
         e.setCancelled(true);
      }

   }

   public boolean esReliquia(Player p, ItemStack stack) {
      if (stack == null) {
         return false;
      } else if (!stack.hasItemMeta()) {
         return false;
      } else {
         if (stack.getType() == Material.LIGHT_BLUE_DYE) {
            String var10000 = stack.getItemMeta().getDisplayName();
            Main var10001 = this.main;
            if (var10000.endsWith(Main.format("&6Reliquia Del Fin"))) {
               return true;
            }
         }

         return false;
      }
   }
}
