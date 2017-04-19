package cn.edu.ldu.sniffer.packet;

import org.pcap4j.core.*;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

//捕获数据包类，提供了一系列处理方法
public class MyPacketCapture {
    private Set<MyPacketListener> listeners = new HashSet<>();
    private boolean capturing = false; //抓包状态
    private PcapNetworkInterface networkInterface; //网络接口
    private PcapHandle handle;
    //根据给定网络接口地址创建抓包对象
    public MyPacketCapture(InetAddress inetAddress) throws PcapNativeException {
        networkInterface = Pcaps.getDevByAddress(inetAddress);
    }
    //为捕获的包注册侦听器
    public void registerListener(MyPacketListener listener) {
        listeners.add(listener);
    }
    //注销侦听器
    public void unregisterListener(MyPacketListener listener) {
        listeners.remove(listener);
    }
    //开始抓包，如果没有包，则阻塞等待
    public void startCapturing() throws PcapNativeException, NotOpenException, InterruptedException {
        if (!capturing) {
            capturing = true;
            System.out.println("开始抓包...");
            handle = networkInterface.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
            PacketListener listener = packet -> broadcastPacketReceived(packet, handle);
            handle.loop(-1, listener);
        }
    }
    //停止抓包
    public void stopCapturing() throws NotOpenException {
        if (capturing) {
            capturing = false;
            System.out.println("停止抓包！");
        }
    }
    //通过侦听器发布捕获的包
    private void broadcastPacketReceived(org.pcap4j.packet.Packet packet, PcapHandle handle) {
        if (capturing) {
            MyPacket snifferPacket = MyPacket.fromPcapPacket(packet);
            if (snifferPacket != null) {//是TCP包或UDP包
                for (MyPacketListener listener : listeners) {
                    listener.packetCaptured(snifferPacket);
                } //end for
            } //end if
        } //end if
    } //end broadcastPacketReceived
} //end class