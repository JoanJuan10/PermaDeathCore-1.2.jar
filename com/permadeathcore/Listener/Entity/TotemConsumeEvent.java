package com.permadeathcore.Listener.Entity;

import com.permadeathcore.Main;
import java.util.Iterator;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.inventory.ItemStack;

public class TotemConsumeEvent implements Listener {
   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void totemNerf(EntityResurrectEvent event) {
      if (event.getEntity() instanceof Player) {
         if (((Player)event.getEntity()).getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING || ((Player)event.getEntity()).getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {
            if (!Main.instance.getConfig().getBoolean("TotemFail.Enable")) {
               return;
            }

            Player p = (Player)event.getEntity();
            String player = p.getName();
            int failProb = 0;
            boolean containsDay;
            if (Main.getInstance().getConfig().contains("TotemFail.FailProbs." + Main.getInstance().getDays())) {
               failProb = (Integer)Objects.requireNonNull(Objects.requireNonNull(Main.instance.getConfig().getInt("TotemFail.FailProbs." + Main.getInstance().getDays())));
               containsDay = true;
            } else {
               System.out.println("[INFO] La probabilidad del tótem se encuentra desactivada para el día: " + Main.getInstance().getDays());
               containsDay = false;
            }

            String totemFail = (String)Objects.requireNonNull(Main.instance.getConfig().getString("TotemFail.ChatMessage"));
            String totemMessage = (String)Objects.requireNonNull(Main.instance.getConfig().getString("TotemFail.PlayerUsedTotemMessage"));
            if (Main.getInstance().getDays() >= 40L) {
               if (Main.getInstance().getDays() < 60L) {
                  totemMessage = (String)Objects.requireNonNull(Main.instance.getConfig().getString("TotemFail.PlayerUsedTotemsMessage").replace("{ammount}", "dos").replace("%player%", player));
               } else {
                  totemMessage = (String)Objects.requireNonNull(Main.instance.getConfig().getString("TotemFail.PlayerUsedTotemsMessage").replace("{ammount}", "tres").replace("%player%", player));
               }
            }

            Iterator var8 = Main.instance.getConfig().getConfigurationSection("TotemFail.FailProbs").getKeys(false).iterator();

            int toShow;
            while(var8.hasNext()) {
               String k = (String)var8.next();

               try {
                  toShow = Integer.valueOf(k);
                  if ((long)toShow == Main.getInstance().getDays()) {
                     containsDay = true;
                  }
               } catch (NumberFormatException var20) {
                  System.out.println("[ERROR] Ha ocurrido un error al cargar la probabilidad de tótem del día '" + k + "'");
               }
            }

            if (!containsDay) {
               return;
            }

            if (failProb >= 101) {
               failProb = 100;
            }

            if (failProb < 0) {
               failProb = 1;
            }

            Main var10000;
            if (failProb == 100) {
               var10000 = Main.instance;
               Bukkit.broadcastMessage(Main.format(totemMessage.replace("%player%", player).replace("%porcent%", "=").replace("%totem_fail%", String.valueOf(100)).replace("%number%", String.valueOf(failProb))));
               var10000 = Main.instance;
               Bukkit.broadcastMessage(Main.format(totemFail.replace("%player%", player)));
               event.setCancelled(true);
            } else {
               int random = (int)(Math.random() * 100.0D) + 1;
               int resta = 100 - failProb;
               toShow = resta;
               if (resta == random) {
                  toShow = resta - 1;
               }

               int raShow = random;
               if (random == resta) {
                  raShow = random - 1;
               }

               if (Main.instance.getDays() < 40L) {
                  if (this.doPlayerHaveSpecialTotem(p)) {
                     ItemStack s = this.getTotem(p);
                     p.getInventory().removeItem(new ItemStack[]{s});
                     var10000 = Main.instance;
                     Bukkit.broadcastMessage(Main.format(Main.instance.getConfig().getString("TotemFail.Medalla").replace("%player%", p.getName())));
                     return;
                  }

                  if (random > resta) {
                     var10000 = Main.instance;
                     Bukkit.broadcastMessage(Main.format(totemMessage.replace("%player%", player).replace("%porcent%", "=").replace("%totem_fail%", String.valueOf(toShow)).replace("%number%", String.valueOf(resta))));
                     var10000 = Main.instance;
                     Bukkit.broadcastMessage(Main.format(totemFail.replace("%player%", player)));
                     event.setCancelled(true);
                  } else {
                     var10000 = Main.instance;
                     Bukkit.broadcastMessage(Main.format(totemMessage.replace("%player%", player).replace("%porcent%", "!=").replace("%totem_fail%", String.valueOf(raShow)).replace("%number%", String.valueOf(resta))));
                  }
               } else {
                  int neededTotems = Main.instance.getDays() < 60L ? 2 : 3;
                  int totems = p.getInventory().all(Material.TOTEM_OF_UNDYING).size();
                  if (p.getInventory().getItemInOffHand() != null && p.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {
                     ++totems;
                  }

                  int removedTotems = 0;
                  boolean hasTotem = this.doPlayerHaveSpecialTotem(p);
                  if (hasTotem) {
                     ItemStack s = this.getTotem(p);
                     if (this.getSpecialTotem(p) == TotemConsumeEvent.EnumPlayerTotemSlot.OFF_HAND) {
                        p.getInventory().setItemInOffHand((ItemStack)null);
                     } else {
                        p.getInventory().removeItem(new ItemStack[]{s});
                     }

                     ++removedTotems;
                  } else if (p.getInventory().getItemInOffHand() != null && p.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {
                     p.getInventory().setItemInOffHand((ItemStack)null);
                     ++removedTotems;
                  }

                  ItemStack[] var24 = p.getInventory().getContents();
                  int var17 = var24.length;

                  for(int var18 = 0; var18 < var17; ++var18) {
                     ItemStack s = var24[var18];
                     if (s != null && s.getType() == Material.TOTEM_OF_UNDYING && removedTotems < neededTotems) {
                        p.getInventory().removeItem(new ItemStack[]{s});
                        ++removedTotems;
                     }
                  }

                  if (totems < neededTotems) {
                     var10000 = Main.instance;
                     Bukkit.broadcastMessage(Main.format(Main.instance.getConfig().getString("TotemFail.NotEnoughTotems").replace("%player%", player).replace("%porcent%", "=").replace("%totem_fail%", String.valueOf(toShow)).replace("%number%", String.valueOf(resta))));
                     event.setCancelled(true);
                     return;
                  }

                  if (hasTotem) {
                     var10000 = Main.instance;
                     Bukkit.broadcastMessage(Main.format(Main.instance.getConfig().getString("TotemFail.Medalla").replace("%player%", p.getName())));
                     return;
                  }

                  if (random > resta) {
                     var10000 = Main.instance;
                     Bukkit.broadcastMessage(Main.format(totemMessage.replace("%player%", player).replace("%porcent%", "=").replace("%totem_fail%", String.valueOf(toShow)).replace("%number%", String.valueOf(resta))));
                     var10000 = Main.instance;
                     Bukkit.broadcastMessage(Main.format(Main.instance.getConfig().getString("TotemFail.ChatMessageTotems").replace("%player%", player)));
                     event.setCancelled(true);
                  } else {
                     var10000 = Main.instance;
                     Bukkit.broadcastMessage(Main.format(totemMessage.replace("%player%", player).replace("%porcent%", "!=").replace("%totem_fail%", String.valueOf(raShow)).replace("%number%", String.valueOf(resta))));
                  }
               }
            }
         }

      }
   }

   private ItemStack getTotem(Player p) {
      return this.getSpecialTotem(p) == TotemConsumeEvent.EnumPlayerTotemSlot.MAIN_HAND ? p.getInventory().getItemInMainHand() : p.getInventory().getItemInOffHand();
   }

   private TotemConsumeEvent.EnumPlayerTotemSlot getSpecialTotem(Player p) {
      ItemStack main = p.getInventory().getItemInMainHand();
      ItemStack off = p.getInventory().getItemInOffHand();
      if (this.isSpecial(main)) {
         return TotemConsumeEvent.EnumPlayerTotemSlot.MAIN_HAND;
      } else {
         return this.isSpecial(off) ? TotemConsumeEvent.EnumPlayerTotemSlot.OFF_HAND : null;
      }
   }

   private boolean doPlayerHaveSpecialTotem(Player p) {
      boolean tieneMedalla = false;
      if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING && p.getInventory().getItemInMainHand().getItemMeta().isUnbreakable()) {
         tieneMedalla = true;
      }

      if (p.getInventory().getItemInOffHand() != null && p.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING && p.getInventory().getItemInOffHand().getItemMeta().isUnbreakable()) {
         tieneMedalla = true;
      }

      return tieneMedalla;
   }

   private boolean isSpecial(ItemStack off) {
      return off != null && off.getType() == Material.TOTEM_OF_UNDYING && off.getItemMeta().isUnbreakable();
   }

   public static enum EnumPlayerTotemSlot {
      MAIN_HAND,
      OFF_HAND;
   }
}
