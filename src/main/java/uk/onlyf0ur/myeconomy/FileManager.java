package uk.onlyf0ur.myeconomy;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;

public class FileManager {

    private final JavaPlugin plugin;

    private final HashMap<String, Config> configs = new HashMap<>();

    public FileManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public Config getConfig(String name) {
        if (!configs.containsKey(name)) {
            configs.put(name, new Config(name));
        }
        return configs.get(name);
    }

    public class Config {

        private final String name;
        private File file;
        private YamlConfiguration config;

        public Config(String name) {
            this.name = name;
        }

        public void save() {
            if (this.config == null || this.file == null) {
                plugin.getLogger().log(Level.SEVERE, "Could not save file.");
                return;
            }

            try {
                this.config.save(this.file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public YamlConfiguration get() {
            if (this.config == null) {
                reload();
            }

            return this.config;
        }

        public void reload() {
            if (file == null) {
                this.file = new File(plugin.getDataFolder(), this.name);
            }

            this.config = YamlConfiguration.loadConfiguration(file);

            InputStream defStream = plugin.getResource(this.name);
            if (defStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(
                        new InputStreamReader(defStream)
                );
                this.config.setDefaults(defConfig);
            } else {
                plugin.getLogger().log(Level.SEVERE, "Failed config copy.");
            }
        }

        public void setDefaults(boolean force) {
            get().options().copyDefaults(true);
            save();
        }

        public Object get(String key) {
            return get().get(key);
        }
    }
}
