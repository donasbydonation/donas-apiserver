package me.donas.boost.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class QuerydslConfig {

	@PersistenceContext
	private final EntityManager em;

	@Bean
	public JPAQueryFactory querydsl() {
		return new JPAQueryFactory(em);
	}
}
