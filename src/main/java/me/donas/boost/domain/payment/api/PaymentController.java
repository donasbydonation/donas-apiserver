package me.donas.boost.domain.payment.api;

import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.payment.application.PaymentService;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {
	private static final String CLIENT_ID = "S2_af4543a0be4d49a98122e01ec2059a56";
	private static final String SECRET_KEY = "9eb85607103646da9f9c02b128f2e5ee";
	private final WebClient webClient = WebClient.create("https://sandbox-api.nicepay.co.kr");
	private static final String SUCCESS_CODE = "0000";

	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@PostMapping("/payment")
	public ResponseEntity<String> payment(@RequestParam String tid, @RequestParam Long amount) {

		AmountDto amountDto = new AmountDto(String.valueOf(amount));
		JsonNode responseNode = webClient.post().uri(uriBuilder -> uriBuilder.path("/v1/payments/" + tid).build())
			.header("Authorization",
				"Basic " + Base64.getEncoder().encodeToString((CLIENT_ID + ":" + SECRET_KEY).getBytes())
			)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.bodyValue(amountDto)
			.retrieve()
			.bodyToMono(JsonNode.class)
			.block();

		String resultCode = responseNode.get("resultCode").asText();

		System.out.println(responseNode.toPrettyString());

		if (resultCode.equalsIgnoreCase(SUCCESS_CODE)) {
			// 결제 성공 비즈니스 로직 구현
			paymentService.success();
		} else {
			// 결제 실패 비즈니스 로직 구현
			paymentService.fail();
		}

		return ResponseEntity.ok("st");
	}

	@PostMapping("/payment/cancel")
	public ResponseEntity<String> cancelPayment(@RequestParam String tid, @RequestParam Long amount) {

		CancelDto cancelDto = new CancelDto(String.valueOf(amount), "test", UUID.randomUUID().toString());
		JsonNode responseNode = webClient.post().uri(uriBuilder ->
				uriBuilder.path("/v1/payments/" + tid + "/cancel").build()
			)
			.header("Authorization",
				"Basic " + Base64.getEncoder().encodeToString((CLIENT_ID + ":" + SECRET_KEY).getBytes()))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.bodyValue(cancelDto)
			.retrieve()
			.bodyToMono(JsonNode.class)
			.block();

		String resultCode = responseNode.get("resultCode").asText();

		System.out.println(responseNode.toPrettyString());

		if (resultCode.equalsIgnoreCase("0000")) {
			// 취소 성공 비즈니스 로직 구현
		} else {
			// 취소 실패 비즈니스 로직 구현
		}

		return ResponseEntity.ok("st");
	}

	public static class AmountDto {
		private final String amount;

		public AmountDto(String amount) {
			this.amount = amount;
		}
	}

	public static class CancelDto {
		private final String amount;
		private final String reason;
		private final String orderId;

		public CancelDto(String amount, String reason, String orderId) {
			this.amount = amount;
			this.reason = reason;
			this.orderId = orderId;
		}
	}
}
