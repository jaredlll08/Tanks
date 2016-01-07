package getfluxed.tanks.network;

import getfluxed.tanks.tileentities.fluids.TileEntityFluidTank;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageFluid implements IMessage, IMessageHandler<MessageFluid, IMessage> {
	public int fluid;
	public int amount;
	public BlockPos pos;

	public MessageFluid() {
	}

	public MessageFluid(TileEntityFluidTank tile) {
		this.fluid = 0;
		if (tile.tank.getFluid() != null)
			this.fluid = tile.tank.getFluid().getFluid().getID();
		this.amount = tile.tank.getFluidAmount();
		this.pos = tile.getPos();
	}

	public MessageFluid(int fluid, int amount, BlockPos pos) {
		this.fluid = fluid;
		this.amount = amount;
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.fluid = buf.readInt();
		this.amount = buf.readInt();

		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) {

		buf.writeInt(fluid);
		buf.writeInt(amount);
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		
	}

	@Override
	public IMessage onMessage(MessageFluid message, MessageContext ctx) {

		TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.pos);

		if (tileEntity instanceof TileEntityFluidTank) {
			Fluid fluid = null;
			FluidStack stack = null;
			if (message.fluid != 0) {
				fluid = FluidRegistry.getFluid(message.fluid);
				stack = new FluidStack(fluid, message.amount);
			}
			((TileEntityFluidTank) tileEntity).tank.setFluid(stack);

		}

		return null;
	}
}