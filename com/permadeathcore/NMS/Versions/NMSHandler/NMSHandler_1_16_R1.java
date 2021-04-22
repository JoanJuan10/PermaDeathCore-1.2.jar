package com.permadeathcore.NMS.Versions.NMSHandler;

import com.permadeathcore.Main;
import com.permadeathcore.CustomMobs.v1_16_R1.CustomCreeper;
import com.permadeathcore.CustomMobs.v1_16_R1.CustomGhast;
import com.permadeathcore.NMS.NMSHandler;
import com.permadeathcore.NMS.VersionManager;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.UUID;
import net.minecraft.server.v1_16_R1.BlockPosition;
import net.minecraft.server.v1_16_R1.ChatMessageType;
import net.minecraft.server.v1_16_R1.EntityHuman;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.EntityWither;
import net.minecraft.server.v1_16_R1.EnumMobSpawn;
import net.minecraft.server.v1_16_R1.GenericAttributes;
import net.minecraft.server.v1_16_R1.IChatBaseComponent;
import net.minecraft.server.v1_16_R1.NBTTagCompound;
import net.minecraft.server.v1_16_R1.PacketPlayOutChat;
import net.minecraft.server.v1_16_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_16_R1.World;
import net.minecraft.server.v1_16_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R1.PacketPlayOutTitle.EnumTitleAction;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.persistence.PersistentDataType;

public class NMSHandler_1_16_R1 implements NMSHandler {
   public Class craftEntity(String path) {
      Class c = null;

      try {
         c = Class.forName("org.bukkit.craftbukkit.v1_16_R1.entity." + path);
      } catch (ClassNotFoundException var4) {
         var4.printStackTrace();
      }

      return c;
   }

   public Class getEntityTypesClass() {
      Class c = null;

      try {
         c = Class.forName("net.minecraft.server.v1_16_R1.EntityTypes");
      } catch (ClassNotFoundException var3) {
         var3.printStackTrace();
      }

      return c;
   }

