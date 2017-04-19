package cn.edu.ldu.sniffer.filter;

import cn.edu.ldu.sniffer.packet.MyPacket;

//过滤器接口
public interface Filter {
    boolean matches(MyPacket packet);
}

