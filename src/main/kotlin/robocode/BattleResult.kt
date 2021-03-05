package robocode

import com.jakewharton.picnic.TextAlignment
import com.jakewharton.picnic.table

data class BattleResult(
    val results: List<RobotResult>,
) {
    companion object {
        fun from(results: List<BattleResults>) =
            BattleResult(results.map { RobotResult.from(it) })
    }
}

fun List<BattleResult>.toTable() = table {
    cellStyle {
        alignment = TextAlignment.MiddleRight
        paddingLeft = 1
        paddingRight = 1
    }

    for ((index, battle) in this@toTable.withIndex()) {
        row {
            cell("Battle ${index + 1}") {
                borderTop = true
                alignment = TextAlignment.BottomCenter
                columnSpan = 12
            }
        }

        row {
            cellStyle {
                borderTop = true
                borderBottom = true
            }
            cell("Title") { borderRight = true }
            cell("Rank") { borderRight = true }
            cell("Score") { borderRight = true }
            cell("Survival") { borderRight = true }
            cell("Last Survivor Bonus") { borderRight = true }
            cell("Bullet Damage") { borderRight = true }
            cell("Bullet Damage Bonus") { borderRight = true }
            cell("Ram Damage") { borderRight = true }
            cell("Ram Damage Bonus") { borderRight = true }
            cell("Firsts") { borderRight = true }
            cell("Seconds") { borderRight = true }
            cell("Thirds")
        }

        for (result in battle.results) {
            row {
                cell(result.title) { borderRight = true }
                cell(result.rank) { borderRight = true }
                cell(result.score) { borderRight = true }
                cell(result.survival) { borderRight = true }
                cell(result.lastSurvivorBonus) { borderRight = true }
                cell(result.bulletDamage) { borderRight = true }
                cell(result.bulletDamageBonus) { borderRight = true }
                cell(result.ramDamage) { borderRight = true }
                cell(result.ramDamageBonus) { borderRight = true }
                cell(result.firsts) { borderRight = true }
                cell(result.seconds) { borderRight = true }
                cell(result.thirds)
            }
        }
    }
}
