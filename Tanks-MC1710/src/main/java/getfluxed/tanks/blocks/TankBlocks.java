package getfluxed.tanks.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import getfluxed.tanks.blocks.BlockTankFluid.TileEntityTopTank;
import getfluxed.tanks.tileentities.fluids.TileEntityFluidTank;
import getfluxed.tanks.tileentities.fluids.TileEntityTank;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class TankBlocks {

    public static Block tank = new BlockTank();
    public static Block tankFluid = new BlockTankFluid();
    public static ItemBlock tankItem = new ItemBlockTank(tank);
    public static CreativeTabs tab = new CreativeTabs("tanks") {

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(tank);
        }
    };

    public static void init() {
        GameRegistry.registerTileEntity(TileEntityTank.class, "tankTile");
        GameRegistry.registerTileEntity(TileEntityFluidTank.class, "tankFluid");
        GameRegistry.registerTileEntity(TileEntityTopTank.class, "tankFluidTop");
        registerBlock(tank, "Tank", "tank");
        tankFluid.setBlockName("tankFluid");
        GameRegistry.registerBlock(tankFluid, "tankFluid");

    }

    public static void registerBlock(Block block, String name, String key) {
        block.setBlockTextureName("tanks:" + key).setBlockName(name).setCreativeTab(tab);
        GameRegistry.registerBlock(block, key);
    }

}
