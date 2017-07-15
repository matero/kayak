package kayak.datastore

import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.Key
import java.io.Serializable

abstract class Identifier<T>(val name: String, val model: Model) : Serializable {
    val canonicalName = "${model.javaClass.canonicalName}.${name}"

    abstract fun isDefinedAt(key: Key): Boolean

    fun of(data: Entity) = read(data)

    abstract fun read(data: Entity): T

    fun of(key: Key) = read(key)

    abstract fun read(key: Key): T
}

operator fun <T> Entity.get(identifier: Identifier<T>) = identifier.read(this)

abstract class Parent(name: String, model: Model, val parentModel: Model) : Identifier<Key>(name, model) {

    override fun isDefinedAt(key: Key) = key.parent != null && parentModel.isKindOf(key.parent)

    override fun read(data: Entity) = data.parent

    override fun read(key: Key) = key.parent
}

abstract class Id(name: String, model: WithId) : Identifier<Long>(name, model) {
    override fun isDefinedAt(key: Key) = key.id != 0L

    override fun read(data: Entity) = data.key.id

    override fun read(key: Key) = key.id
}

abstract class Name(name: String, model: WithName) : Identifier<String>(name, model) {
    override fun isDefinedAt(key: Key) = key.name != null

    override fun read(data: Entity) = data.key.name

    override fun read(key: Key) = key.name
}
