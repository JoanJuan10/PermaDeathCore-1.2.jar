package com.permadeathcore.Listener.Player;

import com.permadeathcore.Main;
import com.permadeathcore.Discord.DiscordManager;
import com.permadeathcore.Util.Item.InfernalNetherite;
import com.permadeathcore.Util.Item.NetheriteArmor;
import com.permadeathcore.Util.Item.PermaDeathItems;
import com.permadeathcore.Util.Library.HiddenStringUtils;
import com.permadeathcore.Util.Library.ItemBuilder;
import com.permadeathcore.Util.Library.UpdateChecker;
import com.permadeathcore.Util.Manager.Data.EndDataManager;
import com.permadeathcore.Util.Manager.Data.PlayerDataManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import java.util.SplittableRandom;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.BanList.Type;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Rotatable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

public class PlayerEvents implements Listener {
   ArrayList<Player> sleeping = new ArrayList();
   ArrayList<Player> globalSleeping = new ArrayList();
   long stormTicks;
   long stormHours;

   public PlayerEvents() {
      this.loadTicks();
   }

   public void loadTicks() {
      if (Main.getInstance().getDays() <= 24L) {
         this.stormTicks = Main.getInstance().getDays() * 3600L;
         this.stormHours = this.stormTicks / 60L / 60L;
      }

      long define;
      if (Main.getInstance().getDays() >= 25L && Main.getInstance().getDays() < 50L) {
         define = Main.getInstance().getDays() - 24L;
         this.stormTicks = define * 3600L;
         this.stormHours = this.stormTicks / 60L / 60L;
      }

      if (Main.getInstance().getDays() == 50L) {
         this.stormTicks = 1800L;
         this.stormHours = this.stormTicks / 60L / 60L;
      }

      if (Main.getInstance().getDays() > 50L && Main.getInstance().getDays() < 75L) {
         define = Main.getInstance().getDays() - 49L;
         this.stormTicks = define * 3600L / 2L;
         this.stormHours = this.stormTicks / 60L / 60L;
      }

   }

   @EventHandler
   public void onPlayerDeath(PlayerDeathEvent e) {
      final Player p = e.getEntity();
      String victim = p.getName();
      boolean weather = Main.instance.world.hasStorm();
      BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

      Player player;
      for(Iterator var7 = Bukkit.getOnlinePlayers().iterator(); var7.hasNext(); player.playSound(player.getLocation(), "pdc_muerte", Float.MAX_VALUE, 1.0F)) {
         player = (Player)var7.next();
         String msg = Main.getInstance().getMessages().getMessage("DeathMessageChat", player).replace("%player%", victim);
         player.sendMessage(msg);
         String ServerMessageTitle = Main.getInstance().getMessages().getMessage("DeathMessageTitle", player);
         String ServerMessageSubtitle = Main.getInstance().getMessages().getMessage("DeathMessageSubtitle", player);
         player.sendTitle(ServerMessageTitle, ServerMessageSubtitle.replace("%player%", victim), 20, 100, 20);
         if ((Boolean)Objects.requireNonNull(Main.instance.getConfig().getBoolean("Toggles.DefaultDeathSoundsEnabled"))) {
            player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, Float.MAX_VALUE, -0.1F);
         }
      }

      this.loadTicks();
      int stormDuration = Main.instance.world.getWeatherDuration();
      int stormTicksToSeconds = stormDuration / 20;
      long stormIncrement = (long)stormTicksToSeconds + this.stormTicks;
      int intsTicks = (int)this.stormTicks;
      int inc = (int)stormIncrement;
      final boolean doEnableOP = Main.instance.getConfig().getBoolean("Toggles.OP-Ban");
      boolean causingProblems = true;
      if (!doEnableOP && p.hasPermission("permadeathcore.banoverride")) {
         causingProblems = false;
      }

      Main var10000;
      if (causingProblems) {
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:weather thunder");
         if (weather) {
            Main.instance.world.setWeatherDuration(inc * 20);
         } else {
            Main.instance.world.setWeatherDuration(intsTicks * 20);
         }

         if (Main.instance.getDays() >= 25L) {
            Iterator var15 = Bukkit.getWorlds().iterator();

            while(var15.hasNext()) {
               World w = (World)var15.next();
               Iterator var17 = w.getLivingEntities().iterator();

               while(var17.hasNext()) {
                  LivingEntity l = (LivingEntity)var17.next();
                  Main.instance.deathTrainEffects(l);
               }
            }
         }

         if (Main.instance.getDays() >= 50L) {
            if (Main.instance.getBeginningManager() != null) {
               Main.instance.getBeginningManager().closeBeginning();
            }

            var10000 = Main.instance;
            Bukkit.broadcastMessage(Main.format(Main.tag + "&e¡Ha comenzado el modo UHC!"));
            Main.instance.world.setGameRule(GameRule.NATURAL_REGENERATION, false);
         }

         scheduler.scheduleSyncDelayedTask(Main.instance, new Runnable() {
            public void run() {
               PlayerEvents.this.loadTicks();
               if (Main.getInstance().getDays() < 50L) {
                  Iterator var1 = Bukkit.getOnlinePlayers().iterator();

                  while(var1.hasNext()) {
                     Player p = (Player)var1.next();
                     String msg = Main.getInstance().getMessages().getMessage("DeathTrainMessage", p).replace("%tiempo%", String.valueOf(PlayerEvents.this.stormHours));
                     p.sendMessage(msg);
                     if ((Boolean)Objects.requireNonNull(Main.instance.getConfig().getBoolean("Toggles.DefaultDeathSoundsEnabled"))) {
                        p.playSound(p.getLocation(), Sound.ENTITY_SKELETON_HORSE_DEATH, 10.0F, 1.0F);
                     }
                  }

                  Main.getInstance().getMessages().sendConsole(Main.getInstance().getMessages().getMsgForConsole("DeathTrainMessage").replace("%tiempo%", String.valueOf(PlayerEvents.this.stormHours)));
                  DiscordManager.getInstance().onDeathTrain(Main.getInstance().getMessages().getMsgForConsole("DeathTrainMessage").replace("%tiempo%", String.valueOf(PlayerEvents.this.stormHours)));
               } else {
                  long hours = PlayerEvents.this.stormTicks / 60L / 60L;
                  long minutes = PlayerEvents.this.stormTicks / 60L % 60L;
                  String ct = String.valueOf(hours);
                  String path = "DeathTrainMessage";
                  if (minutes == 30L || minutes == 60L) {
                     path = path + "Minutes";
                     if (hours >= 1L) {
                        ct = "" + hours + " horas y " + minutes;
                     } else {
                        ct = String.valueOf(minutes);
                     }
                  }

                  if (minutes == 0L) {
                     ct = String.valueOf(hours);
                  }

                  String time = ct;
                  Iterator var8 = Bukkit.getOnlinePlayers().iterator();

                  while(var8.hasNext()) {
                     Player px = (Player)var8.next();
                     String msgx = Main.getInstance().getMessages().getMessage(path, px).replace("%tiempo%", time);
                     px.sendMessage(msgx);
                     if ((Boolean)Objects.requireNonNull(Main.instance.getConfig().getBoolean("Toggles.DefaultDeathSoundsEnabled"))) {
                        px.playSound(px.getLocation(), Sound.ENTITY_SKELETON_HORSE_DEATH, 10.0F, 1.0F);
                     }
                  }

                  Main.getInstance().getMessages().sendConsole(Main.getInstance().getMessages().getMsgForConsole(path).replace("%tiempo%", time));
                  DiscordManager.getInstance().onDeathTrain(Main.getInstance().getMessages().getMsgForConsole(path).replace("%tiempo%", time));
               }

            }
         }, 100L);
      } else {
         StringBuilder var24 = new StringBuilder();
         Main var10001 = Main.instance;
         var24 = var24.append(Main.tag);
         var10001 = Main.instance;
         Bukkit.broadcastMessage(String.format(var24.append(Main.format("&eEl jugador &b" + p.getName() + " &eno puede dar más horas de tormenta.")).toString()));
      }

