package getfluxed.tanks.proxy;

import getfluxed.tanks.blocks.TankBlocks;
import getfluxed.tanks.client.render.RenderTank;
import getfluxed.tanks.tileentities.fluids.TileEntityFluidTank;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
	
	@SuppressWarnings("unchecked")
	@Override
	public void initRenderers() {
	
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidTank.class, new RenderTank());
//		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TankBlocks.tank), new RenderItemTank());
		TankBlocks.init();
				
	}
}
