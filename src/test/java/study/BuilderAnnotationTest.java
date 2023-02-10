package study;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.Builder;
import lombok.Getter;

class BuilderAnnotationTest {

	@Test
	void builderAnnotationDefaultValue1() {
		Pojo hello = Pojo.builder().age(103).name("hello").build();
		Assertions.assertThat(hello.getAge()).isEqualTo(103);
		Assertions.assertThat(hello.getName()).isEqualTo("*hello*");
	}

	@Test
	void builderAnnotationDefaultValue2() {
		// 기본 값을 지정해도 설정되지 않는다. 아마도 Builder 패턴이 적용된 생성자에 age의 값을 설정하기 떄문인 듯 하다.
		// `@Builder` 을 클래스 지정으로 하고 `@Builder.Default`를 사용해야 할지도.
		Pojo hello = Pojo.builder().name("hello").build();
		Assertions.assertThat(hello.getAge()).isEqualTo(0);
		Assertions.assertThat(hello.getName()).isEqualTo("*hello*");
	}

	@Test
	void builderAnnotationDefaultValue3() {
		// 생성자에 들어 있지 않은 값은 Builder 패턴이 적용되지 않는다. 그리고 해당 생성자에서 적용한 값을 반영된다.
		Pojo hello = Pojo.builder().name("hello").build();
		Assertions.assertThat(hello.getAge()).isEqualTo(0);
		Assertions.assertThat(hello.getName()).isEqualTo("*hello*");
		Assertions.assertThat(hello.getCreatedAt()).isEqualTo(LocalDateTime.MIN);
	}

	@Getter
	static class Pojo {
		private String name;
		private int age = 10;
		private LocalDateTime createdAt = LocalDateTime.MAX;

		@Builder
		public Pojo(String name, int age) {
			this.name = "*" + name + "*";
			this.age = age;
			this.createdAt = LocalDateTime.MIN;
		}
	}
}
