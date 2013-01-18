package eFrame.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.utils.Configuration;
import com.utils.DateUtil;

import eFrame.exception.ServiceInitException;

/**
 * netty服务器启动类 <br>
 * 
 * @date 2013-1-6
 * @author LiangRL
 * @alias E.E.
 */
public class Server {

	private Logger log = Logger.getLogger(Server.class);

	private static boolean isRunning = false;
	
	/** 服务器实例 */
	private static Server instance;

	private ServerBootstrap bootstrap = null;

	private int port;

	private Channel c = null;
	
	private Server() {
		try {
			String temp = Configuration.getInstance().get("netty.port");
			port = Integer.parseInt(temp);
			bootstrap = new ServerBootstrap(
					new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
							Executors.newCachedThreadPool()));			
		} catch (Exception e) {
			throw new ServiceInitException("ERROR constructing server! cause:"+e.getMessage(), e);
		}
	}

	public static Server getInstance() {
		if (instance == null) {
			instance = new Server();
		}
		return instance;
	}

	public void start() {
		if(isRunning){
			throw new ServiceInitException("service is launched！");
		}
		try {
			bootstrap.setPipelineFactory(new ServerPipelineFactory());

			bootstrap.setOption("child.tcpNoDelay", Configuration.getInstance().get("netty.tcpNoDelay"));
			bootstrap.setOption("child.keepAlive", Configuration.getInstance().get("netty.keepAlive"));
			isRunning = true;
			c = bootstrap.bind(new InetSocketAddress(port));
			log.info("server start at:"
					+ DateUtil.getCurrentDateStr(DateUtil.YMD_HMS));
		} catch (Exception e) {
			log.error("server start failed!", e);
			System.exit(-1);
		}
	}

	public void stop() {
		if(c!=null){
			c.close();			
		}
		bootstrap.releaseExternalResources();
		isRunning = false;
		System.exit(-1);
		log.info("server stopped at:"
				+ DateUtil.getCurrentDateStr(DateUtil.YMD_HMS));
	}
}
