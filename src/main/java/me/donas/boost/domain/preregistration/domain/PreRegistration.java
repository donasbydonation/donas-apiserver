package me.donas.boost.domain.preregistration.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PreRegistration {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	String email;

	@Builder
	public PreRegistration(String email) {
		this.email = email;
	}
}
