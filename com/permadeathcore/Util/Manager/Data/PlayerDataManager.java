package com.permadeathcore.Util.Manager.Data;

import com.permadeathcore.Main;
import com.permadeathcore.Util.Configurations.Language;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerDataManager {
   private String name;
   private String banDay;
   private String banTime;
   private String banCause;
   private String coords;
   private File playersFile;
   private FileConfiguration config;
   private Main instance;

   public PlayerDataManager(String playerName, Main instance) {
      this.name = playerName;
      this.instance = instance;
      this.playersFile = new File(instance.getDataFolder(), "jugadores.yml");
      this.config = YamlConfiguration.loadConfiguration(this.playersFile);
      if (!this.playersFile.exists()) {
         try {
            this.playersFile.createNewFile();
         } catch (IOException var4) {
            System.out.println("[ERROR] Ha ocurrido un error al crear el archivo 'jugadores.yml'");
         }
      }

      if (this.config.contains("Players." + playerName)) {
         this.banDay = this.config.getString("Players." + playerName + ".banDay");
         this.banTime = this.config.getString("Players." + playerName + ".banTime");
         this.banCause = this.config.getString("Players." + playerName + ".banCause");
         this.coords = this.config.getString("Players." + playerName + ".coords");
      } else {
         this.banTime = "";
         this.banDay = "";
         this.banCause = "";
         this.coords = "";
      }

      if (Bukkit.getPlayer(playerName) != null) {
         this.addDefault("Players." + this.getName() + ".UUID", Bukkit.getPlayer(playerName).getUniqueId().toString());
      }

      if (!this.config.contains("Players." + this.getName() + ".HP")) {
         this.config.set("Players." + this.getName() + ".HP", 0);
      }

      this.saveFile();
      this.reloadFile();
   }

   private void addDefault(String path, Object value) {
      if (!this.config.contains(path)) {
         this.config.set(path, value);
      } else if (path.equalsIgnoreCase("Players." + this.getName() + ".Idioma")) {
         String idioma = this.config.getString("Players." + this.getName() + ".Idioma");
         if (!idioma.equalsIgnoreCase("SPANISH") && !idioma.equalsIgnoreCase("ENGLISH")) {
            this.config.set("Players." + this.getName() + ".Idioma", "SPANISH");
            this.saveFile();
            this.reloadFile();
         }
      }

   }

   public Language getLanguage() {
      this.addDefault("Players." + this.getName() + ".Idioma", "SPANISH");
      return Language.valueOf(this.config.getString("Players." + this.getName() + ".Idioma"));
   }

   public void setLanguage(Language language) {
      this.config.set("Players." + this.name + ".Idioma", language.toString());
      this.saveFile();
      this.reloadFile();
   }

   public void generateDayData() {
      long days = this.instance.getDays();
      if (!this.config.contains("Players." + this.name + ".LastDay")) {
         this.setLastDay(days);
      }
   }

   public void setLastDay(long days) {
      this.config.set("Players." + this.name + ".LastDay", days);
      this.saveFile();
      this.reloadFile();
   }

   public long getLastDay() {
      this.generateDayData();
      return this.config.getLong("Players." + this.name + ".LastDay");
   }

   public ItemStack craftHead() {
      ItemStack s = new ItemStack(Material.PLAYER_HEAD);
      ItemMeta meta = s.getItemMeta();
      Main var10001 = this.instance;
      meta.setDisplayName(Main.format("&c&l" + this.name));
      String[] var3 = new String[5];
      Main var10004 = this.instance;
      var3[0] = Main.format("&c&lHA SIDO PERMABANEADO");
      var10004 = this.instance;
      var3[1] = Main.format(" ");
      var10004 = this.instance;
      var3[2] = Main.format("&7Fecha del Baneo: &c" + this.banDay);
      var10004 = this.instance;
      var3[3] = Main.format("&7Hora del Baneo: &c" + this.banTime);
      var10004 = this.instance;
      var3[4] = Main.format("&7Causa del Baneo: " + this.banCause);
      meta.setLore(Arrays.asList(var3));
      s.setItemMeta(meta);
      return s;
   }

   public ItemStack craftHead(ItemStack s) {
      ItemMeta meta = s.getItemMeta();
      Main var10001 = this.instance;
      meta.setDisplayName(Main.format("&c&l" + this.name));
      String[] var3 = new String[5];
      Main var10004 = this.instance;
      var3[0] = Main.format("&c&lHA SIDO PERMABANEADO");
      var10004 = this.instance;
      var3[1] = Main.format(" ");
      var10004 = this.instance;
      var3[2] = Main.format("&7Fecha del Baneo: &c" + this.banDay);
      var10004 = this.instance;
      var3[3] = Main.format("&7Hora del Baneo: &c" + this.banTime);
      var10004 = this.instance;
      var3[4] = Main.format("&7Causa de Muerte: " + this.banCause);
      meta.setLore(Arrays.asList(var3));
      s.setItemMeta(meta);
      return s;
   }

   public void setExtraHP(int hp) {
      this.config.set("Players." + this.getName() + ".HP", hp);
      this.saveFile();
      this.reloadFile();
   }

   public int getExtraHP() {
      return this.config.getInt("Players." + this.getName() + ".HP");
   }

   public void setDeathDay() {
      LocalDate fechaActual = LocalDate.now();
      int month = fechaActual.getMonthValue();
      int day = fechaActual.getDayOfMonth();
      String s = "";
      if (month < 10) {
         s = fechaActual.getYear() + "-0" + month + "-";
      } else {
         s = fechaActual.getYear() + "-" + month + "-";
      }

      if (day < 10) {
         s = s + "0" + day;
      } else {
         s = s + day;
      }

      this.setBanDay(s);
   }

   public void setDeathTime() {
      LocalDateTime fechaActual = LocalDateTime.now();
      int sec = fechaActual.getSecond();
      int min = fechaActual.getMinute();
      int hour = fechaActual.getHour();
      String fSec = "";
      String fMin = "";
      String fHour = "";
      if (sec < 10) {
         fSec = "0" + sec;
      } else {
         fSec = String.valueOf(sec);
      }

      if (min < 10) {
         fMin = "0" + min;
      } else {
         fMin = String.valueOf(min);
      }

      if (hour < 10) {
         fHour = "0" + hour;
      } else {
         fHour = String.valueOf(hour);
      }

      String s = fHour + ":" + fMin + ":" + fSec;
      this.setBanTime(s);
   }

   public void setAutoDeathCause(DamageCause lastDamage) {
      String s = "";
      if (lastDamage == DamageCause.WITHER) {
         s = "&0Efecto Wither";
      } else if (lastDamage == DamageCause.BLOCK_EXPLOSION) {
         s = "Explosión";
      } else if (lastDamage == DamageCause.DRAGON_BREATH) {
         s = "&dEnder Dragon (Breath)";
      } else if (lastDamage == DamageCause.ENTITY_ATTACK) {
         s = "Mobs";
      } else if (lastDamage == DamageCause.DROWNING) {
         s = "Ahogamiento";
      } else if (lastDamage == DamageCause.ENTITY_EXPLOSION) {
         s = "Explosión";
      } else if (lastDamage == DamageCause.FALL) {
         s = "Caída";
      } else if (lastDamage != DamageCause.FIRE && lastDamage != DamageCause.FIRE_TICK) {
         if (lastDamage != DamageCause.HOT_FLOOR && lastDamage != DamageCause.LAVA) {
            if (lastDamage == DamageCause.LIGHTNING) {
               s = "Trueno";
            } else if (lastDamage == DamageCause.POISON) {
               s = "Veneno";
            } else if (lastDamage == DamageCause.VOID) {
               s = "Vacío";
            } else if (lastDamage == DamageCause.SUFFOCATION) {
               s = "Sofocado";
            } else if (lastDamage == DamageCause.SUICIDE) {
               s = "Suicidio";
            } else if (lastDamage == DamageCause.THORNS) {
               s = "Espinas";
            } else if (lastDamage == DamageCause.PROJECTILE) {
               s = "Proyectil";
            } else {
               s = "Causa desconocida.";
            }
         } else {
            s = "Lava";
         }
      } else {
         s = "Fuego";
      }

      this.setBanCause(s);
   }

   public String getName() {
      return this.name;
   }

   public String getBanDay() {
      return this.config.getString("Players." + this.name + ".banDay");
   }

   public void setBanDay(String banDay) {
      this.banDay = banDay;
      this.config.set("Players." + this.getName() + ".banDay", banDay);
      this.saveFile();
      this.reloadFile();
   }

   public void setDeathCoords(Location where) {
      int x = (int)where.getX();
      int y = (int)where.getY();
      int z = (int)where.getZ();
      String s = x + " " + y + " " + z;
      this.coords = s;
      this.config.set("Players." + this.getName() + ".coords", s);
      this.saveFile();
      this.reloadFile();
   }

   public String getBanTime() {
      return this.config.getString("Players." + this.name + ".banTime");
   }

   public void setBanTime(String banTime) {
      this.banTime = banTime;
      this.config.set("Players." + this.getName() + ".banTime", banTime);
      this.saveFile();
      this.reloadFile();
   }

   public String getBanCause() {
      return this.config.getString("Players." + this.name + ".banCause");
   }

   public void setBanCause(String banCause) {
      this.banCause = banCause;
      this.config.set("Players." + this.getName() + ".banCause", banCause);
      this.saveFile();
      this.reloadFile();
   }

   public void saveFile() {
      try {
         this.config.save(this.playersFile);
      } catch (IOException var2) {
         System.out.println("[ERROR] Ha ocurrido un error al guardar el archivo 'players.yml'");
      }

   }

   public void reloadFile() {
      try {
         this.config.load(this.playersFile);
      } catch (IOException var2) {
         System.out.println("[ERROR] Ha ocurrido un error al guardar el archivo 'players.yml'");
      } catch (InvalidConfigurationException var3) {
         System.out.println("[ERROR] Ha ocurrido un error al guardar el archivo 'players.yml'");
      }

   }
}
