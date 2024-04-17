package au.com.addstar.rcon.commands;

import au.com.addstar.rcon.commands.misc.KickCommand;
import au.com.addstar.rcon.commands.misc.ReloadCommand;
import au.com.addstar.rcon.commands.misc.WhoCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;

public final class RconCommand implements SimpleCommand {
	private CommandDispatcher mDispatcher;
	public RconCommand()
	{
		mDispatcher = new CommandDispatcher("Provides access to all rcon commands");
		
		mDispatcher.registerCommand(new AccountCommand());
		mDispatcher.registerCommand(new WhoCommand());
		mDispatcher.registerCommand(new KickCommand());
		mDispatcher.registerCommand(new ReloadCommand());
	}
	
	@Override
	public boolean hasPermission(final Invocation invocation) {
		return invocation.source().hasPermission("rcon.base");
	}

	@Override
	public void execute(final Invocation invocation) {
		CommandSource source = invocation.source();
		String[] args = invocation.arguments();
		mDispatcher.dispatchCommand(source, "/", "brcon", args);
	}

	/*@Override
	public List<String> suggest(final Invocation invocation) {
		return List.of();
	}*/

	/*@Override
	public Iterable<String> onTabComplete( CommandSource sender, String[] args )
	{
		return mDispatcher.tabComplete(sender, "/", "brcon", args);
	}*/
}
