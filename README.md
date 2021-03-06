# NSCTool
A tool for managing NSCs of Pen & Paper role playing games.

## Status

| component | status | code |
|-----------|--------|------|
|api|[![api](https://github.com/Implex1v/NSCTool/actions/workflows/api.yml/badge.svg)](https://github.com/Implex1v/NSCTool/actions/workflows/api.yml)|[api](./api/)|
|web|[![web](https://github.com/Implex1v/NSCTool/actions/workflows/web.yml/badge.svg)](https://github.com/Implex1v/NSCTool/actions/workflows/web.yml)|[web](./web/)|
|_docs|[![_docs](https://github.com/Implex1v/NSCTool/actions/workflows/docs.yml/badge.svg)](https://github.com/Implex1v/NSCTool/actions/workflows/docs.yml)|[_docs](./_docs/)|

## Development

### Keycloak

For local development a default configuration for keycloak is provided.

The default user for the admin panel is: `admin:admin`.

| Type | Name | Password | Comment |
|------|------|----------|---------|
|realm|nstool|||
|client|api|hBQYe4NMrOMRFyPZNCu7KvGwE6w5DYPx||
|client|idm-client|efUD1vwp4VuVLzz999khJCIMZXV3nkMa|Client for managing users|
|client|web|a0l6upjfJP2RD54SC8F0lyy46fUZO0l1||
|user|user|123456|api-role: user|
|user|admin|123456|api-role: admin|
|user|api-admin|acSsYQMPh6bE2rTCKg4niHe6jW9NUWdw|User manager|

Data can be found at: `_data/keycloak`
