package cn.edu.ldu.sniffer.filter;

import cn.edu.ldu.sniffer.packet.MyPacket;
//否逻辑解析
public class NotFilter implements Filter {
    private Filter filter;
    NotFilter(Filter filter) {
        this.filter = filter;
    }
    @Override
    public boolean matches(MyPacket packet) {
        return !filter.matches(packet);
    }
}
