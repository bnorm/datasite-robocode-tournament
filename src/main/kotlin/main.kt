import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import github.GitHubRepository
import robocode.BattleResult
import github.GitHubService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okio.buffer
import okio.sink
import okio.source
import retrofit2.Retrofit
import retrofit2.create
import robocode.control.RobocodeEngine
import robocode.runBattle
import robocode.toTable
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.util.zip.ZipFile

private val participants = listOf(
    GitHubRepository("bnorm", "midnight"),
    GitHubRepository("bnorm", "robocode-robot-template"),
)

suspend fun main() {
    val robocodeDirectory = Paths.get(".robocode")

    val token = System.getenv("GITHUB_TOKEN")
        ?: throw IllegalArgumentException("Requires GITHUB_TOKEN environment variable")
    val gitHubService = Retrofit.Builder()
        .client(OkHttpClient.Builder()
            .addInterceptor {
                it.proceed(it.request().newBuilder()
                    .header("Authorization", "token $token")
                    .build())
            }
            .build())
        .baseUrl("https://api.github.com/")
        .addConverterFactory(Json.asConverterFactory("application/vnd.github.v3+json".toMediaType()))
        .build()
        .create<GitHubService>()
    val robots = gitHubService.downloadRobots(robocodeDirectory.resolve("robots"), participants)

    val engine = RobocodeEngine(robocodeDirectory.toFile())
    val results = engine.roundRobin(robots)
    println(results.toTable())
}

fun RobocodeEngine.roundRobin(names: Set<String>): List<BattleResult> {
    println("Pairing robots: $names")

    val robots = names.map { getLocalRepository(it).single() }
    val pairings = robots.flatMapIndexed { i, first ->
        if (i + 1 < robots.size) robots.subList(i + 1, robots.size).map { first to it }
        else emptyList()
    }

    val results = mutableListOf<BattleResult>()

    for ((first, second) in pairings) {
        println("Running ${first.nameAndVersion} vs ${second.nameAndVersion}...")
        results.add(runBattle(rounds = 35, robots = listOf(first, second)))
    }

    return results
}

private suspend fun GitHubService.downloadRobots(
    target: Path,
    repositories: List<GitHubRepository>,
): Set<String> {
    val robotJars = mutableMapOf<GitHubRepository, List<File>>()
    for (repository in repositories) {
        robotJars[repository] = downloadRobots(repository, target)
    }

    return robotJars.values
        .mapNotNull { it.singleOrNull() }
        .map { it.nameWithoutExtension.replace("_", " ") }
        .toSet()
}

private suspend fun GitHubService.downloadRobots(repository: GitHubRepository, directory: Path): List<File> {
    val response = getArtifacts(repository.owner, repository.repo)
    val artifact = response.artifacts.maxByOrNull { it.created_at } ?: return emptyList()

    val file = File("${artifact.name}.zip")
    try {
        downloadArtifact(repository.owner, repository.repo, artifact.id).source().use { source ->
            file.sink().use { sink -> source.readAll(sink) }
        }

        val zip = ZipFile(file)
        return zip.entries().asSequence()
            .filter { !it.isDirectory && it.name.endsWith(".jar") }
            .map {
                val fileName = it.name.substringAfter('/')
                val robotJar = directory.resolve(fileName)
                zip.getInputStream(it).source().buffer().use { source ->
                    source.readAll(robotJar.sink())
                }
                robotJar.toFile()
            }
            .toList()
    } finally {
        file.delete()
    }
}
