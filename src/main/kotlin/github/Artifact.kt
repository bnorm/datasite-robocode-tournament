package github

import kotlinx.serialization.Serializable

@Serializable
data class Artifact(
    val id: Long,
    val node_id: String,
    val name: String,
    val size_in_bytes: Long,
    val url: String,
    val archive_download_url: String,
    val expired: Boolean,
    val created_at: String,
    val expires_at: String,
    val updated_at: String,
)
