package eFrame.server.http;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.COOKIE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;

import eFrame.server.FileChannelBuffer;
import eFrame.server.http.paramParser.TextParser;
import eFrame.server.http.paramParser.base.DataParser;
import eFrame.utils.Configuration;

/**
 * 自定义请求
 * <br>
 * @date 2013-1-10
 * @author LiangRL
 * @alias E.E.
 */
public class ServerHttpRequest extends DefaultHttpRequest{

	private String basePath;
	private String contentType;
	private static String encoding;
	private String querystring="";
	private String reqAddress;
	private InputStream body = null;
	private Map<String, eFrame.server.http.Cookie> cookies = null;
	private Map<String, String> params = new HashMap<String, String>();
	/**
	 * 构造这个request对象。流先是在body中。
	 * 然后第一次取params的时候，检查body对象（不关闭流）。
	 * 此标志位用于说明取出流信息了没有。
	 * */
	private boolean hasTransferParam = false;
	/**is https?*/
	private boolean secure = false;

	static{
		try {
			encoding = Configuration.getInstance().get("context.properties", "encoding");
		} catch (Exception e) {
			throw new RuntimeException("error getting encoding!");
		}		
	}
	
	/**
	 * netty的请求转化为自定义的请求
	 * @param nettyRequest
	 * @param remoteAddress
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public ServerHttpRequest(DefaultHttpRequest nettyRequest, String remoteAddress) throws NumberFormatException, Exception {
		super(HttpVersion.HTTP_1_1, nettyRequest.getMethod(), nettyRequest.getUri());
		this.setContent(nettyRequest.getContent());
		//请求方的地址
		reqAddress = remoteAddress;
		//header
		for(String headerName : nettyRequest.getHeaderNames()){
			this.setHeader(headerName, nettyRequest.getHeaders(headerName));
		}
		//cookies
		cookies = getCookies(nettyRequest);
		//path
		basePath = "http://"+getHost()+"/";
		//ct
		contentType = nettyRequest.getHeader(CONTENT_TYPE);

		setParams();
	}	
	
	public ServerHttpRequest(HttpMethod method, String uri) {
		super(HttpVersion.HTTP_1_1, method, uri);
	}
	
	public ServerHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri) {
		super(httpVersion, method, uri);
	}

	/**
	 * 设置get请求的参数
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	private void setGetParam() throws UnsupportedEncodingException, Exception{
        final int i = this.getUri().indexOf("?");
        if (i == -1) {
        	querystring = "";
        	return;
        }		
        //？后面的字符串
        querystring = this.getUri().substring(i + 1);        
        //UTF-8解码
        querystring = java.net.URLDecoder.decode(querystring, 
        		Configuration.getInstance().getDefault("encoding", "UTF-8".intern()));
        Str2Params(querystring);
	}
	
	private void Str2Params(String str){
        String[] arrParams = str.split("&");
        for(String temp:arrParams){
        	String[] arr = temp.split("=");
        	this.params.put(arr[0], arr[1]);
        }		
	}
	
	/**
	 * POST请求
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	private void setPostParam() throws UnsupportedEncodingException, Exception{
        //可能是post请求
        ChannelBuffer b = this.getContent();
        if (b instanceof FileChannelBuffer) {
            FileChannelBuffer buffer = (FileChannelBuffer) b;
            Integer max = Integer.valueOf(
            			Configuration.getInstance().getDefault("netty.maxContentLength", "-1"));
            body = buffer.getInputStream();
            if (!(max == -1 || body.available() < max)) {
                body = new ByteArrayInputStream(new byte[0]);
            }
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(new ChannelBufferInputStream(b), out);
            byte[] n = out.toByteArray();
            body = new ByteArrayInputStream(n);
        }      
	}	
	
	/**
	 * 设置get参数。
	 * post的信息在body中， 先不从流转化为参数；
	 * 可能body的内容是文件？
	 * @param nettyRequest
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	private void setParams() throws NumberFormatException, Exception{
		setGetParam();
		setPostParam();
	}
	
	/**
	 * netty的request是没有cookie对象的。netty的cookies都在header。
	 * 所以取出，放置在自定义的cookies中。
	 * @param nettyRequest
	 * @return
	 */
	private Map<String, eFrame.server.http.Cookie> getCookies(HttpRequest nettyRequest) {
        Map<String, eFrame.server.http.Cookie> cookies = new HashMap<String, eFrame.server.http.Cookie>(8);
        String value = nettyRequest.getHeader(COOKIE);
        if (value != null) {
            Set<Cookie> cookieSet = new CookieDecoder().decode(value);
            if (cookieSet != null) {
                for (Cookie cookie : cookieSet) {
                	eFrame.server.http.Cookie myCookie = new eFrame.server.http.Cookie();
                    myCookie.name = cookie.getName();
                    myCookie.domain = cookie.getDomain();
                    myCookie.secure = cookie.isSecure();
                    myCookie.value = cookie.getValue();
                    myCookie.httpOnly = cookie.isHttpOnly();
                    cookies.put(myCookie.name, myCookie);
                }
            }
        }
        return cookies;
    }	
	
	/**
	 * 懒加载，把请求体放到本request的参数map中
	 * 不再关闭流
	 * @throws IOException
	 */
    private void checkAndParse() {
    	try{
        	if(this.body==null){
        		return;
        	}    	
            if (this.getContentType() == null) {
            	return;
            }
            //这个http请求没问题才做下面的步骤
        	DataParser dataParser = DataParser.parsers.get(contentType);
            if (dataParser != null) {
            	Map<String, String[]> params = dataParser.parse(this.body);
            	String strBody = params.get("body")[0];
            	this.Str2Params(strBody);
            	this.params.put("body", strBody);
            } else {
                if (contentType.startsWith("text/")) {
                	Map<String, String[]> params =  new TextParser().parse(this.body);
                	String strBody = params.get("body")[0];
                	this.Str2Params(strBody);
                	this.params.put("body", strBody);
                }
            }            		
    	}finally{
    		this.hasTransferParam=true;
    	}
    }	
	
    /**
     * 在获取参数的时候才对body进行解析内容。
     * @return
     */
	public Map<String, String> getParams() {
		if(!this.hasTransferParam){
			checkAndParse();			
		}
		return params;
	}	
	
	/** server:port的形式 */
	public String getHost(){
		return super.getHeader("HOST");
	}
	
	public String getContentType() {
		return contentType;
	}	
	
	public String getBasePath(){
		return basePath;
	}	
	
	public String getUserAgent(){
		return super.getHeader("User-Agent");
	}	
	
	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String _encoding) {
		encoding = _encoding;
	}	
	
	public String getQuerystring() {
		return querystring;
	}	
	
	public String getReqAddress() {
		return reqAddress;
	}

	public void setReqAddress(String reqAddress) {
		this.reqAddress = reqAddress;
	}	
	
	public InputStream getBody() {
		return body;
	}	
	public boolean isSecure() {
		return secure;
	}	
	public Map<String, eFrame.server.http.Cookie> getCookies(){
		return this.cookies;
	}
	public eFrame.server.http.Cookie getCookie(String key){
		return this.cookies.get(key);
	}
}
