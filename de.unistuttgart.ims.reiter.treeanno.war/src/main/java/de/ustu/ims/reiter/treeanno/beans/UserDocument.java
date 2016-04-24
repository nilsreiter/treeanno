package de.ustu.ims.reiter.treeanno.beans;

import java.util.Date;

import javax.persistence.Entity;

import com.j256.ormlite.field.DatabaseField;

@Entity(name = "treeanno_userdocument")
public class UserDocument {
	public static final String FIELD_ID = "id";
	public static final String FIELD_MODIFICATION_DATE = "modificationDate";
	public static final String FIELD_SRC_DOCUMENT = "document";
	public static final String FIELD_XMI = "xmi";
	public static final String FIELD_USER = "user";
	public static final String FIELD_STATUS = "status";

	@DatabaseField(generatedId = true, columnName = FIELD_ID)
	int id;

	@DatabaseField(columnName = FIELD_MODIFICATION_DATE, version = true)
	Date modificationDate;

	@DatabaseField(columnName = FIELD_SRC_DOCUMENT, foreign = true)
	Document document;

	@DatabaseField(columnName = FIELD_XMI, columnDefinition = "LONGTEXT")
	String xmi;

	@DatabaseField(columnName = FIELD_USER, foreign = true,
			foreignAutoRefresh = true)
	User user;

	@DatabaseField(columnName = FIELD_STATUS, defaultValue = "ASSIGNED")
	DocumentStatus status;

	public String getXmi() {
		return xmi;
	}

	public void setXmi(String xmi) {
		this.xmi = xmi;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public DocumentStatus getStatus() {
		return status;
	}

	public void setStatus(DocumentStatus status) {
		this.status = status;
	}

}
