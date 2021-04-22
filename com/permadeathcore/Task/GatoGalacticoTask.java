package com.permadeathcore.Task;

import com.permadeathcore.Main;
import com.permadeathcore.NMS.VersionManager;
import com.permadeathcore.Util.Library.ItemBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.SplittableRandom;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.PufferFish;
import org.bukkit.entity.Ravager;
import org.bukkit.entity.Villager;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.EnderDragon.Phase;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class GatoGalacticoTask extends BukkitRunnable {
   private Location cat;
   private Main main;
   int time = 5;

   public GatoGalacticoTask(Location cat, Main main) {
      this.cat = cat;
      this.main = main;
   }

   public void run() {
      Main var10000;
      if (this.time > 0) {
         var10000 = this.main;
         Bukkit.broadcastMessage(Main.format("&eUn gato galáctico invocará un mob al azar en: &b" + this.time));
         Iterator var1 = Bukkit.getOnlinePlayers().iterator();

         while(var1.hasNext()) {
            Player all = (Player)var1.next();
            all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100.0F, 100.0F);
         }

         --this.time;
      }

      if (this.time == 0) {
         String entidad = "";
         ArrayList<EntityType> entity = new ArrayList();
         entity.add(EntityType.CAT);
         entity.add(EntityType.PUFFERFISH);
         entity.add(EntityType.RAVAGER);
         entity.add(EntityType.ENDER_DRAGON);
         entity.add(EntityType.SKELETON);
         entity.add(EntityType.SLIME);
         entity.add(EntityType.MAGMA_CUBE);
         entity.add(EntityType.WITCH);
         entity.add(EntityType.SPIDER);
         entity.add(EntityType.SILVERFISH);
         entity.add(EntityType.ENDERMITE);
         entity.add(EntityType.PHANTOM);
         entity.add(EntityType.GHAST);
         entity.add(EntityType.CREEPER);
         entity.add(EntityType.SHULKER);
         entity.add(EntityType.GIANT);
         entity.add(EntityType.WITHER_SKELETON);
         int c = (new Random()).nextInt(entity.size());
         EntityType type = (EntityType)entity.get(c);
         SplittableRandom random = new SplittableRandom();
         Main var10001;
         if (type == EntityType.CAT) {
            entidad = "Gato Supernova";
            Cat gato = (Cat)this.cat.getWorld().spawnEntity(this.cat, EntityType.CAT);
            gato.setAdult();
            var10001 = this.main;
            gato.setCustomName(Main.format("&6" + entidad));
            Main.getInstance().getSpawnListener().explodeCat(gato);
         } else if (type == EntityType.PUFFERFISH) {
            entidad = "Pufferfish invulnerable";
            PufferFish var23 = (PufferFish)this.cat.getWorld().spawnEntity(this.cat, EntityType.PUFFERFISH);
         } else {
            LivingEntity GhastDemon;
            if (type == EntityType.RAVAGER) {
               EntityType PIGMAN;
               if (VersionManager.isRunningNetherUpdate()) {
                  PIGMAN = EntityType.valueOf("ZOMBIFIED_PIGLIN");
               } else {
                  PIGMAN = EntityType.valueOf("PIG_ZOMBIE");
               }

               entidad = "Ultra Ravager";
               Ravager ravager = (Ravager)this.cat.getWorld().spawn(this.cat, Ravager.class);
               GhastDemon = (LivingEntity)this.cat.getWorld().spawnEntity(this.cat, PIGMAN);
               Villager jess = (Villager)this.cat.getWorld().spawn(this.cat, Villager.class);
               GhastDemon.addPassenger(jess);
               ravager.addPassenger(GhastDemon);
               Main.instance.getNmsAccesor().setMaxHealth(jess, 500.0D, true);
               Main.instance.getNmsAccesor().setMaxHealth(GhastDemon, 150.0D, true);
               Main.instance.getNmsAccesor().setMaxHealth(ravager, 240.0D, true);
               jess.setCustomName(ChatColor.GREEN + "Jess la Emperatriz");
               GhastDemon.setCustomName(ChatColor.GREEN + "Carlos el Esclavo");
               ravager.setCustomName(ChatColor.GREEN + "Ultra Ravager");
               jess.getEquipment().setItemInMainHand(new ItemStack(Material.GOLDEN_APPLE, 2));
               jess.getEquipment().setItemInMainHandDropChance(0.0F);
               GhastDemon.getEquipment().setItemInMainHand(new ItemStack(Material.GOLD_INGOT, 32));
               GhastDemon.getEquipment().setItemInMainHandDropChance(0.0F);
               ravager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
               ravager.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
               ravager.getPersistentDataContainer().set(new NamespacedKey(Main.instance, "ultra_ravager"), PersistentDataType.BYTE, (byte)1);
            } else if (type == EntityType.ENDER_DRAGON) {
               entidad = "Permadeath Demon";
               EnderDragon demon = (EnderDragon)this.cat.getWorld().spawnEntity(this.cat, EntityType.ENDER_DRAGON);
               demon.setPhase(Phase.LEAVE_PORTAL);
               demon.setAI(true);
               var10001 = this.main;
               demon.setCustomName(Main.format("&6&lPERMADEATH DEMON"));
               this.main.getNmsAccesor().setMaxHealth(demon, 1350.0D, true);
            } else if (type == EntityType.SKELETON) {
               entidad = "Esqueleto de Clase";
               Main.instance.getNmsHandler().spawnNMSEntity("Skeleton", EntityType.SKELETON, this.cat, SpawnReason.NATURAL);
            } else if (type == EntityType.SLIME) {
               entidad = "Giga Slime";
               this.cat.getWorld().spawnEntity(this.cat, EntityType.SLIME);
            } else if (type == EntityType.MAGMA_CUBE) {
               entidad = "Giga MagmaCube";
               this.cat.getWorld().spawnEntity(this.cat, EntityType.MAGMA_CUBE);
            } else if (type == EntityType.WITCH) {
               entidad = "Bruja Imposible";
               this.cat.getWorld().spawnEntity(this.cat, EntityType.WITCH);
            } else if (type == EntityType.PHANTOM) {
               entidad = "Giga Phantom";
               Main.instance.getNmsHandler().spawnNMSEntity("Phantom", EntityType.PHANTOM, this.cat, SpawnReason.NATURAL);
            } else if (type == EntityType.SPIDER) {
               entidad = "Araña con Efectos";
               this.cat.getWorld().spawnEntity(this.cat, EntityType.CAVE_SPIDER);
            } else if (type == EntityType.SILVERFISH) {
               entidad = "Silverfish de la Muerte";
               this.cat.getWorld().spawnEntity(this.cat, EntityType.SILVERFISH);
            } else if (type == EntityType.ENDERMITE) {
               entidad = "Endermite Misterioso";
               this.cat.getWorld().spawnEntity(this.cat, EntityType.ENDERMITE);
            } else if (type == EntityType.SHULKER) {
               entidad = "Shulker Tnt";
               this.cat.getWorld().spawnEntity(this.cat, EntityType.SHULKER);
            } else {
               int i;
               if (type == EntityType.GHAST) {
                  i = random.nextInt(3) + 1;
                  if (i == 1) {
                     entidad = "Ender Ghast";
                     this.main.getNmsHandler().spawnCustomGhast(this.cat, SpawnReason.CUSTOM, true);
                  }

                  if (i == 2) {
                     entidad = "Ghast Demoníaco";
                     Ghast GhastDemon = (Ghast)this.cat.getWorld().spawnEntity(this.cat, EntityType.GHAST);
                     Double HPGenerator = ThreadLocalRandom.current().nextDouble(40.0D, 61.0D);
                     this.main.getNmsAccesor().setMaxHealth(GhastDemon, HPGenerator, true);
                     GhastDemon.setHealth(HPGenerator);
                     GhastDemon.setCustomName(ChatColor.GOLD + "Ghast Demoníaco");
                  }

                  if (i == 3) {
                     entidad = "Demonio Flotante";
                     Double HPGenerator = ThreadLocalRandom.current().nextDouble(40.0D, 61.0D);
                     GhastDemon = (LivingEntity)this.cat.getWorld().spawnEntity(this.cat, EntityType.GHAST);
                     this.main.getNmsAccesor().setMaxHealth(GhastDemon, HPGenerator, true);
                     GhastDemon.setHealth(HPGenerator);
                     GhastDemon.setCustomName(ChatColor.GOLD + "Demonio flotante");
                  }
               } else if (type == EntityType.CREEPER) {
                  if (Main.instance.getDays() < 60L) {
                     i = random.nextInt(3) + 1;
                  } else {
                     i = random.nextInt(2) + 1;
                  }

                  if (i == 1) {
                     entidad = "Ender Quantum Creeper";
                     this.main.getFactory().spawnEnderQuantumCreeper(this.cat, (Creeper)null);
                  }

                  if (i == 2) {
                     entidad = "Quantum Creeper";
                     this.main.getFactory().spawnQuantumCreeper(this.cat, (Creeper)null);
                  }

                  if (i == 3) {
                     entidad = "Ender Creeper";
                     this.main.getFactory().spawnEnderCreeper(this.cat, (Creeper)null);
                  }
               }
            }
         }

         if (type == EntityType.GIANT) {
            entidad = "Gigante";
            Main.instance.getNmsHandler().spawnNMSCustomEntity("CustomGiant", EntityType.GIANT, this.cat, SpawnReason.CUSTOM);
         }

         if (type == EntityType.WITHER_SKELETON) {
            entidad = "Wither Skeleton Emperador";
            WitherSkeleton skeleton = (WitherSkeleton)Main.instance.getNmsHandler().spawnNMSEntity("SkeletonWither", EntityType.WITHER_SKELETON, this.cat, SpawnReason.CUSTOM);
            EntityEquipment eq = skeleton.getEquipment();
            Main.instance.getNmsAccesor().setMaxHealth(skeleton, 80.0D, true);
            var10001 = Main.instance;
            skeleton.setCustomName(Main.format("&6Wither Skeleton Emperador"));
            skeleton.setCollidable(false);
            ItemStack i = new ItemStack(Material.BLACK_BANNER, 1);
            BannerMeta m = (BannerMeta)i.getItemMeta();
            List<Pattern> patterns = new ArrayList();
            patterns.add(new Pattern(DyeColor.YELLOW, PatternType.STRAIGHT_CROSS));
            patterns.add(new Pattern(DyeColor.BLACK, PatternType.BRICKS));
            patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
            patterns.add(new Pattern(DyeColor.YELLOW, PatternType.FLOWER));
            patterns.add(new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_TOP));
            patterns.add(new Pattern(DyeColor.RED, PatternType.GRADIENT_UP));
            m.setPatterns(patterns);
            i.setItemMeta(m);
            eq.setHelmet(i);
            eq.setHelmetDropChance(0.0F);
            eq.setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
            eq.setLeggings(new ItemStack(Material.GOLDEN_LEGGINGS));
            eq.setBoots(new ItemStack(Material.GOLDEN_BOOTS));
            eq.setItemInMainHand((new ItemBuilder(Material.BOW)).addEnchant(Enchantment.ARROW_KNOCKBACK, 5).addEnchant(Enchantment.ARROW_DAMAGE, 100).build());
            eq.setItemInMainHandDropChance(0.0F);
         }

         var10000 = this.main;
         Bukkit.broadcastMessage(Main.format("&eUn gato galáctico ha invicado un(a) &c&l" + entidad + " &7(" + this.cat.getX() + ", " + this.cat.getY() + ", " + this.cat.getZ()));
         this.cancel();
      }

   }
}
