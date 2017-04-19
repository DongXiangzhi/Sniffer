package cn.edu.ldu.sniffer.file;

import cn.edu.ldu.sniffer.filter.PacketList;
import cn.edu.ldu.sniffer.packet.MyPacket;
import cn.edu.ldu.sniffer.packet.MyTcpPacket;
import cn.edu.ldu.sniffer.packet.MyUdpPacket;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;

//从文本文件导入包信息
public class FileImporter {
    //导入的包存放到列表中
    public PacketList readFromFile(String filepath) throws IOException, MalformedDataException {
        PacketList packetList = new PacketList();
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(filepath);
            br = new BufferedReader(fr);
            String line = br.readLine();
            while (line != null) {
                packetList.addPacket(parsePacket(line));
                line = br.readLine();
            }
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new MalformedDataException("导入包失败！", e);
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return packetList;
    }
    //将单行字符串解析成包结构
    private MyPacket parsePacket(String line) {
        String[] parts = line.split(";");
        MyPacket.PacketType type = parts[0].equals("TCP") ? MyPacket.PacketType.TCP : MyPacket.PacketType.UDP;
        LocalDateTime time = LocalDateTime.parse(parts[1]);
        String srcAddr = parts[2];
        int srcPort = Integer.parseInt(parts[3]);
        String destAddr = parts[4];
        int destPort = Integer.parseInt(parts[5]);
        byte[] header = parseByteArray(parts[6]);
        byte[] payload = parseByteArray(parts[7]);
        if (type == MyPacket.PacketType.TCP) {
            return new MyTcpPacket(srcAddr, destAddr, srcPort,
                destPort, header, payload, time);
        } else {
            return new MyUdpPacket(srcAddr, destAddr, srcPort,
                destPort, header, payload, time);
        }
    }
    //字符串还原为字节数组
    private byte[] parseByteArray(String array) {
        String[] parts = array.split(",");
        //如果string = []，返回空数组
        if (parts.length == 1) {
            return new byte[0];
        }
        byte[] bytes = new byte[parts.length];
        for (int i = 0; i < parts.length; i++) {
            String stringByte = parts[i].replace("[", "").replace("]", ""); //去掉[]
            bytes[i] = Byte.parseByte(stringByte);
        }
        return bytes;
    }
}
