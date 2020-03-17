package com.adarshr.gradle.testlogger

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.gradle.api.tasks.testing.TestDescriptor

import static com.adarshr.gradle.testlogger.util.RendererUtils.escape

@CompileStatic
class TestDescriptorWrapper {

    @Delegate
    private final TestDescriptor testDescriptor
    private final TestLoggerExtension testLoggerExtension
    final boolean firstTest

    TestDescriptorWrapper(TestDescriptor testDescriptor, TestLoggerExtension testLoggerExtension, boolean firstTest) {
        this.testDescriptor = testDescriptor
        this.testLoggerExtension = testLoggerExtension
        this.firstTest = firstTest
    }

    TestDescriptorWrapper getParent() {
        new TestDescriptorWrapper(testDescriptor.parent, testLoggerExtension, false)
    }

    String getClassName() {
        escape(testDescriptor.className)
    }

    @CompileDynamic
    String getClassDisplayName() {
        def className = testDescriptor.className
        def classDisplayName = testDescriptor.properties.classDisplayName as String
        def useClassDisplayName = classDisplayName && classDisplayName != className && !className.endsWith(classDisplayName)

        if (testLoggerExtension.showSimpleNames) {
            className = className.substring(className.lastIndexOf('.') + 1)
            className = className.substring(className.lastIndexOf('$') + 1)
        }

        escape(useClassDisplayName ? classDisplayName : className)
    }

    @CompileDynamic
    String getDisplayName() {
        escape(testDescriptor.properties.displayName ?: testDescriptor.name as String)
    }

    String getSuiteKey() {
        "${testDescriptor.className}:${testDescriptor.className}"
    }

    String getTestKey() {
        "${testDescriptor.className}:${testDescriptor.name}"
    }

    boolean isValid() {
        testDescriptor.className
    }

    boolean isParentValid() {
        testDescriptor.parent && testDescriptor.parent.className
    }

    int getLevel() {
        def level = 0
        def descriptor = !this.composite ? this.parent : this

        while (descriptor.parentValid) {
            descriptor = descriptor.parent
            level++
        }

        level
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        TestDescriptorWrapper that = (TestDescriptorWrapper) o

        if (composite != that.composite) return false
        if (className != that.className) return false
        if (name != that.name) return false

        true
    }

    int hashCode() {
        int result
        result = name.hashCode()
        result = 31 * result + className.hashCode()
        result = 31 * result + (composite ? 1 : 0)

        result
    }
}
