package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.example.ui.screens.RegisterScreen
import com.example.ui.theme.AntsoirayTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class RegisterScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testRegisterScreenDisplaysComponents() {
        var continued = false
        var currentPhone = "+261 34 00 000 01"

        composeTestRule.setContent {
            AntsoirayTheme {
                RegisterScreen(
                    currentPhone = currentPhone,
                    currentCountryCode = "+261",
                    onPhoneChange = { currentPhone = it },
                    onCountryCodeChange = {},
                    onContinue = { continued = true },
                    onQuickDemoLogin = {}
                )
            }
        }

        // Verify country code selector exists and is displayed
        composeTestRule.onNodeWithTag("country_code_selector").assertIsDisplayed()

        // Verify phone input field exists and is displayed
        composeTestRule.onNodeWithTag("register_phone_input").assertIsDisplayed()

        // Verify continue button exists and is displayed
        composeTestRule.onNodeWithTag("register_continue_button").performScrollTo().assertIsDisplayed()

        // Verify fill demo number button exists and is displayed
        composeTestRule.onNodeWithTag("fill_demo_number_button").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun testRegisterScreenFillDemoNumberAndContinue() {
        var continued = false
        var updatedPhone = ""

        composeTestRule.setContent {
            AntsoirayTheme {
                RegisterScreen(
                    currentPhone = "",
                    currentCountryCode = "+261",
                    onPhoneChange = { updatedPhone = it },
                    onCountryCodeChange = {},
                    onContinue = { continued = true },
                    onQuickDemoLogin = {}
                )
            }
        }

        // Scroll and tap fill demo number
        composeTestRule.onNodeWithTag("fill_demo_number_button").performScrollTo().performClick()

        // Verify updated phone matches Madagascar demo number
        assertTrue(updatedPhone.contains("+261"))
    }
}
