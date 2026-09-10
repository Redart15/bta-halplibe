package turniplabs.halplibe;

import net.fabricmc.api.DedicatedServerModInitializer;
import turniplabs.halplibe.util.Sound3;

public class HalpLibeServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        Sound3.load();
    }
}
