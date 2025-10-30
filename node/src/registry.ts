/**
 * Type Registry for Protobuf Messages
 * 
 * This registry is used for handling google.protobuf.Any fields
 * and proper type resolution in binary protobuf communication.
 * 
 * NOT for JSON serialization - we use binary protobuf for all processing.
 */

import { createRegistry } from "@bufbuild/protobuf";

// For now, create an empty registry
// Services will add their own types as needed
export const typeRegistry = createRegistry();

// Export registry as default for convenience
export default typeRegistry;

