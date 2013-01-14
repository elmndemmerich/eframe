package eFrame.server;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.util.CharsetUtil;

import eFrame.constants.Encoding;
import eFrame.server.http.ServerHttpRequest;

/**
 * 请求分发器
 * <br>
 * 与route.xml对应。
 * @date 2013-1-5
 * @author LiangRL
 * @alias E.E.
 */
public class ServerHandler extends SimpleChannelUpstreamHandler{

	private Logger logger = Logger.getLogger(ServerHandler.class);
	
	/** 如果路由表匹配不上， 是不是直接匹配 */
	private boolean catchAll = false;
	/** 有没有过滤器 */
	private boolean hasFilter = false;
	
	//读取路由表存放在这里
	static{
		
	}
	
	/**
	 * 根据请求， 分发
	 * @param ctx
	 * @param e
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		DefaultHttpRequest nettyRequest = (DefaultHttpRequest) e.getMessage();
		String remoteAddress = 
				((InetSocketAddress) ctx.getChannel().getRemoteAddress()).getAddress().getHostAddress();
		//自定义请求
		ServerHttpRequest request = new ServerHttpRequest(nettyRequest, remoteAddress);
		System.out.println(request.getParams());
		//网页页面图标
		if(request.getUri().equalsIgnoreCase("/favicon.ico")){
			return;
		}		
		sendTestMsg(ctx, HttpResponseStatus.OK, 
				"the server is:"+request.getBasePath()+
				"the request uri is:"+request.getUri());
	}

	/**
	 * netty必须实现的方法。
	 * netty底层处理异常
	 * @param ctx
	 * @param e
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		Channel ch = e.getChannel();
		Throwable cause = e.getCause();
		logger.error(cause, cause);
		if (cause instanceof TooLongFrameException) {
			sendError(ctx, HttpResponseStatus.BAD_REQUEST);
			return;
		}
		if (ch.isConnected()) {
			sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
			return;
		}
	}

	private void sendTestMsg(ChannelHandlerContext ctx, HttpResponseStatus status, String msg) {
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
		response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset="+Encoding.UTF_8);
		response.setContent(ChannelBuffers.copiedBuffer(msg, CharsetUtil.UTF_8));
		ctx.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);
	}	
	
	/**
	 * 一般异常情况
	 * @param ctx
	 * @param status
	 */
	private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
		response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset="+Encoding.UTF_8);
		response.setContent(ChannelBuffers.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
		ctx.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);
	}
}
