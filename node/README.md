# @io-pipeline/grpc-stubs

TypeScript Connect-ES stubs for Pipeline Engine services, generated from Protocol Buffers.

## Installation

```bash
pnpm add @io-pipeline/grpc-stubs
# or
npm install @io-pipeline/grpc-stubs
```

## Usage

```typescript
import { PlatformRegistrationClient } from '@io-pipeline/grpc-stubs/registration';
import { createPromiseClient } from '@connectrpc/connect';
import { createConnectTransport } from '@connectrpc/connect-web';

const transport = createConnectTransport({
  baseUrl: 'http://localhost:38106'
});

const client = createPromiseClient(PlatformRegistrationService, transport);
const response = await client.listServices({});
```

## Versioning

This package is versioned in sync with the Java `grpc-stubs` module. When proto files change and Java stubs are updated to version `1.5.0`, this package will also be updated to `1.5.0`.

## Generated From

These stubs are generated from the Protocol Buffer definitions in `grpc/grpc-stubs/src/main/proto/`.

## Build

```bash
# Generate stubs from proto files
pnpm run build

# Clean generated files
pnpm run clean
```

## Registry

Published to:
- Primary: https://git.rokkon.com/api/packages/io-pipeline/npm
- Secondary: https://npm.pkg.github.com

## License

Apache-2.0

