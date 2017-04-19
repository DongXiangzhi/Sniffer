package cn.edu.ldu.sniffer.packet;

import java.time.LocalDateTime;

/**
 * TCP 包的结构实现
 */
public class MyTcpPacket extends MyPacket {
    public MyTcpPacket(String srcAddress, String destAddress, 
       int srcPort, int destPort, byte[] header,
       byte[] payload, LocalDateTime timestamp) {
       super(srcAddress, destAddress, srcPort, destPort, header,
       payload, PacketType.TCP, timestamp);
    }
}
