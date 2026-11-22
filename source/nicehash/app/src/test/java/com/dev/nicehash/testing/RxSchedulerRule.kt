package com.dev.nicehash.testing

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Test rule for unit tests: replaces async Rx schedulers with synchronous
 * trampoline so network chains run deterministic on the testing thread.
 */
class RxSchedulerRule : TestRule {
    private val trampoline = Schedulers.trampoline()

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                RxJavaPlugins.reset()
                RxAndroidPlugins.reset()
                RxJavaPlugins.setIoSchedulerHandler { trampoline }
                RxJavaPlugins.setComputationSchedulerHandler { trampoline }
                RxJavaPlugins.setNewThreadSchedulerHandler { trampoline }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { trampoline }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}
