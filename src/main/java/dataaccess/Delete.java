package dataaccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import entity.Entity;
import support.Executable;

public class Delete implements Executable {
	private Connection connection;
	private String table;
	private String where;

	private Delete(Connection connection) {
		this.connection = connection;
	}

	public static Delete of(Connection connection) {
		return new Delete(connection);
	}

	public Delete from(String table) {
		this.table = table;
		return this;
	}

	public Delete from(Class<? extends Entity> entity) {
		this.table = entity.getSimpleName();
		return this;
	}

	public Delete where(String where) {
		this.where = where;
		return this;
	}

	@Override
	public int execute() throws SQLException {
		Statement stmt = null;
		try {
			String sql = biuldSQL();
			stmt = connection.createStatement();
			return stmt.executeUpdate(sql);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private String biuldSQL() {
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(table);
		if (where != null && !where.isEmpty()) {
			sql.append(" WHERE ");
			sql.append(where);
		}
		return sql.toString();
	}
}
