package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chess.ui.screens.FriendsScreen
import com.example.chess.ui.screens.GamePlayScreen
import com.example.chess.ui.screens.HomeScreen
import com.example.chess.ui.screens.LeaderboardScreen
import com.example.chess.ui.screens.ProfileScreen
import com.example.chess.viewmodel.ActiveScreen
import com.example.chess.viewmodel.ChessViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: ChessViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: ChessViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101118)),
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            if (currentScreen != ActiveScreen.GAME) {
                NavigationBar(
                    containerColor = Color(0xFF131520),
                    tonalElevation = 0.dp,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .background(Color(0xFF131520))
                ) {
                    val navItems = listOf(
                        Triple(ActiveScreen.HOME, "Play", Pair(Icons.Filled.SportsEsports, Icons.Outlined.SportsEsports)),
                        Triple(ActiveScreen.LEADERBOARD, "Ranking", Pair(Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents)),
                        Triple(ActiveScreen.FRIENDS, "Friends", Pair(Icons.Filled.Group, Icons.Outlined.Group)),
                        Triple(ActiveScreen.PROFILE, "Profile", Pair(Icons.Filled.Person, Icons.Outlined.Person))
                    )

                    navItems.forEach { (screen, title, icons) ->
                        val isSelected = currentScreen == screen
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.navigateTo(screen) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) icons.first else icons.second,
                                    contentDescription = title,
                                    tint = if (isSelected) Color(0xFFFFD700) else Color(0xFF94A3B8)
                                )
                            },
                            label = {
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color(0xFFFFD700) else Color(0xFF94A3B8)
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color(0xFFFFD700).copy(alpha = 0.18f),
                                selectedIconColor = Color(0xFFFFD700),
                                unselectedIconColor = Color(0xFF94A3B8),
                                selectedTextColor = Color(0xFFFFD700),
                                unselectedTextColor = Color(0xFF94A3B8)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF101118))
        ) {
            Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
                when (screen) {
                    ActiveScreen.HOME -> HomeScreen(viewModel = viewModel)
                    ActiveScreen.GAME -> GamePlayScreen(viewModel = viewModel)
                    ActiveScreen.LEADERBOARD -> LeaderboardScreen(viewModel = viewModel)
                    ActiveScreen.FRIENDS -> FriendsScreen(viewModel = viewModel)
                    ActiveScreen.PROFILE -> ProfileScreen(viewModel = viewModel)
                }
            }
        }
    }
}
