package study;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class recordTest {

	@Test
	void recordToString() {
		Pojo p = new Pojo("name", 1);
		Assertions.assertThat(p.toString()).hasToString("Pojo[name=name, age=1]");
	}

	record Pojo(String name, int age) {

	}
}
