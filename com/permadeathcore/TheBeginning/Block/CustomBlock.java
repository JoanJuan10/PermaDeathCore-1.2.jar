package com.permadeathcore.TheBeginning.Block;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockBreakEvent;

public interface CustomBlock {
   Location blockFaceToLocation(Block var1, BlockFace var2);

   void placeCustomBlock(Location var1);

   void checkForBreak(BlockBreakEvent var1);

   boolean isInfernalNetherite(Location var1);
}
