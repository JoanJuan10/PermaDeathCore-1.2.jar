package com.permadeathcore.TheBeginning.WorldGenerator;

import com.permadeathcore.TheBeginning.BeginningManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SplittableRandom;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BeginningLootTable {
   private List<Integer> randomLoc = new ArrayList();
   private List<String> probs;
   private List<Material> alreadyRolled;
   private BeginningManager manager;
   private SplittableRandom random;

   public BeginningLootTable(BeginningManager man) {
      this.manager = man;

      for(int i = 0; i < 27; ++i) {
         this.randomLoc.add(i);
      }

      this.probs = new ArrayList();
      this.alreadyRolled = new ArrayList();
      this.random = new SplittableRandom();
      this.addItem(this.probs, Material.GOLD_INGOT, 5, 50, 60);
      this.addItem(this.probs, Material.GOLDEN_APPLE, 60, 1, 8);
      this.addItem(this.probs, Material.DIAMOND, 60, 16, 24);
      this.addItem(this.probs, Material.ARROW, 10, 10, 16);
      this.addItem(this.probs, Material.FIREWORK_ROCKET, 20, 55, 64);
      this.addItem(this.probs, Material.TOTEM_OF_UNDYING, 5, 1, 2);
      this.addItem(this.probs, Material.STRUCTURE_VOID, 1, 1, 1);
   }

   public void populateChest(Chest chest) {
      World w = chest.getWorld();
      Inventory inv = chest.getBlockInventory();
      if (w.getName().equalsIgnoreCase("pdc_the_beginning")) {
         if (!inv.contains(Material.DIAMOND_PICKAXE)) {
            this.roll(chest);
         }
      }
   }

   private List<String> addItem(List<String> list, Material mat, int chance, int min, int max) {
      list.add(mat.toString() + ";" + chance + ";" + min + ";" + max);
      return list;
   }

   private void roll(Chest c) {
      int rollTimes = this.random.nextInt(3) + 1;

      for(int i = 0; i < rollTimes; ++i) {
         this.generate(c);
      }

   }

   private void generate(Chest chest) {
      Iterator t = this.probs.iterator();

      while(t.hasNext()) {
         String[] split = String.valueOf(t.next()).split(";");
         Inventory inventory = chest.getBlockInventory();
         Collections.shuffle(this.randomLoc);
         int added = 0;
         if (this.random.nextInt(100) + 1 <= this.getChance(split) && !this.alreadyRolled.contains(this.getMaterial(split))) {
            if (this.getMaterial(split) != Material.TOTEM_OF_UNDYING && this.getMaterial(split) != Material.STRUCTURE_VOID) {
               int ammount = this.generateValue(this.getMin(split), this.getMax(split));
               ItemStack s = new ItemStack(this.getMaterial(split), ammount);
               inventory.setItem((Integer)this.randomLoc.get(added), s);

               try {
                  int x = ammount + this.getMin(split) / 2;
                  ItemStack s2 = new ItemStack(s.getType(), x);
                  int r = this.random.nextInt(5) + 1;
                  int slot = (this.random.nextBoolean() ? -1 : 1) * r;
                  inventory.setItem((Integer)this.randomLoc.get(added + slot), s2);
               } catch (Exception var12) {
               }

               byte var10000 = added;
               int var13 = added + 1;
               if (var10000 < inventory.getSize() - 1) {
                  this.alreadyRolled.add(s.getType());
                  continue;
               }
               break;
            }

            inventory.setItem((Integer)this.randomLoc.get(added), new ItemStack(this.getMaterial(split)));
            return;
         }
      }

   }

   private boolean hasSlot(Inventory inventory) {
      boolean b = false;

      for(int i = 0; i < inventory.getSize(); ++i) {
         if (inventory.getItem(i) == null) {
            b = true;
         }
      }

      return b;
   }

   private int getMin(String[] s) {
      return Integer.valueOf(s[2]);
   }

   private int getMax(String[] s) {
      return Integer.valueOf(s[3]);
   }

   private int getChance(String[] s) {
      return Integer.valueOf(s[1]);
   }

   private Material getMaterial(String[] s) {
      return Material.valueOf(s[0]);
   }

   private int generateValue(int min, int max) {
      return this.random.nextInt(max - min) + this.random.nextInt(min) + 1;
   }
}
