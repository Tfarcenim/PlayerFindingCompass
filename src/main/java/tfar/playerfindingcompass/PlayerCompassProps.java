package tfar.playerfindingcompass;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PlayerCompassProps implements ItemPropertyFunction {

    private double prevAngle = 0.0D;
    private double prevWobble = 0.0D;
    private long prevWorldTime = 0L;

    /**
     * Calculates the compass angle from an item stack and an entity/item frame
     *
     * @param stack The item stack
     * @param level The level
     * @param livingEntity The entity
     * @return The angle
     */
    @Override
    public float call(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity livingEntity) {
        boolean isLiving = livingEntity != null;

        if (!isLiving && !stack.isFramed() || !stack.hasTag() || !stack.getTag().contains("pos")) {
            return 0;
        }

        Entity entity = isLiving ? livingEntity : stack.getFrame();

        if (level == null) {
            level = (ClientLevel) entity.level;
        }

        int[] playerPos = stack.getTag().getIntArray("pos");
        double angle;

        double entityAngle = isLiving ? entity.yRot : getFrameAngle((ItemFrame) entity);
        entityAngle /= 360.0D;
        entityAngle = Mth.positiveModulo(entityAngle, 1.0D);
        double posAngle = getPosToAngle(playerPos, entity);
        posAngle /= Math.PI * 2D;
        angle = 0.5D - (entityAngle - 0.25D - posAngle);

        if (isLiving) {
            angle = wobble(level, angle);
        }

        return Mth.positiveModulo((float) angle, 1.0F);
    }

    /**
     * Adds wobbliness based on the previous angle and the specified angle
     *
     * @param world The world
     * @param angle The current angle
     * @return The new, wobbly angle
     */
    private double wobble(Level world, double angle) {
        long worldTime = world.getGameTime();
        if (worldTime != prevWorldTime) {
            prevWorldTime = worldTime;
            double angleDifference = angle - prevAngle;
            angleDifference = Mth.positiveModulo(angleDifference + 0.5D, 1.0D) - 0.5D;
            prevWobble += angleDifference * 0.1D;
            prevWobble *= 0.8D;
            prevAngle = Mth.positiveModulo(prevAngle + prevWobble, 1.0D);
        }
        return prevAngle;
    }

    /**
     * Gets the facing direction of an item frame in degrees
     *
     * @param itemFrame The entity instance of the item frame
     * @return The angle
     */
    private double getFrameAngle(ItemFrame itemFrame) {
        Direction direction = itemFrame.getDirection();
        int i = direction.getAxis().isVertical() ? 90 * direction.getAxisDirection().getStep() : 0;
        return (double)Mth.wrapDegrees(180 + direction.get2DDataValue() * 90 + itemFrame.getRotation() * 45 + i);    }

    /**
     * Gets the angle from an entity to the specified position in radians
     *
     * @param pos The position
     * @param entity The entity
     * @return The angle
     */
    private double getPosToAngle(int[] pos, Entity entity) {
        return Math.atan2(pos[2] - entity.getZ(), pos[0] - entity.getX());
    }
}
