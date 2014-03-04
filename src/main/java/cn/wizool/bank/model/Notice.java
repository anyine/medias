package cn.wizool.bank.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Notice")
public class Notice extends Task {

	@Column()
	private String important;

	public String getImportant() {
		return important;
	}

	public void setImportant(String important) {
		this.important = important;
	}

}
