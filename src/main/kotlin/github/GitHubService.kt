package github

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface GitHubService {
    @GET("/repos/{owner}/{repo}/actions/artifacts")
    suspend fun getArtifacts(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
    ): ArtifactsResponse

    @GET("/repos/{owner}/{repo}/actions/artifacts/{artifactId}/{archiveFormat}")
    @Streaming
    suspend fun downloadArtifact(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("artifactId") artifactId: Long,
        @Path("archiveFormat") archiveFormat: String = "zip",
    ): ResponseBody
}
