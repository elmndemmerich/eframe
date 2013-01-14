package eFrame.server;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.HashedWheelTimer;

import eFrame.utils.Configuration;

/**
 * 
 * <br>
 * @date 2013-1-11
 * @author LiangRL
 * @alias E.E.
 */
public class ServerPipelineFactory implements ChannelPipelineFactory {
	
	public ChannelPipeline getPipeline() throws Exception {
		int timeout = Integer.parseInt(Configuration.getInstance().get("netty.timeout-sec"));
		int maxContentLength = Integer.parseInt(Configuration.getInstance().get("netty.maxContentLength"));
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("timeout", new ReadTimeoutHandler(new HashedWheelTimer(), timeout));
		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("aggregator", new StreamChunkAggregator(maxContentLength));
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
		String gZipOn = Configuration.getInstance().get("netty.gZip.enabled");
		if("true".equals(gZipOn)){
			int min = Integer.parseInt(Configuration.getInstance().get("netty.gZip.min"));
			int level = Integer.parseInt(Configuration.getInstance().get("netty.gZip.level"));
			pipeline.addLast("gzip", new GZipCompressor(min, level));
		}
		
		pipeline.addLast("handler", new ServerHandler());
		return pipeline;
	}
}
