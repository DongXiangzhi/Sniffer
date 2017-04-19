package cn.edu.ldu.sniffer.packet;
//捕获包接口
public interface MyPacketListener {
    void packetCaptured(MyPacket packet);    
}

