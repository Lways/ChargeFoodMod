package hm.moe.lws2d.chargefoodmod;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public enum PlayerMessages
{
	noFood;

	public IChatComponent get()
	{
		return new ChatComponentTranslation( this.getName() );
	}

	String getName()
	{
		return "chat." + this.toString();
	}

}