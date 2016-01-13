package getfluxed.tanks;

import getfluxed.tanks.blocks.TankBlocks;
import getfluxed.tanks.network.PacketHandler;
import getfluxed.tanks.proxy.CommonProxy;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(modid = "tanks", name = "Tanks", version = "1.0.4", dependencies="required-after:fluxedcore")
public class Tanks {
	@Instance("tanks")
	public static Tanks instance;
	@SidedProxy(clientSide = "getfluxed.tanks.proxy.ClientProxy", serverSide = "getfluxed.tanks.proxy.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {

		TankBlocks.preInit();
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
