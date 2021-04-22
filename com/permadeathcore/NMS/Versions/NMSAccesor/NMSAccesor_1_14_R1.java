package com.permadeathcore.NMS.Versions.NMSAccesor;

import com.permadeathcore.Main;
import com.permadeathcore.NMS.NMSAccesor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.server.v1_14_R1.AttributeInstance;
import net.minecraft.server.v1_14_R1.AttributeMapBase;
import net.minecraft.server.v1_14_R1.DamageSource;
import net.minecraft.server.v1_14_R1.EntityCreature;
import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.EntityInsentient;
import net.minecraft.server.v1_14_R1.EntityLiving;
import net.minecraft.server.v1_14_R1.GenericAttributes;
import net.minecraft.server.v1_14_R1.IAttribute;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.PathEntity;
import net.minecraft.server.v1_14_R1.PathfinderGoalAvoidTarget;
import net.minecraft.server.v1_14_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_14_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_14_R1.PathfinderGoalPanic;
import net.minecraft.server.v1_14_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_14_R1.PathfinderGoalWrapped;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class NMSAccesor_1_14_R1 implements NMSAccesor {
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
         net.minecraft.server.v1_14_R1.Entity entity = ((CraftEntity)en).getHandle();
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
      EntityInsentient insentient = this.getEntityInsentiment(entity);
      if (insentient != null) {
         IAttribute attribute = (IAttribute)at;
         insentient.getAttributeMap().b(attribute);
      }
   }

   public void setAttributeValue(Object attribute, Double value, Entity entity) {
      EntityInsentient insentient = this.getEntityInsentiment(entity);
      if (insentient != null) {
         IAttribute at = (IAttribute)attribute;
         insentient.getAttributeInstance(at).setValue(value);
      }
   }

   public void registerAttribute(Attribute a, Double value, Entity who) {
      EntityInsentient insentient = (EntityInsentient)((CraftEntity)who).getHandle();

      try {
         insentient.getAttributeMap().b(this.bukkitToNMSAttribute(a));
      } catch (IllegalArgumentException var6) {
      }

      insentient.getAttributeInstance(this.bukkitToNMSAttribute(a)).setValue(value);
   }

   public void unregisterAttributes(Entity who) {
      EntityLiving insentient = (EntityLiving)((CraftEntity)who).getHandle();

      try {
         Field f = EntityLiving.class.getDeclaredField("attributeMap");
         f.setAccessible(true);
         f.set(insentient, (Object)null);
         Class c = ((CraftEntity)who).getHandle().getClass();
         Method m = c.getDeclaredMethod("initAttributes");
         m.setAccessible(true);
         m.invoke(((CraftEntity)who).getHandle());
      } catch (Throwable var6) {
         var6.printStackTrace();
      }

   }

   public void registerKnockback(Double value, Double attackValue, Entity who) {
   }

   public void registerAttribute(HashMap<Attribute, Double> a, Entity who) {
      EntityInsentient insentient = (EntityInsentient)((CraftEntity)who).getHandle();

      Attribute attribute;
      Double value;
      for(Iterator var4 = a.keySet().iterator(); var4.hasNext(); insentient.getAttributeInstance(this.bukkitToNMSAttribute(attribute)).setValue(value)) {
         attribute = (Attribute)var4.next();
         value = (Double)a.get(attribute);

         try {
            insentient.getAttributeMap().b(this.bukkitToNMSAttribute(attribute));
         } catch (IllegalArgumentException var8) {
         }
      }

   }

   public void registerHostileMobs() {
      EntityType[] var1 = EntityType.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EntityType p = var1[var3];
         if (Main.getInstance().getHostile().isHostileMob(p)) {
            return;
         }

         try {
            Field fieldMap = EntityLiving.class.getDeclaredField("attributeMap");
            fieldMap.setAccessible(true);
            AttributeMapBase map = (AttributeMapBase)fieldMap.get(EntityLiving.class);
            map.b(GenericAttributes.ATTACK_DAMAGE);
            map.a(GenericAttributes.ATTACK_DAMAGE).setValue(8.0D);
            fieldMap.set(EntityLiving.class, map);
         } catch (Throwable var7) {
         }
      }

   }

   public void injectHostilePathfinders(Entity entity) {
      net.minecraft.server.v1_14_R1.Entity nms = ((CraftEntity)entity).getHandle();
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
   }

   public void addNMSTag(Entity entity) {
      net.minecraft.server.v1_14_R1.Entity en = ((CraftEntity)entity).getHandle();
      NBTTagCompound t = new NBTTagCompound();
      t.setInt("Invul", 100);
      en.save(t);
   }

   public IAttribute bukkitToNMSAttribute(Attribute attribute) {
      IAttribute ia = null;
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

      return ia;
   }

   public Class getNMSEntityClass(String name) {
      try {
         return Class.forName("net.minecraft.server.v1_14_R1.Entity" + name);
      } catch (ClassNotFoundException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public void drown(Player p, double ammount) {
      net.minecraft.server.v1_14_R1.Entity en = ((CraftEntity)p).getHandle();
      en.damageEntity(DamageSource.DROWN, (float)ammount);
   }

   public AttributeInstance getAtribute(Entity en, Object at) {
      return this.getEntityInsentiment(en) == null ? null : this.getEntityInsentiment(en).getAttributeInstance((IAttribute)at);
   }
}
