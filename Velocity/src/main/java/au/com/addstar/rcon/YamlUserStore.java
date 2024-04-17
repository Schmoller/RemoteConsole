package au.com.addstar.rcon;

import java.io.File;
import java.io.IOException;

import net.cubespace.Yamler.Config.InvalidConfigurationException;

import au.com.addstar.rcon.config.UserConfig;
import au.com.addstar.rcon.config.UserStorageConfig;
import au.com.addstar.rcon.server.User;
import au.com.addstar.rcon.server.auth.IUserStore;
import au.com.addstar.rcon.server.auth.StoredPassword;

public class YamlUserStore implements IUserStore
{
	private File mStorageFile;
	private UserStorageConfig mConfig;
	
	public YamlUserStore(File file)
	{
		mStorageFile = file;
	}
	
	@Override
	public void initialize() throws IOException
	{
		try
		{
			mConfig = new UserStorageConfig();
			mConfig.init(mStorageFile);
		}
		catch ( InvalidConfigurationException e )
		{
			throw new IOException(e);
		}
	}

	@Override
	public void shutdown() throws IOException
	{
	}

	@Override
	public boolean loadUser( User user ) throws IOException
	{
		if(!mConfig.users.containsKey(user.getName()))
			return false;
		
		UserConfig data = mConfig.users.get(user.getName());
		
		if(data.password != null)
		{
			String[] parts = data.password.split(":");
			if(parts.length == 2)
				user.setPassword(new StoredPassword(parts[0], parts[1]));
		}
		
		return true;
	}

	@Override
	public void saveUser( User user ) throws IOException
	{
		UserConfig data = mConfig.users.get(user.getName());
		
		if(data == null)
		{
			data = new UserConfig();
			mConfig.users.put(user.getName(), data);
		}
		
		data.password = String.format("%s:%s", user.getPassword().getHash(), user.getPassword().getSalt());
		
		try
		{
			mConfig.save();
		}
		catch(InvalidConfigurationException e)
		{
			throw new IOException(e);
		}
	}

	@Override
	public void addUser( User user ) throws IOException
	{
		saveUser(user);
	}

	@Override
	public void removeUser( User user ) throws IOException
	{
		if(mConfig.users.remove(user.getName()) != null)
		{
			try
			{
				mConfig.save();
			}
			catch(InvalidConfigurationException e)
			{
				throw new IOException(e);
			}
		}
	}

}
