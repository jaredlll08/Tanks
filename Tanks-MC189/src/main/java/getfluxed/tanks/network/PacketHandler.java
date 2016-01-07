package getfluxed.tanks.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("");

	private static int id = 0;

	public static void init() {

		INSTANCE.registerMessage(MessageFluid.class, MessageFluid.class, id++, Side.CLIENT);

	}

}
