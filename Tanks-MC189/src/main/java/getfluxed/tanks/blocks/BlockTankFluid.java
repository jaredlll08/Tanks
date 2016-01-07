package getfluxed.tanks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class BlockTankFluid extends Block implements ITileEntityProvider {

	public BlockTankFluid() {
		super(Material.glass);
		setHardness(2f);
		setHarvestLevel("pickaxe", 2);

	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		return world.getBlockState(pos.down()).getBlock().onBlockActivated(world, pos.down(), state, playerIn, side, hitX, hitY, hitZ);
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		return false;
	}

	@Override
	public boolean canPlaceTorchOnTop(IBlockAccess world, BlockPos pos) {
		return false;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return false;
	}

	@Override
	public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos) {
		return 1;
	}

	@Override
	public boolean isBlockNormalCube() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(TankBlocks.tank);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		super.onBlockHarvested(world, pos.down(), state, player);
		world.getBlockState(pos.down()).getBlock().onBlockHarvested(world, pos.down(), state, player);
		if (!player.capabilities.isCreativeMode)
			world.getBlockState(pos.down()).getBlock().dropBlockAsItem(world, pos, state, 0);
		world.setBlockToAir(pos.down());
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityTopTank();
	}

	public class TileEntityTopTank extends TileEntity implements IFluidHandler {

		@Override
		public double getMaxRenderDistanceSquared() {
			return 8192;
		}
		@Override
		public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
			return ((IFluidHandler) worldObj.getTileEntity(getPos().down())).fill(from, resource, doFill);
		}

		@Override
		public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
			return ((IFluidHandler) worldObj.getTileEntity(getPos().down())).drain(from, resource, doDrain);
		}

		@Override
		public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
			return ((IFluidHandler) worldObj.getTileEntity(getPos().down())).drain(from, maxDrain, doDrain);
		}

		@Override
		public boolean canFill(EnumFacing from, Fluid fluid) {
			return ((IFluidHandler) worldObj.getTileEntity(getPos().down())).canFill(from, fluid);
		}

		@Override
		public boolean canDrain(EnumFacing from, Fluid fluid) {
			return ((IFluidHandler) worldObj.getTileEntity(getPos().down())).canDrain(from, fluid);
		}

		@Override
		public FluidTankInfo[] getTankInfo(EnumFacing from) {
			return ((IFluidHandler) worldObj.getTileEntity(getPos().down())).getTankInfo(from);
		}

	}
}
