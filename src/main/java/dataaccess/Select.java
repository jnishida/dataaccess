package dataaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import support.ExecutableQuery;

public final class Select<E> implements ExecutableQuery<E> {
	private Connection connection;
	private Class<Entity> entityClass;
	private String table;
	private String[] columns;
	private String where;

	public Select(Connection connection) {
		this.connection = connection;
		this.columns = new String[]{"*"};
	}

	public Select<E> columns(String... columns) {
		this.columns = columns;
		return this;
	}

	public Select<E> from(String table) {
		this.table = table;
		this.entityClass = classOf(table);
		return this;
	}

	public Select<E> from(Class<E> entity) {
		this.table = entity.getSimpleName();
		@SuppressWarnings("unchecked")
		Class<Entity> e = (Class<Entity>)entity;
		this.entityClass = e;
		return this;
	}

	public Select<E> where(String where) {
		this.where = where;
		return this;
	}

	@Override
	public List<E> executeQuery() throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;

		List<E> list = new ArrayList<E>();
		try {
			String sql = biuldSQL();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				list.add(row(rs));
			}
			return list;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private E row(ResultSet rs) throws SQLException {
		try {
			Entity entity = entityClass.newInstance();
			@SuppressWarnings("unchecked")
			E row = (E)entity.row(rs);
			return row;
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private String biuldSQL() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(createColumns(columns));
		sql.append(" FROM ");
		sql.append(table);
		if (where != null && !where.isEmpty()) {
			sql.append(" WHERE ");
			sql.append(where);
		}
		return sql.toString();
	}

	private String createColumns(String[] array) {
		StringBuilder sb = new StringBuilder();
		for (String obj : array) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(obj);
		}

		return sb.toString();
	}

	private Class<Entity> classOf(String table) {
		String[] split = table.split(" |\t|\r|\n");
		try {
			@SuppressWarnings("unchecked")
			Class<Entity> cls = (Class<Entity>)Class.forName("entity." + split[0]);
			return cls;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
