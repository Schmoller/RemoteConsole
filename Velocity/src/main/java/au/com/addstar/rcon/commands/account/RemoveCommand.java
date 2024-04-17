package au.com.addstar.rcon.commands.account;

import java.util.EnumSet;
import java.util.List;

import au.com.addstar.rcon.VelocityRconServer;
import au.com.addstar.rcon.VelocityUser;
import au.com.addstar.rcon.commands.BadArgumentException;
import au.com.addstar.rcon.commands.CommandSourceType;
import au.com.addstar.rcon.commands.ICommand;
import au.com.addstar.rcon.server.RconServer;

public class RemoveCommand implements ICommand
{
	@Override
	public String getName()
	{
		return "remove";
	}

	@Override
	public String[] getAliases()
	{
		return null;
	}

	@Override
	public String getPermission()
	{
		return "rcon.account.manage.remove";
	}

	@Override
	public String getUsageString( String label, CommandSource sender )
	{
		return label + " <username>";
	}

	@Override
	public String getDescription()
	{
		return "Removes a user account";
	}

	@Override
	public EnumSet<CommandSourceType> getAllowedSenders()
	{
		return EnumSet.of(CommandSourceType.Console);
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public boolean onCommand( CommandSource sender, String parent, String label, String[] args )
	{
		if(args.length != 1)
			return false;
		
		VelocityRconServer server = (VelocityRconServer)RconServer.instance;
		VelocityUser user = (VelocityUser)server.getUser(args[0]);
		
		if(user == null)
			throw new BadArgumentException(0, "Unknown account");
		
		if(user.getManager() != null)
			user.getManager().getNetHandler().disconnect("Your account has been removed");
		
		if(server.removeUser(user))
			sender.sendMessage(ChatColor.GREEN + "That user has been removed.");
		else
			sender.sendMessage(ChatColor.RED + "An error occured while saving account data.");
		
		return true;
	}

	@Override
	public List<String> onTabComplete( CommandSource sender, String parent, String label, String[] args )
	{
		return null;
	}

}
