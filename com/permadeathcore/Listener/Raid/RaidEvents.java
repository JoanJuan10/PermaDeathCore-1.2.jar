package com.permadeathcore.Listener.Raid;

import com.permadeathcore.Main;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.raid.RaidFinishEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RaidEvents implements Listener {
   @EventHandler
   public void onRaidFinish(final RaidFinishEvent e) {
      if (Main.getInstance().getDays() >= 50L) {
         if (!e.getWinners().isEmpty()) {
            Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
               public void run() {
                  Iterator var1 = e.getWinners().iterator();

                  while(var1.hasNext()) {
                     Player player = (Player)var1.next();
                     if (player.hasPotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE)) {
                        PotionEffect effect = player.getPotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE);
                        player.removePotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE);
                        int min = 300;
                        player.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, min, effect.getAmplifier()));
                     }
                  }

               }
            }, 10L);
         }
      }
   }
}
