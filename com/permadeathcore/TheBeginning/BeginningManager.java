package com.permadeathcore.TheBeginning;

import com.permadeathcore.Main;
import com.permadeathcore.TheBeginning.WorldGenerator.BeginningGenerator;
import com.permadeathcore.TheBeginning.WorldGenerator.BeginningLootTable;
import com.permadeathcore.Util.Manager.Data.BeginningDataManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.EndGateway;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class BeginningManager implements Listener {
   private Main main;
   private World beginningWorld;
   private BeginningDataManager data;
   private boolean closed = false;

   public BeginningManager(Main main) {
      this.main = main;
      this.beginningWorld = null;
      this.data = main.getBeData();
      if (main.getDays() >= 40L) {
         this.generateWorld();
         main.getServer().getPluginManager().registerEvents(this, main);
      }

   }

   public World getBeginningWorld() {
      return this.beginningWorld;
   }

   private void generateWorld() {
      if (Bukkit.getWorld("pdc_the_beginning") == null) {
         WorldCreator creator = new WorldCreator("pdc_the_beginning");
         creator.environment(Environment.THE_END);
         creator.generator(new BeginningGenerator());
         creator.generateStructures(false);
         this.beginningWorld = creator.createWorld();
         if (this.main.getConfig().getBoolean("Toggles.Doble-Mob-Cap")) {
            this.beginningWorld.setMonsterSpawnLimit(140);
         }

         this.beginningWorld.setGameRule(GameRule.MOB_GRIEFING, false);
      } else {
         this.beginningWorld = Bukkit.getWorld("pdc_the_beginning");
      }

   }

   public void generatePortal(boolean overworld, Location to) {
      if (!this.data.generatedOverWorldBeginningPortal() && overworld) {
         int x = this.main.getConfig().getInt("TheBeginning.X-Limit");
         int z = this.main.getConfig().getInt("TheBeginning.Z-Limit");
         int ranX = (new Random()).nextInt(x);
         int ranZ = (new Random()).nextInt(z);
         if ((new Random()).nextBoolean()) {
            ranX *= -1;
         }

         if ((new Random()).nextBoolean()) {
            ranZ *= -1;
         }

         Location loc = new Location(this.main.world, (double)ranX, 0.0D, (double)ranZ);
         int highestBlockAt = this.main.world.getHighestBlockAt(loc).getY();
         if (highestBlockAt == -1) {
            highestBlockAt = 50;
         }

         highestBlockAt += 15;
         loc.setY((double)highestBlockAt);
         this.pasteSchematic(loc, new File(this.main.getDataFolder().getAbsolutePath() + "/schematics/beginning_portal.schem"));
         this.data.setOverWorldPortal(loc);
      }

      if (!this.data.generatedBeginningPortal() && !overworld) {
         this.beginningWorld.loadChunk(to.getChunk());
         this.pasteSchematic(to, new File(this.main.getDataFolder().getAbsolutePath() + "/schematics/beginning_portal.schem"));
         this.data.setBeginningPortal(to);
      }

   }

   private void pasteSchematic(Location loc, File schematic) {
      com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(loc.getWorld());
      ClipboardFormat format = ClipboardFormats.findByFile(schematic);

      try {
         ClipboardReader reader = format.getReader(new FileInputStream(schematic));
         Throwable var6 = null;

         try {
            Clipboard clipboard = reader.read();
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1);
            Throwable var9 = null;

            try {
               Operation operation = (new ClipboardHolder(clipboard)).createPaste(editSession).to(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ())).ignoreAirBlocks(true).build();

               try {
                  Operations.complete(operation);
                  editSession.flushSession();
               } catch (WorldEditException var39) {
                  var39.printStackTrace();
               }
            } catch (Throwable var40) {
               var9 = var40;
               throw var40;
            } finally {
               if (editSession != null) {
                  if (var9 != null) {
                     try {
                        editSession.close();
                     } catch (Throwable var38) {
                        var9.addSuppressed(var38);
                     }
                  } else {
                     editSession.close();
                  }
               }

            }
         } catch (Throwable var42) {
            var6 = var42;
            throw var42;
         } finally {
            if (reader != null) {
               if (var6 != null) {
                  try {
                     reader.close();
                  } catch (Throwable var37) {
                     var6.addSuppressed(var37);
                  }
               } else {
                  reader.close();
               }
            }

         }
      } catch (FileNotFoundException var44) {
         var44.printStackTrace();
      } catch (IOException var45) {
         var45.printStackTrace();
      }

   }

   public void closeBeginning() {
      if (this.beginningWorld != null) {
         this.beginningWorld.getPlayers().forEach((p) -> {
            p.teleport(this.main.world.getSpawnLocation());
            p.playSound(p.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 1.0F, 1.0F);
         });
         Main var10000 = this.main;
         StringBuilder var1 = new StringBuilder();
         Main var10001 = this.main;
         Bukkit.broadcastMessage(Main.format(var1.append(Main.tag).append("&eThe Beginning ha cerrado temporalmente (DeathTrain).").toString()));
         this.closed = true;
      }
   }

   @EventHandler
   public void onPlayerPortal(PlayerPortalEvent e) {
      Main var10000 = this.main;
      if (!Main.isRunningPaperSpigot()) {
         Player p = e.getPlayer();
         if (e.getCause() == TeleportCause.END_GATEWAY) {
            if (p.getWorld().getName().equalsIgnoreCase(this.main.world.getName())) {
               try {
                  e.getClass().getDeclaredMethod("setCanCreatePortal", Boolean.class).invoke(e, false);
               } catch (Exception var5) {
               }
            }

            if (p.getWorld().getName().equalsIgnoreCase(this.beginningWorld.getName())) {
               if (p.getLocation().getBlock().getState() instanceof EndGateway) {
                  EndGateway gateway = (EndGateway)p.getLocation().getBlock().getState();
                  gateway.setExitLocation((Location)null);
                  gateway.update();
                  p.getLocation().getBlock().getState().update();
               }

               try {
                  e.getClass().getDeclaredMethod("setCanCreatePortal", Boolean.class).invoke(e, false);
               } catch (Exception var4) {
               }
            }

         }
      }
   }

   @EventHandler
   public void onTeleport(final PlayerTeleportEvent e) {
      Player p = e.getPlayer();
      if (e.getCause() == TeleportCause.END_GATEWAY) {
         if (this.isClosed()) {
            e.setCancelled(true);
         } else if (this.main.getDays() >= 50L) {
            if (p.getWorld().getName().equalsIgnoreCase(this.main.world.getName())) {
               Player var6 = e.getPlayer();
               Main var10001 = this.main;
               var6.sendMessage(Main.format("&eBienvenido a The Beginning."));
               e.getPlayer().teleport(this.beginningWorld.getSpawnLocation());
               e.setCancelled(true);
               Bukkit.getScheduler().runTaskLater(this.main, new Runnable() {
                  public void run() {
                     e.getPlayer().teleport(BeginningManager.this.beginningWorld.getSpawnLocation());
                  }
               }, 20L);
            }

            int x = (int)p.getLocation().getX();
            int z = (int)p.getLocation().getZ();
            if (p.getWorld().getName().equalsIgnoreCase(this.beginningWorld.getName()) && x != 200 && z != 200) {
               if (p.getLocation().getBlock().getState() instanceof EndGateway) {
                  EndGateway gateway = (EndGateway)p.getLocation().getBlock().getState();
                  gateway.setExitLocation((Location)null);
                  gateway.update();
                  p.getLocation().getBlock().getState().update();
               }

               e.getPlayer().teleport(this.main.world.getSpawnLocation(), TeleportCause.PLUGIN);
               e.setCancelled(true);
            }

         } else {
            if (e.getPlayer().getWorld().getName().equalsIgnoreCase(this.main.world.getName()) || e.getPlayer().getWorld().getName().equalsIgnoreCase(this.beginningWorld.getName())) {
               e.getPlayer().setNoDamageTicks(e.getPlayer().getMaximumNoDamageTicks());
               e.getPlayer().damage(e.getPlayer().getHealth() + 1.0D, (Entity)null);
               e.getPlayer().setNoDamageTicks(0);
               Main var10000 = this.main;
               Bukkit.broadcastMessage(Main.format("&c&lEl jugador &4&l" + e.getPlayer().getName() + " &c&lentró a TheBeginning antes de tiempo."));
            }

         }
      }
   }

   @EventHandler
   public void onBreak(BlockBreakEvent e) {
      Player p = e.getPlayer();
      if (p.getWorld().getName().equalsIgnoreCase(this.beginningWorld.getName()) && e.getBlock().getState() instanceof Chest) {
         Chest chest = (Chest)e.getBlock().getState();
         this.populateChest(chest);
      }

      if (e.getBlock().getType() == Material.SPAWNER && this.main.getNetheriteBlock() != null && this.main.getDays() < 60L) {
         this.main.getNetheriteBlock().checkForBreak(e);
      }

   }

   @EventHandler
   public void onInteract(PlayerInteractEvent e) {
      Player p = e.getPlayer();
      if (e.getClickedBlock() != null && p.getWorld().getName().equalsIgnoreCase(this.beginningWorld.getName()) && (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getState() instanceof Chest) {
         Chest chest = (Chest)e.getClickedBlock().getState();
         this.populateChest(chest);
      }

      if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getPlayer().getInventory().getItemInMainHand() != null && e.getClickedBlock() != null && e.getBlockFace() != null && this.main.getNetheriteBlock() != null) {
         if (e.getClickedBlock().getType() == Material.CHEST) {
            return;
         }

         ItemStack ih = e.getPlayer().getInventory().getItemInMainHand();
         if (ih.getType() == Material.DIAMOND && ih.getItemMeta().isUnbreakable()) {
            this.main.getNetheriteBlock().placeCustomBlock(this.main.getNetheriteBlock().blockFaceToLocation(e.getClickedBlock(), e.getBlockFace()));
            if (ih.getAmount() > 0) {
               ih.setAmount(ih.getAmount() - 1);
            } else {
               ih = null;
            }

            e.getPlayer().getInventory().setItemInMainHand(ih);
            e.getPlayer().updateInventory();
         }
      }

   }

   @EventHandler
   public void onCreatePortal(PortalCreateEvent e) {
      if (e.getWorld().getName().equalsIgnoreCase(this.beginningWorld.getName())) {
         Iterator var2 = e.getBlocks().iterator();

         while(true) {
            BlockState s;
            Block b;
            do {
               if (!var2.hasNext()) {
                  e.setCancelled(true);
                  return;
               }

               s = (BlockState)var2.next();
               b = s.getBlock();
            } while(b.getType() != Material.END_GATEWAY && b.getType() != Material.BEDROCK && !(s instanceof EndGateway));

            if (b.getChunk().getX() == 0 && b.getChunk().getZ() == 0) {
               e.getBlocks().remove(s);
               s.setType(Material.AIR);
            }
         }
      }
   }

   @EventHandler
   public void onBucket(PlayerBucketEmptyEvent e) {
      if (this.beginningWorld != null) {
         if (e.getPlayer().getWorld().getName().equalsIgnoreCase(this.beginningWorld.getName())) {
            e.setCancelled(true);
         }

      }
   }

   @EventHandler
   public void onWater(BlockDispenseEvent e) {
      if (this.beginningWorld != null) {
         if (e.getItem() != null && (e.getItem().getType() == Material.BUCKET || e.getItem().getType() == Material.WATER_BUCKET) && e.getBlock().getWorld().getName().equalsIgnoreCase(this.beginningWorld.getName())) {
            e.setCancelled(true);
         }

      }
   }

   @EventHandler
   public void onSpawnerSpawn(SpawnerSpawnEvent e) {
      if (this.beginningWorld != null) {
         if (!e.isCancelled()) {
            CreatureSpawner spawner = e.getSpawner();
            if (e.getEntity().getWorld().getName().equalsIgnoreCase(this.beginningWorld.getName())) {
               if (e.getEntityType() != EntityType.ARMOR_STAND) {
                  spawner.setSpawnedType(e.getEntityType());
                  spawner.update();
               }

               if (e.getEntityType() == EntityType.WITHER) {
                  Wither w = (Wither)e.getEntity();
                  w.setRemoveWhenFarAway(true);
               }

               Main var10001;
               if (e.getEntityType() == EntityType.GHAST) {
                  Ghast g = (Ghast)this.main.getNmsHandler().spawnCustomGhast(e.getLocation().add(0.0D, 5.0D, 0.0D), SpawnReason.CUSTOM, true);
                  var10001 = this.main;
                  g.setCustomName(Main.format("&6Ender Ghast Definitivo"));
                  this.main.getNmsAccesor().setMaxHealth(g, 150.0D, true);
                  e.setCancelled(true);
               }

               if (e.getEntityType() == EntityType.CREEPER) {
                  Entity var10000 = e.getEntity();
                  var10001 = this.main;
                  var10000.setCustomName(Main.format("&6Quantum Creeper"));
                  ((Creeper)e.getEntity()).setPowered(true);
                  e.getEntity().getPersistentDataContainer().set(new NamespacedKey(this.main, "quantum_creeper"), PersistentDataType.BYTE, (byte)1);
               }
            }

         }
      }
   }

   @EventHandler
   public void onPlace(BlockPlaceEvent e) {
      if (this.beginningWorld != null) {
         if (e.getPlayer().getWorld().getName().equalsIgnoreCase(this.beginningWorld.getName()) && e.getBlock().getState() instanceof Chest) {
            BeginningDataManager ma = this.main.getBeData();
            ma.addPopulatedChest(e.getBlock().getLocation());
         }

      }
   }

   private void populateChest(Chest chest) {
      if (this.data.getConfig().contains("PopulatedChests")) {
         if (this.data.hasPopulatedChest(chest.getLocation())) {
            return;
         }

         if (this.main.getDays() < 60L) {
            (new BeginningLootTable(this)).populateChest(chest);
         }

         this.data.addPopulatedChest(chest.getLocation());
      }

   }

   public boolean isClosed() {
      return this.closed;
   }

   public void setClosed(boolean closed) {
      this.closed = closed;
   }
}
