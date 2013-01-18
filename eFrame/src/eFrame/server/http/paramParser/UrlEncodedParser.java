package eFrame.server.http.paramParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.utils.Configuration;

import eFrame.server.http.paramParser.base.DataParser;
import eFrame.utils.CollectionUtil;


public class UrlEncodedParser extends DataParser{

	private static Logger logger = Logger.getLogger(UrlEncodedParser.class);
	
	private static String encoding;
    boolean forQueryString = false;

    static{
    	try {
			encoding = Configuration.getInstance().getDefault("encoding", "UTF-8".intern());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    }
    
    public static Map<String, String[]> parse(String urlEncoded) {
        try {
            return new UrlEncodedParser().parse(new ByteArrayInputStream(urlEncoded.getBytes( encoding )));
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Map<String, String[]> parseQueryString(InputStream is) {
        UrlEncodedParser parser = new UrlEncodedParser();
        parser.forQueryString = true;
        return parser.parse(is);
    }

    @Override
    public Map<String, String[]> parse(InputStream is) {
        // Encoding is either retrieved from contentType or it is the default encoding
        try {
            Map<String, String[]> params = new HashMap<String, String[]>();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ( (bytesRead = is.read(buffer)) > 0 ) {
                os.write( buffer, 0, bytesRead);
            }

            String data = new String(os.toByteArray(), encoding);
            if (data.length() == 0) {
                //data is empty - can skip the rest
                return new HashMap<String, String[]>(0);
            }

            // data is o the form:
            // a=b&b=c%12...

            // Let us parse in two phases - we wait until everything is parsed before
            // we decoded it - this makes it possible for use to look for the
            // special _charset_ param which can hold the charset the form is encoded in.
            //
            // NB: _charset_ must always be used with accept-charset and it must have the same value

            String[] keyValues = data.split("&");
            for (String keyValue : keyValues) {
                // split this key-value on the first '='
                int i = keyValue.indexOf('=');
                String key=null;
                String value=null;
                if ( i > 0) {
                    key = keyValue.substring(0,i);
                    value = keyValue.substring(i+1);
                } else {
                    key = keyValue;
                }
                if (key.length()>0) {
                	CollectionUtil.mergeValueInMap(params, key, value);
                }
            }

            // Second phase - look for _charset_ param and do the encoding
            String charset = encoding;
            if (params.containsKey("_charset_")) {
                String providedCharset = params.get("_charset_")[0];
                try {
                    "test".getBytes(providedCharset);
                    charset = providedCharset; // it works..
                } catch (Exception e) {
                	throw new RuntimeException("Got invalid _charset_ in form: " + providedCharset);
                }
            }

            // We're ready to decode the params
            Map<String, String[]> decodedParams = new HashMap<String, String[]>(params.size());
            for (Map.Entry<String, String[]> e : params.entrySet()) {
                String key = URLDecoder.decode(e.getKey(),charset);
                for (String value : e.getValue()) {

                	CollectionUtil.mergeValueInMap(decodedParams, key, (value==null ? null : URLDecoder.decode(value,charset)));
                }
            }

            // add the complete body as a parameters
            if(!forQueryString) {
                decodedParams.put("body", new String[] {data});
            }

            return decodedParams;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
   
}
