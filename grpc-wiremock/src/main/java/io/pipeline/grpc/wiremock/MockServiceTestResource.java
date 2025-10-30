package io.pipeline.grpc.wiremock;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.util.Map;

/**
 * Quarkus test resource that automatically starts mock services.
 * 
 * This can be used with @QuarkusTestResource(MockServiceTestResource.class)
 * to automatically start mock services during tests.
 */
public class MockServiceTestResource implements QuarkusTestResourceLifecycleManager {

    @Override
    public Map<String, String> start() {
        // Start the mock platform registration service
        MockServiceFactory.startMockPlatformRegistrationService();
        
        // Return configuration overrides
        return Map.of(
            "quarkus.grpc.clients.platform-registration.host", "localhost",
            "quarkus.grpc.clients.platform-registration.port", String.valueOf(MockServiceFactory.getMockServer().getGrpcPort()),
            "quarkus.grpc.server.port", "0",
            "pipeline.test.mock-services.enabled", "true"
        );
    }

    @Override
    public void stop() {
        // Stop the mock services
        MockServiceFactory.stopMockPlatformRegistrationService();
    }
}
