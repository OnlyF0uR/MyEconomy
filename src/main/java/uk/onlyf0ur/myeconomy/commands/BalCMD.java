package uk.onlyf0ur.myeconomy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import uk.onlyf0ur.myeconomy.EcoPlayer;
import uk.onlyf0ur.myeconomy.MyEconomy;

public class BalCMD implements CommandExecutor {
    private final MyEconomy plugin;

    public BalCMD(MyEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;
        EcoPlayer eP = new EcoPlayer(p);

        p.sendMessage(plugin.getMessage("CurrentBalance", "<balance>", String.valueOf(eP.balance())));

        return true;
    }
}
