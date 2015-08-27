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

	@DatabaseField(generatedId = true, columnName = FIELD_ID)
	int id;

	@DatabaseField(columnName = FIELD_MODIFICATION_DATE, version = true)
	Date modificationDate;

	@DatabaseField(columnName = FIELD_SRC_DOCUMENT, foreign = true)
	Document document;

	@DatabaseField(columnName = FIELD_XMI)
	String xmi;

	@DatabaseField(columnName = FIELD_USER, foreign = true)
	User user;

}
