package wang.tyrael.dbd.jdapi;

/**
 * Created by Administrator on 2017/4/4.
 */

public class ResponseProcessor {
    public static String removeFunction(String response){
        int indexStart = response.indexOf("(");
        int indexEnd = response.indexOf(")");
        return response.substring(indexStart  + 1, indexEnd);
    }
}
