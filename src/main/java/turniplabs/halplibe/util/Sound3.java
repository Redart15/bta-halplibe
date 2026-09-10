package turniplabs.halplibe.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.sound.SoundTypes;
import turniplabs.halplibe.util.HalpLibeUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static turniplabs.halplibe.HalpLibe.MOD_ID;

/**
 * Fabric include and generate for backward compatibility a directory in run/resources called sounds3.
 * Sound3 contains alot of the same sounds that BTA uses but also sounds for things such as firework and enderman.
 * This class loads them as halplibe sounds to be used as modders pleases.
 * For all availble sounds see: halplibe/lang.
 */

public class Sound3 {
    private Sound3() {}

    public static void load() {
        Minecraft minecraft = Minecraft.getMinecraft();
        Path source = new File(minecraft.getMinecraftDir(), "resources/sound3").toPath();
        Path desc = new File(minecraft.getMinecraftDir(), "resources/halplibe/").toPath();
        Path sounds = desc.resolve("sounds");
        try {
            // change the top directory and move all files in halplibe/sounds
            Files.createDirectories(sounds);
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(source)) {
                for (Path path : stream) {
                    if (!path.equals(sounds)) {
                        Files.move(path, sounds.resolve(path.getFileName()));
                    }
                }
            }
            // load the sounds using bta loading
            SoundTypes.loadSoundsJson(MOD_ID);
        } catch (IOException e) {
            HalpLibeUtils.LOGGER.error("Halplibe could not load sound3.", e);
        }
    }
}
