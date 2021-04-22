package com.permadeathcore.Listener.Entity;

import com.permadeathcore.Main;
import com.permadeathcore.CustomMobs.DeathModule.DeathModule;
import com.permadeathcore.NMS.VersionManager;
import com.permadeathcore.Task.GatoGalacticoTask;
import com.permadeathcore.Util.Item.NetheriteArmor;
import com.permadeathcore.Util.Item.PermaDeathItems;
import com.permadeathcore.Util.Library.ItemBuilder;
import com.permadeathcore.Util.Library.LeatherArmorBuilder;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.SplittableRandom;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Cat;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cod;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Illager;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Monster;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Player;
import org.bukkit.entity.PufferFish;
import org.bukkit.entity.Ravager;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Vindicator;
import org.bukkit.entity.Witch;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class SpawnListener implements Listener {
   private Main plugin;
   private SplittableRandom random;
   private ArrayList<LivingEntity> gatosSupernova;
   private EntityType PIGMAN;
   private Class pigmanClass;
   private boolean optimizeSpawns;

   public SpawnListener(Main instance) {
      this.plugin = instance;
      this.random = new SplittableRandom();
      this.gatosSupernova = new ArrayList();
      this.optimizeSpawns = instance.getConfig().getBoolean("Toggles.Optimizar-Mob-Spawns");
      /*if (VersionManager.isRunningNetherUpdate()) {
         this.PIGMAN = EntityType.valueOf("ZOMBIFIED_PIGLIN");
      } else {
         this.PIGMAN = EntityType.valueOf("PIG_ZOMBIE");
      }

      this.pigmanClass = this.PIGMAN.getEntityClass();*/
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onSpawn(CreatureSpawnEvent event) {
      final LivingEntity entity = event.getEntity();
      EntityType eventEntityType = event.getEntityType();
      Location location = event.getLocation();
      World world = location.getWorld();
      SpawnReason reason = event.getSpawnReason();
      Stream var10000;
      if (this.optimizeSpawns && world.getEnvironment() == Environment.NORMAL && (reason == SpawnReason.NATURAL || reason == SpawnReason.CUSTOM) && (eventEntityType == EntityType.COD || eventEntityType == EntityType.VINDICATOR || eventEntityType == EntityType.GUARDIAN || eventEntityType == EntityType.ELDER_GUARDIAN || eventEntityType == this.PIGMAN || eventEntityType == EntityType.EVOKER || eventEntityType == EntityType.CAVE_SPIDER || eventEntityType == EntityType.SKELETON || eventEntityType == EntityType.BLAZE)) {
         var10000 = Arrays.stream(location.getChunk().getEntities()).filter((entity1) -> {
            return entity1.getType() == eventEntityType;
         });
         Class var10001 = eventEntityType.getEntityClass();
         var10001.getClass();
         if (((List)var10000.map(var10001::cast).collect(Collectors.toList())).size() >= 8) {
            event.setCancelled(true);
         }
      }

      if (this.optimizeSpawns && world.getEnvironment() != Environment.THE_END && entity instanceof Monster && world.getEntitiesByClass(Monster.class).size() >= 220) {
         event.setCancelled(true);
      }

      if (!event.isCancelled()) {
         this.spawnBeginningMob(event);
         this.spawnNetheriteMob(event);
         this.plugin.deathTrainEffects(entity);
         if (entity instanceof Spider || eventEntityType == EntityType.SKELETON) {
            if (this.plugin.getConfig().getBoolean("Toggles.Spider-Effect") && entity instanceof Spider) {
               this.addMobEffects(entity, 100);
            }

            if (this.plugin.getDays() >= 20L) {
               if (reason == SpawnReason.CUSTOM) {
                  return;
               }

               this.spawnSkeletonClass(entity, location);
            }
         }

         int prob;
         int i;
         int clase;
         ItemStack helmet;
         EntityEquipment eq;
         int prob;
         if (this.plugin.getDays() >= 20L) {
            if (eventEntityType == EntityType.PHANTOM) {
               Phantom phantom = (Phantom)entity;
               prob = this.plugin.getDays() < 50L ? 9 : 18;
               this.plugin.getNmsAccesor().setMaxHealth(phantom, this.plugin.getNmsAccesor().getMaxHealth(phantom) * 2.0D, true);
               if (this.plugin.getDays() >= 40L) {
                  Skeleton skeleton = (Skeleton)this.plugin.getNmsHandler().spawnNMSEntity("Skeleton", EntityType.SKELETON, event.getLocation(), SpawnReason.NATURAL);
                  phantom.addPassenger(skeleton);
               }

               if (this.plugin.getDays() >= 50L) {
                  prob = this.plugin.getDays() < 60L ? 1 : 25;
                  if (this.random.nextInt(101) <= prob) {
                     for(i = 0; i < 4; ++i) {
                        this.plugin.getNmsHandler().spawnCustomGhast(event.getLocation(), SpawnReason.CUSTOM, true);
                     }
                  }

                  this.addMobEffects(entity, 3);
               }

               phantom.setSize(prob);
            }

            if (event.getEntityType() == this.PIGMAN) {
               if (this.plugin.getDays() >= 60L) {
                  event.setCancelled(true);
                  return;
               }

               LivingEntity pigman = entity;

               try {
                  Method m = pigman.getClass().getDeclaredMethod("setAngry", Boolean.TYPE);
                  m.setAccessible(true);
                  m.invoke(pigman, true);
               } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException var19) {
                  var19.printStackTrace();
               }

               if (this.plugin.getDays() >= 30L && this.plugin.getDays() < 40L) {
                  eq = entity.getEquipment();
                  eq.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
                  eq.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
                  eq.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
                  eq.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
               }

               if (this.plugin.getDays() >= 40L) {
                  if (event.getSpawnReason() == SpawnReason.CUSTOM) {
                     return;
                  }

                  if (this.plugin.getDays() >= 60L && world.getEnvironment() == Environment.NETHER) {
                     event.setCancelled(true);
                     return;
                  }

                  prob = this.random.nextInt(99) + 1;
                  prob = this.plugin.getDays() < 50L ? 5 : 20;
                  EntityEquipment eq;
                  if (prob <= prob) {
                     eq = entity.getEquipment();
                     clase = ThreadLocalRandom.current().nextInt(1, 6);
                     if (clase == 1) {
                        //this.spawnUltraRavager(event);
                     }

                     ItemStack che;
                     ItemStack le;
                     ItemStack bo;
                     if (clase == 2) {
                        helmet = (new LeatherArmorBuilder(Material.LEATHER_HELMET, 1)).setColor(Color.YELLOW).build();
                        che = (new LeatherArmorBuilder(Material.LEATHER_CHESTPLATE, 1)).setColor(Color.YELLOW).build();
                        le = (new LeatherArmorBuilder(Material.LEATHER_LEGGINGS, 1)).setColor(Color.YELLOW).build();
                        bo = (new LeatherArmorBuilder(Material.LEATHER_BOOTS, 1)).setColor(Color.YELLOW).build();
                        eq.setHelmet(helmet);
                        eq.setChestplate(che);
                        eq.setLeggings(le);
                        eq.setBoots(bo);
                        this.plugin.getNmsAccesor().setMaxHealth(entity, entity.getHealth(), true);
                        final LivingEntity bee;
                        if (VersionManager.isRunning14()) {
                           bee = (LivingEntity)event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.PARROT);
                           this.plugin.getNmsAccesor().registerAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 8.0D, bee);
                           this.plugin.getNmsAccesor().injectHostilePathfinders(bee);
                        } else {
                           bee = (LivingEntity)this.plugin.getNmsHandler().spawnNMSCustomEntity("PigmanJockeys.SpecialBee", EntityType.valueOf("BEE"), event.getLocation(), SpawnReason.CUSTOM);
                        }

                        entity.setCollidable(true);
                        bee.setCollidable(true);
                        Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable() {
                           public void run() {
                              entity.teleport(bee.getLocation());
                              bee.addPassenger(entity);
                           }
                        }, 10L);
                     }

                     if (clase == 3) {
                        entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(8.0D);
                        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
                        Ghast ghast = (Ghast)this.plugin.getNmsHandler().spawnCustomGhast(event.getLocation(), SpawnReason.CUSTOM, false);
                        ghast.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "tp_ghast"), PersistentDataType.BYTE, (byte)1);
                        ghast.setCollidable(true);
                        entity.setCollidable(true);
                        ghast.addPassenger(entity);
                     }

                     if (clase == 4) {
                        MagmaCube cube = (MagmaCube)this.plugin.getNmsHandler().spawnNMSEntity("MagmaCube", EntityType.MAGMA_CUBE, event.getLocation(), SpawnReason.SLIME_SPLIT);
                        cube.setSize(1);
                        this.plugin.getNmsAccesor().setMaxHealth(entity, 1.0D, true);
                        entity.setCollidable(false);
                        cube.addPassenger(entity);
                     }

                     if (clase == 5) {
                        helmet = (new LeatherArmorBuilder(Material.LEATHER_HELMET, 1)).setColor(Color.GRAY).build();
                        che = (new LeatherArmorBuilder(Material.LEATHER_CHESTPLATE, 1)).setColor(Color.GRAY).build();
                        le = (new LeatherArmorBuilder(Material.LEATHER_LEGGINGS, 1)).setColor(Color.GRAY).build();
                        bo = (new LeatherArmorBuilder(Material.LEATHER_BOOTS, 1)).setColor(Color.GRAY).build();
                        eq.setHelmet(helmet);
                        eq.setChestplate(che);
                        eq.setLeggings(le);
                        eq.setBoots(bo);
                        this.plugin.getNmsAccesor().setMaxHealth(entity, entity.getHealth(), true);
                        final Pig pig = (Pig)this.plugin.getNmsHandler().spawnNMSCustomEntity("PigmanJockeys.SpecialPig", EntityType.PIG, event.getLocation(), SpawnReason.SPAWNER_EGG);
                        Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable() {
                           public void run() {
                              pig.setSaddle(true);
                              entity.teleport(pig.getLocation());
                              pig.addPassenger(entity);
                           }
                        }, 10L);
                     }
                  } else {
                     eq = entity.getEquipment();
                     eq.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
                     eq.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
                     eq.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
                     eq.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
                  }
               }
            }
         }

         Ravager ultraRavager;
         if (this.plugin.getDays() >= 25L) {
            if (entity instanceof Ravager) {
               ultraRavager = (Ravager)entity;
               if (this.plugin.getDays() < 40L) {
                  ultraRavager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                  ultraRavager.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));
                  ultraRavager.setRemoveWhenFarAway(true);
               }
            }

            if (entity.getEquipment() != null && (entity.getType() == EntityType.SKELETON || entity.getType() == EntityType.ZOMBIE)) {
               ItemStack[] contents = (ItemStack[])entity.getEquipment().getArmorContents().clone();
               prob = 0;
               ItemStack[] var30 = contents;
               i = contents.length;

               for(clase = 0; clase < i; ++clase) {
                  helmet = var30[clase];
                  if (helmet != null && helmet.getType().name().toLowerCase().contains("leather_") && !helmet.getItemMeta().isUnbreakable()) {
                     contents[prob] = null;
                  }

                  ++prob;
               }

               entity.getEquipment().setArmorContents(contents);
            }
         }

         if (this.plugin.getDays() >= 30L) {
            if (entity instanceof Silverfish || entity instanceof Endermite) {
               this.addMobEffects(entity, 100);
            }

            Creeper c;
            if (entity instanceof Enderman) {
               entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, this.plugin.getDays() < 60L ? 1 : 9));
               if (this.plugin.getDays() >= 40L) {
                  if (world.getEnvironment() == Environment.NETHER) {
                     event.setCancelled(true);
                     c = this.plugin.getFactory().spawnEnderCreeper(event.getLocation(), (Creeper)null);
                     c.setMetadata("nether_creeper", new FixedMetadataValue(this.plugin, true));
                  }

                  if (this.random.nextInt(100) <= 4 && world.getEnvironment() == Environment.NORMAL) {
                     try {
                        DeathModule module = (DeathModule)Class.forName("com.permadeathcore.CustomMobs.DeathModule.DeathModule_" + VersionManager.getVersion()).newInstance();
                        module.spawn(event.getLocation());
                        event.setCancelled(true);
                     } catch (Exception var18) {
                     }
                  }
               }
            }

            if (entity instanceof Squid) {
               event.setCancelled(true);
               var10000 = location.getWorld().getNearbyEntities(location, 20.0D, 20.0D, 20.0D).stream().filter((entity1) -> {
                  return entity1 instanceof Guardian;
               });
               Guardian.class.getClass();
               if (((List)var10000.map(Guardian.class::cast).collect(Collectors.toList())).size() < 20) {
                  Guardian g = (Guardian)entity.getWorld().spawnEntity(event.getLocation(), EntityType.GUARDIAN);
                  g.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
               }
            }

            if (entity instanceof IronGolem) {
               entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
               if (this.plugin.getDays() >= 40L) {
                  entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, this.plugin.getDays() < 60L ? 0 : 3));
                  if (this.plugin.getDays() >= 50L) {
                     entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, this.plugin.getDays() < 60L ? 1 : 3));
                  }
               }
            }

            if (entity instanceof Bat) {
               event.setCancelled(true);
               var10000 = location.getWorld().getLivingEntities().stream().filter((entity1) -> {
                  return entity1 instanceof Blaze;
               });
               Blaze.class.getClass();
               if (((List)var10000.map(Blaze.class::cast).collect(Collectors.toList())).size() < 30) {
                  Blaze g = (Blaze)entity.getWorld().spawnEntity(event.getLocation(), EntityType.BLAZE);
                  g.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
               }
            }

            if (entity instanceof Creeper) {
               c = (Creeper)entity;
               c.setPowered(true);
               if (this.plugin.getDays() >= 40L) {
                  entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                  entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
                  if (this.plugin.getDays() >= 50L) {
                     if (this.plugin.getDays() < 60L) {
                        prob = this.random.nextInt(10);
                        if (prob <= 1) {
                           this.plugin.getFactory().spawnEnderCreeper(location, c);
                        } else {
                           this.plugin.getFactory().spawnQuantumCreeper(location, c);
                        }
                     } else {
                        this.plugin.getFactory().spawnEnderQuantumCreeper(location, c);
                        c.setMaxFuseTicks(c.getMaxFuseTicks() / 2);
                     }
                  }
               }
            }

            if (entity instanceof Pillager) {
               Pillager p = (Pillager)entity;
               p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
               p.getEquipment().setItemInMainHand((new ItemBuilder(Material.CROSSBOW)).addEnchant(Enchantment.QUICK_CHARGE, 4).build());
               p.getEquipment().setItemInMainHandDropChance(0.0F);
               if (this.plugin.getDays() >= 50L) {
                  prob = this.random.nextInt(100);
                  if (prob == 0) {
                     event.setCancelled(true);
                     event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.EVOKER);
                  }
               }
            }
         }

         Main var48;
         if (this.plugin.getDays() >= 40L) {
            if (entity.getType() == EntityType.GUARDIAN) {
               if (this.plugin.getDays() >= 60L) {
                  event.setCancelled(true);
                  var10000 = location.getWorld().getNearbyEntities(location, 20.0D, 20.0D, 20.0D).stream().filter((entity1) -> {
                     return entity1 instanceof ElderGuardian;
                  });
                  ElderGuardian.class.getClass();
                  if (((List)var10000.map(ElderGuardian.class::cast).collect(Collectors.toList())).size() < 5) {
                     event.getLocation().getWorld().spawn(event.getLocation(), ElderGuardian.class);
                  }

                  return;
               }

               entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
               entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
            }

            if (entity.getType() == EntityType.SPIDER) {
               event.setCancelled(true);
               this.plugin.getNmsHandler().spawnNMSEntity("CaveSpider", EntityType.CAVE_SPIDER, event.getLocation(), SpawnReason.NATURAL);
               var48 = this.plugin;
               entity.setCustomName(Main.format("&6Araña inmortal"));
            }

            if (entity.getType() == EntityType.ZOMBIE) {
               event.setCancelled(true);
               var10000 = world.getNearbyEntities(location, 15.0D, 15.0D, 15.0D).stream().filter((entity1) -> {
                  return entity1 instanceof Vindicator;
               });
               Vindicator.class.getClass();
               if (((List)var10000.map(Vindicator.class::cast).collect(Collectors.toList())).size() < 5) {
                  Vindicator vindicator = (Vindicator)event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.VINDICATOR);
                  vindicator.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));
                  this.plugin.getNmsAccesor().setMaxHealth(vindicator, this.plugin.getNmsAccesor().getMaxHealth(vindicator) * 2.0D, true);
               }
            }

            if (eventEntityType == EntityType.WOLF) {
               event.setCancelled(true);
               this.plugin.getNmsHandler().spawnNMSEntity("Cat", EntityType.CAT, event.getLocation(), SpawnReason.NATURAL);
            }

            if (eventEntityType == EntityType.CAT || eventEntityType == EntityType.OCELOT) {
               if (this.plugin.getDays() < 50L) {
                  var48 = this.plugin;
                  entity.setCustomName(Main.format("&6Gato Supernova"));
                  this.explodeCat(entity);
               } else if (event.getSpawnReason() == SpawnReason.CUSTOM) {
                  this.explodeCat(event.getEntity());
               } else {
                  LivingEntity var49 = event.getEntity();
                  var48 = this.plugin;
                  var49.setCustomName(Main.format("&6Gato Galáctico"));
               }
            }

            if (entity instanceof Cow || entity instanceof Sheep || entity instanceof Pig || entity instanceof MushroomCow) {
               if (!event.getLocation().getWorld().getName().equalsIgnoreCase(this.plugin.world.getName())) {
                  return;
               }

               if (this.plugin.getDays() < 50L && this.plugin.getDays() >= 40L) {
                  event.setCancelled(true);
                  this.plugin.getNmsHandler().spawnNMSEntity("Ravager", EntityType.RAVAGER, event.getLocation(), SpawnReason.NATURAL);
               }

               if (this.plugin.getDays() >= 50L) {
                  event.setCancelled(true);
                  ultraRavager = (Ravager)this.plugin.getNmsHandler().spawnNMSCustomEntity("PigmanJockeys.UltraRavager", EntityType.RAVAGER, event.getLocation(), SpawnReason.CUSTOM);
                  var48 = this.plugin;
                  ultraRavager.setCustomName(Main.format("&6Ultra Ravager"));
                  ultraRavager.setCustomNameVisible(true);
                  ultraRavager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                  ultraRavager.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
                  this.plugin.getNmsAccesor().setMaxHealth(ultraRavager, 500.0D, true);
               }
            }

            if (entity instanceof Chicken) {
               if (this.plugin.getDays() < 50L && this.plugin.getDays() >= 40L) {
                  event.setCancelled(true);
                  this.plugin.getNmsHandler().spawnNMSEntity("Ravager", EntityType.RAVAGER, event.getLocation(), SpawnReason.NATURAL);
                  return;
               }

               if (this.plugin.getDays() >= 50L) {
                  event.setCancelled(true);
                  event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.SILVERFISH);
               }
            }

            if (entity instanceof Witch) {
               entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
               this.plugin.getNmsAccesor().setMaxHealth(entity, entity.getHealth() * 2.0D, true);
               var48 = this.plugin;
               entity.setCustomName(Main.format("&6Bruja Imposible"));
            }
         }

         if (this.plugin.getDays() >= 50L) {
            if (entity instanceof Vindicator) {
               Illager i = (Illager)entity;
               i.getEquipment().setItemInMainHand((new ItemBuilder(Material.DIAMOND_AXE)).addEnchant(Enchantment.DAMAGE_ALL, 5).build());
            }

            if (event.getEntityType() == EntityType.VEX) {
               entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2));
            }

            if (event.getEntityType() == EntityType.BLAZE) {
               this.plugin.getNmsAccesor().setMaxHealth(entity, 200.0D, true);
            }

            if (entity.getType() == EntityType.COD) {
               if (event.getSpawnReason() == SpawnReason.CUSTOM) {
                  return;
               }

               Cod cod = (Cod)entity;
               cod.getEquipment().setItemInMainHand((new ItemBuilder(Material.WOODEN_SWORD)).addEnchant(Enchantment.DAMAGE_ALL, 50).addEnchant(Enchantment.KNOCKBACK, 100).build());
               cod.getEquipment().setItemInMainHandDropChance(0.0F);
               var48 = this.plugin;
               cod.setCustomName(Main.format("&6Bacalao de la Muerte"));
            }

            if (entity.getType() == EntityType.DROWNED) {
               entity.getEquipment().setItemInMainHand(new ItemStack(Material.TRIDENT));
            }

            if (entity.getType() == EntityType.SALMON) {
               event.setCancelled(true);
               var10000 = world.getNearbyEntities(location, 15.0D, 15.0D, 15.0D).stream().filter((entity1) -> {
                  return entity1 instanceof PufferFish;
               });
               PufferFish.class.getClass();
               if (((List)var10000.map(PufferFish.class::cast).collect(Collectors.toList())).size() < 2) {
                  event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.PUFFERFISH);
               }
            }

            if (entity.getType() == EntityType.PUFFERFISH) {
               PufferFish fish = (PufferFish)entity;
               var48 = this.plugin;
               fish.setCustomName(Main.format("&6Pufferfish invulnerable"));
               fish.setInvulnerable(true);
            }

            if (entity.getType() == EntityType.WITHER_SKELETON) {
               WitherSkeleton skeleton = (WitherSkeleton)entity;
               eq = skeleton.getEquipment();
               prob = this.random.nextInt(this.plugin.getDays() < 60L ? 50 : 13) + 1;
               if (skeleton.getWorld().getEnvironment() == Environment.NETHER && prob == 5) {
                  this.plugin.getNmsAccesor().setMaxHealth(skeleton, 80.0D, true);
                  var48 = this.plugin;
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
                  event.setCancelled(false);
               }
            }

            if (entity.getType() == EntityType.ZOMBIE) {
               boolean b = false;
               if (VersionManager.isRunning14()) {
                  b = event.getLocation().getWorld().getBiome((int)event.getLocation().getX(), (int)event.getLocation().getZ()) == Biome.PLAINS;
               } else {
                  try {
                     b = (Boolean)World.class.getDeclaredMethod("getBiome", Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke(event.getLocation().getWorld(), (int)event.getLocation().getX(), (int)event.getLocation().getY(), (int)event.getLocation().getZ());
                  } catch (Exception var17) {
                  }
               }

               if (b) {
                  int prob = true;
                  if (this.plugin.getDays() < 60L) {
                     prob = this.random.nextInt(500) + 1;
                  } else {
                     prob = this.random.nextInt(125) + 1;
                  }

                  if (event.getLocation().getBlock().getBiome() == Biome.PLAINS && prob == 5) {
                     this.plugin.getNmsHandler().spawnNMSCustomEntity("CustomGiant", EntityType.GIANT, event.getLocation(), SpawnReason.CUSTOM);
                     event.setCancelled(true);
                  }
               }
            }

            if (entity instanceof Ravager) {
               if (event.getSpawnReason() == SpawnReason.CUSTOM) {
                  return;
               }

               if (this.plugin.getDays() >= 50L && entity.getWorld().getName().equalsIgnoreCase(this.plugin.world.getName())) {
                  ultraRavager = (Ravager)this.plugin.getNmsHandler().spawnNMSCustomEntity("PigmanJockeys.UltraRavager", EntityType.RAVAGER, event.getLocation(), SpawnReason.CUSTOM);
                  var48 = this.plugin;
                  ultraRavager.setCustomName(Main.format("&6Ultra Ravager"));
                  ultraRavager.setCustomNameVisible(true);
                  ultraRavager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                  ultraRavager.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
                  this.plugin.getNmsAccesor().setMaxHealth(ultraRavager, 500.0D, true);
                  event.setCancelled(true);
               }
            }
         }

         if (this.plugin.getDays() >= 60L) {
            if (event.getEntityType() == EntityType.VEX) {
               Vex v = (Vex)entity;
               v.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2));
               v.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(7.0D);
            }

            if (event.getEntityType() == EntityType.VILLAGER) {
               event.setCancelled(true);
               if (this.random.nextBoolean()) {
                  event.getLocation().getWorld().spawn(event.getLocation(), Vindicator.class);
               } else {
                  event.getLocation().getWorld().spawn(event.getLocation(), Vex.class);
               }
            }

            if (eventEntityType == EntityType.VINDICATOR && this.random.nextBoolean()) {
               event.setCancelled(true);
               var10000 = world.getNearbyEntities(location, 15.0D, 15.0D, 15.0D).stream().filter((entity1) -> {
                  return entity1 instanceof Evoker;
               });
               Evoker.class.getClass();
               if (((List)var10000.map(Evoker.class::cast).collect(Collectors.toList())).size() < 5) {
                  Evoker evoker = (Evoker)event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.EVOKER);
                  this.plugin.getNmsAccesor().setMaxHealth(evoker, this.plugin.getNmsAccesor().getMaxHealth(evoker) * 2.0D, true);
                  evoker.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2));
               }
            }
         }

      }
   }

   @EventHandler
   public void onChunkLoad(ChunkLoadEvent e) {
      Stream var10000;
      Iterator var2;
      LivingEntity cats;
      if (this.plugin.getConfig().getBoolean("Toggles.Replace-Mobs-On-Chunk-Load")) {
         var10000 = Arrays.stream(e.getChunk().getEntities()).filter((entity) -> {
            return entity instanceof LivingEntity;
         });
         LivingEntity.class.getClass();
         var2 = ((List)var10000.map(LivingEntity.class::cast).collect(Collectors.toList())).iterator();

         while(var2.hasNext()) {
            cats = (LivingEntity)var2.next();
            this.applyDayChanges(cats);
         }
      }

      if (this.plugin.getDays() >= 40L && this.plugin.getDays() < 50L) {
         var10000 = Arrays.stream(e.getChunk().getEntities()).filter((entity) -> {
            return this.isACat(entity);
         });
         LivingEntity.class.getClass();
         var2 = ((List)var10000.map(LivingEntity.class::cast).collect(Collectors.toList())).iterator();

         Main var10001;
         while(var2.hasNext()) {
            cats = (LivingEntity)var2.next();
            var10001 = this.plugin;
            cats.setCustomName(Main.format("&6Gato Supernova"));
            this.explodeCat(cats);
         }

         var10000 = Arrays.stream(e.getChunk().getEntities()).filter((entity) -> {
            return entity instanceof Wolf;
         });
         Wolf.class.getClass();
         var2 = ((List)var10000.map(Wolf.class::cast).collect(Collectors.toList())).iterator();

         while(var2.hasNext()) {
            Wolf wolf = (Wolf)var2.next();
            Cat cat = (Cat)wolf.getWorld().spawn(wolf.getLocation().clone(), Cat.class);
            wolf.remove();
            cat.setAdult();
            var10001 = this.plugin;
            cat.setCustomName(Main.format("&6Gato Supernova"));
            this.explodeCat(cat);
         }
      }

   }

   private boolean isACat(Entity entity) {
      return entity.getType() == EntityType.CAT || entity.getType() == EntityType.OCELOT;
   }

   public void applyDayChanges(LivingEntity entity) {
      if (this.plugin.getDays() >= 30L) {
         if (entity instanceof Squid) {
            entity.remove();
            Guardian g = (Guardian)entity.getWorld().spawnEntity(entity.getLocation(), EntityType.GUARDIAN);
            g.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
         }

         if (entity instanceof Bat) {
            entity.remove();
            Blaze g = (Blaze)entity.getWorld().spawnEntity(entity.getLocation(), EntityType.BLAZE);
            g.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
         }
      }

      if (this.plugin.getDays() >= 40L) {
         if (entity instanceof Cow || entity instanceof Sheep || entity instanceof Pig || entity instanceof MushroomCow) {
            if (!entity.getLocation().getWorld().getName().equalsIgnoreCase(this.plugin.world.getName())) {
               return;
            }

            if (this.plugin.getDays() < 50L && this.plugin.getDays() >= 40L) {
               entity.remove();
               this.plugin.getNmsHandler().spawnNMSEntity("Ravager", EntityType.RAVAGER, entity.getLocation(), SpawnReason.NATURAL);
            }

            if (this.plugin.getDays() >= 50L) {
               entity.remove();
               Ravager ultraRavager = (Ravager)this.plugin.getNmsHandler().spawnNMSCustomEntity("PigmanJockeys.UltraRavager", EntityType.RAVAGER, entity.getLocation(), SpawnReason.CUSTOM);
               Main var10001 = this.plugin;
               ultraRavager.setCustomName(Main.format("&6Ultra Ravager"));
               ultraRavager.setCustomNameVisible(true);
               ultraRavager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
               ultraRavager.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
               this.plugin.getNmsAccesor().setMaxHealth(ultraRavager, 500.0D, true);
            }
         }

         if (entity instanceof Chicken) {
            if (this.plugin.getDays() < 50L && this.plugin.getDays() >= 40L) {
               entity.remove();
               this.plugin.getNmsHandler().spawnNMSEntity("Ravager", EntityType.RAVAGER, entity.getLocation(), SpawnReason.NATURAL);
               return;
            }

            if (this.plugin.getDays() >= 50L) {
               entity.getLocation().getWorld().spawnEntity(entity.getLocation(), EntityType.SILVERFISH);
               entity.remove();
            }
         }
      }

      if (this.plugin.getDays() >= 60L && entity.getType() == EntityType.VILLAGER) {
         if (this.random.nextBoolean()) {
            entity.getWorld().spawn(entity.getLocation(), Vex.class);
         } else {
            entity.getWorld().spawn(entity.getLocation(), Vindicator.class);
         }

         entity.remove();
      }

   }

   private void addMobEffects(LivingEntity entity, int force) {
      if (this.plugin.getDays() >= 10L) {
         ArrayList<String> effectList = new ArrayList();
         effectList.add("SPEED;2");
         effectList.add("REGENERATION;3");
         effectList.add("INCREASE_DAMAGE;3");
         effectList.add("INVISIBILITY;0");
         effectList.add("JUMP;4");
         effectList.add("SLOW_FALLING;0");
         effectList.add("DAMAGE_RESISTANCE;2");
         if (this.plugin.getDays() < 50L) {
            effectList.add("GLOWING;0");
         }

         int times = force == 100 ? (this.plugin.getDays() < 25L ? this.random.nextInt(this.plugin.getDays() < 20L ? 3 : 4) + 1 : 5) : force;

         for(int i = 0; i < times; ++i) {
            String[] s = ((String)effectList.get(this.random.nextInt(effectList.size()))).split(";");
            PotionEffectType type = PotionEffectType.getByName(s[0]);
            int lvl = Integer.parseInt(s[1]);
            if (entity.hasPotionEffect(type)) {
               --i;
               return;
            }

            entity.addPotionEffect(new PotionEffect(type, Integer.MAX_VALUE, lvl));
         }

         if (Main.DEBUG) {
            Bukkit.broadcastMessage("Efectos: " + times);
            Iterator var9 = entity.getActivePotionEffects().iterator();

            while(var9.hasNext()) {
               PotionEffect p = (PotionEffect)var9.next();
               Bukkit.broadcastMessage(p.getType().getName());
            }
         }

      }
   }

   private void spawnSkeletonClass(LivingEntity liv, Location l) {
      int bound = (this.plugin.getDays() < 60L ? 5 : 7) + 1;
      int randomClass = this.random.nextInt(bound);
      LivingEntity spider = null;
      Skeleton skeleton = null;
      World w = l.getWorld();
      if (!(liv instanceof CaveSpider)) {
         if (liv instanceof Spider) {
            if (randomClass != 5 && randomClass != 2) {
               skeleton = (Skeleton)w.spawn(l, Skeleton.class);
            } else {
               skeleton = (Skeleton)w.spawn(l, WitherSkeleton.class);
            }

            spider = liv;
         } else if (liv instanceof Skeleton) {
            skeleton = (Skeleton)liv;
            if (randomClass == 5 || randomClass == 2) {
               skeleton.remove();
               skeleton = (Skeleton)w.spawn(l, WitherSkeleton.class);
            }
         }

         EntityEquipment eq = skeleton.getEquipment();
         ItemStack helmet = null;
         ItemStack chestplate = null;
         ItemStack legs = null;
         ItemStack boots = null;
         ItemStack mainHand = null;
         ItemStack offHand = null;
         Float mainDrop = 0.0F;
         Float offDrop = 0.0F;
         Float armorDropChance = 0.8F;
         Enchantment armorEnchant = null;
         int armorEnchantLvl = 0;
         Double health = 20.0D;
         String name = "";
         String id = "";
         if (this.plugin.getDays() >= 30L) {
            offHand = this.getPotionItemStack();
         }

         if (this.plugin.getDays() >= 60L && this.random.nextInt(101) == 1) {
            if (skeleton != null) {
               skeleton.remove();
            }

            skeleton = (Skeleton)w.spawn(l, WitherSkeleton.class);
            skeleton.getEquipment().setItemInMainHand(this.buildItem(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 32765).build());
            skeleton.getEquipment().setItemInMainHandDropChance(0.0F);
            skeleton.setRemoveWhenFarAway(false);
            skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
            id = "skeleton_definitivo";
            name = "&6Ultra Esqueleto Definitivo";
            this.plugin.getNmsAccesor().setMaxHealth(skeleton, 400.0D, true);
         } else if (randomClass == 1) {
            helmet = this.buildItem(Material.DIAMOND_HELMET).build();
            chestplate = this.buildItem(Material.DIAMOND_CHESTPLATE).build();
            legs = this.buildItem(Material.DIAMOND_LEGGINGS).build();
            boots = this.buildItem(Material.DIAMOND_BOOTS).build();
            mainHand = this.buildItem(Material.BOW).build();
            health = this.plugin.getDays() < 30L ? 20.0D : (this.plugin.getDays() < 50L ? 40.0D : 100.0D);
            if (this.plugin.getDays() >= 30L) {
               armorEnchant = Enchantment.PROTECTION_ENVIRONMENTAL;
               armorEnchantLvl = this.plugin.getDays() < 60L ? 4 : 5;
               if (this.plugin.getDays() >= 60L) {
                  armorDropChance = 0.0F;
               }
            }
         } else {
            int bow_power_level;
            int infernal_skeleton_axe_sharpness;
            if (randomClass == 2) {
               helmet = this.buildItem(Material.CHAINMAIL_HELMET).build();
               chestplate = this.buildItem(Material.CHAINMAIL_CHESTPLATE).build();
               legs = this.buildItem(Material.CHAINMAIL_LEGGINGS).build();
               boots = this.buildItem(Material.CHAINMAIL_BOOTS).build();
               bow_power_level = this.plugin.getDays() < 30L ? 20 : (this.plugin.getDays() < 50L ? 30 : 50);
               infernal_skeleton_axe_sharpness = this.plugin.getDays() < 50L ? 25 : (this.plugin.getDays() < 60L ? 40 : 110);
               health = this.plugin.getDays() < 60L ? 40.0D : 60.0D;
               mainHand = this.buildItem(Material.BOW).addEnchant(Enchantment.ARROW_KNOCKBACK, bow_power_level).build();
               if (this.plugin.getDays() >= 30L) {
                  mainHand = (new ItemBuilder(mainHand)).addEnchant(Enchantment.ARROW_DAMAGE, infernal_skeleton_axe_sharpness).build();
               }
            } else if (randomClass == 3) {
               helmet = this.buildItem(Material.IRON_HELMET).build();
               chestplate = this.buildItem(Material.IRON_CHESTPLATE).build();
               legs = this.buildItem(Material.IRON_LEGGINGS).build();
               boots = this.buildItem(Material.IRON_BOOTS).build();
               bow_power_level = this.plugin.getDays() < 30L ? 2 : (this.plugin.getDays() < 50L ? 10 : 20);
               infernal_skeleton_axe_sharpness = this.plugin.getDays() < 60L ? 25 : 100;
               Material material = this.plugin.getDays() < 30L ? Material.IRON_AXE : Material.DIAMOND_AXE;
               mainHand = this.buildItem(material).addEnchant(Enchantment.FIRE_ASPECT, bow_power_level).build();
               health = this.plugin.getDays() < 30L ? 20.0D : (this.plugin.getDays() < 60L ? 40.0D : 100.0D);
               if (this.plugin.getDays() >= 50L) {
                  mainHand = (new ItemBuilder(mainHand)).addEnchant(Enchantment.DAMAGE_ALL, infernal_skeleton_axe_sharpness).build();
               }
            } else if (randomClass == 4) {
               helmet = this.buildItem(Material.GOLDEN_HELMET).build();
               chestplate = this.buildItem(Material.GOLDEN_CHESTPLATE).build();
               legs = this.buildItem(Material.GOLDEN_LEGGINGS).build();
               boots = this.buildItem(Material.GOLDEN_BOOTS).build();
               bow_power_level = this.plugin.getDays() < 30L ? 20 : (this.plugin.getDays() < 50L ? 25 : (this.plugin.getDays() < 60L ? 50 : 100));
               mainHand = this.buildItem(Material.CROSSBOW).addEnchant(Enchantment.DAMAGE_ALL, bow_power_level).build();
               health = this.plugin.getDays() < 60L ? 40.0D : 60.0D;
               if (this.plugin.getDays() >= 30L) {
                  skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, this.plugin.getDays() < 60L ? 1 : 3));
               }
            } else if (randomClass == 5) {
               helmet = (new LeatherArmorBuilder(Material.LEATHER_HELMET, 1)).setColor(Color.fromRGB(16711680)).setUnbrekeable(true).build();
               chestplate = (new LeatherArmorBuilder(Material.LEATHER_CHESTPLATE, 1)).setColor(Color.fromRGB(16711680)).setUnbrekeable(true).build();
               legs = (new LeatherArmorBuilder(Material.LEATHER_LEGGINGS, 1)).setColor(Color.fromRGB(16711680)).setUnbrekeable(true).build();
               boots = (new LeatherArmorBuilder(Material.LEATHER_BOOTS, 1)).setColor(Color.fromRGB(16711680)).setUnbrekeable(true).build();
               armorDropChance = 0.0F;
               bow_power_level = this.plugin.getDays() < 30L ? 10 : (this.plugin.getDays() < 50L ? 50 : (this.plugin.getDays() < 60L ? 60 : 150));
               mainHand = this.buildItem(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, bow_power_level).build();
               health = this.plugin.getDays() < 60L ? 40.0D : 60.0D;
            } /*else if (randomClass == 6) {
               helmet = (new LeatherArmorBuilder(Material.LEATHER_HELMET, 1)).setColor(Color.BLUE).setUnbrekeable(true).build();
               chestplate = (new LeatherArmorBuilder(Material.LEATHER_CHESTPLATE, 1)).setColor(Color.BLUE).setUnbrekeable(true).build();
               legs = (new LeatherArmorBuilder(Material.LEATHER_LEGGINGS, 1)).setColor(Color.BLUE).setUnbrekeable(true).build();
               boots = (new LeatherArmorBuilder(Material.LEATHER_BOOTS, 1)).setColor(Color.BLUE).setUnbrekeable(true).build();
               mainHand = this.buildItem(Material.BOW).build();
               armorDropChance = 0.0F;
               Main var10000 = this.plugin;
               name = Main.format("&6Ultra Esqueleto Demoníaco");
               id = "demon_skeleton";
               health = 100.0D;
            } else if (randomClass == 7) {
               helmet = (new LeatherArmorBuilder(Material.LEATHER_HELMET, 1)).setColor(Color.GREEN).setUnbrekeable(true).build();
               chestplate = (new LeatherArmorBuilder(Material.LEATHER_CHESTPLATE, 1)).setColor(Color.GREEN).setUnbrekeable(true).build();
               legs = (new LeatherArmorBuilder(Material.LEATHER_LEGGINGS, 1)).setColor(Color.GREEN).setUnbrekeable(true).build();
               boots = (new LeatherArmorBuilder(Material.LEATHER_BOOTS, 1)).setColor(Color.GREEN).setUnbrekeable(true).build();
               mainHand = this.buildItem(Material.BOW).build();
               offHand = this.getPotionItemStack2();
               armorDropChance = 0.0F;
               health = 100.0D;
               name = "&6Ultra Esqueleto Científico";
            }*/ else {
               helmet = this.buildItem(Material.DIAMOND_HELMET).build();
               chestplate = this.buildItem(Material.DIAMOND_CHESTPLATE).build();
               legs = this.buildItem(Material.DIAMOND_LEGGINGS).build();
               boots = this.buildItem(Material.DIAMOND_BOOTS).build();
               mainHand = this.buildItem(Material.BOW).build();
               health = this.plugin.getDays() < 30L ? 20.0D : (this.plugin.getDays() < 50L ? 40.0D : 100.0D);
               if (this.plugin.getDays() >= 30L) {
                  armorEnchant = Enchantment.PROTECTION_ENVIRONMENTAL;
                  armorEnchantLvl = this.plugin.getDays() < 60L ? 4 : 5;
                  if (this.plugin.getDays() >= 60L) {
                     armorDropChance = 0.0F;
                  }
               }
            }
         }

         if (armorEnchant != null) {
            helmet = (new ItemBuilder(helmet)).addEnchant(armorEnchant, armorEnchantLvl).build();
            chestplate = (new ItemBuilder(chestplate)).addEnchant(armorEnchant, armorEnchantLvl).build();
            legs = (new ItemBuilder(legs)).addEnchant(armorEnchant, armorEnchantLvl).build();
            boots = (new ItemBuilder(boots)).addEnchant(armorEnchant, armorEnchantLvl).build();
         }

         if (helmet != null) {
            eq.setHelmet(helmet);
         }

         if (chestplate != null) {
            eq.setChestplate(chestplate);
         }

         if (legs != null) {
            eq.setLeggings(legs);
         }

         if (boots != null) {
            eq.setBoots(boots);
         }

         if (mainHand != null) {
            eq.setItemInMainHand(mainHand);
         }

         if (offHand != null) {
            eq.setItemInOffHand(offHand);
         }

         if (!name.isEmpty()) {
            Main var10001 = this.plugin;
            skeleton.setCustomName(Main.format(name));
         }

         if (!id.isEmpty()) {
            skeleton.getPersistentDataContainer().set(new NamespacedKey(this.plugin, id), PersistentDataType.BYTE, (byte)1);
         }

         this.setMaxHealth(skeleton, health);
         skeleton.setHealth(health);
         eq.setItemInMainHandDropChance(mainDrop);
         eq.setItemInOffHandDropChance(offDrop);
         eq.setHelmetDropChance(armorDropChance);
         eq.setChestplateDropChance(armorDropChance);
         eq.setLeggingsDropChance(armorDropChance);
         eq.setBootsDropChance(armorDropChance);
         if (spider != null) {
            spider.addPassenger(skeleton);
         }

      }
   }

   private void spawnNetheriteMob(CreatureSpawnEvent event) {
      if (this.plugin.getDays() >= 25L) {
         Double HPGenerator;
         if (event.getEntityType() == EntityType.SLIME) {
            if (event.getSpawnReason() == SpawnReason.SLIME_SPLIT) {
               return;
            }

            Slime slime = (Slime)event.getEntity();
            HPGenerator = this.plugin.getDays() < 50L ? slime.getHealth() * 2.0D : slime.getHealth() * 4.0D;
            slime.setSize(this.plugin.getDays() < 50L ? 15 : 16);
            this.plugin.getNmsAccesor().setMaxHealth(slime, HPGenerator, true);
            slime.setCustomName(ChatColor.GOLD + "GIGA Slime");
         }

         int r;
         if (event.getEntityType() == EntityType.MAGMA_CUBE) {
            if (event.getSpawnReason() == SpawnReason.SLIME_SPLIT) {
               return;
            }

            r = this.plugin.getDays() < 50L ? 16 : 17;
            if (((List)event.getLocation().getWorld().getEntitiesByClass(MagmaCube.class).stream().filter((entity) -> {
               return entity.getSize() == r;
            }).collect(Collectors.toList())).size() >= 10) {
               event.setCancelled(true);
               return;
            }

            MagmaCube magmaCube = (MagmaCube)event.getEntity();
            magmaCube.setSize(r);
            magmaCube.setCustomName(ChatColor.GOLD + "GIGA MagmaCube");
            if (this.plugin.getDays() >= 50L) {
               this.plugin.getNmsAccesor().setMaxHealth(magmaCube, this.plugin.getNmsAccesor().getMaxHealth(magmaCube) * 2.0D, true);
            }
         }

         if (event.getEntityType() == EntityType.GHAST && event.getLocation().getWorld().getEnvironment() != Environment.THE_END && event.getSpawnReason() != SpawnReason.CUSTOM) {
            if (this.plugin.getDays() < 40L) {
               Double health = ThreadLocalRandom.current().nextDouble(40.0D, 61.0D);
               LivingEntity GhastDemon = event.getEntity();
               this.plugin.getNmsAccesor().setMaxHealth(GhastDemon, health, true);
               GhastDemon.setHealth(health);
               GhastDemon.setCustomName(ChatColor.GOLD + "Ghast Demoníaco");
            } else {
               r = this.random.nextInt(100) + 1;
               HPGenerator = ThreadLocalRandom.current().nextDouble(40.0D, 61.0D);
               LivingEntity GhastDemon = event.getEntity();
               this.plugin.getNmsAccesor().setMaxHealth(GhastDemon, HPGenerator, true);
               GhastDemon.setHealth(HPGenerator);
               if (r <= 75) {
                  GhastDemon.setCustomName(ChatColor.GOLD + "Demonio flotante");
                  GhastDemon.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "demonio_flotante"), PersistentDataType.BYTE, (byte)1);
               } else {
                  GhastDemon.setCustomName(ChatColor.GOLD + "Ghast Demoníaco");
                  GhastDemon.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "ghast_demoniaco"), PersistentDataType.BYTE, (byte)1);
               }
            }
         }

      }
   }

   public void explodeCat(final LivingEntity cat) {
      if (!this.gatosSupernova.contains(cat)) {
         this.gatosSupernova.add(cat);
         boolean canContinue = true;
         if (Bukkit.getOnlinePlayers().size() == 0) {
            canContinue = false;
         }

         if (this.gatosSupernova.size() > 2) {
            canContinue = false;
         }

         if (!canContinue) {
            cat.remove();
         } else {
            final World w = cat.getWorld();
            final Location loc = cat.getLocation().clone();
            final Chunk chunk = loc.getChunk();
            if (!chunk.isForceLoaded()) {
               chunk.setForceLoaded(true);
            }

            if (!chunk.isLoaded()) {
               chunk.load();
            }

            Main var10000 = this.plugin;
            Bukkit.broadcastMessage(Main.format("&cUn gato supernova va a explotar en: " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + " (" + cat.getWorld().getName() + ")."));
            Bukkit.getServer().getScheduler().runTaskLater(this.plugin, new Runnable() {
               public void run() {
                  if (cat == null) {
                     if (chunk.isForceLoaded()) {
                        chunk.setForceLoaded(false);
                     }

                     if (chunk.isLoaded()) {
                        chunk.unload();
                     }

                  } else if (!SpawnListener.this.gatosSupernova.contains(cat)) {
                     if (chunk.isForceLoaded()) {
                        chunk.setForceLoaded(false);
                     }

                     if (chunk.isLoaded()) {
                        chunk.unload();
                     }

                  } else {
                     float power = Float.valueOf((float)SpawnListener.this.plugin.getConfig().getInt("Toggles.Gatos-Supernova.Explosion-Power"));
                     boolean breakBlocks = SpawnListener.this.plugin.getConfig().getBoolean("Toggles.Gatos-Supernova.Destruir-Bloques");
                     boolean placeFire = SpawnListener.this.plugin.getConfig().getBoolean("Toggles.Gatos-Supernova.Fuego");
                     w.createExplosion(loc, power, placeFire, breakBlocks, cat);
                     SpawnListener.this.gatosSupernova.remove(cat);
                     cat.remove();
                     if (chunk.isForceLoaded()) {
                        chunk.setForceLoaded(false);
                     }

                     if (chunk.isLoaded()) {
                        chunk.unload();
                     }

                  }
               }
            }, 600L);
         }
      }
   }

   @EventHandler
   public void onDeath(EntityDeathEvent event) {
      if (this.plugin.getDays() >= 20L) {
         LivingEntity mob = event.getEntity();
         int chance;
         int randomSentence;
         int prob;
         String var10000;
         Main var10001;
         if (this.plugin.getDays() < 40L) {
            if (mob instanceof IronGolem || mob.getType() == this.PIGMAN || mob instanceof Ghast || mob instanceof Guardian || mob instanceof Enderman || mob instanceof Witch || mob instanceof WitherSkeleton || mob instanceof Evoker || mob instanceof Phantom || mob instanceof Slime || mob instanceof Drowned || mob instanceof Blaze) {
               event.getDrops().clear();
            }

            if (event.getEntity().getKiller() == null) {
               return;
            }

            Player killer = event.getEntity().getKiller();
            if (mob instanceof Ravager) {
               chance = this.random.nextInt(100) + 1;
               int needed = 1;
               if (this.plugin.getDays() >= 25L) {
                  needed = 20;
               }

               randomSentence = ThreadLocalRandom.current().nextInt(1, 5);
               if (chance <= needed) {
                  ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING, 1, (short)3);
                  mob.getWorld().dropItem(mob.getLocation(), totem);
                  killer.sendMessage(ChatColor.YELLOW + "¡Un tótem!");
                  killer.playSound(killer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, -5.0F);
               } else if (randomSentence == 1) {
                  assert killer != null;

                  killer.sendMessage(ChatColor.RED + "Vaya que mala suerte, ese ravager no tenia nada :(");
               } else if (randomSentence == 2) {
                  assert killer != null;

                  killer.sendMessage(ChatColor.RED + "¡Porras... otro ravager sin suerte!");
               } else if (randomSentence == 3) {
                  assert killer != null;

                  killer.sendMessage(ChatColor.RED + "Nada... hoy no hay totem :(");
               } else if (randomSentence == 4) {
                  assert killer != null;

                  killer.sendMessage(ChatColor.RED + "¡Hoy no es tu día!");
               }
            }
         } else {
            if (mob instanceof IronGolem || mob instanceof Ghast || mob instanceof Guardian || mob instanceof Enderman || mob instanceof Witch || mob instanceof WitherSkeleton || mob instanceof Evoker || mob instanceof Phantom || mob instanceof Slime || mob instanceof Drowned || mob instanceof Blaze) {
               event.getDrops().clear();
            }

            if (event.getEntityType() == EntityType.CAT || event.getEntityType() == EntityType.OCELOT) {
               if (this.gatosSupernova.contains(event.getEntity())) {
                  this.gatosSupernova.remove(event.getEntity());
               }

               if (event.getEntity().getCustomName() == null) {
                  return;
               }

               var10000 = event.getEntity().getCustomName();
               var10001 = this.plugin;
               if (var10000.contains(Main.format("&6Gato Gal"))) {
                  Location l = event.getEntity().getLocation();
                  chance = (int)l.getX();
                  int y = (int)l.getY();
                  randomSentence = (int)l.getZ();
                  Main var13 = this.plugin;
                  Bukkit.broadcastMessage(Main.format("&cLa maldición de un Gato Galáctico ha comenzado en: " + chance + ", " + y + ", " + randomSentence));
                  (new GatoGalacticoTask(event.getEntity().getLocation(), this.plugin)).runTaskTimer(this.plugin, 0L, 20L);
               }
            }

            if (mob.getType() == this.PIGMAN) {
               if (mob.getCustomName() == null) {
                  event.getDrops().clear();
                  return;
               }

               if (mob.getCustomName().equalsIgnoreCase(ChatColor.GREEN + "Carlos el Esclavo")) {
                  prob = this.random.nextInt(100) + 1;
                  chance = this.plugin.getDays() < 60L ? 100 : 33;
                  if (prob <= chance) {
                     event.getDrops().add(new ItemStack(Material.GOLD_INGOT, 32));
                  }
               }
            }

            if (mob instanceof Villager) {
               if (mob.getCustomName() == null) {
                  return;
               }

               if (mob.getCustomName().equalsIgnoreCase(ChatColor.GREEN + "Jess la Emperatriz")) {
                  prob = this.random.nextInt(100) + 1;
                  int prob = 100;
                  if (this.plugin.getDays() >= 60L) {
                     prob = 33;
                  }

                  if (prob <= prob) {
                     event.getDrops().add(new ItemStack(Material.GOLDEN_APPLE, 2));
                  }
               }
            }

            if (event.getEntity() instanceof Ravager) {
               Ravager ravager = (Ravager)event.getEntity();
               if (ravager.getCustomName() == null) {
                  return;
               }

               var10000 = ravager.getCustomName();
               var10001 = this.plugin;
               if (var10000.contains(Main.format("&6Ultra Ravager"))) {
                  if (ravager.getWorld().getEnvironment() != Environment.NETHER) {
                     event.getDrops().clear();
                  } else if (this.random.nextInt(100) + 1 <= (this.plugin.getDays() < 60L ? 100 : 33)) {
                     event.getDrops().add(new ItemStack(Material.TOTEM_OF_UNDYING));
                  }
               }
            }
         }

         if (this.plugin.getDays() < 60L && this.plugin.getDays() >= 50L) {
            if (event.getEntity().getType() == EntityType.GIANT) {
               List var14 = event.getDrops();
               ItemBuilder var15 = new ItemBuilder(Material.BOW);
               Main var10002 = this.plugin;
               var14.add(var15.setDisplayName(Main.format("&bArco de Gigante")).addEnchant(Enchantment.ARROW_DAMAGE, 10).build());
            }

            if (event.getEntity().getType() == EntityType.WITHER_SKELETON) {
               if (event.getEntity().getCustomName() == null) {
                  return;
               }

               var10000 = event.getEntity().getCustomName();
               var10001 = this.plugin;
               if (var10000.contains(Main.format("&6Wither Skeleton Emperador")) && this.plugin.getDays() < 60L) {
                  prob = (int)(Math.random() * 100.0D) + 1;
                  if (prob <= 50) {
                     event.getDrops().add(PermaDeathItems.createNetheriteSword());
                  }
               }
            }
         }
      }

      this.runNetheriteCheck(event);
   }

   private void runNetheriteCheck(EntityDeathEvent event) {
      if (this.plugin.getDays() >= 25L && this.plugin.getDays() < 30L && event.getEntity().getKiller() != null) {
         LivingEntity Mob = event.getEntity();
         int hp = Integer.parseInt((String)Objects.requireNonNull(Main.instance.getConfig().getString("Toggles.Netherite.Helmet")));
         int cp = Integer.parseInt((String)Objects.requireNonNull(Main.instance.getConfig().getString("Toggles.Netherite.Chestplate")));
         int lp = Integer.parseInt((String)Objects.requireNonNull(Main.instance.getConfig().getString("Toggles.Netherite.Leggings")));
         int bp = Integer.parseInt((String)Objects.requireNonNull(Main.instance.getConfig().getString("Toggles.Netherite.Boots")));
         int RandProb = ThreadLocalRandom.current().nextInt(1, 101);
         if (Mob instanceof CaveSpider && RandProb <= hp) {
            event.getDrops().clear();
            event.getDrops().add(NetheriteArmor.craftNetheriteHelmet());
         }

         if (Mob instanceof Slime && RandProb <= cp) {
            event.getDrops().clear();
            event.getDrops().add(NetheriteArmor.craftNetheriteChest());
         }

         if (Mob instanceof MagmaCube && RandProb <= lp) {
            event.getDrops().clear();
            event.getDrops().add(NetheriteArmor.craftNetheriteLegs());
         }

         if (Mob instanceof Ghast && RandProb <= bp) {
            event.getDrops().clear();
            event.getDrops().add(NetheriteArmor.craftNetheriteBoots());
         }

      }
   }

   /*private void spawnUltraRavager(CreatureSpawnEvent event) {
      Ravager ravager = (Ravager)event.getLocation().getWorld().spawn(event.getLocation(), Ravager.class);
      LivingEntity carlos = (LivingEntity)event.getLocation().getWorld().spawnEntity(event.getLocation(), this.PIGMAN);
      Villager jess = (Villager)event.getLocation().getWorld().spawn(event.getLocation(), Villager.class);
      carlos.addPassenger(jess);
      ravager.addPassenger(carlos);
      this.plugin.getNmsAccesor().setMaxHealth(jess, 500.0D, true);
      this.plugin.getNmsAccesor().setMaxHealth(carlos, 150.0D, true);
      this.plugin.getNmsAccesor().setMaxHealth(ravager, 240.0D, true);
      jess.setCustomName(ChatColor.GREEN + "Jess la Emperatriz");
      carlos.setCustomName(ChatColor.GREEN + "Carlos el Esclavo");
      ravager.setCustomName(ChatColor.GREEN + "Ultra Ravager");
      jess.getEquipment().setItemInMainHand(new ItemStack(Material.GOLDEN_APPLE, 2));
      jess.getEquipment().setItemInMainHandDropChance(0.0F);
      carlos.getEquipment().setItemInMainHand(new ItemStack(Material.GOLD_INGOT, 32));
      carlos.getEquipment().setItemInMainHandDropChance(0.0F);
      ravager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
      ravager.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
      ravager.getPersistentDataContainer().set(new NamespacedKey(this.plugin, "ultra_ravager"), PersistentDataType.BYTE, (byte)1);
      event.setCancelled(true);
   }*/

   private void spawnBeginningMob(CreatureSpawnEvent e) {
      World beginningWorld = e.getLocation().getWorld();
      Location location = e.getLocation();
      if (this.plugin.getDays() >= 0L) {
         if (this.plugin.getBeginningManager() != null) {
            if (this.plugin.getBeginningManager().getBeginningWorld() != null) {
               if (beginningWorld.getName().equalsIgnoreCase(this.plugin.getBeginningManager().getBeginningWorld().getName())) {
                  if (e.getSpawnReason() != SpawnReason.SPAWNER && e.getSpawnReason() != SpawnReason.CUSTOM) {
                     e.setCancelled(true);
                     if (beginningWorld.getLivingEntities().size() > 70) {
                        return;
                     }

                     int p = this.random.nextInt(101);
                     Main var10001;
                     if (p <= 60) {
                        WitherSkeleton skeleton = (WitherSkeleton)this.plugin.getNmsHandler().spawnNMSEntity("SkeletonWither", EntityType.WITHER_SKELETON, e.getLocation(), SpawnReason.CUSTOM);
                        skeleton.getEquipment().setChestplate((new LeatherArmorBuilder(Material.LEATHER_CHESTPLATE, 1)).setColor(Color.fromRGB(255, 182, 193)).build());
                        skeleton.getEquipment().setBoots((new LeatherArmorBuilder(Material.LEATHER_BOOTS, 1)).setColor(Color.fromRGB(255, 182, 193)).build());
                        int enchantLevel = (int)(Math.random() * 5.0D) + 1;
                        skeleton.getEquipment().setItemInMainHand((new ItemBuilder(PermaDeathItems.createNetheriteSword())).addEnchant(Enchantment.DAMAGE_ALL, enchantLevel).build());
                        skeleton.getEquipment().setChestplateDropChance(0.0F);
                        skeleton.getEquipment().setBootsDropChance(0.0F);
                        skeleton.getEquipment().setItemInMainHandDropChance(0.0F);
                        var10001 = this.plugin;
                        skeleton.setCustomName(Main.format("&6Wither Skeleton Rosáceo"));
                        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
                        this.plugin.getNmsAccesor().setMaxHealth(skeleton, 100.0D, true);
                     }

                     if (p > 60 && p <= 75) {
                        Vex vex = (Vex)beginningWorld.spawn(location, Vex.class);
                        vex.getEquipment().setHelmet((new ItemBuilder(Material.valueOf("HONEY_BLOCK"))).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).build());
                        vex.getEquipment().setItemInMainHand((new ItemBuilder(Material.END_CRYSTAL)).addEnchant(Enchantment.DAMAGE_ALL, 15).addEnchant(Enchantment.KNOCKBACK, 10).build());
                        vex.getEquipment().setHelmetDropChance(0.0F);
                        vex.getEquipment().setItemInMainHandDropChance(0.0F);
                        var10001 = this.plugin;
                        vex.setCustomName(Main.format("&6Vex Definitivo"));
                     }

                     if (p > 75 && p <= 79) {
                        Ghast ghast = (Ghast)this.plugin.getNmsHandler().spawnCustomGhast(e.getLocation().add(0.0D, 5.0D, 0.0D), SpawnReason.CUSTOM, true);
                        this.plugin.getNmsAccesor().setMaxHealth(ghast, 150.0D, true);
                        var10001 = this.plugin;
                        ghast.setCustomName(Main.format("&6Ender Ghast Definitivo"));
                     }

                     if (p >= 80) {
                        Creeper c = this.plugin.getFactory().spawnEnderQuantumCreeper(e.getLocation(), (Creeper)null);
                        this.plugin.getNmsAccesor().setMaxHealth(c, 100.0D, true);
                        c.setExplosionRadius(7);
                     }
                  }

               }
            }
         }
      }
   }

   public ItemStack getPotionItemStack() {
      ItemStack arrow = new ItemStack(Material.TIPPED_ARROW);
      PotionMeta meta = (PotionMeta)arrow.getItemMeta();
      meta.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE, false, true));
      arrow.setItemMeta(meta);
      return arrow;
   }

   public ItemStack getPotionItemStack2() {
      ItemStack arrow = new ItemStack(Material.TIPPED_ARROW);
      PotionMeta meta = (PotionMeta)arrow.getItemMeta();
      meta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 3600, 2), false);
      meta.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, 3600, 0), false);
      meta.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, 3600, 0), false);
      meta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 3600, 2), false);
      arrow.setItemMeta(meta);
      return arrow;
   }

   private Double getMaxHealthOf(LivingEntity entity) {
      return this.plugin.getNmsAccesor().getMaxHealth(entity);
   }

   private void setMaxHealth(Entity entity, Double health) {
      this.plugin.getNmsAccesor().setMaxHealth(entity, health, false);
   }

   private ItemBuilder buildItem(Material mat) {
      return new ItemBuilder(mat);
   }
}
