package com.finanzen.api;



import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Base abstract class for infrastructure integration tests.
 * Spins up the Spring Boot application context and a lightweight Docker container
 * running a real PostgreSQL database to ensure a shared, single-boot test context.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class BaseIntegrationTest { // Classe abstrata para subir o contexto do Spring e do BD uma única vez

    /**
     * Captures the ephemeral random HTTP port allocated by Spring Boot.
     */
    @LocalServerPort
    protected int port; // Captura porta HTTP aleatória onde a API vai subir

    /**
     * Instantiates an isolated official PostgreSQL Docker container instance.
     * Uses '@ServiceConnection' to dynamically discover and bind container database credentials
     * directly into the Spring DataSource configuration without manual property registry overrides.
     */
    @Container
    @ServiceConnection // Spring auto-configura as credenciais de conexao do banco
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine");


    /// Container genérico rodando a imagem em producao
    @Container
    public static GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);

    /// Injeta a porta aleatória no Spring
    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379));
    }
}
