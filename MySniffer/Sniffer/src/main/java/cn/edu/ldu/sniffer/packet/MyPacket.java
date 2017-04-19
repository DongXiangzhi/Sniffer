package cn.edu.ldu.sniffer.packet;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;

import java.time.LocalDateTime;

/**
 * 定义包抽象类，包含UDP和TCP协议的所有字段结构。
 * 定义一个静态方法，基于Pcap4j中的org.pcap4j.packet.Packet类创建包
 */
public abstract class MyPacket {
    private String srcAddress; //源地址
    private String destAddress; //目标地址
    private int srcPort; //源端口
    private int destPort; //目标端口
    private byte[] header; //头部字节数组
    private byte[] payload; //数据字节数组
    private PacketType packetType; //包类型
    private LocalDateTime timestamp; //时间戳
    //构造函数
    public MyPacket(String srcAddress, String destAddress, int srcPort, int destPort, byte[] header, byte[] payload, PacketType packetType, LocalDateTime timestamp) {
        this.srcAddress = srcAddress;
        this.destAddress = destAddress;
        this.srcPort = srcPort;
        this.destPort = destPort;
        this.header = header;
        this.payload = payload;
        this.packetType = packetType;
        this.timestamp = timestamp;
    }
    /**
     * 由pcap4j的packet构建Sniffer的packet
     * @param packet MyPacket to be converted
     * @return 返回TCPPacket或UDPPacket或null
     */
    static MyPacket fromPcapPacket(org.pcap4j.packet.Packet packet) {    
        if (packet.contains(IpV4Packet.class)) {  //是IP包
            IpV4Packet ipPacket = packet.get(IpV4Packet.class);
            String srcAddr = ipPacket.getHeader().getSrcAddr().toString().replace("/", "");
            String dstAddr = ipPacket.getHeader().getDstAddr().toString().replace("/", "");
            LocalDateTime time = LocalDateTime.now();
            if (ipPacket.contains(TcpPacket.class)) { //是TCP包
                TcpPacket tcpPacket = ipPacket.get(TcpPacket.class);
                byte[] payload = tcpPacket.getPayload() != null ?
                    tcpPacket.getPayload().getRawData() : new byte[0];
                return new MyTcpPacket(srcAddr, dstAddr, 
                    tcpPacket.getHeader().getSrcPort().valueAsInt(),
                    tcpPacket.getHeader().getDstPort().valueAsInt(),
                    tcpPacket.getHeader().getRawData(),payload, time);
            } else if (ipPacket.contains(UdpPacket.class)) {//是UDP包
                UdpPacket udpPacket = ipPacket.get(UdpPacket.class);
                byte[] payload = udpPacket.getPayload() != null ? udpPacket.getPayload().getRawData() : new byte[0];
                return new MyUdpPacket(srcAddr, dstAddr,
                udpPacket.getHeader().getSrcPort().valueAsInt(),
                udpPacket.getHeader().getDstPort().valueAsInt(), 
                udpPacket.getHeader().getRawData(),payload, time);
            }
        }       
        return null;
    }
    public String getSrcAddress() {return srcAddress;}
    public String getDestAddress() {return destAddress;}
    public int getSrcPort() {
        return srcPort;
    }
    public int getDestPort() {return destPort;}
    public byte[] getHeader() {return header;}
    public byte[] getPayload() {return payload;}
    public PacketType getPacketType() {return packetType;}
    public LocalDateTime getTimestamp() {return timestamp;}
    public int getSize() {return header.length + payload.length;}
    public enum PacketType {UDP, TCP}
}