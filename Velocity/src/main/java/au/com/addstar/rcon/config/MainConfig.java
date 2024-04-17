package au.com.addstar.rcon.config;

import net.cubespace.Yamler.Config.Comment;
import net.cubespace.Yamler.Config.Comments;
import net.cubespace.Yamler.Config.YamlConfig;
import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.cubespace.Yamler.Config.Path;

public class MainConfig extends YamlConfig
{
	public int port = 22050;
	
	@Comments({"The storage mode (where the account data will be stored)","Valid values are:","file, mysql"})
	public String store = "file";
	
	@Comment("The server name to display to the client.")
	public String serverName = "Proxy";

	@Comment("Set True to enable dubug mode")
	public boolean debug = false;

	@Path("database.host")
	public String databaseHost = "localhost:3306";
	@Path("database.database")
	public String databaseName = "RemoteConsole";
	@Path("database.username")
	public String databaseUsername = "user";
	@Path("database.password")
	public String databasePassword = "password";
	@Path("database.useSSL")
	public String databaseUseSSL = "false";
	
	public void checkValid() throws InvalidConfigurationException
	{
		if(port < 0 || port > 65535)
			throw new InvalidConfigurationException("Port number must be between 0 and 65535");
		
		if(!store.equalsIgnoreCase("file") && !store.equalsIgnoreCase("mysql"))
			throw new InvalidConfigurationException("Store type must be 'file' or 'mysql'");
	}
	
}
