package de.nilsreiter.util.db;

public class SQLBuilder {
	StringBuilder sb = new StringBuilder();

	public SQLBuilder select(String... cols) {
		sb.append("SELECT ");

		for (String col : cols) {
			sb.append(col).append(',');
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(' ');
		return this;
	}

	public SQLBuilder from(String s) {
		sb.append("FROM ").append(s).append(' ');
		return this;
	}

	public SQLBuilder where(String condition) {
		sb.append(" WHERE ").append(condition).append(' ');
		return this;
	}

	public SQLBuilder insert(String into, String... colnames) {
		sb.append("INSERT INTO ").append(into).append(' ');
		if (colnames.length > 0) {
			sb.append('(');
			for (String col : colnames) {
				sb.append(col).append(',');
			}
			this.removeLastChar();
			sb.append(')');
		}
		return this;
	}

	public SQLBuilder values(String... values) {
		sb.append("VALUES (");
		for (String val : values) {
			sb.append(val).append(',');
		}
		removeLastChar();
		sb.append(")");
		return this;
	}

	public SQLBuilder create(String tablename) {
		sb.append("CREATE TABLE ").append(tablename).append(" ");
		return this;
	}

	public SQLBuilder createIfNotExists(String tablename) {
		sb.append("CREATE TABLE IF NOT EXISTS ").append(tablename).append(" ");
		return this;
	}

	public SQLBuilder struct(String s) {
		sb.append("(").append(s).append(")");
		return this;
	}

	public SQLBuilder update(String table) {
		sb.append("UPDATE ").append(table).append(' ');
		return this;
	}

	public SQLBuilder set(String[] cols, String[] vals) {
		sb.append("SET ");
		for (int i = 0; i < cols.length; i++) {
			sb.append(cols[i]).append("='").append(vals[i]).append("',");
		}
		removeLastChar();
		return this;
	}

	protected void removeLastChar() {
		sb.deleteCharAt(sb.length() - 1);

	}

	public SQLBuilder groupBy(String s) {
		sb.append(" GROUP BY ").append(s);
		return this;
	}

	public SQLBuilder delete(String from) {
		sb.append("DELETE FROM ").append(from).append(' ');
		return this;
	}

	public SQLBuilder drop(String s) {
		sb.append("DROP TABLE ").append(s);
		return this;
	}

	@Override
	public String toString() {
		return sb.toString();
	}
}
