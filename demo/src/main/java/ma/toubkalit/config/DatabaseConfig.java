package ma.toubkalit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "ma.toubkalit.repositories")
@EntityScan(basePackages = "ma.toubkalit.entity")
public class DatabaseConfig {
}