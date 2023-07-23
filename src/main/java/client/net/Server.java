package client.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-20
 * Time: 22:11
 */
public class Server {
    //服务器生成UUID传给客户端，客户端再应用
    public static ChannelGroup clients=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public void serverStart(){
        EventLoopGroup bossGroup=new NioEventLoopGroup(1);//负责客户端的链接
        EventLoopGroup workerGroup=new NioEventLoopGroup(2);//负责处理每个客户端的信息

        try {
            ServerBootstrap b=new ServerBootstrap();
            ChannelFuture f=b.group(bossGroup, workerGroup)//指定线程池的两个组
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pl=ch.pipeline();
                            pl.addLast(new MsgEncoder());
                            pl.addLast(new MsgDecoder());
                            pl.addLast(new ServerChildHandler());
                        }
                    })//对Client的处理
                    .bind(8888)//监听8888端口
                    .sync();//等待完成

            ServerFrame.INSTANCE.updateServerMsg("server started!");
            //图形用户界面摁下某个按钮的时候才调用close
            f.channel().closeFuture().sync();//close()->ChannelFuture

        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }
}

class  ServerChildHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Server.clients.add(ctx.channel());
        ServerFrame.INSTANCE.updateServerMsg("add new client"+ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ServerFrame.INSTANCE.updateClientMsg(msg.toString());
        Server.clients.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ServerFrame.INSTANCE.updateServerMsg("客户端传来的数据有误");
        //删除出现异常的客户端channel，并关闭链接
        Server.clients.remove(ctx.channel());
        ctx.close();
    }
}