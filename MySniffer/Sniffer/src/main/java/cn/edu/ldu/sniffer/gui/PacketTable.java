package cn.edu.ldu.sniffer.gui;

import cn.edu.ldu.sniffer.Main;
import cn.edu.ldu.sniffer.filter.PacketList;
import cn.edu.ldu.sniffer.packet.MyPacket;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

//在表格控件中显示所有捕获的包，以及根据过滤规则显示指定类型的包
public class PacketTable {   
    private Main parent;
    private PacketList allPackets = new PacketList(); //所有包
    private PacketList filterPackets = allPackets;//过滤包
    private DefaultTableModel tableModel; //表格数据模型
    private final String[] tableColumns = new String[]{"编号","时间序列", "源地址", "源端口", "目标地址", "目标端口", "协议", "长度"};
    public PacketTable(Main parent) {
        if(parent!=null) {           
            this.parent = parent;
            //初始化表格
            tableModel = (DefaultTableModel) this.parent.table.getModel();           
            updateModel(filterPackets);
            this.parent.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.parent.table.getSelectionModel()
                .addListSelectionListener((e) -> {
                if (filterPackets.getPackets().size()>0) {
                    int rowIndex = this.parent.table.getSelectedRow();                 
                    if (rowIndex==-1) {return;}
                    if (filterPackets.getPackets().size() > rowIndex) {
                       this.parent.analyzingPacket(filterPackets
                           .getPackets().get(rowIndex));
                    } //end if
                } //end if
            });            
        } //end if
    }
    //向表格中追加一行新包
    public void addPacket(MyPacket packet) {
        allPackets.addPacket(packet);
        tableModel.addRow(createRow(packet));
    }
    //根据过滤规则更新过滤包列表和表格数据模型
    public void filterPacketList(String filter) {
        if (filter.isEmpty()) {
            filterPackets = allPackets;
        } else {
            filterPackets = allPackets.filter(filter);
        }
        if (filterPackets!=null)
            updateModel(filterPackets); 
    }
    public void resetFilter() {
        allPackets = new PacketList();
        filterPackets = allPackets;       
        updateModel(filterPackets);
    }
    public PacketList getFilterPackets() {
        return filterPackets;
    }
    //设置包列表，更新表格数据模型
    public void setPacketList(PacketList packetList) {
        allPackets = packetList;
        filterPackets = allPackets;
        updateModel(filterPackets);
    }
    //更新表格数据模型
    private void updateModel(PacketList packets) {
        Object[][] rows = packetListToObjectArray(packets);
        tableModel.setDataVector(rows, tableColumns);
    }
    //将包列表转换为二维对象列表
    private Object[][] packetListToObjectArray(PacketList packetList) {
        Object[][] rows = new Object[packetList.getPackets().size()][tableColumns.length];
        for (int i = 0; i < rows.length; i++) {
            MyPacket p = packetList.getPackets().get(i);
            rows[i] = createRow(p);
        }
        return rows;
    }
    //根据表格结构将包转化为一维数组
    private Object[] createRow(MyPacket p) {
        Main.order++;
        return new Object[]{Main.order,p.getTimestamp(), p.getSrcAddress(), p.getSrcPort(), p.getDestAddress(),
                p.getDestPort(), p.getPacketType().name(), p.getSize()};
    }
}