package eFrame.server;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.HashedWheelTimer;

import com.utils.Configuration;


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
		//超时处理器 ReadTimeoutException when no data was read within a certain period of time
		pipeline.addLast("timeout", new ReadTimeoutHandler(new HashedWheelTimer(), timeout));
		//http解码器
		pipeline.addLast("decoder", new HttpRequestDecoder());
		//流块聚合器
		pipeline.addLast("aggregator", new StreamChunkAggregator(maxContentLength));
		//http编码器
		pipeline.addLast("encoder", new HttpResponseEncoder());
		//通过队列的方式，支持大块数据写.
		pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
		//http请求压缩发送
		String gZipOn = Configuration.getInstance().get("netty.gzip.enabled");
		if("true".equals(gZipOn)){
			int min = Integer.parseInt(Configuration.getInstance().get("netty.gzip.min"));
			int level = Integer.parseInt(Configuration.getInstance().get("netty.gzip.level"));
			pipeline.addLast("gzip", new GZipCompressor(min, level));
		}
		//框架核心处理器
		pipeline.addLast("handler", new ServerHandler());
		return pipeline;
	}
}
