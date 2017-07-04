package dataaccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import entity.Entity;
import support.Executable;

public class Update implements Executable {
	private Connection connection;
	private String table;
	private String set;
	private String where;

	private Update(Connection connection, String table) {
		this.connection = connection;
		this.table = table;
	}

	private Update(Connection connection, Class<? extends Entity> entity) {
		this.connection = connection;
		this.table = entity.getSimpleName();
	}

	public static Update of(Connection connection, String table) {
		return new Update(connection, table);
	}

	public static Update of(Connection connection, Class<? extends Entity> entity) {
		return new Update(connection, entity);
	}

	public Update set(String set) {
		this.set = set;
		return this;
	}

	public Update where(String where) {
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
		sql.append("UPDATE ");
		sql.append(table);
		sql.append(" SET ");
		sql.append(set);
		if (where != null && !where.isEmpty()) {
			sql.append(" WHERE ");
			sql.append(where);
		}
		return sql.toString();
	}
}
