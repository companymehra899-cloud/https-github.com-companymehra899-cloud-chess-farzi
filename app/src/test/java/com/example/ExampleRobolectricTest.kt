package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.chess.engine.ChessEngine
import com.example.chess.model.PieceColor
import com.example.chess.model.PieceType
import com.example.chess.model.Position
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @get:org.junit.Rule
  val composeTestRule = androidx.compose.ui.test.junit4.createAndroidComposeRule<MainActivity>()

  @Test
  fun `verify full Compose UI renders without crashing`() {
    composeTestRule.waitForIdle()
    val activity = composeTestRule.activity
    val vm = androidx.lifecycle.ViewModelProvider(activity)[com.example.chess.viewmodel.ChessViewModel::class.java]
    
    activity.runOnUiThread {
      vm.navigateTo(com.example.chess.viewmodel.ActiveScreen.GAME)
    }
    composeTestRule.waitForIdle()

    activity.runOnUiThread {
      vm.navigateTo(com.example.chess.viewmodel.ActiveScreen.LEADERBOARD)
    }
    composeTestRule.waitForIdle()

    activity.runOnUiThread {
      vm.navigateTo(com.example.chess.viewmodel.ActiveScreen.FRIENDS)
    }
    composeTestRule.waitForIdle()

    activity.runOnUiThread {
      vm.navigateTo(com.example.chess.viewmodel.ActiveScreen.PROFILE)
    }
    composeTestRule.waitForIdle()

    activity.runOnUiThread {
      vm.navigateTo(com.example.chess.viewmodel.ActiveScreen.HOME)
    }
    composeTestRule.waitForIdle()
  }

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Chess Master", appName)
  }

  @Test
  fun `chess engine initial setup`() {
    val engine = ChessEngine()
    val whiteKing = engine.pieceAt(Position(7, 4))
    assertNotNull(whiteKing)
    assertEquals(PieceType.KING, whiteKing?.type)
    assertEquals(PieceColor.WHITE, whiteKing?.color)

    val blackKing = engine.pieceAt(Position(0, 4))
    assertNotNull(blackKing)
    assertEquals(PieceType.KING, blackKing?.type)
    assertEquals(PieceColor.BLACK, blackKing?.color)

    val moves = engine.getAllLegalMoves(PieceColor.WHITE)
    // In standard chess starting position, White has 20 opening moves (16 pawn moves + 4 knight moves)
    assertEquals(20, moves.size)
  }

  @Test
  fun `launch MainActivity test`() {
    androidx.test.core.app.ActivityScenario.launch(MainActivity::class.java).use { scenario ->
      scenario.onActivity { activity ->
        assertNotNull(activity)
      }
    }
  }
}
