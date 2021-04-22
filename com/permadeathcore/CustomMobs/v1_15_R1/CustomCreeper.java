package com.permadeathcore.CustomMobs.v1_15_R1;

import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.EntityCreeper;
import net.minecraft.server.v1_15_R1.EntityDamageSourceIndirect;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EnumDirection;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_15_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_15_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_15_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_15_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_15_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_15_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_15_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_15_R1.PathfinderGoalSwell;
import net.minecraft.server.v1_15_R1.TagsFluid;
import net.minecraft.server.v1_15_R1.World;
import net.minecraft.server.v1_15_R1.BlockPosition.MutableBlockPosition;
import net.minecraft.server.v1_15_R1.PathfinderGoal.Type;

public class CustomCreeper extends EntityCreeper {
   private boolean isEnderCreeper;

   public CustomCreeper(EntityTypes<? extends EntityCreeper> type, World world, boolean ender) {
      super(type, world);
      PathfinderGoalSelector goalSelector = this.goalSelector;
      PathfinderGoalSelector targetSelector = this.targetSelector;

      Field dField;
      Field cField;
      Field fField;
      try {
         dField = PathfinderGoalSelector.class.getDeclaredField("d");
         dField.setAccessible(true);
         dField.set(goalSelector, new LinkedHashSet());
         cField = PathfinderGoalSelector.class.getDeclaredField("c");
         cField.setAccessible(true);
         cField.set(goalSelector, new EnumMap(Type.class));
         fField = PathfinderGoalSelector.class.getDeclaredField("f");
         fField.setAccessible(true);
         fField.set(goalSelector, EnumSet.noneOf(Type.class));
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var10) {
         var10.printStackTrace();
      }

      try {
         dField = PathfinderGoalSelector.class.getDeclaredField("d");
         dField.setAccessible(true);
         dField.set(targetSelector, new LinkedHashSet());
         cField = PathfinderGoalSelector.class.getDeclaredField("c");
         cField.setAccessible(true);
         cField.set(targetSelector, new EnumMap(Type.class));
         fField = PathfinderGoalSelector.class.getDeclaredField("f");
         fField.setAccessible(true);
         fField.set(targetSelector, EnumSet.noneOf(Type.class));
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var9) {
         var9.printStackTrace();
      }

      this.goalSelector.a(1, new PathfinderGoalFloat(this));
      this.goalSelector.a(2, new PathfinderGoalSwell(this));
      this.goalSelector.a(3, new PathfinderGoalMeleeAttack(this, 1.0D, false));
      this.goalSelector.a(4, new PathfinderGoalRandomStrollLand(this, 0.8D));
      this.goalSelector.a(5, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
      this.goalSelector.a(5, new PathfinderGoalRandomLookaround(this));
      this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
      this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, new Class[0]));
      this.isEnderCreeper = ender;
   }

   public boolean damageEntity(DamageSource damagesource, float f) {
      if (this.isInvulnerable(damagesource)) {
         return false;
      } else if (!(damagesource instanceof EntityDamageSourceIndirect) && damagesource != DamageSource.FIREWORKS) {
         boolean flag = super.damageEntity(damagesource, f);
         if (!this.world.p_() && damagesource.ignoresArmor() && this.random.nextInt(10) != 0 && this.isEnderCreeper) {
            this.eq();
         }

         return flag;
      } else {
         for(int i = 0; i < 64; ++i) {
            if (this.eq()) {
               return true;
            }
         }

         return false;
      }
   }

   protected boolean eq() {
      if (!this.world.p_() && this.isAlive()) {
         double d0 = this.locX() + (this.random.nextDouble() - 0.5D) * 64.0D;
         double d1 = this.locY() + (double)(this.random.nextInt(64) - 32);
         double d2 = this.locZ() + (this.random.nextDouble() - 0.5D) * 64.0D;
         return this.o(d0, d1, d2);
      } else {
         return false;
      }
   }

   private boolean o(double d0, double d1, double d2) {
      MutableBlockPosition blockposition_mutableblockposition = new MutableBlockPosition(d0, d1, d2);

      while(blockposition_mutableblockposition.getY() > 0 && !this.world.getType(blockposition_mutableblockposition).getMaterial().isSolid()) {
         blockposition_mutableblockposition.c(EnumDirection.DOWN);
      }

      IBlockData iblockdata = this.world.getType(blockposition_mutableblockposition);
      boolean flag = iblockdata.getMaterial().isSolid();
      boolean flag1 = iblockdata.getFluid().a(TagsFluid.WATER);
      if (flag && !flag1) {
         boolean flag2 = this.a(d0, d1, d2, true);
         return flag2;
      } else {
         return false;
      }
   }
}
