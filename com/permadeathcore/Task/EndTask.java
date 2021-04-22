package com.permadeathcore.Task;

import com.permadeathcore.Main;
import com.permadeathcore.End.Util.DemonCurrentAttack;
import com.permadeathcore.End.Util.DemonPhase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SplittableRandom;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.EnderDragon.Phase;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class EndTask extends BukkitRunnable {
   private Map<Location, Integer> regenTime = new HashMap();
   private Location teleportLocation;
   private DemonCurrentAttack currentAttack;
   private DemonPhase currentDemonPhase;
   private MovesTask currentMovesTask;
   private EnderDragon enderDragon;
   private Main main;
   private int timeForTnT;
   private int nextDragonAttack;
   private int lightingDuration;
   private int nightVisionDuration;
   private int timeForEnd360;
   private boolean nightVision;
   private boolean isDied;
   private boolean attack360;
   private boolean lightingRain;
   private boolean canMakeAnAttack;
   private boolean decided;
   private Location eggLocation;
   private SplittableRandom random;

   public EndTask(Main plugin, EnderDragon enderDragon) {
      this.currentAttack = DemonCurrentAttack.NONE;
      this.currentDemonPhase = DemonPhase.NORMAL;
      this.currentMovesTask = null;
      this.timeForTnT = 30;
      this.nextDragonAttack = 20;
      this.lightingDuration = 5;
      this.nightVisionDuration = 5;
      this.timeForEnd360 = 20;
      this.nightVision = false;
      this.attack360 = false;
      this.lightingRain = false;
      this.canMakeAnAttack = true;
      this.decided = false;
      this.random = new SplittableRandom();
      this.main = plugin;
      this.isDied = false;
      this.enderDragon = enderDragon;

      int y;
      for(y = this.main.endWorld.getMaxHeight() - 1; y > 0 && this.main.endWorld.getBlockAt(0, y, 0).getType() != Material.BEDROCK; --y) {
      }

      this.eggLocation = this.main.endWorld.getHighestBlockAt(new Location(this.main.endWorld, 0.0D, (double)y, 0.0D)).getLocation();
      enderDragon.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((double)Main.instance.getConfig().getInt("Toggles.End.PermadeathDemon.Health"));
      enderDragon.setHealth((double)Main.instance.getConfig().getInt("Toggles.End.PermadeathDemon.Health"));
      this.teleportLocation = this.eggLocation.clone().add(0.0D, 2.0D, 0.0D);
      this.teleportLocation.setPitch(enderDragon.getLocation().getPitch());
      Iterator var4 = enderDragon.getWorld().getEntitiesByClass(Ghast.class).iterator();

      while(var4.hasNext()) {
         Entity all = (Entity)var4.next();
         all.remove();
      }

   }

   public void run() {
      if (!this.isDied && !this.enderDragon.isDead()) {
         this.tickTnTAttack();
         this.tickLightingRain();
         this.tickNightVision();
         this.tick360Attack();
         this.tickDemonPhase();
         this.tickRandomLighting();
         this.tickEnderCrystals();
         this.tickDragonAttacks();
      } else {
         this.main.setTask((EndTask)null);
         this.cancel();
      }
   }

   private void tickEnderCrystals() {
      if (!this.regenTime.isEmpty()) {
         Iterator var1 = this.regenTime.keySet().iterator();

         while(var1.hasNext()) {
            Location loc = (Location)var1.next();
            int time = (Integer)this.regenTime.get(loc);
            if (time >= 1) {
               this.regenTime.replace(loc, time, time - 1);
            } else {
               loc.getWorld().spawnEntity(loc, EntityType.ENDER_CRYSTAL);
               this.regenTime.remove(loc);
               if (loc.getWorld().getBlockAt(loc) != null) {
                  if (loc.getWorld().getBlockAt(loc).getType() == Material.BEDROCK || loc.getWorld().getBlockAt(loc).getType() == Material.AIR) {
                     return;
                  }

                  loc.getWorld().getBlockAt(loc).setType(Material.AIR);
               }
            }
         }
      }

   }

   private void tickRandomLighting() {
      int x = (this.random.nextBoolean() ? 1 : -1) * this.random.nextInt(21);
      int z = (this.random.nextBoolean() ? 1 : -1) * this.random.nextInt(21);
      int y = this.main.endWorld.getHighestBlockYAt(x, z);
      if (y >= 0) {
         this.main.endWorld.strikeLightning(new Location(this.main.endWorld, (double)x, (double)y, (double)z));
      }
   }

   private void tickDemonPhase() {
      EnderDragon dragon;
      if (this.currentDemonPhase == DemonPhase.ENRAGED) {
         dragon = this.enderDragon;
         dragon.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 7));
         Main var10001 = this.main;
         dragon.setCustomName(Main.format(this.main.getConfig().getString("Toggles.End.PermadeathDemon.DisplayNameEnraged")));
      } else {
         dragon = this.enderDragon;
         dragon.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 5));
      }

   }

   private void tick360Attack() {
      if (this.enderDragon.getLocation().distance(this.eggLocation) >= 10.0D && this.decided) {
         this.decided = false;
      }

      if (this.enderDragon.getLocation().distance(this.eggLocation) <= 3.0D && !this.decided) {
         this.decided = true;
         this.enderDragon.setRotation(this.enderDragon.getLocation().getPitch(), 0.0F);
         if (this.random.nextInt(10) <= 7) {
            this.start360attack();
         }
      }

      if (this.attack360) {
         this.canMakeAnAttack = false;
         if (this.timeForEnd360 >= 1) {
            --this.timeForEnd360;
         }

         if (this.timeForEnd360 >= 16) {
            EnderDragon dragon = this.enderDragon;
            if (dragon.getPhase() != Phase.LAND_ON_PORTAL) {
               dragon.setPhase(Phase.LAND_ON_PORTAL);
            }

            dragon.teleport(this.teleportLocation);
         }

         if (this.timeForEnd360 == 15) {
            this.currentMovesTask = new MovesTask(this.main, this.enderDragon, this.teleportLocation);
            this.currentMovesTask.runTaskTimer(this.main, 5L, 5L);
         }

         if (this.timeForEnd360 == 0) {
            if (this.currentMovesTask != null) {
               this.currentMovesTask.cancel();
               this.currentMovesTask = null;
            }

            this.canMakeAnAttack = true;
            this.timeForEnd360 = 20;
            this.attack360 = false;
            this.enderDragon.setPhase(Phase.LEAVE_PORTAL);
         }
      }

   }

   private void tickDragonAttacks() {
      if (this.nextDragonAttack >= 1) {
         --this.nextDragonAttack;
      } else if (this.nextDragonAttack == 0) {
         if (this.getCurrentDemonPhase() == DemonPhase.NORMAL) {
            this.nextDragonAttack = 60;
         } else {
            this.nextDragonAttack = 40;
         }

         if (this.canMakeAnAttack) {
            this.chooseAnAttack();
         } else {
            this.currentAttack = DemonCurrentAttack.NONE;
         }

         if (this.currentAttack == DemonCurrentAttack.NONE) {
            return;
         }

         if (this.currentAttack == DemonCurrentAttack.ENDERMAN_BUFF) {
            int endermanschoosed = 0;
            ArrayList<Enderman> endermen = new ArrayList();
            Iterator var3 = this.main.endWorld.getEntitiesByClass(Enderman.class).iterator();

            Enderman mans;
            while(var3.hasNext()) {
               mans = (Enderman)var3.next();
               Location backUp = mans.getLocation();
               backUp.setY(0.0D);
               if (this.eggLocation.distance(backUp) <= 35.0D && endermanschoosed < 4) {
                  ++endermanschoosed;
                  endermen.add(mans);
               }
            }

            if (!endermen.isEmpty()) {
               var3 = endermen.iterator();

               while(var3.hasNext()) {
                  mans = (Enderman)var3.next();
                  AreaEffectCloud a = (AreaEffectCloud)this.main.endWorld.spawnEntity(this.main.endWorld.getHighestBlockAt(mans.getLocation()).getLocation().add(0.0D, 1.0D, 0.0D), EntityType.AREA_EFFECT_CLOUD);
                  a.setRadius(10.0F);
                  a.setParticle(Particle.VILLAGER_HAPPY);
                  a.setColor(Color.GREEN);
                  a.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 999999, 0), false);
                  mans.setInvulnerable(true);
               }
            }
         } else if (this.currentAttack == DemonCurrentAttack.LIGHTING_RAIN) {
            this.lightingRain = true;
            this.lightingDuration = 5;
         } else if (this.currentAttack == DemonCurrentAttack.NIGHT_VISION) {
            this.nightVision = true;
            this.nightVisionDuration = 5;
            Iterator var6 = this.main.endWorld.getPlayers().iterator();

            while(var6.hasNext()) {
               Player all = (Player)var6.next();
               all.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 140, 0));
            }
         }
      }

   }

   private void tickTnTAttack() {
      --this.timeForTnT;
      if (this.timeForTnT == 0) {
         if (this.enderDragon.getPhase() != Phase.DYING && !this.attack360 && this.enderDragon.getLocation().distance(this.eggLocation) >= 15.0D) {
            TNTPrimed tnt1 = (TNTPrimed)this.enderDragon.getWorld().spawnEntity(this.enderDragon.getLocation().add(3.0D, 0.0D, -3.0D), EntityType.PRIMED_TNT);
            tnt1.setFuseTicks(60);
            tnt1.setYield(tnt1.getYield() * 2.0F);
            tnt1.setCustomName("dragontnt");
            tnt1.setCustomNameVisible(false);
            TNTPrimed tnt2 = (TNTPrimed)this.enderDragon.getWorld().spawnEntity(this.enderDragon.getLocation().add(3.0D, 0.0D, 3.0D), EntityType.PRIMED_TNT);
            tnt2.setFuseTicks(60);
            tnt2.setYield(tnt2.getYield() * 2.0F);
            tnt2.setCustomName("dragontnt");
            tnt2.setCustomNameVisible(false);
            TNTPrimed tnt3 = (TNTPrimed)this.enderDragon.getWorld().spawnEntity(this.enderDragon.getLocation().add(3.0D, 0.0D, 0.0D), EntityType.PRIMED_TNT);
            tnt3.setFuseTicks(60);
            tnt3.setYield(tnt3.getYield() * 2.0F);
            tnt3.setCustomName("dragontnt");
            tnt3.setCustomNameVisible(false);
            TNTPrimed tnt4 = (TNTPrimed)this.enderDragon.getWorld().spawnEntity(this.enderDragon.getLocation().add(-3.0D, 0.0D, 3.0D), EntityType.PRIMED_TNT);
            tnt4.setFuseTicks(60);
            tnt4.setYield(tnt4.getYield() * 2.0F);
            tnt4.setCustomName("dragontnt");
            tnt4.setCustomNameVisible(false);
            TNTPrimed tnt5 = (TNTPrimed)this.enderDragon.getWorld().spawnEntity(this.enderDragon.getLocation().add(-3.0D, 0.0D, -3.0D), EntityType.PRIMED_TNT);
            tnt5.setFuseTicks(60);
            tnt5.setYield(tnt5.getYield() * 2.0F);
            tnt5.setCustomName("dragontnt");
            tnt5.setCustomNameVisible(false);
            TNTPrimed tnt6 = (TNTPrimed)this.enderDragon.getWorld().spawnEntity(this.enderDragon.getLocation().add(-3.0D, 0.0D, 0.0D), EntityType.PRIMED_TNT);
            tnt6.setFuseTicks(60);
            tnt6.setYield(tnt6.getYield() * 2.0F);
            tnt6.setCustomName("dragontnt");
            tnt6.setCustomNameVisible(false);
         }

         this.timeForTnT = 30 + this.random.nextInt(61);
      }

   }

   private void tickLightingRain() {
      if (this.lightingRain) {
         if (this.lightingDuration >= 1) {
            this.canMakeAnAttack = false;
            --this.lightingDuration;
            Iterator var1 = this.main.endWorld.getPlayers().iterator();

            while(var1.hasNext()) {
               Player all = (Player)var1.next();
               this.main.endWorld.strikeLightning(all.getLocation());
               if (this.currentDemonPhase == DemonPhase.ENRAGED) {
                  all.damage(1.0D);
               }
            }
         } else {
            this.lightingRain = false;
            this.lightingDuration = 5;
            this.canMakeAnAttack = true;
         }
      }

   }

   private void tickNightVision() {
      if (this.nightVision) {
         if (this.nightVisionDuration >= 1) {
            --this.nightVisionDuration;
         } else {
            Iterator var1 = this.main.endWorld.getPlayers().iterator();

            while(var1.hasNext()) {
               Player all = (Player)var1.next();
               Location highest;
               AreaEffectCloud eff;
               if (this.currentDemonPhase == DemonPhase.NORMAL) {
                  highest = this.main.endWorld.getHighestBlockAt(all.getLocation()).getLocation().add(0.0D, 1.0D, 0.0D);
                  eff = (AreaEffectCloud)this.main.endWorld.spawnEntity(highest, EntityType.AREA_EFFECT_CLOUD);
                  eff.setParticle(Particle.DAMAGE_INDICATOR);
                  eff.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 100, 1), false);
                  eff.setRadius(3.0F);
               } else {
                  highest = this.main.endWorld.getHighestBlockAt(all.getLocation()).getLocation();
                  eff = (AreaEffectCloud)this.main.endWorld.spawnEntity(highest, EntityType.AREA_EFFECT_CLOUD);
                  eff.setParticle(Particle.DAMAGE_INDICATOR);
                  eff.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 100, 1), false);
                  eff.setRadius(3.0F);
               }
            }

            this.nightVision = false;
            this.canMakeAnAttack = true;
         }
      }

   }

   public void chooseAnAttack() {
      int ran = this.random.nextInt(25);
      if (ran <= 3) {
         this.currentAttack = DemonCurrentAttack.LIGHTING_RAIN;
      } else if (ran >= 4 && ran <= 15) {
         this.currentAttack = DemonCurrentAttack.ENDERMAN_BUFF;
      } else if (ran >= 15 && ran <= 25) {
         this.currentAttack = DemonCurrentAttack.NIGHT_VISION;
      }

   }

   public Map<Location, Integer> getRegenTime() {
      return this.regenTime;
   }

   public void setDied(boolean died) {
      this.isDied = died;
   }

   public Entity getEnderDragon() {
      return this.enderDragon;
   }

   public boolean isDied() {
      return this.isDied;
   }

   public Main getMain() {
      return this.main;
   }

   public void start360attack() {
      this.attack360 = true;
   }

   public DemonPhase getCurrentDemonPhase() {
      return this.currentDemonPhase;
   }

   public void setCurrentDemonPhase(DemonPhase currentDemonPhase) {
      this.currentDemonPhase = currentDemonPhase;
   }
}
