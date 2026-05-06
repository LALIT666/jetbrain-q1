package com.example.uitest

import com.intellij.driver.sdk.ui.components.UiComponent
import com.intellij.driver.sdk.ui.components.UiComponent.Companion.waitFound
import com.intellij.driver.sdk.ui.components.common.ideFrame
import com.intellij.ide.starter.driver.engine.runIdeWithDriver
import com.intellij.ide.starter.ide.IdeProductProvider
import com.intellij.ide.starter.junit5.hyphenateWithClass
import com.intellij.ide.starter.models.TestCase
import com.intellij.ide.starter.project.GitHubProject
import com.intellij.ide.starter.runner.CurrentTestMethod
import com.intellij.ide.starter.runner.Starter
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.awt.event.KeyEvent
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class ChangelistsSettingsTest {

    @Test
    fun testCreateChangelistsAutomaticallyCheckbox() {
        val testContext = Starter.newContext(
            CurrentTestMethod.hyphenateWithClass(),
            TestCase(
                IdeProductProvider.IC,
                GitHubProject.fromGithub(
                    branchName = "master",
                    repoRelativeUrl = "JetBrains/kotlin-examples.git"
                )
            ).useRelease()
        ).prepareProjectCleanImport()

        testContext.runIdeWithDriver().useDriverAndCloseIde {
            ideFrame {
                waitForIndicators(5.minutes)

                val isMac = System.getProperty("os.name").lowercase().contains("mac")
                keyboard {
                    if (isMac) {
                        hotKey(KeyEvent.VK_META, KeyEvent.VK_COMMA)
                    } else {
                        hotKey(KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_S)
                    }
                }

                Thread.sleep(2000)

                x("//div[@text='Version Control']", UiComponent::class.java)
                    .waitFound(30.seconds)
                    .click()

                Thread.sleep(1000)

                x("//div[@text='Changelists']", UiComponent::class.java)
                    .waitFound(20.seconds)
                    .click()

                Thread.sleep(1500)

                val checkboxXpath =
                    "//div[@class='JCheckBox' and contains(@text, 'Create changelists automatically')]"

                val selectedCheckboxXpath =
                    "//div[@class='JCheckBox' and contains(@text, 'Create changelists automatically') and @selected='true']"

                val checkbox = x(checkboxXpath, UiComponent::class.java)
                    .waitFound(20.seconds)

                val alreadySelected = try {
                    x(selectedCheckboxXpath, UiComponent::class.java)
                        .waitFound(2.seconds)
                    true
                } catch (_: Throwable) {
                    false
                }

                if (!alreadySelected) {
                    checkbox.click()
                    Thread.sleep(500)
                }

                val finalSelected = try {
                    x(selectedCheckboxXpath, UiComponent::class.java)
                        .waitFound(10.seconds)
                    true
                } catch (_: Throwable) {
                    false
                }

                assertTrue(finalSelected) {
                    "'Create changelists automatically' checkbox should be selected"
                }

                x("//div[@class='JButton' and @text='OK']", UiComponent::class.java)
                    .waitFound(10.seconds)
                    .click()

                Thread.sleep(1000)
            }
        }
    }
}