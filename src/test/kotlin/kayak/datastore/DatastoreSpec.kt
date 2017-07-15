package kayak.datastore

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig
import com.google.appengine.tools.development.testing.LocalServiceTestHelper

import io.kotlintest.TestCaseContext
import io.kotlintest.specs.StringSpec

abstract class DatastoreSpec : StringSpec() {
    val helper = makeDatastoreTestHelper()

    fun makeDatastoreTestHelper() = LocalServiceTestHelper(makeDatastoreTestConfig())

    fun makeDatastoreTestConfig() = LocalDatastoreServiceTestConfig().
            setDefaultHighRepJobPolicyRandomSeed(1L).
            setDefaultHighRepJobPolicyUnappliedJobPercentage(0.0f)

    override protected fun interceptTestCase(context: TestCaseContext, test: () -> Unit) {
        helper.setUp()
        try {
            test()
        } finally {
            helper.tearDown()
        }
    }
}
