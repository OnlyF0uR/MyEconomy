package uk.onlyf0ur.myeconomy.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import uk.onlyf0ur.myeconomy.EcoPlayer;
import uk.onlyf0ur.myeconomy.MyEconomy;

public class SetbalCMD implements CommandExecutor {
    private final MyEconomy plugin;

    public SetbalCMD(MyEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("myeconomy.setbal") || sender.hasPermission("myeconomy.admin")) {
            sender.sendMessage(plugin.getMessage("NoPermission"));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(plugin.getMessage("SetbalUsage"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(plugin.getMessage("InvalidPlayer"));
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(plugin.getMessage("InvalidAmount"));
            return true;
        }

        if (amount == 0) {
            sender.sendMessage(plugin.getMessage("InvalidAmount"));
            return true;
        }

        EcoPlayer eP = new EcoPlayer(target);
        eP.set(amount);

        sender.sendMessage(plugin.getMessage("BalanceUpdated"));

        return true;
    }
}
