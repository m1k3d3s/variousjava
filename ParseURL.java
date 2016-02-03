import java.net.*;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class ParseURL {
    public static void main(String[] args) throws Exception {

        URL aURL = new URL("http://finance.yahoo.com/q/hp?s=VIRT&a=03&b=16&c=2015&d=05&e=11&f=2015&g=d");

        String urlquery = aURL.getQuery();
        
        System.out.println(urlquery);
        
        Map<String,String> map = new LinkedHashMap<String,String>();
        for(String keyValue : urlquery.split(" *& *")) {
            String[] pairs = keyValue.split(" *=", 2);
            map.put(pairs[0], pairs.length == 1 ? "" : pairs[1]);
            //System.out.println(pairs[0] + " " + pairs[1]); 
            //s=stock
            //a=begin month
            //b=begin day
            //c=begin year
            //d=end day
            //e=end month
            //f= end year
        }

        String stock = map.get("s");
        System.out.println(stock);
        
        String begin_month = map.get("a");
        System.out.println(begin_month);
        
        String begin_day = map.get("b");
        System.out.println(begin_day);
        
        String begin_year = map.get("c");
        System.out.println(begin_year);
        
        String end_month = map.get("d");
        System.out.println(end_month);
        
        String end_day = map.get("e");
        System.out.println(end_day);
        
        String end_year = map.get("f");
        System.out.println(end_year);

        //System.out.println("protocol = " + aURL.getProtocol());
        //System.out.println("authority = " + aURL.getAuthority());
        //System.out.println("host = " + aURL.getHost());
        //System.out.println("port = " + aURL.getPort());
        //System.out.println("path = " + aURL.getPath());
        //System.out.println("query = " + aURL.getQuery());
        //System.out.println("filename = " + aURL.getFile());
        //System.out.println("ref = " + aURL.getRef());
    }

}
