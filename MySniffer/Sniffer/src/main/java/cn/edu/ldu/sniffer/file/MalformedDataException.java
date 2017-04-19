package cn.edu.ldu.sniffer.file;

//包结构不完整时抛出异常
public class MalformedDataException extends Exception {
    public MalformedDataException(String message) {
        super(message);
    }
    public MalformedDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
