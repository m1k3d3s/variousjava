import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SwitchDemo {
    public static void main(String[] args) {

    	SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
    	String date = sdf.format(new Date());
    	String[] full_date = date.split("\\W");
    	//System.out.println(date);
    	//for(int i=0;i<full_date.length;i++){
    	//	System.out.println(full_date[i]);
    	//}
        int month = Integer.parseInt(full_date[1]);
        String monthString;
        switch (month) {
            case 1:  monthString = "January";
                     break;
            case 2:  monthString = "February";
                     break;
            case 3:  monthString = "March";
                     break;
            case 4:  monthString = "April";
                     break;
            case 5:  monthString = "May";
                     break;
            case 6:  monthString = "June";
                     break;
            case 7:  monthString = "July";
                     break;
            case 8:  monthString = "August";
                     break;
            case 9:  monthString = "September";
                     break;
            case 10: monthString = "October";
                     break;
            case 11: monthString = "November";
                     break;
            case 12: monthString = "December";
                     break;
            default: monthString = "Invalid month";
                     break;
        }
        System.out.println(monthString);
    }
}