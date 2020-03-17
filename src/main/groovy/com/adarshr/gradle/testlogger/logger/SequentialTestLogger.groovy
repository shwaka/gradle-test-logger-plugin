package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

@InheritConstructors
@CompileStatic
class SequentialTestLogger extends TestLoggerAdapter {

    private List<TestDescriptorWrapper> unloggedDescriptors = []
//    private boolean testLogged

    @Override
    protected void beforeAllSuites(TestDescriptorWrapper descriptor) {
        logger.logNewLine()
    }

    @Override
    void beforeSuite(TestDescriptorWrapper suite) {
//        unloggedDescriptors << suite
//        if (unloggedDescriptors.size() == 1) {
//            logger.logNewLine()
//        }
//        if (testLogged) {
//            logger.logNewLine()
//            testLogged = false
//        }

        logger.log theme.suiteText(suite)


    }

    @Override
    void afterSuite(TestDescriptorWrapper suite, TestResultWrapper result) {
//        if (testLogged) {
//            logger.logNewLine()
//            testLogged = false
//        }
        logger.log theme.suiteStandardStreamText(outputCollector.removeSuiteOutput(suite), result)
//        unloggedDescriptors.remove(suite)
    }

    @Override
    void afterAllSuites(TestDescriptorWrapper suite, TestResultWrapper result) {
        logger.logNewLine()
        logger.log theme.summaryText(suite, result)
    }

    @Override
    void afterTest(TestDescriptorWrapper descriptor, TestResultWrapper result) {
//        logger.log '[up]'
        logger.log theme.testText(descriptor, result)
    }
//    @Override
//    void afterTest(TestDescriptorWrapper descriptor, TestResultWrapper result) {
//        if (unloggedDescriptors.empty) {
//            return
//        }
//
//        unloggedDescriptors.remove(descriptor)
//
//        logSuite(descriptor, result)
//
//        testLogged = true
//
//        logger.log theme.testText(descriptor, result)
//        logger.log theme.testStandardStreamText(outputCollector.removeTestOutput(descriptor), result)
//    }
//
//    void logSuite(TestDescriptorWrapper suite, TestResultWrapper result) {
//        if (suite.parentValid && !wasSuiteLogged(suite.parent)) {
//            logSuite(suite.parent, result)
//        }
//        if (wasSuiteLogged(suite)) {
//            return
//        }
//
//        def suiteText = suite.composite ? theme.suiteText(suite, result) : theme.testText(suite, result)
//
//        if (suiteText) {
//            logger.log theme.suiteStandardStreamText(outputCollector.removeSuiteOutput(suite), result)
////            if (!suite.composite) {
////                logger.logNewLine()
////            }
//            logger.log suiteText
//        }
//
//        unloggedDescriptors.remove(suite)
//    }

    private boolean wasSuiteLogged(TestDescriptorWrapper suite) {
        !unloggedDescriptors.contains(suite)
    }

    @Override
    protected void beforeTest(TestDescriptorWrapper descriptor) {
        logger.log theme.testText(descriptor)

//        unloggedDescriptors << descriptor
    }
}
