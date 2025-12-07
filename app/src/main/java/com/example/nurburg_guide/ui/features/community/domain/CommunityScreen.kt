package com.example.nurburg_guide.ui.features.community

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.composed
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.nurburg_guide.ui.features.community.domain.CommunityPost
import com.example.nurburg_guide.ui.features.community.domain.PostTag
import java.time.format.DateTimeFormatter

@Composable
fun CommunityScreen(
    modifier: Modifier = Modifier,
    viewModel: CommunityViewModel = viewModel()
) {
    val state = viewModel.uiState
    val primary = MaterialTheme.colorScheme.primary

    Scaffold(
        // 🔹 Eigene, kleinere grüne Top-Bar
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp) // kleiner als der Standard-AppBar
                    .background(primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Community",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    // TODO: später Screen/Dialog für "Neuer Beitrag"
                }
            ) {
                Text("Neuer Beitrag")
            }
        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.posts.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Noch keine Beiträge – sei der erste und teile dein Ring-Setup!")
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Post der Woche (optional)
                        state.postOfWeek?.let { pow ->
                            item {
                                PostOfWeekCard(post = pow)
                            }
                        }

                        item {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Aktuelle Beiträge",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        items(state.posts, key = { it.id }) { post ->
                            PostCard(
                                post = post,
                                onLikeClicked = { viewModel.onLikeClicked(post.id) },
                                accentColor = primary
                            )
                        }

                        item { Spacer(Modifier.height(64.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun PostOfWeekCard(
    post: CommunityPost,
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colorScheme.primary

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = primary.copy(alpha = 0.08f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Post der Woche",
                    tint = primary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Post der Woche",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = post.title ?: post.content.take(80),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "von ${post.author.displayName} • ${post.likeCount} Likes",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun PostCard(
    post: CommunityPost,
    onLikeClicked: () -> Unit,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM. HH:mm")

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.large)
                        .background(accentColor.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.author.avatarInitials,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = post.author.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${post.createdAt.format(dateFormatter)} • ${tagLabel(post.tag)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            post.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
            }

            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium
            )

            // 🔹 Bild zum Post (z.B. AC-Schnitzer-M2)
            post.imageUrl?.let { imageUrl ->
                Spacer(Modifier.height(8.dp))

                AsyncImage(
                    model = imageUrl,
                    contentDescription = post.title ?: "Community Bild",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(8.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Like",
                        tint = if (post.isLikedByMe) accentColor
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .padding(top = 2.dp)
                            .clickableNoRipple { onLikeClicked() }
                    )
                    Text("${post.likeCount}")
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ModeComment,
                        contentDescription = "Kommentare",
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .padding(top = 2.dp)
                    )
                    Text("${post.commentCount}")
                }
            }
        }
    }
}

private fun tagLabel(tag: PostTag): String =
    when (tag) {
        PostTag.TOURISTENFAHRTEN -> "Touristenfahrten"
        PostTag.SETUP -> "Setup"
        PostTag.SPOTTING -> "Spotting"
        PostTag.FRAGEN -> "Fragen"
        PostTag.GENERAL -> "Allgemein"
    }

/**
 * Kleines Hilfs-Extension, damit der Like-Icon klickbar ist ohne Ripple.
 */
private fun Modifier.clickableNoRipple(onClick: () -> Unit): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}
