package com.subin.cleanbookstore

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.subin.cleanbookstore.presentation.search.BookSearchViewModel
import com.subin.cleanbookstore.presentation.search.SearchScreen
import com.subin.cleanbookstore.ui.theme.CleanBookStoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CleanBookStoreTheme {
                val viewModel: BookSearchViewModel = hiltViewModel()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // 2. 검색 테스트를 위해 화면이 켜지자마자 초기 검색어 실행
                    // (이슈 #11에서 SearchBar가 생기면 이 부분은 지워질 예정입니다.)
                    LaunchedEffect(Unit) {
                        viewModel.searchBooks("android")
                    }

                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                    ) {
                        SearchScreen(
                            viewModel = viewModel,
                            onBookClick = { bookId ->
                                // 상세 페이지 이동 로직 (이슈 #13 등에서 구현)
                                Log.d("MainActivity", "클릭된 도서 ID: $bookId")
                            }
                        )
                    }
                }
            }
        }
    }
}