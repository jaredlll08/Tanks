package getfluxed.tanks.blocks;

import java.util.Arrays;

import getfluxed.tanks.tileentities.fluids.TileEntityFluidTank;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class BlockTank extends Block implements ITileEntityProvider {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private ExtendedBlockState state = new ExtendedBlockState(this, new IProperty[0], new IUnlistedProperty[] { OBJModel.OBJProperty.instance });

    public static BlockTank instance = new BlockTank();

    public BlockTank() {
        super(Material.iron);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        setHardness(2f);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        EnumFacing facing = (EnumFacing) state.getValue(FACING);
        TRSRTransformation transform = new TRSRTransformation(facing);
        OBJModel.OBJState retState = new OBJModel.OBJState(Arrays.asList(new String[] { OBJModel.Group.ALL }), true, transform);
        return ((IExtendedBlockState) state).withProperty(OBJModel.OBJProperty.instance, retState);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(FACING)).getIndex();
    }

    public BlockState createBlockState() {
        return new ExtendedBlockState(this, new IProperty[] { FACING }, new IUnlistedProperty[] { OBJModel.OBJProperty.instance });
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().isReplaceable(world, pos);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.setBlockState(pos.up(), TankBlocks.tankFluid.getDefaultState());
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow
     * post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        IBlockState st = null;
        if (placer.getHorizontalFacing() == EnumFacing.EAST) {
            st = this.getDefaultState().withProperty(FACING, EnumFacing.EAST);
        } else if (placer.getHorizontalFacing() == EnumFacing.NORTH) {
            st = this.getDefaultState().withProperty(FACING, EnumFacing.WEST);
        } else if (placer.getHorizontalFacing() == EnumFacing.SOUTH) {
            st = this.getDefaultState().withProperty(FACING, EnumFacing.NORTH);
        } else if (placer.getHorizontalFacing() == EnumFacing.WEST) {
            st = this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
        }
        worldIn.setBlockState(pos, st);

    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to
     * allow for adjustments to the IBlockstate
     */
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState st = null;
        if (placer.getHorizontalFacing() == EnumFacing.EAST) {
            st = this.getDefaultState().withProperty(FACING, EnumFacing.EAST);
        } else if (placer.getHorizontalFacing() == EnumFacing.NORTH) {
            st = this.getDefaultState().withProperty(FACING, EnumFacing.WEST);
        } else if (placer.getHorizontalFacing() == EnumFacing.SOUTH) {
            st = this.getDefaultState().withProperty(FACING, EnumFacing.NORTH);
        } else if (placer.getHorizontalFacing() == EnumFacing.WEST) {
            st = this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
        }

        return st;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (player.getCurrentEquippedItem() != null) {
            ItemStack current = player.getCurrentEquippedItem();
            TileEntityFluidTank tank = (TileEntityFluidTank) world.getTileEntity(pos);
            FluidStack currentFluid = tank.tank.getFluid();
            int currentFluidAmount = tank.tank.getFluidAmount();
            FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(current);

            boolean hasFluid = currentFluid != null;

            if (player.getCurrentEquippedItem().getItem() instanceof ItemDye) {

                for (int i : OreDictionary.getOreIDs(current)) {
                    System.out.println(OreDictionary.getOreNames()[i]);
                    String ore = OreDictionary.getOreNames()[i];

                    if (ore.toLowerCase().toLowerCase().startsWith("dye")) {
                        System.out.println("done");
                        String dict = ore.toLowerCase().replace("dye", "");
                        if (dict.equals("black")) {
                            int col = ItemDye.dyeColors[0];
                            if (col == tank.colour) {
                                tank.colour = -1;
                            } else
                                tank.colour = ItemDye.dyeColors[0];
                        } else if (dict.equals("red")) {
                            int col = ItemDye.dyeColors[1];
                            if (col == tank.colour) {
                                tank.colour = -1;
                            } else
                            tank.colour = ItemDye.dyeColors[1];
                        } else if (dict.equals("green")) {
                            int col = ItemDye.dyeColors[2];
                            if (col == tank.colour) {
                                tank.colour = -1;
                            } else
                            tank.colour = ItemDye.dyeColors[2];
                        } else if (dict.equals("brown")) {
                            int col = ItemDye.dyeColors[3];
                            if (col == tank.colour) {
                                tank.colour = -1;
                            } else
                            tank.colour = ItemDye.dyeColors[3];
                        } else if (dict.equals("blue")) {
                            int col = ItemDye.dyeColors[4];
                            if (col == tank.colour) {
                                tank.colour = -1;
                            } else
                            tank.colour = ItemDye.dyeColors[4];
                        } else if (dict.equals("purple")) {
                            int col = ItemDye.dyeColors[5];
                            if (col == tank.colour) {
                                tank.colour = -1;
                            } else
                            tank.colour = ItemDye.dyeColors[5];
                        } else if (dict.equals("cyan")) {
                            int col = ItemDye.dyeColors[6];
                            if (col == tank.colour) {
                                tank.colour = -1;
                            } else
                            tank.colour = ItemDye.dyeColors[6];
                        } else if (dict.equals("lightgray")) {
                            int col = ItemDye.dyeColors[7];
                            if (col == tank.colour) {
                                tank.colour = -1;
                            } else
                            tank.colour = ItemDye.dyeColors[7];
                        } else if (dict.equals("gray")) {
                            int col = ItemDye.dyeColors[8];
                            if (col == tank.colour) {
                                tank.colour = -1;
                            } else
                            tank.colour = ItemDye.dyeColors[8];
                        } else if (dict.equals("pink")) {
                            int col = ItemDye.dyeColors[9];
                            if (col == tank.colour) {
                                tank.colour = -1;
                            } else
                            tank.colour = ItemDye.dyeColors[9];
                        } else if (dict.equals("lime")) {
                            int col = ItemDye.dyeColors[10];
                            if (col == tank.colour) {
                                tank.colour = -1;
                            } else
                            tank.colour = ItemDye.dyeColors[10];
                        } else if (dict.equals("yellow")) {
                            int col = ItemDye.dyeColors[11];
                            if (col == tank.colour) {
                                tank.colour = -1;
                            } else
                            tank.colour = ItemDye.dyeColors[11];
                        } else if (dict.equals("lightblue")) {
                            int col = ItemDye.dyeColors[12];
                            if (col == tank.colour) {
                                tank.colour = -1;
                            } else
                            tank.colour = ItemDye.dyeColors[12];
                        } else if (dict.equals("magenta")) {
                            int col = ItemDye.dyeColors[13];
                            if (col == tank.colour) {
                                tank.colour = -1;
                            } else
                            tank.colour = ItemDye.dyeColors[13];
                        } else if (dict.equals("orange")) {
                            int col = ItemDye.dyeColors[14];
                            if (col == tank.colour) {
                                tank.colour = -1;
                            } else
                            tank.colour = ItemDye.dyeColors[14];
                        } else if (dict.equals("white")) {
                            int col = ItemDye.dyeColors[15];
                            if (col == tank.colour) {
                                tank.colour = -1;
                            } else
                            tank.colour = ItemDye.dyeColors[15];
                        }
                    }

                }

            }
            if (hasFluid) {
                if (FluidContainerRegistry.isFilledContainer(current)) {
                    Fluid containerFluid = FluidContainerRegistry.getFluidForFilledItem(current).getFluid();
                    int containerFluidAmount = FluidContainerRegistry.getFluidForFilledItem(current).amount;
                    if (32000 - (containerFluidAmount + currentFluidAmount) >= 0) {
                        if (currentFluid.getFluid().equals(containerFluid)) {
                            tank.tank.setFluid(new FluidStack(containerFluid, currentFluidAmount + containerFluidAmount));
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, FluidContainerRegistry.drainFluidContainer(current).copy());
                            // tank.markDirty();
                        }
                    }
                }

                if (FluidContainerRegistry.isEmptyContainer(current)) {
                    FluidStack available = tank.getTankInfo(EnumFacing.UP)[0].fluid;

                    if (available != null) {
                        ItemStack filled = FluidContainerRegistry.fillFluidContainer(available, current).copy();

                        liquid = FluidContainerRegistry.getFluidForFilledItem(filled);

                        if (liquid != null) {
                            if (!player.capabilities.isCreativeMode) {
                                if (current.stackSize > 1) {
                                    if (!player.inventory.addItemStackToInventory(filled)) {
                                        return false;
                                    } else {
                                        player.inventory.setInventorySlotContents(player.inventory.currentItem, consumeItem(current));
                                    }
                                } else {
                                    player.inventory.setInventorySlotContents(player.inventory.currentItem, consumeItem(current));
                                    player.inventory.setInventorySlotContents(player.inventory.currentItem, filled);
                                }
                            }

                            tank.drain(EnumFacing.UP, liquid.amount, true);
                            return true;
                        }
                    }
                }
            }
            if (!hasFluid) {
                if (FluidContainerRegistry.isFilledContainer(current)) {
                    Fluid containerFluid = FluidContainerRegistry.getFluidForFilledItem(current).getFluid();
                    int containerFluidAmount = FluidContainerRegistry.getFluidForFilledItem(current).amount;
                    if (32000 - (containerFluidAmount + currentFluidAmount) >= 0) {
                        tank.tank.setFluid(new FluidStack(containerFluid, currentFluidAmount + containerFluidAmount));
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, FluidContainerRegistry.drainFluidContainer(current));
                        tank.markDirty();
                    }
                }
            }
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityFluidTank();
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
    public boolean isVisuallyOpaque() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        TileEntityFluidTank tank = (TileEntityFluidTank) world.getTileEntity(pos);
        tank.onBlockBreak();
        if (world.getBlockState(pos.up()) != null)
            world.setBlockToAir(pos.up());
        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {

        TileEntityFluidTank tank = (TileEntityFluidTank) world.getTileEntity(pos);
        tank.onBlockBreak();
        if (world.getBlockState(pos.up()) != null)
            world.setBlockToAir(pos.up());
        super.breakBlock(world, pos, state);
    }

    public static ItemStack consumeItem(ItemStack stack) {
        if (stack.stackSize == 1) {
            if (stack.getItem().hasContainerItem(stack)) {
                return stack.getItem().getContainerItem(stack);
            } else {
                return null;
            }
        } else {
            stack.splitStack(1);

            return stack;
        }
    }

}
