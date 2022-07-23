package fun.milkyway.milkybandwidthsaver;

import com.destroystokyo.paper.event.player.PlayerClientOptionsChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public record PlayerSettingsListener(MilkyBandwidthSaver plugin) implements Listener {

    @EventHandler
    public void playerChangeSettings(PlayerClientOptionsChangeEvent event) {
        var player = event.getPlayer();

        if (!plugin.isPlayerWithSaver(player.getUniqueId())) {
            return;
        }

        var settings = plugin.getSavingsSettings(player.getUniqueId());

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline()) {
                player.setSendViewDistance(settings.getViewDistance());
            }
        }, 1L);
    }
}
