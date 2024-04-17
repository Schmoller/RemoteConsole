package au.com.addstar.rcon;

import au.com.addstar.rcon.network.NetworkManager;
import au.com.addstar.rcon.network.handlers.AbstractNetworkHandler;
import au.com.addstar.rcon.network.handlers.INetworkMainHandlerServer;
import au.com.addstar.rcon.network.packets.main.*;
import au.com.addstar.rcon.network.packets.main.PacketOutMessage.MessageType;
import au.com.addstar.rcon.server.RconServer;
import au.com.addstar.rcon.server.ServerNetworkManager;
import au.com.addstar.rcon.server.auth.StoredPassword;
import au.com.addstar.rcon.util.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class NetHandler extends AbstractNetworkHandler implements INetworkMainHandlerServer
{
	public NetHandler(NetworkManager manager)
	{
		super(manager);
	}
	
	@Override
	public void handleCommand( final PacketInCommand packet )
	{
		ProxyServer.getInstance().getScheduler().schedule(RemoteConsolePlugin.instance, new Runnable()
		{
			@Override
			public void run()
			{
				VelocityUser user = (VelocityUser)((ServerNetworkManager)getManager()).getUser();
				
				if (user.isRestricted())
				{
					if (!RconServer.instance.getWhitelist().isWhitelisted(packet.command))
					{
						user.asCommandSender().sendMessage(TextComponent.fromLegacyText(ChatColor.RED + "You are not permitted to use that command"));
						return;
					}
				}

				if(!ProxyServer.getInstance().getPluginManager().dispatchCommand(user.asCommandSender(), packet.command))
					user.asCommandSender().sendMessage(TextComponent.fromLegacyText(ChatColor.RED + "Command not found"));
			}
		}, 0, TimeUnit.MILLISECONDS);
	}

	@Override
	public void handleTabComplete( final PacketInTabComplete packet )
	{
		ProxyServer.getInstance().getScheduler().schedule(RemoteConsolePlugin.instance, new Runnable()
		{
			@Override
			public void run()
			{
				VelocityUser user = (VelocityUser)((ServerNetworkManager)getManager()).getUser();
				
				if (user.isRestricted())
				{
					if (!RconServer.instance.getWhitelist().isWhitelisted(packet.message))
					{
						getManager().sendPacket(new PacketOutTabComplete(Collections.<String>emptyList()));
						return;
					}
				}
				
				ArrayList<String> results = new ArrayList<>();

				ProxyServer.getInstance().getPluginManager().dispatchCommand(user.asCommandSender(), packet.message, results);
				getManager().sendPacket(new PacketOutTabComplete(results));
					
			}
		}, 0, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public void handlePassword( final PacketInPassword packet )
	{
		ProxyServer.getInstance().getScheduler().schedule(RemoteConsolePlugin.instance, new Runnable()
		{
			@Override
			public void run()
			{
				VelocityUser user = (VelocityUser)((ServerNetworkManager)getManager()).getUser();
				StoredPassword current = user.getPassword();
				
				if (current.matches(packet.previousPassword))
				{
					user.setPassword(StoredPassword.generate(packet.newPassword));
					if(!RconServer.instance.saveUser(user))
						getManager().sendPacket(new PacketOutMessage(new Message(ChatColor.RED + "Unable to save changes, an internal error occured.", MessageType.Directed, "RemoteConsole")));
					else
						getManager().sendPacket(new PacketOutMessage(new Message(ChatColor.YELLOW + "Your password has been updated.", MessageType.Directed, "RemoteConsole")));
				}
				else
					getManager().sendPacket(new PacketOutMessage(new Message(ChatColor.RED + "Your password did not match your current password", MessageType.Directed, "RemoteConsole")));
			}
		}, 0, TimeUnit.MILLISECONDS);
	}
}
