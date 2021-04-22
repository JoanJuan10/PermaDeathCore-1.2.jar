package com.permadeathcore.NMS;

import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface NMSAccesor {
   void setMaxHealth(Entity var1, Double var2, boolean var3);

   Double getMaxHealth(Entity var1);

   Object getAtribute(Entity var1, Object var2);

   Object getEntityInsentiment(Entity var1);

   void moveEntityTo(Entity var1, Location var2, Double var3);

   void registerNewAttribute(Entity var1, Object var2);

   void setAttributeValue(Object var1, Double var2, Entity var3);

   void registerAttribute(Attribute var1, Double var2, Entity var3);

   void unregisterAttributes(Entity var1);

   void registerKnockback(Double var1, Double var2, Entity var3);

   void registerAttribute(HashMap<Attribute, Double> var1, Entity var2);

   void registerHostileMobs();

   void injectHostilePathfinders(Entity var1);

   void unregisterHostilePathfinders(Entity var1);

   void addNMSTag(Entity var1);

   Object bukkitToNMSAttribute(Attribute var1);

   Class getNMSEntityClass(String var1);

   void drown(Player var1, double var2);
}
