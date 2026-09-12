package turniplabs.halplibe.helper.creativeInventory;

import it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import it.unimi.dsi.fastutil.ints.IntObjectPair;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.NamespaceID;

import java.util.*;


public class CreativeInventoryRegistry {

    private CreativeInventoryRegistry() {
    }

    public static final CreativeInventoryRegistry INSTANCE = new CreativeInventoryRegistry();
    private final Map<CreativeInventoryCategory, List<ItemStack>> itemsByCategory = new EnumMap<>(CreativeInventoryCategory.class);
    private final Map<IntObjectPair<NamespaceID>, List<ItemStack>> itemsByInserted = new HashMap<>();

    private final List<FakeStack> selfList = new ArrayList<>();
    private final List<CreativeInventoryPlacement> placementList = new ArrayList<>();

    public void register(IItemConvertible self, CreativeInventoryPlacement placement) {
        this.register(new FakeStack(() -> self, 0), placement);
    }

    public void register(FakeStack self, CreativeInventoryPlacement placement) {
        selfList.add(self);
        placementList.add(placement);
    }

    /// call this after all blocks are registered to get all the inserts.
    public void bakeAll() {
        Iterator<FakeStack> itemIt = selfList.iterator();
        Iterator<CreativeInventoryPlacement> placementIt = placementList.iterator();

        while (itemIt.hasNext()) {
            CreativeInventoryPlacement placement = placementIt.next();
            FakeStack item = itemIt.next();
            List<ItemStack> toAdd = placement.getCustomSupplier() != null
                    ? placement.getCustomSupplier().get()
                    : List.of(item.getDefaultStack());

            List<ItemStack> list;
            if (placement instanceof CreativeInventoryPlacement.After after) {
                FakeStack fakeStack = after.getEntry();
                IItemConvertible iItemConvertible = fakeStack.iItemConvertible().get();
                if (iItemConvertible != null) {
                    // ItemStack cannot be used as a key in the map, as thet key only need to hold the name and metadata we use pair instead.
                    IntObjectImmutablePair<NamespaceID> key = new IntObjectImmutablePair<>(fakeStack.metadata(), iItemConvertible.asItem().namespaceID);
                    list = itemsByInserted.computeIfAbsent(key, k -> new ArrayList<>());
                    list.addAll(toAdd);
                }
            } else if (placement instanceof CreativeInventoryPlacement.Category cat) {
                list = itemsByCategory.computeIfAbsent(cat.getCategory(), k -> new ArrayList<>());
                list.addAll(toAdd);
            } else {
                throw new RuntimeException("CreativeInventoryPlacement type not registered. Call an developer!");
            }
        }
    }

    public List<ItemStack> getAllFor(CreativeInventoryCategory category) {
        return this.itemsByCategory.getOrDefault(category, new ArrayList<>());
    }

    public List<ItemStack> getAllFor(ItemStack item) {
        IntObjectPair<NamespaceID> key = new IntObjectImmutablePair<>(item.getMetadata(), item.getItem().namespaceID);
        return this.itemsByInserted.getOrDefault(key, List.of());
    }
}