      PlayerDataManager man = new PlayerDataManager(e.getEntity().getPlayer().getName(), Main.instance);
      man.setAutoDeathCause(e.getEntity().getPlayer().getLastDamageCause().getCause());
      man.setDeathTime();
      man.setDeathDay();
      man.setDeathCoords(e.getEntity().getPlayer().getLocation());
      DiscordManager.getInstance().banPlayer(p, false);
      String msg;
      if (Main.instance.getConfig().contains("Server-Messages.CustomDeathMessages." + p.getName())) {
         msg = Main.instance.getConfig().getString("Server-Messages.CustomDeathMessages." + p.getName()).replace("%player%", p.getName());
         var10000 = Main.instance;
         Bukkit.broadcastMessage(Main.format(StringUtils.capitalize(msg) + (msg.endsWith(".") ? "" : ".")));
      } else {
         var10000 = Main.instance;
         msg = Main.format(Main.instance.getConfig().getString("Server-Messages.DefaultDeathMessage").replace("%player%", p.getName()));
         var10000 = Main.instance;
         Bukkit.broadcastMessage(Main.format(StringUtils.capitalize(msg) + (msg.endsWith(".") ? "" : ".")));
      }

      Main.getInstance().getMessages().sendConsole(Main.getInstance().getMessages().getMsgForConsole("DeathMessageChat").replace("%player%", victim));
      if (Main.instance.getConfig().getBoolean("Server-Messages.coords-msg-enable")) {
         int Dx = e.getEntity().getPlayer().getLocation().getBlockX();
         int Dy = e.getEntity().getPlayer().getLocation().getBlockY();
         int Dz = e.getEntity().getPlayer().getLocation().getBlockZ();
         Bukkit.broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "X: " + Dx + " || Y: " + Dy + " || Z: " + Dz + ChatColor.RESET);
      }

      p.setGameMode(GameMode.SPECTATOR);
      scheduler.runTaskLater(Main.instance, new Runnable() {
         public void run() {
            boolean isban = doEnableOP ? !p.hasPermission("permadeathcore.banoverride") : true;
            if (Main.instance.getConfig().getBoolean("ban-enabled") && isban) {
               if (p.isOnline()) {
                  ((Player)p).kickPlayer(ChatColor.RED + "Has sido PERMABANEADO");
               }

               Bukkit.getBanList(Type.NAME).addBan(p.getName(), ChatColor.RED + "Has sido PERMABANEADO", (Date)null, "console");
            }

         }
      }, 40L);
      Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
         public void run() {
            if (Main.getInstance().getConfig().getBoolean("Toggles.Player-Skulls")) {
               Location l = p.getEyeLocation().clone();
               if (l.getY() < 3.0D) {
                  l.setY(3.0D);
               }

               Block skullBlock = l.getBlock();
               skullBlock.setType(Material.PLAYER_HEAD);
               Skull skullState = (Skull)skullBlock.getState();
               skullState.setOwningPlayer(p);
               skullState.update();
               Rotatable rotatable = (Rotatable)skullBlock.getBlockData();
               rotatable.setRotation(PlayerEvents.this.getRotation(p));
               skullBlock.setBlockData(rotatable);
               skullBlock.getRelative(BlockFace.DOWN).setType(Material.NETHER_BRICK_FENCE);
               skullBlock.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).setType(Material.BEDROCK);
            }

         }
      }, 10L);
   }

   public BlockFace getRotation(Player player) {
      float rotation = player.getLocation().getYaw();
      if (rotation < 0.0F) {
         rotation = (float)((double)rotation + 360.0D);
      }

      if (0.0F <= rotation && (double)rotation < 22.5D) {
         return BlockFace.NORTH;
      } else if (22.5D <= (double)rotation && (double)rotation < 67.5D) {
         return BlockFace.NORTH_EAST;
      } else if (67.5D <= (double)rotation && (double)rotation < 112.5D) {
         return BlockFace.EAST;
      } else if (112.5D <= (double)rotation && (double)rotation < 157.5D) {
         return BlockFace.SOUTH_EAST;
      } else if (157.5D <= (double)rotation && (double)rotation < 202.5D) {
         return BlockFace.SOUTH;
      } else if (202.5D <= (double)rotation && (double)rotation < 247.5D) {
         return BlockFace.SOUTH_WEST;
      } else if (247.5D <= (double)rotation && (double)rotation < 292.5D) {
         return BlockFace.WEST;
      } else if (292.5D <= (double)rotation && (double)rotation < 337.5D) {
         return BlockFace.NORTH_WEST;
      } else {
         return 337.5D <= (double)rotation && rotation <= 360.0F ? BlockFace.NORTH : BlockFace.WEST;
      }
   }

   @EventHandler
   public void onSleep(final PlayerBedEnterEvent event) {
      Main var10001;
      Player var10;
      if (event.getPlayer().getWorld().getEnvironment() != Environment.NORMAL) {
         var10 = event.getPlayer();
         var10001 = Main.instance;
         var10.sendMessage(Main.format("&cSolo puedes dormir en el Overworld."));
      } else if (event.getBedEnterResult() != BedEnterResult.OK) {
         var10 = event.getPlayer();
         var10001 = Main.instance;
         var10.sendMessage(Main.format("&cNo puedes dormir ahora."));
      } else if (Main.getInstance().getDays() >= 20L) {
         Location playerbed = event.getBed().getLocation().add(0.0D, 1.0D, 0.0D);
         Main.instance.world.playSound(playerbed, Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
         Main.instance.world.spawnParticle(Particle.EXPLOSION_HUGE, playerbed, 1);
         if (Main.getInstance().getDays() >= 50L) {
            if ((new SplittableRandom()).nextInt(100) + 1 <= 10) {
               var10 = event.getPlayer();
               Main.getInstance();
               var10.sendMessage(Main.format(Main.tag + " &aHas restablecido el contador de Phantoms."));
               event.getPlayer().setStatistic(Statistic.TIME_SINCE_REST, 0);
            }
         } else {
            var10 = event.getPlayer();
            Main.getInstance();
            var10.sendMessage(Main.format(Main.tag + " &aHas restablecido el contador de Phantoms."));
            event.getPlayer().setStatistic(Statistic.TIME_SINCE_REST, 0);
         }

         event.setCancelled(true);
      } else {
         final Player player = event.getPlayer();
         long time = Main.instance.world.getTime();
         int neededPlayers = 1;
         if (Main.instance.getDays() >= 10L) {
            neededPlayers = 4;
         }

         if (Bukkit.getOnlinePlayers().size() < neededPlayers) {
            player.sendMessage(Main.format("&cNo puedes dormir porque no hay suficientes personas en línea (" + neededPlayers + ")."));
            event.setCancelled(true);
         } else if (time < 13000L) {
            var10001 = Main.instance;
            player.sendMessage(Main.format("&cSolo puedes dormir de noche."));
            event.setCancelled(true);
         } else {
            if (Main.getInstance().getDays() < 10L && time >= 13000L) {
               final ArrayList<Player> sent = new ArrayList();
               Bukkit.getServer().getScheduler().runTaskLater(Main.instance, new Runnable() {
                  public void run() {
                     event.getPlayer().getWorld().setTime(0L);
                     player.setStatistic(Statistic.TIME_SINCE_REST, 0);
                     if (!sent.contains(player)) {
                        Bukkit.getOnlinePlayers().forEach((p) -> {
                           String msg = Main.getInstance().getMessages().getMessage("Sleep", p).replace("%player%", player.getName());
                           p.sendMessage(msg);
                        });
                        Main.getInstance().getMessages().sendConsole(Main.getInstance().getMessages().getMsgForConsole("Sleep").replace("%player%", player.getName()));
                        sent.add(player);
                        player.damage(0.1D);
                     }

                  }
               }, 60L);
            }

            if (Main.getInstance().getDays() >= 10L && Main.getInstance().getDays() <= 19L && time >= 13000L) {
               this.globalSleeping.add(player);
               Bukkit.getOnlinePlayers().forEach((p) -> {
                  String msg = Main.getInstance().getMessages().getMessage("Sleeping", p).replace("%needed%", String.valueOf(4)).replace("%players%", String.valueOf(this.globalSleeping.size())).replace("%player%", player.getName());
                  p.sendMessage(msg);
               });
               Main.getInstance().getMessages().sendConsole(Main.getInstance().getMessages().getMsgForConsole("Sleeping").replace("%needed%", String.valueOf(4)).replace("%players%", String.valueOf(this.globalSleeping.size())).replace("%player%", player.getName()));
               if (this.globalSleeping.size() >= neededPlayers && this.globalSleeping.size() < Bukkit.getOnlinePlayers().size()) {
                  Bukkit.getServer().getScheduler().runTaskLater(Main.instance, new Runnable() {
                     public void run() {
                        if (PlayerEvents.this.globalSleeping.size() >= 4) {
                           event.getPlayer().getWorld().setTime(0L);
                           Iterator var1 = Bukkit.getOnlinePlayers().iterator();

                           Main var10000;
                           while(var1.hasNext()) {
                              Player all = (Player)var1.next();
                              if (all.isSleeping()) {
                                 all.setStatistic(Statistic.TIME_SINCE_REST, 0);
                                 all.damage(0.1D);
                                 var10000 = Main.instance;
                                 Bukkit.broadcastMessage(Main.format((String)Objects.requireNonNull(Main.instance.getConfig().getString("Server-Messages.Sleep").replace("%player%", all.getName()))));
                              }
                           }

                           var10000 = Main.instance;
                           Bukkit.broadcastMessage(Main.format("&eHan dormido suficientes jugadores (&b4&e)."));
                           PlayerEvents.this.globalSleeping.clear();
                        }

                     }
                  }, 40L);
               }

               if (this.globalSleeping.size() == Bukkit.getOnlinePlayers().size()) {
                  event.getPlayer().getWorld().setTime(0L);
                  Iterator var9 = Bukkit.getOnlinePlayers().iterator();

                  Main var10000;
                  while(var9.hasNext()) {
                     Player all = (Player)var9.next();
                     all.setStatistic(Statistic.TIME_SINCE_REST, 0);
                     all.damage(0.1D);
                     var10000 = Main.instance;
                     Bukkit.broadcastMessage(Main.format((String)Objects.requireNonNull(Main.instance.getConfig().getString("Server-Messages.Sleep").replace("%player%", all.getName()))));
                  }

                  var10000 = Main.instance;
                  Bukkit.broadcastMessage(Main.format("&eHan dormido todos los jugadores."));
                  this.globalSleeping.clear();
                  event.setCancelled(true);
               }
            }

         }
      }
   }

   @EventHandler
   public void onBedLeave(PlayerBedLeaveEvent e) {
      Player p = e.getPlayer();
      if (p.getWorld().getEnvironment() == Environment.NORMAL) {
         if (this.sleeping.contains(p)) {
            this.sleeping.remove(p);
         }

         if (this.globalSleeping.contains(p)) {
            this.globalSleeping.remove(p);
         }

         if (p.getWorld().getTime() < 0L || p.getWorld().getTime() >= 13000L) {
            Main var10001 = Main.instance;
            p.sendMessage(Main.format("&eHas abandonado la cama, ya no contarás para pasar la noche."));
         }
      }
   }

   @EventHandler
   public void onJoin(PlayerJoinEvent e) {
      final Player player = e.getPlayer();
      e.setJoinMessage((String)null);
      Bukkit.getOnlinePlayers().forEach((p) -> {
         String JoinMessage = Main.getInstance().getMessages().getMessage("OnJoin", p).replace("%player%", e.getPlayer().getName());
         p.sendMessage(JoinMessage);
      });
      Main.getInstance().getMessages().sendConsole(Main.getInstance().getMessages().getMsgForConsole("OnJoin").replace("%player%", player.getName()));
      new PlayerDataManager(e.getPlayer().getName(), Main.instance);
      if (Main.instance.getShulkerEvent().isRunning()) {
         Main.instance.getShulkerEvent().addPlayer(e.getPlayer());
      }

      if (Main.instance.getOrbEvent().isRunning()) {
         Main.instance.getOrbEvent().addPlayer(e.getPlayer());
      }

      final boolean sendingTitle = player.hasPlayedBefore() ? (new Random()).nextInt(5) == 0 : true;
      Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
         public void run() {
            if (player != null) {
               if (player.isOnline()) {
                  player.sendMessage(Main.format("&e&m-------------------------------------------"));
                  player.sendMessage(Main.format("        &c&lPERMA&7&lDEATH &4&lCORE"));
                  player.sendMessage(Main.format("&7Servidor InfernalCore (Servidor original del Plugin): discord.gg/InfernalCore"));
                  player.sendMessage(Main.format(" "));
                  player.sendMessage(Main.format("&b&l - Servidor de Discord con soporte del Desarrollador: -"));
                  player.sendMessage(Main.format("&7Forma parte de nuestra comunidad, no solo damos soporte,"));
                  player.sendMessage(Main.format("&7jugamos juegos, nos divertimos y también hay plugins adicionales."));
                  player.sendMessage(Main.format("&7Se ofrece soporte oficial del desarrollador del Plugin."));
                  player.sendMessage(Main.format(" "));
                  player.sendMessage(Main.format("&e&nInvitación a Discord&r&7 (soporte, noticias y proyectos):"));
                  player.sendMessage(Main.format("&cNota: este no es el mismo Discord que InfernalCore."));
                  player.sendMessage(Main.format("&9https://discord.gg/w58wzrcJU8"));
                  player.sendMessage(Main.format("&e&m-------------------------------------------"));
                  if (!Main.isOptifineEnabled()) {
                     player.sendMessage(Main.format("&cRecuerda aceptar los paquetes de Recursos para ver los ítems y texturas personalizadas."));
                  }

                  player.sendMessage(Main.tag + Main.format("&eEjecuta el comando &f&l/pdc &r&epara más información."));
                  if (sendingTitle) {
                     player.sendTitle(Main.format("&c&lPERMA&7&lDEATH &4&lCORE"), Main.format("&7Twitter: &b@SebazCRC"), 1, 100, 1);
                     player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100.0F, 100.0F);
                  }

               }
            }
         }
      }, 300L);
      Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
         public void run() {
            if (player != null) {
               if (player.isOnline()) {
                  if (sendingTitle) {
                     player.sendTitle(Main.format("&c&lPERMA&7&lDEATH &4&lCORE"), Main.format("&7Discord: &9https://discord.gg/8evPbuxPke"), 1, 100, 1);
                     player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100.0F, 100.0F);
                  }

               }
            }
         }
      }, 400L);
      if (!Main.isOptifineEnabled()) {
         player.setResourcePack("https://www.dropbox.com/s/h3v77ga72l9vhpg/PermaDeathCore%20RP%20v1.2.zip?dl=1");
      }

      if (player.isOp()) {
         StringBuilder var10001 = new StringBuilder();
         Main var10002 = Main.instance;
         var10001 = var10001.append(Main.tag);
         var10002 = Main.instance;
         player.sendMessage(var10001.append(Main.format("&eSi tienes algún problema puedes unirte a nuestro discord, enlace con: &9/pdc discord")).toString());
         (new UpdateChecker(Main.getInstance())).getVersion((version) -> {
            if (Main.getInstance().getDescription().getVersion().equalsIgnoreCase(version)) {
               Main.getInstance();
               player.sendMessage(Main.format(Main.tag + "&3Estás utilizando la versión más reciente del Plugin."));
            } else {
               Main.getInstance();
               player.sendMessage(Main.format(Main.tag + "&3Se ha encontrado una nueva versión del Plugin"));
               Main.getInstance();
               player.sendMessage(Main.format(Main.tag + "&eDescarga en: &7http://permadeathcore.com/"));
            }

         });
      }

      if (Main.instance.getDays() >= 50L && Main.instance.getBeginningManager() != null && Main.instance.getBeginningManager().isClosed() && e.getPlayer().getWorld().getName().equalsIgnoreCase("pdc_the_beginning")) {
         e.getPlayer().teleport(Main.instance.world.getSpawnLocation());
      }

      if (Main.instance.getBeginningManager() != null && Main.instance.getBeginningManager().getBeginningWorld() != null) {
         if (Main.instance.getBeginningManager().isClosed() && e.getPlayer().getWorld().getName().equalsIgnoreCase(Main.instance.getBeginningManager().getBeginningWorld().getName())) {
            e.getPlayer().teleport(Main.instance.world.getSpawnLocation());
         }

         if (!Main.instance.getBeData().generatedOverWorldBeginningPortal()) {
            Main.instance.getBeginningManager().generatePortal(true, (Location)null);
         }

         if (!Main.instance.getBeData().generatedBeginningPortal()) {
            Main.instance.getBeginningManager().generatePortal(false, new Location(Main.instance.getBeginningManager().getBeginningWorld(), 50.0D, 140.0D, 50.0D));
            Main.instance.getBeginningManager().getBeginningWorld().setSpawnLocation(new Location(Main.instance.getBeginningManager().getBeginningWorld(), 50.0D, 140.0D, 50.0D));
         }
      }

   }

   @EventHandler
   public void onLeave(PlayerQuitEvent e) {
      e.setQuitMessage((String)null);
      Bukkit.getOnlinePlayers().forEach((px) -> {
         String JoinMessage = Main.getInstance().getMessages().getMessage("OnLeave", px).replace("%player%", e.getPlayer().getName());
         px.sendMessage(JoinMessage);
      });
      Main.getInstance().getMessages().sendConsole(Main.getInstance().getMessages().getMsgForConsole("OnLeave").replace("%player%", e.getPlayer().getName()));
      Main.instance.getShulkerEvent().removePlayer(e.getPlayer());
      Main.instance.getOrbEvent().removePlayer(e.getPlayer());
      Player p = e.getPlayer();
      if (this.sleeping.contains(p)) {
         this.sleeping.remove(p);
      }

      if (this.globalSleeping.contains(p)) {
         this.globalSleeping.remove(p);
      }

   }

   @EventHandler
   public void onPreLogin(AsyncPlayerPreLoginEvent e) {
      if (Main.instance.getConfig().getBoolean("anti-afk-enabled")) {
         if (Main.instance.getConfig().getStringList("AntiAFK.Bypass").contains(e.getName())) {
            return;
         }

         PlayerDataManager dataManager = new PlayerDataManager(e.getName(), Main.instance);
         long actualDay = Main.instance.getDays();
         long lastConection = dataManager.getLastDay();
         if (actualDay < lastConection) {
            dataManager.setLastDay(actualDay);
            return;
         }

         OfflinePlayer off = Bukkit.getOfflinePlayer(e.getName());
         if (off == null) {
            return;
         }

         if (off.isBanned() || !off.isWhitelisted()) {
            return;
         }

         long result = actualDay - lastConection;
         if (result >= (long)Main.instance.getConfig().getInt("AntiAFK.DaysForBan")) {
            Main var10000 = Main.instance;
            String reason = Main.format("&c&lHas sido PERMABANEADO\n&eRazón: AFK\n&7Si crees que es un\n&7error, contacta un\n&7administrador.");
            e.disallow(Result.KICK_BANNED, reason);
            Bukkit.getBanList(Type.NAME).addBan(e.getName(), reason, (Date)null, "console");
            DiscordManager.getInstance().banPlayer(Bukkit.getOfflinePlayer(e.getName()), true);
         } else {
            dataManager.setLastDay(Main.instance.getDays());
         }
      }

   }

   @EventHandler
   public void onAirChange(EntityAirChangeEvent e) {
      if (e.getEntity() instanceof Player && Main.instance.getDays() >= 50L) {
         Player p = (Player)e.getEntity();
         if (p.getRemainingAir() >= e.getAmount()) {
            int speed = Main.instance.getDays() < 60L ? 5 : 10;
            Double damage = Main.instance.getDays() < 60L ? 5.0D : 10.0D;
            if (e.getAmount() >= 20) {
               int seconds = e.getAmount() / 20;
               int remain = seconds / speed;
               int newAmount = remain * 20;
               if (remain <= 0) {
                  int newAmount = 0;
                  e.setAmount(newAmount);
                  Main.instance.getNmsAccesor().drown(p, damage);
               } else {
                  e.setAmount(newAmount);
               }
            }
         }
      }
   }

   @EventHandler
   public void onConsume(final PlayerItemConsumeEvent e) {
      if (Main.instance.getDays() >= 40L && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()) {
         String var10000 = e.getItem().getItemMeta().getDisplayName();
         Main.getInstance();
         Player p;
         short fmin;
         if (var10000.equalsIgnoreCase(Main.format("&6Super Golden Apple +"))) {
            p = e.getPlayer();
            fmin = 300;
            if (!p.hasPotionEffect(PotionEffectType.HEALTH_BOOST)) {
               p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * fmin, 0));
            }
         } else {
            var10000 = e.getItem().getItemMeta().getDisplayName();
            Main.getInstance();
            Main var10001;
            if (var10000.equalsIgnoreCase(Main.format("&6Hyper Golden Apple +"))) {
               Player var8;
               StringBuilder var9;
               Main var10002;
               if (Main.instance.getDays() < 60L) {
                  if (e.getPlayer().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "hyper_one"), PersistentDataType.BYTE)) {
                     var8 = e.getPlayer();
                     var10001 = Main.instance;
                     var9 = new StringBuilder();
                     var10002 = Main.instance;
                     var8.sendMessage(Main.format(var9.append(Main.tag).append("&c¡Ya has comido una Hyper Golden Apple!").toString()));
                     return;
                  }

                  var8 = e.getPlayer();
                  var10001 = Main.instance;
                  var9 = new StringBuilder();
                  var10002 = Main.instance;
                  var8.sendMessage(Main.format(var9.append(Main.tag).append("&a¡Has obtenido contenedores de vida extra!").toString()));
                  e.getPlayer().getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "hyper_one"), PersistentDataType.BYTE, (byte)1);
               } else {
                  boolean doPlayerAteOne = e.getPlayer().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "hyper_one"), PersistentDataType.BYTE);
                  boolean doPlayerAteTwo = e.getPlayer().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "hyper_two"), PersistentDataType.BYTE);
                  if (!doPlayerAteOne) {
                     var8 = e.getPlayer();
                     var10001 = Main.instance;
                     var9 = new StringBuilder();
                     var10002 = Main.instance;
                     var8.sendMessage(Main.format(var9.append(Main.tag).append("&a¡Has obtenido contenedores de vida extra! &e(Hyper Golden Apple 1/2)").toString()));
                     e.getPlayer().getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "hyper_one"), PersistentDataType.BYTE, (byte)1);
                  } else {
                     if (doPlayerAteTwo) {
                        var8 = e.getPlayer();
                        var10001 = Main.instance;
                        var9 = new StringBuilder();
                        var10002 = Main.instance;
                        var8.sendMessage(Main.format(var9.append(Main.tag).append("&c¡Ya has comido una Hyper Golden Apple #2!").toString()));
                        return;
                     }

                     var8 = e.getPlayer();
                     var10001 = Main.instance;
                     var9 = new StringBuilder();
                     var10002 = Main.instance;
                     var8.sendMessage(Main.format(var9.append(Main.tag).append("&a¡Has obtenido contenedores de vida extra! &e(Hyper Golden Apple 2/2)").toString()));
                     e.getPlayer().getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "hyper_two"), PersistentDataType.BYTE, (byte)1);
                  }
               }
            } else {
               var10000 = e.getItem().getItemMeta().getDisplayName();
               var10001 = Main.instance;
               if (var10000.equalsIgnoreCase(Main.format("&6Super Golden Apple +"))) {
                  p = e.getPlayer();
                  fmin = 300;
                  if (!p.hasPotionEffect(PotionEffectType.HEALTH_BOOST)) {
                     p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 20 * fmin, 0));
                  }
               }
            }
         }
      }

      if (Main.getInstance().getDays() >= 50L && e.getItem() != null) {
         if (e.getItem().getType() == Material.MILK_BUCKET && e.getPlayer().hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
            final PotionEffect effect = e.getPlayer().getPotionEffect(PotionEffectType.SLOW_DIGGING);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
               public void run() {
                  e.getPlayer().addPotionEffect(effect);
               }
            }, 10L);
         }

         if (e.getItem().getType() == Material.PUMPKIN_PIE) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 100, 0));
         }

         if (e.getItem().getType() == Material.SPIDER_EYE) {
            Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
               public void run() {
                  e.getPlayer().removePotionEffect(PotionEffectType.POISON);
                  e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, Integer.MAX_VALUE, 0));
               }
            }, 5L);
         }

         if (e.getItem().getType() == Material.PUFFERFISH) {
            Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
               public void run() {
                  e.getPlayer().removePotionEffect(PotionEffectType.CONFUSION);
                  e.getPlayer().removePotionEffect(PotionEffectType.POISON);
                  e.getPlayer().removePotionEffect(PotionEffectType.HUNGER);
                  e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, Integer.MAX_VALUE, 3));
                  e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 2));
                  e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, Integer.MAX_VALUE, 1));
               }
            }, 5L);
         }

         if (e.getItem().getType() == Material.ROTTEN_FLESH) {
            Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
               public void run() {
                  e.getPlayer().removePotionEffect(PotionEffectType.HUNGER);
                  e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 1));
               }
            }, 5L);
         }

         if (e.getItem().getType() == Material.POISONOUS_POTATO) {
            Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
               public void run() {
                  e.getPlayer().removePotionEffect(PotionEffectType.POISON);
                  e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, Integer.MAX_VALUE, 0));
               }
            }, 5L);
         }
      }

      if (Main.getInstance().getDays() >= 60L) {
         ItemStack s = e.getItem();
         if (s != null && s.getType() == Material.PUMPKIN_PIE) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 3));
         }
      }

   }

   @EventHandler
   public void onTeleport(PlayerTeleportEvent e) {
      if (e.getCause() == TeleportCause.ENDER_PEARL && Main.instance.getDays() >= 60L) {
         e.getPlayer().setCooldown(Material.ENDER_PEARL, 120);
      }

   }

   @EventHandler
   public void onWC(PlayerChangedWorldEvent e) {
      if (e.getPlayer().getWorld().getName().equalsIgnoreCase(Main.getInstance().endWorld.getName())) {
         this.createRegenZone(e.getPlayer().getLocation());
      }

   }

   @EventHandler
   public void onChunkPopulate(ChunkPopulateEvent e) {
      if (Main.getInstance().getDays() >= 40L && e.getChunk().getWorld().getName().equalsIgnoreCase(Main.getInstance().endWorld.getName())) {
         Entity[] var2 = e.getChunk().getEntities();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Entity entity = var2[var4];
            if (entity instanceof ItemFrame) {
               ItemFrame frame = (ItemFrame)entity;
               if (frame.getItem() != null && frame.getItem().getType() == Material.ELYTRA) {
                  ItemStack s = (new ItemBuilder(Material.ELYTRA)).setDurability(431).build();
                  frame.setItem(s);
               }
            }
         }
      }

   }

   private void createRegenZone(Location playerZone) {
      EndDataManager ma = Main.getInstance().getEndData();
      if (!ma.getConfig().getBoolean("CreatedRegenZone")) {
         Location added = playerZone.add(-10.0D, 0.0D, 0.0D);
         Location toGenerate = Main.getInstance().endWorld.getHighestBlockAt(added).getLocation();
         if (toGenerate.getY() == -1.0D) {
            toGenerate.setY(playerZone.getY());
         }

         Block centerBlock = Main.getInstance().endWorld.getBlockAt(toGenerate);
         this.generateBlocks(true, toGenerate);
         this.generateBlocks(false, toGenerate);
         centerBlock.getRelative(BlockFace.UP).setType(Material.RED_CARPET);
         centerBlock.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).setType(Material.SEA_LANTERN);
         centerBlock.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).setType(Material.RED_CARPET);
         AreaEffectCloud a = (AreaEffectCloud)Main.getInstance().endWorld.spawnEntity(centerBlock.getRelative(BlockFace.UP).getLocation(), EntityType.AREA_EFFECT_CLOUD);
         a.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0), false);
         a.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0), false);
         a.setDuration(999999);
         a.setParticle(Particle.BLOCK_CRACK, Material.AIR.createBlockData());
         a.setRadius(4.0F);
         ma.getConfig().set("CreatedRegenZone", true);
         ma.getConfig().set("RegenZoneLocation", this.locationToString(a.getLocation()));
         ma.saveFile();
         ma.reloadFile();
         Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(Main.getInstance(), new Runnable() {
            public void run() {
               Iterator var1 = Main.getInstance().endWorld.getEntities().iterator();

               while(true) {
                  Entity ents;
                  do {
                     if (!var1.hasNext()) {
                        return;
                     }

                     ents = (Entity)var1.next();
                  } while(ents.getType() != EntityType.ENDERMAN && ents.getType() != EntityType.CREEPER);

                  Block b = ents.getLocation().getBlock().getRelative(BlockFace.DOWN);
                  int structure = (new Random()).nextInt(4);
                  ArrayList<Block> toChange = new ArrayList();
                  if (structure == 0) {
                     toChange.add(b.getRelative(BlockFace.NORTH));
                     toChange.add(b.getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST));
                     toChange.add(b.getRelative(BlockFace.SOUTH));
                     toChange.add(b.getRelative(BlockFace.SOUTH_EAST));
                     toChange.add(b.getRelative(BlockFace.SOUTH_WEST));
                     toChange.add(b.getRelative(BlockFace.SOUTH_EAST).getRelative(BlockFace.SOUTH));
                     toChange.add(b.getRelative(BlockFace.SOUTH_EAST).getRelative(BlockFace.NORTH));
                     toChange.add(b.getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH));
                  } else if (structure == 1) {
                     toChange.add(b.getRelative(BlockFace.NORTH));
                     toChange.add(b.getRelative(BlockFace.NORTH_EAST));
                     toChange.add(b);
                  } else if (structure == 2) {
                     toChange.add(b.getRelative(BlockFace.SOUTH));
                     toChange.add(b.getRelative(BlockFace.SOUTH_WEST));
                     toChange.add(b);
                  } else if (structure == 3) {
                     toChange.add(b.getRelative(BlockFace.NORTH));
                     toChange.add(b.getRelative(BlockFace.NORTH_EAST));
                     toChange.add(b);
                     toChange.add(b.getRelative(BlockFace.SOUTH));
                     toChange.add(b.getRelative(BlockFace.EAST));
                  } else if (structure == 4) {
                     toChange.add(b.getRelative(BlockFace.SOUTH));
                     toChange.add(b.getRelative(BlockFace.NORTH_WEST));
                     toChange.add(b);
                     toChange.add(b.getRelative(BlockFace.NORTH));
                     toChange.add(b.getRelative(BlockFace.WEST));
                  }

                  Iterator var6 = toChange.iterator();

                  while(var6.hasNext()) {
                     Block all = (Block)var6.next();
                     Location used = Main.getInstance().endWorld.getHighestBlockAt(new Location(Main.getInstance().endWorld, (double)all.getX(), (double)all.getY(), (double)all.getZ())).getLocation();
                     Block now = Main.getInstance().endWorld.getBlockAt(used);
                     if (now.getType() == Material.END_STONE) {
                        now.setType(Material.END_STONE_BRICKS);
                     }
                  }
               }
            }
         }, 100L);
      }

   }

   private void generateBlocks(boolean b, Location toGenerate) {
      ArrayList blocks;
      Block centerBlock;
      Block all;
      if (b) {
         blocks = new ArrayList();
         centerBlock = Main.getInstance().endWorld.getBlockAt(toGenerate);
         blocks.add(centerBlock);
         blocks.add(Main.getInstance().endWorld.getBlockAt(toGenerate).getRelative(BlockFace.EAST));
         blocks.add(Main.getInstance().endWorld.getBlockAt(toGenerate).getRelative(BlockFace.WEST));
         blocks.add(centerBlock.getRelative(BlockFace.NORTH));
         blocks.add(centerBlock.getRelative(BlockFace.NORTH_WEST));
         blocks.add(centerBlock.getRelative(BlockFace.NORTH_EAST));
         blocks.add(centerBlock.getRelative(BlockFace.SOUTH));
         blocks.add(centerBlock.getRelative(BlockFace.SOUTH_WEST));
         blocks.add(centerBlock.getRelative(BlockFace.SOUTH_EAST));
         Iterator var5 = blocks.iterator();

         while(var5.hasNext()) {
            all = (Block)var5.next();
            all.setType(Material.RED_WOOL);
         }
      } else {
         blocks = new ArrayList();
         centerBlock = Main.getInstance().endWorld.getBlockAt(toGenerate);
         Block corner1 = centerBlock.getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST);
         blocks.add(corner1);
         blocks.add(corner1.getRelative(BlockFace.WEST));
         blocks.add(corner1.getRelative(BlockFace.WEST).getRelative(BlockFace.WEST));
         blocks.add(corner1.getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST));
         blocks.add(corner1.getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST));
         blocks.add(corner1.getRelative(BlockFace.SOUTH));
         blocks.add(corner1.getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH));
         blocks.add(corner1.getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH));
         all = corner1.getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH).getRelative(BlockFace.SOUTH);
         blocks.add(all);
         blocks.add(all.getRelative(BlockFace.WEST));
         blocks.add(all.getRelative(BlockFace.WEST).getRelative(BlockFace.WEST));
         blocks.add(all.getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST));
         Block finalC = all.getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST);
         blocks.add(finalC);
         blocks.add(finalC.getRelative(BlockFace.NORTH));
         blocks.add(finalC.getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH));
         blocks.add(finalC.getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH).getRelative(BlockFace.NORTH));
         Iterator var8 = blocks.iterator();

         while(var8.hasNext()) {
            Block all = (Block)var8.next();
            all.setType(Material.RED_GLAZED_TERRACOTTA);
         }
      }

   }

   private String locationToString(Location loc) {
      return loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getWorld().getName();
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void restrictCrafting(PrepareItemCraftEvent e) {
      PlayerEvents.CraftPrepareManager manager = new PlayerEvents.CraftPrepareManager(e);
      manager.runCheckForLifeOrb();
      manager.runCheckForBeginningRelic();
      manager.runCheckForInfernalPiece();
      manager.runCheckForInfernalElytra();
      manager.runCheckForGaps();
      if (e.getInventory().getResult() != null && e.getInventory().getResult().getType().name().toLowerCase().contains("leather_") && !e.getInventory().getResult().getItemMeta().isUnbreakable() && Main.instance.getDays() >= 25L) {
         e.getInventory().setResult(new ItemStack(Material.AIR));
      }

   }

   @EventHandler
   public void onCraftItem(CraftItemEvent e) {
      CraftingInventory inventory = e.getInventory();
      if (inventory.getResult() != null) {
         ItemStack res = e.getRecipe().getResult();
         if (e.isCancelled() || e.getResult() != org.bukkit.event.Event.Result.ALLOW) {
            return;
         }

         if (res.hasItemMeta()) {
            if (PermaDeathItems.isEndRelic(res)) {
               ItemMeta meta = res.getItemMeta();
               meta.setLore(Arrays.asList(HiddenStringUtils.encodeString("{" + UUID.randomUUID().toString() + ": 0}")));
               res.setItemMeta(meta);
               e.setCurrentItem(res);
               return;
            }

            Player p;
            if ((res.isSimilar(PermaDeathItems.createBeginningRelic()) || res.isSimilar(PermaDeathItems.createLifeOrb())) && e.getWhoClicked() instanceof Player) {
               e.getInventory().setMatrix(this.clearMatrix());
               p = (Player)e.getWhoClicked();
               p.setItemOnCursor(res);
            }

            label29: {
               String var10000;
               Main var10001;
               if (res.getItemMeta().hasDisplayName()) {
                  var10000 = res.getItemMeta().getDisplayName();
                  var10001 = Main.instance;
                  if (var10000.contains(Main.format("&6Hyper Golden Apple +"))) {
                     break label29;
                  }
               }

               var10000 = res.getItemMeta().getDisplayName();
               var10001 = Main.instance;
               if (!var10000.contains(Main.format("&6Super Golden Apple +"))) {
                  return;
               }
            }

            if (e.getWhoClicked() instanceof Player) {
               e.getInventory().setMatrix(this.clearMatrix());
               p = (Player)e.getWhoClicked();
               p.setItemOnCursor(res);
            }
         }
      }

   }

   public ItemStack[] clearMatrix() {
      return new ItemStack[]{new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)};
   }

   private class CraftPrepareManager {
      private PrepareItemCraftEvent e;
      private ItemStack result;

      public CraftPrepareManager(PrepareItemCraftEvent e) {
         this.e = e;
         this.result = e.getInventory().getResult();
      }

      public void runCheckForBeginningRelic() {
         if (this.result != null) {
            if (this.result.isSimilar(PermaDeathItems.createBeginningRelic())) {
               int diamondBlocks = 0;
               int r = 0;
               ItemStack[] var3 = this.e.getInventory().getMatrix();
               int var4 = var3.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                  ItemStack s = var3[var5];
                  if (s != null) {
                     if (s.getType() == Material.DIAMOND_BLOCK && s.getAmount() >= 32) {
                        ++diamondBlocks;
                     }

                     if (PermaDeathItems.isEndRelic(s)) {
                        ++r;
                     }
                  }
               }

               if (diamondBlocks < 4 || r < 1) {
                  this.e.getInventory().setResult((ItemStack)null);
               }

               if (diamondBlocks >= 4 && r >= 1) {
                  this.e.getInventory().setResult(PermaDeathItems.createBeginningRelic());
               }
            }

         }
      }

      public void runCheckForInfernalPiece() {
         if (this.result != null) {
            if (NetheriteArmor.isInfernalPiece(this.result)) {
               if (this.result.getType() == Material.ELYTRA) {
                  return;
               }

               int diamondsFound = 0;
               boolean foundPiece = false;
               ItemStack[] var3 = this.e.getInventory().getMatrix();
               int var4 = var3.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                  ItemStack item = var3[var5];
                  if (item != null && item.hasItemMeta()) {
                     ItemMeta meta = item.getItemMeta();
                     if (item.getType() == Material.DIAMOND && meta.isUnbreakable() && ChatColor.stripColor(item.getItemMeta().getDisplayName()).contains("Infernal")) {
                        ++diamondsFound;
                     }

                     if (NetheriteArmor.isNetheritePiece(item)) {
                        foundPiece = true;
                     }
                  }
               }

               if (diamondsFound < 5 || !foundPiece) {
                  this.e.getInventory().setResult((ItemStack)null);
               }

               if (diamondsFound >= 4 && foundPiece) {
                  Material mat = this.result.getType();
                  if (mat == Material.LEATHER_HELMET) {
                     this.e.getInventory().setResult(InfernalNetherite.craftNetheriteHelmet());
                  }

                  if (mat == Material.LEATHER_CHESTPLATE) {
                     this.e.getInventory().setResult(InfernalNetherite.craftNetheriteChest());
                  }

                  if (mat == Material.LEATHER_LEGGINGS) {
                     this.e.getInventory().setResult(InfernalNetherite.craftNetheriteLegs());
                  }

                  if (mat == Material.LEATHER_BOOTS) {
                     this.e.getInventory().setResult(InfernalNetherite.craftNetheriteBoots());
                  }
               }
            }

         }
      }

      public void runCheckForInfernalElytra() {
         if (this.result != null) {
            if (this.result.getType() == Material.ELYTRA) {
               int diamondsFound = 0;
               ItemStack[] var2 = this.e.getInventory().getMatrix();
               int var3 = var2.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                  ItemStack item = var2[var4];
                  if (item != null && item.hasItemMeta()) {
                     ItemMeta meta = item.getItemMeta();
                     if (item.getType() == Material.DIAMOND && meta.isUnbreakable() && ChatColor.stripColor(item.getItemMeta().getDisplayName()).contains("Infernal")) {
                        ++diamondsFound;
                     }
                  }
               }

               if (diamondsFound >= 8) {
                  this.e.getInventory().setResult(PermaDeathItems.crearElytraInfernal());
               } else {
                  this.e.getInventory().setResult((ItemStack)null);
               }
            }

         }
      }

      public void runCheckForGaps() {
         if (this.result != null) {
            String var10000 = this.result.getItemMeta().getDisplayName();
            Main var10001 = Main.instance;
            int found;
            ItemStack[] var2;
            int var3;
            int var4;
            ItemStack item;
            CraftingInventory var10;
            ItemBuilder var11;
            Main var10002;
            if (var10000.startsWith(Main.format("&6Hyper"))) {
               if (Main.instance.getDays() < 60L) {
                  found = 0;
                  var2 = this.e.getInventory().getMatrix();
                  var3 = var2.length;

                  for(var4 = 0; var4 < var3; ++var4) {
                     item = var2[var4];
                     if (item != null && item.getType() == Material.GOLD_BLOCK && item.getAmount() >= 8) {
                        ++found;
                     }
                  }

                  if (found >= 8) {
                     var10 = this.e.getInventory();
                     var11 = new ItemBuilder(Material.GOLDEN_APPLE, 1);
                     var10002 = Main.instance;
                     var10.setResult(var11.setDisplayName(Main.format("&6Hyper Golden Apple +")).addEnchant(Enchantment.ARROW_INFINITE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build());
                  } else {
                     this.e.getInventory().setResult((ItemStack)null);
                  }
               } else {
                  found = 0;
                  boolean enoughGaps = false;
                  ItemStack[] var8 = this.e.getInventory().getMatrix();
                  var4 = var8.length;

                  for(int var9 = 0; var9 < var4; ++var9) {
                     ItemStack itemx = var8[var9];
                     if (itemx != null) {
                        if (itemx.getType() == Material.GOLD_BLOCK && itemx.getAmount() >= 8) {
                           ++found;
                        }

                        if (itemx.getType() == Material.GOLDEN_APPLE && itemx.getAmount() == 64) {
                           enoughGaps = true;
                        }
                     }
                  }

                  if (found >= 8 && enoughGaps) {
                     var10 = this.e.getInventory();
                     var11 = new ItemBuilder(Material.GOLDEN_APPLE, 1);
                     var10002 = Main.instance;
                     var10.setResult(var11.setDisplayName(Main.format("&6Hyper Golden Apple +")).addEnchant(Enchantment.ARROW_INFINITE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build());
                  } else {
                     this.e.getInventory().setResult((ItemStack)null);
                  }
               }
            }

            var10000 = this.result.getItemMeta().getDisplayName();
            var10001 = Main.instance;
            if (var10000.startsWith(Main.format("&6Super"))) {
               found = 0;
               var2 = this.e.getInventory().getMatrix();
               var3 = var2.length;

               for(var4 = 0; var4 < var3; ++var4) {
                  item = var2[var4];
                  if (item != null && item.getType() == Material.GOLD_INGOT && item.getAmount() >= 8) {
                     ++found;
                  }
               }

               if (found < 8) {
                  this.e.getInventory().setResult((ItemStack)null);
                  return;
               }

               if (found >= 8) {
                  var10 = this.e.getInventory();
                  var11 = new ItemBuilder(Material.GOLDEN_APPLE, 1);
                  var10002 = Main.instance;
                  var10.setResult(var11.setDisplayName(Main.format("&6Super Golden Apple +")).addEnchant(Enchantment.ARROW_INFINITE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build());
               }
            }

         }
      }

      public void runCheckForLifeOrb() {
         if (this.result != null) {
            if (this.result.isSimilar(PermaDeathItems.createLifeOrb())) {
               if (Main.instance.getOrbEvent().isRunning()) {
                  int items = 0;
                  ItemStack[] var2 = this.e.getInventory().getMatrix();
                  int var3 = var2.length;

                  for(int var4 = 0; var4 < var3; ++var4) {
                     ItemStack s = var2[var4];
                     if (s != null) {
                        if (s.getType() == Material.HEART_OF_THE_SEA) {
                           ++items;
                        } else if (s.getAmount() >= 64) {
                           ++items;
                        }
                     }
                  }

                  if (items < 9) {
                     this.e.getInventory().setResult((ItemStack)null);
                  }

                  if (items >= 9) {
                     this.e.getInventory().setResult(PermaDeathItems.createLifeOrb());
                  }

               }
            }
         }
      }
   }
}
