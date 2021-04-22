package com.permadeathcore;

import com.permadeathcore.Discord.DiscordManager;
import com.permadeathcore.NMS.VersionManager;
import com.permadeathcore.Util.Configurations.Language;
import com.permadeathcore.Util.Item.InfernalNetherite;
import com.permadeathcore.Util.Item.NetheriteArmor;
import com.permadeathcore.Util.Item.PermaDeathItems;
import com.permadeathcore.Util.Library.ItemBuilder;
import com.permadeathcore.Util.Manager.Data.DateManager;
import com.permadeathcore.Util.Manager.Data.PlayerDataManager;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PDCCommand implements CommandExecutor {
   private Main instance;
   public static int dias;

   public PDCCommand(Main instance) {
      this.instance = instance;
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (command.getName().equalsIgnoreCase("pdc")) {
         World world = this.instance.world;
         World endWorld = this.instance.endWorld;
         CommandSender player = sender;
         if (args.length == 0) {
            this.sendHelp(sender);
            return false;
         }

         int x;
         StringBuilder var10001;
         Main var10002;
         if (args[0].equalsIgnoreCase("awake")) {
            if (sender instanceof Player) {
               x = ((Player)sender).getStatistic(Statistic.TIME_SINCE_REST) / 20;
               long hours = (long)(x % 86400 / 3600);
               long minutes = (long)(x % 3600 / 60);
               long seconds = (long)(x % 60);
               long days = (long)(x / 86400);
               String awake = (days >= 1L ? days + " días " : "") + String.format("%02d:%02d:%02d", hours, minutes, seconds);
               var10001 = new StringBuilder();
               var10002 = this.instance;
               sender.sendMessage(var10001.append(Main.tag).append(ChatColor.RED).append("Tiempo despierto: ").append(ChatColor.GRAY).append(awake).toString());
            }
         } else {
            int y;
            int hours;
            int z;
            if (args[0].equalsIgnoreCase("duracion")) {
               boolean weather = world.hasStorm();
               hours = world.getWeatherDuration();
               y = hours / 20;
               z = y;
               if (weather) {
                  LocalTime timeOfDay;
                  String time;
                  if (y < 86400) {
                     timeOfDay = LocalTime.ofSecondOfDay((long)y);
                     time = timeOfDay.toString();
                     var10001 = new StringBuilder();
                     var10002 = this.instance;
                     sender.sendMessage(var10001.append(Main.tag).append(ChatColor.RED).append("Quedan ").append(ChatColor.GRAY).append(time).toString());
                  } else {
                     for(dias = 0; z > 86400; ++dias) {
                        z -= 86400;
                     }

                     timeOfDay = LocalTime.ofSecondOfDay((long)z);
                     time = timeOfDay.toString();
                     var10001 = new StringBuilder();
                     var10002 = this.instance;
                     sender.sendMessage(var10001.append(Main.tag).append(ChatColor.RED).append("Quedan ").append(ChatColor.GRAY).append(dias).append("d ").append(time).toString());
                  }
               } else {
                  var10001 = new StringBuilder();
                  var10002 = this.instance;
                  sender.sendMessage(var10001.append(Main.tag).append(ChatColor.RED).append("¡No hay ninguna tormenta en marcha!").toString());
               }
            } else {
               Player p;
               String s;
               Main var44;
               if (args[0].equalsIgnoreCase("idioma")) {
                  if (!(sender instanceof Player)) {
                     return false;
                  }

                  p = (Player)sender;
                  if (args.length == 1) {
                     var44 = this.instance;
                     sender.sendMessage(Main.format("&ePor favor ingresa un idioma."));
                     var44 = this.instance;
                     p.sendMessage(Main.format("&7Ejemplo: &b/pdc idioma es"));
                     var44 = this.instance;
                     p.sendMessage(Main.format("&eArgumentos válidos: &b<es, en>"));
                     return false;
                  }

                  s = args[1];
                  PlayerDataManager data = new PlayerDataManager(p.getName(), this.instance);
                  if (s.equalsIgnoreCase("es")) {
                     if (data.getLanguage() == Language.SPANISH) {
                        var44 = this.instance;
                        p.sendMessage(Main.format("&c¡Ya estás usando el idioma español!"));
                        return false;
                     }

                     data.setLanguage(Language.SPANISH);
                     var44 = this.instance;
                     p.sendMessage(Main.format("&eHas cambiado tu idioma a: &bEspañol"));
                  } else if (s.equalsIgnoreCase("en")) {
                     if (data.getLanguage() == Language.ENGLISH) {
                        var44 = this.instance;
                        p.sendMessage(Main.format("&cYou're already using the English Language"));
                        return false;
                     }

                     data.setLanguage(Language.ENGLISH);
                     var44 = this.instance;
                     p.sendMessage(Main.format("&eYou've changed your language sucessfully to: &bEnglish"));
                  } else {
                     var44 = this.instance;
                     p.sendMessage(Main.format("&cNo has ingresado un idioma válido."));
                  }
               } else if (args[0].equalsIgnoreCase("cambiarDia")) {
                  if (!sender.hasPermission("permadeathcore.cambiardia")) {
                     var44 = this.instance;
                     sender.sendMessage(Main.format("&cNo tienes permiso para hacer esto"));
                     return false;
                  }

                  if (Main.SPEED_RUN_MODE) {
                     sender.sendMessage(Main.format("&cNo puedes hacer esto por que el modo SpeedRun está activo."));
                     return false;
                  }

                  if (args.length <= 1) {
                     var44 = this.instance;
                     sender.sendMessage(Main.format("&cNecesitas agregar un día"));
                     var44 = this.instance;
                     sender.sendMessage(Main.format("&eEjemplo: &7/pdc cambiarDia <día>"));
                     return false;
                  }

                  DateManager.getInstance().setDay(sender, args[1]);
               } else if (args[0].equalsIgnoreCase("reload")) {
                  if (sender.hasPermission("permadeathcore.reload")) {
                     this.instance.reload(sender);
                  } else {
                     var44 = this.instance;
                     sender.sendMessage(Main.format("&cNo tienes permiso para utilizar este comando."));
                  }
               } else {
                  int pX;
                  int pZ;
                  if (args[0].equalsIgnoreCase("debug")) {
                     if (!(sender instanceof Player)) {
                        var44 = this.instance;
                        sender.sendMessage(Main.format("&cNecesitas usar este comando en el juego."));
                        return false;
                     }

                     p = (Player)sender;
                     if (!p.hasPermission("permadeathcore.admin")) {
                        var44 = this.instance;
                        sender.sendMessage(Main.format("&cNo tienes permisos para utilizar este comando (&f&npermadeathcore.admin&c)."));
                        return false;
                     }

                     if (args.length == 1) {
                        var44 = this.instance;
                        var10001 = new StringBuilder();
                        var10002 = this.instance;
                        sender.sendMessage(Main.format(var10001.append(Main.tag).append("&eEste comando te servirá en nuestro soporte si tienes problemas").toString()));
                        var44 = this.instance;
                        sender.sendMessage(Main.format("&b&nSub comandos:"));
                        var44 = this.instance;
                        sender.sendMessage(Main.format("&7/pdc debug info&f&l- &eInformación importante acerca del plugin, suele usarse en soporte."));
                        var44 = this.instance;
                        sender.sendMessage(Main.format("&7/pdc debug generate_beginning&f&l- &eSi tienes problemas con The Beginning puedes generarlo manualmente."));
                        return false;
                     }

                     if (args[1].equalsIgnoreCase("info")) {
                        var44 = this.instance;
                        var10001 = new StringBuilder();
                        var10002 = this.instance;
                        sender.sendMessage(Main.format(var10001.append(Main.tag).append("&6&lMostrando información debug para soporte").toString()));
                        var44 = this.instance;
                        sender.sendMessage(Main.format(" "));
                        var44 = this.instance;
                        sender.sendMessage(Main.format("&fDía actual: &a" + DateManager.getInstance().getDays()));
                        var44 = this.instance;
                        sender.sendMessage(Main.format("&fWorldEdit: " + (Bukkit.getPluginManager().getPlugin("WorldEdit") == null ? "&cNo instalado" : "&aInstalado, &eversión: &b" + Bukkit.getPluginManager().getPlugin("WorldEdit").getDescription().getVersion())));
                        var44 = this.instance;
                        sender.sendMessage(Main.format("&fVersión del Plugin: &a" + this.instance.getDescription().getVersion()));
                        var44 = this.instance;
                        sender.sendMessage(Main.format("&fVersión del Servidor: &a" + VersionManager.getFormatedVersion() + " &b(" + VersionManager.getVersion() + ")"));
                        var44 = this.instance;
                        sender.sendMessage(Main.format("&fMundo de overworld: &a" + this.instance.world.getName()));
                        var44 = this.instance;
                        sender.sendMessage(Main.format("&fMundo de end: &a" + this.instance.endWorld.getName()));
                        var44 = this.instance;
                        sender.sendMessage(Main.format(""));
                        var44 = this.instance;
                        sender.sendMessage(Main.format("&eEsta información es brindada en nuestro discord, &f&nhttps://discord.gg/w58wzrcJU8"));
                     } else if (!args[1].equalsIgnoreCase("generate_beginning")) {
                        if (args[1].equalsIgnoreCase("toggle")) {
                           Main.DEBUG = !Main.DEBUG;
                           sender.sendMessage("Debug cambiado a " + Main.DEBUG);
                        } else if (args[1].equalsIgnoreCase("health")) {
                           sender.sendMessage("Vida máxima: " + NetheriteArmor.getAvaibleMaxHealth(p));
                        } else if (args[1].equalsIgnoreCase("events")) {
                           sender.sendMessage("Eventos:");
                           sender.sendMessage("Shulker shell: " + (Main.getInstance().getShulkerEvent().isRunning() ? "corriendo, tiempo: " + Main.getInstance().getShulkerEvent().getTimeLeft() + ", bossbar:" + Main.getInstance().getShulkerEvent().getBossBar().getTitle() : "no corriendo"));
                           sender.sendMessage("Life Orb: " + (Main.getInstance().getOrbEvent().isRunning() ? "corriendo, tiempo: " + Main.getInstance().getOrbEvent().getTimeLeft() + ", bossbar:" + Main.getInstance().getOrbEvent().getBossBar().getTitle() : "no corriendo"));
                        } else {
                           boolean b;
                           if (args[1].equalsIgnoreCase("hasOrb")) {
                              b = this.instance.getOrbEvent().isRunning() ? true : NetheriteArmor.checkForOrb(p);
                              p.sendMessage("Orb: " + b);
                           } else if (args[1].equalsIgnoreCase("hyper")) {
                              b = p.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "hyper_one"), PersistentDataType.BYTE);
                              boolean doPlayerAteTwo = p.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "hyper_two"), PersistentDataType.BYTE);
                              p.sendMessage("Gap 1: " + b + " | Gap2:" + doPlayerAteTwo);
                           } else if (args[1].equalsIgnoreCase("removegaps")) {
                              p.getPersistentDataContainer().remove(new NamespacedKey(Main.getInstance(), "hyper_one"));
                              p.getPersistentDataContainer().remove(new NamespacedKey(Main.getInstance(), "hyper_two"));
                           } else if (args[1].equalsIgnoreCase("showHealthSkeleton")) {
                              hours = Integer.parseInt(args[2]);
                              p.sendMessage("Actual health: " + (hours < 50 ? 25 : (hours < 60 ? 40 : 110)));
                           } else if (args[1].equalsIgnoreCase("withertime")) {
                              p.sendMessage("tiempo: " + p.getPersistentDataContainer().get(new NamespacedKey(this.instance, "wither"), PersistentDataType.INTEGER));
                           } else if (args[1].equalsIgnoreCase("testtotems")) {
                              p.sendMessage("Totems sin offhand debug: " + p.getInventory().all(Material.TOTEM_OF_UNDYING).size());
                           } else if (args[1].equalsIgnoreCase("testtotemsb")) {
                              hours = p.getInventory().all(Material.TOTEM_OF_UNDYING).size();
                              if (p.getInventory().getItemInOffHand() != null && p.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {
                                 ++hours;
                              }

                              p.sendMessage("Totems con offhand debug: " + hours);
                           } else if (args[1].equalsIgnoreCase("testlingering")) {
                              p.sendMessage("Lingering: " + Main.DISABLED_LINGERING);
                           } else if (args[1].equalsIgnoreCase("summonske")) {
                              WitherSkeleton skeleton = (WitherSkeleton)p.getWorld().spawn(p.getLocation().clone(), WitherSkeleton.class);
                              skeleton.getEquipment().setItemInMainHand((new ItemBuilder(Material.BOW)).addEnchant(Enchantment.ARROW_DAMAGE, 32765).build());
                              skeleton.getEquipment().setItemInMainHandDropChance(0.0F);
                              skeleton.setRemoveWhenFarAway(false);
                              skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                              skeleton.setCustomName("&6Ultra Esqueleto Definitivo");
                              this.instance.getNmsAccesor().setMaxHealth(skeleton, 400.0D, true);
                           } else if (args[1].equalsIgnoreCase("testwither")) {
                              p.getPersistentDataContainer().set(new NamespacedKey(this.instance, "wither"), PersistentDataType.INTEGER, 3595);
                           } else if (args[1].equalsIgnoreCase("spawncreeper")) {
                              Location l = p.getLocation().clone();

                              for(y = (int)l.getY(); y < l.getWorld().getMaxHeight() - 1 && l.getWorld().getBlockAt((int)l.getX(), y, (int)l.getZ()).getType() != Material.AIR; ++y) {
                              }

                              Random r = new Random();
                              pX = (r.nextBoolean() ? -1 : 1) * r.nextInt(19);
                              pZ = (r.nextBoolean() ? -1 : 1) * r.nextInt(19);
                              if (y == l.getWorld().getMaxHeight() - 1) {
                                 y = l.getWorld().getHighestBlockYAt(pX, pZ);
                              }

                              Location f = new Location(l.getWorld(), l.getX() + (double)pX, (double)y, l.getZ() + (double)pZ);
                              if (f.getBlock().getType() == Material.AIR && f.clone().add(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.AIR) {
                                 this.instance.getFactory().spawnEnderQuantumCreeper(f, (Creeper)null);
                                 p.sendMessage("x: " + f.getBlockX() + " y: " + f.getBlockY() + " z: " + f.getBlockZ());
                              }
                           } else if (args[1].equalsIgnoreCase("addtimespeedrun")) {
                              this.instance.setPlayTime(this.instance.getPlayTime() + Integer.valueOf(args[2]));
                           } else if (args[1].equalsIgnoreCase("muerte")) {
                              b = Boolean.parseBoolean(args[3]);
                              DiscordManager.getInstance().banPlayer(Bukkit.getOfflinePlayer(args[2]), b);
                           } else {
                              var44 = this.instance;
                              sender.sendMessage(Main.format("&c¡No existe ese sub-comando!"));
                           }
                        }
                     }
                  } else {
                     String action;
                     String medalla;
                     if (args[0].equalsIgnoreCase("mensaje")) {
                        if (!(sender instanceof Player)) {
                           return false;
                        }

                        if (args.length == 1) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&cDebes escribir un mensaje, ejemplo: /ic mensaje He muerto"));
                           if (this.instance.getConfig().contains("Server-Messages.CustomDeathMessages." + sender.getName())) {
                              var44 = this.instance;
                              sender.sendMessage(Main.format("&eTu mensaje de muerte actual es: &7" + this.instance.getConfig().getString("Server-Messages.CustomDeathMessages." + sender.getName())));
                           } else {
                              var44 = this.instance;
                              sender.sendMessage(Main.format("&eTu mensaje de muerte actual es: &7" + this.instance.getConfig().getString("Server-Messages.DefaultDeathMessage")));
                           }

                           return false;
                        }

                        action = "";

                        for(hours = 0; hours < args.length; ++hours) {
                           if (!args[hours].equalsIgnoreCase(args[0])) {
                              medalla = args[hours];
                              action = action + " " + medalla;
                           }
                        }

                        if (action.contains("&")) {
                           sender.sendMessage(ChatColor.RED + "No se admite el uso de " + ChatColor.GOLD + "&");
                           return false;
                        }

                        this.instance.getConfig().set("Server-Messages.CustomDeathMessages." + sender.getName(), "&7" + action);
                        this.instance.saveConfig();
                        this.instance.reloadConfig();
                        if (sender instanceof Player) {
                           ((Player)sender).playSound(((Player)sender).getLocation(), Sound.ENTITY_BLAZE_DEATH, 10.0F, -5.0F);
                        }

                        var44 = this.instance;
                        sender.sendMessage(Main.format("&eHas cambiado tu mensaje de muerte a: &7" + action));
                     } else if (args[0].equalsIgnoreCase("dias")) {
                        if (this.instance.getDays() < 1L) {
                           var10001 = new StringBuilder();
                           var10002 = this.instance;
                           sender.sendMessage(var10001.append(Main.tag).append(ChatColor.DARK_RED).append("[ERROR] Se ha producido un error al cargar el dia, config.yml mal configurado.").toString());
                        } else if (Main.SPEED_RUN_MODE) {
                           var10001 = new StringBuilder();
                           var10002 = this.instance;
                           sender.sendMessage(var10001.append(Main.tag).append(ChatColor.RED).append("Estamos en la hora: ").append(ChatColor.GRAY).append(this.instance.getDays()).toString());
                        } else {
                           var10001 = new StringBuilder();
                           var10002 = this.instance;
                           sender.sendMessage(var10001.append(Main.tag).append(ChatColor.RED).append("Estamos en el día: ").append(ChatColor.GRAY).append(this.instance.getDays()).toString());
                        }
                     } else if (args[0].equalsIgnoreCase("info")) {
                        var10001 = new StringBuilder();
                        var10002 = this.instance;
                        sender.sendMessage(var10001.append(Main.tag).append(ChatColor.RED).append("Version Info:").toString());
                        sender.sendMessage(ChatColor.GRAY + "- Nombre: " + ChatColor.GREEN + "PermaDeathCore.jar");
                        sender.sendMessage(ChatColor.GRAY + "- Versión: " + ChatColor.GREEN + "PermaDeathCore v" + this.instance.getDescription().getVersion());
                        sender.sendMessage(ChatColor.GRAY + "- Dificultades: " + ChatColor.GREEN + "Soportado de día 1 a día 60");
                        sender.sendMessage(ChatColor.GRAY + "- Autor: " + ChatColor.GREEN + "Equipo de InfernalCore (Desarrollador principal: SebazCRC)");
                     } else if (args[0].equalsIgnoreCase("discord")) {
                        var10001 = new StringBuilder();
                        var10002 = this.instance;
                        sender.sendMessage(var10001.append(Main.tag).append(ChatColor.BLUE).append("https://discord.gg/w58wzrcJU8 | https://discord.gg/infernalcore").toString());
                     } else if (args[0].equalsIgnoreCase("cambios")) {
                        sender.sendMessage(Main.format("&eEste plugin contiene &c&lTODOS &r&elos cambios de PermaDeath."));
                        sender.sendMessage(Main.format("&eMás información aquí:"));
                        sender.sendMessage(Main.format("&b> &f&lhttps://twitter.com/permadeathsmp"));
                        sender.sendMessage(Main.format("&b> &f&lhttps://permadeath.fandom.com/es/wiki/Cambios_de_dificultad"));
                     } else if (args[0].equalsIgnoreCase("beginning")) {
                        if (!sender.hasPermission("permadeathcore.admin")) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&cNo tienes permiso para ejecutar este comando."));
                           return false;
                        }

                        if (args.length == 1) {
                           var44 = this.instance;
                           var10001 = new StringBuilder();
                           var10002 = this.instance;
                           sender.sendMessage(Main.format(var10001.append(Main.tag).append("&cLista de comandos para The Beginning").toString()));
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&7/pdc beginning bendicion <jugador> &f&l- &cOtorga la bendición de The Beginning a un jugador."));
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&7/pdc beginning maldicion <jugador> &f&l- &cOtorga la maldición de The Beginning a un jugador."));
                           return false;
                        }

                        if (args.length == 2) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&cEscribe el nombre de un jugador."));
                           return false;
                        }

                        p = Bukkit.getPlayer(args[2]);
                        if (p == null) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&c¡No hemos podido encontrar a ese jugador!"));
                           return false;
                        }

                        if (args[1].equalsIgnoreCase("bendicion")) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&aSe ha otorgado la bendición de The Beginning a &b" + p.getName()));
                           Bukkit.broadcastMessage(Main.format(Main.tag + "&d&lEnhorabuena " + p.getName() + " has recibido la bendición del comienzo por entrar primero a The Beginning. Suerte."));
                           p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 864000, 1));
                        }

                        if (args[1].equalsIgnoreCase("maldicion")) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&aSe ha otorgado la maldición de The Beginning a &b" + p.getName()));
                           Bukkit.broadcastMessage(Main.format(Main.tag + "&d&l" + p.getName() + ", ¡Desgracia! has recibido la maldición de The Beginning por entrar de último."));
                           Bukkit.broadcastMessage(Main.format("&d&l¡Sufre y muere por lento! NO puedes usar cubos de leche dentro de Permadeath por 12 horas o serás PERMABANEADO."));
                           p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 864000, 0));
                           p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 864000, 0));
                        }
                     } else if (args[0].equalsIgnoreCase("speedrun")) {
                        if (!sender.hasPermission("permadeathcore.admin")) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&cNo tienes permiso para ejecutar este comando."));
                           return false;
                        }

                        if (args.length == 1) {
                           var44 = this.instance;
                           var10001 = new StringBuilder();
                           var10002 = this.instance;
                           sender.sendMessage(Main.format(var10001.append(Main.tag).append("&cLista de comandos para el modo SpeedRun").toString()));
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&7/pdc speedrun toggle &f&l- &cActiva o desactiva el modo SpeedRun."));
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&7/pdc speedrun tiempo &f&l- &cObtén el tiempo de juego total."));
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&7/pdc speedrun reset &f&l- &cReinicia el tiempo."));
                           return false;
                        }

                        if (args[1].equalsIgnoreCase("toggle")) {
                           var44 = this.instance;
                           var10001 = (new StringBuilder()).append(Main.tag);
                           var10002 = this.instance;
                           sender.sendMessage(Main.format(var10001.append(Main.SPEED_RUN_MODE ? "&cHas desactivado el modo SpeedRun" : "&aHas activado el modo SpeedRun").toString()));
                           Main.SPEED_RUN_MODE = !Main.SPEED_RUN_MODE;
                        } else if (args[1].equalsIgnoreCase("tiempo")) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format(Main.tag + "&eEl tiempo de juego actual es de: &b" + this.instance.formatInterval(this.instance.getPlayTime())));
                        } else if (args[1].equalsIgnoreCase("reset")) {
                           sender.sendMessage(Main.format(Main.tag + "&aHas reiniciado el tiempo del modo SpeedRun."));
                           this.instance.setPlayTime(0);
                        } else {
                           var44 = this.instance;
                           var10001 = new StringBuilder();
                           var10002 = this.instance;
                           sender.sendMessage(Main.format(var10001.append(Main.tag).append("&cLista de comandos para el modo SpeedRun").toString()));
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&7/pdc speedrun toggle &f&l- &cActiva o desactiva el modo SpeedRun."));
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&7/pdc speedrun tiempo &f&l- &cObtén el tiempo de juego total."));
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&7/pdc speedrun reset &f&l- &cReinicia el tiempo."));
                        }
                     } else if (args[0].equalsIgnoreCase("event")) {
                        if (!sender.hasPermission("permadeathcore.event")) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&cNo tienes permiso para ejecutar este comando."));
                           return false;
                        }

                        if (args.length == 1) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&cPor favor introduce un evento, ejemplo: &e/pdc event shulkershell"));
                           return false;
                        }

                        Iterator var26;
                        Player p;
                        if (args[1].equalsIgnoreCase("shulkershell")) {
                           if (this.instance.getShulkerEvent().isRunning()) {
                              var44 = this.instance;
                              sender.sendMessage(Main.format("&cEse evento ya está en ejecución."));
                              return false;
                           }

                           this.instance.getShulkerEvent().setRunning(true);
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&aSe ha iniciado el evento correctamente."));
                           var26 = Bukkit.getOnlinePlayers().iterator();

                           while(var26.hasNext()) {
                              p = (Player)var26.next();
                              this.instance.getShulkerEvent().addPlayer(p);
                           }
                        } else if (args[1].equalsIgnoreCase("lifeorb")) {
                           if (this.instance.getOrbEvent().isRunning()) {
                              var44 = this.instance;
                              sender.sendMessage(Main.format("&cEse evento ya está en ejecución."));
                              return false;
                           }

                           if (this.instance.getDays() < 60L) {
                              var44 = this.instance;
                              sender.sendMessage(Main.format("&cEste evento solo puede ser iniciado en días superiores a 60."));
                              return false;
                           }

                           this.instance.getOrbEvent().setRunning(true);
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&aSe ha iniciado el evento correctamente."));
                           var26 = Bukkit.getOnlinePlayers().iterator();

                           while(var26.hasNext()) {
                              p = (Player)var26.next();
                              this.instance.getOrbEvent().addPlayer(p);
                           }
                        } else {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&cNo hemos podido encontrar ese evento."));
                        }
                     } else if (args[0].equalsIgnoreCase("locate")) {
                        if (!sender.hasPermission("permadeathcore.locate")) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&cNo tienes permiso para ejecutar este comando."));
                           return false;
                        }

                        if (args.length == 1) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&eDebes introducir una palabra clave."));
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&eEjemplo: &7/pdc locate beginning"));
                           return false;
                        }

                        if (!args[1].equalsIgnoreCase("beginning")) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&cDebes introducir una palabra clave correcta."));
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&eEjemplo: &7beginning"));
                           return false;
                        }

                        if (this.instance.getDays() < 40L) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&c&lERROR&7: &eNo existe el portal a The Beginning, por que estamos en el día &b" + this.instance.getDays()));
                           return false;
                        }

                        if (this.instance.getBeData() == null) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&c&lERROR&7: &eNo pudimos encontrar The Beginning, por favor reinicia el servidor."));
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&bPasos para generar la dimensión:"));
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&e1. Debería generarse cuando un jugador entra en el día indicado (40)"));
                           return false;
                        }

                        if (!(sender instanceof Player)) {
                           if (!this.instance.getBeData().generatedOverWorldBeginningPortal()) {
                              var44 = this.instance;
                              sender.sendMessage(Main.format("&c&lERROR&7: &eNo se ha generado el portal a The Beginning aún, reinicia el servidor."));
                              return false;
                           }

                           x = (int)this.instance.getBeData().getOverWorldPortal().getX();
                           hours = (int)this.instance.getBeData().getOverWorldPortal().getY();
                           y = (int)this.instance.getBeData().getOverWorldPortal().getZ();
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&eCoordenadas del portal a The Beginning (overworld) &b" + x + " " + hours + " " + y));
                        } else {
                           p = (Player)sender;
                           if (p.getWorld().getName().equalsIgnoreCase(this.instance.world.getName())) {
                              if (!this.instance.getBeData().generatedOverWorldBeginningPortal()) {
                                 var44 = this.instance;
                                 sender.sendMessage(Main.format("&c&lERROR&7: &eNo se ha generado el portal a The Beginning aún, reinicia el servidor."));
                                 return false;
                              }

                              hours = (int)this.instance.getBeData().getOverWorldPortal().getX();
                              y = (int)this.instance.getBeData().getOverWorldPortal().getY();
                              z = (int)this.instance.getBeData().getOverWorldPortal().getZ();
                              var44 = this.instance;
                              sender.sendMessage(Main.format("&eCoordenadas del portal a The Beginning (overworld) &b" + hours + " " + y + " " + z));
                           } else if (p.getWorld().getName().equalsIgnoreCase("pdc_the_beginning")) {
                              if (!this.instance.getBeData().generatedBeginningPortal()) {
                                 var44 = this.instance;
                                 sender.sendMessage(Main.format("&c&lERROR&7: &eNo se ha generado el portal a The Beginning aún, reinicia el servidor."));
                                 return false;
                              }

                              hours = (int)this.instance.getBeData().getBeginningPortal().getX();
                              y = (int)this.instance.getBeData().getBeginningPortal().getY();
                              z = (int)this.instance.getBeData().getBeginningPortal().getZ();
                              var44 = this.instance;
                              sender.sendMessage(Main.format("&eCoordenadas del portal a The Beginning (dimensión) &b" + hours + " " + y + " " + z));
                           } else {
                              var44 = this.instance;
                              sender.sendMessage(Main.format("&c&lERROR&7: &eEste comando no puede ser ejecutado en tu mundo actual."));
                              var44 = this.instance;
                              sender.sendMessage(Main.format("&eRecuerda escribir correctamente el nombre de los mundos en config.yml"));
                           }
                        }
                     } else if (args[0].equalsIgnoreCase("give")) {
                        if (!(sender instanceof Player)) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&cNecesitas ser un jugador."));
                           return false;
                        }

                        p = (Player)sender;
                        if (!p.hasPermission("permadeathcore.give")) {
                           var44 = this.instance;
                           p.sendMessage(Main.format("&cNo tienes permisos."));
                           return false;
                        }

                        if (args.length == 1) {
                           var44 = this.instance;
                           p.sendMessage(Main.format("&ePor favor introduce el ítem deseado"));
                           var44 = this.instance;
                           p.sendMessage(Main.format("&eEjemplos: &7medalla - netheriteArmor - infernalArmor - infernalBlock - netheriteTools - lifeOrb - endRelic - beginningRelic"));
                           return false;
                        }

                        s = args[1];
                        if (s.toLowerCase().equalsIgnoreCase("netheritearmor")) {
                           p.getInventory().addItem(new ItemStack[]{NetheriteArmor.craftNetheriteHelmet()});
                           p.getInventory().addItem(new ItemStack[]{NetheriteArmor.craftNetheriteChest()});
                           p.getInventory().addItem(new ItemStack[]{NetheriteArmor.craftNetheriteLegs()});
                           p.getInventory().addItem(new ItemStack[]{NetheriteArmor.craftNetheriteBoots()});
                           var44 = this.instance;
                           p.sendMessage(Main.format("&eHas recibido la armadura de Netherite (comprueba no tener el inventario lleno)"));
                        } else if (s.toLowerCase().equalsIgnoreCase("medalla")) {
                           Main var10000 = this.instance;
                           medalla = Main.format("&4&l[&c&l☠&4&l] &e&ki &r&6&lMedalla de Superviviente &e&ki &r&4&l[&c&l☠&4&l]");
                           p.getInventory().addItem(new ItemStack[]{(new ItemBuilder(Material.TOTEM_OF_UNDYING)).setUnbrekeable(true).addItemFlag(ItemFlag.HIDE_UNBREAKABLE).setDisplayName(medalla).build()});
                           var44 = this.instance;
                           p.sendMessage(Main.format("&eHas recibido la medalla de superviviente (comprueba no tener el inventario lleno)"));
                        } else if (s.toLowerCase().equalsIgnoreCase("infernalarmor")) {
                           p.getInventory().addItem(new ItemStack[]{InfernalNetherite.craftNetheriteHelmet()});
                           p.getInventory().addItem(new ItemStack[]{InfernalNetherite.craftNetheriteChest()});
                           p.getInventory().addItem(new ItemStack[]{InfernalNetherite.craftNetheriteLegs()});
                           p.getInventory().addItem(new ItemStack[]{InfernalNetherite.craftNetheriteBoots()});
                           var44 = this.instance;
                           p.sendMessage(Main.format("&eHas recibido la armadura de Netherite Infernal (comprueba no tener el inventario lleno)"));
                        } else if (s.toLowerCase().equalsIgnoreCase("netheritetools")) {
                           p.getInventory().addItem(new ItemStack[]{PermaDeathItems.createNetheritePickaxe()});
                           p.getInventory().addItem(new ItemStack[]{PermaDeathItems.createNetheriteSword()});
                           p.getInventory().addItem(new ItemStack[]{PermaDeathItems.createNetheriteAxe()});
                           p.getInventory().addItem(new ItemStack[]{PermaDeathItems.createNetheriteShovel()});
                           p.getInventory().addItem(new ItemStack[]{PermaDeathItems.createNetheriteHoe()});
                           var44 = this.instance;
                           p.sendMessage(Main.format("&eHas recibido las herramientas de Netherite (comprueba no tener el inventario lleno)"));
                        } else if (s.toLowerCase().equalsIgnoreCase("infernalblock")) {
                           p.getInventory().addItem(new ItemStack[]{PermaDeathItems.crearInfernalNetherite()});
                           var44 = this.instance;
                           p.sendMessage(Main.format("&eHas recibido el Bloque de Netherite Infernal (comprueba no tener el inventario lleno)"));
                        } else if (s.toLowerCase().equalsIgnoreCase("lifeorb")) {
                           p.getInventory().addItem(new ItemStack[]{PermaDeathItems.createLifeOrb()});
                           var44 = this.instance;
                           p.sendMessage(Main.format("&eHas recibido el Orbe de Vida (comprueba no tener el inventario lleno)"));
                        } else if (s.toLowerCase().equalsIgnoreCase("endrelic")) {
                           p.getInventory().addItem(new ItemStack[]{PermaDeathItems.crearReliquia()});
                           var44 = this.instance;
                           p.sendMessage(Main.format("&eHas recibido la Reliquia del Fin (comprueba no tener el inventario lleno)"));
                        } else if (s.toLowerCase().equalsIgnoreCase("beginningrelic")) {
                           p.getInventory().addItem(new ItemStack[]{PermaDeathItems.createLifeOrb()});
                           var44 = this.instance;
                           p.sendMessage(Main.format("&eHas recibido la Reliquia del Comienzo (comprueba no tener el inventario lleno)"));
                        } else {
                           var44 = this.instance;
                           p.sendMessage(Main.format("&ePor favor introduce el ítem deseado"));
                           var44 = this.instance;
                           p.sendMessage(Main.format("&eEjemplos: &7medalla - netheriteArmor - infernalArmor - infernalBlock - netheriteTools - lifeOrb - endRelic - beginningRelic"));
                        }
                     } else if (args[0].equalsIgnoreCase("afk")) {
                        if (!sender.hasPermission("permadeathcore.admin")) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&cNo tienes permisos para ejecutar este comando."));
                           return false;
                        }

                        if (args.length == 1) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&cLista de comandos disponibles para el sistema Anti-AFK"));
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&7/pdc afk unban <jugador> &f&l- &cRevoca un baneo por AFK."));
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&7/pdc afk bypass <add/remove> <jugador> &f&l- &cAgrega o elimina un jugador a la lista de personas inmunes."));
                           return false;
                        }

                        if (args[1].equalsIgnoreCase("unban")) {
                           if (args.length == 2) {
                              var44 = this.instance;
                              sender.sendMessage(Main.format("&cPor favor, ingresa a un jugador."));
                              return false;
                           }

                           action = args[2];
                           if (Bukkit.getOfflinePlayer(action) == null) {
                              var44 = this.instance;
                              sender.sendMessage(Main.format("&cJugador no encontrado"));
                              return false;
                           }

                           Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pardon" + action);
                           PlayerDataManager data = new PlayerDataManager(action, this.instance);
                           data.setLastDay(this.instance.getDays());
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&aAhora el jugador &e" + action + " &apodrá volver a jugar."));
                        } else if (args[1].equalsIgnoreCase("bypass")) {
                           if (args.length <= 3) {
                              var44 = this.instance;
                              sender.sendMessage(Main.format("&ePor favor, ingresa todos los argumentos del comando."));
                              return false;
                           }

                           action = args[2];
                           s = args[3];
                           List list;
                           if (action.equalsIgnoreCase("add")) {
                              list = this.instance.getConfig().getStringList("AntiAFK.Bypass");
                              if (list.contains(s)) {
                                 var44 = this.instance;
                                 sender.sendMessage(Main.format("&cEl jugador &e" + s + " &cya se encuentra en la lista de jugadores inmunes."));
                                 return false;
                              }

                              list.add(s);
                              this.instance.getConfig().set("AntiAFK.Bypass", list);
                              this.instance.saveConfig();
                              this.instance.reloadConfig();
                              var44 = this.instance;
                              sender.sendMessage(Main.format("&aEl jugador &e" + s + " &aha sido agregado a la lista de jugadores inmunes."));
                           } else if (action.equalsIgnoreCase("remove")) {
                              list = this.instance.getConfig().getStringList("AntiAFK.Bypass");
                              if (!list.contains(s)) {
                                 var44 = this.instance;
                                 sender.sendMessage(Main.format("&cEl jugador &e" + s + " &cno se encuentra en la lista de jugadores inmunes."));
                                 return false;
                              }

                              list.remove(s);
                              this.instance.getConfig().set("AntiAFK.Bypass", list);
                              this.instance.saveConfig();
                              this.instance.reloadConfig();
                              var44 = this.instance;
                              sender.sendMessage(Main.format("&aEl jugador &e" + s + " &aha sido eliminado de la lista de jugadores inmunes."));
                           } else {
                              var44 = this.instance;
                              sender.sendMessage(Main.format("&cAcción &e" + action + " &cno reconocida."));
                           }
                        } else {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&cEse sub-comando no existe."));
                        }
                     } else if (args[0].equalsIgnoreCase("storm")) {
                        if (!sender.hasPermission("permadeathcore.admin")) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&cNo tienes permisos para ejecutar este comando."));
                           return false;
                        }

                        if (args.length <= 2) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&cLista de comandos disponibles para la tormenta."));
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&7/pdc storm removeHours <horas> &f&l- &cElimina cantidad de horas de la tormenta."));
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&7/pdc storm addHours <horas> &f&l- &cAgrega cantidad de horas a la tormenta."));
                           return false;
                        }

                        action = args[1];
                        hours = 1;

                        try {
                           hours = Integer.parseInt(args[2]);
                           if (hours < 1) {
                              hours = 1;
                           }
                        } catch (Exception var18) {
                           var44 = this.instance;
                           sender.sendMessage(Main.format("&cIngresa una cantidad válida."));
                        }

                        Iterator var40 = ((List)Bukkit.getWorlds().stream().filter((world1) -> {
                           return world1.getEnvironment() == Environment.NORMAL;
                        }).collect(Collectors.toList())).iterator();

                        while(true) {
                           while(var40.hasNext()) {
                              World w = (World)var40.next();
                              if (action.equalsIgnoreCase("addHours")) {
                                 pX = w.getWeatherDuration();
                                 pZ = pX / 20;
                                 long stormIncrement = (long)(pZ + hours * 3600);
                                 int intsTicks = hours * 3600;
                                 int inc = (int)stormIncrement;
                                 Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:weather thunder");
                                 if (!w.hasStorm() && !w.isThundering()) {
                                    this.instance.world.setWeatherDuration(intsTicks * 20);
                                 } else {
                                    this.instance.world.setWeatherDuration(inc * 20);
                                 }

                                 var44 = this.instance;
                                 player.sendMessage(Main.format("&aOperación completada exitosamente."));
                              } else if (!w.hasStorm() && !w.isThundering()) {
                                 var44 = this.instance;
                                 player.sendMessage(Main.format("&cNo hay ninguna tormenta en marcha."));
                              } else {
                                 pX = w.getWeatherDuration();
                                 pZ = pX / 20;
                                 int newStormTime = Math.max(1, pZ - hours * 3600);
                                 this.instance.world.setWeatherDuration(newStormTime * 20);
                              }
                           }

                           return false;
                        }
                     } else {
                        this.sendHelp(sender);
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private void sendHelp(CommandSender sender) {
      StringBuilder var10001 = new StringBuilder();
      Main var10002 = this.instance;
      sender.sendMessage(var10001.append(Main.tag).append(ChatColor.RED).append("Comandos disponibles:").toString());
      sender.sendMessage(ChatColor.RED + "/pdc idioma <es, en>" + ChatColor.GRAY + ChatColor.ITALIC + "(Cambia tu idioma)");
      sender.sendMessage(ChatColor.RED + "/pdc dias " + ChatColor.GRAY + ChatColor.ITALIC + "(Muestra el día en el que está el plugin)");
      sender.sendMessage(ChatColor.RED + "/pdc duracion " + ChatColor.GRAY + ChatColor.ITALIC + "(Muestra la duración de la tormenta)");
      sender.sendMessage(ChatColor.RED + "/pdc cambios " + ChatColor.GRAY + ChatColor.ITALIC + "(Muestra los cambios de dificultad disponibles)");
      if (sender instanceof Player) {
         sender.sendMessage(ChatColor.RED + "/pdc mensaje <mensaje> " + ChatColor.GRAY + ChatColor.ITALIC + "(Cambia tu mensaje de muerte)");
         sender.sendMessage(ChatColor.RED + "/pdc awake " + ChatColor.GRAY + ChatColor.ITALIC + "(Muestra el tiempo despierto)");
      }

      sender.sendMessage(ChatColor.RED + "/pdc info " + ChatColor.GRAY + ChatColor.ITALIC + "(Información general)");
      sender.sendMessage(ChatColor.RED + "/pdc discord " + ChatColor.GRAY + ChatColor.ITALIC + "(Discord oficial del plugin)");
      if (sender.hasPermission("permadeathcore.admin")) {
         sender.sendMessage("");
         var10001 = new StringBuilder();
         var10002 = this.instance;
         sender.sendMessage(var10001.append(Main.tag).append(ChatColor.RED).append("Comandos de administrador:").toString());
         sender.sendMessage(ChatColor.RED + "/pdc debug " + ChatColor.GRAY + ChatColor.ITALIC + "(Información importante para el soporte)");
         sender.sendMessage(ChatColor.RED + "/pdc reload " + ChatColor.GRAY + ChatColor.ITALIC + "(Recarga el archivo config.yml)");
         sender.sendMessage(ChatColor.RED + "/pdc afk " + ChatColor.GRAY + ChatColor.ITALIC + "(Administra el sistema Anti-AFK)");
         sender.sendMessage(ChatColor.RED + "/pdc storm " + ChatColor.GRAY + ChatColor.ITALIC + "(Administra la tormenta)");
         sender.sendMessage(ChatColor.RED + "/pdc cambiarDia <dia> " + ChatColor.GRAY + ChatColor.ITALIC + "(Cambia el día actual, pd: puede que requiera un reinicio)");
         sender.sendMessage(ChatColor.RED + "/pdc speedrun " + ChatColor.GRAY + ChatColor.ITALIC + "(Comandos del modo SpeedRun, cada día es una hora)");
         sender.sendMessage(ChatColor.RED + "/pdc beginning " + ChatColor.GRAY + ChatColor.ITALIC + "(Comandos de TheBeginning)");
         if (sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + "/pdc give <medalla, netheriteArmor, infernalArmor, infernalBlock, netheriteTools> " + ChatColor.GRAY + ChatColor.ITALIC + "(Obtén ítems especiales de Permadeath)");
         }

         sender.sendMessage(ChatColor.RED + "/pdc event <shulkershell, lifeorb> " + ChatColor.GRAY + ChatColor.ITALIC + "(Comienza un evento)");
         sender.sendMessage(ChatColor.RED + "/pdc locate <beginning> " + ChatColor.GRAY + ChatColor.ITALIC + "(Localiza el portal a The Beginning)");
      }

   }
}
