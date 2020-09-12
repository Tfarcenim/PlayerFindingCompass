package tfar.playerfindingcompass.mixin;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemProperties.class)
public interface ItemPropertiesAccessor {
    @Invoker("register")
    static void $register(Item item, ResourceLocation resourceLocation, ItemPropertyFunction itemPropertyFunction) {
        throw new RuntimeException("oops, no mixin");
    }
}
