package com.subin.cleanbookstore.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.subin.cleanbookstore.presentation.detail.DetailScreen
import com.subin.cleanbookstore.presentation.search.BookSearchViewModel
import com.subin.cleanbookstore.presentation.search.SearchScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.Search,
        modifier = modifier
    ) {
        composable<NavRoute.Search> {
            val viewModel: BookSearchViewModel = hiltViewModel()

            SearchScreen(
                viewModel = viewModel,
                onBookClick = { id ->
                    navController.navigate(NavRoute.Detail(bookId = id))
                }
            )
        }

        composable<NavRoute.Detail> { backStackEntry ->
            val detail: NavRoute.Detail = backStackEntry.toRoute()

            DetailScreen(bookId = detail.bookId, onBackClick = { navController.popBackStack() })
        }

        // 3. 즐겨찾기 화면 (나중에 구현)
        composable<NavRoute.Bookmark> {
            // BookmarkScreen()
        }
    }
}