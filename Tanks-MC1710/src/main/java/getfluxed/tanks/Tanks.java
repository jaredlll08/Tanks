package getfluxed.tanks;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import getfluxed.tanks.blocks.TankBlocks;
import getfluxed.tanks.network.PacketHandler;
import getfluxed.tanks.proxy.CommonProxy;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(modid = "tanks", name = "Tanks", version = "1.0.4", dependencies = "required-after:fluxedcore")
public class Tanks {
    @Instance("tanks")
    public static Tanks instance;
    @SidedProxy(clientSide = "getfluxed.tanks.proxy.ClientProxy", serverSide = "getfluxed.tanks.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {

        TankBlocks.init();
        PacketHandler.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.initRenderers();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TankBlocks.tank), "iii", "ibi", "iii", 'i', "ingotIron", 'b', Items.bucket));
    }
}