   public Class getNMSClass(String name) {
      try {
         return Class.forName("net.minecraft.server.v1_16_R1." + name);
      } catch (ClassNotFoundException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public EntityType convertEntityType(Object ogType) {
      EntityTypes type = (EntityTypes)ogType;
      if (type != null) {
         if (type == EntityTypes.IRON_GOLEM) {
            return EntityType.IRON_GOLEM;
         }

         if (type == EntityTypes.SNOW_GOLEM) {
            return EntityType.SNOWMAN;
         }

         if (type == EntityTypes.WITHER) {
            return EntityType.WITHER;
         }

         if (type == EntityTypes.CHICKEN) {
            return EntityType.CHICKEN;
         }

         if (type == EntityTypes.COW) {
            return EntityType.COW;
         }

         if (type == EntityTypes.MOOSHROOM) {
            return EntityType.MUSHROOM_COW;
         }

         if (type == EntityTypes.PIG) {
            return EntityType.PIG;
         }

         if (type == EntityTypes.SHEEP) {
            return EntityType.SHEEP;
         }

         if (type == EntityTypes.SQUID) {
            return EntityType.SQUID;
         }

         if (type == EntityTypes.VILLAGER) {
            return EntityType.VILLAGER;
         }

         if (type == EntityTypes.WANDERING_TRADER) {
            return EntityType.WANDERING_TRADER;
         }

         if (type == EntityTypes.BAT) {
            return EntityType.BAT;
         }

         if (type == EntityTypes.OCELOT) {
            return EntityType.OCELOT;
         }

         if (type == EntityTypes.CAT) {
            return EntityType.CAT;
         }

         if (type == EntityTypes.DONKEY) {
            return EntityType.DONKEY;
         }

         if (type == EntityTypes.HORSE) {
            return EntityType.HORSE;
         }

         if (type == EntityTypes.MULE) {
            return EntityType.MULE;
         }

         if (type == EntityTypes.SKELETON_HORSE) {
            return EntityType.SKELETON_HORSE;
         }

         if (type == EntityTypes.ZOMBIE_HORSE) {
            return EntityType.ZOMBIE_HORSE;
         }

         if (type == EntityTypes.WOLF) {
            return EntityType.WOLF;
         }

         if (type == EntityTypes.FOX) {
            return EntityType.FOX;
         }

         if (type == EntityTypes.RABBIT) {
            return EntityType.RABBIT;
         }

         if (type == EntityTypes.PARROT) {
            return EntityType.PARROT;
         }

         if (type == EntityTypes.TURTLE) {
            return EntityType.TURTLE;
         }

         if (type == EntityTypes.COD) {
            return EntityType.COD;
         }

         if (type == EntityTypes.SALMON) {
            return EntityType.SALMON;
         }

         if (type == EntityTypes.PUFFERFISH) {
            return EntityType.PUFFERFISH;
         }

         if (type == EntityTypes.TROPICAL_FISH) {
            return EntityType.TROPICAL_FISH;
         }

         if (type == EntityTypes.ZOMBIE) {
            return EntityType.ZOMBIE;
         }

         if (type == EntityTypes.ENDERMAN) {
            return EntityType.ENDERMAN;
         }

         if (type == EntityTypes.DOLPHIN) {
            return EntityType.DOLPHIN;
         }

         if (type == EntityTypes.BEE) {
            return EntityType.valueOf("BEE");
         }

         if (type == EntityTypes.SPIDER) {
            return EntityType.SPIDER;
         }

         if (type == EntityTypes.CAVE_SPIDER) {
            return EntityType.CAVE_SPIDER;
         }

         if (type == EntityTypes.POLAR_BEAR) {
            return EntityType.POLAR_BEAR;
         }

         if (type == EntityTypes.LLAMA) {
            return EntityType.LLAMA;
         }

         if (type == EntityTypes.PANDA) {
            return EntityType.PANDA;
         }

         if (type == EntityTypes.BLAZE) {
            return EntityType.BLAZE;
         }

         if (type == EntityTypes.CREEPER) {
            return EntityType.CREEPER;
         }

         if (type == EntityTypes.GHAST) {
            return EntityType.GHAST;
         }

         if (type == EntityTypes.MAGMA_CUBE) {
            return EntityType.MAGMA_CUBE;
         }

         if (type == EntityTypes.SILVERFISH) {
            return EntityType.SILVERFISH;
         }

         if (type == EntityTypes.SKELETON) {
            return EntityType.SKELETON;
         }

         if (type == EntityTypes.SLIME) {
            return EntityType.SLIME;
         }

         if (type == EntityTypes.ZOMBIE_VILLAGER) {
            return EntityType.ZOMBIE_VILLAGER;
         }

         if (type == EntityTypes.DROWNED) {
            return EntityType.DROWNED;
         }

         if (type == EntityTypes.WITHER_SKELETON) {
            return EntityType.WITHER_SKELETON;
         }

         if (type == EntityTypes.VINDICATOR) {
            return EntityType.VINDICATOR;
         }

         if (type == EntityTypes.EVOKER) {
            return EntityType.EVOKER;
         }

         if (type == EntityTypes.PILLAGER) {
            return EntityType.PILLAGER;
         }

         if (type == EntityTypes.RAVAGER) {
            return EntityType.RAVAGER;
         }

         if (type == EntityTypes.WITCH) {
            return EntityType.WITCH;
         }

         if (type == EntityTypes.VEX) {
            return EntityType.VEX;
         }

         if (type == EntityTypes.ENDERMITE) {
            return EntityType.ENDERMITE;
         }

         if (type == EntityTypes.GUARDIAN) {
            return EntityType.GUARDIAN;
         }

         if (type == EntityTypes.ELDER_GUARDIAN) {
            return EntityType.ELDER_GUARDIAN;
         }

         if (type == EntityTypes.SHULKER) {
            return EntityType.SHULKER;
         }

         if (type == EntityTypes.HUSK) {
            return EntityType.HUSK;
         }

         if (type == EntityTypes.STRAY) {
            return EntityType.STRAY;
         }

         if (type == EntityTypes.PHANTOM) {
            return EntityType.PHANTOM;
         }
      }

      return null;
   }

   public EntityTypes convertBukkitToNMS(EntityType type) {
      if (type == EntityType.IRON_GOLEM) {
         return EntityTypes.IRON_GOLEM;
      } else if (type == EntityType.SNOWMAN) {
         return EntityTypes.SNOW_GOLEM;
      } else if (type == EntityType.WITHER) {
         return EntityTypes.WITHER;
      } else if (type == EntityType.CHICKEN) {
         return EntityTypes.CHICKEN;
      } else if (type == EntityType.COW) {
         return EntityTypes.COW;
      } else if (type == EntityType.MUSHROOM_COW) {
         return EntityTypes.MOOSHROOM;
      } else if (type == EntityType.PIG) {
         return EntityTypes.PIG;
      } else if (type == EntityType.SHEEP) {
         return EntityTypes.SHEEP;
      } else if (type == EntityType.SQUID) {
         return EntityTypes.SQUID;
      } else if (type == EntityType.VILLAGER) {
         return EntityTypes.VILLAGER;
      } else if (type == EntityType.WANDERING_TRADER) {
         return EntityTypes.WANDERING_TRADER;
      } else if (type == EntityType.BAT) {
         return EntityTypes.BAT;
      } else if (type == EntityType.OCELOT) {
         return EntityTypes.OCELOT;
      } else if (type == EntityType.CAT) {
         return EntityTypes.CAT;
      } else if (type == EntityType.DONKEY) {
         return EntityTypes.DONKEY;
      } else if (type == EntityType.HORSE) {
         return EntityTypes.HORSE;
      } else if (type == EntityType.MULE) {
         return EntityTypes.MULE;
      } else if (type == EntityType.SKELETON_HORSE) {
         return EntityTypes.SKELETON_HORSE;
      } else if (type == EntityType.ZOMBIE_HORSE) {
         return EntityTypes.ZOMBIE_HORSE;
      } else if (type == EntityType.WOLF) {
         return EntityTypes.WOLF;
      } else if (type == EntityType.FOX) {
         return EntityTypes.FOX;
      } else if (type == EntityType.RABBIT) {
         return EntityTypes.RABBIT;
      } else if (type == EntityType.PARROT) {
         return EntityTypes.PARROT;
      } else if (type == EntityType.TURTLE) {
         return EntityTypes.TURTLE;
      } else if (type == EntityType.COD) {
         return EntityTypes.COD;
      } else if (type == EntityType.SALMON) {
         return EntityTypes.SALMON;
      } else if (type == EntityType.PUFFERFISH) {
         return EntityTypes.PUFFERFISH;
      } else if (type == EntityType.TROPICAL_FISH) {
         return EntityTypes.TROPICAL_FISH;
      } else if (type == EntityType.ZOMBIE) {
         return EntityTypes.ZOMBIE;
      } else if (type == EntityType.ENDERMAN) {
         return EntityTypes.ENDERMAN;
      } else if (type == EntityType.DOLPHIN) {
         return EntityTypes.DOLPHIN;
      } else if (type == EntityType.valueOf("BEE")) {
         return EntityTypes.BEE;
      } else if (type == EntityType.SPIDER) {
         return EntityTypes.SPIDER;
      } else if (type == EntityType.CAVE_SPIDER) {
         return EntityTypes.CAVE_SPIDER;
      } else if (type == EntityType.POLAR_BEAR) {
         return EntityTypes.POLAR_BEAR;
      } else if (type == EntityType.LLAMA) {
         return EntityTypes.LLAMA;
      } else if (type == EntityType.PANDA) {
         return EntityTypes.PANDA;
      } else if (type == EntityType.BLAZE) {
         return EntityTypes.BLAZE;
      } else if (type == EntityType.CREEPER) {
         return EntityTypes.CREEPER;
      } else if (type == EntityType.GHAST) {
         return EntityTypes.GHAST;
      } else if (type == EntityType.MAGMA_CUBE) {
         return EntityTypes.MAGMA_CUBE;
      } else if (type == EntityType.SILVERFISH) {
         return EntityTypes.SILVERFISH;
      } else if (type == EntityType.SKELETON) {
         return EntityTypes.SKELETON;
      } else if (type == EntityType.SLIME) {
         return EntityTypes.SLIME;
      } else if (type == EntityType.ZOMBIE_VILLAGER) {
         return EntityTypes.ZOMBIE_VILLAGER;
      } else if (type == EntityType.DROWNED) {
         return EntityTypes.DROWNED;
      } else if (type == EntityType.WITHER_SKELETON) {
         return EntityTypes.WITHER_SKELETON;
      } else if (type == EntityType.VINDICATOR) {
         return EntityTypes.VINDICATOR;
      } else if (type == EntityType.EVOKER) {
         return EntityTypes.EVOKER;
      } else if (type == EntityType.PILLAGER) {
         return EntityTypes.PILLAGER;
      } else if (type == EntityType.RAVAGER) {
         return EntityTypes.RAVAGER;
      } else if (type == EntityType.WITCH) {
         return EntityTypes.WITCH;
      } else if (type == EntityType.VEX) {
         return EntityTypes.VEX;
      } else if (type == EntityType.ENDERMITE) {
         return EntityTypes.ENDERMITE;
      } else if (type == EntityType.GUARDIAN) {
         return EntityTypes.GUARDIAN;
      } else if (type == EntityType.ELDER_GUARDIAN) {
         return EntityTypes.ELDER_GUARDIAN;
      } else if (type == EntityType.SHULKER) {
         return EntityTypes.SHULKER;
      } else if (type == EntityType.HUSK) {
         return EntityTypes.HUSK;
      } else if (type == EntityType.STRAY) {
         return EntityTypes.STRAY;
      } else {
         return type == EntityType.PHANTOM ? EntityTypes.PHANTOM : null;
      }
   }

   public void sendActionBar(Player player, String actionbar) {
      IChatBaseComponent chat = ChatSerializer.a("{\"text\": \"" + actionbar + "\"}");
      PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(chat, ChatMessageType.GAME_INFO, UUID.randomUUID());
      CraftPlayer craft = (CraftPlayer)player;
      craft.getHandle().playerConnection.sendPacket(packetPlayOutChat);
   }

   public void sendTitle(Player player, String title, String subtitle, int time1, int time2, int time3) {
      IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + title + "\"}");
      IChatBaseComponent chatSubtitle = ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
      PacketPlayOutTitle titleT = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
      PacketPlayOutTitle subtitleT = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatSubtitle);
      PacketPlayOutTitle length = new PacketPlayOutTitle(time1, time2, time3);
      ((CraftPlayer)player).getHandle().playerConnection.sendPacket(titleT);
      ((CraftPlayer)player).getHandle().playerConnection.sendPacket(subtitleT);
      ((CraftPlayer)player).getHandle().playerConnection.sendPacket(length);
   }

