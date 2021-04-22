package com.permadeathcore.Util.GameEvent;

import com.permadeathcore.Main;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class ShellEvent {
   private Main instance;
   private boolean running;
   private int timeLeft;
   private BossBar bossBar;
   private String title;

   public ShellEvent(Main instance) {
      this.instance = instance;
      this.timeLeft = 14400;
      this.title = Main.format("&c&lX2 Shulker Shells: &b&n");
      this.bossBar = Bukkit.createBossBar(this.title, BarColor.RED, BarStyle.SOLID, new BarFlag[0]);
   }

   public BossBar getBossBar() {
      return this.bossBar;
   }

   public void addPlayer(Player p) {
      this.bossBar.addPlayer(p);
   }

   public void clearPlayers() {
      Iterator var1 = this.bossBar.getPlayers().iterator();

      while(var1.hasNext()) {
         Player p = (Player)var1.next();
         this.bossBar.removePlayer(p);
      }

   }

   public void setTitle(String title) {
      this.title = title;
      this.bossBar.setTitle(title);
   }

   public boolean isRunning() {
      return this.running;
   }

   public void setRunning(boolean running) {
      this.running = running;
   }

   public int getTimeLeft() {
      return this.timeLeft;
   }

   public void setTimeLeft(int timeLeft) {
      this.timeLeft = timeLeft;
   }

   public void reduceTime() {
      --this.timeLeft;
   }

   public void removePlayer(Player player) {
      if (!this.bossBar.getPlayers().contains(player)) {
         this.bossBar.addPlayer(player);
      }
   }
}
