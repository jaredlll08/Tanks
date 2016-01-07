package getfluxed.tanks.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import getfluxed.tanks.blocks.TankBlocks;
import getfluxed.tanks.client.render.RenderItemTank;
import getfluxed.tanks.client.render.RenderTank;
import getfluxed.tanks.tileentities.fluids.TileEntityFluidTank;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
	
	@SuppressWarnings("unchecked")
	@Override
	public void initRenderers() {
	
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidTank.class, new RenderTank());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TankBlocks.tank), new RenderItemTank());
				
	}
}
