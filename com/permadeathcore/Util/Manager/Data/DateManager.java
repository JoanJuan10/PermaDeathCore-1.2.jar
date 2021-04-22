package com.permadeathcore.Util.Manager.Data;

import com.permadeathcore.Main;
import com.permadeathcore.Discord.DiscordManager;
import com.permadeathcore.Util.Manager.Log.PDCLog;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DateManager {
   private Main instance = Main.getInstance();
   private static DateManager dai;
   private File f;
   private FileConfiguration c;
   public String fecha;
   public LocalDate fechaInicio;
   public LocalDate fechaActual = LocalDate.now();

   public DateManager() {
      this.prepareFile();
      this.fecha = this.c.getString("Fecha");

      try {
         this.fechaInicio = LocalDate.parse(this.fecha);
      } catch (DateTimeParseException var2) {
         ConsoleCommandSender var10000 = Bukkit.getConsoleSender();
         Main var10001 = this.instance;
         StringBuilder var3 = new StringBuilder();
         Main var10002 = this.instance;
         var10000.sendMessage(Main.format(var3.append(Main.tag).append("&4&lERROR: &eLa fecha en config.yml estaba mal configurada &7(").append(this.c.getString("Fecha")).append(")&e.").toString()));
         var10000 = Bukkit.getConsoleSender();
         var10001 = this.instance;
         var3 = new StringBuilder();
         var10002 = this.instance;
         var10000.sendMessage(Main.format(var3.append(Main.tag).append("&eSe ha establecido el día: &b1").toString()));
         this.fechaInicio = LocalDate.parse(this.getDateForDayOne());
         this.c.set("Fecha", this.getDateForDayOne());
         this.saveFile();
         this.reloadFile();
      }

   }

   public void tick() {
      LocalDate now = LocalDate.now();
      if (this.fechaActual.isBefore(now)) {
         this.fechaActual = now;
         DiscordManager.getInstance().onDayChange();
      }

   }

   public void reloadDate() {
      this.fecha = this.c.getString("Fecha");
      this.fechaInicio = LocalDate.parse(this.fecha);
      this.fechaActual = LocalDate.now();
   }

   public void setDay(CommandSender sender, String args1) {
      Main var10001;
      int nD;
      try {
         int d = Integer.parseInt(args1);
         if (d <= 120 && d >= 0) {
            nD = d;
         } else {
            nD = 0;
         }
      } catch (NumberFormatException var10) {
         var10001 = this.instance;
         sender.sendMessage(Main.format("&cNecesitas ingresar un número válido."));
         return;
      }

      if (nD == 0) {
         var10001 = this.instance;
         sender.sendMessage(Main.format("&cHas ingresado un número no válido, o ni siquiera un número."));
      } else {
         LocalDate add = this.fechaActual.minusDays((long)nD);
         getInstance().setNewDate(String.format(add.getYear() + "-%02d-%02d", add.getMonthValue(), add.getDayOfMonth()));
         var10001 = this.instance;
         sender.sendMessage(Main.format("&eSe han actualizado los días a: &7" + nD));
         var10001 = this.instance;
         sender.sendMessage(Main.format("&c&lNota importante: &7Algunos cambios pueden requerir un reinicio y la fecha puede no ser exacta."));
         PDCLog.getInstance().log("Día cambiado a: " + nD);
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pdc reload");
         if (Bukkit.getOnlinePlayers() != null && Bukkit.getOnlinePlayers().size() >= 1) {
            OfflinePlayer[] var5 = Bukkit.getOfflinePlayers();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               OfflinePlayer off = var5[var7];
               if (off == null) {
                  return;
               }

               if (off.isBanned()) {
                  return;
               }

               PlayerDataManager manager = new PlayerDataManager(off.getName(), this.instance);
               manager.setLastDay(this.getDays());
            }
         }

      }
   }

   public long getDays() {
      return Main.SPEED_RUN_MODE ? (long)(this.instance.getPlayTime() / 3600) : this.fechaInicio.until(this.fechaActual, ChronoUnit.DAYS);
   }

   public void setNewDate(String value) {
      this.c.set("Fecha", value);
      this.saveFile();
      this.reloadFile();
   }

   public String getDateForDayOne() {
      LocalDate w = this.fechaActual.minusDays(1L);
      return String.format(w.getYear() + "-%02d-%02d", w.getMonthValue(), w.getDayOfMonth());
   }

   private void prepareFile() {
      this.f = new File(this.instance.getDataFolder(), "fecha.yml");
      this.c = YamlConfiguration.loadConfiguration(this.f);
      if (!this.f.exists()) {
         this.instance.saveResource("fecha.yml", false);
         this.c.set("Fecha", this.getDateForDayOne());
         this.saveFile();
         this.reloadFile();
      }

      if (this.c.getString("Fecha").isEmpty()) {
         this.c.set("Fecha", this.getDateForDayOne());
         this.saveFile();
         this.reloadFile();
      }

   }

   private void saveFile() {
      try {
         this.c.save(this.f);
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   private void reloadFile() {
      try {
         this.c.load(this.f);
      } catch (IOException var2) {
         var2.printStackTrace();
      } catch (InvalidConfigurationException var3) {
         var3.printStackTrace();
      }

   }

   public static DateManager getInstance() {
      if (dai == null) {
         dai = new DateManager();
      }

      return dai;
   }
}
