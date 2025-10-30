package io.pipeline.grpc.wiremock;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.protobuf.Timestamp;
import org.wiremock.grpc.dsl.WireMockGrpcService;
import io.pipeline.repository.account.Account;
import io.pipeline.repository.account.CreateAccountResponse;
import io.pipeline.repository.account.InactivateAccountResponse;

import static io.pipeline.grpc.wiremock.WireMockGrpcCompat.*;

/**
 * Ready-to-use mock utilities for the Account Manager Service.
 * Uses standard gRPC mocking that works with both standard and Mutiny clients.
 */
public class AccountManagerMock {

    private final WireMockGrpcService mockService;

    public AccountManagerMock(int wireMockPort) {
        this.mockService = new WireMockGrpcService(
            new WireMock(wireMockPort), 
            "io.pipeline.repository.account.AccountService"
        );
    }

    /**
     * Mock successful account creation.
     */
    public AccountManagerMock mockCreateAccount(String accountId, String name, String description) {
        mockService.stubFor(
            method("CreateAccount")
                .willReturn(message(
                    CreateAccountResponse.newBuilder()
                        .setAccount(Account.newBuilder()
                            .setAccountId(accountId)
                            .setName(name)
                            .setDescription(description)
                            .setActive(true)
                            .setCreatedAt(Timestamp.newBuilder()
                                .setSeconds(System.currentTimeMillis() / 1000)
                                .setNanos((int) ((System.currentTimeMillis() % 1000) * 1_000_000))
                                .build())
                            .setUpdatedAt(Timestamp.newBuilder()
                                .setSeconds(System.currentTimeMillis() / 1000)
                                .setNanos((int) ((System.currentTimeMillis() % 1000) * 1_000_000))
                                .build())
                            .build())
                        .setCreated(true)
                        .build()
                ))
        );
        
        return this;
    }

    /**
     * Mock account already exists (not created).
     */
    public AccountManagerMock mockCreateAccountExists(String accountId, String name, String description) {
        mockService.stubFor(
            method("CreateAccount")
                .willReturn(message(
                    CreateAccountResponse.newBuilder()
                        .setAccount(Account.newBuilder()
                            .setAccountId(accountId)
                            .setName(name)
                            .setDescription(description)
                            .setActive(true)
                            .setCreatedAt(Timestamp.newBuilder()
                                .setSeconds((System.currentTimeMillis() - 1000) / 1000)
                                .setNanos((int) (((System.currentTimeMillis() - 1000) % 1000) * 1_000_000))
                                .build())
                            .setUpdatedAt(Timestamp.newBuilder()
                                .setSeconds((System.currentTimeMillis() - 1000) / 1000)
                                .setNanos((int) (((System.currentTimeMillis() - 1000) % 1000) * 1_000_000))
                                .build())
                            .build())
                        .setCreated(false)
                        .build()
                ))
        );
        
        return this;
    }

    /**
     * Mock account not found error.
     */
    public AccountManagerMock mockAccountNotFound(String accountId) {
        mockService.stubFor(
            method("GetAccount")
                .withRequestMessage(equalToMessage(
                    io.pipeline.repository.account.GetAccountRequest.newBuilder()
                        .setAccountId(accountId)
                        .build()
                ))
                .willReturn(org.wiremock.grpc.dsl.WireMockGrpc.Status.NOT_FOUND,
                    "Account not found: " + accountId)
        );

        return this;
    }

    /**
     * Mock successful account retrieval.
     */
    public AccountManagerMock mockGetAccount(String accountId, String name, String description, boolean active) {
        mockService.stubFor(
            method("GetAccount")
                .withRequestMessage(equalToMessage(
                    io.pipeline.repository.account.GetAccountRequest.newBuilder()
                        .setAccountId(accountId)
                        .build()
                ))
                .willReturn(message(
                    Account.newBuilder()
                        .setAccountId(accountId)
                        .setName(name)
                        .setDescription(description)
                        .setActive(active)
                        .setCreatedAt(Timestamp.newBuilder()
                            .setSeconds((System.currentTimeMillis() - 1000) / 1000)
                            .setNanos((int) (((System.currentTimeMillis() - 1000) % 1000) * 1_000_000))
                            .build())
                        .setUpdatedAt(Timestamp.newBuilder()
                            .setSeconds((System.currentTimeMillis() - 1000) / 1000)
                            .setNanos((int) (((System.currentTimeMillis() - 1000) % 1000) * 1_000_000))
                            .build())
                        .build()
                ))
        );
        
        return this;
    }

    /**
     * Mock successful account inactivation.
     */
    public AccountManagerMock mockInactivateAccount(String accountId) {
        mockService.stubFor(
            method("InactivateAccount")
                .willReturn(message(
                    InactivateAccountResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Account inactivated successfully")
                        .setDrivesAffected(0)
                        .build()
                ))
        );
        
        return this;
    }

    /**
     * Mock account inactivation failure (account not found).
     */
    public AccountManagerMock mockInactivateAccountNotFound(String accountId) {
        mockService.stubFor(
            method("InactivateAccount")
                .willReturn(message(
                    InactivateAccountResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Account not found: " + accountId)
                        .setDrivesAffected(0)
                        .build()
                ))
        );
        
        return this;
    }

    /**
     * Get the underlying WireMockGrpcService for advanced usage.
     */
    public WireMockGrpcService getService() {
        return mockService;
    }
}
