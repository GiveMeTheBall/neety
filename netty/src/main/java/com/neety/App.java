package com.neety;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
    
    /**
     * Created by moon on 2017/4/5.
     */
    public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1)
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception { // (2)
//            super.channelRead(ctx, msg);
            ((ByteBuf) msg).release(); // (3)
//            ByteBuf in = (ByteBuf) msg;
//            try {
//                while (in.isReadable()) {
//                    System.out.print((char) in.readByte());
//                    System.out.flush();
//                }
//            } finally {
//                ReferenceCountUtil.release(msg);
//            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception { // (5)
//            super.exceptionCaught(ctx, cause);
            cause.printStackTrace();
            ctx.close();
        }
    }
}
