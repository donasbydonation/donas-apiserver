package me.donas.boost.global.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import me.donas.boost.domain.common.admin.AdminUser;

@Configuration
@EnableConfigurationProperties(value = {
	AdminUser.class
})
public class PropertyConfig {
}
