package eFrame.server.http;

import java.io.Serializable;

/**
 * 
 * <br>
 * @date 2013-1-9
 * @author LiangRL
 * @alias E.E.
 */
public class Cookie implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
     * When creating cookie without specifying domain,
     * this value is used. Can be configured using
     * the property 'application.defaultCookieDomain'
     * in application.conf.
     *
     * This feature can be used to allow sharing
     * session/cookies between multiple sub domains.
     */
    public static String defaultDomain = null;

    /**
     * Cookie name
     */
    public String name;
    /**
     * Cookie domain
     */
    public String domain;
    
    /**
     * for HTTPS ?
     */
    public boolean secure = false;
    /**
     * Cookie value
     */
    public String value;
    /**
     * Cookie max-age
     */
    public Integer maxAge;

    /**
     * See http://www.owasp.org/index.php/HttpOnly
     */
    public boolean httpOnly = false;
    
    @Override
    public String toString() {
        return String.format("Cookie: domain=%s, name=%s, value=%s, maxAge=%d, secure=%s",
                domain, name, value, maxAge, secure);
    }
}
