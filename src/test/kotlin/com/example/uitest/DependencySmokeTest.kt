package com.example.uitest

import com.intellij.ide.starter.junit5.hyphenateWithClass
import com.intellij.ide.starter.models.IdeInfo
import com.intellij.ide.starter.models.TestCase
import com.intellij.ide.starter.project.GitHubProject
import com.intellij.ide.starter.runner.CurrentTestMethod
import com.intellij.ide.starter.runner.Starter
import org.junit.jupiter.api.Test

class DependencySmokeTest {

    @Test
    fun starterDependenciesResolve() {
        println(Starter::class.qualifiedName)
        println(CurrentTestMethod::class.qualifiedName)
        println(TestCase::class.qualifiedName)
        println(IdeInfo::class.qualifiedName)
        println(GitHubProject::class.qualifiedName)
        println(CurrentTestMethod.hyphenateWithClass())
    }
}