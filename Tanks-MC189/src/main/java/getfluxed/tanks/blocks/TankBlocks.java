package getfluxed.tanks.blocks;

import java.util.Map;

import getfluxed.tanks.blocks.BlockTankFluid.TileEntityTopTank;
import getfluxed.tanks.tileentities.fluids.TileEntityFluidTank;
import getfluxed.tanks.tileentities.fluids.TileEntityTank;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TankBlocks {

    public static Block tank = new BlockTank();
    public static Block tankFluid = new BlockTankFluid();
    public static CreativeTabs tab = new CreativeTabs("tanks") {

        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(tank);
        }
    };

    public static void preInit() {
        GameRegistry.registerTileEntity(TileEntityTank.class, "tank");
        GameRegistry.registerTileEntity(TileEntityFluidTank.class, "tankFluid");
        GameRegistry.registerTileEntity(TileEntityTopTank.class, "tankFluidTop");

        registerBlock(tank, "Tank", "tank");
        // tank.setUnlocalizedName("tank").setCreativeTab(tab);
        // GameRegistry.registerBlock(tank, ItemBlockTank.class, "tank");
        tankFluid.setUnlocalizedName("tankFluid");
        GameRegistry.registerBlock(tankFluid, "tankFluid");

    }

    public static void init() {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(tank), 0, new ModelResourceLocation("tanks" + ":" + "tank", "inventory"));

    }

    public static void registerBlock(Block block, String name, String key) {
        block.setUnlocalizedName(name).setCreativeTab(tab);
        GameRegistry.registerBlock(block, key);
    }

}
