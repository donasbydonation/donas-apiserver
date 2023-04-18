package study;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

public class DeserializeTest {

	ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void StringTest() throws IOException {
		String requestBody = "{\"str\":\"2023-4-23T23:00:00\"}";
		StrTest result = objectMapper.readValue(requestBody, StrTest.class);
		System.out.println(result.str);
	}

	@Test
	void LocalDateTimeDeserializeTest() throws IOException {
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addDeserializer(LocalDateTime.class, new OffsetDateTimeWithLocalDateTimeDeserializer());

		objectMapper.registerModule(javaTimeModule);
		String requestBody = "{\"time\":\"2023-04-23T12:00:00.000Z\"}";
		Time result = objectMapper.readValue(requestBody, Time.class);
		System.out.println(result.getTime().toString());
	}

	public static class OffsetDateTimeWithLocalDateTimeDeserializer extends LocalDateTimeDeserializer {

		public OffsetDateTimeWithLocalDateTimeDeserializer() {
			super(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		}

		@Override
		public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
			return super.deserialize(parser, context);
		}

	}
	public static class Time {
		ZonedDateTime time;

		public Time() {

		}

		public Time(ZonedDateTime time) {
			this.time = time;
		}

		public ZonedDateTime getTime() {
			return time;
		}

		public void setTime(ZonedDateTime time) {
			this.time = time;
		}
	}

	public static class StrTest {
		String str;

		public StrTest() {

		}
		public StrTest(String str) {
			this.str = str;
		}

		public String getStr() {
			return str;
		}

		public void setStr(String str) {
			this.str = str;
		}
	}
}
