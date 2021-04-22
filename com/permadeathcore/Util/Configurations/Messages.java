package com.permadeathcore.Util.Configurations;

import com.permadeathcore.Main;
import com.permadeathcore.Util.Library.FileAPI;
import com.permadeathcore.Util.Manager.Data.PlayerDataManager;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Messages {
   private Main instance;

   public Messages(Main instance) {
      this.instance = instance;
      this.saveDataES();
      this.saveDataEN();
   }

   public void saveDataES() {
      new FileAPI.FileOut(this.instance, "mensajes_ES", "mensajes/", false);
      File f = new File(this.instance.getDataFolder(), "mensajes/mensajes_ES.yml");
      FileConfiguration config = YamlConfiguration.loadConfiguration(f);
      FileAPI.UtilFile c = FileAPI.select(this.instance, f, config);
      c.set("Server-Messages.coords-msg-enable", true);
      c.set("Server-Messages.OnJoin", "&e%player% se ha unido al servidor.");
      c.set("Server-Messages.OnLeave", "&e%player% ha abandonado el servidor.");
      c.set("Server-Messages.StormEnd", "&cLa tormenta ha llegado a su fin.");
      c.set("Server-Messages.Sleep", "&7%player% &efue a dormir.");
      c.set("Server-Messages.Sleeping", "&7%player% &eestá durmiendo &e(&b%players%&7/&b%needed%&e)");
      c.set("Server-Messages.DeathMessageTitle", "&c¡Permadeath!");
      c.set("Server-Messages.DeathMessageSubtitle", "%player% ha muerto");
      c.set("Server-Messages.DeathMessageChat", "&c&lEste es el comienzo del sufrimiento eterno de &4&l%player%&c&l. ¡HA SIDO PERMABANEADO!");
      c.set("Server-Messages.DeathTrainMessage", "&c¡Comienza el Death Train con duración de %tiempo% horas!");
      c.set("Server-Messages.DeathTrainMessageMinutes", "&c¡Comienza el Death Train con duración de %tiempo% minutos!");
      c.set("Server-Messages.ActionBarMessage", "&7Quedan %tiempo% de tormenta");
      c.save();
      c.load();
   }

   public void saveDataEN() {
      new FileAPI.FileOut(this.instance, "mensajes_EN", "mensajes/", false);
      File f = new File(this.instance.getDataFolder(), "mensajes/mensajes_EN.yml");
      FileConfiguration config = YamlConfiguration.loadConfiguration(f);
      FileAPI.UtilFile c = FileAPI.select(this.instance, f, config);
      c.set("Server-Messages.OnJoin", "&e%player% joined the game.");
      c.set("Server-Messages.OnLeave", "&e%player% left the game.");
      c.set("Server-Messages.StormEnd", "&cThe storm has been ended.");
      c.set("Server-Messages.Sleep", "&7%player% &ewent to sleep, Sweet dreams!.");
      c.set("Server-Messages.Sleeping", "&7%player% &eis sleeping &e(&b%players%&7/&b%needed%&e)");
      c.set("Server-Messages.DeathMessageTitle", "&cPermadeath!");
      c.set("Server-Messages.DeathMessageSubtitle", "%player% died");
      c.set("Server-Messages.DeathMessageChat", "&c&lThis is the beginning of the eternal suffering of &4&l%player%&c&l. HAS BEEN PERMA-BANNED!");
      c.set("Server-Messages.DeathTrainMessage", "&cStarting the Death Train with a duration of %tiempo% hours!");
      c.set("Server-Messages.DeathTrainMessageMinutes", "&cStarting the Death Train with a duration of %tiempo% minutes!");
      c.set("Server-Messages.ActionBarMessage", "&7%tiempo% storm left");
      c.save();
      c.load();
   }

   public void reloadFiles() {
      this.loadEs();
      this.loadEn();
   }

   private void loadEn() {
      new FileAPI.FileOut(this.instance, "mensajes_EN", "mensajes/", false);
      File f = new File(this.instance.getDataFolder(), "mensajes/mensajes_EN.yml");
      FileConfiguration config = YamlConfiguration.loadConfiguration(f);
      FileAPI.select(this.instance, f, config);
   }

   private void loadEs() {
      new FileAPI.FileOut(this.instance, "mensajes_ES", "mensajes/", false);
      File f = new File(this.instance.getDataFolder(), "mensajes/mensajes_ES.yml");
      FileConfiguration config = YamlConfiguration.loadConfiguration(f);
      FileAPI.UtilFile c = FileAPI.select(this.instance, f, config);
      c.load();
   }

   public String getMessageByPlayer(String path, String playerName, List replaces) {
      HashMap<String, Object> r = new HashMap();
      Iterator var5 = replaces.iterator();

      String returning;
      while(var5.hasNext()) {
         Object o = var5.next();
         String s = String.valueOf(o);
         String[] l = s.split(";;;");
         returning = l[1];
         r.put(l[0], returning);
      }

      PlayerDataManager data = new PlayerDataManager(playerName, this.instance);
      Language lang = data.getLanguage();
      File f = this.getByLang(lang);
      FileConfiguration c = YamlConfiguration.loadConfiguration(f);
      returning = c.getString(path);

      String a;
      String value;
      for(Iterator var10 = r.keySet().iterator(); var10.hasNext(); returning = returning.replace(a, value)) {
         a = (String)var10.next();
         value = String.valueOf(r.get(a));
      }

      return Main.format(returning);
   }

   public String getMessageForConsole(String path) {
      Language lang = Language.SPANISH;
      File f = this.getByLang(lang);
      FileConfiguration c = YamlConfiguration.loadConfiguration(f);
      String returning = c.getString(path);
      return Main.format(returning);
   }

   public String getMessageByPlayer(String path, String playerName) {
      HashMap<String, Object> r = new HashMap();
      PlayerDataManager data = new PlayerDataManager(playerName, this.instance);
      Language lang = data.getLanguage();
      File f = this.getByLang(lang);
      FileConfiguration c = YamlConfiguration.loadConfiguration(f);
      String returning = c.getString(path);

      String a;
      String value;
      for(Iterator var9 = r.keySet().iterator(); var9.hasNext(); returning = returning.replace(a, value)) {
         a = (String)var9.next();
         value = String.valueOf(r.get(a));
      }

      return Main.format(returning);
   }

   private File getByLang(Language lang) {
      if (lang == Language.SPANISH) {
         return new File(this.instance.getDataFolder(), "mensajes/mensajes_ES.yml");
      } else {
         return lang == Language.ENGLISH ? new File(this.instance.getDataFolder(), "mensajes/mensajes_EN.yml") : null;
      }
   }

   public String getMessage(String path, Player player) {
      return this.instance.getMessages().getMessageByPlayer("Server-Messages." + path, player.getName());
   }

   public String getMessage(String path, Player player, List l) {
      return this.instance.getMessages().getMessageByPlayer("Server-Messages." + path, player.getName(), l);
   }

   public String getMsgForConsole(String path) {
      return this.instance.getMessages().getMessageForConsole("Server-Messages." + path);
   }

   public void sendConsole(String mensaje) {
      Bukkit.getConsoleSender().sendMessage(mensaje);
   }
}
