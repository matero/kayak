package kayak.datastore

import com.google.appengine.api.datastore.DatastoreService
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.FetchOptions.Builder.withDefaults
import com.google.appengine.api.datastore.Query
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldNotBe

class ShortcutsSpec : DatastoreSpec() {
    init {
        "datastore should be accessible" {
            datastore shouldNotBe null
            datastore should beInstanceOf(DatastoreService::class)
        }

        "entity properties should be redeable as array properties" {
            val data = Entity("test")
            data.setUnindexedProperty("int", 1)
            data.setIndexedProperty("string", "value")

            data["int"] shouldBe 1
            data["string"] shouldBe "value"
        }

        "entity properties should be writable as array properties" {
            val data = Entity("test")
            data["int"] = 1
            data["string"] = "value"

            data.getProperty("int") shouldBe 1
            data.getProperty("string") shouldBe "value"
        }

        "entities can be persisted" {
            val p1 = Entity("people")
            p1["name"] = "John Doe"
            p1.save()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 1

            val p2 = Entity("people")
            p2["name"] = "Jane Doe"
            p2.save()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 2

            val p3 = Entity("people")
            p3["name"] = "Joselito"
            p3.save()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 3

            val p4 = Entity("people", p2.key)
            p4["name"] = "cleta"
            p4.save()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 4
        }

        "entities can be deleted" {
            val p1 = Entity("people")
            p1["name"] = "John Doe"
            p1.save()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 1

            val p2 = Entity("people")
            p2["name"] = "Jane Doe"
            p2.save()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 2

            val p3 = Entity("people")
            p3["name"] = "Joselito"
            p3.save()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 3

            val p4 = Entity("people", p2.key)
            p4["name"] = "cleta"
            p4.save()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 4

            p4.delete()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 3
            p3.delete()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 2
            p2.delete()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 1
            p1.delete()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 0
        }

        "entities can be deleted using keys" {
            val p1 = Entity("people")
            p1["name"] = "John Doe"
            val k1 = p1.save()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 1

            val p2 = Entity("people")
            p2["name"] = "Jane Doe"
            val k2 = p2.save()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 2

            val p3 = Entity("people")
            p3["name"] = "Joselito"
            val k3 = p3.save()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 3

            val p4 = Entity("people", p2.key)
            p4["name"] = "cleta"
            val k4= p4.save()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 4

            k4.delete()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 3
            k3.delete()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 2
            k2.delete()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 1
            k1.delete()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 0
        }

        "entities can be fetched from keys" {
            val p1 = Entity("people")
            p1["name"] = "John Doe"
            val k1 = p1.save()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 1

            val p2 = Entity("people")
            p2["name"] = "Jane Doe"
            val k2 = p2.save()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 2

            val p3 = Entity("people")
            p3["name"] = "Joselito"
            val k3 = p3.save()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 3

            val p4 = Entity("people", p2.key)
            p4["name"] = "cleta"
            val k4= p4.save()
            datastore.prepare(Query("people")).countEntities(withDefaults()) shouldBe 4

            k1.get() shouldBe p1
            k2.get() shouldBe p2
            k3.get() shouldBe p3
            k4.get() shouldBe p4
        }
    }
}
