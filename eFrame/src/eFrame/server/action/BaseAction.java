package eFrame.server.action;

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
	
	private ServerHttpRequest request;

	public ServerHttpRequest getRequest() {
		return request;
	}

	public void setRequest(ServerHttpRequest request) {
		this.request = request;
	}
	
}
