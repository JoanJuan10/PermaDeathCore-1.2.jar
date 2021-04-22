package com.permadeathcore.Util.Manager.Log;

import com.permadeathcore.Main;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PDCLog {
   private static PDCLog logs;
   private Main instance = Main.getInstance();
   private File file;

   public PDCLog() {
      this.file = new File(this.instance.getDataFolder(), "logs.txt");
      if (!this.file.exists()) {
         try {
            this.file.createNewFile();
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      }

   }

   public void disable(String reason) {
      this.log("El plugin ha sido apagado: " + reason);
   }

   public void log(String log) {
      LocalDate date = LocalDate.now();
      LocalDateTime time = LocalDateTime.now();
      String msg = String.format("[%02d/%02d/%02d] ", date.getDayOfMonth(), date.getMonthValue(), date.getYear()) + String.format("%02d:%02d:%02d ", time.getHour(), time.getMinute(), time.getSecond()) + log;
      this.add(msg);
   }

   private void add(String msg) {
      try {
         BufferedWriter bw = new BufferedWriter(new FileWriter(this.file, true));
         bw.append(msg);
         bw.close();
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public static PDCLog getInstance() {
      if (logs == null) {
         logs = new PDCLog();
      }

      return logs;
   }
}
