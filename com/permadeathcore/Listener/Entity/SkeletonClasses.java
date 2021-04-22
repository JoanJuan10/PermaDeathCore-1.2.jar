package com.permadeathcore.Listener.Entity;

import com.permadeathcore.Main;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.persistence.PersistentDataType;

public class SkeletonClasses implements Listener {
   private final Main plugin;

   public SkeletonClasses(Main instance) {
      this.plugin = instance;
   }

   @EventHandler
   public void onH(ProjectileHitEvent e) {
      if (e.getEntity().getShooter() instanceof Skeleton) {
         Skeleton s = (Skeleton)e.getEntity().getShooter();
         if (this.plugin.getDays() >= 60L && s.getPersistentDataContainer().has(new NamespacedKey(this.plugin, "demon_skeleton"), PersistentDataType.BYTE)) {
            if (e.getEntity() != null) {
               Entity h = e.getHitEntity();
               h.getWorld().createExplosion(h.getLocation(), 3.0F, true, true, s);
            } else if (e.getHitBlock() != null) {
               e.getEntity().getWorld().createExplosion(e.getHitBlock().getLocation(), 3.0F, true, true, s);
            }
         }

      }
   }

   @EventHandler
   public void onNDamage(EntityDamageByEntityEvent e) {
      if (e.getDamager() instanceof Arrow && ((Arrow)e.getDamager()).getShooter() instanceof Skeleton && ((Skeleton)((Arrow)e.getDamager()).getShooter()).getPersistentDataContainer().has(new NamespacedKey(this.plugin, "skeleton_definitivo"), PersistentDataType.BYTE)) {
         try {
            if (e.getEntity() instanceof LivingEntity) {
               ((LivingEntity)e.getEntity()).damage(((LivingEntity)e.getEntity()).getHealth());
            }
         } catch (Exception var3) {
         }
      }

      if (e.getDamager() instanceof ShulkerBullet) {
         ShulkerBullet b = (ShulkerBullet)e.getDamager();
         if (b.getShooter() instanceof Shulker && ((Shulker)b).getColor() == DyeColor.RED && e.getEntity() != null && e.getEntity().getType() == EntityType.CAVE_SPIDER) {
            e.setCancelled(true);
         }
      }

   }

   @EventHandler
   public void onEntityDamageEvent(EntityDamageEvent e) {
      if (e.getEntity() instanceof Shulker && ((Shulker)e.getEntity()).getColor() == DyeColor.RED && e.getCause() == DamageCause.MAGIC) {
         e.setCancelled(true);
      }

   }
}
