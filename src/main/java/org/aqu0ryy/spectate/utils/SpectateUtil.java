package org.aqu0ryy.spectate.utils;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SpectateUtil {

    private static final Map<Player, Player> specPlayer = new HashMap<>();
    private static final Map<Player, GameMode> savedGameMode = new HashMap<>();
    private static final Map<Player, Location> savedLocation = new HashMap<>();

    public static int countSpectators(Player target) {
        int count = 0;

        for (Player spectator : specPlayer.keySet()) {
            if (specPlayer.get(spectator).equals(target)) {
                count++;
            }
        }
        return count;
    }

    public static Map<Player, Player> getSpecPlayer() {
        return specPlayer;
    }

    public static Map<Player, GameMode> getSavedGameMode() {
        return savedGameMode;
    }

    public static Map<Player, Location> getSavedLocation() {
        return savedLocation;
    }
}
