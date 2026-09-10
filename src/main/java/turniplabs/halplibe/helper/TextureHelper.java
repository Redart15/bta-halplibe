package turniplabs.halplibe.helper;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.render.texture.stitcher.AtlasStitcher;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.util.HalpLibeUtils;

import java.util.Optional;

@SuppressWarnings("unused")
public final class TextureHelper {

    public static void initializeAllFiles(String modId, AtlasStitcher atlas, boolean searchSubDirs) {
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(modId);
        if (modContainer.isEmpty()) {
            HalpLibeUtils.LOGGER.error("Failed to find mod '{}' when loading textures!", modId);
            return;
        }

        try {
            TextureRegistry.initializeAllFiles(modId, atlas, searchSubDirs);
        } catch (Exception e) {
            HalpLibeUtils.LOGGER.error("Failed to initialize textures for mod '{}' in atlas!", modId, e);
        }
    }

    private TextureHelper() {}
}
