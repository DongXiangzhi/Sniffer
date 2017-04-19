package cn.edu.ldu.sniffer.filter;

import cn.edu.ldu.sniffer.packet.MyPacket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

//数据包列表类，实现了添加新包和过滤数据包方法
public class PacketList {
    private List<MyPacket> packets;
    public PacketList(List<MyPacket> packets) {
        this.packets = packets;
    }
    public PacketList() {
        this.packets = new ArrayList<>();
    }
    public void addPacket(MyPacket packet) {
        if (packet == null) {
           throw new IllegalArgumentException("试图向列表中加入空包！");
        }
        packets.add(packet);
    }
    /**
     * 过滤规则：srcaddr=127.0.0.1
     *          destaddr=127.0.0.1
     *          srcport=
     *          destport=
     *          type=
     *          length=
     * 或者匹配上述字段值的一部分内容
     */
    public PacketList filter(String filterString) {
        //包含=的情况
        if (filterString.contains("=")) {
            try {
                Filter filter = Filters.parseFilter(filterString);
                return new PacketList(packets.stream()
                    .filter(filter::matches).collect(Collectors.toList()));
            } catch (InvalidFilterException e) {
                JOptionPane.showMessageDialog(null, "错误提示",
                "过滤器语法错误", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
        //匹配字段的情况
        return new PacketList(packets.stream()
            .filter(p -> anyFieldContains(p, filterString))
            .collect(Collectors.toList()));
    }
    //根据包各部分结构模糊匹配
    private boolean anyFieldContains(MyPacket p, String filter) {
        return p.getSrcAddress().contains(filter)
            || p.getDestAddress().contains(filter)
            || Integer.toString(p.getSrcPort()).contains(filter)
            || Integer.toString(p.getDestPort()).contains(filter)
            || p.getPacketType().name().contains(filter);
}
    public List<MyPacket> getPackets() {
        return packets;
    }
}
