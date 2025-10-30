package io.pipeline.grpc.wiremock;

import io.pipeline.common.grpc.GrpcClientFactory;
import io.pipeline.platform.registration.MutinyPlatformRegistrationGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;

/**
 * Mock implementation of GrpcClientFactory that routes all gRPC calls to WireMock.
 * Use this in test profiles to bypass Consul service discovery.
 */
@Alternative
@ApplicationScoped
public class MockGrpcClientFactory implements GrpcClientFactory {

    private final int wireMockPort;
    private ManagedChannel mockChannel;

    public MockGrpcClientFactory() {
        // Get WireMock port from system property set by WireMockGrpcTestBase
        this.wireMockPort = Integer.parseInt(
            System.getProperty("test.wiremock.port", "0")
        );
        
        if (wireMockPort > 0) {
            this.mockChannel = ManagedChannelBuilder
                .forAddress("localhost", wireMockPort)
                .usePlaintext()
                .build();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Uni<T> getMutinyClientForService(String serviceName) {
        if (mockChannel == null) {
            return Uni.createFrom().failure(
                new IllegalStateException("WireMock port not configured for testing")
            );
        }

        // For now, we only support platform registration
        if ("platform-registration-service".equals(serviceName)) {
            return (Uni<T>) getPlatformRegistrationClient(serviceName);
        }
        
        return Uni.createFrom().failure(
            new UnsupportedOperationException("Mock not implemented for service: " + serviceName)
        );
    }

    @Override
    public Uni<MutinyPlatformRegistrationGrpc.MutinyPlatformRegistrationStub> getPlatformRegistrationClient(String serviceName) {
        if (mockChannel == null) {
            return Uni.createFrom().failure(
                new IllegalStateException("WireMock port not configured for testing")
            );
        }

        MutinyPlatformRegistrationGrpc.MutinyPlatformRegistrationStub client =
            MutinyPlatformRegistrationGrpc.newMutinyStub(mockChannel);

        return Uni.createFrom().item(client);
    }

    public Uni<io.pipeline.repository.account.MutinyAccountServiceGrpc.MutinyAccountServiceStub> getAccountServiceClient(String serviceName) {
        if (mockChannel == null) {
            return Uni.createFrom().failure(
                new IllegalStateException("WireMock port not configured for testing")
            );
        }

        io.pipeline.repository.account.MutinyAccountServiceGrpc.MutinyAccountServiceStub client =
            io.pipeline.repository.account.MutinyAccountServiceGrpc.newMutinyStub(mockChannel);

        return Uni.createFrom().item(client);
    }

    @Override
    public int getActiveServiceCount() {
        return mockChannel != null ? 1 : 0;
    }

    @Override
    public void evictChannel(String serviceName) {
        // No-op for mock
    }

    @Override
    public String getCacheStats() {
        return "Mock gRPC client factory - routing to WireMock on port " + wireMockPort;
    }
}