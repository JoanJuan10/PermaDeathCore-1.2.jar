package com.permadeathcore.CustomMobs.v1_16_R2.PigmanJockeys;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Random;
import net.minecraft.server.v1_16_R2.DamageSource;
import net.minecraft.server.v1_16_R2.EntityHuman;
import net.minecraft.server.v1_16_R2.EntityPig;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_16_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_16_R2.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_16_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_16_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_16_R2.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_16_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_16_R2.SoundEffect;
import net.minecraft.server.v1_16_R2.SoundEffects;
import net.minecraft.server.v1_16_R2.PathfinderGoal.Type;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.entity.Pig;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpecialPig extends EntityPig {
   public SpecialPig(Location loc) {
      super(EntityTypes.PIG, ((CraftWorld)loc.getWorld()).getHandle());
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
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var12) {
         var12.printStackTrace();
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
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var11) {
         var11.printStackTrace();
      }

      this.goalSelector.a(0, new PathfinderGoalFloat(this));
      this.goalSelector.a(1, new PathfinderGoalRandomStrollLand(this, 1.0D));
      this.goalSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
      this.goalSelector.a(3, new PathfinderGoalRandomLookaround(this));
      this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 1.0D, true));
      this.targetSelector.a(0, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
      ArrayList<String> effectList = new ArrayList();
      Pig pig = (Pig)this.getBukkitEntity();
      effectList.add("SPEED");
      effectList.add("REGENERATION");
      effectList.add("INCREASE_DAMAGE");
      effectList.add("INVISIBILITY");
      effectList.add("JUMP");
      effectList.add("SLOW_FALLING");
      effectList.add("GLOWING");
      effectList.add("DAMAGE_RESISTANCE");

      for(int i = 0; i < 5; ++i) {
         Random rand = new Random();
         int randomIndex = rand.nextInt(effectList.size());
         String randomEffectName = (String)effectList.get(randomIndex);
         byte effectLevel;
         if (randomEffectName.equals("SPEED")) {
            effectLevel = 2;
            pig.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, effectLevel));
         }

         if (randomEffectName.equals("REGENERATION")) {
            effectLevel = 3;
            pig.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999999, effectLevel));
         }

         if (randomEffectName.equals("INCREASE_DAMAGE")) {
            effectLevel = 3;
            pig.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 9999999, effectLevel));
         }

         if (randomEffectName.equals("INVISIBILITY")) {
            effectLevel = 0;
            pig.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, effectLevel));
         }

         if (randomEffectName.equals("JUMP")) {
            effectLevel = 4;
            pig.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 9999999, effectLevel));
         }

         if (randomEffectName.equals("SLOW_FALLING")) {
            effectLevel = 0;
            pig.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 9999999, effectLevel));
         }

         if (randomEffectName.equals("GLOWING")) {
            effectLevel = 0;
            pig.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 9999999, effectLevel));
         }

         if (randomEffectName.equals("DAMAGE_RESISTANCE")) {
            effectLevel = 2;
            pig.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 9999999, effectLevel));
         }
      }

   }

   protected SoundEffect getSoundAmbient() {
      return SoundEffects.ENTITY_PIG_AMBIENT;
   }

   protected SoundEffect getSoundHurt(DamageSource damagesource) {
      return SoundEffects.ENTITY_PIG_HURT;
   }

   protected SoundEffect getSoundDeath() {
      return SoundEffects.ENTITY_PIG_DEATH;
   }
}
