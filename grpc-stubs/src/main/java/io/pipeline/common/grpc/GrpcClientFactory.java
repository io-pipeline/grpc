package io.pipeline.common.grpc;

import io.pipeline.data.module.MutinyPipeStepProcessorGrpc;
import io.pipeline.platform.registration.MutinyPlatformRegistrationGrpc;
import io.smallrye.mutiny.Uni;

/**
 * Interface for dynamic gRPC client creation and management.
 */
public interface GrpcClientFactory {

    /**
     * Get a generic Mutiny gRPC client for a service by name.
     */
    <T> Uni<T> getMutinyClientForService(String serviceName);

    /**
     * Get a platform registration client for service/module registration.
     */
    Uni<MutinyPlatformRegistrationGrpc.MutinyPlatformRegistrationStub> getPlatformRegistrationClient(String serviceName);

    /**
     * Get the number of active service connections being managed.
     */
    int getActiveServiceCount();

    /**
     * Evict (close) a cached channel for a service to force reconnection.
     */
    void evictChannel(String serviceName);

    /**
     * Get cache statistics for debugging and monitoring.
     */
    String getCacheStats();
}

