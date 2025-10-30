/**
 * @io-pipeline/grpc-stubs
 * 
 * TypeScript Connect-ES stubs for Pipeline Engine services
 * 
 * This package provides:
 * - Generated protobuf message types and service clients
 * - Binary transport utilities (ALWAYS use binary format)
 * - Type registry for protobuf Any field handling
 */

// Export transport utilities
export { 
  createBinaryTransport, 
  createModuleTransport,
  toDisplayJson,
  getProxyUrl,
  DEFAULT_PROXY_URL 
} from './transport.js';

export { typeRegistry } from './registry.js';

