package me.donas.boost.domain.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	String name;

	int count;

	long price;

	long totalPrice;
}
