package cn.wizool.bank.iwebutil;

public class ContentType {
	private String type;
	private String boundary;
	private String subType;
	private String charset;

	public ContentType(String contentType) {
		String strs[] = contentType.split(";");
		if (strs.length == 2) {
			String parameters[] = strs[1].trim().split("=");
			if (parameters.length == 2) {
				if (parameters[0].toLowerCase().equals("charset")) {
					this.charset = parameters[1].toLowerCase();
				} else if (parameters[0].toLowerCase().equals("boundary")) {
					this.boundary = parameters[1];
				} else {
					System.out.println("Unknown content type parameter:"
							+ parameters[0]);
				}
			}

		} else {
			charset = null;
		}

		String types[] = strs[0].split("/");
		if (types.length == 2) {
			this.type = types[0].toLowerCase();
			this.subType = types[1].toLowerCase();
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getBoundary() {
		return boundary;
	}

	public void setBoundary(String boundary) {
		this.boundary = boundary;
	}

	@Override
	public String toString() {
		String str = type + "/" + subType;
		if (charset != null)
			str += "; charset=" + charset;
		if (boundary != null)
			str += "; boundary=" + boundary;
		return str;
	}

}
