# Acceptance Test 1 - Wheel management

## Purpose

Verify that the simulator can swap two wheels and keep a locked wheel fixed
while the other wheels spin.

## Initial state

- The simulator is visible.
- The ordered symbol catalog is `red`, `blue`, `green`.
- Wheel 1 shows `red`.
- Wheel 2 shows `blue`.

## Commands and expected results

| Step | Command | Expected result |
| ---: | --- | --- |
| 1 | `swap(1, 2)` | The configuration becomes `[blue, red]`. |
| 2 | `lock(1)` | Wheel 1 becomes locked and `ok()` returns `true`. |
| 3 | `spin()` | Wheel 1 remains `blue`; wheel 2 advances from `red` to `blue`. |
| 4 | `configuration()` | The result is `[blue, blue]`. |
| 5 | `isJackpot()` | The result is `true`. |
| 6 | `unlock(1)` | Wheel 1 becomes available for rotation again. |
| 7 | `spin(1)` | Wheel 1 advances from `blue` to `green`. |

## Invalid case

Calling `swap(1, 3)` when the machine has only two wheels must leave the
configuration unchanged and make `ok()` return `false`.

## Presentation evidence

During the presentation, show the configuration after every command and point
out that the first wheel does not move while it is locked.
