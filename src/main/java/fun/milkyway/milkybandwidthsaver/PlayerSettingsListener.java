package fun.milkyway.milkybandwidthsaver;

import com.destroystokyo.paper.event.player.PlayerClientOptionsChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerSettingsListener implements Listener {
    private final MilkyBandwidthSaver plugin;

    public PlayerSettingsListener(MilkyBandwidthSaver plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerChangeSettings(PlayerClientOptionsChangeEvent event) {
        var player = event.getPlayer();

        if (!plugin.isPlayerWithSaver(player.getUniqueId())) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline()) {
                player.setSendViewDistance(3);
            }
        }, 1L);
    }
}
