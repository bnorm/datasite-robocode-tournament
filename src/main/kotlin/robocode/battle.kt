package robocode

import robocode.control.BattleSpecification
import robocode.control.BattlefieldSpecification
import robocode.control.RobocodeEngine
import robocode.control.RobotSpecification
import robocode.control.events.BattleCompletedEvent

fun RobocodeEngine.runBattle(
    rounds: Int = 35,
    battleField: BattlefieldSpecification = BattlefieldSpecification(800, 600),
    robots: List<RobotSpecification>,
): BattleResult {
    val spec = BattleSpecification(
        rounds,
        battleField,
        robots.toTypedArray()
    )

    val listener = MemoryBattleListener()
    addBattleListener(listener)
    runBattle(spec, true)
    removeBattleListener(listener)

    return BattleResult.from(listener.events
        .filterIsInstance<BattleCompletedEvent>().single()
        .indexedResults.toList())
}
