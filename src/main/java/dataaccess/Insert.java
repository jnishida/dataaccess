package dataaccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import support.Executable;

public class Insert implements Executable {
	private Connection connection;
	private String table;
	private Object[] columns = new String[]{};
	private List<Object[]> valuesList = new ArrayList<Object[]>();

	private Insert(Connection connection) {
		this.connection = connection;
	}

	public static Insert of(Connection connection) {
		return new Insert(connection);
	}
	public Insert into(String table) {
		this.table = table;
		return this;
	}

	public Insert into(Class<? extends Entity> entity) {
		this.table = entity.getSimpleName();
		return this;
	}

	public Insert columns(Object... columns) {
		this.columns = columns;
		return this;
	}

	public Insert columns(String... columns) {
		this.columns = columns;
		return this;
	}

	public Insert values(Object... values) {
		this.valuesList.add(values);
		return this;
	}

	@Override
	public int execute() throws SQLException {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			for (String sql : biuldSQL()) {
				stmt.addBatch(sql);
			}

			int[] counts = stmt.executeBatch();

			int totalCount = 0;
			for (int count : counts) {
				totalCount += count;
			}
			return totalCount;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private List<String> biuldSQL() {
		List<String> sqlList = new ArrayList<String>();
		for (Object[] values : valuesList) {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO ");
			sql.append(table);
			if (columns.length > 0) {
				sql.append(" (");
				sql.append(createColumns(columns));
				sql.append(")");
			}
			{
				sql.append(" VALUES (");
				sql.append(createValues(values));
				sql.append(")");
			}
			sqlList.add(sql.toString());
		}
		return sqlList;
	}

	private String createColumns(Object[] array) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : array) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(obj);
		}

		return sb.toString();
	}

	private String createValues(Object[] array) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : array) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			if (obj instanceof String) {
				sb.append("'");
				sb.append(obj);
				sb.append("'");
			} else {
				sb.append(obj);
			}
		}

		return sb.toString();
	}
}
