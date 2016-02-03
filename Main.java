

public class Main {
  public static void main(String[] args) throws Exception {  
	MySQLAccess dao = new MySQLAccess();
	//dao.getProperties();
    dao.readDataBase();
    LoadStockPrices lsp = new LoadStockPrices();
    lsp.readDataBase();
  }

} 
