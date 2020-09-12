package tfar.playerfindingcompass;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import tfar.playerfindingcompass.mixin.ItemPropertiesAccessor;

public class PlayerFindingCompass implements ModInitializer, ClientModInitializer {

	public static final String MODID = "playerfindingcompass";
	public static final Item PLAYER_FINDING_COMPASS = new PlayerFindingCompassItem((new Item.Properties()).tab(CreativeModeTab.TAB_TOOLS));

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, new ResourceLocation(MODID,"player_finding_compass"),PLAYER_FINDING_COMPASS);
	}

	@Override
	public void onInitializeClient() {
		ItemPropertiesAccessor.$register(PLAYER_FINDING_COMPASS,new ResourceLocation(MODID,"angle"),new PlayerCompassProps());
	}
}
