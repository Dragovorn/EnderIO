package crazypants.enderio.conduit.packet;

import com.enderio.core.common.util.DyeColor;

import crazypants.enderio.conduit.item.FilterRegister;
import crazypants.enderio.conduit.item.IItemConduit;
import crazypants.enderio.conduit.item.filter.IItemFilter;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;

public class PacketItemConduitFilter extends AbstractConduitPacket<IItemConduit> implements IMessageHandler<PacketItemConduitFilter, IMessage> {

  private EnumFacing dir;
  private boolean loopMode;
  private boolean roundRobin;
  private DyeColor colIn;
  private DyeColor colOut;
  private int priority;

  private IItemFilter inputFilter;
  private IItemFilter outputFilter;

  public PacketItemConduitFilter() {
  }

  public PacketItemConduitFilter(IItemConduit con, EnumFacing dir) {
    super(con.getBundle().getEntity(), ConTypeEnum.ITEM);
    this.dir = dir;
    loopMode = con.isSelfFeedEnabled(dir);
    roundRobin = con.isRoundRobinEnabled(dir);
    colIn = con.getInputColor(dir);
    colOut = con.getOutputColor(dir);
    priority = con.getOutputPriority(dir);

    inputFilter = con.getInputFilter(dir);
    outputFilter = con.getOutputFilter(dir);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    super.toBytes(buf);
    if(dir == null) {
      buf.writeShort(-1);
    }else {
      buf.writeShort(dir.ordinal());
    }
    buf.writeBoolean(loopMode);
    buf.writeBoolean(roundRobin);
    buf.writeInt(priority);
    buf.writeShort(colIn.ordinal());
    buf.writeShort(colOut.ordinal());
    FilterRegister.writeFilter(buf, inputFilter);
    FilterRegister.writeFilter(buf, outputFilter);
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    super.fromBytes(buf);
    short ord = buf.readShort();
    if(ord < 0) {
      dir = null;
    } else {
      dir = EnumFacing.values()[ord];
    }
    loopMode = buf.readBoolean();
    roundRobin = buf.readBoolean();
    priority = buf.readInt();
    colIn = DyeColor.values()[buf.readShort()];
    colOut = DyeColor.values()[buf.readShort()];
    inputFilter = FilterRegister.readFilter(buf);
    outputFilter = FilterRegister.readFilter(buf);
  }

  @Override
  public IMessage onMessage(PacketItemConduitFilter message, MessageContext ctx) {
    IItemConduit conduit = message.getTileCasted(ctx);
    conduit.setSelfFeedEnabled(message.dir, message.loopMode);
    conduit.setRoundRobinEnabled(message.dir, message.roundRobin);
    conduit.setInputColor(message.dir, message.colIn);
    conduit.setOutputColor(message.dir, message.colOut);
    conduit.setOutputPriority(message.dir, message.priority);
    applyFilter(message.dir, conduit, message.inputFilter, true);
    applyFilter(message.dir, conduit, message.outputFilter, false);

    message.getWorld(ctx).markBlockForUpdate(message.x, message.y, message.z);
    return null;
  }

  private void applyFilter(EnumFacing dir, IItemConduit conduit, IItemFilter filter, boolean isInput) {
    if(isInput) {
      conduit.setInputFilter(dir, filter);
    } else {
      conduit.setOutputFilter(dir, filter);
    }
    return;
  }

}
