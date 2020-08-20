package kristi71111.bungeekickbaco;

import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class PlayerListener implements Listener {

    BungeeKickBaco plugin;

    public PlayerListener(BungeeKickBaco plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerKickEvent(ServerKickEvent ev) {
        ServerInfo kickedFrom;

        if (ev.getPlayer().getServer() != null) {
            kickedFrom = ev.getPlayer().getServer().getInfo();
        } else if (this.plugin.getProxy().getReconnectHandler() != null) {
            kickedFrom = this.plugin.getProxy().getReconnectHandler().getServer(ev.getPlayer());
        } else {
            kickedFrom = AbstractReconnectHandler.getForcedHost(ev.getPlayer().getPendingConnection());
            if (kickedFrom == null) {
                if (ev.getPlayer().getPendingConnection().getListener().getServerPriority() != null) {
                    kickedFrom = ProxyServer.getInstance().getServerInfo(ev.getPlayer().getPendingConnection().getListener().getServerPriority().get(0));
                } else {
                    kickedFrom = ProxyServer.getInstance().getServerInfo(ev.getPlayer().getPendingConnection().getListener().getDefaultServer());
                }
            }
        }
        if(Checker.isReachable(kickedFrom.getAddress())){
           return;
        }

        ServerInfo kickTo = this.plugin.getProxy().getServerInfo(BungeeKickBaco.config.getString("ServerName"));

        if (kickedFrom != null && kickedFrom.equals(kickTo)) {
            return;
        }

        ev.setCancelled(true);
        ev.setCancelServer(kickTo);
        if (BungeeKickBaco.config.getBoolean("ShowKickMessage")) {
            String msg = BungeeKickBaco.config.getString("KickMessage");
            msg = ChatColor.translateAlternateColorCodes('&', msg);
            String kmsg = ChatColor.stripColor(BaseComponent.toLegacyText(ev.getKickReasonComponent()));
            msg = msg + kmsg;
            ev.getPlayer().sendMessage(new TextComponent(msg));
        }
    }
}
class Checker{
    public static boolean isReachable(InetSocketAddress address) {
        int pingTimeout = 1000;
        Socket socket = new Socket();
        try {
            socket.connect(address, pingTimeout);
            socket.close();
            return true;
        } catch(IOException ignored) {
        }
        return false;
    }
}
