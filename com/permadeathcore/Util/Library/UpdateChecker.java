package com.permadeathcore.Util.Library;

import com.permadeathcore.Main;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Consumer;

public class UpdateChecker {
   private Plugin plugin;
   private int resourceId;
   private boolean hasInternetConection = true;

   public UpdateChecker(Plugin plugin) {
      this.plugin = plugin;
      this.resourceId = 78993;
   }

   public void getVersion(Consumer<String> consumer) {
      Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
         try {
            InputStream inputStream = (new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId)).openStream();
            Throwable var3 = null;

            try {
               Scanner scanner = new Scanner(inputStream);
               Throwable var5 = null;

               try {
                  if (scanner.hasNext()) {
                     consumer.accept(scanner.next());
                  }
               } catch (Throwable var30) {
                  var5 = var30;
                  throw var30;
               } finally {
                  if (scanner != null) {
                     if (var5 != null) {
                        try {
                           scanner.close();
                        } catch (Throwable var29) {
                           var5.addSuppressed(var29);
                        }
                     } else {
                        scanner.close();
                     }
                  }

               }
            } catch (Throwable var32) {
               var3 = var32;
               throw var32;
            } finally {
               if (inputStream != null) {
                  if (var3 != null) {
                     try {
                        inputStream.close();
                     } catch (Throwable var28) {
                        var3.addSuppressed(var28);
                     }
                  } else {
                     inputStream.close();
                  }
               }

            }
         } catch (IOException var34) {
            this.plugin.getServer().getConsoleSender().sendMessage(Main.format(Main.tag + "&7> &4&lNO SE HA PODIDO VERIFICAR UNA ACTUALIZACIÓN"));
            this.hasInternetConection = false;
         }

      });
   }

   public boolean isHasInternetConection() {
      return this.hasInternetConection;
   }
}
