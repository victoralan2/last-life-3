package net.olimpium.last_life_iii;

import net.olimpium.last_life_iii.Enchants.precisionEnchant;
import net.olimpium.last_life_iii.HealthSystem.LifeSystem;
import net.olimpium.last_life_iii.Teams.TeamsManager;
import net.olimpium.last_life_iii.advancements.AdvancementManager;
import net.olimpium.last_life_iii.commands.CrashCommand;
import net.olimpium.last_life_iii.commands.LinkCommand;
import net.olimpium.last_life_iii.commands.MainCommand;
import net.olimpium.last_life_iii.commands.MainCommandCompleter;
import net.olimpium.last_life_iii.discordBot.Bot;
import net.olimpium.last_life_iii.Items.CraftingMaps;
import net.olimpium.last_life_iii.Items.MidasSword;
import net.olimpium.last_life_iii.Items.PicoDeMidas;
import net.olimpium.last_life_iii.Items.Totems.TotemDeReclamo;
import net.olimpium.last_life_iii.Items.TreasureShovel;
import net.olimpium.last_life_iii.mecanicas.*;
import net.olimpium.last_life_iii.mobs.*;
import net.olimpium.last_life_iii.mobs.MiniBosses.StarCollapser;
import net.olimpium.last_life_iii.utils.*;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

public final class Last_life_III extends JavaPlugin {
    private static Last_life_III plugin;
    public static precisionEnchant precisionEnchant;
    public static String botToken = "MTAwODcxNTk3NzQwOTcwODEzMw.GH0g2f.U4WC1Zqcw9z4YyGnufSWp5yqL4Mt3TNsgN174s";
    private static String ip;
    @Override
    public void onEnable() {
        //PLUGIN STUFF
        plugin = this;
        ip = this.getServer().getIp();

        //verification system
        getServer().getPluginManager().registerEvents(new VerificationSystem(), this);

        //Bot
        try {
            Bot.start();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        getServer().getPluginManager().registerEvents(new Bot(), this);


        //FABRIC MOD
        getServer().getMessenger().registerOutgoingPluginChannel(this, "lastlife:packet");
        getServer().getMessenger().registerIncomingPluginChannel(this, "lastlife:packet", new fabricMod());

        getServer().getPluginManager().registerEvents(new fabricMod(), this);

        //commands
        getPlugin().getCommand("LL3").setExecutor(new MainCommand());
        getPlugin().getCommand("LL3").setTabCompleter(new MainCommandCompleter());
        getPlugin().getCommand("link").setExecutor(new LinkCommand());
        getPlugin().getCommand("crash").setExecutor(new CrashCommand());
        MidasSword.init();

        // Health
        getServer().getPluginManager().registerEvents(new LifeSystem(), this);


        //items
        getServer().getPluginManager().registerEvents(new TreasureShovel(), this);
        getServer().getPluginManager().registerEvents(new PicoDeMidas(), this);
        getServer().getPluginManager().registerEvents(new MidasSword(), this);

        //getServer().getPluginManager().registerEvents(new InhibidorTemporal(), this);
        //InhibidorTemporal.Runnable();
        getServer().getPluginManager().registerEvents(new TotemDeReclamo(), this);


        //mobs
        getServer().getPluginManager().registerEvents(new RandomMobNumber(), this);
        getServer().getPluginManager().registerEvents(new Escorpiones(), this);
        getServer().getPluginManager().registerEvents(new FireworkCreepers(), this);
        getServer().getPluginManager().registerEvents(new OverheatedCreeper(),this);
        //getServer().getPluginManager().registerEvents(new MobBomb(), this);
        getServer().getPluginManager().registerEvents(new StarCollapser(), this);
        Catalyzer.Schelduler(this);
        getServer().getPluginManager().registerEvents(new Catalyzer(), this);
        getServer().getPluginManager().registerEvents(new TankArcher(),this);


        //mecanicas
        getServer().getPluginManager().registerEvents(new NightPass(), this);
        getServer().getPluginManager().registerEvents(new CreeperExplodeWhenFire(), this);
        getServer().getPluginManager().registerEvents(new ShieldBlock(), this);
        getServer().getPluginManager().registerEvents(new BatDropsMoreEXP(), this);
        getServer().getPluginManager().registerEvents(new CropsNerf(), this);
        getServer().getPluginManager().registerEvents(new NetherHeat(), this);
        getServer().getPluginManager().registerEvents(new VillagerDisabler(), this);
        NetherHeat.BukkitSchedulerNetherCheck();
        Screamer.screemerRunable();

        //enchants
        precisionEnchant = new precisionEnchant("precision"); registerEnchantment(precisionEnchant);

        //utils
        getServer().getPluginManager().registerEvents(new RenameCanceller(), this);
        getServer().getPluginManager().registerEvents(new SandLocationSaver(), this);
        getServer().getPluginManager().registerEvents(new CraftingMaps(), this);
        getServer().getPluginManager().registerEvents(new MobSpawnerCanceller(), this);


        //teams
        TeamsManager.loadTeams();

        //advancements
        AdvancementManager.createBaseAdvancements();
        //OverheatedKillAdvancement.create();
        //OverheatedKill10TimesAdv.create();
        AdvancementManager.registerAdvancements();


        //time system
        try {
            File file = new File(getDataFolder() + "\\Time.json");
            System.out.println(file.exists()+"");
            if (file.exists()){
                JSONParser jsonParser = new JSONParser();
                FileReader reader = new FileReader(file.getPath());
                Object obj = jsonParser.parse(reader);
                JSONObject jsonObject = (JSONObject) obj;
                System.out.println(TimeSystem.FixedTime+"");
                if (!jsonObject.isEmpty()){
                    TimeSystem.Week = TimeSystem.getWeek();
                    TimeSystem.FixedTime = TimeSystem.getFixedTime();
                    TimeSystem.inMaintenance = TimeSystem.getIsInMaintenance();
                    getServer().getPluginManager().registerEvents(new MainCommand(), this);
                    System.out.println(TimeSystem.FixedTime+"");
                    if (TimeSystem.FixedTime != 0){
                        System.out.println("not 0");
                        TimeSystem.WeekScheduler();
                    }
                }

            }
        } catch (Exception ignore){

        }

    }

    @Override
    public void onDisable() {
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, "fabric:fabric");
        try {
            TeamsManager.saveTeams();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bot.shutDown();

        //enchantmnets
        precisionEnchant = new precisionEnchant("precision"); unrRegisterEnchantment(precisionEnchant);
    }



    public static Last_life_III getPlugin(){
        return plugin;
    }

    public static String getIp(){
        return ip;
    }

    public static String getBotToken(){
        return botToken;
    }

    public void unrRegisterEnchantment(Enchantment enchant){
        try {
            Field keyField = Enchantment.class.getDeclaredField("byKey");

            keyField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) keyField.get(null);

            if(byKey.containsKey(enchant.getKey())) {
                byKey.remove(enchant.getKey());
            }
            Field nameField = Enchantment.class.getDeclaredField("byName");

            nameField.setAccessible(true);
            @SuppressWarnings("unchecked")
            HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nameField.get(null);

            if(byName.containsKey(enchant.getName())) {
                byName.remove(enchant.getName());
            }
        } catch (Exception ignored) { }
    }

    public static void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            registered = false;
            e.printStackTrace();
        }
        if(registered){
            // It's been registered!
        }
    }

}