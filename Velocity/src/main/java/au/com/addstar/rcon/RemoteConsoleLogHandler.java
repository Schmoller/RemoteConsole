package au.com.addstar.rcon;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import au.com.addstar.rcon.server.RconServer;
import au.com.addstar.rcon.server.ServerNetworkManager;

public class RemoteConsoleLogHandler extends Handler
{
	@Override
	public void publish( LogRecord record )
	{
		for(ServerNetworkManager connection : RconServer.instance.getConnections())
		{
			VelocityUser user = (VelocityUser)connection.getUser();
			if(user != null)
				user.sendLog(record);
		}
	}

	@Override
	public void flush()
	{
	}

	@Override
	public void close() throws SecurityException
	{
	}

}
