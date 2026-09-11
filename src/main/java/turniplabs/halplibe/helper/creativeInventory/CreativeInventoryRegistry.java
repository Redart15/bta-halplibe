package turniplabs.halplibe.helper.creativeInventory;

import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.ItemStack;

import java.util.*;


public class CreativeInventoryRegistry {

    private CreativeInventoryRegistry() {
    }

    public static final CreativeInventoryRegistry INSTANCE = new CreativeInventoryRegistry();
    private final Map<CreativeInventoryCategory, List<ItemStack>> itemsByCategory = new HashMap<>();
    private final Map<ItemStack, List<ItemStack>> itemsByInserted = new HashMap<>();

    private final List<ItemStack> selfList = new ArrayList<>();
    private final List<CreativeInventoryPlacement> placementList = new ArrayList<>();

    public void register(IItemConvertible self, CreativeInventoryPlacement placement) {
        this.register(self.getDefaultStack(), placement);
    }

    public void register(ItemStack self, CreativeInventoryPlacement placement) {
        selfList.add(self);
        placementList.add(placement);
    }

    /// call this after all blocks are registered to get all the inserts.
    public void bakeAll() {
        Iterator<ItemStack> itemIt = selfList.iterator();
        Iterator<CreativeInventoryPlacement> placementIt = placementList.iterator();

        while (itemIt.hasNext()) {
            CreativeInventoryPlacement placement = placementIt.next();
            ItemStack item = itemIt.next();
            List<ItemStack> toAdd = placement.getCustomSupplier() != null
                    ? placement.getCustomSupplier().get()
                    : List.of(item);

            List<ItemStack> list;
            if (placement instanceof CreativeInventoryPlacement.After after) {
                list = itemsByInserted.computeIfAbsent(after.getEntry(), k -> new ArrayList<>());
            } else if (placement instanceof CreativeInventoryPlacement.Category cat) {
                list = itemsByCategory.computeIfAbsent(cat.getCategory(), k -> new ArrayList<>());
            } else {
                throw new RuntimeException("CreativeInventoryPlacement type not registered. Call an developer!");
            }
            list.addAll(toAdd);
        }
    }

    public List<ItemStack> getAllFor(CreativeInventoryCategory category) {
        return this.itemsByCategory.getOrDefault(category, new ArrayList<>());
    }

    public List<ItemStack> getAllFor(ItemStack item) {
        return this.itemsByInserted.getOrDefault(item, List.of());
    }
}
