import java.util.Iterator;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


class TestTickData {

    public static void main(String[] args) {
        TickData td = new TickData();
        try {
            td.readCsv();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
