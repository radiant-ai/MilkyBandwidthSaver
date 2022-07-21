package fun.milkyway.milkybandwidthsaver;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

@CommandAlias("mbs")
public class MbsCommand extends BaseCommand {
    private final MilkyBandwidthSaver plugin;

    public MbsCommand(MilkyBandwidthSaver plugin) {
        this.plugin = plugin;
    }

    @Subcommand("profile")
    @CommandPermission("mbs.profile")
    @CommandCompletion("@players")
    public void profile(CommandSender player, String playerName) {
        if (playerName.toLowerCase(Locale.ROOT).equals("stop")) {
            plugin.setTargetPlayer(null);
            player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>[MilkyBandwidthSaver] Отладка отключена."));
            return;
        }
        var otherPlayer = Bukkit.getPlayer(playerName);
        if (otherPlayer == null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>[MilkyBandwidthSaver] <red>Игрок не найден."));
            return;
        }
        plugin.setTargetPlayer(otherPlayer.getUniqueId());
        plugin.setProfilesReceiver(player);
        player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>[MilkyBandwidthSaver] <green>Начало отладки для <white>"+otherPlayer.getName()+"<green>."));
    }

    @Subcommand("toggle")
    @CommandPermission("mbs.toggle")
    public void toggle(Player player) {
        if (plugin.isPlayerWithSaver(player.getUniqueId())) {
            plugin.removePlayerWithSaver(player.getUniqueId());
            player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>[MilkyBandwidthSaver] <red>Вы выключили гипер-экономию трафика. Перезайдите или измените дальность прорисовки клиента, чтобы изменения вступили в силу."));
            player.setSendViewDistance(8);
            return;
        }

        plugin.addPlayerWithSaver(player.getUniqueId());
        player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>[MilkyBandwidthSaver] <green>Вы включили гипер-экономию трафика."));
        player.setSendViewDistance(3);
    }
}
