package com.permadeathcore.Task;

import com.permadeathcore.Main;
import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.EnderDragon.Phase;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class MovesTask extends BukkitRunnable {
   private float yaw;
   private Main main;
   private EnderDragon dragon;
   int ticksRotating = 0;
   int currentPitchRotation = -360;
   Location teleportLocation;
   boolean spawnedPaticles = false;

   public MovesTask(Main main, EnderDragon dragon, Location tp) {
      this.main = main;
      this.dragon = dragon;
      this.teleportLocation = tp;
   }

   public void run() {
      if (!this.dragon.isDead() && this.main.getTask() != null) {
         int ticks = 300;
         if (this.ticksRotating == ticks) {
            this.cancel();
         } else {
            this.ticksRotating += 5;
            if (this.currentPitchRotation >= 0) {
               this.currentPitchRotation = -360;
            }

            if (this.currentPitchRotation < 0) {
               if (this.dragon.getPhase() != Phase.LAND_ON_PORTAL) {
                  this.dragon.setPhase(Phase.LAND_ON_PORTAL);
               }

               this.dragon.setRotation((float)this.currentPitchRotation, 0.0F);
               this.currentPitchRotation += 30;
               if (!this.spawnedPaticles) {
                  this.spawnedPaticles = true;
                  ArrayList<Location> locations = new ArrayList();
                  locations.add(this.main.endWorld.getHighestBlockAt(this.dragon.getLocation().add(7.0D, 0.0D, 7.0D)).getLocation());
                  locations.add(this.main.endWorld.getHighestBlockAt(this.dragon.getLocation().add(7.0D, 0.0D, 0.0D)).getLocation());
                  locations.add(this.main.endWorld.getHighestBlockAt(this.dragon.getLocation().add(7.0D, 0.0D, -7.0D)).getLocation());
                  locations.add(this.main.endWorld.getHighestBlockAt(this.dragon.getLocation().add(0.0D, 0.0D, -7.0D)).getLocation());
                  locations.add(this.main.endWorld.getHighestBlockAt(this.dragon.getLocation().add(0.0D, 0.0D, 7.0D)).getLocation());
                  locations.add(this.main.endWorld.getHighestBlockAt(this.dragon.getLocation().add(-7.0D, 0.0D, 7.0D)).getLocation());
                  locations.add(this.main.endWorld.getHighestBlockAt(this.dragon.getLocation().add(-7.0D, 0.0D, 0.0D)).getLocation());
                  locations.add(this.main.endWorld.getHighestBlockAt(this.dragon.getLocation().add(-7.0D, 0.0D, -7.0D)).getLocation());
                  Iterator var3 = locations.iterator();

                  while(var3.hasNext()) {
                     Location locs = (Location)var3.next();
                     AreaEffectCloud a = (AreaEffectCloud)locs.getWorld().spawnEntity(locs.add(0.0D, 1.0D, 0.0D), EntityType.AREA_EFFECT_CLOUD);
                     a.setParticle(Particle.CLOUD);
                     a.setRadius(5.0F);
                     a.setDuration(300);
                     a.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 5, 3), false);
                     a.setColor(Color.WHITE);
                  }
               }
            }

         }
      } else {
         this.cancel();
      }
   }
}
