package study;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.zone.ZoneOffsetTransition;

import org.junit.jupiter.api.Test;

public class ZonedDateTimeTest {

	static final int YEAR = 2023;
	static final int MONTH = 4;
	static final int DAY_OF_MONTH = 24;
	static final int HOUR = 21;
	static final int MINUTE = 30;
	static final int SECOND = 31;
	static final int NANO = 111;

	@Test
	void zonedDateTimeToLocalDateTimeTest() {
		ZonedDateTime zonedDateTime = ZonedDateTime.of(YEAR, MONTH, DAY_OF_MONTH, HOUR, MINUTE, SECOND, NANO,
			ZoneId.of("Asia/Seoul"));
		ZonedDateTime utcZonedDateTime = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
		ZonedDateTime startUtcZonedDateTime = utcZonedDateTime.minusHours(zonedDateTime.getHour());
		int hour = zonedDateTime.getHour();
		ZonedDateTime time = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).minusHours(hour);
		//2023-04-23T15:30:31.000000111Z
		System.out.println(zonedDateTime);
		System.out.println(utcZonedDateTime);
		System.out.println(startUtcZonedDateTime);
		System.out.println(time);

		LocalDateTime localDateTime = LocalDateTime.of(
			startUtcZonedDateTime.getYear(),
			startUtcZonedDateTime.getMonth(),
			startUtcZonedDateTime.getDayOfMonth(),
			startUtcZonedDateTime.getHour(),
			0
		);

		System.out.println(localDateTime);
		System.out.println(localDateTime.plusHours(zonedDateTime.getHour()));
		System.out.println(localDateTime.plusHours(24));
	}

	@Test
	void zonedDateTimeToLocalDateTimeTest2() {
		ZonedDateTime zonedDateTime = ZonedDateTime.of(YEAR, MONTH, DAY_OF_MONTH, HOUR, MINUTE, SECOND, NANO,
			ZoneId.of("Asia/Seoul"));
		ZoneId zone = zonedDateTime.getZone();
		LocalDateTime date = LocalDateTime.of(zonedDateTime.toLocalDate(), LocalTime.MIDNIGHT);
		System.out.println(date);
		ZoneOffsetTransition zoneOffsetTransition = ZoneOffsetTransition.of(date,
			ZoneOffset.of(zonedDateTime.getOffset().getId()),
			ZoneOffset.UTC);
		System.out.println(zoneOffsetTransition.getDateTimeAfter());
		System.out.println(zoneOffsetTransition.getDateTimeBefore());
	}
}
