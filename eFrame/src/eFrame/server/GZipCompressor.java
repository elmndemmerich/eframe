package eFrame.server;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpMessage;
import org.jboss.netty.handler.codec.http.HttpResponse;

/**
 * 解压缩gzip的请求
 * <br>
 * @date 2013-1-14
 * @author LiangRL
 * @alias E.E.
 */
public class GZipCompressor extends HttpContentCompressor{
	private final int minlen;
	
	public GZipCompressor(int min, int level) {
		super(level);
		this.minlen = min;
	}
	
    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
    	if (minlen <= 0) {
    		ctx.sendDownstream(e);
    		return;
    	}
    	
        Object msg = e.getMessage();
        if (msg instanceof HttpResponse && ((HttpResponse) msg).getStatus().getCode() == 100) {
            // 100-continue response must be passed through.
            ctx.sendDownstream(e);
        } else if (msg instanceof HttpMessage) {
            HttpMessage m = (HttpMessage) msg;
            if (m.getContent().readableBytes() < minlen) {
            	ctx.sendDownstream(e);
            } else {
            	super.writeRequested(ctx, e);
            }
        } else {
        	// 1.staticDir 静态资源ChunkFile
        	// 2.staticDir 静态资源的GZip 字节流 ChannelBuffer
        	ctx.sendDownstream(e);
        }
    }	
}
