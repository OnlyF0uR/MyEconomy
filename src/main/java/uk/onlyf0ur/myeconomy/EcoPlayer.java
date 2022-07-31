package uk.onlyf0ur.myeconomy;

import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EcoPlayer {
    private final MyEconomy plugin = MyEconomy.getPlugin(MyEconomy.class);
    private final Player player;

    public EcoPlayer(Player player) {
        this.player = player;
    }

    public boolean exists() {
        boolean result = false;

        plugin.sqLite.openConnection();
        try {
            PreparedStatement stmt = plugin.sqLite.getConnection().prepareStatement(
                    "SELECT balance FROM players WHERE uuid = ?"
            );
            stmt.setString(1, String.valueOf(player.getUniqueId()));

            stmt.closeOnCompletion();
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        plugin.sqLite.closeConnection();

        return result;
    }

    public int balance() {
        int result = 0;

        plugin.sqLite.openConnection();
        try {
            PreparedStatement stmt = plugin.sqLite.getConnection().prepareStatement(
                    "SELECT balance FROM players WHERE uuid = ?"
            );
            stmt.setString(1, String.valueOf(player.getUniqueId()));

            stmt.closeOnCompletion();
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        plugin.sqLite.closeConnection();

        return result;
    }

    public void set(int a) {
        plugin.sqLite.openConnection();
        try {
            PreparedStatement stmt = plugin.sqLite.getConnection().prepareStatement(
                    "UPDATE players SET balance = ? WHERE uuid = ?"
            );
            stmt.setInt(1, a);
            stmt.setString(2, String.valueOf(player.getUniqueId()));

            stmt.closeOnCompletion();
            stmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        plugin.sqLite.closeConnection();
    }

    public void transfer(EcoPlayer target, int a) {
        int newBal1 = this.balance() - a;
        int newBal2 = target.balance() + a;

        this.set(newBal1);
        target.set(newBal2);
    }

}
