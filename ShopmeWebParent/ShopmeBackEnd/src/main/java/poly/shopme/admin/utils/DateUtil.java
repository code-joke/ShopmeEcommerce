package poly.shopme.admin.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static String toSimpleFormat(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simple_format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        return simple_format.format(date);
    }
}
