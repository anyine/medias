package cn.wizool.bank.iwebutil.newlay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class IniUtil {

	Properties p;

	public IniUtil(Properties p) {
		this.p = p;
	}

	public void write(String url) throws IOException {

		File file = new File(url);
		Writer writer = new OutputStreamWriter(new FileOutputStream(file),
				"UTF-8");

		Iterator it = this.p.entrySet().iterator();

		writer.write("[app]\r\n");

		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();

			Object key = entry.getKey();
			Object value = entry.getValue();

			writer.write(key + "=" + value + "\r\n");
		}
		writer.flush();
		writer.close();
	}
}
