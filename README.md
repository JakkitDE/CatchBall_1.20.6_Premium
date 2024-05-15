
# FangBall

A simple plugin to able users to catch and release mobs using single- or multiuseable fangballs. Custom NBT data are not supported. Only PaperMC is supported. No support for Spigot, Bukkit or any other.
!! Paper >= 1.20.6, Java >= 21 !!




## Commands
1) Command `/fangball give <player> <multi|single> <amount>`
 Permission `fangball.admin.command.give`


## Permissions
1) Permission `fangball.use`
 Description `Allows a player to use a fangball`

2) Permission `fangball.use.world.bypass`
 Description `Allows a player bypassing world whitelist`

3) Permission `fangball.use.bypass.insideisland`
 Description `Allows a player bypassing the rule not to be allowed using fangballs outside an island`
 
## Hooks
### SuperiorSkyblock2
This plugin supports hooking into SuperiorSkyblock2 in order to depend a users action on configured IslandPrivileges. To do so, follow the instruction:

1) Go to
`/plugins/FangBall/settings.yml` 
and set `enabled` of `SuperiorSkyBlock2`to `true`.

2) Go to
`/plugins/SuperiorSkyBlock2/menus/permissions.yml` and add the `fangball` permission like following:

```
  fangball:
    display-menu: true
    permission-enabled:
      type: MAGMA_CREAM
      name: '&6Fangball Use'
      lore:
        - '&7Access to use fangballs on island.'
        - '&7Currently &aENABLED&7.'
    permission-disabled:
      name: '&6Fangball Use'
      type: MAGMA_CREAM
      lore:
        - '&7Access to use fangballs on island.'
        - '&7Currently &cDISABLED&7.'
    role-permission:
      name: '&6Fangball Use'
      type: MAGMA_CREAM
      lore:
        - '&7Access to use fangballs on island.'
        - '&7Role: &e{}&7.'
        - ''
        - '{0}'
    has-access:
      sound:
        type: ENTITY_EXPERIENCE_ORB_PICKUP
        volume: 0.2
        pitch: 0.2
    no-access:
      sound:
        type: BLOCK_ANVIL_PLACE
        volume: 0.2
        pitch: 0.2
```
