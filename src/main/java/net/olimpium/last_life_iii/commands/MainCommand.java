package net.olimpium.last_life_iii.commands;

import net.olimpium.last_life_iii.discordBot.Bot;
import net.olimpium.last_life_iii.items.LastItem;
import net.olimpium.last_life_iii.utils.TimeSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

public class MainCommand implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            System.out.println(ChatColor.YELLOW +"[" + ChatColor.GOLD + "LAST LIFE"+ ChatColor.YELLOW + "]" + ChatColor.RED + "This command is only for players");
            return true;
        }
        Player sender = (Player)commandSender;
        if (sender.isOp()) {
            if (args.length == 0) {
                sendErrorMessage(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("team")) {
                SubCommands.CommandTeam(args, sender);
            /*} else if (args[0].equalsIgnoreCase("help")) {
                player.sendMessage(ChatColor.DARK_AQUA + "----------------------------------");
                player.sendMessage(ChatColor.GREEN + "/lastlife help" + ChatColor.LIGHT_PURPLE + "  =>  show's this list");
                player.sendMessage(ChatColor.GREEN + "/lastlife team [create/add] <teamname> [player]" + ChatColor.LIGHT_PURPLE + "  =>  the manager for last life teams");
                player.sendMessage(ChatColor.DARK_AQUA + "----------------------------------");*/
            } else if (args[0].equalsIgnoreCase("start")) {
                SubCommands.CommandStart(sender);
            } else if (args[0].equalsIgnoreCase("close")) {
                if (TimeSystem.Week != 0) {
                    if (!TimeSystem.getIsInMaintenance()) {
                        sender.sendMessage(ChatColor.YELLOW + "LAST LIFE HAS BEEN CLOSED TO PUBLIC");
                        try {
                            if (args.length == 2) {
                                String MTime = args[1];
                                Bot.serverMaintenance(true,  Integer.parseInt(MTime));
                            } else {
                                Bot.serverMaintenance(true,null);
                            }
                        } catch (NumberFormatException exception){
                            sender.sendMessage("Incorrect time");
                        }

                        TimeSystem.setIsInMaintenance(true);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.isOp()) continue;

                            if (args.length == 2) {
                                String MTime = args[1];
                                player.kickPlayer(ChatColor.RED + "EL SERVIDOR ESTA EN MANTENIMIENTO" + "\n" + ChatColor.GOLD + "TIEMPO ESPERADO: " + ChatColor.BOLD + MTime + " MINUTOS" + ChatColor.YELLOW + ChatColor.ITALIC + "\nConsulte #anuncios para poder saber más información.");
                            } else {
                                player.kickPlayer(ChatColor.RED + "EL SERVIDOR ESTA EN MANTENIMIENTO\n" + ChatColor.YELLOW + " Consulte #anuncios para saber más información.");
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + "The server is already closed");
                    }
                }
            } else if (args[0].equalsIgnoreCase("open")) {
                if (TimeSystem.Week != 0) {
                    if (TimeSystem.getIsInMaintenance()) {
                        sender.sendMessage(ChatColor.GREEN + "LAST LIFE HAS BEEN OPENED");
                        Bot.serverMaintenance(false, null);
                        TimeSystem.setIsInMaintenance(false);

                    } else {
                        sender.sendMessage(ChatColor.YELLOW + "The server is not closed ");
                    }
                }
            } else if (args[0].equalsIgnoreCase("testinventory")){
                Inventory inventory = Bukkit.createInventory(null, 9);
                Bukkit.getPlayer("mariogandia").openInventory(inventory);
                Bukkit.getPlayer("TheDracon_").openInventory(inventory);
            } else if (args[0].equalsIgnoreCase("trustadd")) {
                if (args[1]!= null){
                    Bukkit.getPlayer(args[1]).getScoreboardTags().add("Trusted");
                }
            } else if (args[0].equalsIgnoreCase("trustrem")) {
                if (args[1] != null) {
                    Bukkit.getPlayer(args[1]).getScoreboardTags().remove("Trusted");
                }
            }else if(args[0].equalsIgnoreCase("help")){
                sender.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+ChatColor.BOLD+"You may provide these arguments:");
                sender.sendMessage("/game start"+ChatColor.GRAY+" - Runs Last Life III PROJECT.");
                sender.sendMessage("/game close <minutes>"+ChatColor.GRAY+" - Closes the server, useful for maintenance.");
                sender.sendMessage("/game open"+ChatColor.GRAY+" - Reopens the server, the counterpart of "+ChatColor.ITALIC+"/game close.");
                sender.sendMessage("/game trustadd <Player Name>"+ChatColor.GRAY+" - Adds the Trusted tag to a player, AntiCheat functionality.");
                sender.sendMessage("/game trustrem  <Player Name>"+ChatColor.GRAY+" - Removes the Trusted tag of the player.");
                sender.sendMessage("/game team"+ChatColor.GRAY+" - Needs further description.");

            }else if(args[0].equalsIgnoreCase("give")){
                for (LastItem value : LastItem.values()){
                    if (args[1].equalsIgnoreCase(value.toString())){
                        if(args[2]!=null) {
                            for (int i = 0; i < Integer.parseInt(args[2]); i++) {
                                sender.getInventory().addItem(value.getItemStack());
                            }
                        }
                    }
                }
            }else{
                sender.sendMessage(ChatColor.DARK_AQUA + "----------------------------------");
                sender.sendMessage(ChatColor.GREEN + "use </game help>");
                sender.sendMessage(ChatColor.DARK_AQUA + "----------------------------------");
            }
        }
        return true;
    }
    @EventHandler
    public void onPlayerTryToJoin(PlayerJoinEvent event){
        if (TimeSystem.getIsInMaintenance()) {
            if (!event.getPlayer().isOp()){
                event.getPlayer().kickPlayer(ChatColor.RED + "EL SERVIDOR ESTA ACTUALMENTE EN MANTENIMIENTO\n"+ChatColor.YELLOW+"Consulte #anuncios para saber más información.");
            }
        }
    }
    public static void sendErrorMessage(Player player){
        player.sendMessage(ChatColor.DARK_AQUA + "----------------------------------");
        player.sendMessage(ChatColor.GREEN + "use </game help>");
        player.sendMessage(ChatColor.DARK_AQUA + "----------------------------------");

    }
}
