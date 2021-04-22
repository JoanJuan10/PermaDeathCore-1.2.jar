package com.permadeathcore.Util.Library;

import com.permadeathcore.Main;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class FileAPI {
   private static FileAPI.UtilFile UF;

   public static FileAPI.UtilFile select(Main plugin, File f, FileConfiguration fc) {
      UF = new FileAPI.UtilFile(plugin, f, fc);
      return UF;
   }

   public static class UtilFile implements FileAPI.InterfaceFile {
      private Main plugin;
      private File f;
      private FileConfiguration fc;

      public UtilFile(Main plugin, File f, FileConfiguration fc) {
         this.plugin = plugin;
         this.f = f;
         this.fc = fc;
      }

      public void create(String filename, boolean saveResource) {
         this.f = new File(this.plugin.getDataFolder(), filename);
         this.fc = new YamlConfiguration();
         if (!this.f.exists()) {
            this.f.getParentFile().mkdirs();
            if (!saveResource) {
               try {
                  this.f.createNewFile();
               } catch (IOException var4) {
                  var4.printStackTrace();
               }
            } else if (this.plugin.getResource(filename) == null) {
               this.plugin.saveResource(filename, true);
            } else {
               this.plugin.saveResource(filename, false);
            }

            this.load();
         }

      }

      public void load() {
         try {
            this.fc.load(this.f);
         } catch (Exception var2) {
            var2.printStackTrace();
         }

      }

      public void save() {
         try {
            this.fc.save(this.f);
         } catch (Exception var2) {
            var2.printStackTrace();
         }

      }

      public File getF() {
         return this.f;
      }

      public FileConfiguration getFc() {
         return this.fc;
      }

      public void set(String path, Object s) {
         if (!this.fc.contains(path)) {
            this.fc.set(path, s);
         }

      }
   }

   public static class FileOut {
      public FileOut(Plugin plugin, @NotNull String path, String outPath, boolean replace) {
         this.saveFile(plugin, path, outPath, replace);
      }

      private void saveFile(Plugin plugin, @NotNull String resourcePath, String outPath, boolean replace) {
         if (resourcePath != null && !resourcePath.equals("")) {
            resourcePath = resourcePath.replace('\\', '/');
            InputStream in = plugin.getResource(resourcePath);
            if (in != null) {
               File outFile = new File(plugin.getDataFolder(), outPath + resourcePath);
               int lastIndex = resourcePath.lastIndexOf(47);
               File outDir = new File(plugin.getDataFolder(), outPath + resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));
               if (!outDir.exists()) {
                  outDir.mkdirs();
               }

               try {
                  if (!outFile.exists() || replace) {
                     OutputStream out = new FileOutputStream(outFile);
                     byte[] buf = new byte[1024];

                     int len;
                     while((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                     }

                     out.close();
                     in.close();
                  }
               } catch (IOException var12) {
                  plugin.getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, var12);
               }
            }

         } else {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
         }
      }
   }

   private interface InterfaceFile {
      void create(String var1, boolean var2);

      void load();

      void save();

      void set(String var1, Object var2);
   }
}
