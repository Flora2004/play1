package Tank.net;

import Tank.TankFrame;
import Tank.net.AllMsg.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: angel
 * Date: 2023-07-20
 * Time: 10:37
 */
public class Client {
    public static final Client INSTANCE = new Client();
    private Channel channel=null;
    private boolean isConnect=false;
    private int waiting=0;
    private Client(){    }
    /**
     * 连接到服务器的方法，通过 NIO 创建非阻塞连接。
     */
    public void connect(){
        //创建NIO线程池,创建线程处理链接和读取
        EventLoopGroup group=new NioEventLoopGroup(1);

        Bootstrap b=new Bootstrap();

        try {//链接服务器
            ChannelFuture f=b.group(group)
                    .channel(NioSocketChannel.class)//链接到服务器的NIO非阻塞版
                    .handler(new ClientChannelInitializer())//处理出现的特殊事件
                    //异步方法，无论有没有连接都会执行下一行代码
                    .connect("localhost",8888);

            f.addListener(new ChannelFutureListener() {//判断客户端是否链接到服务器
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(!future.isSuccess()){
                        if(waiting<3) {
                            // 连接失败，尝试重新连接
                            System.out.println("not connected!waiting");
                            Thread.sleep(10000);
                            Client.INSTANCE.connect();
                            waiting++;
                        }else {
                            System.out.println("Connection attempts failed! Exiting...");
                        }
                    }else {
                        isConnect= true;
                        System.out.println("connected!");
                        //连接成功后初始化channel
                        channel=future.channel();
                    }
                }
            });

            f.sync();

            System.out.println("...");

            f.channel().closeFuture().sync();

        }catch (InterruptedException e){
            e.printStackTrace();
        }finally{
            group.shutdownGracefully();
        }
    }
    /**
     * 关闭与服务器的连接
     */
    public void closeConnect() {
        channel.close().addListener((ChannelFutureListener) future -> {
            EventLoopGroup group = future.channel().eventLoop().parent();
            group.shutdownGracefully();
            ServerFrame.INSTANCE.updateServerMsg(TankFrame.INSTANCE.getGm().getMainTank().getId().toString()+"client close");
            System.out.println("Client closed.");
        });
    }

    /**
     * 发送消息给服务器的方法。
     * @param msg 要发送的消息对象。
     */
    public void send(Msg msg) {
        channel.writeAndFlush(msg);
    }

    /**
     * 判断客户端是否连接到服务器。
     * @return 连接成功返回 true，否则返回 false。
     */
    public boolean isConnect() {
        return isConnect;
    }
}

/**
 * 客户端的 Channel 初始化器，用于添加处理器来处理消息的编解码和处理。
 */
class ClientChannelInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel ch)throws Exception{
        ch.pipeline()
                .addLast(new MsgEncoder())//对传出的信息进行处理
                .addLast(new MsgDecoder())//对传回的信息进行处理
                .addLast(new ClientHandler())//对服务器传回的消息进行处理
                .addLast(new ExceptionHandler()); // 添加异常处理器
    }
}
class  ClientHandler extends SimpleChannelInboundHandler<Msg>{//处理单一的数据类型

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Msg msg) throws Exception {
        //TODO:处理异常的信息
        msg.handle();

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //拿到主战坦克
        ctx.writeAndFlush(new TankJoinMsg(TankFrame.INSTANCE.getGm().getMainTank()));
    }
}
class ExceptionHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 在这里处理异常信息，例如记录日志、关闭连接等
        cause.printStackTrace();
        ctx.close(); // 关闭连接
    }
}
