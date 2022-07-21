package fun.milkyway.milkybandwidthsaver;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.out.entity.WrappedPacketOutEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityheadrotation.WrappedPacketOutEntityHeadRotation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketProfilerListener extends PacketListenerAbstract {

    private final MilkyBandwidthSaver plugin;
    private final Map<String, AtomicInteger> packets;

    public PacketProfilerListener(MilkyBandwidthSaver plugin) {
        super(PacketListenerPriority.MONITOR);
        this.plugin = plugin;
        packets = new ConcurrentHashMap<>();
        startSchedule();
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        var player = event.getPlayer();

        if (event.isCancelled()) {
            return;
        }

        if (plugin.getTargetPlayer() == null) {
            return;
        }

        if (!player.getUniqueId().equals(plugin.getTargetPlayer())) {
            return;
        }

        incrementPacket(event.getNMSPacket().getName());
    }

    private void incrementPacket(String packetName) {
        var count = packets.get(packetName);
        if (count == null) {
            count = new AtomicInteger(1);
            packets.put(packetName, count);
            return;
        }
        count.incrementAndGet();
    }

    private void startSchedule() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (plugin.getTargetPlayer() == null) {
                return;
            }
            if (plugin.getProfilesReceiver() == null || (plugin.getProfilesReceiver() instanceof Player player && !player.isOnline())) {
                return;
            }
            var sortedEntryList = packets.entrySet().stream().sorted((e1, e2) -> e2.getValue().get() - e1.getValue().get()).collect(java.util.stream.Collectors.toList());
            var builder = Component.text();
            builder.append(MiniMessage.miniMessage().deserialize("\n<gray>========= PACKET SUMMARY FOR 10 SECONDS ==========\n"));

            for (var entry : sortedEntryList) {
                var packet = entry.getKey();
                var count = entry.getValue();
                builder.append(MiniMessage.miniMessage().deserialize("<white>- <gray>" + packet + ": <red>" + count.get() + "\n"));
            }
            builder.append(Component.newline());
            plugin.getProfilesReceiver().sendMessage(builder.build());
            packets.clear();
        }, 200, 200);
    }
}
