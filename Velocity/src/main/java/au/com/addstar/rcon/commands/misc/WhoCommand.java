package au.com.addstar.rcon.commands.misc;

import java.util.EnumSet;
import java.util.List;

import au.com.addstar.rcon.commands.BadArgumentException;
import au.com.addstar.rcon.commands.CommandSourceType;
import au.com.addstar.rcon.commands.ICommand;
import au.com.addstar.rcon.server.RconServer;
import au.com.addstar.rcon.server.ServerNetworkManager;
import com.velocitypowered.api.command.CommandSource;

public class WhoCommand implements ICommand
{
	@Override
	public String getName()
	{
		return "who";
	}

	@Override
	public String[] getAliases()
	{
		return new String[] {"list"};
	}

	@Override
	public String getPermission()
	{
		return "rcon.who";
	}

	@Override
	public String getUsageString( String label, CommandSource sender )
	{
		return label;
	}

	@Override
	public String getDescription()
	{
		return "List all remote console connections";
	}

	@Override
	public EnumSet<CommandSourceType> getAllowedSenders()
	{
		return EnumSet.allOf(CommandSourceType.class);
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public boolean onCommand(CommandSource sender, String parent, String label, String[] args ) throws BadArgumentException
	{
		if(args.length != 0)
			return false;
		
		StringBuilder list = new StringBuilder();
		
		for(ServerNetworkManager manager : RconServer.instance.getConnections())
		{
			if(manager.getUser() == null)
				continue;
			
			if(list.length() != 0)
				list.append(", ");
			
			list.append(manager.getUser().getName());
		}
		
		sender.sendMessage(ChatColor.GOLD + "There are " + ChatColor.RED + RconServer.instance.getConnections().size() + " connections active.");
		sender.sendMessage(list.toString());
		return true;
	}

	@Override
	public List<String> onTabComplete( CommandSource sender, String parent, String label, String[] args )
	{
		return null;
	}

}
