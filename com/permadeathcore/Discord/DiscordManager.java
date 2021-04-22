package com.permadeathcore.Discord;

import com.permadeathcore.Main;
import com.permadeathcore.Util.Manager.Data.PlayerDataManager;
import com.permadeathcore.Util.Manager.Log.PDCLog;
import java.awt.Color;
import java.io.File;
import java.time.LocalDate;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class DiscordManager {
   private static DiscordManager discordManager;
   private Main instance = Main.getInstance();
   private File file;
   private FileConfiguration configuration;
   private JDA bot;

   public DiscordManager() {
      this.file = new File(this.instance.getDataFolder(), "discord.yml");
      this.configuration = YamlConfiguration.loadConfiguration(this.file);
      if (!this.file.exists()) {
         this.instance.saveResource("discord.yml", false);
      }

      if (this.configuration.getBoolean("Enable")) {
         this.log("Intentando cargar la aplicación de Discord.");
         String token = this.configuration.getString("Token");
         if (token.isEmpty()) {
            this.log("No se ha proporcionado un token por el usuario");
            return;
         }

         try {
            JDABuilder builder = JDABuilder.createDefault(token);
            builder.setActivity(Activity.watching("Gaming-Force.es | Permadeath " + this.configuration.getString("Status")));
            builder.addEventListeners(new Object[]{new JDAListeners(this)});
            this.bot = builder.build();
            this.bot.awaitReady();
         } catch (LoginException var5) {
            var5.printStackTrace();
            this.log("Ha ocurrido un error al iniciar sesión con la aplicación de Discord, revisa tu token.");
         } catch (InterruptedException var6) {
            var6.printStackTrace();
            this.log("Ha ocurrido un error al iniciar sesión con la aplicación de Discord, revisa tu token.");
         }

         try {
            String s = this.configuration.getString("Channels.Anuncios");
            if (s == null) {
               return;
            }

            TextChannel channel = this.bot.getTextChannelById(s);
            if (channel == null) {
               return;
            }

            this.sendEmbed(channel, this.buildEmbed("G-F Permadeath", Color.GREEN, (String)null, (String)null, (String)null, "Servidor encendido."));
         } catch (Exception var4) {
         }
      } else {
         this.log("El bot de discord no está activado en la config");
      }

   }

   public void onDisable() {
      if (this.bot != null) {
         String s = this.configuration.getString("Channels.Anuncios");
         if (s != null) {
            TextChannel channel = this.bot.getTextChannelById(s);
            if (channel != null) {
               this.sendEmbed(channel, this.buildEmbed("G-F Permadeath", Color.RED, (String)null, (String)null, (String)null, "Servidor apagado."));
            }
         }
      }
   }

   public void onDeathTrain(String msg) {
      if (this.bot != null) {
         String s = this.configuration.getString("Channels.Anuncios");
         if (s != null) {
            TextChannel channel = this.bot.getTextChannelById(s);
            if (channel != null) {
               this.sendEmbed(channel, this.buildEmbed("G-F Permadeath", Color.RED, (String)null, (String)null, (String)null, ":fire: " + ChatColor.stripColor(msg)));
            }
         }
      }
   }

   public void onDayChange() {
      if (this.bot != null) {
         String s = this.configuration.getString("Channels.Anuncios");
         if (s != null) {
            TextChannel channel = this.bot.getTextChannelById(s);
            if (channel != null) {
               this.sendEmbed(channel, this.buildEmbed("G-F Permadeath", Color.GREEN, (String)null, (String)null, (String)null, ":alarm_clock: Han avanzado al día " + this.instance.getDays()));
            }
         }
      }
   }

   public void banPlayer(OfflinePlayer off, boolean isAFKBan) {
      if (this.bot != null) {
         Player p = off.isOnline() ? (Player)off : null;
         PlayerDataManager data = new PlayerDataManager(off.getName(), this.instance);
         String playerLoc = isAFKBan ? "" : p.getLocation().getBlockX() + " " + p.getLocation().getBlockY() + " " + p.getLocation().getBlockZ();
         String serverName = this.configuration.getString("ServerName");
         LocalDate n = LocalDate.now();
         String date = String.format("%02d/%02d/%02d", n.getDayOfMonth(), n.getMonthValue(), n.getYear());
         String cause = isAFKBan ? "AFK" : data.getBanCause();
         EmbedBuilder b = this.buildEmbed(off.getName() + " ha sido PERMABANEADO en " + serverName + "\n", new Color(15993868), (String)null, (String)null, "https://minotar.net/armor/bust/" + off.getName() + "/100.png");
         b.setAuthor("Gaming-Force.es | Permadeath", "https://media.discordapp.net/attachments/493059088125788162/783994257274175518/g-f.png");
         b.addField("\ud83d\udcc5 Fecha", date, true);
         b.addField("\ud83d\udc80 Razón", cause, true);
         if (!isAFKBan) {
            b.addField("\ud83e\udded Coordenadas", playerLoc, true);
         }

         TextChannel channel = this.getBot().getTextChannelById(this.configuration.getString("Channels.DeathChannel"));
         if (channel == null) {
            this.log("No pudimos encontrar el canal de muertes.");
         }

         channel.sendMessage(b.build()).queue((message) -> {
            message.addReaction("☠").queue();
         });
         this.log("Enviando mensaje de muerte a discord");
      }
   }

   private void log(String s) {
      PDCLog.getInstance().log("[DISCORD] " + s);
   }

   public JDA getBot() {
      return this.bot;
   }

   public File getFile() {
      return this.file;
   }

   public FileConfiguration getConfiguration() {
      return this.configuration;
   }

   public static DiscordManager getInstance() {
      if (discordManager == null) {
         discordManager = new DiscordManager();
      }

      return discordManager;
   }

   private MessageBuilder buildMessage(String description, MessageEmbed... embed) {
      MessageBuilder b = new MessageBuilder();
      b.setContent(description);
      MessageEmbed[] var4 = embed;
      int var5 = embed.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         MessageEmbed e = var4[var6];
         b.setEmbed(e);
      }

      return b;
   }

   private void sendMessage(MessageChannel channel, MessageBuilder b, String... reaction) {
      channel.sendMessage(b.build()).queue((message) -> {
         String[] var2 = reaction;
         int var3 = reaction.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String s = var2[var4];
            message.addReaction(s).queue();
         }

      });
   }

   private EmbedBuilder buildEmbed(String title, Color color, String footer, String image, String thumbnail, String... description) {
      EmbedBuilder eb = new EmbedBuilder();
      if (title != null) {
         eb.setTitle(title);
      }

      if (color != null) {
         eb.setColor(color);
      }

      if (footer != null) {
         eb.setFooter(footer);
      }

      if (image != null) {
         eb.setImage(image);
      }

      if (thumbnail != null) {
         eb.setThumbnail(thumbnail);
      }

      String[] var8 = description;
      int var9 = description.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         String s = var8[var10];
         eb.addField("", s, false);
      }

      return eb;
   }

   private void sendEmbed(MessageChannel channel, EmbedBuilder b, String... reaction) {
      channel.sendMessage(b.build()).queue((message) -> {
         String[] var2 = reaction;
         int var3 = reaction.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String s = var2[var4];
            message.addReaction(s).queue();
         }

      });
   }
}
