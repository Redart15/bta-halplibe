package turniplabs.halplibe.helper.creativeInventory;

import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
/**We cannot use ItemStack due to initialization. */
public record FakeStack(@NotNull Supplier<IItemConvertible> iItemConvertible, int metadata){
    public ItemStack getDefaultStack(){
        ItemStack stack = this.iItemConvertible().get().getDefaultStack();
        stack.setMetadata(this.metadata);
        return stack;
    }
}
