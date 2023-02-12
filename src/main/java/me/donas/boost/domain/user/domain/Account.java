package me.donas.boost.domain.user.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Account {
	String bankCode;

	String accountNumber;

	@Builder
	public Account(String bankCode, String accountNumber) {
		this.bankCode = bankCode;
		this.accountNumber = accountNumber;
	}
}
