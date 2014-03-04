package cn.wizool.bank.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Branch {

	@Id
	private String id;

	@Column(nullable = true)
	private String name;

	@ManyToOne()
	private Branch parent;

	@OneToMany(mappedBy = "parent")
	private List<Branch> child = new ArrayList<Branch>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Branch getParent() {
		return parent;
	}

	public void setParent(Branch parent) {
		this.parent = parent;
	}

	public List<Branch> getChild() {
		return child;
	}

	public void setChild(List<Branch> child) {
		this.child = child;
	}
	
	
}
