package cn.edu.ldu.sniffer.filter;

import cn.edu.ldu.sniffer.packet.MyPacket;
//检查过滤器字符串格式是否正确
class FieldFilter implements Filter {
    private String filterName;
    private String filterValue;
    FieldFilter(String filterString) throws InvalidFilterException {
        String[] parts = filterString.split("=");
        if (parts.length != 2) {
            throw new InvalidFilterException("过滤器字符串: " + filterString + " 格式不正确！");
        }
        filterName = parts[0].trim();
        filterValue = parts[1].trim();
    }
    @Override
    public boolean matches(MyPacket p) {
        switch (filterName) {
            case "srcaddr":
                return p.getSrcAddress().equalsIgnoreCase(filterValue);
            case "destaddr":
                return p.getDestAddress().equalsIgnoreCase(filterValue);
            case "srcport":
                return Integer.toString(p.getSrcPort()).equalsIgnoreCase(filterValue);
            case "destport":
                return Integer.toString(p.getDestPort()).equalsIgnoreCase(filterValue);
            case "type":
                return p.getPacketType().name().equalsIgnoreCase(filterValue);
            case "length":
                return Integer.toString(p.getSize()).equalsIgnoreCase(filterValue);
            default:
                return false;
        } //end switch
    } //end matches
} //end class
