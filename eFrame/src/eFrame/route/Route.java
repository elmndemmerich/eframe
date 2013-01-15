package eFrame.route;

/**
 * 路由配置。
 * <br>
 * @date 2013-1-9
 * @author LiangRL
 * @alias E.E.
 */
public class Route {

	private String reqMethodType;
	private String url;
	private String method;	
	
	public Route(String reqMethodType, String url, String method) {
		super();
		this.reqMethodType = reqMethodType;
		this.url = url;
		this.method = method;
	}
	public Route() {
		super();
	}
	
	public String getReqMethodType() {
		return reqMethodType;
	}
	public String getUrl() {
		return url;
	}
	public String getMethod() {
		return method;
	}
	public void setReqMethodType(String reqMethodType) {
		this.reqMethodType = reqMethodType;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
	/**
	 * 三者都相等才是相等
	 * @param route
	 * @return
	 */
	public boolean equals(Route route){
		boolean flagUrl = this.url.equalsIgnoreCase(route.getUrl());
		boolean flagMethod = this.method.equalsIgnoreCase(route.getMethod());
		boolean flagType = this.reqMethodType.equalsIgnoreCase(route.getReqMethodType());		
		return flagType&&flagUrl&&flagMethod;
	}
}