   public Entity spawnNMSEntity(String name, EntityType type, Location location, SpawnReason reason) {
      World nmsW = ((CraftWorld)location.getWorld()).getHandle();
      net.minecraft.server.v1_16_R1.Entity nmsEntity = null;

      try {
         if (name.toLowerCase().contains("bee") && VersionManager.getVersion().equalsIgnoreCase("1_14_R1")) {
            return null;
         }

         Class c = this.getNMSClass("Entity" + name);
         if (!name.toLowerCase().contains("bat") && !name.toLowerCase().contains("cod") && !name.toLowerCase().contains("salmon") && !name.toLowerCase().contains("squid") && !name.toLowerCase().contains("pufferfish") && !name.toLowerCase().contains("tropicalfish")) {
            nmsEntity = (net.minecraft.server.v1_16_R1.Entity)c.getConstructor(this.getNMSClass("EntityTypes"), this.getNMSClass("World")).newInstance(this.convertBukkitToNMS(type), nmsW);
         } else if (reason == SpawnReason.SPAWNER_EGG) {
            if ((new Random()).nextInt(499) <= 10) {
               nmsEntity = (net.minecraft.server.v1_16_R1.Entity)c.getConstructor(this.getNMSClass("EntityTypes"), this.getNMSClass("World")).newInstance(this.convertBukkitToNMS(type), nmsW);
            }
         } else {
            nmsEntity = (net.minecraft.server.v1_16_R1.Entity)c.getConstructor(this.getNMSClass("EntityTypes"), this.getNMSClass("World")).newInstance(this.convertBukkitToNMS(type), nmsW);
         }
      } catch (NoSuchMethodException var8) {
      } catch (IllegalAccessException var9) {
      } catch (InstantiationException var10) {
      } catch (InvocationTargetException var11) {
      }

      if (nmsEntity == null) {
         return null;
      } else {
         nmsEntity.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
         nmsW.addEntity(nmsEntity, reason);
         return nmsEntity == null ? null : nmsEntity.getBukkitEntity();
      }
   }

