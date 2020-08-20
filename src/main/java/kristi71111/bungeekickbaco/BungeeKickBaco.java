package kristi71111.bungeekickbaco;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class BungeeKickBaco extends Plugin {
    public static Configuration config;
    public static ConfigurationProvider cProvider;
    public static File cFile;

    @Override
    public void onEnable() {
        {
            File cFolder = new File(this.getDataFolder(), "");

            if (!cFolder.exists()) {
                cFolder.mkdir();
            }

            cFile = new File(this.getDataFolder() + "/config.yml");

            if (!cFile.exists()) {
                try {
                    String file = "ServerName: \'lobby\'\n"
                            + "# This is where the player is kicked to. This is usually the lobby/hub server\n"
                            + "KickMessage: \'&6You have been kicked! Reason:&4 \'\n"
                            + "# Message to be sent to the player who has been kicked. This message is followed by the kick reason\n"
                            + "ShowKickMessage: True\n"
                            + "# Set this to True if you want the kicked player to be able to see the kick reason.";

                    FileWriter fw = new FileWriter(cFile);
                    BufferedWriter out = new BufferedWriter(fw);
                    out.write(file);
                    out.close();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            cProvider = ConfigurationProvider.getProvider(YamlConfiguration.class);
            try {
                config = cProvider.load(cFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.getProxy().getPluginManager().registerListener(this, new PlayerListener(this));
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
