package study;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ThreadLocalRandomTest {
	@Test
	void getRandom() {
		IntStream.range(1, 100).forEach((c) -> {
			int i = ThreadLocalRandom.current().nextInt(0, 999999);
			String str = String.format("%06d", i);
			System.out.println(str);
			Assertions.assertThat(str).hasSize(6);
			Assertions.assertThat(Integer.parseInt(str)).isEqualTo(i);
		});
	}
}
