package uk.onlyf0ur.myeconomy;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class SQLite {
    private final MyEconomy plugin;
    private final String dbFile;
    private Connection connection;

    public SQLite(MyEconomy plugin, String dbFile) {
        this.plugin = plugin;
        this.dbFile = dbFile;
    }

    public void openConnection() {
        File dataFolder = new File(this.plugin.getDataFolder(), this.dbFile + ".db");
        if (!dataFolder.exists()) {
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                this.plugin.getLogger().log(Level.SEVERE, "File write error: " + this.dbFile + ".db");
            }
        }

        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);

            this.connection.setAutoCommit(true);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleShutdown() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.closeConnection();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public boolean tableDoesNotExist(String tableName) {
        try {
            ResultSet rs = this.connection.getMetaData().getTables(null, null, tableName, null);

            if (rs.next()) {
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

}
