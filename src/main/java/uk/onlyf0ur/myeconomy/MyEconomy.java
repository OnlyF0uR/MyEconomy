package uk.onlyf0ur.myeconomy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import uk.onlyf0ur.myeconomy.commands.BalCMD;
import uk.onlyf0ur.myeconomy.commands.EarnCMD;
import uk.onlyf0ur.myeconomy.commands.PayCMD;
import uk.onlyf0ur.myeconomy.commands.SetbalCMD;

import java.sql.SQLException;
import java.sql.Statement;

public class MyEconomy extends JavaPlugin {
    public FileManager fileManager = new FileManager(this);
    public SQLite sqLite = new SQLite(this, "data");

    public void onEnable() {
        this.fileManager.getConfig("Config.yml").setDefaults(true);

        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);

        this.getCommand("bal").setExecutor(new BalCMD(this));
        this.getCommand("earn").setExecutor(new EarnCMD(this));
        this.getCommand("pay").setExecutor(new PayCMD(this));
        this.getCommand("setbal").setExecutor(new SetbalCMD(this));

        this.sqLite.openConnection();
        if (this.sqLite.tableDoesNotExist("players")) {
            try {
                Statement stmt = this.sqLite.getConnection().createStatement();

                stmt.closeOnCompletion();
                stmt.execute("CREATE TABLE players (uuid VARCHAR(32) NOT NULL PRIMARY KEY, balance INTEGER NOT NULL)");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        this.sqLite.closeConnection();
    }

    public void onDisable() {
        this.sqLite.handleShutdown();
    }


    public Component getMessage(String path) {
        return buildColourComponent(this.fileManager.getConfig("Config.yml").get().getString("Text." + path));
    }


    public Component getMessage(String path, String plch, String value) {
        return buildColourComponent(this.fileManager.getConfig("Config.yml").get().getString("Text." + path)
                .replaceAll(plch, value));
    }

    public Component buildColourComponent(String plaintext) {
        // --#fffff--Hello --#11111--World!
        if (!plaintext.contains("--")) {
            return Component.text(plaintext);
        }

        Component result = Component.text("");

        String[] ptArray = plaintext.split("--");
        String activeColour = null;
        for (String pt : ptArray) {
            if (pt.length() == 7 && pt.contains("#")) {
                activeColour = pt;
            } else {
                if (activeColour == null) {
                    result = result.append(Component.text(pt).decoration(TextDecoration.ITALIC, false));
                } else {
                    result = result.append(Component.text(pt).color(TextColor.fromHexString(activeColour)));
                }
            }
        }

        return result;
    }

}
