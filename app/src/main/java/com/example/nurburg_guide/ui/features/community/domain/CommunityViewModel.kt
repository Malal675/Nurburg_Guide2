package com.example.nurburg_guide.ui.features.community

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nurburg_guide.ui.features.community.domain.CommunityPost
import com.example.nurburg_guide.ui.features.community.domain.CommunityRepository
import com.example.nurburg_guide.ui.features.community.domain.InMemoryCommunityRepository
import kotlinx.coroutines.launch

data class CommunityUiState(
    val isLoading: Boolean = true,
    val posts: List<CommunityPost> = emptyList(),
    val postOfWeek: CommunityPost? = null
)

class CommunityViewModel(
    private val repository: CommunityRepository = InMemoryCommunityRepository()
) : ViewModel() {

    var uiState by mutableStateOf(CommunityUiState())
        private set

    init {
        loadFeed()
    }

    private fun loadFeed() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            val posts = repository.getFeed()

            val postOfWeek = posts.maxByOrNull { it.likeCount }

            uiState = CommunityUiState(
                isLoading = false,
                posts = posts,
                postOfWeek = postOfWeek
            )
        }
    }
//test für git
    fun onLikeClicked(postId: String) {
        val current = uiState.posts
        val updated = current.map { post ->
            if (post.id == postId) {
                val likedNow = !post.isLikedByMe
                post.copy(
                    isLikedByMe = likedNow,
                    likeCount = post.likeCount + if (likedNow) 1 else -1
                )
            } else {
                post
            }
        }

        val newPostOfWeek = updated.maxByOrNull { it.likeCount }

        uiState = uiState.copy(
            posts = updated,
            postOfWeek = newPostOfWeek
        )
    }
}
