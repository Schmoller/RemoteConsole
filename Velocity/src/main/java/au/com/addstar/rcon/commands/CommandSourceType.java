package au.com.addstar.rcon.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

public enum CommandSourceType
{
	/**
	 * Can be Player
	 */
	Player,
	/**
	 * Can be ConsoleCommandSource OR RemoteConsoleCommandSender
	 */
	Console;
	
	public boolean matches(CommandSource sender)
	{
		switch(this)
		{
		case Player:
			return (sender instanceof Player);
		case Console:
			return !(sender instanceof Player);
		default:
			return false;
		}
	}
	
	public static CommandSourceType from(CommandSource sender)
	{
		if(sender instanceof Player)
			return Player;
		return Console;
	}
	
	@Override
	public String toString()
	{
		switch(this)
		{
		case Console:
			return "Console";
		case Player:
			return "Player";
		default:
			return name();
		}
	}
}
