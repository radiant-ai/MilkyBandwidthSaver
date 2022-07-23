package fun.milkyway.milkybandwidthsaver;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
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
            player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[MilkyBandwidthSaver] Отладка отключена."));
            return;
        }
        var otherPlayer = Bukkit.getPlayer(playerName);
        if (otherPlayer == null) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[MilkyBandwidthSaver] <red>Игрок не найден."));
            return;
        }
        plugin.setTargetPlayer(otherPlayer.getUniqueId());
        plugin.setProfilesReceiver(player);
        player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[MilkyBandwidthSaver] <green>Начало отладки для <white>"+otherPlayer.getName()+"<green>."));
    }

    @Subcommand("toggle")
    @CommandPermission("mbs.toggle")
    public void toggle(Player player, @Optional Integer viewDistance) {
        if (plugin.isPlayerWithSaver(player.getUniqueId())) {
            plugin.removePlayerWithSaver(player.getUniqueId());
            player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[MilkyBandwidthSaver] <red>Вы выключили гипер-экономию трафика. Перезайдите или измените дальность прорисовки клиента, чтобы изменения вступили в силу."));
            player.setSendViewDistance(8);
            return;
        }

        if (viewDistance == null) {
            viewDistance = 2;
        }

        if (viewDistance < 2 || viewDistance > 6) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[MilkyBandwidthSaver] <red>Неверное значение дистанции. Укажите значение в диапазоне от 2 до 6."));
            return;
        }

        plugin.addPlayerWithSaver(player.getUniqueId(), new SavingsSettings(viewDistance));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[MilkyBandwidthSaver] <green>Вы включили гипер-экономию трафика с дальностью обзора <white>"+viewDistance+"<green> Чтобы выключить, введите команду еще раз"));
        player.setSendViewDistance(viewDistance);
    }

    @Subcommand("help")
    @Default
    @CatchUnknown
    public void help(CommandSender sender) {
        sender.sendMessage(MiniMessage.miniMessage().deserialize("<gray>[MilkyBandwidthSaver] <green>Плагин позволяет убрать много ненужных сетевых пакетов отправляемых на клиент игрокам." +
                " Для включения экономии трафика введите команду <white>/mbs toggle [дальность прогрузки]<green>."));
    }
}
