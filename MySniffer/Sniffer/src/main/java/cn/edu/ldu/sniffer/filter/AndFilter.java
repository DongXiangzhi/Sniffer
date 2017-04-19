package cn.edu.ldu.sniffer.filter;

import cn.edu.ldu.sniffer.packet.MyPacket;
//与逻辑解析
public class AndFilter implements Filter {
    private Filter field1;
    private Filter field2;
    AndFilter(Filter field1, Filter field2) {
        this.field1 = field1;
        this.field2 = field2;
    }
    @Override
    public boolean matches(MyPacket packet) {
        return field1.matches(packet) && field2.matches(packet);
    }
}
