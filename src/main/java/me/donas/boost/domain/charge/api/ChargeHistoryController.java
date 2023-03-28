package me.donas.boost.domain.charge.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.charge.application.ChargeHistoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ChargeHistoryController {
	private final ChargeHistoryService chargeHistoryService;


}
