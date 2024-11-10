package org.aqu0ryy.spectate.listeners;

import org.aqu0ryy.spectate.Loader;
import org.aqu0ryy.spectate.utils.ChatUtil;
import org.aqu0ryy.spectate.utils.SpectateUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

public class SpectateListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (SpectateUtil.getSpecPlayer().containsKey(player)) {
            Location location = SpectateUtil.getSavedLocation().remove(player);

            player.setGameMode(SpectateUtil.getSavedGameMode().remove(player));
            player.teleport(location);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Player target = SpectateUtil.getSpecPlayer().get(player);

        if (SpectateUtil.getSpecPlayer().containsKey(player)) {
            if (player.getLocation().distance(target.getLocation()) > Loader.getInst().getConfig().getInt("settings.max-range")) {
                player.teleport(target);
                ChatUtil.sendMessage(player, Objects.requireNonNull(
                        Loader.getInst().getConfig().getString("settings.too-far-target")).replace("{target}", target.getName()));
            }
        }
    }
}
