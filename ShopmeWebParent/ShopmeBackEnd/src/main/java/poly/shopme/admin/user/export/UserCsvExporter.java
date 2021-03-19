package poly.shopme.admin.user.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import poly.shopme.common.entity.User;

public class UserCsvExporter extends AbstractExporter {

	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv;charset=UTF-8", ".csv");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
				CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"ID", "E-mail", "Họ", "Tên", "Vai trò", "Kích hoạt"};
		String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};
		
		csvWriter.writeHeader(csvHeader);
		
		for (User user : listUsers) {
			csvWriter.write(user, fieldMapping);
		}
		csvWriter.close();
	}
}
