package fun.milkyway.milkybandwidthsaver;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class PacketSaverListener extends PacketListenerAbstract {
    private final MilkyBandwidthSaver plugin;

    public PacketSaverListener(MilkyBandwidthSaver plugin) {
        super(PacketListenerPriority.HIGHEST);
        this.plugin = plugin;
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
            var entity = SpigotReflectionUtil.getEntityById(entityId);
            if (!(entity instanceof Player)) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_HEAD_LOOK) {
            var wrapper = new WrapperPlayServerEntityHeadLook(event);
            var entityId = wrapper.getEntityId();
            var entity = SpigotReflectionUtil.getEntityById(entityId);
            if (!(entity instanceof Player)) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_RELATIVE_MOVE_AND_ROTATION) {
            var wrapper = new WrapperPlayServerEntityRelativeMoveAndRotation(event);
            var entityId = wrapper.getEntityId();
            var entity = SpigotReflectionUtil.getEntityById(entityId);
            if (!(entity instanceof Player)) {
                var newPacket = new WrapperPlayServerEntityRelativeMove(entityId, wrapper.getDeltaX(), wrapper.getDeltaY(), wrapper.getDeltaZ(), wrapper.isOnGround());
                try {
                    event.setByteBuf(newPacket.buffer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_VELOCITY) {
            var wrapper = new WrapperPlayServerEntityVelocity(event);
            var entityId = wrapper.getEntityId();
            var entity = SpigotReflectionUtil.getEntityById(entityId);
            if (!(entity instanceof Player) || !(entity instanceof FishHook) || !(entity instanceof Item)) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_EFFECT) {
            var wrapper = new WrapperPlayServerEntityEffect(event);
            var entityId = wrapper.getEntityId();
            var entity = SpigotReflectionUtil.getEntityById(entityId);
            if (!(entity instanceof Player)) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_PROPERTIES) {
            var wrapper = new WrapperPlayServerEntityProperties(event);
            var entityId = wrapper.getEntityId();
            var entity = SpigotReflectionUtil.getEntityById(entityId);
            if (!(entity instanceof Player)) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getPacketType() == PacketType.Play.Server.REMOVE_ENTITY_EFFECT) {
            var wrapper = new WrapperPlayServerRemoveEntityEffect(event);
            var entityId = wrapper.getEntityId();
            var entity = SpigotReflectionUtil.getEntityById(entityId);
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
            return;
        }
    }

    /*
    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        var player = event.getPlayer();

        if (event.isCancelled()) {
            return;
        }

        if (!plugin.isPlayerWithSaver(player.getUniqueId())) {
            return;
        }

        if (event.getPacketId() == PacketType.Play.Client.SETTINGS) {
            var packet = new WrappedPacketInSettings(event.getNMSPacket());
            packet.setViewDistance(3);
        }
    }

     */
}
