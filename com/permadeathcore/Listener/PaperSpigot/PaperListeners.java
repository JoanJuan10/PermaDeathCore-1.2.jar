package com.permadeathcore.Listener.PaperSpigot;

import com.destroystokyo.paper.event.entity.EnderDragonFireballHitEvent;
import com.destroystokyo.paper.event.entity.EntityTeleportEndGatewayEvent;
import com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent;
import com.permadeathcore.Main;
import com.permadeathcore.End.Util.DemonPhase;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.SplittableRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.EndGateway;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class PaperListeners implements Listener {
   private Main main;
   private SplittableRandom random = new SplittableRandom();

   public PaperListeners(Main main) {
      this.main = main;
   }

   @EventHandler
   public void onProjectileHit(EnderDragonFireballHitEvent e) {
      AreaEffectCloud a = e.getAreaEffectCloud();
      if (this.main.getTask() != null) {
         ArrayList<Block> toChange = new ArrayList();
         Block b = this.main.endWorld.getHighestBlockAt(a.getLocation());
         Location highest = this.main.endWorld.getHighestBlockAt(a.getLocation()).getLocation();
         int structure = this.random.nextInt(4);
         if (structure == 0) {
            toChange.add(b.getRelative(BlockFace.NORTH));
            toChange.add(b.getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST));
            toChange.add(b.getRelative(BlockFace.SOUTH));
            toChange.add(b.getRelative(BlockFace.SOUTH_EAST));
            toChange.add(b.getRelative(BlockFace.SOUTH_WEST));
            toChange.add(b.getRelative(BlockFace.SOUTH_EAST).getRelative(BlockFace.SOUTH));
            toChange.add(b.getRelative(BlockFace.SOUTH_EAST).getRelative(BlockFace.NORTH));
            toChange.add(b.getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH));
         } else if (structure == 1) {
            toChange.add(b.getRelative(BlockFace.NORTH));
            toChange.add(b.getRelative(BlockFace.NORTH_EAST));
            toChange.add(b);
         } else if (structure == 2) {
            toChange.add(b.getRelative(BlockFace.SOUTH));
            toChange.add(b.getRelative(BlockFace.SOUTH_WEST));
            toChange.add(b);
         } else if (structure == 3) {
            toChange.add(b.getRelative(BlockFace.NORTH));
            toChange.add(b.getRelative(BlockFace.NORTH_EAST));
            toChange.add(b);
            toChange.add(b.getRelative(BlockFace.SOUTH));
            toChange.add(b.getRelative(BlockFace.EAST));
         } else if (structure == 4) {
            toChange.add(b.getRelative(BlockFace.SOUTH));
            toChange.add(b.getRelative(BlockFace.NORTH_WEST));
            toChange.add(b);
            toChange.add(b.getRelative(BlockFace.NORTH));
            toChange.add(b.getRelative(BlockFace.WEST));
         }

         Iterator var7;
         Block all;
         Location used;
         Block now;
         if (this.main.getTask().getCurrentDemonPhase() == DemonPhase.NORMAL) {
            if (highest.getY() > 0.0D) {
               var7 = toChange.iterator();

               while(var7.hasNext()) {
                  all = (Block)var7.next();
                  used = this.main.endWorld.getHighestBlockAt(new Location(this.main.endWorld, (double)all.getX(), (double)all.getY(), (double)all.getZ())).getLocation();
                  now = this.main.endWorld.getBlockAt(used);
                  if (now.getType() != Material.AIR) {
                     now.setType(Material.BEDROCK);
                  }
               }
            }
         } else if (this.random.nextBoolean()) {
            a.setParticle(Particle.SMOKE_NORMAL);
            a.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 20, 1), false);
         } else if (highest.getY() > 0.0D) {
            var7 = toChange.iterator();

            while(var7.hasNext()) {
               all = (Block)var7.next();
               used = this.main.endWorld.getHighestBlockAt(new Location(this.main.endWorld, (double)all.getX(), (double)all.getY(), (double)all.getZ())).getLocation();
               now = this.main.endWorld.getBlockAt(used);
               if (now.getType() != Material.AIR) {
                  now.setType(Material.BEDROCK);
               }
            }
         }
      }

   }

   @EventHandler
   public void onGatewayTeleport(EntityTeleportEndGatewayEvent e) {
      if (this.main.getDays() >= 40L) {
         if (this.main.getDays() >= 50L) {
            if (this.main.getBeginningManager().isClosed()) {
               e.setCancelled(true);
               return;
            }

            final Entity entity = e.getEntity();
            Location from = e.getFrom();
            World world = from.getWorld();
            if (entity instanceof Player) {
               return;
            }

            e.setCancelled(true);
            final Vector direction = entity.getLocation().getDirection();
            final Vector velocity = entity.getVelocity();
            final Float pitch = entity.getLocation().getPitch();
            final Float yaw = entity.getLocation().getYaw();
            if (world.getName().equalsIgnoreCase(this.main.world.getName())) {
               Location loc = this.main.getBeData().getBeginningPortal();
               loc.setDirection(direction);
               loc.setPitch(pitch);
               loc.setYaw(yaw);
               entity.teleport(loc, TeleportCause.PLUGIN);
               entity.setVelocity(velocity);
            }

            if (world.getName().equalsIgnoreCase("pdc_the_beginning")) {
               Bukkit.getScheduler().runTaskLater(this.main, new Runnable() {
                  public void run() {
                     Location loc = PaperListeners.this.main.world.getSpawnLocation();
                     loc.setDirection(direction);
                     loc.setPitch(pitch);
                     loc.setYaw(yaw);
                     entity.teleport(loc, TeleportCause.PLUGIN);
                     entity.setVelocity(velocity);
                  }
               }, 1L);
            }
         }

      }
   }

   @EventHandler
   public void onGatewayTeleport(PlayerTeleportEndGatewayEvent e) {
      if (this.main.getDays() >= 40L) {
         if (this.main.getDays() >= 50L) {
            if (this.main.getDays() >= 50L) {
               if (this.main.getBeginningManager().isClosed()) {
                  e.setCancelled(true);
                  return;
               }

               EndGateway gateway = e.getGateway();
               final Player p = e.getPlayer();
               Location from = e.getFrom();
               World world = from.getWorld();
               gateway.setExitLocation(gateway.getLocation());
               gateway.update();
               e.setCancelled(true);
               final Vector direction = p.getLocation().getDirection();
               final Vector velocity = p.getVelocity();
               if (world.getName().equalsIgnoreCase(this.main.world.getName())) {
                  Bukkit.getScheduler().runTaskLater(this.main, new Runnable() {
                     public void run() {
                        Location loc = PaperListeners.this.main.getBeData().getBeginningPortal();
                        loc.setDirection(direction);
                        p.teleport(loc, TeleportCause.PLUGIN);
                        p.setVelocity(velocity);
                     }
                  }, 1L);
               }

               if (world.getName().equalsIgnoreCase("pdc_the_beginning")) {
                  Bukkit.getScheduler().runTaskLater(this.main, new Runnable() {
                     public void run() {
                        Location loc = PaperListeners.this.main.world.getSpawnLocation();
                        loc.setDirection(direction);
                        p.teleport(loc, TeleportCause.PLUGIN);
                        p.setVelocity(velocity);
                     }
                  }, 1L);
               }
            }

         } else {
            if (e.getPlayer().getWorld().getName().equalsIgnoreCase(this.main.world.getName()) || e.getPlayer().getWorld().getName().equalsIgnoreCase(this.main.getBeginningManager().getBeginningWorld().getName())) {
               e.getPlayer().setNoDamageTicks(e.getPlayer().getMaximumNoDamageTicks());
               e.getPlayer().damage(e.getPlayer().getHealth() + 1.0D, (Entity)null);
               e.getPlayer().setNoDamageTicks(0);
               Main var10000 = this.main;
               Bukkit.broadcastMessage(Main.format("&c&lEl jugador &4&l" + e.getPlayer().getName() + " &c&lentró a TheBeginning antes de tiempo."));
            }

         }
      }
   }
}
