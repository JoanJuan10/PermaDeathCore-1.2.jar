package com.permadeathcore.CustomMobs.v1_14_R1.PigmanJockeys;

import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.EntityInsentient;
import net.minecraft.server.v1_14_R1.EntityLiving;
import net.minecraft.server.v1_14_R1.EntityRaider;
import net.minecraft.server.v1_14_R1.EntityRavager;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_14_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_14_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_14_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_14_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_14_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_14_R1.PathfinderGoal.Type;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;

public class UltraRavager extends EntityRavager {
   public UltraRavager(Location loc) {
      super(EntityTypes.RAVAGER, ((CraftWorld)loc.getWorld()).getHandle());
      this.setPosition(loc.getX(), loc.getY(), loc.getZ());
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
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var8) {
         var8.printStackTrace();
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
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var7) {
         var7.printStackTrace();
      }

      this.goalSelector.a(0, new PathfinderGoalFloat(this));
      this.goalSelector.a(5, new PathfinderGoalRandomStrollLand(this, 0.4D));
      this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
      this.goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityInsentient.class, 8.0F));
      this.targetSelector.a(2, (new PathfinderGoalHurtByTarget(this, new Class[]{EntityRaider.class})).a(new Class[0]));
      this.goalSelector.a(1, new UltraRavager.a());
      this.targetSelector.a(0, new PathfinderGoalMeleeAttack(this, 1.0D, true));
   }

   class a extends PathfinderGoalMeleeAttack {
      public a() {
         super(UltraRavager.this, 1.0D, true);
      }

      protected double a(EntityLiving entityliving) {
         float f = UltraRavager.this.getWidth() - 0.1F;
         return (double)(f * 2.0F * f * 2.0F + entityliving.getWidth());
      }
   }
}
