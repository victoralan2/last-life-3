package net.olimpium.last_life_iii.mecanicas;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.ChunkCoordIntPair;
import com.comphenix.protocol.wrappers.MultiBlockChangeInfo;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import net.minecraft.server.v1_16_R3.PacketPlayOutMultiBlockChange;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AntiXRay implements Listener {

    public boolean isVisible(Block block){
        List<Block> collindantBlocks = new ArrayList<>();
        collindantBlocks.add(block.getWorld().getBlockAt(block.getLocation().clone().add(0,1,0)));
        collindantBlocks.add(block.getWorld().getBlockAt(block.getLocation().clone().add(0,-1,0)));
        collindantBlocks.add(block.getWorld().getBlockAt(block.getLocation().clone().add(0,0,1)));
        collindantBlocks.add(block.getWorld().getBlockAt(block.getLocation().clone().add(0,0,-1)));
        collindantBlocks.add(block.getWorld().getBlockAt(block.getLocation().clone().add(1,0,0)));
        collindantBlocks.add(block.getWorld().getBlockAt(block.getLocation().clone().add(-1,0,0)));
        for (Block collindantBlock : collindantBlocks){
            if (collindantBlock.getType().equals(Material.LAVA)){
                return false;
            }
            if (!collindantBlock.getType().isOccluding()){
                return true;
            }
        }
        return false;
    }

    public List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }


    public short setShortLocation(int x, int y, int z) {
        //Convert to location within chunk.
        x = x & 0xF;
        y = y & 0xF;
        z = z & 0xF;
        //Creates position from location within chunk
        return (short) (x << 8 | z << 4 | y << 0);
    }

    List<Material> oreList = Arrays.asList(Material.DIAMOND_ORE, Material.GOLD_ORE, Material.ANCIENT_DEBRIS, Material.GOLD_ORE, Material.IRON_ORE, Material.REDSTONE_ORE, Material.LAPIS_ORE, Material.COAL_ORE, Material.EMERALD_ORE, Material.INFESTED_STONE, Material.CHEST, Material.MOSSY_COBBLESTONE, Material.NETHER_QUARTZ_ORE, Material.NETHER_GOLD_ORE);

    @Deprecated
    public void onPlayerLoadChunk(PlayerSwapHandItemsEvent e){
        ProtocolManager pm = ProtocolLibrary.getProtocolManager();



        Bukkit.broadcastMessage("GOT HERE1");
        PacketContainer packet = pm
                .createPacket(PacketType.Play.Server.MULTI_BLOCK_CHANGE);
        Chunk chunk = e.getPlayer().getLocation().getChunk();
        Bukkit.broadcastMessage("GOT HERE2");

        ChunkCoordIntPair chunkcoords = new ChunkCoordIntPair(chunk.getX(),
                chunk.getZ());
        Bukkit.broadcastMessage("GOT HERE3");

        List<MultiBlockChangeInfo> changes = new ArrayList<>();
        Bukkit.broadcastMessage("GOT HERE4");

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 16; y++) {
                    Bukkit.broadcastMessage("GOT HERE");
                    Block block = chunk.getBlock(x, y, z);
                    MultiBlockChangeInfo change = new MultiBlockChangeInfo(
                            new Location(e.getPlayer().getWorld(), x ,y ,z),
                            WrappedBlockData.createData(Material.DIAMOND_ORE)
                    );
                    changes.add(change);
                }
            }
        }

        //.getChunkCoordIntPairs().write(0, chunkcoords);
        //packet.getMultiBlockChangeInfoArrays().write(0, changes.toArray(new MultiBlockChangeInfo[0]));


        try {
           pm.sendServerPacket(e.getPlayer(), packet);
            Bukkit.broadcastMessage("sent");
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }




/*
        if (e.getPlayer().getScoreboardTags().contains("Trusted")) return;
        if (e.getPlayer().getLocation().getBlockY() >= 60) return;
        new BukkitRunnable(){
            @Override
            public void run() {
                if(e.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                    for (int y = 2; y < 64; y++) {
                        for (int x = 0; x < 16; x++) {
                            for (int z = 0; z < 16; z++) {
                                if (e.getPlayer().getScoreboardTags().contains("Trusted")) return;
                                if (e.getPlayer().getLocation().getBlockY() >= 62) return;
                                Block currentBlock = e.getChunk().getBlock(x, y, z);
                                if (oreList.contains(currentBlock.getType())) {
                                    //if (isVisible(currentBlock)) continue;
                                    for (Player player : Bukkit.getOnlinePlayers()) {

                                        player.sendBlockChange(e.getChunk().getBlock(x, y, z).getLocation(), Material.STONE.createBlockData());

                                    }
                                }
                            }
                        }
                    }
                } else if (e.getWorld().getEnvironment().equals(World.Environment.NETHER)){
                    for (int y = 0; y < 128; y++) {
                        for (int x = 0; x < 16; x++) {
                            for (int z = 0; z < 16; z++) {

                                Block currentBlock = e.getChunk().getBlock(x, y, z);
                                if (oreList.contains(currentBlock.getType())) {
                                    if (isVisible(currentBlock)) continue;
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        //Bukkit.broadcastMessage(y+"");
                                        player.sendBlockChange(e.getChunk().getBlock(x, y, z).getLocation(), Material.NETHERRACK.createBlockData());
                                    }
                                }
                                if (y >= 28 && y<96){
                                    y = 96;
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskLaterAsynchronously(Last_life_III.getPlugin(), 1);*/
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if (e.getPlayer().getScoreboardTags().contains("Trusted")) return;
        if (e.getPlayer().getLocation().getBlockY() >= 62) return;
        if (
                e.getFrom().getBlockX() == e.getTo().getBlockX() &&
                e.getFrom().getBlockY() == e.getTo().getBlockY() &&
                e.getFrom().getBlockZ() == e.getTo().getBlockZ()
        ) return;

        List<Block> nearbyBlocks = getNearbyBlocks(e.getTo(), 20);
        for (Block block : nearbyBlocks){
            if (oreList.contains(block.getType())){
                if (isVisible(block)){
                    e.getPlayer().sendBlockChange(block.getLocation(), block.getBlockData());
                }
            }
        }


    }

    @Deprecated
    public void onPlayerBreack(BlockBreakEvent e){
        if (e.getPlayer().getScoreboardTags().contains("Trusted")) return;
        if (e.getPlayer().getLocation().getBlockY() >= 62) return;
        List<Block> collindantBlocks = new ArrayList<>();
        collindantBlocks.add(e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation().clone().add(0,1,0)));
        collindantBlocks.add(e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation().clone().add(0,-1,0)));
        collindantBlocks.add(e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation().clone().add(0,0,1)));
        collindantBlocks.add(e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation().clone().add(0,0,-1)));
        collindantBlocks.add(e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation().clone().add(1,0,0)));
        collindantBlocks.add(e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation().clone().add(-1,0,0)));
        for (Block block : collindantBlocks){
            if (oreList.contains(block.getType())){
                e.getPlayer().sendBlockChange(block.getLocation(), block.getBlockData());
            }
        }
    }
}
