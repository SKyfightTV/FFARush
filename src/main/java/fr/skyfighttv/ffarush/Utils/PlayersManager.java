package fr.skyfighttv.ffarush.Utils;

import fr.skyfighttv.ffarush.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class PlayersManager {
    private static HashMap<Player, YamlConfiguration> playersFiles;

    public PlayersManager() throws IOException {
        playersFiles = new HashMap<>();

        for (File files : Objects.requireNonNull(new File(Main.getInstance().getDataFolder() + "/Players/").listFiles())) {
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(files);
            playersFiles.put(Bukkit.getOfflinePlayer(UUID.fromString(Objects.requireNonNull(yamlConfiguration.getString("UUID")))).getPlayer(), yamlConfiguration);
        }

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getInstance(), () -> {
            try {
                saveAll();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 36000, 36000);
    }

    public static boolean create(Player player) throws IOException {
        File playerFile = new File(Main.getInstance().getDataFolder() + "/Players/" + player.getName() + ".yml");

        if (!playerFile.exists()) {
            InputStream fileStream = Main.getInstance().getResource(Files.PlayerFile + ".yml");
            byte[] buffer = new byte[fileStream.available()];
            fileStream.read(buffer);

            playerFile.createNewFile();
            OutputStream outStream = new FileOutputStream(playerFile);
            outStream.write(buffer);

            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(playerFile);

            yamlConfiguration.set("UUID", player.getUniqueId());

            yamlConfiguration.save(playerFile);

            playersFiles.put(player, yamlConfiguration);
            return true;
        } else {
            return false;
        }
    }

    public static void save(Player player) throws IOException {
        File file = new File(Main.getInstance().getDataFolder() + "/Players/" + player.getName() + ".yml");
        playersFiles.get(player).save(file);
    }

    public static void saveAll() throws IOException {
        for (Player player : playersFiles.keySet()) {
            File file = new File(Main.getInstance().getDataFolder() + "/Players/" + player.getName() + ".yml");
            playersFiles.get(player).save(file);
        }
    }

    public static YamlConfiguration getPlayer(Player player) {
        return playersFiles.get(player);
    }

    public static HashMap<Player, YamlConfiguration> getPlayers() {
        return playersFiles;
    }
}
