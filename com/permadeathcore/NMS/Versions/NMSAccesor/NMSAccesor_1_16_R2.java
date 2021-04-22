package com.permadeathcore.NMS.Versions.NMSAccesor;

import com.permadeathcore.NMS.NMSAccesor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.server.v1_16_R2.AttributeBase;
import net.minecraft.server.v1_16_R2.AttributeMapBase;
import net.minecraft.server.v1_16_R2.AttributeModifiable;
import net.minecraft.server.v1_16_R2.AttributeProvider;
import net.minecraft.server.v1_16_R2.DamageSource;
import net.minecraft.server.v1_16_R2.EntityCreature;
import net.minecraft.server.v1_16_R2.EntityHuman;
import net.minecraft.server.v1_16_R2.EntityInsentient;
import net.minecraft.server.v1_16_R2.EntityLiving;
import net.minecraft.server.v1_16_R2.GenericAttributes;
import net.minecraft.server.v1_16_R2.NBTTagCompound;
import net.minecraft.server.v1_16_R2.PathEntity;
import net.minecraft.server.v1_16_R2.PathfinderGoal;
import net.minecraft.server.v1_16_R2.PathfinderGoalAvoidTarget;
import net.minecraft.server.v1_16_R2.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_16_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_16_R2.PathfinderGoalPanic;
import net.minecraft.server.v1_16_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_16_R2.PathfinderGoalWrapped;
import net.minecraft.server.v1_16_R2.AttributeProvider.Builder;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R2.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class NMSAccesor_1_16_R2 implements NMSAccesor {
   public void setMaxHealth(Entity entity, Double d, boolean setHealth) {
      this.getAtribute(entity, GenericAttributes.MAX_HEALTH).setValue(d);
      if (setHealth && entity instanceof LivingEntity) {
         ((LivingEntity)entity).setHealth(d);
      }

   }

   public Double getMaxHealth(Entity entity) {
      return this.getAtribute(entity, GenericAttributes.MAX_HEALTH).getValue();
   }

   public EntityInsentient getEntityInsentiment(Entity en) {
      if (en == null) {
         return null;
      } else {
         net.minecraft.server.v1_16_R2.Entity entity = ((CraftEntity)en).getHandle();
         if (entity instanceof EntityInsentient) {
            EntityInsentient insentient = (EntityInsentient)entity;
            return insentient;
         } else {
            return null;
         }
      }
   }

   public void moveEntityTo(Entity entity, Location location, Double speed) {
      EntityInsentient insentient = this.getEntityInsentiment(entity);
      if (insentient != null) {
         PathEntity path = insentient.getNavigation().a(location.getX(), location.getY(), location.getZ(), 2);
         if (path != null) {
            insentient.getNavigation().a(path, speed);
         }

      }
   }

   public void registerNewAttribute(Entity entity, Object at) {
      if (entity instanceof LivingEntity) {
         ((LivingEntity)entity).getAttribute((Attribute)at).setBaseValue(5.0D);
      }

   }

   public void setAttributeValue(Object attribute, Double value, Entity entity) {
      if (entity instanceof LivingEntity) {
         ((LivingEntity)entity).getAttribute((Attribute)attribute).setBaseValue(value);
      }

   }

   public void registerAttribute(Attribute a, Double value, Entity who) {
      EntityLiving creature = (EntityLiving)((CraftEntity)who).getHandle();

      try {
         Field map = EntityLiving.class.getDeclaredField("attributeMap");
         map.setAccessible(true);
         AttributeProvider provider;
         if (who.getType() != EntityType.valueOf("BEE") && who.getType() != EntityType.PARROT) {
            provider = EntityInsentient.p().a(GenericAttributes.FOLLOW_RANGE, creature.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).getValue()).a(GenericAttributes.MOVEMENT_SPEED, creature.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()).a(GenericAttributes.MAX_HEALTH, creature.getAttributeInstance(GenericAttributes.MAX_HEALTH).getValue()).a(GenericAttributes.KNOCKBACK_RESISTANCE, creature.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).getValue()).a(GenericAttributes.ARMOR, creature.getAttributeInstance(GenericAttributes.ARMOR).getValue()).a(this.bukkitToNMSAttribute(a), value).a();
         } else {
            provider = EntityInsentient.p().a(GenericAttributes.FOLLOW_RANGE, creature.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).getValue()).a(GenericAttributes.MOVEMENT_SPEED, creature.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()).a(GenericAttributes.FLYING_SPEED, creature.getAttributeInstance(GenericAttributes.FLYING_SPEED).getValue()).a(GenericAttributes.MAX_HEALTH, creature.getAttributeInstance(GenericAttributes.MAX_HEALTH).getValue()).a(GenericAttributes.KNOCKBACK_RESISTANCE, creature.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).getValue()).a(GenericAttributes.ARMOR, creature.getAttributeInstance(GenericAttributes.ARMOR).getValue()).a(this.bukkitToNMSAttribute(a), value).a();
         }

         map.set(creature, new AttributeMapBase(provider));
      } catch (Throwable var7) {
      }

   }

   public void unregisterAttributes(Entity who) {
      EntityLiving creature = (EntityLiving)((CraftEntity)who).getHandle();

      try {
         Field map = EntityLiving.class.getDeclaredField("attributeMap");
         map.setAccessible(true);
         AttributeProvider provider;
         if (who.getType() != EntityType.valueOf("BEE") && who.getType() != EntityType.PARROT) {
            provider = EntityInsentient.p().a(GenericAttributes.FOLLOW_RANGE, creature.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).getValue()).a(GenericAttributes.MOVEMENT_SPEED, creature.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()).a(GenericAttributes.MAX_HEALTH, creature.getAttributeInstance(GenericAttributes.MAX_HEALTH).getValue()).a(GenericAttributes.KNOCKBACK_RESISTANCE, creature.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).getValue()).a(GenericAttributes.ARMOR, creature.getAttributeInstance(GenericAttributes.ARMOR).getValue()).a();
         } else {
            provider = EntityInsentient.p().a(GenericAttributes.FOLLOW_RANGE, creature.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).getValue()).a(GenericAttributes.MOVEMENT_SPEED, creature.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()).a(GenericAttributes.FLYING_SPEED, creature.getAttributeInstance(GenericAttributes.FLYING_SPEED).getValue()).a(GenericAttributes.MAX_HEALTH, creature.getAttributeInstance(GenericAttributes.MAX_HEALTH).getValue()).a(GenericAttributes.KNOCKBACK_RESISTANCE, creature.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).getValue()).a(GenericAttributes.ARMOR, creature.getAttributeInstance(GenericAttributes.ARMOR).getValue()).a();
         }

         map.set(creature, new AttributeMapBase(provider));
      } catch (Throwable var5) {
         var5.printStackTrace();
      }

   }

   public void registerKnockback(Double value, Double attackValue, Entity who) {
      EntityLiving creature = (EntityLiving)((CraftEntity)who).getHandle();

      try {
         Field map = EntityLiving.class.getDeclaredField("attributeMap");
         map.setAccessible(true);
         AttributeProvider provider;
         if (who.getType() != EntityType.valueOf("BEE") && who.getType() != EntityType.PARROT) {
            provider = EntityInsentient.p().a(GenericAttributes.FOLLOW_RANGE, creature.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).getValue()).a(GenericAttributes.MOVEMENT_SPEED, creature.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()).a(GenericAttributes.MAX_HEALTH, creature.getAttributeInstance(GenericAttributes.MAX_HEALTH).getValue()).a(GenericAttributes.KNOCKBACK_RESISTANCE, creature.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).getValue()).a(GenericAttributes.ARMOR, creature.getAttributeInstance(GenericAttributes.ARMOR).getValue()).a(GenericAttributes.ATTACK_KNOCKBACK, value).a(GenericAttributes.ATTACK_DAMAGE, attackValue).a();
         } else {
            provider = EntityInsentient.p().a(GenericAttributes.FOLLOW_RANGE, creature.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).getValue()).a(GenericAttributes.MOVEMENT_SPEED, creature.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()).a(GenericAttributes.FLYING_SPEED, creature.getAttributeInstance(GenericAttributes.FLYING_SPEED).getValue()).a(GenericAttributes.MAX_HEALTH, creature.getAttributeInstance(GenericAttributes.MAX_HEALTH).getValue()).a(GenericAttributes.KNOCKBACK_RESISTANCE, creature.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).getValue()).a(GenericAttributes.ARMOR, creature.getAttributeInstance(GenericAttributes.ARMOR).getValue()).a(GenericAttributes.ATTACK_KNOCKBACK, value).a(GenericAttributes.ATTACK_DAMAGE, attackValue).a();
         }

         map.set(creature, new AttributeMapBase(provider));
      } catch (Throwable var7) {
         throw new RuntimeException(var7);
      }
   }

   public void registerAttribute(HashMap<Attribute, Double> a, Entity who) {
      EntityLiving creature = (EntityLiving)((CraftEntity)who).getHandle();

      try {
         Field map = EntityLiving.class.getDeclaredField("attributeMap");
         map.setAccessible(true);
         AttributeMapBase defaultMap = (AttributeMapBase)map.get(creature);
         Field aP = AttributeMapBase.class.getDeclaredField("d");
         aP.setAccessible(true);
         AttributeProvider provider = (AttributeProvider)aP.get(defaultMap);
         Builder c = AttributeProvider.a();

         AttributeBase b;
         for(Iterator var9 = a.keySet().iterator(); var9.hasNext(); c = c.a(b, (Double)a.get(b))) {
            Attribute l = (Attribute)var9.next();
            b = this.bukkitToNMSAttribute(l);
         }

         AttributeProvider p = c.a();
         aP.set(defaultMap, p);
         map.set(creature, defaultMap);
         Field craftF = EntityLiving.class.getDeclaredField("craftAttributes");
         map.setAccessible(true);
         CraftAttributeMap cam = new CraftAttributeMap(defaultMap);
         craftF.set(creature, cam);
      } catch (Throwable var12) {
         throw new RuntimeException(var12);
      }
   }

   public void registerHostileMobs() {
   }

   public void injectHostilePathfinders(Entity entity) {
      net.minecraft.server.v1_16_R2.Entity nms = ((CraftEntity)entity).getHandle();
      EntityType type = entity.getType();
      PathfinderGoalSelector goalSelector;
      Field dField;
      Set set;
      if (nms instanceof EntityCreature) {
         EntityCreature insentient = (EntityCreature)((CraftEntity)entity).getHandle();
         goalSelector = insentient.goalSelector;

         try {
            dField = PathfinderGoalSelector.class.getDeclaredField("d");
            dField.setAccessible(true);
            set = (Set)dField.get(goalSelector);
            boolean containsMelee = false;
            Iterator var9 = set.iterator();

            while(var9.hasNext()) {
               PathfinderGoalWrapped goals = (PathfinderGoalWrapped)var9.next();
               if (goals.getClass().getName().equalsIgnoreCase(PathfinderGoalAvoidTarget.class.getName())) {
                  set.remove(goals);
               }

               if (goals.getClass().getName().equalsIgnoreCase(PathfinderGoalPanic.class.getName())) {
                  set.remove(goals);
               }

               if (goals.getClass().getName().equalsIgnoreCase(PathfinderGoalMeleeAttack.class.getName())) {
                  containsMelee = true;
               }
            }

            if (!containsMelee) {
               set.add(new PathfinderGoalWrapped(0, new PathfinderGoalMeleeAttack(insentient, 1.0D, true)));
            }

            dField.set(goalSelector, set);
         } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var12) {
            var12.printStackTrace();
         }
      }

      if (nms instanceof EntityInsentient) {
         EntityInsentient insentient = (EntityInsentient)((CraftEntity)entity).getHandle();
         goalSelector = insentient.targetSelector;

         try {
            dField = PathfinderGoalSelector.class.getDeclaredField("d");
            dField.setAccessible(true);
            set = (Set)dField.get(goalSelector);
            set.add(new PathfinderGoalWrapped(0, new PathfinderGoalNearestAttackableTarget(insentient, EntityHuman.class, true)));
            dField.set(goalSelector, set);
         } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var11) {
            var11.printStackTrace();
         }
      }

   }

   public void unregisterHostilePathfinders(Entity entity) {
      net.minecraft.server.v1_16_R2.Entity nms = ((CraftEntity)entity).getHandle();
      EntityType type = entity.getType();
      PathfinderGoalSelector targetSelector;
      Field dField;
      Set set;
      Iterator var8;
      PathfinderGoalWrapped w;
      Field f;
      PathfinderGoal goal;
      if (nms instanceof EntityCreature) {
         EntityCreature insentient = (EntityCreature)((CraftEntity)entity).getHandle();
         targetSelector = insentient.goalSelector;

         try {
            dField = PathfinderGoalSelector.class.getDeclaredField("d");
            dField.setAccessible(true);
            set = (Set)dField.get(targetSelector);
            var8 = set.iterator();

            while(var8.hasNext()) {
               w = (PathfinderGoalWrapped)var8.next();
               f = w.getClass().getDeclaredField("a");
               f.setAccessible(true);
               goal = (PathfinderGoal)f.get(w);
               if (goal.getClass().getName().equalsIgnoreCase(PathfinderGoalNearestAttackableTarget.class.getName())) {
                  set.remove(w);
               }
            }

            dField.set(targetSelector, set);
         } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var13) {
            var13.printStackTrace();
         }
      }

      if (nms instanceof EntityInsentient) {
         EntityInsentient insentient = (EntityInsentient)((CraftEntity)entity).getHandle();
         targetSelector = insentient.targetSelector;

         try {
            dField = PathfinderGoalSelector.class.getDeclaredField("d");
            dField.setAccessible(true);
            set = (Set)dField.get(targetSelector);
            var8 = set.iterator();

            while(var8.hasNext()) {
               w = (PathfinderGoalWrapped)var8.next();
               f = w.getClass().getDeclaredField("a");
               f.setAccessible(true);
               goal = (PathfinderGoal)f.get(w);
               if (goal.getClass().getName().equalsIgnoreCase(PathfinderGoalNearestAttackableTarget.class.getName())) {
                  set.remove(w);
               }
            }

            dField.set(targetSelector, set);
         } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var12) {
            var12.printStackTrace();
         }
      }

   }

   public void addNMSTag(Entity entity) {
      net.minecraft.server.v1_16_R2.Entity en = ((CraftEntity)entity).getHandle();
      NBTTagCompound t = new NBTTagCompound();
      t.setInt("Invul", 100);
      en.save(t);
      en.load(t);
   }

   public AttributeBase bukkitToNMSAttribute(Attribute attribute) {
      AttributeBase ia = null;
      if (attribute == Attribute.GENERIC_ATTACK_DAMAGE) {
         ia = GenericAttributes.ATTACK_DAMAGE;
      }

      if (attribute == Attribute.GENERIC_ARMOR_TOUGHNESS) {
         ia = GenericAttributes.ARMOR_TOUGHNESS;
      }

      if (attribute == Attribute.GENERIC_ARMOR) {
         ia = GenericAttributes.ARMOR;
      }

      if (attribute == Attribute.GENERIC_MAX_HEALTH) {
         ia = GenericAttributes.MAX_HEALTH;
      }

      if (attribute == Attribute.GENERIC_KNOCKBACK_RESISTANCE) {
         ia = GenericAttributes.KNOCKBACK_RESISTANCE;
      }

      if (attribute == Attribute.GENERIC_ATTACK_SPEED) {
         ia = GenericAttributes.ATTACK_SPEED;
      }

      if (attribute == Attribute.GENERIC_FLYING_SPEED) {
         ia = GenericAttributes.FLYING_SPEED;
      }

      if (attribute == Attribute.GENERIC_FOLLOW_RANGE) {
         ia = GenericAttributes.FOLLOW_RANGE;
      }

      if (attribute == Attribute.GENERIC_MOVEMENT_SPEED) {
         ia = GenericAttributes.MOVEMENT_SPEED;
      }

      if (attribute == Attribute.GENERIC_LUCK) {
         ia = GenericAttributes.LUCK;
      }

      if (attribute == Attribute.HORSE_JUMP_STRENGTH) {
         ia = GenericAttributes.JUMP_STRENGTH;
      }

      if (attribute == Attribute.ZOMBIE_SPAWN_REINFORCEMENTS) {
         ia = GenericAttributes.SPAWN_REINFORCEMENTS;
      }

      return ia;
   }

   public Class getNMSEntityClass(String name) {
      try {
         return Class.forName("net.minecraft.server.v1_16_R2.Entity" + name);
      } catch (ClassNotFoundException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public void drown(Player p, double ammount) {
      net.minecraft.server.v1_16_R2.Entity en = ((CraftEntity)p).getHandle();
      en.damageEntity(DamageSource.DROWN, (float)ammount);
   }

   public AttributeModifiable getAtribute(Entity en, Object at) {
      return this.getEntityInsentiment(en) == null ? null : this.getEntityInsentiment(en).getAttributeInstance((AttributeBase)at);
   }
}
