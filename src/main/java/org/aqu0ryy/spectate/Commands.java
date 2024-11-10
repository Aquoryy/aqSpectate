package org.aqu0ryy.spectate;

import org.aqu0ryy.spectate.utils.ChatUtil;
import org.aqu0ryy.spectate.utils.SpectateUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Commands implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("aqspectate.admin")) {
            if (args.length == 0) {
                for (String line : Loader.getInst().getConfig().getStringList("messages.help-admin")) {
                    line = line.replace("{label}", label);
                    ChatUtil.sendMessage(sender, line);
                }

                return true;
            }

            switch (args[0].toLowerCase()) {
                case "reload":
                    reloadCommand(sender);
                    break;
                case "start":
                    startCommand((Player) sender, args, label);
                    break;
                case "end":
                    endCommand((Player) sender);
                    break;
                default:
                    ChatUtil.sendMessage(sender, Loader.getInst().getConfig().getString("messages.no-arg"));
                    break;
            }
        } else if (sender.hasPermission("aqspectate.user")) {
            if (args.length == 0) {
                for (String line : Loader.getInst().getConfig().getStringList("messages.help-user")) {
                    line = line.replace("{label}", label);
                    ChatUtil.sendMessage(sender, line);
                }

                return true;
            }

            if (sender instanceof Player) {
                Player player = (Player) sender;

                switch (args[0].toLowerCase()) {
                    case "start":
                        startCommand(player, args, label);
                        break;
                    case "end":
                        endCommand(player);
                        break;
                    default:
                        ChatUtil.sendMessage(sender, Loader.getInst().getConfig().getString("messages.no-arg"));
                        break;
                }
            } else {
                ChatUtil.sendMessage(sender, Loader.getInst().getConfig().getString("messages.no-console"));
            }
        } else {
            ChatUtil.sendMessage(sender, Loader.getInst().getConfig().getString("messages.no-permission"));
        }

        return false;
    }

    private void startCommand(Player player, String[] args, String label) {
        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);

            if (target != player) {
                if (target != null) {
                    if (!SpectateUtil.getSpecPlayer().containsKey(player)) {
                        if (SpectateUtil.countSpectators(target) < Loader.getInst().getConfig()
                                .getInt("settings.max-spectators")) {
                            SpectateUtil.getSavedLocation().put(player, player.getLocation());
                            SpectateUtil.getSpecPlayer().put(player, target);
                            SpectateUtil.getSavedGameMode().put(player, player.getGameMode());

                            player.setGameMode(GameMode.SPECTATOR);
                            player.teleport(target);
                            ChatUtil.sendMessage(player, Objects.requireNonNull(
                                            Loader.getInst().getConfig().getString("commands.start.success"))
                                    .replace("{target}", target.getName()));
                        } else {
                            ChatUtil.sendMessage(player,
                                    Loader.getInst().getConfig().getString("settings.too-many-spectators"));
                        }
                    } else {
                        ChatUtil.sendMessage(player, Objects.requireNonNull(
                                        Loader.getInst().getConfig().getString("commands.start.already"))
                                .replace("{label}", label));
                    }
                } else {
                    ChatUtil.sendMessage(player, Loader.getInst().getConfig().getString("commands.start.no-player"));
                }
            } else {
                ChatUtil.sendMessage(player, Loader.getInst().getConfig().getString("commands.start.your-self"));
            }
        } else {
            ChatUtil.sendMessage(player, Objects.requireNonNull(
                            Loader.getInst().getConfig().getString("commands.start.usage"))
                    .replace("{label}", label));
        }
    }

    private void endCommand(Player player) {
        if (SpectateUtil.getSpecPlayer().containsKey(player)) {
            Player target = SpectateUtil.getSpecPlayer().remove(player);

            player.setGameMode(SpectateUtil.getSavedGameMode().remove(player));
            player.teleport(SpectateUtil.getSavedLocation().remove(player));
            ChatUtil.sendMessage(player, Objects.requireNonNull(
                            Loader.getInst().getConfig().getString("commands.end.success"))
                    .replace("{target}", target.getName()));
        } else {
            ChatUtil.sendMessage(player, Loader.getInst().getConfig().getString("commands.end.already"));
        }
    }

    private void reloadCommand(CommandSender sender) {
        Loader.getInst().reloadConfig();
        ChatUtil.sendMessage(sender, Loader.getInst().getConfig().getString("commands.reload.success"));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            if (sender.hasPermission("aqspectate.admin")) {
                list.add("reload");
                list.add("start");
                list.add("end");
            } else if (sender.hasPermission("aqspectate.user")) {
                list.add("start");
                list.add("end");
            }
            return list;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("start")) {
            List<String> list = new ArrayList<>();

            for (Player player : Bukkit.getOnlinePlayers()) {
                list.add(player.getName());
            }
            return list;
        }
        return null;
    }
}
