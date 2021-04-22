package com.permadeathcore.CustomMobs.v1_16_R2;

import net.minecraft.server.v1_16_R2.EntityCod;
import net.minecraft.server.v1_16_R2.EntityHuman;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_16_R2.PathfinderGoalNearestAttackableTarget;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;

public class CustomCod extends EntityCod {
   public CustomCod(Location loc) {
      super(EntityTypes.COD, ((CraftWorld)loc.getWorld()).getHandle());
      this.setPosition(loc.getX(), loc.getY(), loc.getZ());
   }

   public void initPathfinder() {
      super.initPathfinder();
      this.goalSelector.a(0, new PathfinderGoalMeleeAttack(this, 1.0D, true));
      this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
   }
}
