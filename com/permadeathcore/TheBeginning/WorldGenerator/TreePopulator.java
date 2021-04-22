package com.permadeathcore.TheBeginning.WorldGenerator;

import io.netty.util.internal.ConcurrentSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import org.bukkit.BlockChangeDelegate;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

public class TreePopulator extends BlockPopulator {
   private static Set<TreePopulator.Coordinates> chunks = new ConcurrentSet();
   private static Set<TreePopulator.Coordinates> unpopulatedChunks = new ConcurrentSet();

   public void populate(World world, Random random, Chunk chunk) {
      int chunkX = chunk.getX();
      int chunkZ = chunk.getZ();
      TreePopulator.Coordinates chunkCoordinates = new TreePopulator.Coordinates(chunkX, chunkZ);
      if (!chunks.contains(chunkCoordinates)) {
         chunks.add(chunkCoordinates);
         unpopulatedChunks.add(chunkCoordinates);
      }

      Iterator var7 = unpopulatedChunks.iterator();

      while(var7.hasNext()) {
         TreePopulator.Coordinates unpopulatedChunk = (TreePopulator.Coordinates)var7.next();
         if (chunks.contains(unpopulatedChunk.left()) && chunks.contains(unpopulatedChunk.right()) && chunks.contains(unpopulatedChunk.above()) && chunks.contains(unpopulatedChunk.below()) && chunks.contains(unpopulatedChunk.upperLeft()) && chunks.contains(unpopulatedChunk.upperRight()) && chunks.contains(unpopulatedChunk.lowerLeft()) && chunks.contains(unpopulatedChunk.lowerRight())) {
            this.actuallyPopulate(world, random, world.getChunkAt(unpopulatedChunk.x, unpopulatedChunk.z));
            unpopulatedChunks.remove(unpopulatedChunk);
         }
      }

   }

   private void actuallyPopulate(final World world, Random random, Chunk chunk) {
      int x = random.nextInt(16);
      int z = random.nextInt(16);

      int y;
      for(y = world.getMaxHeight() - 1; y > 0 && chunk.getBlock(x, y, z).getType() == Material.AIR; --y) {
      }

      if (y > 0 && y < 255 && y >= 100 && y < 105) {
         world.generateTree(chunk.getBlock(x, y + 1, z).getLocation(), TreeType.CHORUS_PLANT, new BlockChangeDelegate() {
            public boolean setBlockData(int i, int i1, int i2, @NotNull BlockData blockData) {
               if (blockData.getMaterial() == Material.CHORUS_FLOWER) {
                  world.getBlockAt(i, i1, i2).setType(Material.SEA_LANTERN);
               } else if (blockData.getMaterial() == Material.CHORUS_PLANT) {
                  world.getBlockAt(i, i1, i2).setType(Material.END_STONE_BRICK_WALL);
               }

               return true;
            }

            @NotNull
            public BlockData getBlockData(int i, int i1, int i2) {
               return null;
            }

            public int getHeight() {
               return 255;
            }

            public boolean isEmpty(int i, int i1, int i2) {
               return false;
            }
         });
      }

   }

   private class Coordinates {
      public final int x;
      public final int z;

      public Coordinates(int x, int z) {
         this.x = x;
         this.z = z;
      }

      public TreePopulator.Coordinates left() {
         return TreePopulator.this.new Coordinates(this.x - 1, this.z);
      }

      public TreePopulator.Coordinates right() {
         return TreePopulator.this.new Coordinates(this.x + 1, this.z);
      }

      public TreePopulator.Coordinates above() {
         return TreePopulator.this.new Coordinates(this.x, this.z - 1);
      }

      public TreePopulator.Coordinates below() {
         return TreePopulator.this.new Coordinates(this.x, this.z + 1);
      }

      public TreePopulator.Coordinates upperLeft() {
         return TreePopulator.this.new Coordinates(this.x - 1, this.z - 1);
      }

      public TreePopulator.Coordinates upperRight() {
         return TreePopulator.this.new Coordinates(this.x + 1, this.z - 1);
      }

      public TreePopulator.Coordinates lowerLeft() {
         return TreePopulator.this.new Coordinates(this.x - 1, this.z + 1);
      }

      public TreePopulator.Coordinates lowerRight() {
         return TreePopulator.this.new Coordinates(this.x + 1, this.z + 1);
      }

      public int hashCode() {
         return (this.x + this.z) * (this.x + this.z + 1) / 2 + this.x;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (obj == null) {
            return false;
         } else if (this.getClass() != obj.getClass()) {
            return false;
         } else {
            TreePopulator.Coordinates other = (TreePopulator.Coordinates)obj;
            if (this.x != other.x) {
               return false;
            } else {
               return this.z == other.z;
            }
         }
      }
   }
}
