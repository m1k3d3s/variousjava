import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class TickData {

    
    public HashMap<Double,Integer> readCsv() throws IOException {
        String csvFile = "/home/mikedes/Downloads/pg.csv";
        String line = "";
        String cvsSplitBy = ",";
        HashMap<Double,Integer> maps = new HashMap<Double,Integer>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
             while((line = br.readLine()) != null) {
                //System.out.println(line);
                String[] pgPrices = line.split(cvsSplitBy);
                maps.put(Double.valueOf(pgPrices[4]), Integer.valueOf(pgPrices[5]));
            }
            for (Map.Entry<Double, Integer> entry : maps.entrySet()) {
               //  System.out.println("Close price: " + entry.getKey() + ", volume= " + entry.getValue());
            }
        } catch(IOException e) {
            System.out.println("bad" + e);
        } 
        return maps;
    }

    public int bar() {
        return 100;
    }
    
}
