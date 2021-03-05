package robocode

data class RobotResult(
    val title: String,
    val rank: Int,
    val score: Double,
    val survival: Double,
    val lastSurvivorBonus: Double,
    val bulletDamage: Double,
    val bulletDamageBonus: Double,
    val ramDamage: Double,
    val ramDamageBonus: Double,
    val firsts: Int,
    val seconds: Int,
    val thirds: Int,
) {
    companion object {
        fun from(result: BattleResults) = RobotResult(
            title = result.teamLeaderName,
            rank = result.rank,
            score = result.score,
            survival = result.survival,
            lastSurvivorBonus = result.lastSurvivorBonus,
            bulletDamage = result.bulletDamage,
            bulletDamageBonus = result.bulletDamageBonus,
            ramDamage = result.ramDamage,
            ramDamageBonus = result.ramDamageBonus,
            firsts = result.firsts,
            seconds = result.seconds,
            thirds = result.thirds,
        )
    }
}
