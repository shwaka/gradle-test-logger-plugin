package com.adarshr.test

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import kotlin.time.Duration
import kotlin.time.ExperimentalTime


@ExperimentalTime
class WordSpecTest : WordSpec() {

    init {
        "a context" should {
            "have a test" {
                2.shouldBeGreaterThan(1)
            }
            "have another test" {
                2.shouldBeGreaterThan(1)
            }
            "have a test with config".config(enabled = false) {

            }
        }

        "another context" When {

            "using when" Should {
                "have a test" {
                    2.shouldBeGreaterThan(1)
                }
                "have a test with config".config(timeout = Duration.milliseconds(10000)) {
                    2.shouldBeGreaterThan(1)
                }
            }

        }
    }
}
