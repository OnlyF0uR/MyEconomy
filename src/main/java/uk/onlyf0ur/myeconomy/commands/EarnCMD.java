package uk.onlyf0ur.myeconomy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import uk.onlyf0ur.myeconomy.EcoPlayer;
import uk.onlyf0ur.myeconomy.MyEconomy;

import java.time.Instant;
import java.util.HashMap;

public class EarnCMD implements CommandExecutor {
    private final MyEconomy plugin;
    private final HashMap<String, Long> cooldowns = new HashMap<>();

    public EarnCMD(MyEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;

        Long cTime = Instant.now().getEpochSecond();
        // Check for cooldown
        if (this.cooldowns.containsKey(p.getName())) {
            // Check if cooldown expired
            Long diff = cTime - this.cooldowns.get(p.getName());
            Long cd = this.plugin.fileManager.getConfig("Config.yml").get().getLong("EarnCooldown");
            if (diff <= cd) {
                p.sendMessage(this.plugin.getMessage("CooldownActive", "<seconds>", String.valueOf(cd - diff)));
                return true;
            }
        }
        this.cooldowns.put(p.getName(), cTime);

        EcoPlayer eP = new EcoPlayer(p);
        int newBal = eP.balance() + this.plugin.fileManager.getConfig("Config.yml").get().getInt("EarnAmount");
        eP.set(newBal);

        p.sendMessage(this.plugin.getMessage("MoneyEarned", "<balance>", String.valueOf(newBal)));

        // ...

        return true;
    }
}
