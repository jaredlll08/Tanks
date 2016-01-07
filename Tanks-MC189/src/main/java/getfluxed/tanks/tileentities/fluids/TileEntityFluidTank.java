package getfluxed.tanks.tileentities.fluids;

import getfluxed.tanks.network.MessageFluid;
import getfluxed.tanks.network.PacketHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class TileEntityFluidTank extends TileEntity implements IFluidHandler {

	public FluidTank tank;
	public int prevAmount;
	public FluidStack prevFluid;
	public int initPer = 80;

	public TileEntityFluidTank() {
		tank = new FluidTank(32000);
		prevAmount = 0;
	}

	public void onBlockBreak() {
		if (tank.getFluidAmount() > 0) {
			FluidEvent.fireEvent(new FluidEvent.FluidSpilledEvent(tank.getFluid(), worldObj, getPos()));
		}
	}

	@Override
	public double getMaxRenderDistanceSquared() {
		return 8192.0D;
	}

	// @Override
	// public void update() {
	// if (tank.getFluidAmount() >= 0 && !(tank.getFluidAmount() == prevAmount))
	// {
	// prevAmount = tank.getFluidAmount();
	// markDirty();
	// }
	// if (initPer >= 0 && tank.getFluid() != null) {
	// initPer--;
	// if (initPer <= 0) {
	// drain(EnumFacing.UP, 1000, false);
	// fill(EnumFacing.UP, tank.getFluid(), false);
	//// markDirty();
	// }
	// }
	//
	// }

	@Override
	public void markDirty() {
		super.markDirty();
		PacketHandler.INSTANCE.sendToAllAround(new MessageFluid(this), new NetworkRegistry.TargetPoint(this.worldObj.provider.getDimensionId(), (double) this.pos.getX(), (double) this.pos.getY(), (double) this.pos.getZ(), 128d));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagCompound tankTag = new NBTTagCompound();
		tank.writeToNBT(tankTag);
		nbt.setTag("tank", tankTag);
		nbt.setInteger("prev", prevAmount);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagCompound tankTag = nbt.getCompoundTag("tank");
		this.tank.readFromNBT(tankTag);
		this.prevAmount = nbt.getInteger("prev");
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		if (resource == null) {
			return 0;
		}

		FluidStack resourceCopy = resource.copy();
		int totalUsed = 0;

		FluidStack liquid = tank.getFluid();
		if (liquid != null && liquid.amount > 0 && !liquid.isFluidEqual(resourceCopy)) {
			return 0;
		}

		while (resourceCopy.amount > 0) {
			int used = tank.fill(resourceCopy, doFill);
			resourceCopy.amount -= used;
			if (used > 0) {
				markDirty();
			}

			totalUsed += used;

		}

		return totalUsed;
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxEmpty, boolean doDrain) {

		FluidStack output = tank.drain(maxEmpty, doDrain);
		markDirty();
		return output;
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		if (resource == null) {
			return null;
		}
		if (!resource.isFluidEqual(tank.getFluid())) {
			return null;
		}
		return drain(from, resource.amount, doDrain);
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing direction) {
		FluidTank compositeTank = new FluidTank(tank.getCapacity());

		int capacity = tank.getCapacity();

		if (tank.getFluid() != null) {
			compositeTank.setFluid(tank.getFluid().copy());
		} else {
			return new FluidTankInfo[] { compositeTank.getInfo() };
		}

		FluidStack liquid = tank.getFluid();
		if (liquid == null || liquid.amount == 0) {

		} else {
			compositeTank.getFluid().amount += liquid.amount;
		}

		capacity += tank.getCapacity();

		compositeTank.setCapacity(capacity);
		return new FluidTankInfo[] { compositeTank.getInfo() };
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		Fluid tankFluid = getFluidType();
		return tankFluid == null || tankFluid == fluid;
	}

	public Fluid getFluidType() {
		return tank.getFluid() != null ? tank.getFluid().getFluid() : null;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		Fluid tankFluid = getFluidType();
		return tankFluid != null && tankFluid == fluid;
	}

	// we send all our info to the client on load
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		readFromNBT(pkt.getNbtCompound());

	}

}
