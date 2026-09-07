# Acceptance Test 2 - Step rotation and target configuration

## Purpose

Verify that a wheel can rotate a signed number of steps and that the machine
can reach a complete requested configuration without moving an incompatible
locked wheel.

## Initial state

- The simulator is visible.
- The ordered symbols are `red`, `blue`, `green`.
- The initial configuration is `[red, blue, green]`.

## Commands and expected results

| Step | Command | Expected result |
| ---: | --- | --- |
| 1 | `spin(1, 5)` | Wheel 1 moves five visible steps and finishes in `green`. |
| 2 | `configuration()` | The result is `[green, blue, green]`. |
| 3 | `spin(2, -1)` | Wheel 2 moves one step backwards and finishes in `red`. |
| 4 | `configuration()` | The result is `[green, red, green]`. |
| 5 | `lock(3)` | Wheel 3 is fixed in `green`. |
| 6 | `spin(new String[] {"red", "blue", "green"})` | Wheels 1 and 2 rotate; locked wheel 3 stays in `green`. |
| 7 | `configuration()` | The result is `[red, blue, green]`. |
| 8 | `ok()` | The result is `true`. |

## Invalid case and atomicity

Call:

```java
spin(new String[] {"blue", "cyan", "red"});
```

Because `cyan` is not in the symbol catalog, `ok()` must return `false` and no
wheel may change. The configuration must remain `[red, blue, green]`.

## Usability evidence

During `spin(1, 5)`, point out that the visible wheel shows every intermediate
symbol instead of jumping directly to the final result.
