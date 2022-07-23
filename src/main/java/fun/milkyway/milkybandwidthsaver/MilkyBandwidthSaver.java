package fun.milkyway.milkybandwidthsaver;

import co.aikar.commands.PaperCommandManager;
import com.github.retrooper.packetevents.PacketEvents;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class MilkyBandwidthSaver extends JavaPlugin {

    private UUID targetPlayer;
    private CommandSender profilesReceiver;
    private Map<UUID, SavingsSettings> playersWithSaver;

    @Override
    public void onEnable() {
        targetPlayer = null;
        profilesReceiver = null;
        playersWithSaver = new ConcurrentHashMap<>();
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new MbsCommand(this));
        PacketEvents.getAPI().getEventManager().registerListener(new PacketProfilerListener(this));
        PacketEvents.getAPI().getEventManager().registerListener(new PacketSaverListener(this));
        getServer().getPluginManager().registerEvents(new PlayerSettingsListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public UUID getTargetPlayer() {
        return targetPlayer;
    }

    public void setTargetPlayer(UUID targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    public CommandSender getProfilesReceiver() {
        return profilesReceiver;
    }

    public void setProfilesReceiver(CommandSender profilesReceiver) {
        this.profilesReceiver = profilesReceiver;
    }

    public boolean isPlayerWithSaver(UUID player) {
        return playersWithSaver.containsKey(player);
    }

    public void addPlayerWithSaver(UUID player, SavingsSettings settings) {
        playersWithSaver.put(player, settings);
    }

    public void removePlayerWithSaver(UUID player) {
        playersWithSaver.remove(player);
    }

    public SavingsSettings getSavingsSettings(UUID player) {
        return playersWithSaver.get(player);
    }
}
