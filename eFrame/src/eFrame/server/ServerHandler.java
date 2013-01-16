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

import eFrame.annotations.ActionBean;
import eFrame.constants.Encoding;
import eFrame.container.EntityContainer;
import eFrame.exception.DispatchRequestException;
import eFrame.exception.ServiceInitException;
import eFrame.route.Route;
import eFrame.route.RouteMapping;
import eFrame.server.action.BaseAction;
import eFrame.server.http.ServerHttpRequest;
import eFrame.utils.Configuration;

/**
 * 请求分发器
 * <br>
 * 与route.xml对应。
 * @date 2013-1-5
 * @author LiangRL
 * @alias E.E.
 */
public class ServerHandler extends SimpleChannelUpstreamHandler{

	private static Logger logger = Logger.getLogger(ServerHandler.class);
	
	/** 有没有过滤器。先略过。 */
	private boolean hasFilter = false;
	/** 路由映射相关。并且是否全匹配在此映射中。 */
	private RouteMapping routeMapping = RouteMapping.getInstance();
	
	private static EntityContainer container = EntityContainer.getInstance();
	
	static{
		try{
			String configs = Configuration.getInstance().get("context.properties", "toScanPackage");
			container.invoke(configs.split(","));
		}catch(Exception e){
			throw new ServiceInitException("init container error!",e);
		}
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
		//网页页面图标
		if(nettyRequest.getUri().equalsIgnoreCase("/favicon.ico")){
			ctx.sendUpstream(e);
		}		
		//自定义请求
		ServerHttpRequest request = new ServerHttpRequest(nettyRequest, remoteAddress);
		//如果没有这个路由
		String requestType = request.getMethod().getName();
		String uri = request.getUri();		
		Route existsRoute = routeMapping.getRoute(uri, requestType);
		if(existsRoute==null){
			sendNotFound(ctx, HttpResponseStatus.OK, 
					"no route found.</br>the request uri is:"+request.getUri());					
		}else{
			//有这个路由
			dispatchRequest(request, existsRoute);
		}
	}
	
	/**
	 * 分发请求
	 * step1:查看路由映射中有没有；
	 * step2:能否catch all。
	 * step3:就是没有了就说没有~
	 * @param request 请求
	 * @param route 路由
	 */
	private void dispatchRequest(ServerHttpRequest request, Route route){
		String[] temp = route.getMethod().split(".");
		String action = temp[0];
		String method = temp[1];
		Object obj = container.getBean(action);
		try {
			if(obj instanceof BaseAction && obj.getClass().isAnnotationPresent(ActionBean.class)){
				obj.getClass().getMethod("setRequest").invoke(request);
				ActionBean ab = obj.getClass().getAnnotation(ActionBean.class);
				obj.getClass().getMethod("setResponse").invoke(ab.resultType(), ab.responseCharset());
				obj.getClass().getMethod(method).invoke(obj);			
			}else{
				throw new DispatchRequestException("error invoking dispatchRequest！"+obj.getClass().getName());				
			}
		} catch (Exception e) {
			throw new DispatchRequestException("error invoking dispatchRequest！", e);
		}
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

	private void sendNotFound(ChannelHandlerContext ctx, HttpResponseStatus status, String msg) {
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
