# Porting tools

Run from the repository root. `.ci/mappings/` (Yarn + Mojang tables, not committed) comes
from the one-shot fetch-mappings CI job.

- `remap_mixins.py` - remap Yarn names in mixin strings (`method=`, `@At target=`, `@Invoker`).
- `remap_shadows.py` - remap `@Shadow` members reported by the Mixin AP as unresolved.
- `deferred_register.py` - convert eager registry fields to `RegistryObject` + add `.get()` at reads.

Forge patch check (done once during the port, see commit history): mixin `@At(INVOKE)` targets
were compared against Forge's 1.20.1 source patches (translated to SRG names with MCPConfig's
joined.tsrg), because the Mixin AP only verifies target *methods* at compile time, not the call
sites inside them. Forge's `if (false)` blocks (e.g. drowning in LivingEntity.baseTick) remove
calls that vanilla mixins expect.
