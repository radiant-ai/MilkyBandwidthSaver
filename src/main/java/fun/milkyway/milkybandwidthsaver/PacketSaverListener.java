package fun.milkyway.milkybandwidthsaver;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketSaverListener extends PacketListenerAbstract {
    private final MilkyBandwidthSaver plugin;
    private final PacketEventsAPI<?> packetEventsAPI;
    private final Map<Integer,AtomicInteger> cartCounters;

    public PacketSaverListener(MilkyBandwidthSaver plugin) {
        super(PacketListenerPriority.HIGHEST);
        this.plugin = plugin;
        this.packetEventsAPI = PacketEvents.getAPI();
        this.cartCounters = new ConcurrentSkipListMap<>();
        startMapClearSchedule();
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        if (!plugin.isPlayerWithSaver(player.getUniqueId())) {
            return;
        }

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_ROTATION) {
            var wrapper = new WrapperPlayServerEntityRotation(event);
            var entityId = wrapper.getEntityId();
            var entity = SpigotConversionUtil.getEntityById(player.getWorld(), entityId);
            if (!(entity instanceof Player)) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_HEAD_LOOK) {
            var wrapper = new WrapperPlayServerEntityHeadLook(event);
            var entityId = wrapper.getEntityId();
            var entity = SpigotConversionUtil.getEntityById(player.getWorld(), entityId);
            if (!(entity instanceof Player)) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_RELATIVE_MOVE_AND_ROTATION) {
            var wrapper = new WrapperPlayServerEntityRelativeMoveAndRotation(event);
            var entityId = wrapper.getEntityId();
            var entity = SpigotConversionUtil.getEntityById(player.getWorld(), entityId);
            if (!(entity instanceof Player)) {
                var newPacket = new WrapperPlayServerEntityRelativeMove(entityId, wrapper.getDeltaX(), wrapper.getDeltaY(), wrapper.getDeltaZ(), wrapper.isOnGround());
                packetEventsAPI.getPlayerManager().sendPacketSilently(player, newPacket);
                event.setCancelled(true);
                return;
            }
        }

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_VELOCITY) {
            var wrapper = new WrapperPlayServerEntityVelocity(event);
            var entityId = wrapper.getEntityId();
            var entity = SpigotConversionUtil.getEntityById(player.getWorld(), entityId);
            if (!(entity instanceof Player || entity instanceof Projectile || entity instanceof Item)) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_TELEPORT) {
            var wrapper = new WrapperPlayServerEntityTeleport(event);
            var entityId = wrapper.getEntityId();
            var entity = SpigotConversionUtil.getEntityById(player.getWorld(), entityId);

            if (!(entity instanceof Minecart)) {
                return;
            }

            var counter = cartCounters.computeIfAbsent(entityId, (id) -> new AtomicInteger(0));

            if (counter.incrementAndGet() % 2 == 0) {
                return;
            }

            event.setCancelled(true);
            return;
        }

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_EFFECT) {
            var wrapper = new WrapperPlayServerEntityEffect(event);
            var entityId = wrapper.getEntityId();
            var entity = SpigotConversionUtil.getEntityById(player.getWorld(), entityId);
            if (!(entity instanceof Player)) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getPacketType() == PacketType.Play.Server.REMOVE_ENTITY_EFFECT) {
            var wrapper = new WrapperPlayServerRemoveEntityEffect(event);
            var entityId = wrapper.getEntityId();
            var entity = SpigotConversionUtil.getEntityById(player.getWorld(), entityId);
            if (!(entity instanceof Player)) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getPacketType() == PacketType.Play.Server.PARTICLE) {
            event.setCancelled(true);
            return;
        }

        if (event.getPacketType() == PacketType.Play.Server.UPDATE_LIGHT) {
            event.setCancelled(true);
            return;
        }

        if (event.getPacketType() == PacketType.Play.Server.SPAWN_EXPERIENCE_ORB) {
            event.setCancelled(true);
            return;
        }

        if (event.getPacketType() == PacketType.Play.Server.BLOCK_BREAK_ANIMATION) {
            event.setCancelled(true);
            return;
        }

        if (event.getPacketType() == PacketType.Play.Server.STOP_SOUND) {
            event.setCancelled(true);
            return;
        }

        if (event.getPacketType() == PacketType.Play.Server.SOUND_EFFECT) {
            event.setCancelled(true);
            return;
        }

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_SOUND_EFFECT) {
            event.setCancelled(true);
            return;
        }

        if (event.getPacketType() == PacketType.Play.Server.NAMED_SOUND_EFFECT) {
            event.setCancelled(true);
            return;
        }

        if (event.getPacketType() == PacketType.Play.Server.UPDATE_SCORE) {
            event.setCancelled(true);
        }
    }



    private void startMapClearSchedule() {
        Bukkit.getScheduler().runTaskTimer(plugin, cartCounters::clear, 20 * 120, 20 * 120);
    }
}
