package au.com.addstar.rcon.config;

import java.util.HashMap;

import net.cubespace.Yamler.Config.YamlConfig;

public class UserStorageConfig extends YamlConfig
{
	public HashMap<String, UserConfig> users = new HashMap<>();
}
