package me.donas.boost.domain.product.application;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.donas.boost.domain.product.repository.ProductRepository;

// TODO
@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;


}
