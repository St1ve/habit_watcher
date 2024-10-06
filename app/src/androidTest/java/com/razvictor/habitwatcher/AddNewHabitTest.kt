package com.razvictor.habitwatcher

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test
import com.razvictor.habitwatcher.habitlist.screenTag as habitListScreenTag
import com.razvictor.habitwatcher.new_habit.screenTag as newHabitScreenTag

class AddNewHabitTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testAddNewHabitWith() {
        with(composeTestRule) {
            /* Пустой список отображается верно */
            onNodeWithTag(habitListScreenTag("container")).assertIsDisplayed()
            onNodeWithTag(habitListScreenTag("addHabitFloatingButton")).assertIsDisplayed()
            onNodeWithTag(habitListScreenTag("emptyList")).assertIsDisplayed()

            /* Перейти на новый экран */
            onNodeWithTag(habitListScreenTag("addHabitFloatingButton")).performClick()

            /* Экран с добавлением новой привычки отображается */
            onNodeWithTag(newHabitScreenTag("container")).assertIsDisplayed()
            with(onNodeWithTag(newHabitScreenTag("title_text"))) {
                assertIsDisplayed()
                assertTextEquals("Create new habit")
            }
            onNodeWithTag(newHabitScreenTag("habitName")).assertIsDisplayed()

            /* Ввести название новой привычки и нажать на `Create habit` */
            onNodeWithTag(newHabitScreenTag("habitName")).performTextInput("Test new habit")
            onNodeWithTag(newHabitScreenTag("createButton")).performClick()

            /* Новая привычка отображается на списке привычек */
            onNodeWithTag(habitListScreenTag("container")).assertIsDisplayed()
            onNodeWithTag(habitListScreenTag("emptyList")).assertIsNotDisplayed()
            onNodeWithTag(habitListScreenTag("dataList")).assertIsDisplayed()

            val newHabitCardMatcher = hasTestTag(habitListScreenTag("habitCard")) and hasAnyDescendant(
                hasText("Test new habit")
            )

            onNode(hasTestTag(habitListScreenTag("actionButton_text")) and hasAnyAncestor(newHabitCardMatcher), useUnmergedTree = true).assertTextEquals("Mark Done")
            onNode(hasTestTag(habitListScreenTag("actionButton_icon")) and hasAnyAncestor(newHabitCardMatcher), useUnmergedTree = true).assertIsNotDisplayed()

            /* Завершить привычку */
            onNode(hasTestTag(habitListScreenTag("actionButton_text")) and hasAnyAncestor(newHabitCardMatcher), useUnmergedTree = true).performClick()
            onNode(hasTestTag(habitListScreenTag("actionButton_text")) and hasAnyAncestor(newHabitCardMatcher), useUnmergedTree = true).assertTextEquals("Done")
            onNode(hasTestTag(habitListScreenTag("actionButton_icon")) and hasAnyAncestor(newHabitCardMatcher), useUnmergedTree = true).assertIsDisplayed()
        }
    }
}
