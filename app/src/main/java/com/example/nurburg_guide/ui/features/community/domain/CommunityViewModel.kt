package com.example.nurburg_guide.ui.features.community

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nurburg_guide.ui.features.community.domain.CommunityPost
import com.example.nurburg_guide.ui.features.community.domain.CommunityRepository
import com.example.nurburg_guide.ui.features.community.domain.InMemoryCommunityRepository
import com.example.nurburg_guide.ui.features.community.domain.PostCategory
import com.example.nurburg_guide.ui.features.community.domain.PostType
import com.example.nurburg_guide.ui.features.community.domain.UserProfile
import java.time.LocalDateTime
import kotlinx.coroutines.launch

data class CommunityUiState(
    val isLoading: Boolean = true,

    // Liste ALLER Posts (ungefiltert)
    val allPosts: List<CommunityPost> = emptyList(),

    // aktuell angezeigte (gefilterte) Posts
    val posts: List<CommunityPost> = emptyList(),

    val postOfWeek: CommunityPost? = null,

    // aktive Filter
    val activeTypes: Set<PostType> = emptySet(),
    val activeCategories: Set<PostCategory> = emptySet()
) {
    val hasActiveFilters: Boolean
        get() = activeTypes.isNotEmpty() || activeCategories.isNotEmpty()
}

class CommunityViewModel(
    private val repository: CommunityRepository = InMemoryCommunityRepository()
) : ViewModel() {

    var uiState by mutableStateOf(CommunityUiState())
        private set

    // Dummy-User für neue Posts (kannst du später mit echtem Profil ersetzen)
    private val currentUser = UserProfile(
        id = "me",
        displayName = "Du",
        avatarInitials = "DU"
    )

    init {
        loadFeed()
    }

    private fun loadFeed() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            val posts = repository.getFeed()
            val postOfWeek = posts.maxByOrNull { it.likeCount }

            val filtered = applyFilters(
                posts = posts,
                types = uiState.activeTypes,
                categories = uiState.activeCategories
            )

            uiState = uiState.copy(
                isLoading = false,
                allPosts = posts,
                posts = filtered,
                postOfWeek = postOfWeek
            )
        }
    }

    private fun applyFilters(
        posts: List<CommunityPost>,
        types: Set<PostType>,
        categories: Set<PostCategory>
    ): List<CommunityPost> {
        return posts.filter { post ->
            (types.isEmpty() || post.type in types) &&
                    (categories.isEmpty() || post.category in categories)
        }
    }

    fun toggleTypeFilter(type: PostType) {
        val currentTypes = uiState.activeTypes
        val newTypes = if (currentTypes.contains(type)) {
            currentTypes - type
        } else {
            currentTypes + type
        }

        val newPosts = applyFilters(
            posts = uiState.allPosts,
            types = newTypes,
            categories = uiState.activeCategories
        )

        uiState = uiState.copy(
            activeTypes = newTypes,
            posts = newPosts
        )
    }

    fun toggleCategoryFilter(category: PostCategory) {
        val currentCategories = uiState.activeCategories
        val newCategories = if (currentCategories.contains(category)) {
            currentCategories - category
        } else {
            currentCategories + category
        }

        val newPosts = applyFilters(
            posts = uiState.allPosts,
            types = uiState.activeTypes,
            categories = newCategories
        )

        uiState = uiState.copy(
            activeCategories = newCategories,
            posts = newPosts
        )
    }

    fun clearFilters() {
        uiState = uiState.copy(
            activeTypes = emptySet(),
            activeCategories = emptySet(),
            posts = uiState.allPosts
        )
    }

    fun onLikeClicked(postId: String) {
        val updatedAll = uiState.allPosts.map { post ->
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

        val updatedFiltered = applyFilters(
            posts = updatedAll,
            types = uiState.activeTypes,
            categories = uiState.activeCategories
        )

        val newPostOfWeek = updatedAll.maxByOrNull { it.likeCount }

        uiState = uiState.copy(
            allPosts = updatedAll,
            posts = updatedFiltered,
            postOfWeek = newPostOfWeek
        )
    }

    // 🔹 Neuer Post anlegen – inkl. Typ & Kategorie
    fun createPost(
        title: String?,
        content: String,
        type: PostType,
        category: PostCategory
    ) {
        val newPost = CommunityPost(
            id = System.currentTimeMillis().toString(),
            author = currentUser,
            createdAt = LocalDateTime.now(),
            title = title,
            content = content,
            type = type,
            category = category
        )
//.
        val updatedAll = uiState.allPosts + newPost
        val updatedFiltered = applyFilters(
            posts = updatedAll,
            types = uiState.activeTypes,
            categories = uiState.activeCategories
        )
        val newPostOfWeek = updatedAll.maxByOrNull { it.likeCount }

        uiState = uiState.copy(
            allPosts = updatedAll,
            posts = updatedFiltered,
            postOfWeek = newPostOfWeek
        )
    }
}
