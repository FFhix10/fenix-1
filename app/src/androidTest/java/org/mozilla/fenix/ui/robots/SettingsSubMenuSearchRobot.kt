/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

@file:Suppress("TooManyFunctions")

package org.mozilla.fenix.ui.robots

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.allOf
import org.junit.Assert.assertTrue
import org.mozilla.fenix.R
import org.mozilla.fenix.helpers.TestAssetHelper.waitingTime
import org.mozilla.fenix.helpers.click

/**
 * Implementation of Robot Pattern for the settings search sub menu.
 */
class SettingsSubMenuSearchRobot {
    fun verifyDefaultSearchEngineHeader() = assertDefaultSearchEngineHeader()
    fun verifySearchEngineList() = assertSearchEngineList()
    fun verifyShowSearchSuggestions() = assertShowSearchSuggestions()
    fun verifyShowSearchShortcuts() = assertShowSearchShortcuts()
    fun verifyShowClipboardSuggestions() = assertShowClipboardSuggestions()
    fun verifySearchBrowsingHistory() = assertSearchBrowsingHistory()
    fun verifySearchBookmarks() = assertSearchBookmarks()

    fun changeDefaultSearchEngine(searchEngineName: String) =
        selectSearchEngine(searchEngineName)

    fun disableShowSearchSuggestions() = toggleShowSearchSuggestions()
    fun enableShowSearchShortcuts() = toggleShowSearchShortcuts()

    fun openAddSearchEngineMenu() = addSearchEngineButton().click()

    fun verifyAddSearchEngineList() = assertAddSearchEngineList()

    fun verifyEngineListContains(searchEngineName: String) = assertEngineListContains(searchEngineName)

    fun saveNewSearchEngine() {
        addSearchEngineSaveButton().click()
        assertTrue(
            mDevice.findObject(
                UiSelector().textContains("Default search engine")
            ).waitForExists(waitingTime)
        )
    }

    fun addNewSearchEngine(searchEngineName: String) {
        selectSearchEngine(searchEngineName)
        saveNewSearchEngine()
    }

    fun selectAddCustomSearchEngine() = onView(withText("Other")).click()

    fun typeCustomEngineDetails(engineName: String, engineURL: String) {
        onView(withId(R.id.edit_engine_name))
            .perform(clearText())
            .perform(typeText(engineName))
        onView(withId(R.id.edit_search_string))
            .perform(clearText())
            .perform(typeText(engineURL))
    }

    fun openEngineOverflowMenu(searchEngineName: String) {
        mDevice.findObject(
            UiSelector().resourceId("org.mozilla.fenix.debug:id/overflow_menu")
        ).waitForExists(waitingTime)
        threeDotMenu(searchEngineName).click()
    }

    fun clickEdit() = onView(withText("Edit")).click()

    fun saveEditSearchEngine() {
        onView(withId(R.id.save_button)).click()
        assertTrue(
            mDevice.findObject(
                UiSelector().textContains("Saved")
            ).waitForExists(waitingTime)
        )
    }

    class Transition {
        val mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        fun goBack(interact: SettingsRobot.() -> Unit): SettingsRobot.Transition {
            mDevice.waitForIdle()
            goBackButton().perform(click())

            SettingsRobot().interact()
            return SettingsRobot.Transition()
        }
    }
}

private fun assertDefaultSearchEngineHeader() =
    onView(withText("Default search engine"))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

private fun assertSearchEngineList() {
    onView(withText("Google"))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    onView(withText("Amazon.com"))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    onView(withText("Bing"))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    onView(withText("DuckDuckGo"))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    onView(withText("Wikipedia"))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    onView(withText("Add search engine"))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
}

private fun assertShowSearchSuggestions() {
    onView(withId(androidx.preference.R.id.recycler_view)).perform(
        RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
            hasDescendant(withText("Show search suggestions"))
        )
    )
    onView(withText("Show search suggestions"))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
}

private fun assertShowSearchShortcuts() {
    onView(withId(androidx.preference.R.id.recycler_view)).perform(
        RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
            hasDescendant(withText("Show search engines"))
        )
    )
    onView(withText("Show search engines"))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
}

private fun assertShowClipboardSuggestions() {
    onView(withId(androidx.preference.R.id.recycler_view)).perform(
        RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
            hasDescendant(withText("Show clipboard suggestions"))
        )
    )
    onView(withText("Show clipboard suggestions"))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
}

private fun assertSearchBrowsingHistory() {
    onView(withId(androidx.preference.R.id.recycler_view)).perform(
        RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
            hasDescendant(withText("Search browsing history"))
        )
    )
    onView(withText("Search browsing history"))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
}

private fun assertSearchBookmarks() {
    onView(withId(androidx.preference.R.id.recycler_view)).perform(
        RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
            hasDescendant(withText("Search bookmarks"))
        )
    )
    onView(withText("Search bookmarks"))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
}

private fun selectSearchEngine(searchEngine: String) {
    onView(withText(searchEngine))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        .perform(click())
}

private fun toggleShowSearchSuggestions() {
    onView(withId(androidx.preference.R.id.recycler_view)).perform(
        RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
            hasDescendant(withText("Show search suggestions"))
        )
    )

    onView(withText("Show search suggestions"))
        .perform(click())
}

private fun toggleShowSearchShortcuts() {
    onView(withId(androidx.preference.R.id.recycler_view)).perform(
        RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
            hasDescendant(withText("Show search engines"))
        )
    )

    onView(withText("Show search engines"))
        .perform(click())
}

private fun goBackButton() =
    onView(CoreMatchers.allOf(withContentDescription("Navigate up")))

private fun addSearchEngineButton() = onView(withText("Add search engine"))

private fun assertAddSearchEngineList() {
    onView(withText("Reddit")).check(matches(isDisplayed()))
    onView(withText("YouTube")).check(matches(isDisplayed()))
    onView(withText("Other")).check(matches(isDisplayed()))
}

private fun addSearchEngineSaveButton() = onView(withId(R.id.add_search_engine))

private fun assertEngineListContains(searchEngineName: String) {
    onView(withId(R.id.search_engine_group)).check(matches(hasDescendant(withText(searchEngineName))))
}

private fun threeDotMenu(searchEngineName: String) =
    onView(
        allOf(
            withId(R.id.overflow_menu),
            withParent(withChild(withText(searchEngineName)))
        )
    )
