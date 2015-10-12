package de.ustu.ims.reiter.treeanno.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "treeanno_users_permissions")
public class UserPermission {
	public static final String FIELD_ID = "id";
	public static final String FIELD_USER = "userId";
	public static final String FIELD_PROJECT = "projectId";
	public static final String FIELD_LEVEL = "level";

	@DatabaseField(columnName = FIELD_ID, generatedId = true)
	int id;

	@DatabaseField(columnName = FIELD_USER, foreign = true)
	User userId;

	@DatabaseField(columnName = FIELD_PROJECT, foreign = true)
	Project projectId;

	@DatabaseField(columnName = FIELD_LEVEL)
	int level;

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public Project getProjectId() {
		return projectId;
	}

	public void setProjectId(Project projectId) {
		this.projectId = projectId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getId() {
		return id;
	}
}
