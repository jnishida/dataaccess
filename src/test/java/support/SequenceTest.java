package support;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;

import org.junit.Test;

import support.Executable;

public class SequenceTest {
	@Test
	public void test() throws Exception {
		Executable e3 = e(3);
		Executable e5 = e(5);
		Executable e7 = e(7);

		int[] actual = Sequence.execute(e3, e7, e5, e3, e5);

		assertThat(actual).containsExactly(3, 7, 5, 3, 5);
	}

	private Executable e(final int count) {
		return new Executable() {
			@Override
			public int execute() throws SQLException {
				return count;
			}
		};
	}
}
