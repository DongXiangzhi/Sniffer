package cn.edu.ldu.sniffer.file;

import cn.edu.ldu.sniffer.filter.PacketList;
import cn.edu.ldu.sniffer.packet.MyPacket;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

//以文本格式导出包信息，保存到文件中
public class FileExporter {
    /**
     * 导出包列表中的包到文本文件，以;分隔各字段值
     * @param filepath 文件路径
     * @param packetList 包列表
     */
    public void exportToFile(String filepath, PacketList packetList) throws IOException {
        PrintWriter writer = new PrintWriter(filepath, "UTF-8");
        for (MyPacket packet : packetList.getPackets()) {
            String line = serializePacket(packet);
            writer.println(line);
        }
        writer.close();
    }
    //用分号分隔包的各字段，返回一个单行字符串
    private String serializePacket(MyPacket packet) {
        try {
            return String.format("%s;%s;%s;%s;%s;%s;%s;%s",
                packet.getPacketType(), 
                packet.getTimestamp(), 
                packet.getSrcAddress(),
                packet.getSrcPort(),
                packet.getDestAddress(), 
                packet.getDestPort(),
                Arrays.toString(packet.getHeader()).replace(" ", ""),
                Arrays.toString(packet.getPayload())).replace(" ", "");
        } catch (NullPointerException e) {
            System.out.println(packet);
            return null;
        } //end try
    } //end serializePacket
} //end class
