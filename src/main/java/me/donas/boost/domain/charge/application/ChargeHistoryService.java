package me.donas.boost.domain.charge.application;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.charge.repository.ChargeHistoryRepository;

@Service
@RequiredArgsConstructor
public class ChargeHistoryService {
	private final ChargeHistoryRepository chargeHistoryRepository;

}
