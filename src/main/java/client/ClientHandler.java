package main.java.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.UnsupportedEncodingException;

import static java.lang.System.exit;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    ChannelHandlerContext ctx;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        System.out.println("Calculator started! To close the client write \'quit\'.");
        System.out.println("Request examples: 5*5 8/3 8+7 67-24 23%4.");
        //sendMsg("5+5");
    }

    public boolean sendMsg(String msg){
        System.out.println("Client sends a message："+msg);
        byte[] req = msg.getBytes();
        ByteBuf m = Unpooled.buffer(req.length);
        m.writeBytes(req);
        boolean exit = msg.equals("quit")?false:true;
        if(exit){
            ctx.writeAndFlush(m);
        }else{
            System.out.println("Goodbye!");
            exit(-1);
        }
        return exit;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req,"utf-8");
        System.out.println("Server message："+ body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
