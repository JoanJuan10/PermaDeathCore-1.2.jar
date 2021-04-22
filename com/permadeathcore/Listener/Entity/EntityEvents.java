package com.permadeathcore.Listener.Entity;

import com.permadeathcore.Main;
import com.permadeathcore.Util.Manager.EntityTeleport;
import com.permadeathcore.Util.Manager.Data.PlayerDataManager;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.entity.PolarBear;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EntityEvents implements Listener {
   @EventHandler
   public void onVD(VehicleDestroyEvent e) {
      if (e.getVehicle().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "module_minecart"), PersistentDataType.BYTE)) {
         e.setCancelled(true);
      }

   }

   @EventHandler
   public void onExplode(EntityExplodeEvent e) {
      if (e.getEntity() instanceof Creeper) {
         Creeper c = (Creeper)e.getEntity();
         if (c.hasMetadata("nether_creeper") && e.blockList() != null) {
            e.blockList().forEach((block) -> {
               if (block.getType() != Material.BEDROCK) {
                  block.setType(Material.MAGMA_BLOCK);
               }

            });
            e.setCancelled(true);
         }
      }

   }

   @EventHandler
   public void onBreakSkull(EntityPickupItemEvent e) {
      if (!e.isCancelled()) {
         if (e.getEntity() instanceof Player) {
            ItemStack i = e.getItem().getItemStack();
            if (i.getType() == Material.PLAYER_HEAD) {
               SkullMeta meta = (SkullMeta)i.getItemMeta();
               PlayerDataManager man = new PlayerDataManager(meta.getOwner(), Main.instance);
               man.craftHead(i);
            }

            if (i.getType() == Material.STRUCTURE_VOID) {
               e.setCancelled(true);
               e.getItem().remove();
            }
         }

      }
   }

   @EventHandler
   public void onDamage(EntityDamageEvent e) {
      if (e.getCause() == DamageCause.DROWNING && Main.instance.getDays() >= 50L && e.getEntity() instanceof Player) {
         if (Main.instance.getDays() < 60L) {
            e.setDamage(5.0D);
         } else {
            e.setDamage(10.0D);
         }
      }

      if (e.getEntity().getType() == EntityType.DROPPED_ITEM && e.getCause() == DamageCause.ENTITY_EXPLOSION && e.getEntity().getWorld().getEnvironment() == Environment.THE_END) {
         Item item = (Item)e.getEntity();
         if (item.getItemStack().getType() == Material.SHULKER_SHELL) {
            e.setCancelled(true);
         }
      }

      if (e.getEntity() instanceof Creeper || e.getEntity() instanceof Ghast) {
         new EntityTeleport(e.getEntity(), e);
      }

   }

   @EventHandler
   public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
      if (Main.getInstance().getDays() >= 50L) {
         Player p;
         if (e.getEntity() instanceof Player && e.getDamager() instanceof PolarBear) {
            p = (Player)e.getEntity();
            final PolarBear b = (PolarBear)e.getDamager();
            b.setAI(false);
            p.getWorld().playSound(b.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0F, 1.0F);
            final Location l = b.getLocation();
            Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
               public void run() {
                  l.getWorld().createExplosion(l, 1.5F, true, false, b);
                  b.remove();
               }
            }, 10L);
            e.setCancelled(true);
         }

         if (e.getEntity() instanceof Player && e.getDamager() instanceof LlamaSpit) {
            p = (Player)e.getEntity();
            p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 600, 2));
            p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 0));
            p.setVelocity(p.getVelocity().multiply(3));
         }
      }

      if (Main.instance.getDays() >= 60L && e.getDamager() instanceof Drowned) {
         e.setDamage(e.getDamage() * 3.0D);
      }

      if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
         if (Main.getInstance().getDays() >= 40L) {
            e.setCancelled(false);
         } else if (Main.getInstance().getDays() <= 39L) {
            e.setCancelled(true);
         }
      }

      if (e.getDamager().getType() == EntityType.FIREBALL) {
         Fireball f = (Fireball)e.getDamager();
         if (f.getShooter() instanceof Ghast) {
            Ghast ghast = (Ghast)f.getShooter();
            if (ghast.getPersistentDataContainer().has(new NamespacedKey(Main.instance, "demonio_flotante"), PersistentDataType.BYTE)) {
               Entity entity = e.getEntity();
               if (entity instanceof LivingEntity) {
                  LivingEntity liv = (LivingEntity)entity;
                  liv.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 100, 49));
                  liv.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 400, 4));
               }
            }
         }
      }

   }

   @EventHandler
   public void onFireBallHit(ProjectileLaunchEvent e) {
      if (e.getEntity().getShooter() instanceof Ghast && Main.instance.getDays() >= 25L) {
         Ghast ghast = (Ghast)e.getEntity().getShooter();
         Fireball f = (Fireball)e.getEntity();
         int yield = e.getEntity().getWorld().getEnvironment() != Environment.THE_END && Main.instance.getDays() < 50L ? ThreadLocalRandom.current().nextInt(3, 6) : 6;
         if (ghast.getPersistentDataContainer().has(new NamespacedKey(Main.instance, "demonio_flotante"), PersistentDataType.BYTE)) {
            yield = 0;
         }

         if (e.getEntity() instanceof Fireball) {
            f.setYield((float)yield);
         }
      }

   }
}
