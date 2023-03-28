package me.donas.boost.domain.payment.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

	@Transactional
	public void success() {

	}

	@Transactional
	public void fail() {

	}
}
