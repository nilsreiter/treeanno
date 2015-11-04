package de.ustu.ims.reiter.treeanno.beans;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.j256.ormlite.field.DatabaseField;

@Entity(name = "treeanno_users")
public class User {
	@Column(unique = true)
	String username;

	@DatabaseField
	String email;

	@DatabaseField(generatedId = true)
	int id;

	@DatabaseField
	String language = "en";

	public String getName() {
		return username;
	}

	public void setName(String name) {
		this.username = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
