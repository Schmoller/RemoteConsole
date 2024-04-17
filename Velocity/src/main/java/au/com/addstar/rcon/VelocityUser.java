package au.com.addstar.rcon;

import java.util.logging.LogRecord;

import au.com.addstar.rcon.network.NetworkManager;
import au.com.addstar.rcon.network.packets.main.PacketOutMessage;
import au.com.addstar.rcon.network.packets.main.PacketOutMessage.MessageType;
import au.com.addstar.rcon.server.User;
import au.com.addstar.rcon.util.Message;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.command.CommandSource;

public class VelocityUser extends User
{
	private UserCommandSource mSender;
	
	public VelocityUser(String name)
	{
		super(name);
	}
	
	@Override
	public void setManager( NetworkManager manager )
	{
		super.setManager(manager);
		
		if(manager == null)
			mSender = null;
		else
			mSender = new UserCommandSource(this);
	}
	
	public CommandSource asCommandSender()
	{
		return mSender;
	}
	
	public void sendLog( LogRecord record )
	{
		MessageType type = MessageType.Log;
		if(record.getThrown() != null)
			type = MessageType.Exception;
		
		int threadId = record.getThreadID();
		String threadName = "";
		for(Thread thread : Thread.getAllStackTraces().keySet())
		{
			if(thread.getId() == threadId)
			{
				threadName = thread.getName();
				break;
			}
		}
		
		String text = record.getMessage();
		if(text == null)
			text = "";
		
		if(record.getParameters() != null)
		{
			for(int i = 0; i < record.getParameters().length; ++i)
				text = text.replace("{" + i + "}", String.valueOf(record.getParameters()[i]));
		}

		ProxyServer proxy = RemoteConsolePlugin.instance.proxyServer;
		final Message message = new Message(text, type, record.getMillis(), record.getLevel(), threadName, record.getLoggerName());

		proxy.getScheduler().buildTask(RemoteConsolePlugin.instance, new Runnable()
		{
			@Override
			public void run()
			{
				getManager().sendPacket(new PacketOutMessage(message));
			}
		}).schedule();
	}
}
