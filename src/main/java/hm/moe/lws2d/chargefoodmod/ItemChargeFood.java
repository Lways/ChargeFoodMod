package hm.moe.lws2d.chargefoodmod;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemChargeFood extends FPRootPoweredItem {
	private HashMap<String, Integer> map = new HashMap<String, Integer>(){
		{
			put("item.apple", 4);
			put("item.bread", 5);
			put("item.porkchopRaw", 3);
			put("item.porkchopCooked", 8);
			put("item.cookie", 2);
			put("item.melon", 2);
			put("item.beefRaw", 3);
			put("item.beefCooked", 8);
			put("item.chickenRaw", 2);
			put("item.chickenCooked", 6);
			put("item.rottenFlesh", 4);
			put("item.spiderEye", 2);
			put("item.potatoBaked", 5);
			put("item.potatoPoisonous", 2);
			//put("item.carrotGolden", 6);
			put("item.pumpkinPie", 8);
			put("item.rabbitRaw", 3);
			put("item.rabbitCooked", 5);
			put("item.muttonRaw", 2);
			put("item.muttonCooked", 6);
			put("item.mushroomStew", 6);
			put("item.rabbitStew", 10);
			//put("item.appleGold", 4);
			put("item.carrots", 3);
			put("item.potato", 1);
			put("item.fish.pufferfish.raw", 1);
			put("item.fish.clownfish.raw", 1);
			put("item.fish.salmon.raw", 2);
			put("item.fish.cod.raw", 2);
			put("item.fish.salmon.cooked", 6);
			put("item.fish.cod.cooked", 5);

		}
	};
	public static int powerCapacity = 100000;
	public ItemChargeFood(float par2, boolean par3)
	{
		super(powerCapacity, par2, par3);
		this.setUnlocalizedName("chargeFood");	//システム名の登録
		this.setMaxStackSize(1);	//スタックできる量
	}

	@Override
	public EnumAction getItemUseAction(ItemStack var1)
	{
		 return EnumAction.DRINK;
	}

	@Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
		float heals = 20 - playerIn.getFoodStats().getFoodLevel();
		System.out.println("heals : " + heals);
		if(super.getFPCurrentPower(stack)>=heals*10){
	        usePower(heals*10, stack);
	        playerIn.getFoodStats().addStats((int)heals, this.getSaturationModifier(stack));
	        worldIn.playSoundAtEntity(playerIn, "random.burp", 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
	        this.onFoodEaten(stack, worldIn, playerIn);
	        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
		}
        return stack;
    }

	@Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
		double currentPower = super.getFPCurrentPower(itemStackIn);
		if( playerIn.isSneaking()){
			double maxPower = super.getFPMaxPower(itemStackIn);

			if(currentPower < maxPower){
				if (playerIn != null && playerIn.inventory instanceof IInventory) {
					IInventory inv = (IInventory)playerIn.inventory;
					int charges = 0;

					for(int i=0;i < inv.getSizeInventory(); i++){
						ItemStack item = inv.getStackInSlot(i);

						if(item!=null){
							System.out.println(item.getUnlocalizedName());
							if(map.get(item.getUnlocalizedName()) != null){
								if (item.getItem().getItemUseAction(null) == EnumAction.EAT && map.get(item.getUnlocalizedName()) > 0) {
									charges += item.stackSize * map.get(item.getUnlocalizedName());
									inv.setInventorySlotContents(i, null);
									if(currentPower + charges*10 > maxPower) break;
								}
							}
						}
					}
					if(charges == 0){
						playerIn.addChatMessage(PlayerMessages.noFood.get());
						return itemStackIn;
					}

					this.chargePower(charges * 10, itemStackIn);
			        worldIn.playSoundAtEntity(playerIn, "random.burp", 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
				}
			}
		}
		else{
			if (playerIn.canEat(false) && currentPower >= 10)
	        {
	            playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
	        }
		}

        return itemStackIn;
    }

	public boolean usePower(final float amount, final ItemStack is )
	{
		return this.extractFPPower( is, amount ) >= amount - 0.5;
	}

	public double chargePower(final float amount, final ItemStack is )
	{
		return this.injectFPPower( is, amount );
	}
}