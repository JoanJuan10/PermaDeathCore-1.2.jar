package com.permadeathcore.Util.Manager.Data;

import com.permadeathcore.Main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class BeginningDataManager {
   private File begginingFile;
   private FileConfiguration config;
   private Main instance;

   public BeginningDataManager(Main instance) {
      this.instance = instance;
      this.begginingFile = new File(instance.getDataFolder(), "theBeginning.yml");
      this.config = YamlConfiguration.loadConfiguration(this.begginingFile);
      if (!this.begginingFile.exists()) {
         try {
            this.begginingFile.createNewFile();
         } catch (IOException var3) {
            System.out.println("[ERROR] Ha ocurrido un error al crear el archivo 'theBeginning.yml'");
         }
      }

      if (!this.config.contains("GeneratedOverWorldBeginningPortal")) {
         this.config.set("GeneratedOverWorldBeginningPortal", false);
      }

      if (!this.config.contains("GeneratedBeginningPortal")) {
         this.config.set("GeneratedBeginningPortal", false);
      }

      if (!this.config.contains("OverWorldPortal")) {
         this.config.set("OverWorldPortal", "");
      }

      if (!this.config.contains("BeginningPortal")) {
         this.config.set("BeginningPortal", "");
      }

      if (!this.config.contains("KilledED")) {
         this.config.set("KilledED", false);
      }

      if (!this.config.contains("PopulatedChests")) {
         this.config.set("PopulatedChests", new ArrayList());
      }

      this.saveFile();
      this.reloadFile();
   }

   public boolean hasPopulatedChest(Location l) {
      String s = locationToString(l);
      return this.config.getStringList("PopulatedChests").contains(s);
   }

   public void addPopulatedChest(Location l) {
      ArrayList<String> chests = (ArrayList)this.config.getStringList("PopulatedChests");
      chests.add(locationToString(l));
      this.config.set("PopulatedChests", chests);
      this.saveFile();
      this.reloadFile();
   }

   public boolean generatedOverWorldBeginningPortal() {
      return this.config.getBoolean("GeneratedOverWorldBeginningPortal");
   }

   public boolean generatedBeginningPortal() {
      return this.config.getBoolean("GeneratedBeginningPortal");
   }

   public Location getBeginningPortal() {
      return !this.generatedBeginningPortal() ? null : buildLocation(this.config.getString("BeginningPortal"));
   }

   public void setBeginningPortal(Location loc) {
      if (!this.generatedBeginningPortal()) {
         this.config.set("GeneratedBeginningPortal", true);
         this.config.set("BeginningPortal", locationToString(loc));
         this.saveFile();
         this.reloadFile();
      }
   }

   public Location getOverWorldPortal() {
      return !this.generatedOverWorldBeginningPortal() ? null : buildLocation(this.config.getString("OverWorldPortal"));
   }

   public void setOverWorldPortal(Location loc) {
      if (!this.generatedOverWorldBeginningPortal()) {
         this.config.set("GeneratedOverWorldBeginningPortal", true);
         this.config.set("OverWorldPortal", locationToString(loc));
         this.saveFile();
         this.reloadFile();
      }
   }

   public boolean killedED() {
      return this.config.getBoolean("KilledED");
   }

   public void setKilledED() {
      this.config.set("KilledED", true);
      this.saveFile();
      this.reloadFile();
   }

   public static Location buildLocation(String s) {
      String[] split = s.split(";");
      Double x = Double.valueOf(split[0]);
      Double y = Double.valueOf(split[1]);
      Double z = Double.valueOf(split[2]);
      World w = Bukkit.getWorld(split[3]);
      return new Location(w, x, y, z);
   }

   public static String locationToString(Location loc) {
      return loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getWorld().getName();
   }

   public FileConfiguration getConfig() {
      return this.config;
   }

   public void saveFile() {
      try {
         this.config.save(this.begginingFile);
      } catch (IOException var2) {
         System.out.println("[ERROR] Ha ocurrido un error al guardar el archivo 'players.yml'");
      }

   }

   public void reloadFile() {
      try {
         this.config.load(this.begginingFile);
      } catch (IOException var2) {
         System.out.println("[ERROR] Ha ocurrido un error al guardar el archivo 'players.yml'");
      } catch (InvalidConfigurationException var3) {
         System.out.println("[ERROR] Ha ocurrido un error al guardar el archivo 'players.yml'");
      }

   }
}
