/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.lolnet.james137137.lolnettwitchaddonbc;

import com.google.common.io.ByteStreams;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import twitter4j.Status;

/**
 *
 * @author James
 */
public class LolnetTwitchAddonBC extends Plugin {
    
    private static final int delay = 60;
    private static LolnetTwitchAddonBC plugin;
    boolean run, isSafe;
    Date lastTweetDate = null;
    TwitterAPI twitterAPI;
    private static Configuration config;
    Config myconfig;
    
    @Override
    public void onEnable() {
        plugin = this;
        
        setupConfigFile();
        myconfig = new Config(config);
        
        twitterAPI = new TwitterAPI();
        getLatestDateForce();
        run = true;
        isSafe = true;
        startChecking();
    }
    
    @Override
    public void onDisable() {
        run = false;
        isSafe = false;
    }

    //This should only run on startup only
    public void getLatestDateForce() {
        List<Status> latestStatus = twitterAPI.getLatestStatus();
        if (!latestStatus.isEmpty()) {
            lastTweetDate = latestStatus.get(0).getCreatedAt();
        } else {
            lastTweetDate = new Date(System.currentTimeMillis());
        }
    }
    
    private void startChecking() {
        getProxy().getScheduler().runAsync(this, new Runnable() {
            
            @Override
            public void run() {
                while (run) {
                    while (isSafe) {
                        try {
                            
                            List<Status> latestStatus = twitterAPI.getLatestStatus();
                            if (!latestStatus.isEmpty()) {
                                Date lastTweet = latestStatus.get(0).getCreatedAt();
                                if (lastTweet.after(lastTweetDate)) {
                                    lastTweetDate = lastTweet;
                                    plugin.getProxy().broadcast(ChatColor.GOLD + "[" + ChatColor.DARK_RED + "Broadcast" + ChatColor.GOLD + "] "
                                            + ChatColor.GREEN + latestStatus.get(0).getText());
                                }
                            }
                            
                            Thread.sleep(delay * 1000);
                        } catch (InterruptedException ex) {
                            isSafe = false;
                            Logger.getLogger(LolnetTwitchAddonBC.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    try {
                        Thread.sleep(300000);
                        isSafe = true;
                    } catch (InterruptedException ex) {
                        Logger.getLogger(LolnetTwitchAddonBC.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }
            
        });
    }
    
    private void setupConfigFile() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                if (getResourceAsStream("config.yml") == null) {
                    System.out.println("Failed to obtain config.yml from getResourceAsStream(\"config.yml\")");
                }
                try (InputStream is = getResourceAsStream("config.yml");
                        OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(is, os);
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }
        try {
            LolnetTwitchAddonBC.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException ex) {
            Logger.getLogger(LolnetTwitchAddonBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        saveConfig();
    }
    
    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            Logger.getLogger(LolnetTwitchAddonBC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
