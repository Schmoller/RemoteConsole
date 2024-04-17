package au.com.addstar.rcon;

import au.com.addstar.rcon.commands.RconCommand;
import au.com.addstar.rcon.config.MainConfig;
import au.com.addstar.rcon.network.HandlerCreator;
import au.com.addstar.rcon.network.NetworkManager;
import au.com.addstar.rcon.network.handlers.INetworkHandler;
import au.com.addstar.rcon.server.RconServer;
import au.com.addstar.rcon.server.ServerLoginHandler;
import au.com.addstar.rcon.server.auth.IUserStore;
import au.com.addstar.rcon.server.auth.MySQLUserStore;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import net.cubespace.Yamler.Config.InvalidConfigurationException;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Plugin(id = "RemoteConsole", version = "${plugin.version}", authors = {"Schmoller"})
public class RemoteConsolePlugin
{
	public static RemoteConsolePlugin instance;

	public ProxyServer proxyServer;
	public Logger logger;
	private RconServer mServer;
	private RemoteConsoleLogHandler mLogHandler;
	private MainConfig mConfig;

	private Formatter mFormatter;
	private final Path pluginDir;

	@Inject
	public RemoteConsolePlugin(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
		this.proxyServer = server;
		this.logger = logger;
		this.pluginDir = dataDirectory;

		logger.info("Hello there, it's a test plugin I made!");
	}

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		instance = this;
		mConfig = new MainConfig();
		
		try
		{
			mConfig.init(new File(pluginDir.toFile(), "config.yml"));
			mConfig.checkValid();
		}
		catch ( InvalidConfigurationException e )
		{
			System.err.println("[RCON] Unable to start RconServer. Error loading config:");
			e.printStackTrace();
			return;
		}
		
		HandlerCreator creator = new HandlerCreator()
		{
			@Override
			public INetworkHandler newHandlerLogin( NetworkManager manager )
			{
				manager.setDebug(mConfig.debug);
				return new ServerLoginHandler(manager);
			}
			
			@Override
			public INetworkHandler newHandlerMain( NetworkManager manager )
			{
				manager.setDebug(mConfig.debug);
				return new NetHandler(manager);
			}
		};
		
		IUserStore userstore = null;
		
		if(mConfig.store.equalsIgnoreCase("mysql")) {
			Properties props = new Properties();
			props.put("user",mConfig.databaseUsername);
			props.put("password",mConfig.databasePassword);
			props.put("useSSL",mConfig.databaseUseSSL);
			userstore = new MySQLUserStore(mConfig.databaseHost, mConfig.databaseName,props);

		}
		else
			userstore = new YamlUserStore(new File(pluginDir.toFile(), "users.yml"));
		
		String serverName = mConfig.serverName;
		if(serverName == null)
			serverName = "Proxy";
		
		installLogHandler();
		
		mServer = new VelocityRconServer(mConfig.port, serverName, userstore);
		
		loadWhitelist();
		
		try
		{
			logger.info("Starting RconServer on port " + mConfig.port);
			mServer.start(creator);
			mServer.openServer();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			mServer = null;
			return;
		}
		
		CommandMeta commandMeta = proxyServer.getCommandManager().metaBuilder("brcon").plugin(this).build();
		proxyServer.getCommandManager().register(commandMeta, new RconCommand());
	}

	private void installLogHandler()
	{
		mFormatter = null;
		for(Handler handler : logger.getHandlers())
		{
			if(handler instanceof FileHandler && handler.getFormatter() != null)
				mFormatter = handler.getFormatter();
			
			if(mFormatter != null)
				break;
		}
		
		mLogHandler = new RemoteConsoleLogHandler();
		if(mFormatter != null){mLogHandler.setFormatter(mFormatter);}
		else{
			System.out.println("[RemoteConsole]Log formatter was null thus the console handler " +
				"has no formatter set");
			mFormatter = new SimpleFormatter();
			mLogHandler.setFormatter(mFormatter);
		}
		logger.addHandler(mLogHandler);
	}
	
	public static String formatMessage(String message)
	{
		message = instance.mFormatter.format(new LogRecord(Level.INFO, message));
		if(message.endsWith("\n"))
			message = message.substring(0, message.length()-1);
		
		return message;
	}
	
	public boolean loadWhitelist()
	{
		File whitelist = new File(pluginDir.toFile(), "whitelist.txt");
		if (whitelist.exists())
		{
			try
			{
				mServer.getWhitelist().load(whitelist);
			}
			catch (IOException e)
			{
				logger.severe("Failed to load whitelist:");
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
}
