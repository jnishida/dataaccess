package support;

import java.sql.SQLException;

public interface Executable {
	abstract int execute() throws SQLException;
}
