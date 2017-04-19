package cn.edu.ldu.sniffer.filter;

//过滤器字符串格式异常
public class InvalidFilterException extends Exception {
    InvalidFilterException(String message) {
        super(message);
    }
}
