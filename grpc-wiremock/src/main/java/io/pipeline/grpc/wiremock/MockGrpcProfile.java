package io.pipeline.grpc.wiremock;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;
import java.util.Set;

/**
 * Test profile that enables gRPC mocking by replacing the real DynamicGrpcClientFactory
 * with MockGrpcClientFactory that routes all calls to WireMock.
 */
public class MockGrpcProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        // Get WireMock port from system property
        String wireMockPort = System.getProperty("test.wiremock.port", "8080");

        return Map.ofEntries(
            // Use random port for tests
            Map.entry("quarkus.http.test-port", "0"),
            Map.entry("quarkus.grpc.server.use-separate-server", "false"),

            // Disable Consul service discovery - point services at WireMock
            Map.entry("quarkus.stork.platform-registration-service.service-discovery.type", "static"),
            Map.entry("quarkus.stork.platform-registration-service.service-discovery.address-list", "localhost:" + wireMockPort),

            Map.entry("quarkus.stork.account-manager.service-discovery.type", "static"),
            Map.entry("quarkus.stork.account-manager.service-discovery.address-list", "localhost:" + wireMockPort),

            // Disable automatic service registration during tests
            Map.entry("pipeline.registration.auto-register", "false")
        );
    }

    @Override
    public Set<Class<?>> getEnabledAlternatives() {
        return Set.of(MockGrpcClientFactory.class);
    }

    @Override
    public String getConfigProfile() {
        return "mock-grpc-test";
    }
}