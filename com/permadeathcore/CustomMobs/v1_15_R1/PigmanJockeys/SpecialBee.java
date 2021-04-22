package com.permadeathcore.CustomMobs.v1_15_R1.PigmanJockeys;

import net.minecraft.server.v1_15_R1.EntityBee;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.GenericAttributes;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_15_R1.PathfinderGoalNearestAttackableTarget;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.Bee;

public class SpecialBee extends EntityBee {
   public SpecialBee(Location loc) {
      super(EntityTypes.BEE, ((CraftWorld)loc.getWorld()).getHandle());
      this.setPosition(loc.getX(), loc.getY(), loc.getZ());
      this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(100.0D);
      this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(12.0D);
      Bee bee = (Bee)this.getBukkitEntity();
      bee.setHealth(100.0D);
      bee.setAnger(1);
      this.goalSelector.a(0, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
      this.targetSelector.a(0, new PathfinderGoalMeleeAttack(this, 1.0D, true));
   }

   public void b(NBTTagCompound tag) {
      super.b(tag);
   }
}
