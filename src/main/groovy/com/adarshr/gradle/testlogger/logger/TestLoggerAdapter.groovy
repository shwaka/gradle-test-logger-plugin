package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.TestResultWrapper
import com.adarshr.gradle.testlogger.theme.Theme
import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestOutputEvent
import org.gradle.api.tasks.testing.TestResult

@CompileStatic
class TestLoggerAdapter implements TestLogger {

    protected final Theme theme
    protected final ConsoleLogger logger
    protected final OutputCollector outputCollector
    private final TestLoggerExtension testLoggerExtension

    private List<TestDescriptor> tests = []

    TestLoggerAdapter(Project project, TestLoggerExtension testLoggerExtension, Theme theme) {
        this.logger = new ConsoleLogger(project.logger)
        this.testLoggerExtension = testLoggerExtension
        this.theme = theme
        this.outputCollector = new OutputCollector()
    }

    boolean firstValidSuite

    @Override
    final void beforeSuite(TestDescriptor descriptor) {
        def wrappedDescriptor = wrap(descriptor, false)

        if (wrappedDescriptor.valid) {
            if (!wrappedDescriptor.parentValid) {
                beforeAllSuites(wrappedDescriptor)
            }

            if (!tests.empty) {
                logger.logNewLine()
            }


//            if (!firstValidSuite) {
//                firstValidSuite = true
//                logger.logNewLine()
//            }
            tests = []
            beforeSuite(wrappedDescriptor)
        }
    }

    protected void beforeAllSuites(TestDescriptorWrapper descriptor) {

    }

    protected void beforeSuite(TestDescriptorWrapper descriptor) {

    }

    @Override
    final void afterSuite(TestDescriptor descriptor, TestResult result) {
        def wrappedDescriptor = wrap(descriptor, false)
        def wrappedResult = wrap(result)

        if (wrappedDescriptor.valid) {
            tests.clear()
            afterSuite(wrappedDescriptor, wrappedResult)
        }

        if (!descriptor.parent) {
            afterAllSuites(wrappedDescriptor, wrappedResult)
        }
    }

    protected void afterSuite(TestDescriptorWrapper descriptor, TestResultWrapper result) {
    }

    protected void afterAllSuites(TestDescriptorWrapper descriptor, TestResultWrapper result) {
    }

    @Override
    final void beforeTest(TestDescriptor descriptor) {
        tests << descriptor
        def wrappedDescriptor = wrap(descriptor, tests.size() == 1)

//        if (wrappedDescriptor.valid) {
            beforeTest(wrappedDescriptor)
//        }
    }

    protected void beforeTest(TestDescriptorWrapper descriptor) {
    }

    @Override
    final void afterTest(TestDescriptor descriptor, TestResult result) {
        def wrappedDescriptor = wrap(descriptor, tests.size() == 1)

        if (wrappedDescriptor.valid) {
            afterTest(wrappedDescriptor, wrap(result))
        }
    }

    protected void afterTest(TestDescriptorWrapper descriptor, TestResultWrapper result) {
    }

    @Override
    void onOutput(TestDescriptor descriptor, TestOutputEvent outputEvent) {
        outputCollector.collect(wrap(descriptor, false), outputEvent.message)
    }

    private TestDescriptorWrapper wrap(TestDescriptor descriptor, boolean firstTest) {
        new TestDescriptorWrapper(descriptor, testLoggerExtension, firstTest)
    }

    private TestResultWrapper wrap(TestResult result) {
        new TestResultWrapper(result, testLoggerExtension)
    }
}
