package study;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LocalDateTimeTest {

	@Test
	void isBeforeAndIsAfterTest() {
		LocalDateTime before = LocalDateTime.of(2023, 1, 12, 12, 30);
		LocalDateTime after = LocalDateTime.of(2023, 1, 13, 12, 30);

		Assertions.assertThat(before.isBefore(after)).isTrue();

		LocalDateTime validTime = LocalDateTime.of(2023, 1, 13, 12, 30);
		LocalDateTime now = LocalDateTime.of(2023, 1, 12, 12, 30);

		Assertions.assertThat(validTime.isAfter(now)).isTrue();
	}
}
