package io.pipeline.grpc.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base test class that provides easy access to all service mocks.
 * 
 * Usage:
 * @QuarkusTest
 * @TestProfile(MockGrpcProfile.class)
 * public class MyTest extends WireMockTestBase {
 *     @Test
 *     public void testSomething() {
 *         // Setup mocks
 *         mocks.accountManager().mockGetAccount("test", "Test", "Desc", true);
 *         mocks.platformRegistration().mockServiceRegistration();
 *         
 *         // Your test code here
 *     }
 * }
 */
public abstract class WireMockTestBase {
    
    @InjectWireMock
    protected WireMockServer wireMockServer;
    
    protected ServiceMocks mocks;
    
    @BeforeEach
    protected void setUpMocks() {
        mocks = new ServiceMocks(wireMockServer);
    }
}
