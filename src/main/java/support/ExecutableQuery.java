package support;

import java.sql.SQLException;
import java.util.List;

public interface ExecutableQuery<E> {
	abstract List<E> executeQuery() throws SQLException;
}
