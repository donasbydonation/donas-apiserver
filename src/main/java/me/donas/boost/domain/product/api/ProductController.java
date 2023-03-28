package me.donas.boost.domain.product.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.product.application.ProductService;
import me.donas.boost.domain.product.dto.CreateProductRequest;

// TODO
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {
	private final ProductService productService;

	@PostMapping("/products")
	public ResponseEntity<Void> createProduct(@RequestBody CreateProductRequest request) {
		return null;
	}

}
