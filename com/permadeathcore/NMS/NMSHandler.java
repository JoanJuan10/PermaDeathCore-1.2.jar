package com.permadeathcore.NMS;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public interface NMSHandler {
   EntityType convertEntityType(Object var1);

   Class getEntityTypesClass();

   Class getNMSClass(String var1);

   Object convertBukkitToNMS(EntityType var1);

   Object craftEntity(String var1);

   void sendActionBar(Player var1, String var2);

   void sendTitle(Player var1, String var2, String var3, int var4, int var5, int var6);

   Object craftWorld(World var1);

   Entity spawnNMSEntity(String var1, EntityType var2, Location var3, SpawnReason var4);

   Entity spawnNMSWither(Location var1);

   Entity spawnHostileMob(EntityType var1, Location var2, SpawnReason var3);

   Entity spawnNMSCustomEntity(String var1, EntityType var2, Location var3, SpawnReason var4);

   Entity spawnCustomCreeper(Location var1, SpawnReason var2, boolean var3);

   Entity spawnCustomGhast(Location var1, SpawnReason var2, boolean var3);

   void addMushrooms();
}
