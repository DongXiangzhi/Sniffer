package cn.edu.ldu.sniffer.gui;

import cn.edu.ldu.sniffer.packet.MyPacket;
import javax.swing.*;

//获取包的详细信息
public class PacketDetail extends JTree {
    public MyPacket packet;
    public String packetHeader,packetData;
    public String[] frameData ={"物理层（Frame）：","大小"}; 
    public String[] ethernetData={"数据链路层（Ethernet II）："};
    public String[] ipv4Data={"网络层（IPV4）：","源地址","目标地址"};
    public String[] fourthData={"传输层（TCP/UDP）：","类型","源端口","目标端口"};
    public PacketDetail(MyPacket packet) {
        if (packet!=null) {
            this.packet = packet;
            renderDetail();
        }
            
    }
    //获取包的详细信息
    private void renderDetail() {
        packetHeader=byteArrayToHexString(packet.getHeader());
        packetData=byteArrayToHexString(packet.getPayload());

        frameData[1]="Frame大小：" + packet.getSize();
        ipv4Data[1]="源地址：" + packet.getSrcAddress();
        ipv4Data[2]="目标地址：" + packet.getDestAddress();
        fourthData[1]="协议类型：" + packet.getPacketType().name();
        fourthData[2]="源端口：" + packet.getSrcPort();             fourthData[3]="目标端口：" +packet.getDestPort();    
    }
    //字节数组转换为16进制字符串
    private String byteArrayToHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        int i=0;
        for (byte b : bytes) {
            builder.append(String.format("%02x", b & 0xff));
            i++;
            switch (i) {
                case 8:
                    builder.append("   ");
                    break;
                case 16:
                    i=0;
                    builder.append("\n");
                    break;            
                default:
                    builder.append(" ");
                    break;
            }
        }
        return builder.toString();
    }
}
