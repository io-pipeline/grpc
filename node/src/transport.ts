/**
 * Transport utilities for binary protobuf communication
 * 
 * Always uses binary format for efficiency and type safety.
 * JSON is only used for display/debugging purposes.
 */

import { createConnectTransport } from "@connectrpc/connect-web";
import type { Transport } from "@connectrpc/connect";
import { typeRegistry } from "./registry.js";

/**
 * Default web-proxy URL
 */
export const DEFAULT_PROXY_URL = "";

/**
 * Create a binary transport for Connect-ES communication.
 * Always uses binary format for all service communication.
 * 
 * @param baseUrl - The base URL for the transport (defaults to web-proxy)
 * @param options - Additional options for the transport
 * @returns Configured transport for binary protobuf communication
 */
export function createBinaryTransport(
  baseUrl?: string,
  options: {
    headers?: Record<string, string>;
    interceptors?: Array<(next: any) => any>;
  } = {}
): Transport {
  const url = baseUrl || getProxyUrl();
  return createConnectTransport({
    baseUrl: url,
    
    // ALWAYS use binary format for processing
    useBinaryFormat: true,
    
    // Type registry for Any field handling (even in binary)
    jsonOptions: {
      registry: typeRegistry,
      ignoreUnknownFields: false,
    },
    
    // Custom interceptors if provided
    interceptors: options.interceptors || [],
    
    // No default timeout - let individual calls set their own
    // Streaming calls need no timeout, unary calls can set as needed
    
    // Merge in any additional headers
    ...(options.headers && {
      interceptors: [
        ...(options.interceptors || []),
        (next: any) => async (req: any) => {
          Object.entries(options.headers || {}).forEach(([key, value]) => {
            req.header.set(key, value);
          });
          return await next(req);
        },
      ],
    }),
  });
}

/**
 * Create a transport with module routing header.
 * Used when connecting to specific backend modules through the web-proxy.
 * 
 * @param moduleAddress - The address of the backend module (e.g., "localhost:39101")
 * @param baseUrl - The base URL for the web-proxy
 * @returns Transport configured for specific module communication
 */
export function createModuleTransport(
  moduleAddress: string,
  baseUrl: string = DEFAULT_PROXY_URL
): Transport {
  return createBinaryTransport(baseUrl, {
    interceptors: [
      (next: any) => async (req: any) => {
        // Add module address header for web-proxy routing
        req.header.set("x-module-address", moduleAddress);
        return await next(req);
      },
    ],
  });
}

/**
 * Convert a protobuf message to JSON for display purposes only.
 * This should ONLY be used for UI display or debugging.
 * 
 * @param message - The protobuf message to display
 * @returns JSON string for display
 */
export function toDisplayJson(message: any): string {
  return JSON.stringify(message, null, 2);
}

/**
 * Get the appropriate proxy URL based on environment
 */
export function getProxyUrl(): string {
  // Always use web-proxy directly - WebProxyBridge was removed in favor of Traefik
  if (typeof window !== 'undefined') {
    // Use web-proxy directly, not current origin
    return DEFAULT_PROXY_URL;
  }
  
  // In Node.js context
  if (typeof process !== 'undefined' && process.env) {
    return process.env.PROXY_URL || DEFAULT_PROXY_URL;
  }
  
  return DEFAULT_PROXY_URL;
}

