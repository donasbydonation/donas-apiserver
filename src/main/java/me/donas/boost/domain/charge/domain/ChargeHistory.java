package me.donas.boost.domain.charge.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ChargeHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;


}
