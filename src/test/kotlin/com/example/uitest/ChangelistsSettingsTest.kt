package com.example.uitest

import com.intellij.driver.client.Remote
import com.intellij.driver.sdk.ui.components.UiComponent
import com.intellij.driver.sdk.ui.components.UiComponent.Companion.waitFound
import com.intellij.driver.sdk.ui.components.common.ideFrame
import com.intellij.driver.sdk.ui.ui
import com.intellij.ide.starter.driver.engine.runIdeWithDriver
import com.intellij.ide.starter.ide.IdeProductProvider
import com.intellij.ide.starter.junit5.hyphenateWithClass
import com.intellij.ide.starter.models.TestCase
import com.intellij.ide.starter.project.GitHubProject
import com.intellij.ide.starter.runner.CurrentTestMethod
import com.intellij.ide.starter.runner.Starter
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Remote("javax.swing.AbstractButton")
interface RemoteAbstractButton {
    fun doClick()
}

class ChangelistsSettingsTest {

    @Test
    fun testCreateChangelistsAutomaticallyCheckbox() {
        val testContext = Starter.newContext(
            CurrentTestMethod.hyphenateWithClass(),
            TestCase(
                IdeProductProvider.IC,
                GitHubProject.fromGithub(
                    branchName = "main",
                    repoRelativeUrl = "JetBrains/intellij-sdk-code-samples.git"
                )
            ).useRelease()
        ).prepareProjectCleanImport()

        testContext.runIdeWithDriver().useDriverAndCloseIde {
            ideFrame {
                waitForIndicators(5.minutes)
                openSettingsDialog()
            }

            Thread.sleep(3000)

            // Settings search
            ui.keyboard {
                typeText("Create changelists automatically")
                Thread.sleep(1000)
                enter()
            }

            Thread.sleep(3000)

            val checkboxXpath =
                "//div[@class='JCheckBox' and contains(@text, 'Create changelists automatically')]"
            val selectedCheckboxXpath =
                "//div[@class='JCheckBox' and contains(@text, 'Create changelists automatically') and @selected='true']"

            val checkbox = ui.x(checkboxXpath, UiComponent::class.java)
                .waitFound(20.seconds)

            val alreadySelected = try {
                ui.x(selectedCheckboxXpath, UiComponent::class.java)
                    .waitFound(2.seconds)
                true
            } catch (_: Throwable) {
                false
            }

            if (!alreadySelected) {
                val remoteCheckbox = this.cast(checkbox.component, RemoteAbstractButton::class)
                remoteCheckbox.doClick()
                Thread.sleep(1000)
            }

            val finalSelected = try {
                ui.x(selectedCheckboxXpath, UiComponent::class.java)
                    .waitFound(10.seconds)
                true
            } catch (_: Throwable) {
                false
            }

            assertTrue(finalSelected) {
                "'Create changelists automatically' checkbox should be selected"
            }

            val okButton = ui.x("//div[@class='JButton' and @text='OK']", UiComponent::class.java)
                .waitFound(10.seconds)
            val remoteOkButton = this.cast(okButton.component, RemoteAbstractButton::class)
            remoteOkButton.doClick()

            Thread.sleep(2000)
        }
    }
}