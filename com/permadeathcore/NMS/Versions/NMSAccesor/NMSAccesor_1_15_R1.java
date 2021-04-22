package com.permadeathcore.NMS.Versions.NMSAccesor;

import com.permadeathcore.NMS.NMSAccesor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.server.v1_15_R1.AttributeInstance;
import net.minecraft.server.v1_15_R1.AttributeMapBase;
import net.minecraft.server.v1_15_R1.AttributeMapServer;
import net.minecraft.server.v1_15_R1.DamageSource;
import net.minecraft.server.v1_15_R1.EntityCreature;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityInsentient;
import net.minecraft.server.v1_15_R1.EntityLiving;
import net.minecraft.server.v1_15_R1.GenericAttributes;
import net.minecraft.server.v1_15_R1.IAttribute;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.PathEntity;
import net.minecraft.server.v1_15_R1.PathfinderGoal;
import net.minecraft.server.v1_15_R1.PathfinderGoalAvoidTarget;
import net.minecraft.server.v1_15_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_15_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_15_R1.PathfinderGoalPanic;
import net.minecraft.server.v1_15_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_15_R1.PathfinderGoalWrapped;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_15_R1.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

public class NMSAccesor_1_15_R1 implements NMSAccesor {
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
         net.minecraft.server.v1_15_R1.Entity entity = ((CraftEntity)en).getHandle();
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
      if (who instanceof LivingEntity) {
         net.minecraft.server.v1_15_R1.Entity e = ((CraftEntity)who).getHandle();
         if (!(e instanceof EntityInsentient)) {
            return;
         }

         EntityInsentient insentient = (EntityInsentient)e;

         try {
            insentient.getAttributeMap().b(this.bukkitToNMSAttribute(a));
         } catch (IllegalArgumentException var7) {
         }

         insentient.getAttributeInstance(this.bukkitToNMSAttribute(a)).setValue(value);
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
      try {
         Field fieldMap = EntityLiving.class.getDeclaredField("attributeMap");
         fieldMap.setAccessible(true);
         AttributeMapBase map = (AttributeMapBase)fieldMap.get((Object)null);
         map.b(GenericAttributes.ATTACK_DAMAGE);
         fieldMap.set((Object)null, map);
      } catch (Throwable var3) {
      }

   }

   public void unregisterAttributes(Entity who) {
      if (who instanceof LivingEntity) {
         if (who instanceof Mob) {
            ((Mob)who).setTarget((LivingEntity)null);
         }

         ((LivingEntity)who).setAI(false);
         EntityLiving insentient = (EntityLiving)((CraftEntity)who).getHandle();

         try {
            Field mapField = EntityLiving.class.getDeclaredField("attributeMap");
            mapField.setAccessible(true);
            AttributeMapServer s = new AttributeMapServer();
            s.b(GenericAttributes.MAX_HEALTH);
            s.b(GenericAttributes.KNOCKBACK_RESISTANCE);
            s.b(GenericAttributes.MOVEMENT_SPEED);
            s.b(GenericAttributes.ARMOR);
            s.b(GenericAttributes.ARMOR_TOUGHNESS);
            if (insentient instanceof EntityInsentient) {
               s.b(GenericAttributes.FOLLOW_RANGE).setValue(15.0D);
               s.b(GenericAttributes.ATTACK_KNOCKBACK);
            }

            s.a(GenericAttributes.MAX_HEALTH).setValue(((LivingEntity)who).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            if (((LivingEntity)who).getAttribute(Attribute.GENERIC_ARMOR) != null) {
               s.a(GenericAttributes.ARMOR).setValue(((LivingEntity)who).getAttribute(Attribute.GENERIC_ARMOR).getValue());
            }

            if (((LivingEntity)who).getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS) != null) {
               s.a(GenericAttributes.ARMOR_TOUGHNESS).setValue(((LivingEntity)who).getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).getValue());
            }

            s.a(GenericAttributes.MOVEMENT_SPEED).setValue(((LivingEntity)who).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue());
            s.a(GenericAttributes.FOLLOW_RANGE).setValue(((LivingEntity)who).getAttribute(Attribute.GENERIC_FOLLOW_RANGE).getValue());
            if (((LivingEntity)who).getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE) != null) {
               s.a(GenericAttributes.KNOCKBACK_RESISTANCE).setValue(((LivingEntity)who).getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE) == null ? 1.0D : ((LivingEntity)who).getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getValue());
            }

