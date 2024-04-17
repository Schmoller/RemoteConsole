package au.com.addstar.rcon;

import java.util.logging.Logger;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;

public class UserCommandSource implements CommandSource
{
	private VelocityUser mUser;
	private ProxyServer proxyServer;
	private Logger logger;
	
	public UserCommandSource(VelocityUser user)
	{
		mUser = user;
		this.proxyServer = RemoteConsolePlugin.instance.proxyServer;
		this.logger = RemoteConsolePlugin.instance.logger;
	}
	
	public String getName()
	{
		return mUser.getName();
	}

	/*
        public void sendMessage( String message )
        {
            mUser.getManager().sendPacket(new PacketOutMessage(new Message(message, MessageType.Directed, logger.getName())));
        }

        public void sendMessage( String message, MessageType type )
        {
            mUser.getManager().sendPacket(new PacketOutMessage(new Message(message, type, logger.getName())));
        }

        public void sendMessages( String... messages )
        {
            for(String message : messages)
                sendMessage(message);
        }

        @Override
        public void sendMessage( BaseComponent... messages )
        {
            for(BaseComponent message : messages)
                sendMessage(message.toLegacyText());
        }

        @Override
        public void sendMessage( BaseComponent message )
        {
            sendMessage(message.toLegacyText());
        }

        @Override
        public Collection<String> getGroups()
        {
            return Collections.emptySet();
        }

        @Override
        public void addGroups( String... groups )
        {
            throw new UnsupportedOperationException("Console may not have groups");
        }

        @Override
        public void removeGroups( String... groups )
        {
            throw new UnsupportedOperationException("Console may not have groups");
        }
    */

	public void sendMessage(String messages)
	{
		sendPlainMessage(messages);
	}

	@Override
	public void sendPlainMessage(@NotNull String message) {
		CommandSource.super.sendPlainMessage(message);
	}

	@Override
	public Tristate getPermissionValue(String s) {
		return null;
	}
}
