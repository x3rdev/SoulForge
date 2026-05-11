# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Soul Forge is a comprehensive magic-themed NeoForge Minecraft mod for version 1.21.1. It adds an occult progression system, custom crafting mechanics, magical items/weapons, animated entities, custom enchantments, and a lore-driven boss encounter.

**Core Features:**
- **Research & Progression:** Ancient tablets unlock research by discovering incantation characters
- **Crafting Systems:** Soul Anvil (soul-powered tool crafting) and Ritual recipes (complex multi-step rituals)
- **Magic Items:** Soul Steel armor/weapons, scythes, wands, necklaces, amulets with special abilities
- **Custom Entities:** Wisps, ghosts, souls, projectiles, and the Nergal boss
- **Magical Blocks:** Soul crystals, soul wood, pedestals, rituals altars, soul cauldrons, statues
- **Enchantments:** Custom enchantment effects (e.g., Reaping)
- **World Generation:** Custom structures and biomes featuring soul wood trees

**Key tech stack:**
- NeoForge 21.1.118 (Minecraft modding framework)
- GeckoLib 4.7.7 (entity/block animations)
- SmartBrainLib 1.16.7 (AI/behavior trees)
- Patchouli 1.21.1-92 (in-game grimoire guidebook)
- JEI 19.21.2 (recipe integration)
- Curios API 9.5.1 (accessory/necklace slots)

## Build & Development

### Running the Mod

```bash
# Client (dev environment with GUI)
./gradlew runClient

# Server (headless)
./gradlew runServer

# Data generation (generates JSON files from code)
./gradlew runData
```

### Building

```bash
# Build the JAR
./gradlew build

# JAR is output to: build/libs/soul_forge-1.0.0.jar
```

### IDE Setup

Open the project as a Gradle project in IntelliJ IDEA. Build configuration handles Minecraft mappings and source downloads automatically.

### Important Gradle Tasks

- `runData` — Regenerates all JSON content in `src/generated/resources/` from code. Run this after modifying registries or data providers.
- `build` — Full clean build, runs tests if any exist.

## Code Architecture

### Module Organization

```
src/main/java/com/github/x3rdev/soul_forge/
├── SoulForge.java                    # Main mod entry point, event bus setup
├── SoulForgeClient.java              # Client-only setup
├── client/                           # Client-side (rendering, screens, keybinds)
│   ├── screen/                       # GUIs (ResearchTableScreen, etc.)
│   ├── renderer/                     # Block/entity/item renders
│   ├── key_mapping/                  # Keybind definitions
│   └── shader/                       # Post-processing shaders
├── common/                           # Shared logic (both sides)
│   ├── registry/                     # Registry definitions (blocks, items, etc.)
│   ├── packet/                       # Network packets & handlers
│   ├── menu/                         # Container logic (Vanilla GUIs)
│   ├── block/                        # Block implementations
│   ├── block_entity/                 # Tile entity logic
│   ├── item/                         # Item implementations
│   ├── entity/                       # Entity AI & data
│   ├── recipe/                       # Custom recipe types (SoulAnvil, Ritual)
│   ├── research/                     # Research tree & progression system
│   ├── datagen/                      # Data generation providers
│   └── compat/                       # Mod compatibility (JEI, Curios, etc.)
└── mixin/                            # Bytecode patches to Minecraft

src/main/resources/data/soul_forge/
├── research/                         # Research definitions (JSON)
├── recipe/                           # Recipe JSON files
├── structures/                       # Structure files for worldgen
├── patchouli_books/                  # In-game book content
└── data_maps/                        # Item property mappings (word lists, etc.)

src/generated/resources/              # Output from runData (gitignored, regenerate with runData)
```

### Core Systems

#### Registry System

All Minecraft content is registered in `common/registry/`. Each registry class (e.g., `ItemRegistry`, `BlockRegistry`) uses lazy-initialized DeferredRegister:

```java
public class ItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SoulForge.MOD_ID);
    public static final RegistryObject<Item> ANCIENT_TABLET = ITEMS.register("ancient_tablet", AncientTablet::new);
}
```

