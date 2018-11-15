package com.neety;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NeetyServer {

	/**
	 * Created by jjj 2018-8-20
	 *  1,NioEventLoopGroup是一个处理I / O操作的多线程事件循环。Netty为不同类型的传输提供了各种EventLoopGroup实现。在这个例子中，我们正在实现一个服务器端应用程序，因此将使用两个NioEventLoopGroup。第一个，通常称为“老板”，接受传入的连接。第二个，通常称为“工人”，一旦老板接受连接并将接受的连接注册给工作人员，就处理接受的连接的流量。使用多少线程以及它们如何映射到创建的通道取决于EventLoopGroup实现，甚至可以通过构造函数进行配置。
		2,ServerBootstrap是一个帮助类，用于设置服务器。您可以直接使用Channel设置服务器。但是请注意，这是一个繁琐的过程，在大多数情况下您不需要这样做。
		3, 在这里，我们指定使用NioServerSocketChannel类来实例化一个新的Channel来接受传入的连接。（可以这么理解，每个客户端连接我们服务端，我们都会为他们创建一个channel，那么这个channel对于面向对象的我们来说就是一个类，我们同意对于我们接受到的连接都初始化为：NioServerSocketChannel）
		4, 这里指定的处理程序将始终由新接受的Channel进行评估。ChannelInitializer是一个特殊的处理程序，旨在帮助用户配置新的Channel。很可能您想通过添加一些处理程序（如DiscardServerHandler）来配置新Channel的ChannelPipeline来实现您的网络应用程序。随着应用程序的复杂化，您可能会在管道中添加更多的处理程序，并将这个匿名类最终提取到顶级类中。（个人感觉说白了就是想自己实现包含自己处理逻辑的Channel，但是又需要包含一些通用的原有功能，咋办，继承呗，这就是为什么上面的DiscardServerHandler继承netty的类）
		5, 您还可以设置特定于Channel实现的参数。我们正在编写一个TCP / IP服务器，因此我们可以设置套接字选项，如tcpNoDelay和keepAlive。请参阅ChannelOption的apidocs和特定的ChannelConfig实现，以获得有关支持的ChannelOptions的概述。
		6, 你有没有注意到option（）和childOption（）？option（）用于接受传入连接的NioServerSocketChannel。childOption（）用于在这种情况下由父级ServerChannel接受的通道，即NioServerSocketChannel。（我的理解就是前者用于配置我们父级Channel，后者用于配置我们自定义的子级Channel）。
		我们现在准备好了。剩下的是绑定到端口并启动服务器。这里，我们绑定机器中所有NIC（网络接口卡）的端口到8080。您现在可以根据需要调用bind（）方法多次（具有不同的绑定地址）。
	 */
	    private int port;

	    public NeetyServer(int port) {
	        this.port = port;
	    }

	    public void run() throws InterruptedException {
	        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
	        EventLoopGroup workerGroup = new NioEventLoopGroup();
	        try {
	            ServerBootstrap b = new ServerBootstrap(); // (2)
	            b.group(bossGroup, workerGroup)
	                    .channel(NioServerSocketChannel.class) // (3)
	                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
	                        @Override
	                        public void initChannel(SocketChannel ch) throws Exception {
	                            ch.pipeline().addLast(new ServerHander());
	                        }
	                    })
	                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
	                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

	            // Bind and start to accept incoming connections.
	            ChannelFuture f = b.bind(port).sync(); // (7)
	            // Wait until the server socket is closed.
	            // In this example, this does not happen, but you can do that to gracefully
	            // shut down your server.
	            f.channel().closeFuture().sync();
	        } finally {
	            workerGroup.shutdownGracefully();
	            bossGroup.shutdownGracefully();
	        }
	    }

	    public static void main(String[] args) throws InterruptedException {
	        int port;
	        if (args.length > 0) {
	            port = Integer.parseInt(args[0]);
	        } else {
	            port = 8080;
	        }
	        new NeetyServer(port).run();
	    }

}
