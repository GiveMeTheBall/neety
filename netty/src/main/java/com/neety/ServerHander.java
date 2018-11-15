package com.neety;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

	 /**
     * Created by jjj 2018-8-20
     * 注释部分是打印输出消息，
     * 未注释部分是直接将接受的消息返回
     */
    public class ServerHander extends ChannelInboundHandlerAdapter { // (1)
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception { // (2)
  
//            ByteBuf in = (ByteBuf) msg;
//            try {
//                while (in.isReadable()) {
//                    System.out.print((char) in.readByte());
//                    System.out.flush();
//                }
//            } finally {
//                ReferenceCountUtil.release(msg);
//            }
        	  ctx.write(msg);
              ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception { // (5)
//            super.exceptionCaught(ctx, cause);
            cause.printStackTrace();
            ctx.close();
        }
    }