Registries are initialized in `SoulForge.java` constructor by calling `.register(modEventBus)` on each one. Registry classes include:
- `ItemRegistry` — Armor, weapons, tools, containers, consumables, trinkets
- `BlockRegistry` — Magical blocks (soul crystal, soul wood, decorative, functional)
- `BlockEntityRegistry` — Block logic (altars, anvils, cauldrons, pedestals)
- `EntityRegistry` — Living entities and projectiles
- `RecipeTypeRegistry` — Soul Anvil and Ritual recipe types
- `MenuTypeRegistry` — Container GUIs (research table, soul anvil, cauldron, etc.)
- `EnchantmentEffectsRegistry` — Custom enchantment effects
- `SoundRegistry` — Ambient and effect sounds
- Plus 15+ other registries for materials, damage types, world gen, etc.

#### Research & Progression System

- **Research.java:** Core data class holding incantations, unlock requirements, and progression gates
- **ResearchTree.java:** Manages the directed graph of research prerequisites and unlocks
- **WordList.java:** Data-driven word lists (common/uncommon/rare/epic) applied to items via data maps
- **AncientTablet.java:** Item that generates randomized word lists based on a seed; used to discover research characters
- **Data source:** `src/main/resources/data/soul_forge/research/*.json`

Gameplay flow: Players read ancient tablets at the Research Table to discover characters. When all characters of an incantation are discovered, the research unlocks automatically.

#### Crafting Systems

**Soul Anvil Recipe:**
- 3×3 grid of inputs + 4 outer slots (typically souls)
- Output a crafted item
- Custom energy/soul cost
- Located in `common/recipe/SoulAnvilRecipe.java` and `src/main/resources/data/soul_forge/recipe/soul_anvil/*.json`

**Ritual Recipe:**
- Multi-step crafting using a Ritual Altar
- Requires specific item placement and activation
- Located in `common/recipe/RitualRecipe.java` and `src/main/resources/data/soul_forge/recipe/ritual/*.json`

#### Packet System

Client-to-server packets (player actions):
- `PickWordPayload` — Pick a word on a research tablet (discovers characters)
- `UpdateUnlockedResearchPayload` — Change active research being studied
- `StartSoulAnvilPayload` — Initiate Soul Anvil crafting

Server-to-client packets (data sync):
- `SendDiscoveredCharsPayload` — Notify client of newly discovered characters
- `SendResearchDataPayload` — Sync full research unlock list at login
- `SendParticlePayload` — Display particle effects

All handlers in `common/packet/handler/` with routing in `PacketRegistry.java`.

**Security note:** Server-side handlers must validate all client input. See `common/packet/handler/ServerPayloadHandler.java` for validation patterns.

#### Menu (Container) System

`common/menu/` contains server-side GUI logic:
- `ResearchTableMenu` — Research study, word picking, character discovery
- `SoulAnvilMenu` — Soul Anvil crafting interface
- `SoulCauldronMenu` — Cauldron interaction (soul extraction/storage)

Client screens (`client/screen/`) render and handle user input, delegating logic to menus.

#### Magic Items & Weapons

**item/ directory contains:**
- `Scythe.java` — Melee weapon with custom attack
- `SoulScythe.java` — Scythe variant that fires projectiles
- `AwakenedSoulSteelSword.java` — Sword with soul-based properties
- `WandOfSparking.java` — Ranged weapon that spawns spark particles
- `SoulContainer.java` — Holds soul entities
- `OccultNecklace.java` — Curios slot item for stat bonuses
- `WispAmulet.java` — Protective amulet
- `ResearcherGlasses.java` — Vision enhancement
- Armor sets: `SoulSteelArmor.java`, `AwakenedSoulSteelArmor.java`

#### Entity System

**Entities (common/entity/):**
- `Soul.java` — Drops from mobs, collected and used in crafting
- `Ghost.java` — Passive/neutral entity spawned in world
- `WispEntity.java` — Smaller magical entity with AI
- `Spark.java` — Harmless particle-like entity for visual effects
- `SoulScytheProjectile.java` — Projectile fired by scythes

**Nergal Boss (common/entity/nergal/):**
- `Nergal.java` — Boss entity with complex AI
- `NergalSpawn.java` — Spawning conditions/summoning logic
- `brain/` — SmartBrainLib behavior trees for boss attacks and movement

#### Enchantments

- `ReapingEnchantmentEffect.java` — Custom enchantment effect for harvesting
- `EnchantmentBootstrap.java` — Registers enchantment effects
- Applied via NeoForge's enchantment system

#### Block Entities & Special Blocks

**Blocks with logic (common/block_entity/):**
- `RitualAltarBlockEntity.java` — Ritual recipe execution
- `SoulAnvilBlockEntity.java` — Soul anvil crafting progress
- `SoulCauldronBlockEntity.java` — Soul storage/extraction
- `PedestalBlockEntity.java` — Display/activation block
- `StatueBlockEntity.java` — Animated statue (GeckoLib)
- `DarkTombBlockEntity.java` — Tomb structure block

