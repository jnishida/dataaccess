package entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Entity {
	public abstract Entity row(ResultSet rs) throws SQLException;
}
