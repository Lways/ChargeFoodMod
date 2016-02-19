package hm.moe.lws2d.chargefoodmod;

import java.text.MessageFormat;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FPRootPoweredItem extends ItemFood{
	private static final String POWER_NBT_KEY = "internalCurrentPower";
	private final double powerCapacity;

	public FPRootPoweredItem( final double powerCapacity, float saturation, boolean isWolfFood )
	{
		super( 0, saturation, isWolfFood );
		this.setMaxDamage( 32 );
		this.hasSubtypes = false;
		this.setFull3D();

		this.powerCapacity = powerCapacity;
	}

	@Override
	public void addInformation( final ItemStack stack, final EntityPlayer player, final List<String> lines, final boolean displayMoreInfo )
	{
		final NBTTagCompound tag = stack.getTagCompound();
		double internalCurrentPower = 0;
		final double internalMaxPower = this.getFPMaxPower( stack );

		if( tag != null )
		{
			internalCurrentPower = tag.getDouble( "internalCurrentPower" );
		}

		final double percent = internalCurrentPower / internalMaxPower;

		lines.add( "FP:" + MessageFormat.format( " {0,number,#} ", internalCurrentPower ) +  " - " + MessageFormat.format( " {0,number,#.##%} ", percent ) );
	}

	@Override
	public boolean isDamageable()
	{
		return true;
	}

	@Override
	public void getSubItems( final Item sameItem, final CreativeTabs creativeTab, final List<ItemStack> itemStacks )
	{
		super.getSubItems( sameItem, creativeTab, itemStacks );

		final ItemStack charged = new ItemStack( this, 1 );
		final NBTTagCompound tag = openNbtData( charged );
		tag.setDouble( "internalCurrentPower", this.getFPMaxPower( charged ) );
		tag.setDouble( "internalMaxPower", this.getFPMaxPower( charged ) );

		itemStacks.add( charged );
	}

	@Override
	public boolean isRepairable()
	{
		return false;
	}

	@Override
	public double getDurabilityForDisplay( final ItemStack is )
	{
		return 1 - this.getFPCurrentPower( is ) / this.getFPMaxPower( is );
	}

	@Override
	public boolean isDamaged( final ItemStack stack )
	{
		return true;
	}

	@Override
	public void setDamage( final ItemStack stack, final int damage )
	{

	}

	private double getInternalBattery( final ItemStack is, final batteryOperation op, final double adjustment )
	{
		final NBTTagCompound data = openNbtData( is );

		double currentStorage = data.getDouble( POWER_NBT_KEY );
		final double maxStorage = this.getFPMaxPower( is );

		switch( op )
		{
			case INJECT:
				currentStorage += adjustment;
				if( currentStorage > maxStorage )
				{
					final double diff = currentStorage - maxStorage;
					data.setDouble( POWER_NBT_KEY, maxStorage );
					return diff;
				}
				data.setDouble( POWER_NBT_KEY, currentStorage );
				return 0;
			case EXTRACT:
				if( currentStorage > adjustment )
				{
					currentStorage -= adjustment;
					data.setDouble( POWER_NBT_KEY, currentStorage );
					return adjustment;
				}
				data.setDouble( POWER_NBT_KEY, 0 );
				return currentStorage;
			default:
				break;
		}

		return currentStorage;
	}

	public double injectFPPower( final ItemStack is, final double amt )
	{
		return this.getInternalBattery( is, batteryOperation.INJECT, amt );
	}

	public double extractFPPower( final ItemStack is, final double amt )
	{
		return this.getInternalBattery( is, batteryOperation.EXTRACT, amt );
	}

	public double getFPMaxPower( final ItemStack is )
	{
		return this.powerCapacity;
	}

	public double getFPCurrentPower( final ItemStack is )
	{
		return this.getInternalBattery( is, batteryOperation.STORAGE, 0 );
	}

	private enum batteryOperation
	{
		STORAGE, INJECT, EXTRACT
	}

	public static NBTTagCompound openNbtData( final ItemStack i )
	{
		NBTTagCompound compound = i.getTagCompound();

		if( compound == null )
		{
			i.setTagCompound( compound = new NBTTagCompound() );
		}

		return compound;
	}
}
