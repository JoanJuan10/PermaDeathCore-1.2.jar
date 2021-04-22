package com.permadeathcore.End;

import com.permadeathcore.Main;
import com.permadeathcore.End.Util.DemonPhase;
import com.permadeathcore.Task.EndTask;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.SplittableRandom;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class EndManager implements Listener {
   private Main main;
   private List<Entity> enderCreepers;
   private List<Entity> enderGhasts;
   private ArrayList<Location> alreadyExploded = new ArrayList();
   private ArrayList<Enderman> invulnerable = new ArrayList();
   private SplittableRandom random;

   public EndManager(Main main) {
      this.main = main;
      main.getServer().getPluginManager().registerEvents(this, main);
      this.enderCreepers = new ArrayList();
      this.enderGhasts = new ArrayList();
      this.random = new SplittableRandom();
   }

   @EventHandler
   public void onExplosionPrime(ExplosionPrimeEvent e) {
      if (this.isInEnd(e.getEntity().getLocation()) && e.getEntity() instanceof TNTPrimed) {
         if (!(e.getEntity() instanceof TNTPrimed)) {
            return;
         }

         if (e.getEntity().getCustomName() == null) {
            return;
         }

         if (!e.getEntity().getCustomName().equalsIgnoreCase("dragontnt")) {
            return;
         }

         if (Main.instance.getConfig().getBoolean("Toggles.End.Optimizar-TNT")) {
            e.setRadius(5.0F);
         } else {
            e.setRadius(15.0F);
         }
      }

   }

   @EventHandler
   public void onEffectApply(AreaEffectCloudApplyEvent e) {
      AreaEffectCloud area = e.getEntity();
      if (this.isInEnd(area.getLocation())) {
         Iterator var3;
         Entity all;
         if (area.getParticle() == Particle.VILLAGER_HAPPY) {
            var3 = e.getAffectedEntities().iterator();

            while(var3.hasNext()) {
               all = (Entity)var3.next();
               if (all instanceof Player) {
                  e.setCancelled(true);
               } else if (all.getType() == EntityType.ENDERMAN) {
                  final Enderman man = (Enderman)all;
                  this.invulnerable.add(man);
                  Bukkit.getServer().getScheduler().runTaskLater(this.main, new Runnable() {
                     public void run() {
                        if (man != null) {
                           EndManager.this.invulnerable.remove(man);
                        }
                     }
                  }, 300L);
                  e.setCancelled(true);
               }
            }
         }

         if (area.getParticle() == Particle.SMOKE_NORMAL) {
            var3 = e.getAffectedEntities().iterator();

            while(true) {
               Player p;
               do {
                  do {
                     do {
                        if (!var3.hasNext()) {
                           return;
                        }

                        all = (Entity)var3.next();
                     } while(!(all instanceof Player));

                     p = (Player)all;
                  } while(!(p.getLocation().distance(area.getLocation()) <= 3.0D));
               } while(p.getActivePotionEffects().size() < 1);

               Iterator var6 = p.getActivePotionEffects().iterator();

               while(var6.hasNext()) {
                  PotionEffect effect = (PotionEffect)var6.next();
                  p.removePotionEffect(effect.getType());
               }
            }
         }
      }

   }

   @EventHandler
   public void onDamageBE(EntityDamageByEntityEvent e) {
      if (e.getEntity() instanceof Enderman) {
         Enderman man = (Enderman)e.getEntity();
         if (this.invulnerable.contains(man)) {
            e.setCancelled(true);
         }
      }

   }

   @EventHandler
   public void onEMDamage(EntityDamageEvent e) {
      if (e.getEntity() instanceof Enderman) {
         Enderman man = (Enderman)e.getEntity();
         if (this.invulnerable.contains(man)) {
            e.setCancelled(true);
         }
      }

   }

   @EventHandler
   public void onDead(EntityDeathEvent e) {
      if (e.getEntity().getType() == EntityType.ENDER_DRAGON && this.main.getTask() != null) {
         this.main.getTask().setDied(true);
         Iterator var2 = this.main.endWorld.getPlayers().iterator();

         while(var2.hasNext()) {
            Player all = (Player)var2.next();
            spawnFireworks(all.getLocation().add(0.0D, 1.0D, 0.0D), 1);
         }
      }

      Entity entity = e.getEntity();
      if (this.enderGhasts.contains(entity)) {
         this.enderGhasts.remove(entity);
         e.getDrops().clear();
         e.setDroppedExp(0);
      }

      if (this.enderCreepers.contains(entity)) {
         this.enderCreepers.remove(entity);
         e.getDrops().clear();
         e.setDroppedExp(0);
      }

      if (entity instanceof Shulker && ((Shulker)entity).getColor() != DyeColor.RED) {
         boolean isSure = true;
         Iterator var4 = e.getEntity().getNearbyEntities(2.0D, 2.0D, 2.0D).iterator();

         while(var4.hasNext()) {
            Entity near = (Entity)var4.next();
            if (near.getType() == EntityType.PRIMED_TNT) {
               isSure = false;
            }
         }

         if (isSure) {
            TNTPrimed tnt = (TNTPrimed)e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.PRIMED_TNT);
            tnt.setFuseTicks(80);
            tnt.setCustomName("tntdeath");
            tnt.setCustomNameVisible(false);
            e.getDrops().clear();
            int randomProb = (new Random()).nextInt(99);
            ++randomProb;
            if (this.main.getDays() <= 39L) {
               if (randomProb <= 20) {
                  if (Main.instance.getShulkerEvent().isRunning()) {
                     e.getDrops().add(new ItemStack(Material.SHULKER_SHELL, 2));
                  } else {
                     e.getDrops().add(new ItemStack(Material.SHULKER_SHELL, 1));
                  }
               }
            } else if (this.main.getDays() >= 40L && randomProb <= 2) {
               if (Main.instance.getShulkerEvent().isRunning()) {
                  e.getDrops().add(new ItemStack(Material.SHULKER_SHELL, 2));
               } else {
                  e.getDrops().add(new ItemStack(Material.SHULKER_SHELL, 1));
               }
            }
         }
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onExplode(EntityExplodeEvent e) {
      Entity t = e.getEntity();
      if (this.isInEnd(t.getLocation()) && e.getEntity().getType() == EntityType.ENDER_CRYSTAL && this.main.getTask() != null) {
         if (this.alreadyExploded.contains(e.getLocation())) {
            return;
         }

         EnderCrystal c = (EnderCrystal)e.getEntity();
         if (c.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEDROCK) {
            int random = (new Random()).nextInt(this.main.getEndData().getTimeList().size());
            this.main.getTask().getRegenTime().put(c.getLocation(), this.main.getEndData().getTimeList().get(random));
            Location nL = e.getLocation().clone().add(0.0D, 10.0D, 0.0D);
            Entity g = Main.instance.getNmsHandler().spawnCustomGhast(nL, SpawnReason.CUSTOM, true);
            final Location loc = e.getLocation();
            this.enderGhasts.add(g);
            this.alreadyExploded.add(loc);
            Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
               public void run() {
                  if (EndManager.this.alreadyExploded.contains(loc)) {
                     EndManager.this.alreadyExploded.remove(loc);
                  }

               }
            }, 100L);
            Iterator var8 = this.main.endWorld.getPlayers().iterator();

            while(var8.hasNext()) {
               Player all = (Player)var8.next();
               all.playSound(nL, Sound.ENTITY_WITHER_SPAWN, 100.0F, 100.0F);
            }
         }
      }

      if (e.getEntity() instanceof TNTPrimed) {
         if (e.getEntity().getCustomName() == null) {
            return;
         }

         if (!e.getEntity().getCustomName().equalsIgnoreCase("dragontnt")) {
            return;
         }

         if (!e.blockList().isEmpty()) {
            Location egg = new Location(this.main.endWorld, 0.0D, 0.0D, 0.0D);
            Location withY = this.main.endWorld.getHighestBlockAt(egg).getLocation();
            if (e.getLocation().distance(withY) <= (double)Main.instance.getConfig().getInt("Toggles.End.Protect-Radius") && Main.instance.getConfig().getBoolean("Toggles.End.Protect-End-Spawn")) {
               e.blockList().clear();
               e.setYield(0.0F);
               return;
            }

            List<FallingBlock> fallingBlocks = new ArrayList();
            final List<Block> blockList = new ArrayList(e.blockList());
            Iterator var17 = blockList.iterator();

            while(true) {
               float y;
               float z;
               Block b;
               float x;
               do {
                  if (!var17.hasNext()) {
                     return;
                  }

                  b = (Block)var17.next();
                  x = (float)(-0.2D + (double)((float)(Math.random() * 0.6000000000000001D)));
                  y = -1.0F + (float)(Math.random() * 3.0D);
                  z = (float)(-0.2D + (double)((float)(Math.random() * 0.6000000000000001D)));
               } while(b.getType() != Material.END_STONE && b.getType() != Material.END_STONE_BRICKS);

               FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), b.getState().getData());
               b.getState().setData(b.getState().getData());
               fb.setVelocity(new Vector(x, y, z));
               fb.setDropItem(false);
               fb.setMetadata("Exploded", new FixedMetadataValue(this.main, 0));
               fallingBlocks.add(fb);
               (new BukkitRunnable() {
                  public void run() {
                     Iterator var1 = blockList.iterator();

                     while(var1.hasNext()) {
                        Block b = (Block)var1.next();
                        b.getState().update();
                        this.cancel();
                     }

                  }
               }).runTaskLater(this.main, 2L);
               e.blockList().clear();
            }
         }
      }

   }

   @EventHandler
   public void onDragonRegen(EntityRegainHealthEvent e) {
      if (e.getEntity() instanceof EnderDragon) {
         e.setAmount(e.getAmount() / 2.0D);
      }

   }

   @EventHandler
   public void onSpawn(CreatureSpawnEvent e) {
      if (!e.isCancelled()) {
         LivingEntity entity = e.getEntity();
         if (this.isInEnd(entity.getLocation())) {
            int creeperProb;
            if (this.main.getTask() == null) {
               Iterator var8 = e.getLocation().getWorld().getEntitiesByClass(EnderDragon.class).iterator();

               while(var8.hasNext()) {
                  Entity n = (Entity)var8.next();
                  if (n.isValid() && !n.isDead()) {
                     Main var10001 = Main.instance;
                     n.setCustomName(Main.format(Main.instance.getConfig().getString("Toggles.End.PermadeathDemon.DisplayName")));
                     ((LivingEntity)n).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((double)Main.instance.getConfig().getInt("Toggles.End.PermadeathDemon.Health"));
                     ((LivingEntity)n).setHealth((double)Main.instance.getConfig().getInt("Toggles.End.PermadeathDemon.Health"));
                     this.main.setTask(new EndTask(this.main, (EnderDragon)n));
                     this.main.getTask().runTaskTimer(this.main, 0L, 20L);
                  }
               }
            } else if (!this.main.getTask().isDied()) {
               Entity n = this.main.getTask().getEnderDragon();
               if (n.getType() == EntityType.ENDER_DRAGON && n.isValid() && !n.isDead()) {
                  EnderDragon dragon = (EnderDragon)n;
                  creeperProb = Main.instance.getConfig().getInt("Toggles.End.PermadeathDemon.EnragedHealth");
                  if (creeperProb > Main.instance.getConfig().getInt("Toggles.End.PermadeathDemon.Health") || creeperProb < 10) {
                     creeperProb = Main.instance.getConfig().getInt("Toggles.End.PermadeathDemon.Health") / 2;
                  }

                  if (dragon.getHealth() <= (double)creeperProb) {
                     this.main.getTask().setCurrentDemonPhase(DemonPhase.ENRAGED);
                  }
               }
            }

            if (!(entity instanceof Enderman)) {
               return;
            }

            int cCP = Main.instance.getConfig().getInt("Toggles.End.Ender-Creeper-Count");
            if (cCP < 1 || cCP > 1000) {
               cCP = 20;
            }

            int cGP = Main.instance.getConfig().getInt("Toggles.End.Ender-Ghast-Count");
            if (cGP < 1 || cGP > 1000) {
               cGP = 170;
            }

            creeperProb = this.random.nextInt(cCP) + 1;
            int ghastProb = this.random.nextInt(cGP) + 1;
            if (creeperProb == 1) {
               if (Main.instance.getDays() < 60L) {
                  Main.instance.getFactory().spawnEnderCreeper(e.getLocation(), (Creeper)null);
               } else {
                  Main.instance.getFactory().spawnEnderQuantumCreeper(e.getLocation(), (Creeper)null);
               }

               e.setCancelled(true);
            }

            if (ghastProb == 1) {
               boolean dragonDied = true;
               if (this.main.endWorld.getEntitiesByClass(EnderDragon.class).size() >= 1) {
                  dragonDied = false;
               }

               if (dragonDied) {
                  this.main.getNmsHandler().spawnCustomGhast(e.getLocation(), SpawnReason.CUSTOM, true);
                  e.setCancelled(true);
               }
            }

            if (Main.instance.getConfig().getBoolean("Toggles.Optimizar-Mob-Spawns")) {
               int removeProb = this.random.nextInt(100) + 1;
               if (removeProb <= 10) {
                  e.setCancelled(true);
               }
            }
         } else if (e.getEntity() instanceof Enderman && this.main.getDays() >= 40L) {
            Enderman man = (Enderman)e.getEntity();
            if (this.random.nextInt(100) + 1 == 1) {
               Main.instance.getNmsAccesor().injectHostilePathfinders(man);
            }
         }

      }
   }

   @EventHandler
   public void onHit(ProjectileHitEvent e) {
      if (this.isInEnd(e.getEntity().getLocation())) {
         ShulkerBullet b;
         Shulker s;
         if (e.getHitBlock() != null && e.getEntity() instanceof ShulkerBullet) {
            b = (ShulkerBullet)e.getEntity();
            if (b.getShooter() instanceof Shulker) {
               s = (Shulker)b.getShooter();
               if (s.getLocation().distance(e.getHitBlock().getLocation()) >= 4.0D) {
                  Location w = e.getHitBlock().getLocation();
                  if (e.getHitBlockFace() == BlockFace.EAST) {
                     w = e.getHitBlock().getRelative(BlockFace.EAST).getLocation();
                  }

                  if (e.getHitBlockFace() == BlockFace.UP) {
                     w = e.getHitBlock().getRelative(BlockFace.UP).getLocation();
                  }

                  if (e.getHitBlockFace() == BlockFace.DOWN) {
                     w = e.getHitBlock().getRelative(BlockFace.DOWN).getLocation();
                  }

                  if (e.getHitBlockFace() == BlockFace.NORTH) {
                     w = e.getHitBlock().getRelative(BlockFace.NORTH).getLocation().add(0.0D, 1.0D, 0.0D);
                  }

                  if (e.getHitBlockFace() == BlockFace.SOUTH) {
                     w = e.getHitBlock().getRelative(BlockFace.SOUTH).getLocation().add(0.0D, 1.0D, 0.0D);
                  }

                  w.getBlock().setType(Material.AIR);
                  TNTPrimed tnt = (TNTPrimed)s.getWorld().spawnEntity(w, EntityType.PRIMED_TNT);
                  tnt.setFuseTicks(40);
                  tnt.setCustomName("tnt");
                  tnt.setCustomNameVisible(false);
               }
            }
         }

         if (e.getHitEntity() != null && e.getEntity() instanceof ShulkerBullet) {
            b = (ShulkerBullet)e.getEntity();
            if (b.getShooter() instanceof Shulker) {
               s = (Shulker)b.getShooter();
               if (s.getLocation().getX() == e.getHitEntity().getLocation().getX() && s.getLocation().getY() == e.getHitEntity().getLocation().getY() && s.getLocation().getZ() == e.getHitEntity().getLocation().getZ()) {
                  return;
               }

               TNTPrimed tnt = (TNTPrimed)s.getWorld().spawnEntity(e.getHitEntity().getLocation(), EntityType.PRIMED_TNT);
               tnt.setFuseTicks(20);
               tnt.setCustomName("tnt");
               tnt.setCustomNameVisible(false);
            }
         }

      }
   }

   public static void spawnFireworks(Location location, int amount) {
      Location loc = location;
      Firework fw = (Firework)location.getWorld().spawnEntity(location, EntityType.FIREWORK);
      FireworkMeta fwm = fw.getFireworkMeta();
      fwm.setPower(2);
      fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());
      fw.setFireworkMeta(fwm);
      fw.detonate();

      for(int i = 0; i < amount; ++i) {
         Firework fw2 = (Firework)loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
         fw2.setFireworkMeta(fwm);
      }

   }

   public boolean isInEnd(Location p) {
      return p.getWorld().getName().endsWith("_the_end");
   }
}
