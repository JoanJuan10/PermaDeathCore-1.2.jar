package com.permadeathcore.Util.Manager;

import com.permadeathcore.Main;
import java.util.SplittableRandom;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EntityTeleport {
   private Entity c;
   private EntityDamageEvent e;
   private World world;
   private Main main;
   private double locX;
   private double locY;
   private double locZ;
   private SplittableRandom random;

   public EntityTeleport(Entity c, EntityDamageEvent e) {
      this.c = c;
      this.e = e;
      this.world = c.getWorld();
      this.main = Main.getInstance();
      this.locX = c.getLocation().getX();
      this.locY = c.getLocation().getY();
      this.locZ = c.getLocation().getZ();
      this.random = new SplittableRandom();
      if (e.getCause() != DamageCause.ENTITY_ATTACK && e.getCause() != DamageCause.VOID) {
         if (this.main.getFactory().hasData(e.getEntity(), "ender_creeper") || this.main.getFactory().hasData(e.getEntity(), "ender_quantum_creeper")) {
            this.teleport();
            e.setCancelled(true);
         }

         if (this.main.getFactory().hasData(e.getEntity(), "ender_ghast") && this.random.nextInt(101) <= 20) {
            this.teleport();
            e.setCancelled(true);
         }

         if (this.main.getFactory().hasData(e.getEntity(), "tp_ghast") && this.random.nextInt(101) <= 80) {
            this.teleport();
            e.setCancelled(true);
         }
      }

   }

   public boolean teleport() {
      for(int i = 0; i < 64; ++i) {
         if (this.eq()) {
            return true;
         }
      }

      return false;
   }

   private boolean eq() {
      double x = this.locX + (this.random.nextDouble() - 0.5D) * 64.0D;
      double y = this.locY + (double)(this.random.nextInt(64) - 32);
      double z = this.locZ + (this.random.nextDouble() - 0.5D) * 64.0D;

      Block b;
      for(b = this.world.getBlockAt((int)x, (int)y, (int)z); b.getY() > 0 && b.getType().isAir(); b = b.getRelative(BlockFace.DOWN)) {
      }

      return b.getY() <= 0 ? false : this.c.teleport(new Location(this.world, x, (double)(b.getY() + 1), z));
   }
}
