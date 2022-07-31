package uk.onlyf0ur.myeconomy;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public record JoinListener(MyEconomy plugin) implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        EcoPlayer eP = new EcoPlayer(e.getPlayer());
        if (!eP.exists()) {
            plugin.sqLite.openConnection();
            try {
                PreparedStatement stmt = plugin.sqLite.getConnection().prepareStatement(
                        "INSERT INTO players (uuid, balance) VALUES (?, ?)"
                );
                stmt.setString(1, String.valueOf(e.getPlayer().getUniqueId()));
                stmt.setInt(2, this.plugin.fileManager.getConfig("Config.yml").get().getInt("StartBalance"));

                stmt.closeOnCompletion();
                stmt.execute();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            plugin.sqLite.closeConnection();
        }
    }
}