            if (((LivingEntity)who).getAttribute(Attribute.GENERIC_FLYING_SPEED) != null) {
               s.b(GenericAttributes.FLYING_SPEED);
               s.a(GenericAttributes.FLYING_SPEED).setValue(((LivingEntity)who).getAttribute(Attribute.GENERIC_FLYING_SPEED).getValue());
            }

            CraftAttributeMap m = new CraftAttributeMap(s);
            mapField.set(insentient, s);
            Field craftAttributesField = EntityLiving.class.getDeclaredField("craftAttributes");
            craftAttributesField.setAccessible(true);
            craftAttributesField.set(insentient, m);
         } catch (Throwable var7) {
            var7.printStackTrace();
         }

         ((LivingEntity)who).setAI(true);
      }
   }

   public void injectHostilePathfinders(Entity entity) {
      net.minecraft.server.v1_15_R1.Entity nms = ((CraftEntity)entity).getHandle();
      EntityType type = entity.getType();
      PathfinderGoalSelector goalSelector;
      Field dField;
      Set set;
      if (nms instanceof EntityCreature) {
         EntityCreature insentient = (EntityCreature)((CraftEntity)entity).getHandle();
         goalSelector = insentient.goalSelector;

         try {
            if (type == EntityType.LLAMA || type == EntityType.PANDA) {
               return;
            }

            dField = PathfinderGoalSelector.class.getDeclaredField("d");
            dField.setAccessible(true);
            set = (Set)dField.get(goalSelector);
            boolean containsMelee = false;
            Iterator var9 = set.iterator();

            while(var9.hasNext()) {
               PathfinderGoalWrapped w = (PathfinderGoalWrapped)var9.next();
               Field f = w.getClass().getDeclaredField("a");
               f.setAccessible(true);
               PathfinderGoal goal = (PathfinderGoal)f.get(w);
               if (goal.getClass().getName().equalsIgnoreCase(PathfinderGoalAvoidTarget.class.getName())) {
                  set.remove(goal);
               }

               if (goal.getClass().getName().equalsIgnoreCase(PathfinderGoalPanic.class.getName())) {
                  set.remove(goal);
               }

               if (goal.getClass().getName().equalsIgnoreCase(PathfinderGoalMeleeAttack.class.getName())) {
                  containsMelee = true;
               }
            }

            if (!containsMelee) {
               set.add(new PathfinderGoalWrapped(0, new PathfinderGoalMeleeAttack(insentient, 1.0D, true)));
            }

            dField.set(goalSelector, set);
         } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var14) {
            var14.printStackTrace();
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
         } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var13) {
            var13.printStackTrace();
         }
      }

   }

   public void unregisterHostilePathfinders(Entity entity) {
      net.minecraft.server.v1_15_R1.Entity nms = ((CraftEntity)entity).getHandle();
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
         } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var14) {
            var14.printStackTrace();
         }
      }

      if (nms instanceof EntityInsentient) {
         EntityInsentient insentient = (EntityInsentient)((CraftEntity)entity).getHandle();
         targetSelector = insentient.targetSelector;

         try {
            dField = PathfinderGoalSelector.class.getDeclaredField("d");
            dField.setAccessible(true);
            set = (Set)dField.get(targetSelector);
            if (set != null) {
               try {
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
               } catch (Throwable var12) {
               }
            }

            dField.set(targetSelector, set);
         } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var13) {
            var13.printStackTrace();
         }
      }

   }

   public void addNMSTag(Entity entity) {
      net.minecraft.server.v1_15_R1.Entity en = ((CraftEntity)entity).getHandle();
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
         return Class.forName("net.minecraft.server.v1_15_R1.Entity" + name);
      } catch (ClassNotFoundException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public void drown(Player p, double ammount) {
      net.minecraft.server.v1_15_R1.Entity en = ((CraftEntity)p).getHandle();
      en.damageEntity(DamageSource.DROWN, (float)ammount);
   }

   public AttributeInstance getAtribute(Entity en, Object at) {
      return this.getEntityInsentiment(en) == null ? null : this.getEntityInsentiment(en).getAttributeInstance((IAttribute)at);
   }
}
