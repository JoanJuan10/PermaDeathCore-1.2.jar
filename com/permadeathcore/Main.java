package com.permadeathcore;

import com.permadeathcore.Discord.DiscordManager;
import com.permadeathcore.End.EndManager;
import com.permadeathcore.Entity.MobFactory.MobFactory;
import com.permadeathcore.Listener.Block.BlockEvents;
import com.permadeathcore.Listener.Entity.EntityEvents;
import com.permadeathcore.Listener.Entity.SkeletonClasses;
import com.permadeathcore.Listener.Entity.SpawnListener;
import com.permadeathcore.Listener.Entity.TotemConsumeEvent;
import com.permadeathcore.Listener.PaperSpigot.PaperListeners;
import com.permadeathcore.Listener.Player.AnvilListener;
import com.permadeathcore.Listener.Player.PlayerEvents;
import com.permadeathcore.Listener.Player.VoidListeners;
import com.permadeathcore.Listener.Raid.RaidEvents;
import com.permadeathcore.Listener.World.WorldEvents;
import com.permadeathcore.NMS.NMSAccesor;
import com.permadeathcore.NMS.NMSFinder;
import com.permadeathcore.NMS.NMSHandler;
import com.permadeathcore.NMS.PeaceToHostileManager;
import com.permadeathcore.NMS.VersionManager;
import com.permadeathcore.Task.EndTask;
import com.permadeathcore.TheBeginning.BeginningManager;
import com.permadeathcore.TheBeginning.Block.CustomBlock;
import com.permadeathcore.Util.Configurations.Messages;
import com.permadeathcore.Util.GameEvent.LifeOrbEvent;
import com.permadeathcore.Util.GameEvent.ShellEvent;
import com.permadeathcore.Util.Item.NetheriteArmor;
import com.permadeathcore.Util.Item.PermaDeathItems;
import com.permadeathcore.Util.Library.FileAPI;
import com.permadeathcore.Util.Library.UpdateChecker;
import com.permadeathcore.Util.Manager.RecipeManager;
import com.permadeathcore.Util.Manager.Data.BeginningDataManager;
import com.permadeathcore.Util.Manager.Data.DateManager;
import com.permadeathcore.Util.Manager.Data.EndDataManager;
import com.permadeathcore.Util.Manager.Data.PlayerDataManager;
import com.permadeathcore.Util.Manager.Log.Log4JFilter;
import com.permadeathcore.Util.Manager.Log.PDCLog;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SplittableRandom;
import java.util.logging.Filter;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.boss.BarColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Ravager;
import org.bukkit.entity.Wither;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class Main extends JavaPlugin implements Listener {
   public static boolean DEBUG = false;
   public static boolean DISABLED_LINGERING = false;
   public static boolean SPEED_RUN_MODE = false;
   private int playTime = 0;
   public static Main instance;
   public static String tag = "";
   private NMSHandler nmsHandler;
   private NMSAccesor nmsAccesor;
   private PeaceToHostileManager hostile;
   private RecipeManager recipes;
   private CustomBlock netheriteBlock;
   private Messages messages;
   public World world = null;
   public World endWorld = null;
   private EndTask task = null;
   private EndManager endManager;
   private MobFactory factory;
   private BeginningManager begginingManager;
   private BeginningDataManager beData;
   private EndDataManager endData;
   public static boolean runningPaperSpigot = false;
   private Map<Integer, Boolean> registeredDays = new HashMap();
   private ArrayList<Player> doneEffectPlayers = new ArrayList();
   private boolean loaded = false;
   private boolean alreadyRegisteredChanges = false;
   private ShellEvent shulkerEvent;
   private LifeOrbEvent orbEvent;
   private SpawnListener spawnListener;
   private SplittableRandom r = new SplittableRandom();

   public void onEnable() {
      instance = this;
      this.saveDefaultConfig();
      this.setupConsoleFilter();
      this.setupListeners();
      this.setupCommands();
      tag = format(this.getConfig().contains("Prefix") ? this.getConfig().getString("Prefix") : "&c&lPERMADEATH&4&lCORE &7➤ &f");
      this.tickAll();
      this.playTime = this.getConfig().getInt("DontTouch.PlayTime");
   }

   public void onLoad() {
      instance = this;
      NMSFinder f = new NMSFinder(this);
      this.nmsHandler = (NMSHandler)f.getNMSHandler();
      this.nmsAccesor = (NMSAccesor)f.getNMSAccesor();
      this.netheriteBlock = (CustomBlock)f.getCustomBlock();
      this.nmsAccesor.registerHostileMobs();
   }

   public void onDisable() {
      this.getConfig().set("DontTouch.PlayTime", this.playTime);
      this.saveConfig();
      this.reloadConfig();
      DiscordManager.getInstance().onDisable();
      Bukkit.getConsoleSender().sendMessage(format("&f&m------------------------------------------"));
      Bukkit.getConsoleSender().sendMessage(format("         &c&lPERMADEATH&4&lCORE"));
      Bukkit.getConsoleSender().sendMessage(format("     &7- Desactivando el Plugin."));
      Bukkit.getConsoleSender().sendMessage(format("&f&m------------------------------------------"));
      instance = null;
   }

   private void tickAll() {
      Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
         public void run() {
            if (!Main.this.getFile().exists()) {
               Main.this.saveDefaultConfig();
            }

            Iterator var6;
            if (!Main.this.loaded) {
               if (Bukkit.getPluginManager().getPlugin("JDASpigot") != null) {
                  Bukkit.getConsoleSender().sendMessage(Main.format(Main.tag + "&aSe ha encontrado JDASpigot, cargando bot de Discord."));
                  DiscordManager.getInstance();
               } else {
                  Bukkit.getConsoleSender().sendMessage(Main.format(Main.tag + "&cNo se ha encontrado JDASpigot, es necesario para utilizar el bot de Discord."));
                  Bukkit.getConsoleSender().sendMessage(Main.format("&eDescarga aquí: &fhttps://www.dropbox.com/s/qdtqgfgv51lvag4/JDASpigot.jar?dl=0"));
                  Bukkit.getConsoleSender().sendMessage(Main.format("&eSi no puedes descargarlo allí, únete a este Discord y te daremos acceso al enlace: &ehttps://discord.gg/8evPbuxPke"));
               }

               Main.this.startPlugin();
               Main.this.setupConfig();
               if (!Main.this.getConfig().contains("config-version")) {
                  PDCLog.getInstance().log("Eliminando config.yml por versión antigua.");
                  Main.this.getFile().delete();
                  Main.this.saveDefaultConfig();
               } else {
                  try {
                     int version = Main.this.getConfig().getInt("config-version");
                     if (version != 2) {
                        Bukkit.getConsoleSender().sendMessage(Main.format(Main.tag + "&eEstamos eliminando config.yml debido a que está desactualizado."));
                        PDCLog.getInstance().log("Eliminando config.yml por versión antigua.");
                        Main.this.getFile().delete();
                        Main.this.saveDefaultConfig();
                     }
                  } catch (Exception var5) {
                     Main.this.getFile().delete();
                  }
               }

               if (Main.this.getConfig().getBoolean("Toggles.Replace-Mobs-On-Chunk-Load")) {
                  var6 = Bukkit.getWorlds().iterator();

                  while(var6.hasNext()) {
                     World worlds = (World)var6.next();
                     Iterator var3 = worlds.getLivingEntities().iterator();

                     while(var3.hasNext()) {
                        LivingEntity liv = (LivingEntity)var3.next();
                        Main.this.spawnListener.applyDayChanges(liv);
                     }
                  }
               }

               Main.this.loaded = true;
            }

            DateManager.getInstance().tick();
            Main.this.registerListeners();
            if (Bukkit.getOnlinePlayers().size() >= 1 && Main.SPEED_RUN_MODE) {
               Main.this.playTime++;
               if (Main.this.playTime % 3600 == 0) {
                  Bukkit.broadcastMessage(Main.tag + Main.format("&cFelicitaciones, han avanzado a la hora número: " + Main.this.getDays()));
                  var6 = Bukkit.getOnlinePlayers().iterator();

                  while(var6.hasNext()) {
                     Player player = (Player)var6.next();
                     player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100.0F, 100.0F);
                  }
               }
            }

            if (Main.this.getDays() >= 60L && !Main.this.getConfig().getBoolean("DontTouch.Event.LifeOrbEnded") && !Main.this.getOrbEvent().isRunning()) {
               if (Main.SPEED_RUN_MODE) {
                  Main.this.orbEvent.setTimeLeft(480);
               }

               Main.this.orbEvent.setRunning(true);
            }

            Main.this.tickEvents();
            Main.this.tickPlayers();
            Main.this.tickWorlds();
         }
      }, 0L, 20L);
   }

   private void tickWorlds() {
      if (this.getDays() >= 40L) {
         Iterator var1 = ((List)Bukkit.getWorlds().stream().filter((world1) -> {
            return world1.getEnvironment() != Environment.THE_END;
         }).collect(Collectors.toList())).iterator();

         label63:
         while(var1.hasNext()) {
            World w = (World)var1.next();
            Iterator var3 = w.getEntitiesByClass(Ravager.class).iterator();

            while(true) {
               Ravager ravager;
               do {
                  if (!var3.hasNext()) {
                     continue label63;
                  }

                  ravager = (Ravager)var3.next();
               } while(!ravager.getPersistentDataContainer().has(new NamespacedKey(instance, "ultra_ravager"), PersistentDataType.BYTE));

               List<Block> b = ravager.getLineOfSight((Set)null, 5);
               Iterator var6 = b.iterator();

               while(var6.hasNext()) {
                  Block block = (Block)var6.next();

                  for(int i = -1; i < 1; ++i) {
                     for(int j = -1; j < 1; ++j) {
                        for(int k = -1; k < 1; ++k) {
                           Block r = block.getRelative(i, j, k);
                           if (r.getType() == Material.NETHERRACK) {
                              r.setType(Material.AIR);
                              r.getWorld().playSound(r.getLocation(), Sound.BLOCK_STONE_BREAK, 2.0F, 1.0F);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   private void tickPlayers() {
      if (Bukkit.getOnlinePlayers().size() >= 1) {
         long segundosbrutos = (long)(this.world.getWeatherDuration() / 20);
         long hours = segundosbrutos % 86400L / 3600L;
         long minutes = segundosbrutos % 3600L / 60L;
         long seconds = segundosbrutos % 60L;
         long days = segundosbrutos / 86400L;
         String time = String.format((days >= 1L ? String.format("%02d día(s) ", days) : "") + "%02d:%02d:%02d", hours, minutes, seconds);
         Iterator var12 = Bukkit.getOnlinePlayers().iterator();

         while(var12.hasNext()) {
            Player player = (Player)var12.next();
            World w = player.getWorld();
            if (this.shulkerEvent.isRunning() && !this.shulkerEvent.getBossBar().getPlayers().contains(player)) {
               this.shulkerEvent.getBossBar().addPlayer(player);
            }

            if (this.orbEvent.isRunning() && !this.orbEvent.getBossBar().getPlayers().contains(player)) {
               this.orbEvent.getBossBar().addPlayer(player);
            }

            NetheriteArmor.setupHealth(player);
            PermaDeathItems.slotBlock(player);
            String actionBar;
            if (SPEED_RUN_MODE) {
               actionBar = "";
               if (this.world.hasStorm()) {
                  actionBar = this.getMessages().getMessageByPlayer("Server-Messages.ActionBarMessage", player.getName()).replace("%tiempo%", time) + " - ";
               }

               actionBar = actionBar + ChatColor.GRAY + "Tiempo total: " + this.formatInterval(this.playTime);
               player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));
            } else if (this.world.hasStorm()) {
               actionBar = this.getMessages().getMessageByPlayer("Server-Messages.ActionBarMessage", player.getName()).replace("%tiempo%", time);
               player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionBar));
            }

            if (player.getWorld().getEnvironment() == Environment.THE_END && this.getDays() >= 30L) {
               if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEDROCK) {
                  player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 200, 9));
               }

               if (player.getWorld().getName().equalsIgnoreCase("pdc_the_beginning") && player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                  player.removePotionEffect(PotionEffectType.INVISIBILITY);
               }
            }

            int pX;
            int pZ;
            int y;
            if (this.getDays() >= 40L && player.getWorld().hasStorm()) {
               Location block = player.getWorld().getHighestBlockAt(player.getLocation().clone()).getLocation();
               int highestY = block.getBlockY();
               if ((double)highestY < player.getLocation().getY()) {
                  pX = this.r.nextInt(10000) + 1;
                  pZ = this.getDays() < 50L ? 1 : 300;
                  if (pX <= pZ) {
                     player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1200, 0));
                  }

                  if (this.getDays() >= 50L && pX == 301) {
                     y = this.r.nextInt(17);
                     y += 3;
                     player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, y * 20, 0));
                  }
               }
            }

            Block up;
            Location l;
            if (this.getDays() >= 50L) {
               int randomEntities;
               Iterator var35;
               if (this.getBeData() != null && this.getBeginningManager() != null) {
                  BeginningDataManager data = this.getBeData();
                  World beginningWorld = this.getBeginningManager().getBeginningWorld();
                  if (!data.killedED()) {
                     Chunk c = beginningWorld.getBlockAt(0, 100, 0).getChunk();

                     for(pZ = 0; pZ < 16; ++pZ) {
                        for(y = beginningWorld.getMaxHeight() - 1; y > 0; --y) {
                           for(randomEntities = 0; randomEntities < 16; ++randomEntities) {
                              up = c.getBlock(pZ, y, randomEntities);
                              if (up.getType() == Material.END_GATEWAY || up.getType() == Material.BEDROCK) {
                                 up.setType(Material.AIR);
                              }
                           }
                        }
                     }

                     if (beginningWorld.getEntitiesByClass(EnderDragon.class).size() >= 1) {
                        var35 = beginningWorld.getEntitiesByClass(EnderDragon.class).iterator();

                        while(var35.hasNext()) {
                           EnderDragon d = (EnderDragon)var35.next();
                           d.remove();
                        }

                        data.setKilledED();
                     }
                  }
               }

               if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
                  PotionEffect e = player.getPotionEffect(PotionEffectType.SLOW_DIGGING);
                  if (e.getDuration() >= 4800 && !this.getDoneEffectPlayers().contains(player)) {
                     int min = 600;
                     player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                     player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, min * 20, 2));
                     this.getDoneEffectPlayers().add(player);
                  }

                  if (e.getDuration() == 4799 && this.getDoneEffectPlayers().contains(player)) {
                     this.getDoneEffectPlayers().remove(player);
                  }
               }

               if (player.getWorld().getEnvironment() == Environment.NETHER && this.getDays() < 60L) {
                  int random = this.r.nextInt(4500) + 1;
                  if (random <= 10 && player.getWorld().getLivingEntities().size() < 110) {
                     l = player.getLocation().clone();
                     ArrayList<Location> spawns = new ArrayList();
                     spawns.add(l.clone().add(10.0D, 25.0D, -5.0D));
                     spawns.add(l.clone().add(5.0D, 25.0D, 5.0D));
                     spawns.add(l.clone().add(-5.0D, 25.0D, 5.0D));
                     var35 = spawns.iterator();

                     label200:
                     while(true) {
                        Location l;
                        do {
                           do {
                              if (!var35.hasNext()) {
                                 break label200;
                              }

                              l = (Location)var35.next();
                           } while(w.getBlockAt(l).getType() != Material.AIR);
                        } while(w.getBlockAt(l.clone().add(0.0D, 1.0D, 0.0D)).getType() != Material.AIR);

                        randomEntities = this.r.nextInt(3) + 1;

                        for(int i = 0; i < randomEntities; ++i) {
                           this.getNmsHandler().spawnNMSEntity("PigZombie", EntityType.valueOf(VersionManager.isRunningNetherUpdate() ? "ZOMBIFIED_PIGLIN" : "PIG_ZOMBIE"), l, SpawnReason.CUSTOM);
                        }
                     }
                  }
               }
            }

            if (this.getDays() >= 60L) {
               if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SOUL_SAND) {
                  player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 600, 2));
               }

               Integer timeForWither = (Integer)player.getPersistentDataContainer().get(new NamespacedKey(this, "wither"), PersistentDataType.INTEGER);
               if (timeForWither == null) {
                  timeForWither = 0;
               }

               player.getPersistentDataContainer().set(new NamespacedKey(this, "wither"), PersistentDataType.INTEGER, timeForWither = timeForWither + 1);
               if (timeForWither % 3600 == 0 && player.getGameMode() == GameMode.SURVIVAL) {
                  Wither wither = (Wither)player.getWorld().spawn(player.getLocation().clone().add(0.0D, 5.0D, 0.0D), Wither.class);

                  try {
                     Object nmsw = wither.getClass().getDeclaredMethod("getHandle").invoke(wither);
                     nmsw.getClass().getDeclaredMethod("r", Integer.TYPE).invoke(nmsw, 100);
                  } catch (Exception var22) {
                  }
               }

               if (this.getConfig().getBoolean("Toggles.Mike-Creeper-Spawn")) {
                  l = player.getLocation().clone();
                  if (this.r.nextInt(30) == 0) {
                     Stream var10000 = player.getNearbyEntities(30.0D, 30.0D, 30.0D).stream().filter((entity) -> {
                        return entity instanceof Creeper;
                     });
                     Creeper.class.getClass();
                     if (((List)var10000.map(Creeper.class::cast).collect(Collectors.toList())).size() < 10) {
                        pX = (this.r.nextBoolean() ? -1 : 1) * this.r.nextInt(15) + 15;
                        pZ = (this.r.nextBoolean() ? -1 : 1) * this.r.nextInt(15) + 15;
                        y = (int)l.getY();
                        Block block = l.getWorld().getBlockAt(l.getBlockX() + pX, y, l.getBlockZ() + pZ);
                        up = block.getRelative(BlockFace.UP);
                        if (block.getType() != Material.AIR && up.getType() == Material.AIR) {
                           this.getFactory().spawnEnderQuantumCreeper(up.getLocation(), (Creeper)null);
                        }
                     }
                  }
               }
            }
         }

      }
   }

   private void tickEvents() {
      Main var10000;
      int res;
      Main var10001;
      int hrs;
      int minAndSec;
      int min;
      int sec;
      String tiempo;
      StringBuilder var7;
      if (this.orbEvent.isRunning()) {
         if (this.orbEvent.getTimeLeft() > 0) {
            this.orbEvent.reduceTime();
            res = this.orbEvent.getTimeLeft();
            hrs = res / 3600;
            minAndSec = res % 3600;
            min = minAndSec / 60;
            sec = minAndSec % 60;
            tiempo = String.format("%02d:%02d:%02d", hrs, min, sec);
            this.orbEvent.getBossBar().setColor(BarColor.values()[this.r.nextInt(BarColor.values().length)]);
            var10001 = instance;
            this.orbEvent.setTitle(format("&6&l" + tiempo + " para obtener el Life Orb"));
         } else {
            var10000 = instance;
            var7 = new StringBuilder();
            var10001 = instance;
            Bukkit.broadcastMessage(format(var7.append(tag).append("&cSe ha acabado el tiempo para obtener el Life Orb, ¡sufrid! ahora tendreís 8 contenedores de vida menos.").toString()));
            this.orbEvent.setRunning(false);
            this.orbEvent.clearPlayers();
            this.orbEvent.setTimeLeft(SPEED_RUN_MODE ? 480 : 28800);
            this.orbEvent.getBossBar().removeAll();
            this.getConfig().set("DontTouch.Event.LifeOrbEnded", true);
            this.saveConfig();
            this.reloadConfig();
         }
      }

      if (this.shulkerEvent.isRunning()) {
         if (this.shulkerEvent.getTimeLeft() > 0) {
            this.shulkerEvent.setTimeLeft(this.shulkerEvent.getTimeLeft() - 1);
            res = this.shulkerEvent.getTimeLeft();
            hrs = res / 3600;
            minAndSec = res % 3600;
            min = minAndSec / 60;
            sec = minAndSec % 60;
            tiempo = String.format("%02d:%02d:%02d", hrs, min, sec);
            var10001 = instance;
            this.shulkerEvent.setTitle(format("&e&lX2 Shulker Shells: &b&n" + tiempo));
         } else {
            var10000 = instance;
            var7 = new StringBuilder();
            var10001 = instance;
            Bukkit.broadcastMessage(format(var7.append(tag).append("&eEl evento de &c&lX2 Shulker Shells &eha acabado.").toString()));
            this.shulkerEvent.setRunning(false);
            this.shulkerEvent.clearPlayers();
            this.shulkerEvent.setTimeLeft(14400);
            this.shulkerEvent.getBossBar().removeAll();
         }
      }

   }

   private void startPlugin() {
      this.messages = new Messages(this);
      this.shulkerEvent = new ShellEvent(this);
      this.orbEvent = new LifeOrbEvent(this);
      this.factory = new MobFactory(this);
      new FileAPI.FileOut(instance, "beginning_portal.schem", "schematics/", true);
      new FileAPI.FileOut(instance, "ytic.schem", "schematics/", true);
      new FileAPI.FileOut(instance, "island1.schem", "schematics/", true);
      new FileAPI.FileOut(instance, "island2.schem", "schematics/", true);
      new FileAPI.FileOut(instance, "island3.schem", "schematics/", true);
      new FileAPI.FileOut(instance, "island4.schem", "schematics/", true);
      new FileAPI.FileOut(instance, "island5.schem", "schematics/", true);
      int HelmetValue = Integer.parseInt((String)Objects.requireNonNull(instance.getConfig().getString("Toggles.Netherite.Helmet")));
      int ChestplateValue = Integer.parseInt((String)Objects.requireNonNull(instance.getConfig().getString("Toggles.Netherite.Chestplate")));
      int LeggingsValue = Integer.parseInt((String)Objects.requireNonNull(instance.getConfig().getString("Toggles.Netherite.Leggings")));
      int BootsValue = Integer.parseInt((String)Objects.requireNonNull(instance.getConfig().getString("Toggles.Netherite.Boots")));
      if (HelmetValue > 100 || HelmetValue < 1) {
         System.out.println("[ERROR] Error al cargar la probabilidad de 'Helmet' en 'config.yml', asegurate de introducir un numero valido del 1 al 100.");
         System.out.println("[ERROR] Ha ocurrido un error al cargar el archivo config.yml, si este error persiste avisanos por discord.");
      }

      if (ChestplateValue > 100 || ChestplateValue < 1) {
         System.out.println("[ERROR] Error al cargar la probabilidad de 'Chestplate' en 'config.yml', asegurate de introducir un numero valido del 1 al 100.");
         System.out.println("[ERROR] Ha ocurrido un error al cargar el archivo config.yml, si este error persiste avisanos por discord.");
      }

      if (LeggingsValue > 100 || LeggingsValue < 1) {
         System.out.println("[ERROR] Error al cargar la probabilidad de 'Leggings' en 'config.yml', asegurate de introducir un numero valido del 1 al 100.");
         System.out.println("[ERROR] Ha ocurrido un error al cargar el archivo config.yml, si este error persiste avisanos por discord.");
      }

      if (BootsValue > 100 || BootsValue < 1) {
         System.out.println("[ERROR] Error al cargar la probabilidad de 'BootsValue' en 'config.yml', asegurate de introducir un numero valido del 1 al 100.");
         System.out.println("[ERROR] Ha ocurrido un error al cargar el archivo config.yml, si este error persiste avisanos por discord.");
      }

      String compatibleVersion = "&cIncompatible";
      if (VersionManager.getFormatedVersion().equalsIgnoreCase("1.15.x") || VersionManager.getFormatedVersion().equalsIgnoreCase("1.14.x") || VersionManager.getFormatedVersion().equalsIgnoreCase("1.16.x")) {
         compatibleVersion = "&aCompatible";
      }

      String software = "";

      try {
         if (Class.forName("org.spigotmc.SpigotConfig") != null) {
            software = "SpigotMC (Compatible)";
         }
      } catch (ClassNotFoundException var9) {
         software = "Bukkit";
      }

      try {
         if (Class.forName("com.destroystokyo.paper.PaperConfig") != null) {
            software = "PaperMC (Compatible)";
            runningPaperSpigot = true;
         }
      } catch (ClassNotFoundException var8) {
      }

      String worldState = this.setupWorld();
      Bukkit.getConsoleSender().sendMessage(format("&f&m------------------------------------------"));
      Bukkit.getConsoleSender().sendMessage(format("         &c&lPERMADEATH&4&lCORE"));
      Bukkit.getConsoleSender().sendMessage(format("     &7- Versión: &e" + this.getDescription().getVersion()));
      Bukkit.getConsoleSender().sendMessage(format("     &7- Versión del Servidor: &e" + VersionManager.getFormatedVersion()));
      Bukkit.getConsoleSender().sendMessage(format("&f&m------------------------------------------"));
      Bukkit.getConsoleSender().sendMessage(format(" &7>> &e&lINFORME DE ESTADO"));
      Bukkit.getConsoleSender().sendMessage(format("&7> &bEstado de mundos: " + worldState));
      Bukkit.getConsoleSender().sendMessage(format("    &7> &eEnd&7: &8" + this.endWorld.getName()));
      Bukkit.getConsoleSender().sendMessage(format("    &7> &aOverworld&7: &8" + this.world.getName()));
      Bukkit.getConsoleSender().sendMessage(format("&7> &bEstado de Compatibilidad: " + compatibleVersion));
      Bukkit.getConsoleSender().sendMessage(format("&7> &bSoftware: " + software));
      Bukkit.getConsoleSender().sendMessage(format("&7> &b&lCambios:"));
      Bukkit.getConsoleSender().sendMessage(format("&7>   &aDías: &71-60"));
      if (Bukkit.getPluginManager().getPlugin("WorldEdit") == null) {
         Bukkit.getConsoleSender().sendMessage(format("&7> &4&lADVERTENCIA: &7No se ha encontrado el Plugin &7World Edit"));
         Bukkit.getConsoleSender().sendMessage(format("&7> &7Si deseas usar PermadeathCore instala &e&lWorldEdit&7."));
         PDCLog.getInstance().log("No se encontró WorldEdit");
      }

      if (software.contains("Bukkit")) {
         Bukkit.broadcastMessage(format(tag + "&7> &4&lADVERTENCIA&7: &eEl plugin NO es compatible con CraftBukkit, cambia a SpigotMC o PaperSpigot"));
         PDCLog.getInstance().disable("No es compatible con Bukkit.");
         Bukkit.getPluginManager().disablePlugin(this);
      } else {
         (new UpdateChecker(this)).getVersion((version) -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
               Bukkit.getConsoleSender().sendMessage(format("&7> &bEstado de Actualización: &aVersión más reciente."));
            } else {
               Bukkit.getConsoleSender().sendMessage(format("&7> &eNueva versión detectada."));
               Bukkit.getConsoleSender().sendMessage(format("&7> &aDescarga: &7http://permadeathcore.com/"));
            }

         });
         this.registerChanges();
         this.generateOfflinePlayerData();
         PDCLog.getInstance().log("Se ha activado el plugin.");
      }
   }

   protected void registerListeners() {
      String prefix = "&e[PermaDeathCore] &7> ";
      if (!(Boolean)this.registeredDays.get(1)) {
         this.registeredDays.replace(1, true);
         this.getServer().getPluginManager().registerEvents(new AnvilListener(this), this);
      }

      if (DateManager.getInstance().getDays() >= 20L && !(Boolean)this.registeredDays.get(20)) {
         this.registeredDays.replace(20, true);
         this.hostile = new PeaceToHostileManager(this);
         this.getServer().getPluginManager().registerEvents(this.hostile, instance);
         Bukkit.getConsoleSender().sendMessage(format(prefix + "&eSe han registrado los cambios de Mobs pacíficos hostiles."));
      }

      if (DateManager.getInstance().getDays() >= 30L && this.endManager == null && this.endData == null && !(Boolean)this.registeredDays.get(30)) {
         this.registeredDays.replace(30, true);
         this.endManager = new EndManager(instance);
         this.getServer().getPluginManager().registerEvents(this.endManager, instance);
         this.endData = new EndDataManager(instance);
         if (runningPaperSpigot) {
            this.getServer().getPluginManager().registerEvents(new PaperListeners(instance), instance);
            Bukkit.getConsoleSender().sendMessage(format(prefix + "&eSe han registrado cambios especiales para &c&lPaperMC&e."));
         }
      }

      if (DateManager.getInstance().getDays() >= 40L && !(Boolean)this.registeredDays.get(40)) {
         this.registeredDays.replace(40, true);
         if (this.recipes == null) {
            this.recipes = new RecipeManager(this);
         }

         this.recipes.registerRecipes();
         this.getNmsHandler().addMushrooms();
         this.getServer().getPluginManager().registerEvents(new VoidListeners(instance), instance);
         Bukkit.getConsoleSender().sendMessage(format(prefix + "&eSe han registrado cambios para el día &b40"));
         if (Bukkit.getPluginManager().getPlugin("WorldEdit") == null) {
            Bukkit.broadcastMessage(format(prefix + "&4&lNo se pudo registrar TheBeginning ya que no se ha encontrado el plugin &7WorldEdit"));
            Bukkit.broadcastMessage(format(prefix + "&7Si necesitas soporte entra a este discord: &ehttps://discord.gg/peM8c6y"));
            return;
         }

         this.beData = new BeginningDataManager(this);
         this.begginingManager = new BeginningManager(this);
         Bukkit.getConsoleSender().sendMessage(format(prefix + "&eSe han registrado cambios de TheBeginning"));
      }

      if (DateManager.getInstance().getDays() >= 50L && !(Boolean)this.registeredDays.get(50)) {
         if (this.recipes == null) {
            this.recipes = new RecipeManager(this);
            this.recipes.registerRecipes();
         }

         Bukkit.getConsoleSender().sendMessage(format(prefix + "&eSe han registrado cambios para el día &b50"));
         this.recipes.registerD50Recipes();
         this.registeredDays.replace(50, true);
      }

      if (DateManager.getInstance().getDays() >= 60L && !(Boolean)this.registeredDays.get(60)) {
         if (this.recipes == null) {
            this.recipes = new RecipeManager(this);
            this.recipes.registerRecipes();
            this.recipes.registerD50Recipes();
         }

         this.recipes.registerD60Recipes();
         Bukkit.getConsoleSender().sendMessage(format(prefix + "&eSe han registrado cambios para el día &b60"));
         this.registeredDays.replace(60, true);
      }

   }

   protected void registerChanges() {
      if (!this.alreadyRegisteredChanges) {
         this.alreadyRegisteredChanges = true;
      }
   }

   public void generateOfflinePlayerData() {
      OfflinePlayer[] var1 = Bukkit.getOfflinePlayers();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         OfflinePlayer off = var1[var3];
         if (off == null) {
            return;
         }

         PlayerDataManager manager = new PlayerDataManager(off.getName(), this);
         manager.generateDayData();
      }

   }

   protected String setupWorld() {
      Iterator var1;
      World w;
      if (Bukkit.getWorld((String)Objects.requireNonNull(instance.getConfig().getString("Worlds.MainWorld"))) == null) {
         var1 = Bukkit.getWorlds().iterator();

         while(var1.hasNext()) {
            w = (World)var1.next();
            if (w.getEnvironment() == Environment.NORMAL) {
               this.world = w;
               break;
            }
         }

         System.out.println("[ERROR] Error al cargar el mundo principal, esto hará que los Death Train no se presenten.");
         System.out.println("[ERROR] Tan solo ve a config.yml y establece el mundo principal en la opción: MainWorld");
         System.out.println("[INFO] El plugin utilizará el mundo " + this.world.getName() + " como mundo principal.");
         System.out.println("[INFO] Si deseas utilizar otro mundo, configura en el archivo config.yml.");
      } else {
         this.world = Bukkit.getWorld((String)Objects.requireNonNull(instance.getConfig().getString("Worlds.MainWorld")));
      }

      if (Bukkit.getWorld((String)Objects.requireNonNull(instance.getConfig().getString("Worlds.EndWorld"))) == null) {
         System.out.println("[ERROR] Error al cargar el mundo del end, esto hará que el end no funcione como debe.");
         System.out.println("[ERROR] Tan solo ve a config.yml y establece el mundo del end en la opción: EndWorld");
         var1 = Bukkit.getWorlds().iterator();

         while(var1.hasNext()) {
            w = (World)var1.next();
            if (w.getEnvironment() == Environment.THE_END) {
               this.endWorld = this.world;
               System.out.println("[INFO] El plugin utilizará el mundo " + w.getName() + " como mundo del End.");
               break;
            }
         }
      } else {
         this.endWorld = Bukkit.getWorld((String)Objects.requireNonNull(instance.getConfig().getString("Worlds.EndWorld")));
      }

      var1 = Bukkit.getWorlds().iterator();

      while(var1.hasNext()) {
         w = (World)var1.next();
         if ((Boolean)Objects.requireNonNull(this.getConfig().getBoolean("Toggles.Doble-Mob-Cap")) && this.getDays() >= 10L) {
            Bukkit.getConsoleSender().sendMessage(tag + "&eDoblando la mob-cap en todos los mundos.");
            w.setMonsterSpawnLimit(140);
         }

         if (isRunningPaperSpigot()) {
            try {
               Object nmsW = w.getClass().getDeclaredMethod("getHandle").invoke(w);
               Field f = nmsW.getClass().getDeclaredField("paperConfig");
               f.setAccessible(true);
               Object paperConfig = f.get(nmsW);
               Field creepers = paperConfig.getClass().getDeclaredField("disableCreeperLingeringEffect");
               creepers.setAccessible(true);
               creepers.set(paperConfig, true);
               Bukkit.getConsoleSender().sendMessage(tag + "&eDeshabilitando Creeper-Lingering-Effect...");
            } catch (Exception var7) {
            }
         }
      }

      return "&aOverworld: &b" + this.world.getName() + " &eEnd: &b" + this.endWorld.getName();
   }

   protected void reload(CommandSender sender) {
      this.setupConfig();
      this.reloadConfig();
      this.messages.reloadFiles();
      DateManager.getInstance().reloadDate();
      this.setupWorld();
      Main var10001 = instance;
      sender.sendMessage(format("&aSe ha recargado el archivo de configuración y los mensajes."));
      var10001 = instance;
      sender.sendMessage(format("&eAlgunos cambios pueden requerir un reinicio para funcionar correctamente."));
      sender.sendMessage(format("&c&lNota importante: &7Algunos cambios pueden requerir un reinicio y la fecha puede no ser exacta."));
      tag = format(this.getConfig().contains("Prefix") ? this.getConfig().getString("Prefix") : "&c&lPERMADEATH&4&lCORE &7➤ &f");
      PDCLog.getInstance().log("Se ha recargado el plugin");
      DiscordManager.getInstance();
   }

   protected void setupListeners() {
      this.getServer().getPluginManager().registerEvents(this, this);
      this.spawnListener = new SpawnListener(this);
      this.getServer().getPluginManager().registerEvents(this.spawnListener, instance);
      this.getServer().getPluginManager().registerEvents(new SkeletonClasses(instance), instance);
      this.getServer().getPluginManager().registerEvents(new PlayerEvents(), instance);
      this.getServer().getPluginManager().registerEvents(new BlockEvents(), instance);
      this.getServer().getPluginManager().registerEvents(new EntityEvents(), instance);
      this.getServer().getPluginManager().registerEvents(new TotemConsumeEvent(), instance);
      this.getServer().getPluginManager().registerEvents(new RaidEvents(), instance);
      this.getServer().getPluginManager().registerEvents(new WorldEvents(), instance);
      this.registeredDays.put(1, false);
      this.registeredDays.put(20, false);
      this.registeredDays.put(30, false);
      this.registeredDays.put(40, false);
      this.registeredDays.put(50, false);
      this.registeredDays.put(60, false);
   }

   protected void setupConsoleFilter() {
      try {
         Class.forName("org.apache.logging.log4j.core.filter.AbstractFilter");
         Logger logger = (Logger)LogManager.getRootLogger();
         logger.addFilter(new Log4JFilter());
      } catch (NoClassDefFoundError | ClassNotFoundException var4) {
         new Log4JFilter();
         Filter f = (Filter)(new Log4JFilter());
         Bukkit.getLogger().setFilter(f);
         java.util.logging.Logger.getLogger("Minecraft").setFilter(f);
      }

   }

   protected void setupCommands() {
      this.getCommand("pdc").setExecutor(new PDCCommand(instance));
      this.getCommand("pdc").setTabCompleter(new PDCCommandCompleter());
   }

   protected void setupConfig() {
      File f = new File(this.getDataFolder(), "config.yml");
      FileConfiguration config = YamlConfiguration.loadConfiguration(f);
      FileAPI.UtilFile c = FileAPI.select(instance, f, config);
      c.set("config-version", 2);
      c.set("Prefix", "&c&lPERMADEATH&4&lCORE &7➤ &f");
      c.set("ban-enabled", true);
      c.set("anti-afk-enabled", false);
      c.set("AntiAFK.DaysForBan", 7);
      c.set("Toggles.OptifineItems", false);
      c.set("Toggles.DefaultDeathSoundsEnabled", true);
      c.set("Toggles.Netherite.Helmet", 10);
      c.set("Toggles.Netherite.Chestplate", 10);
      c.set("Toggles.Netherite.Leggings", 10);
      c.set("Toggles.Netherite.Boots", 10);
      c.set("Toggles.End.Mob-Spawn-Limit", 70);
      c.set("Toggles.End.Ender-Ghast-Count", 170);
      c.set("Toggles.End.Ender-Creeper-Count", 20);
      c.set("Toggles.End.Protect-End-Spawn", false);
      c.set("Toggles.End.Protect-Radius", 10);
      c.set("Toggles.End.PermadeathDemon.DisplayName", "&6&lPERMADEATH DEMON");
      c.set("Toggles.End.PermadeathDemon.DisplayNameEnraged", "&6&lENRAGED PERMADEATH DEMON");
      c.set("Toggles.End.PermadeathDemon.Health", 1350);
      c.set("Toggles.End.PermadeathDemon.EnragedHealth", 1350);
      c.set("Toggles.End.PermadeathDemon.Optimizar-TNT", false);
      c.set("Toggles.TheBeginning.YticGenerateChance", 100000);
      c.set("Toggles.Spider-Effect", true);
      c.set("Toggles.OP-Ban", true);
      c.set("Toggles.Doble-Mob-Cap", false);
      c.set("Toggles.Replace-Mobs-On-Chunk-Load", true);
      c.set("Toggles.Quantum-Explosion-Power", 60);
      c.set("Toggles.Mike-Creeper-Spawn", true);
      c.set("Toggles.Optimizar-Mob-Spawns", false);
      c.set("Toggles.Gatos-Supernova.Destruir-Bloques", true);
      c.set("Toggles.Gatos-Supernova.Fuego", true);
      c.set("Toggles.Gatos-Supernova.Explosion-Power", 200);
      c.set("Server-Messages.coords-msg-enable", true);
      c.set("TotemFail.Enable", true);
      c.set("TotemFail.Medalla", "&7¡El jugador %player% ha usado su medalla de superviviente!");
      c.set("TotemFail.ChatMessage", "&7¡El tótem de &c%player% &7ha fallado!");
      c.set("TotemFail.ChatMessageTotems", "&7¡Los tótems de &c%player% &7han fallado!");
      c.set("TotemFail.NotEnoughTotems", "&7¡%player% no tenía suficientes tótems en el inventario!");
      c.set("TotemFail.PlayerUsedTotemMessage", "&7El jugador %player% ha consumido un tótem (Probabilidad: %totem_fail% %porcent% %number%)");
      c.set("TotemFail.PlayerUsedTotemsMessage", "&7El jugador %player% ha consumido {ammount} tótems (Probabilidad: %totem_fail% %porcent% %number%)");
      c.set("Worlds.MainWorld", "world");
      c.set("Worlds.EndWorld", "world_the_end");
      c.set("DontTouch.PlayTime", 0);
      c.save();
      c.load();
   }

   public static boolean isOptifineEnabled() {
      return instance == null ? false : instance.getConfig().getBoolean("Toggles.OptifineItems");
   }

   public MobFactory getFactory() {
      return this.factory;
   }

   public static String format(String texto) {
      return ChatColor.translateAlternateColorCodes('&', texto);
   }

   public Messages getMessages() {
      return this.messages;
   }

   public NMSHandler getNmsHandler() {
      return this.nmsHandler;
   }

   public BeginningManager getBeginningManager() {
      return this.begginingManager;
   }

   public BeginningDataManager getBeData() {
      return this.beData;
   }

   public EndDataManager getEndData() {
      return this.endData;
   }

   public EndTask getTask() {
      return this.task;
   }

   public void setTask(EndTask task) {
      this.task = task;
   }

   public NMSAccesor getNmsAccesor() {
      return this.nmsAccesor;
   }

   public long getDays() {
      return DateManager.getInstance().getDays();
   }

   public static Main getInstance() {
      return instance;
   }

   public PeaceToHostileManager getHostile() {
      return this.hostile;
   }

   public ArrayList<Player> getDoneEffectPlayers() {
      return this.doneEffectPlayers;
   }

   public ShellEvent getShulkerEvent() {
      return this.shulkerEvent;
   }

   public LifeOrbEvent getOrbEvent() {
      return this.orbEvent;
   }

   public static boolean isRunningPaperSpigot() {
      return runningPaperSpigot;
   }

   public CustomBlock getNetheriteBlock() {
      return this.netheriteBlock;
   }

   public boolean isSmallIslandsEnabled() {
      return true;
   }

   public void deathTrainEffects(LivingEntity entity) {
      if (!(entity instanceof Player)) {
         if (this.getDays() >= 25L) {
            int lvl = this.getDays() >= 50L ? 1 : 0;
            entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, lvl));
            entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, lvl));
            entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, lvl));
            if (this.getDays() >= 50L && this.getDays() < 60L) {
               entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
            }
         }

      }
   }

   public SpawnListener getSpawnListener() {
      return this.spawnListener;
   }

   public int getPlayTime() {
      return this.playTime;
   }

   public void setPlayTime(int playTime) {
      this.playTime = playTime;
   }

   public String formatInterval(int segundos) {
      int hrs = segundos / 3600;
      int minAndSec = segundos % 3600;
      int min = minAndSec / 60;
      int sec = minAndSec % 60;
      return String.format("%02d:%02d:%02d", hrs, min, sec);
   }
}
