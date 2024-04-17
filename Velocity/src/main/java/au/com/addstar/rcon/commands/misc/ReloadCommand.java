package au.com.addstar.rcon.commands.misc;

import java.util.EnumSet;
import java.util.List;

import au.com.addstar.rcon.RemoteConsolePlugin;
import au.com.addstar.rcon.commands.BadArgumentException;
import au.com.addstar.rcon.commands.CommandSourceType;
import au.com.addstar.rcon.commands.ICommand;

public class ReloadCommand implements ICommand
{
	@Override
	public String getName()
	{
		return "reload";
	}

	@Override
	public String[] getAliases()
	{
		return null;
	}

	@Override
	public String getPermission()
	{
		return "rcon.reload";
	}

	@Override
	public String getUsageString( String label, CommandSource sender )
	{
		return label;
	}

	@Override
	public String getDescription()
	{
		return "Reloads the whitelist";
	}

	@Override
	public EnumSet<CommandSourceType> getAllowedSenders()
	{
		return EnumSet.allOf(CommandSourceType.class);
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public boolean onCommand( CommandSource sender, String parent, String label, String[] args ) throws BadArgumentException
	{
		if (RemoteConsolePlugin.instance.loadWhitelist())
			sender.sendMessage(ChatColor.GREEN + "Reloaded whitelist");
		else
			sender.sendMessage(ChatColor.RED + "An error occured while reloading the whitelist");
		
		return true;
	}

	@Override
	public List<String> onTabComplete( CommandSource sender, String parent, String label, String[] args )
	{
		return null;
	}
}
