package hm.moe.lws2d.chargefoodmod;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid=ChargeFoodCore.MOD_ID, name="ChargeFoodMod", version="1.0")
public class ChargeFoodCore
{
    public static final String MOD_ID = "chargefoodmod";
	public static Item chargeFood;
	public static Item mixingModule;
	public static Item subspaceModule;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		//インスタンスの代入。引数はID, 回復量, 狼が食べれるか
		chargeFood = new ItemChargeFood(0.8F, false);	//0.8F equals beef's value
		mixingModule = new Item()
				.setCreativeTab(CreativeTabs.tabMaterials)
				.setUnlocalizedName("mixingModule")
				.setMaxStackSize(64);
		subspaceModule = new Item()
				.setCreativeTab(CreativeTabs.tabMaterials)
				.setUnlocalizedName("subspaceModule")
				.setMaxStackSize(64);

		GameRegistry.registerItem(chargeFood, "chargeFood");
		GameRegistry.registerItem(mixingModule, "mixingModule");
		GameRegistry.registerItem(subspaceModule, "subspaceModule");
    	if(event.getSide().isClient()){
    		ModelLoader.setCustomModelResourceLocation(chargeFood, 0, new ModelResourceLocation(MOD_ID + ":chargefood", "inventory"));
    		ModelLoader.setCustomModelResourceLocation(mixingModule, 0, new ModelResourceLocation(MOD_ID + ":mixingmodule", "inventory"));
    		ModelLoader.setCustomModelResourceLocation(subspaceModule, 0, new ModelResourceLocation(MOD_ID + ":subspacemodule", "inventory"));
    	}
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		GameRegistry.addRecipe(new ItemStack(chargeFood),
				" M ",
				"LSL",
				"IMI",
				'M', mixingModule,
				'L', Items.leather,
				'S', subspaceModule,
				'I', Blocks.ice
				);

		GameRegistry.addRecipe(new ItemStack(mixingModule),
				"IDI",
				"iFd",
				"CAC",
				'I', Items.iron_ingot,
				'D', Items.diamond,
				'i', Items.iron_sword,
				'F', Items.flint,
				'd', Items.diamond_sword,
				'C', Blocks.cobblestone,
				'A', Items.diamond_axe
				);

		GameRegistry.addRecipe(new ItemStack(subspaceModule),
				"PEP",
				"OCO",
				"YCY",
				'P', Items.ender_pearl,
				'E', Blocks.ender_chest,
				'O', Blocks.obsidian,
				'C', Blocks.chest,
				'Y', Items.ender_eye
				);
	}
}