   public Entity spawnNMSWither(Location location) {
      World nmsW = ((CraftWorld)location.getWorld()).getHandle();
      NBTTagCompound compound = new NBTTagCompound();
      compound.setInt("Invul", 160);
      EntityWither wither = (EntityWither)EntityTypes.WITHER.spawnCreature(nmsW, compound, (IChatBaseComponent)null, (EntityHuman)null, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()), EnumMobSpawn.EVENT, true, false);
      return wither == null ? null : wither.getBukkitEntity();
   }

   public Entity spawnHostileMob(EntityType type, Location location, SpawnReason reason) {
      String s = type.toString().toLowerCase();
      net.minecraft.server.v1_16_R1.Entity nms = null;
      String name;
      if (s.contains("_")) {
         String[] split = s.split("_");
         String wordOne = split[0].toLowerCase();
         String wordTwo = split[1].toLowerCase();
         name = StringUtils.capitalize(wordOne) + StringUtils.capitalize(wordTwo);
      } else {
         name = StringUtils.capitalize(s);
      }

      try {
         if (name.toLowerCase().contains("bee") && VersionManager.getVersion().equalsIgnoreCase("1_14_R1")) {
            return null;
         }

         if (name.toLowerCase().contains("cod") && Main.getInstance().getDays() >= 50L) {
            return null;
         }

         Class c = Class.forName("com.permadeathcore.NMS.EntityRegistry.Entities.v" + VersionManager.getVersion() + ".Hostile" + name);
         if (!name.toLowerCase().contains("villager") && !name.toLowerCase().contains("wanderingtrader")) {
            if (!name.toLowerCase().contains("bat") && !name.toLowerCase().contains("cod") && !name.toLowerCase().contains("salmon") && !name.toLowerCase().contains("squid") && !name.toLowerCase().contains("pufferfish") && !name.toLowerCase().contains("tropicalfish")) {
               nms = (net.minecraft.server.v1_16_R1.Entity)c.getConstructor(Location.class, Main.getInstance().getNmsHandler().getNMSClass("EntityTypes")).newInstance(location, Main.getInstance().getNmsHandler().convertBukkitToNMS(type));
            } else if ((new Random()).nextInt(499) <= 10) {
               nms = (net.minecraft.server.v1_16_R1.Entity)c.getConstructor(Location.class, Main.getInstance().getNmsHandler().getNMSClass("EntityTypes")).newInstance(location, Main.getInstance().getNmsHandler().convertBukkitToNMS(type));
            }
         } else if (name.toLowerCase().contains("villager")) {
            nms = (net.minecraft.server.v1_16_R1.Entity)c.getConstructor(Location.class, Main.getInstance().getNmsHandler().getNMSClass("EntityTypes"), Villager.class).newInstance(location, Main.getInstance().getNmsHandler().convertBukkitToNMS(type), null);
         } else {
            nms = (net.minecraft.server.v1_16_R1.Entity)c.getConstructor(Location.class, Main.getInstance().getNmsHandler().getNMSClass("EntityTypes"), WanderingTrader.class).newInstance(location, Main.getInstance().getNmsHandler().convertBukkitToNMS(type), null);
         }
      } catch (NoSuchMethodException | ClassNotFoundException var10) {
      } catch (IllegalAccessException var11) {
      } catch (InstantiationException var12) {
      } catch (InvocationTargetException var13) {
      }

      return nms == null ? null : nms.getBukkitEntity();
   }

   public Entity spawnNMSCustomEntity(String name, EntityType type, Location location, SpawnReason reason) {
      World nmsW = ((CraftWorld)location.getWorld()).getHandle();
      net.minecraft.server.v1_16_R1.Entity nmsEntity = null;

      try {
         if (name.toLowerCase().contains("bee") && VersionManager.getVersion().equalsIgnoreCase("1_14_R1")) {
            return null;
         }

         Class c = Class.forName("com.permadeathcore.CustomMobs.v" + VersionManager.getVersion() + "." + name);
         nmsEntity = (net.minecraft.server.v1_16_R1.Entity)c.getConstructor(Location.class).newInstance(location);
      } catch (ClassNotFoundException | NoSuchMethodException var8) {
      } catch (IllegalAccessException var9) {
      } catch (InstantiationException var10) {
      } catch (InvocationTargetException var11) {
      }

      if (nmsEntity == null) {
         return null;
      } else {
         nmsEntity.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
         if (name.toLowerCase().contains("customcod")) {
            Main.getInstance().getNmsAccesor().registerKnockback(1500.0D, 30.0D, nmsEntity.getBukkitEntity());
         }

         if (name.toLowerCase().contains("pigmanjockeys.specialpig")) {
            Main.getInstance().getNmsAccesor().registerAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 40.0D, nmsEntity.getBukkitEntity());
         }

         nmsW.addEntity(nmsEntity, reason);
         return nmsEntity == null ? null : nmsEntity.getBukkitEntity();
      }
   }

   public Entity spawnCustomCreeper(Location location, SpawnReason reason, boolean ender) {
      World nmsW = ((CraftWorld)location.getWorld()).getHandle();
      CustomCreeper nmsEntity = new CustomCreeper(EntityTypes.CREEPER, nmsW, ender);
      nmsEntity.setPosition(location.getX(), location.getY(), location.getZ());
      nmsW.addEntity(nmsEntity, reason);
      return nmsEntity == null ? null : nmsEntity.getBukkitEntity();
   }

   public Entity spawnCustomGhast(Location location, SpawnReason reason, boolean isEnder) {
      World nmsW = ((CraftWorld)location.getWorld()).getHandle();
      CustomGhast nmsEntity = new CustomGhast(EntityTypes.GHAST, nmsW);
      nmsEntity.setPosition(location.getX(), location.getY(), location.getZ());
      nmsW.addEntity(nmsEntity, reason);
      if (isEnder) {
         nmsEntity.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(200.0D);
         LivingEntity e2 = (LivingEntity)nmsEntity.getBukkitEntity();
         e2.setHealth(200.0D);
         e2.setCustomName("§6Ender Ghast");
         e2.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "ender_ghast"), PersistentDataType.BYTE, (byte)1);
         e2.setCustomNameVisible(false);
      }

      return nmsEntity == null ? null : nmsEntity.getBukkitEntity();
   }

   public void addMushrooms() {
   }

   public World craftWorld(org.bukkit.World world) {
      return ((CraftWorld)world).getHandle();
   }
}
