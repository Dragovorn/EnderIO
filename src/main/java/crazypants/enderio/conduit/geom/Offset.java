package crazypants.enderio.conduit.geom;

import net.minecraft.util.EnumFacing;

/**
 * Offset vectors are based on Y up.
 * 
 * @author brad
 */
public enum Offset {

  NONE(0, 0, 0),
  UP(EnumFacing.UP),
  DOWN(EnumFacing.DOWN),
  EAST(EnumFacing.EAST),
  WEST(EnumFacing.WEST),
  SOUTH(EnumFacing.SOUTH),
  NORTH(EnumFacing.NORTH),
  EAST_UP(EnumFacing.EAST, EnumFacing.UP),
  WEST_UP(EnumFacing.WEST, EnumFacing.UP),
  SOUTH_UP(EnumFacing.SOUTH, EnumFacing.UP),
  NORTH_UP(EnumFacing.NORTH, EnumFacing.UP),
  EAST_DOWN(EnumFacing.EAST, EnumFacing.DOWN),
  WEST_DOWN(EnumFacing.WEST, EnumFacing.DOWN),
  SOUTH_DOWN(EnumFacing.SOUTH, EnumFacing.DOWN),
  NORTH_DOWN(EnumFacing.NORTH, EnumFacing.DOWN),
  NORTH_EAST(EnumFacing.NORTH, EnumFacing.EAST),
  NORTH_WEST(EnumFacing.NORTH, EnumFacing.WEST),
  SOUTH_EAST(EnumFacing.SOUTH, EnumFacing.EAST),
  SOUTH_WEST(EnumFacing.SOUTH, EnumFacing.WEST);

  public final int xOffset;
  public final int yOffset;
  public final int zOffset;

  private Offset(EnumFacing dir) {
    xOffset = dir.getFrontOffsetX();
    yOffset = dir.getFrontOffsetY();
    zOffset = dir.getFrontOffsetZ();
  }

  private Offset(EnumFacing dir, EnumFacing yDir) {
    xOffset = dir.getFrontOffsetX() + yDir.getFrontOffsetX();
    yOffset = dir.getFrontOffsetY() + yDir.getFrontOffsetY();
    zOffset = dir.getFrontOffsetZ() + yDir.getFrontOffsetZ();
  }

  private Offset(int xOffset, int yOffset, int zOffset) {
    this.xOffset = xOffset;
    this.yOffset = yOffset;
    this.zOffset = zOffset;
  }

}
