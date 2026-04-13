package com.subin.cleanbookstore.presentation.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.subin.cleanbookstore.domain.model.Book
import com.subin.cleanbookstore.presentation.components.BookItem

@Composable
fun SearchScreen(
    viewModel: BookSearchViewModel,
    onBookClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var searchQuery by rememberSaveable { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current

    SearchContent(
        uiState = uiState,
        searchQuery = searchQuery,
        onQueryChange = { searchQuery = it },
        onSearch = {
            if (searchQuery.isNotBlank()) {
                viewModel.searchBooks(searchQuery)
                keyboardController?.hide()
            }
        },
        onBookClick = onBookClick
    )
}

@Composable
private fun SearchContent(
    uiState: BookSearchUiState,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onBookClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            label = { Text("도서 검색") },
            placeholder = { Text("제목을 입력해 보세요") },
            trailingIcon = {
                IconButton(onClick = onSearch) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "검색 아이콘")
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() })
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when (uiState) {
                is BookSearchUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is BookSearchUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(
                            items = uiState.books,
                            key = { it.id }
                        ) { book ->
                            BookItem(
                                book = book,
                                onClick = { onBookClick(book.id) },
                                onLikeClick = { /* TODO: 이슈 #12 좋아요 구현 */ }
                            )
                        }
                    }
                }
                is BookSearchUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is BookSearchUiState.Empty -> {
                    Text(
                        text = "검색 결과가 없습니다.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "검색 결과 있음")
@Composable
fun SearchScreenSuccessPreview() {
    val mockBooks = listOf(
        Book(
            id = "1",
            title = "클린 아키텍처",
            subtitle = "소프트웨어 구조와 설계의 원칙",
            authors = listOf("로버트 C. 마틴"),
            publisher = "인사이트",
            description = "",
            imageUrl = "",
            buyLink = ""
        ),
        Book(
            id = "2",
            title = "Kotlin in Action",
            subtitle = "코틀린 핵심 원리",
            authors = listOf("드미트리 제메로프"),
            publisher = "에이콘",
            description = "",
            imageUrl = "",
            buyLink = ""
        )
    )

    MaterialTheme {
        Surface {
            SearchContent(
                uiState = BookSearchUiState.Success(mockBooks),
                searchQuery = "Android",
                onQueryChange = {},
                onSearch = {},
                onBookClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "로딩 중")
@Composable
fun SearchScreenLoadingPreview() {
    MaterialTheme {
        Surface {
            SearchContent(
                uiState = BookSearchUiState.Loading,
                searchQuery = "Android",
                onQueryChange = {},
                onSearch = {},
                onBookClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "검색 결과 없음")
@Composable
fun SearchScreenEmptyPreview() {
    MaterialTheme {
        Surface {
            SearchContent(
                uiState = BookSearchUiState.Empty,
                searchQuery = "이상한 검색어",
                onQueryChange = {},
                onSearch = {},
                onBookClick = {}
            )
        }
    }
}