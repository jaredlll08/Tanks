package getfluxed.tanks.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemBlock;

public class ItemBlockTank extends ItemBlock{

	public ItemBlockTank(Block block) {
		super(block);
	}
	
@Override
public void registerIcons(IIconRegister p_94581_1_) {
    this.itemIcon = p_94581_1_.registerIcon("tanks:tank");
}


}
