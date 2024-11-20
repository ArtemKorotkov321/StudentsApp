package utils

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class PreferenceRule(val localhost: Boolean = false) : TestRule {
    override fun apply(base: Statement, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                println("Pref rule started")
                if (localhost) putLocalhostUrl()
                base.evaluate()
                cleanPrefs()
                println("Pref rule clear")
            }

        }
    }

    fun putLocalhostUrl() {
        InstrumentationRegistry.getInstrumentation().targetContext
            .getSharedPreferences("demo_url", Context.MODE_PRIVATE)
            .edit()
            .putString("url", "http://localhost:5000")
            .commit()
    }

    fun cleanPrefs() {
        InstrumentationRegistry.getInstrumentation().targetContext
            .getSharedPreferences("meowle", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()
    }
}