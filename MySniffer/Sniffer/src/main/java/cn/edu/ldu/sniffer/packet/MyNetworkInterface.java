package cn.edu.ldu.sniffer.packet;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//自定义网络接口类，提供了获取接口地址的方法
public class MyNetworkInterface {
    //获取主机上所有网络接口
    public static List<NetworkInterface> getIpv4NetworkInterfaces() throws SocketException {
        //筛选拥有IPv4地址的网络接口
        return Collections.list(NetworkInterface.
            getNetworkInterfaces()).stream()
            .filter((ni) -> Collections.list(ni.
            getInetAddresses()).stream()
            .anyMatch((a) -> a instanceof Inet4Address))
            .collect(Collectors.toList());
    }
    //获取某一指定网络接口上的所有IPV4地址，以List地址列表形式返回
    public static List<InetAddress> getIpv4Addresses(NetworkInterface nif) {
        return Collections.list(nif.getInetAddresses())
            .stream()
            .filter((a) -> a instanceof Inet4Address)
            .collect(Collectors.toList());
    }
    //获取主机上所有网络接口的所有IPV4地址，
    //以Map映射的形式返回接口和地址列表
    public static Map<NetworkInterface, List<InetAddress>> getIpv4AddressMap() throws SocketException {
        Map<NetworkInterface, List<InetAddress>> addressMap = new HashMap<>();
        for (NetworkInterface nif : getIpv4NetworkInterfaces()) {
            addressMap.put(nif, getIpv4Addresses(nif));
        }
        return addressMap;
    }
}