**Decorative/Functional Blocks (common/block/):**
- Soul crystals, soul wood, candles, chandeliers, lianas, ores, saplings

### Data Generation

Recipes, research definitions, loot tables, advancements, and language files are defined as Java code and generated to JSON/lang files via `common/datagen/`. Data providers include:
- `RecipeProvider` — Soul Anvil and Ritual recipes
- `ResearchProvider` — Research trees and unlock conditions
- `BlockTagProvider`, `ItemTagProvider` — Tag definitions
- `LootTableProvider` — Mob drops and block loot
- `AdvancementProvider` — Progression achievements
- `LanguageProvider` — English language strings
- Various other providers for dimensions, structures, world gen

When modifying data-driven content:

1. Edit the data provider class (e.g., `RecipeProvider.java`)
2. Run `./gradlew runData`
3. Commit the generated files in `src/generated/resources/`

### Client-Side Systems

**Screens (GUIs):**
- `ResearchTableScreen` — Research study interface with word/character discovery
- `SoulAnvilScreen` — Soul Anvil crafting interface
- Both extend `AbstractContainerScreen` and bind to menus

**Renderers:**
- `client/renderer/entity/` — Entity renderers (with GeckoLib support for animated entities)
- `client/renderer/block/` — Block and block entity renderers
- `client/renderer/item/` — Item renderers for special display
- `client/renderer/layer/` — Armor/layer rendering for custom properties

**Particles:**
- `client/particle/` — Custom particle types and behaviors

**Shaders & Post-Processing:**
- `client/shader/` — Post-chain shaders for visual effects (outlines, glows)

**Key Mappings:**
- `client/key_mapping/` — Keybind definitions for player actions

### Event-Driven Architecture

NeoForge uses an event bus for hooks. Subscribe in `SoulForge.java` or side-specific setup classes:

```java
modEventBus.addListener(CommonSetup::createEntityAttributes);  // Mod event bus (mod-specific)
neoEventBus.addListener(CommonSetup::onDeath);                // NeoForge event bus (game-wide)
```

Common events subscribed:
- `EntityAttributeCreationEvent` — Define mob stats
- `LivingDeathEvent` — Soul drop logic on mob death
- `LivingTickEvent` — Per-tick entity logic
- `RegisterPayloadHandlersEvent` — Network packet registration
- `PlayerLoggedInEvent` — Sync research data to new players
- `EntityAttackEvent` — Custom weapon effects
- `LivingFallEvent` — Fall damage modifications

## Code Style & Conventions

### Naming

- Packets/payloads use PascalCase ending in `Payload` (e.g., `PickWordPayload`)
- Registries use PascalCase ending in `Registry` (e.g., `BlockRegistry`)
- Data providers end in `Provider` (e.g., `RecipeProvider`)
- Handlers end in `Handler` (e.g., `ServerPayloadHandler`)

### Formatting

- **No space before parentheses:** `if(condition)` not `if (condition)` (configured in memory)
- **UTF-8 encoding** (enforced in build.gradle)

### Resource Location Format

Item/block/sound identifiers use the mod ID:
```java
ResourceLocation.fromNamespaceAndPath(SoulForge.MOD_ID, "item_name")
// Results in: soul_forge:item_name
```

## Testing

Currently no automated tests. Manual testing:
- `./gradlew runClient` to test in dev environment
- Check logs in `run/logs/latest.log`

## Performance Notes

- Network packets are server-threaded; validate client input early
- Registry access and research lookups can be cached
- Custom structures generate lazily on chunk load

## Debugging

- Check logs in `run/logs/latest.log`
- For data generation issues, run `./gradlew clean runData` to regenerate JSON files

## Dependencies

**Core Libraries:**
- **NeoForge:** Modding framework
- **GeckoLib:** Entity and block animations
- **SmartBrainLib:** AI behavior trees for complex entity behaviors

**Optional Integrations:**
- **Patchouli:** In-game guidebook
- **JEI:** Recipe integration
- **Curios API:** Accessory slots for trinkets

Compatibility code lives in `common/compat/` with conditional loading checks.

## Notes & TODOs

When noting something to come back to — a bug, a missing feature, a design decision — add it to the **TODO section in `README.md`** under the appropriate category. Do not leave standalone TODO comments in code for things that require broader context; put them in the README where they are visible and tracked.
