package support;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class Sequence {
	private final List<Executable> executables = new LinkedList<Executable>();

	private Sequence(Executable... executables) {
		for (Executable executable : executables) {
			add(executable);
		}
	}

	public static Sequence of(Executable... executables) {
		return new Sequence(executables);
	}

	public static int[] execute(Executable... executables) throws SQLException {
		return new Sequence(executables).execute();
	}

	public void add(Executable executable) {
		executables.add(executable);
	}

	public int[] execute() throws SQLException {
		int[] counts = new int[executables.size()];
		int i = 0;
		for (Executable executable : executables) {
			counts[i++] = executable.execute();
		}
		return counts;
	}
}
