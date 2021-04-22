package com.permadeathcore.NMS;

import com.permadeathcore.Main;
import com.permadeathcore.NMS.Versions.ClassFinder.ClassFinder_1_14_R1;
import com.permadeathcore.NMS.Versions.ClassFinder.ClassFinder_1_15_R1;
import com.permadeathcore.NMS.Versions.ClassFinder.ClassFinder_1_16_R1;
import com.permadeathcore.NMS.Versions.ClassFinder.ClassFinder_1_16_R2;

public class NMSFinder {
   private Main instance;

   public NMSFinder(Main instance) {
      this.instance = instance;
   }

   public Object getNMSHandler() {
      if (VersionManager.isRunning14()) {
         return (new ClassFinder_1_14_R1()).findNmsHandler();
      } else if (VersionManager.isRunning15()) {
         return (new ClassFinder_1_15_R1()).findNmsHandler();
      } else if (VersionManager.getVersion().equalsIgnoreCase("1_16_R1")) {
         return (new ClassFinder_1_16_R1()).findNmsHandler();
      } else {
         return VersionManager.getVersion().equalsIgnoreCase("1_16_R2") ? (new ClassFinder_1_16_R2()).findNmsHandler() : null;
      }
   }

   public Object getNMSAccesor() {
      if (VersionManager.isRunning14()) {
         return (new ClassFinder_1_14_R1()).findNmsAccesor();
      } else if (VersionManager.isRunning15()) {
         return (new ClassFinder_1_15_R1()).findNmsAccesor();
      } else if (VersionManager.getVersion().equalsIgnoreCase("1_16_R1")) {
         return (new ClassFinder_1_16_R1()).findNmsAccesor();
      } else {
         return VersionManager.getVersion().equalsIgnoreCase("1_16_R2") ? (new ClassFinder_1_16_R2()).findNmsAccesor() : null;
      }
   }

   public Object getCustomBlock() {
      if (VersionManager.isRunning14()) {
         return (new ClassFinder_1_14_R1()).findCustomBlock();
      } else if (VersionManager.isRunning15()) {
         return (new ClassFinder_1_15_R1()).findCustomBlock();
      } else if (VersionManager.getVersion().equalsIgnoreCase("1_16_R1")) {
         return (new ClassFinder_1_16_R1()).findCustomBlock();
      } else {
         return VersionManager.getVersion().equalsIgnoreCase("1_16_R2") ? (new ClassFinder_1_16_R2()).findCustomBlock() : null;
      }
   }
}
