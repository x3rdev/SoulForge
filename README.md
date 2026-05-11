# Soul Forge

A magic-themed NeoForge mod for Minecraft 1.21.1. Features an occult progression system, soul-powered crafting, custom weapons/armor, animated entities, and a boss encounter.

---

## TODO

### Bugs (broken right now)

- [ ] **Spark projectile is invisible** — `Spark.shouldRender()` returns `false`. Players can't see wand projectiles.
- [ ] **Missing translation key** — `error.soul_forge.patchouli_not_installed` is referenced in `Necronomicon.java` but never defined in `SoulForgeLanguageProvider`.
- [ ] **Typo in whisper message** — "You're knowledge is... insufficient" → "Your knowledge..."

---

### Boss (Nergal)

- [ ] **Nergal drops nothing on death** — loot table is empty, boss fight has no reward. Add drops (e.g. soul steel ingot, scroll fragment, unique material).
- [ ] **SummonGhostsAttack is commented out** — attack is written and looks functional, just disabled with a TODO in `Nergal.java:161`. Re-enable it.
- [ ] **Boss arena / summoning context** — Scroll of Shadows works but Nergal has no dedicated spawn location or arena. Consider a trigger tied to the crypt structure.
- [ ] **Boss music / ambient sound pass** — idle, hurt, death sounds are wired but no boss bar or music event.

---

### Soul Steel Wings

- [ ] **No flight mechanic implemented** — animations are fully defined (fold, unfold, glide, fly, float) but gameplay does not exist. `entityFallEvent` only cancels fall damage with a commented-out velocity block. Needs proper glide/elytra-style logic with server-side velocity sync.
- [ ] **Animation controller not wired to player state** — controller always plays `fold_idle` regardless of whether the player is falling, gliding, or flying.

---

### Soul Type Variants

- [ ] **Undead Soul, Nether Soul, Ender Soul, Dragon Soul all disabled** — entity registrations, drop logic in `CommonSetup.onDeath`, and language entries are all commented out. Either re-enable with proper entity tags, or remove the dead code and commit to a single soul type.

---

### Awakened Soul Steel (Tier 2 gear)

- [ ] **No special abilities** — awakened sword, armor, and shield are visual-only variants. Define what makes this tier unique (e.g. soul drain on hit, damage reduction from undead, shield reflect mechanic).
- [ ] **Set bonus** — consider a passive effect when wearing the full awakened set.

---

### Patchouli Grimoire (Necronomicon)

- [ ] **Book has no content** — only `book.json` exists. No `en_us/` directory, no category JSONs, no entry JSONs. The book opens blank.
- [ ] **Write entries** for: souls & soul bottles, research table, soul anvil, ritual altar, scythes, soul steel, awakened soul steel, Nergal lore, wings, wisp amulet.

---

### Research & Progression

- [ ] **Placeholder descriptions** — `soul.json` and `soul_anvil.json` have `"description": "TODO, this needs to be a translation key"` showing as literal text in-game.
- [ ] **Sparse research tree** — only 6 nodes. Many items (wings, necklace, wand, scythes) have no research gate. Flesh out the tree to gate progression meaningfully.
- [ ] **Research not gating crafting** — most soul anvil and ritual recipes are not locked behind research. Wire `requiredResearch` fields on recipes that should require prior discovery.

---

### Items & Recipes

- [ ] **OccultNecklace is an empty class** — only has passive effect in `CommonSetup`. Give it a proper class-level identity and consider expanding the effect.
- [ ] **Missing recipes** — the following have no crafting recipe defined:
  - Soul Magnet
  - Occult Necklace
  - Ancient Tablet
  - Soul Steel Wings
  - Soul Steel Ingot (intermediate — how does the player first make it?)
- [ ] **Wand of Sparking** — functional but no cooldown, no durability cost, no mana/soul cost. Currently infinite free projectiles.
- [ ] **Scanning system** — planned visual outline system for highlighting entities/blocks (notes in old README about `LevelRenderer.initOutline` / `PostChain`). Not started.
- [ ] **Soul bottle sound** — missing sound feedback when a soul bottle is full and can't accept more souls.

---

### World & Structure

- [ ] **Crypt has no loot or spawns** — structure exists (NBT files) but no chest loot tables and no mob spawners are wired up.
- [ ] **Nergal not tied to crypt** — the dark tomb structure and crypt structure exist but there's no gameplay hook connecting them to Nergal's spawn or the Scroll of Shadows.

---

### Polish

- [ ] **Hardcoded English in Necronomicon whispers** — all `Component.literal("...")` strings in `RitualAltarBlockEntity` and `Necronomicon` should be `Component.translatable(...)` keys.
- [ ] **`playerClone` event commented out** — research data persistence on death is disabled. Verify whether `DataAttachmentRegistry` handles this automatically or re-enable the handler.
- [ ] **`serverChatEvent` in `CommonSetup` is empty** — remove or implement.
- [ ] **`getItemLookingAt` helper is unused** — dead code in `CommonSetup`, remove or wire up.
- [ ] **Debug item in creative tab** — `DEBUG_RESEARCH_UNLEARNER` ships in the main creative tab. Move to a dev-only condition or remove before release.

---

## Feature Completion Checklist (High Level)

- [ ] All items have recipes and are obtainable in survival without creative
- [ ] Progression is gated: player must follow the research tree to reach end-game
- [ ] Nergal boss fight is complete: summon → arena → all attacks active → meaningful drops
- [ ] Soul Steel Wings are fully functional (glide/fly/fold animation reacts to player state)
- [ ] Awakened Soul Steel tier has distinct abilities that justify crafting it
- [ ] Soul type variants are resolved (either all four implemented or simplified to one)
- [ ] Necronomicon has readable content for every major mechanic
- [ ] Crypt structure has loot, mob spawns, and connects to boss progression
- [ ] All hardcoded strings replaced with translation keys
- [ ] No debug/placeholder content ships in release build
