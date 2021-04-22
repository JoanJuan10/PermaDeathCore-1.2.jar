package com.permadeathcore.TheBeginning.WorldGenerator;

import com.permadeathcore.Main;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.SplittableRandom;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.jetbrains.annotations.NotNull;

public class BeginningGenerator extends ChunkGenerator {
   private static final int HEIGHT = 100;
   private static final boolean SMALL_ISLANDS_ENABLED = Main.getInstance().isSmallIslandsEnabled();
   private final SplittableRandom random = new SplittableRandom();

   public ChunkData generateChunkData(World world, Random cRandom, int chunkX, int chunkZ, BiomeGrid biomes) {
      SimplexOctaveGenerator lowGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
      ChunkData chunk = this.createChunkData(world);
      lowGenerator.setScale(0.02D);

      for(int X = 0; X < 16; ++X) {
         for(int Z = 0; Z < 16; ++Z) {
            int noise = (int)(lowGenerator.noise((double)(chunkX * 16 + X), (double)(chunkZ * 16 + Z), 0.5D, 0.5D) * 15.0D);
            if (noise <= 0) {
               if (SMALL_ISLANDS_ENABLED && X == 8 && Z == 8 && this.random.nextInt(20) == 0) {
                  Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                     this.generateIsland(world, chunkX * 16 + X, chunkZ * 16 + Z, this.random);
                  }, 20L);
               }
            } else {
               int chance = Main.getInstance().getConfig().getInt("Toggles.TheBeginning.YticGenerateChance");
               if (chance > 1000000 || chance < 1) {
                  chance = 100000;
               }

               if (this.random.nextInt(chance) == 0) {
                  Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                     this.generateYtic(world, chunkX * 16 + X, chunkZ * 16 + Z);
                  }, 20L);
               }

               int i;
               for(i = 0; i < noise / 3; ++i) {
                  chunk.setBlock(X, i + 100, Z, Material.PURPUR_BLOCK);
               }

               for(i = 0; i < noise; ++i) {
                  chunk.setBlock(X, 100 - i - 1, Z, Material.PURPUR_BLOCK);
               }
            }
         }
      }

      return chunk;
   }

   private void generateIsland(World world, int x, int z, SplittableRandom random) {
      File file;
      switch(random.nextInt(6)) {
      case 0:
         file = new File(Main.getInstance().getDataFolder(), "schematics/island1.schem");
         break;
      case 1:
         file = new File(Main.getInstance().getDataFolder(), "schematics/island2.schem");
         break;
      case 2:
         file = new File(Main.getInstance().getDataFolder(), "schematics/island3.schem");
         break;
      case 3:
         file = new File(Main.getInstance().getDataFolder(), "schematics/island4.schem");
         break;
      default:
         file = new File(Main.getInstance().getDataFolder(), "schematics/island5.schem");
      }

      ClipboardFormat format = ClipboardFormats.findByFile(file);

      assert format != null;

      Clipboard clipboard;
      Throwable var9;
      try {
         ClipboardReader reader = format.getReader(new FileInputStream(file));
         var9 = null;

         try {
            clipboard = reader.read();
         } catch (Throwable var37) {
            var9 = var37;
            throw var37;
         } finally {
            if (reader != null) {
               if (var9 != null) {
                  try {
                     reader.close();
                  } catch (Throwable var34) {
                     var9.addSuppressed(var34);
                  }
               } else {
                  reader.close();
               }
            }

         }
      } catch (IOException var39) {
         var39.printStackTrace();
         return;
      }

      try {
         EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(world), -1);
         var9 = null;

         try {
            Operation operation = (new ClipboardHolder(clipboard)).createPaste(editSession).to(BlockVector3.at(x, 120, z)).ignoreAirBlocks(true).build();
            Operations.complete(operation);
         } catch (Throwable var36) {
            var9 = var36;
            throw var36;
         } finally {
            if (editSession != null) {
               if (var9 != null) {
                  try {
                     editSession.close();
                  } catch (Throwable var35) {
                     var9.addSuppressed(var35);
                  }
               } else {
                  editSession.close();
               }
            }

         }
      } catch (WorldEditException var41) {
         var41.printStackTrace();
      }

   }

   private void generateYtic(World world, int x, int z) {
      File file = new File(Main.getInstance().getDataFolder(), "schematics/ytic.schem");
      ClipboardFormat format = ClipboardFormats.findByFile(file);

      assert format != null;

      Clipboard clipboard;
      Throwable var8;
      try {
         ClipboardReader reader = format.getReader(new FileInputStream(file));
         var8 = null;

         try {
            clipboard = reader.read();
         } catch (Throwable var36) {
            var8 = var36;
            throw var36;
         } finally {
            if (reader != null) {
               if (var8 != null) {
                  try {
                     reader.close();
                  } catch (Throwable var33) {
                     var8.addSuppressed(var33);
                  }
               } else {
                  reader.close();
               }
            }

         }
      } catch (IOException var38) {
         var38.printStackTrace();
         return;
      }

      try {
         EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(world), -1);
         var8 = null;

         try {
            ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard);
            Operation operation = clipboardHolder.createPaste(editSession).to(BlockVector3.at(x, 134, z)).ignoreAirBlocks(true).copyEntities(true).build();
            Operations.complete(operation);
         } catch (Throwable var35) {
            var8 = var35;
            throw var35;
         } finally {
            if (editSession != null) {
               if (var8 != null) {
                  try {
                     editSession.close();
                  } catch (Throwable var34) {
                     var8.addSuppressed(var34);
                  }
               } else {
                  editSession.close();
               }
            }

         }
      } catch (WorldEditException var40) {
         var40.printStackTrace();
      }

   }

   @NotNull
   public List<BlockPopulator> getDefaultPopulators(World world) {
      return Collections.singletonList(new TreePopulator());
   }
}
