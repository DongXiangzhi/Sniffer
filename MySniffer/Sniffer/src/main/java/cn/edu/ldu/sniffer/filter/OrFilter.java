package cn.edu.ldu.sniffer.filter;

import cn.edu.ldu.sniffer.packet.MyPacket;
//或逻辑解析
public class OrFilter implements Filter {
    private Filter leftSide;
    private Filter rightSide;
    OrFilter(Filter leftSide, Filter rightSide) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }
    @Override
    public boolean matches(MyPacket packet) {
        return leftSide.matches(packet) || rightSide.matches(packet);
    }
}
