package fr.skyfighttv.ffarush.Commands.SubCommands;

import fr.skyfighttv.ffarush.Utils.FileManager;
import fr.skyfighttv.ffarush.Utils.Files;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class FFARushSetSpawn {
    public static void init(Player player, String spawn) {
        YamlConfiguration spawnConfig = FileManager.getValues().get(Files.Spawn);
        YamlConfiguration langConfig = FileManager.getValues().get(Files.Lang);

        spawnConfig.set("Spawns." + spawn, player.getLocation());

        FileManager.save(Files.Spawn);

        player.sendMessage(langConfig.getString("SuccessSetSpawn"));
    }
}
