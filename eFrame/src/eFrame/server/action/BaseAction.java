package eFrame.server.action;

import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

import eFrame.annotations.ActionType;
import eFrame.server.http.ServerHttpRequest;

/**
 * action类都继承这个类
 * <br>
 * 这里有request， response，cookies,session等。session用memcached或者map实现。
 * @date 2013-1-5
 * @author LiangRL
 * @alias E.E.
 */
public abstract class BaseAction {
	
	protected HttpResponse response;
	
	protected ServerHttpRequest request;

	public ServerHttpRequest getRequest() {
		return request;
	}

	public void setRequest(ServerHttpRequest request) {
		this.request = request;
	}

	/**
	 * 在请求分发的阶段，初始化response，并设定response的content type。
	 * @param actionType
	 * @param responseCharset
	 */
	public void setResponse(ActionType actionType, String responseCharset) {
		response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		if(actionType==ActionType.page){
			response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset="+responseCharset);			
		}else if(actionType==ActionType.file){
			response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "multipart/form-data; charset="+responseCharset);			
		}else if(actionType==ActionType.json){
			response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "application/json; charset="+responseCharset);			
		}else if(actionType==ActionType.xml){
			response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "application/xml; charset="+responseCharset);			
		}
	}	
	
}
