package eFrame.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import eFrame.exception.ServiceInitException;
import eFrame.utils.Configuration;
import eFrame.utils.DateUtil;

/**
 * netty服务器启动类 <br>
 * 
 * @date 2013-1-6
 * @author LiangRL
 * @alias E.E.
 */
public class Server {

	private Logger log = Logger.getLogger(Server.class);

	private boolean isRunning = false;
	
	/** 服务器实例 */
	private static Server instance;

	private ServerBootstrap bootstrap = null;

	private int port;

	private Server() {
		try {
			String temp = Configuration.getInstance().get("port");
			port = Integer.parseInt(temp);
			bootstrap = new ServerBootstrap(
					new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
							Executors.newCachedThreadPool()));			
		} catch (Exception e) {
			throw new ServiceInitException("ERROR getting port!", e);
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
		Channel c = null;
		try {
			bootstrap.setPipelineFactory(new ServerPipelineFactory());
			/**
			 * TCP为了防止在网络中充满大量的小数据包，
			 * 因此它设置了一个Nagle算法，当A请求B服务器的时候，如果A的数据包小于132字节，然后B服务器就会等待，
			 * 对A发来的数据包，暂时不进行ACK确认，当A发过来的数据包达到一定量的时候，B才会对数据包确认，
			 * 但这有一个超时时间，就是如果A发了数据包小于132字节，超过40ms后，还是没有再发送，那么B服务器也会对这个数据包确认.
			 * 当你关闭Nagle算法后，客户端发送了小数据包过去，小于132字节，因为这时候服务器会延迟40ms确认，
			 * 但你关闭了Nagle算法后，就是告诉客户端，我不需要等待服务器ACK确认，也继续发送数据包，客户端发送数据包就快了，不需要等待。
			 * tcpNoDelay:true 不进行延迟；false:进行延迟
			 * */
			bootstrap.setOption("tcpNoDelay", "true");
			c = bootstrap.bind(new InetSocketAddress(port));
			isRunning = true;
			log.info("server start at:"
					+ DateUtil.getCurrentDateStr(DateUtil.YMD_HMS));
		} catch (Exception e) {
			c.disconnect();
			log.error("server start failed!", e);
		}
	}

	public void stop() {
		bootstrap.releaseExternalResources();
		isRunning = false;
		log.info("server stopped at:"
				+ DateUtil.getCurrentDateStr(DateUtil.YMD_HMS));
	}

	public static void main(String[] args){
		Server server = Server.getInstance();
		server.start();
	}
}
