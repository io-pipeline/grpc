package io.pipeline.grpc.wiremock;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.wiremock.grpc.dsl.WireMockGrpcService;
import com.google.protobuf.Empty;

import static io.pipeline.grpc.wiremock.WireMockGrpcCompat.*;

/**
 * Simple service mock for basic gRPC operations.
 * This is a placeholder that can be extended for specific services.
 */
public class SimpleServiceMock {
    
    private final WireMockGrpcService mockService;
    
    public SimpleServiceMock(int port) {
        this.mockService = new WireMockGrpcService(
            new WireMock(port), 
            "SimpleService" // Generic service name
        );
    }
    
    /**
     * Mock a simple health check.
     */
    public SimpleServiceMock mockHealthCheck() {
        mockService.stubFor(
            method("HealthCheck")
                .willReturn(message(Empty.getDefaultInstance()))
        );
        return this;
    }
    
    /**
     * Mock a simple ping operation.
     */
    public SimpleServiceMock mockPing() {
        mockService.stubFor(
            method("Ping")
                .willReturn(message(Empty.getDefaultInstance()))
        );
        return this;
    }
    
    /**
     * Setup default mocks for basic operations.
     */
    public SimpleServiceMock setupDefaults() {
        return mockHealthCheck()
               .mockPing();
    }
}