package de.ustu.ims.reiter.treeanno.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "treeanno_users_permissions")
public class UserPermission {
	@DatabaseField(generatedId = true)
	int id;

	@DatabaseField(foreign = true)
	User userId;

	@DatabaseField(foreign = true)
	Project projectId;

	@DatabaseField
	int level;
}
