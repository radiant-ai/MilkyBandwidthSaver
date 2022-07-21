package fun.milkyway.milkybandwidthsaver;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.play.out.entity.WrappedPacketOutEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityeffect.WrappedPacketOutEntityEffect;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityheadrotation.WrappedPacketOutEntityHeadRotation;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityvelocity.WrappedPacketOutEntityVelocity;
import io.github.retrooper.packetevents.packetwrappers.play.out.removeentityeffect.WrappedPacketOutRemoveEntityEffect;
import io.github.retrooper.packetevents.packetwrappers.play.out.updateattributes.WrappedPacketOutUpdateAttributes;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

public class PacketSaverListener extends PacketListenerAbstract {
    private final MilkyBandwidthSaver plugin;

    public PacketSaverListener(MilkyBandwidthSaver plugin) {
        super(PacketListenerPriority.HIGHEST);
        this.plugin = plugin;
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        var player = event.getPlayer();

        if (event.isCancelled()) {
            return;
        }

        if (!plugin.isPlayerWithSaver(player.getUniqueId())) {
            return;
        }

        if (event.getPacketId() == PacketType.Play.Server.ENTITY_HEAD_ROTATION) {
            var packet = new WrappedPacketOutEntityHeadRotation(event.getNMSPacket());
            if (!(packet.getEntity() instanceof Player)) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getPacketId() == PacketType.Play.Server.ENTITY_LOOK) {
            var packet = new WrappedPacketOutEntity.WrappedPacketOutEntityLook(event.getNMSPacket());
            if (!(packet.getEntity() instanceof Player)) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getPacketId() == PacketType.Play.Server.REL_ENTITY_MOVE_LOOK) {
            var packet = new WrappedPacketOutEntity.WrappedPacketOutRelEntityMoveLook(event.getNMSPacket());
            if (!(packet.getEntity() instanceof Player)) {
                var newPacket = new WrappedPacketOutEntity.WrappedPacketOutRelEntityMove(packet.getEntityId(), packet.getDeltaX(), packet.getDeltaY(), packet.getDeltaZ(), packet.isOnGround());
                try {
                    event.setNMSPacket(new NMSPacket(newPacket.asNMSPacket()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }

        if (event.getPacketId() == PacketType.Play.Server.ENTITY_VELOCITY) {
            var packet = new WrappedPacketOutEntityVelocity(event.getNMSPacket());
            if (!(packet.getEntity() instanceof Player)) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getPacketId() == PacketType.Play.Server.ENTITY_EFFECT) {
            var packet = new WrappedPacketOutEntityEffect(event.getNMSPacket());
            if (!(packet.getEntity() instanceof Player)) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getPacketId() == PacketType.Play.Server.UPDATE_ATTRIBUTES) {
            var packet = new WrappedPacketOutUpdateAttributes(event.getNMSPacket());
            if (!(packet.getEntity() instanceof Player || packet.getEntity() instanceof Horse)) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getPacketId() == PacketType.Play.Server.REMOVE_ENTITY_EFFECT) {
            var packet = new WrappedPacketOutRemoveEntityEffect(event.getNMSPacket());
            if (!(packet.getEntity() instanceof Player)) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getPacketId() == PacketType.Play.Server.WORLD_PARTICLES) {
            event.setCancelled(true);
            return;
        }

        if (event.getPacketId() == PacketType.Play.Server.LIGHT_UPDATE) {
            event.setCancelled(true);
            return;
        }

        if (event.getPacketId() == PacketType.Play.Server.SPAWN_ENTITY_EXPERIENCE_ORB) {
            event.setCancelled(true);
            return;
        }

        if (event.getPacketId() == PacketType.Play.Server.BLOCK_BREAK_ANIMATION) {
            event.setCancelled(true);
            return;
        }

        if (event.getPacketId() == PacketType.Play.Server.STOP_SOUND) {
            event.setCancelled(true);
            return;
        }

        if (event.getPacketId() == PacketType.Play.Server.CUSTOM_SOUND_EFFECT) {
            event.setCancelled(true);
            return;
        }

        if (event.getPacketId() == PacketType.Play.Server.ENTITY_SOUND) {
            event.setCancelled(true);
            return;
        }

        if (event.getPacketId() == PacketType.Play.Server.NAMED_SOUND_EFFECT) {
            event.setCancelled(true);
            return;
        }

        if (event.getPacketId() == PacketType.Play.Server.LOOK_AT) {
            event.setCancelled(true);
            return;
        }

        if (event.getPacketId() == PacketType.Play.Server.SCOREBOARD_SCORE) {
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
