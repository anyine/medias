package cn.wizool.bank.iwebutil;

public class ContentDisposition {
	private String name;
	private String fileName;
	private String value;

	public ContentDisposition(String value) {
		String strs[] = value.split(";");
		this.setValue(strs[0].trim());
		String nvs[] = strs[1].split("=");
		if (nvs.length == 2) {
			String lowerCase = nvs[0].trim().toLowerCase();
			if (lowerCase.equals("name")) {
				this.setName(nvs[1].trim().replaceAll("\"", ""));
			} else if (lowerCase.equals("filename")) {
				this.setFileName(nvs[1].trim().replaceAll("\"", ""));
			}
		}
		if (strs.length == 3) {
			nvs = strs[2].split("=");
			if (nvs.length == 2) {
				String lowerCase = nvs[0].trim().toLowerCase();
				if (lowerCase.equals("name")) {
					this.setName(nvs[1].trim().replaceAll("\"", ""));
				} else if (lowerCase.equals("filename")) {
					this.setFileName(nvs[1].trim().replaceAll("\"", ""));
				}
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		String str = this.value;
		if (this.getName() != null)
			str += "; name=\"" + this.getName() + "\"";
		if (this.getFileName() != null)
			str += "; filename=\"" + this.getFileName() + "\"";
		return str;
	}

	public boolean isFile() {
		if (this.getFileName() == null)
			return false;
		return true;
	}
}
