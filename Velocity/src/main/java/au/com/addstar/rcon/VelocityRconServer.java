package au.com.addstar.rcon;

import au.com.addstar.rcon.server.RconServer;
import au.com.addstar.rcon.server.auth.IUserStore;
import au.com.addstar.rcon.server.auth.StoredPassword;

public class VelocityRconServer extends RconServer
{
	private String mConsoleFormat;
	
	public VelocityRconServer(int port, String name, IUserStore userstore)
	{
		super(port, name, userstore);
	}
	
	protected VelocityUser createUser(String name)
	{
		return new VelocityUser(name);
	}
	
	@Override
	public boolean createUser( String name, StoredPassword password, boolean restricted )
	{
		VelocityUser user = createUser(name);
		user.setPassword(password);
		user.setIsRestricted(restricted);
		return addUser(user);
	}
	
	@Override
	public String getConsoleFormat()
	{
		if(mConsoleFormat == null)
		{
			String date = System.getProperty("net.md_5.bungee.log-date-format", "HH:mm:ss");
			mConsoleFormat = String.format("%%d{%s} [%%level]: %%msg%%n", date);
		}
		
		return mConsoleFormat;
	}
}
