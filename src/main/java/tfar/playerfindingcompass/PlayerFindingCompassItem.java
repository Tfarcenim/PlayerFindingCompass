package tfar.playerfindingcompass;

import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class PlayerFindingCompassItem extends Item {
    public PlayerFindingCompassItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack compass = player.getItemInHand(interactionHand);
        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) player.level;
            Player nearby = serverLevel.getNearestPlayer(player.getX(),player.getY(),player.getZ(),10000,other -> other != player);
            if (nearby != null) {
                compass.getOrCreateTag().putUUID("tracking",nearby.getUUID());
                player.sendMessage(new TranslatableComponent("text.playerfinding_compass.now_tracking",nearby.getName()), Util.NIL_UUID);
            }
        }
        return InteractionResultHolder.success(compass);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        if (itemStack.hasTag() && itemStack.getTag().hasUUID("tracking") && !entity.level.isClientSide) {
            UUID uuid = itemStack.getTag().getUUID("tracking");
            Player tracking = level.getPlayerByUUID(uuid);
            if (tracking != null) {
                itemStack.getTag().putIntArray("pos",new int[]{(int)tracking.getX(),(int)tracking.getY(),(int)tracking.getZ()});
            }
        }
    }
}
