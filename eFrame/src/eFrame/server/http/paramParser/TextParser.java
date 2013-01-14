package eFrame.server.http.paramParser;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import eFrame.constants.Encoding;
import eFrame.server.http.paramParser.base.DataParser;


public class TextParser extends DataParser{

    @Override
    public Map<String, String[]> parse(InputStream is) {
        try {
            Map<String, String[]> params = new HashMap<String, String[]>();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int b;
            while ((b = is.read()) != -1) {
                os.write(b);
            }
            byte[] data = os.toByteArray();
            params.put("body", new String[] {new String(data, Encoding.UTF_8)});
            return params;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
