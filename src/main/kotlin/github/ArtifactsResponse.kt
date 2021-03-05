package github

import kotlinx.serialization.Serializable

@Serializable
data class ArtifactsResponse(
    val total_count: Long,
    val artifacts: List<Artifact>,
)
