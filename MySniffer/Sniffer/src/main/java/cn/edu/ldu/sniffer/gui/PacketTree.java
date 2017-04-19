package cn.edu.ldu.sniffer.gui;
import cn.edu.ldu.sniffer.Main;
import javax.swing.tree.*;
public class PacketTree {
    private Main parent;
    private PacketDetail packetDetail;
    DefaultMutableTreeNode root, child;
    DefaultTreeModel treeModel;
    public PacketTree(Main parent,PacketDetail packetDetail) {
        this.parent=parent; 
        this.packetDetail=packetDetail;
        if (this.packetDetail !=null) {     
            renderTree();
            renderBytes();
        }
    }
    private void renderTree() {
        treeModel=(DefaultTreeModel)parent.tree.getModel();
        root = (DefaultMutableTreeNode)treeModel.getRoot();
        root.removeAllChildren();
        child = new Branch(packetDetail.frameData).node(); // Frame子节点
        treeModel.insertNodeInto(child, root, 0); 
        child = new Branch(packetDetail.ethernetData).node(); // Ethernet II子节点
        treeModel.insertNodeInto(child, root, 0); 
        child = new Branch(packetDetail.ipv4Data).node(); //IPV4 子节点
        treeModel.insertNodeInto(child, root, 0);
        child = new Branch(packetDetail.fourthData).node(); //传输层子节点
        treeModel.insertNodeInto(child, root, 0);
        treeModel.reload();
    }
    private void renderBytes() {
        parent.packetBytes.setText("");
        parent.packetBytes.setText("包头部：\n"+packetDetail.packetHeader+"\n");
        parent.packetBytes.append("数据部分：\n"+packetDetail.packetData+"\n");
    }
    //定义树的分支节点
    class Branch {
	DefaultMutableTreeNode node;
	public Branch(String[] data) {
            node= new DefaultMutableTreeNode(data[0]);
            for (int i = 1; i < data.length; i++)
                node.add(new DefaultMutableTreeNode(data[i]));
	}
	public DefaultMutableTreeNode node() {// 返回节点
            return node;
	}
    } //end class Branch
} //end class PacketTree


