package com.permadeathcore.NMS;

import com.permadeathcore.Main;
import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Vindicator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

public class PeaceToHostileManager implements Listener {
   private Main instance;
   private ArrayList<Entity> peaceMobs = new ArrayList();

   public PeaceToHostileManager(Main instance) {
      this.instance = instance;
      this.initialize();
   }

   public void initialize() {
      if (this.instance.getDays() >= 20L && !VersionManager.isRunningNetherUpdate_v2()) {
         Iterator var1 = Bukkit.getWorlds().iterator();

         while(var1.hasNext()) {
            World w = (World)var1.next();
            Iterator var3 = w.getEntities().iterator();

            while(var3.hasNext()) {
               Entity entity = (Entity)var3.next();
               EntityType type = entity.getType();
               if (!this.isHostileMob(type) && entity instanceof LivingEntity) {
                  if (type == EntityType.ENDERMAN || type == EntityType.WITHER || type == EntityType.ENDER_DRAGON) {
                     return;
                  }

                  if (type == EntityType.DOLPHIN || type == EntityType.FOX || type == EntityType.WOLF || type == EntityType.CAT || type == EntityType.OCELOT || type == EntityType.PANDA || type == EntityType.POLAR_BEAR || type == EntityType.SNOWMAN) {
                     this.instance.getNmsAccesor().injectHostilePathfinders(entity);
                     return;
                  }

                  this.instance.getNmsAccesor().injectHostilePathfinders(entity);
                  this.instance.getNmsAccesor().registerAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 8.0D, entity);
               }
            }
         }
      }

   }

   @EventHandler
   public void onSpawn(CreatureSpawnEvent e) {
      if (!e.isCancelled() && !VersionManager.isRunningNetherUpdate_v2()) {
         if (this.instance.getDays() >= 20L) {
            LivingEntity entity = e.getEntity();
            if (entity instanceof LivingEntity) {
               if (entity instanceof Player) {
                  return;
               }

               if (!this.isHostileMob(e.getEntityType())) {
                  EntityType type = e.getEntityType();
                  if (type == EntityType.ENDERMAN || type == EntityType.WITHER || type == EntityType.ENDER_DRAGON) {
                     return;
                  }

                  if (type == EntityType.DOLPHIN || type == EntityType.FOX || type == EntityType.WOLF || type == EntityType.CAT || type == EntityType.OCELOT || type == EntityType.PANDA || type == EntityType.POLAR_BEAR || type == EntityType.SNOWMAN) {
                     this.instance.getNmsAccesor().injectHostilePathfinders(entity);
                     return;
                  }

                  this.instance.getNmsAccesor().injectHostilePathfinders(entity);
                  this.instance.getNmsAccesor().registerAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 8.0D, entity);
               }
            }
         }

      }
   }

   @EventHandler
   public void onChunkLoad(ChunkLoadEvent e) {
      if (this.instance.getDays() >= 20L && !VersionManager.isRunningNetherUpdate_v2()) {
         if (!e.isNewChunk()) {
            if (e.getChunk().getEntities().length >= 1) {
               Entity[] var2 = e.getChunk().getEntities();
               int var3 = var2.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                  Entity entity = var2[var4];
                  boolean isNull = false;
                  if (entity == null) {
                     isNull = true;
                     return;
                  }

                  if (!entity.isValid() || entity.isDead()) {
                     isNull = true;
                  }

                  if (entity instanceof Villager && this.instance.getDays() >= 60L) {
                     Location savedLocation = entity.getLocation();
                     entity.remove();
                     savedLocation.getWorld().spawn(savedLocation, Vindicator.class);
                     return;
                  }

                  if (entity instanceof LivingEntity && !isNull) {
                     if (entity instanceof Player) {
                        return;
                     }

                     EntityType type = entity.getType();
                     if (!this.isHostileMob(type)) {
                        if (type == EntityType.ENDERMAN || type == EntityType.WITHER || type == EntityType.ENDER_DRAGON) {
                           return;
                        }

                        if (type == EntityType.DOLPHIN || type == EntityType.FOX || type == EntityType.WOLF || type == EntityType.CAT || type == EntityType.OCELOT || type == EntityType.PANDA || type == EntityType.POLAR_BEAR || type == EntityType.SNOWMAN) {
                           this.instance.getNmsAccesor().injectHostilePathfinders(entity);
                           return;
                        }

                        this.instance.getNmsAccesor().injectHostilePathfinders(entity);
                        this.instance.getNmsAccesor().registerAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 8.0D, entity);
                     }
                  }
               }

            }
         }
      }
   }

   public boolean isHostileMob(EntityType type) {
      return type == EntityType.ENDER_DRAGON || type == EntityType.WITHER || type == EntityType.BLAZE || type == EntityType.CREEPER || type == EntityType.GHAST || type == EntityType.MAGMA_CUBE || type == EntityType.SILVERFISH || type == EntityType.SKELETON || type == EntityType.SLIME || type == EntityType.ZOMBIE || type == EntityType.ZOMBIE_VILLAGER || type == EntityType.DROWNED || type == EntityType.WITHER_SKELETON || type == EntityType.WITCH || type == EntityType.PILLAGER || type == EntityType.EVOKER || type == EntityType.VINDICATOR || type == EntityType.RAVAGER || type == EntityType.VEX || type == EntityType.GUARDIAN || type == EntityType.ELDER_GUARDIAN || type == EntityType.SHULKER || type == EntityType.HUSK || type == EntityType.STRAY || type == EntityType.PHANTOM;
   }
}
