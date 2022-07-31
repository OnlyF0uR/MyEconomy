package uk.onlyf0ur.myeconomy.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import uk.onlyf0ur.myeconomy.EcoPlayer;
import uk.onlyf0ur.myeconomy.MyEconomy;

public class PayCMD implements CommandExecutor {
    private final MyEconomy plugin;

    public PayCMD(MyEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(plugin.getMessage("PayUsage"));
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

        Player p = (Player) sender;

        EcoPlayer payer = new EcoPlayer(p);
        int prevBalance = payer.balance();
        if (prevBalance < amount) {
            sender.sendMessage(plugin.getMessage("InsufficientFunds"));
            return true;
        }
        payer.set(prevBalance - amount);

        EcoPlayer receiver = new EcoPlayer(target);
        receiver.set(receiver.balance() + amount);

        sender.sendMessage(plugin.getMessage("FundsWired"));

        return true;
    }
}
