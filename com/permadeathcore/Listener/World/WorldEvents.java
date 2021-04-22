package com.permadeathcore.Listener.World;

import com.permadeathcore.Main;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldEvents implements Listener {
   @EventHandler
   public void onWeatherStorm(WeatherChangeEvent event) {
      Iterator var2;
      World w;
      if (!event.toWeatherState()) {
         var2 = Bukkit.getOnlinePlayers().iterator();

         while(var2.hasNext()) {
            Player p = (Player)var2.next();
            String msg = Main.tag + Main.getInstance().getMessages().getMessage("StormEnd", p);
            p.sendMessage(msg);
         }

         Main.getInstance().getMessages().sendConsole(Main.getInstance().getMessages().getMsgForConsole("StormEnd"));
         if (Main.instance.getDays() >= 50L) {
            if (Main.instance.getBeginningManager() != null) {
               Main.instance.getBeginningManager().setClosed(false);
            }

            var2 = Bukkit.getWorlds().iterator();

            while(var2.hasNext()) {
               w = (World)var2.next();
               w.setGameRule(GameRule.NATURAL_REGENERATION, true);
            }
         }
      } else if (event.getWorld().getEnvironment() == Environment.NORMAL && Main.getInstance().getDays() >= 25L) {
         var2 = Bukkit.getWorlds().iterator();

         while(var2.hasNext()) {
            w = (World)var2.next();
            Iterator var7 = w.getLivingEntities().iterator();

            while(var7.hasNext()) {
               LivingEntity l = (LivingEntity)var7.next();
               Main.getInstance().deathTrainEffects(l);
            }
         }
      }

   }
}
