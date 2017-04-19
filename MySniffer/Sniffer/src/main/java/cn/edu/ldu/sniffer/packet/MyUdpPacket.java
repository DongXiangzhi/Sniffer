package cn.edu.ldu.sniffer.packet;
import java.time.LocalDateTime;

/**
 * UDP包的结构实现
 */
public class MyUdpPacket extends MyPacket {
    public MyUdpPacket(String srcAddress, String destAddress,
        int srcPort, int destPort, byte[] header, 
        byte[] payload, LocalDateTime timestamp) {
        super(srcAddress, destAddress, srcPort, destPort, header,
        payload, PacketType.UDP, timestamp);
    }
}
