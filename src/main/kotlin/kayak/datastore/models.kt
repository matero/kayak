package kayak.datastore

import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.EntityNotFoundException
import com.google.appengine.api.datastore.Key
import com.google.appengine.api.datastore.Query
import com.google.appengine.api.datastore.KeyFactory
import java.io.Serializable

interface Model : Serializable {
    val kind: String

    @Throws(EntityNotFoundException::class)
    fun get(key: Key): Entity {
        check(key)
        return datastore.get(key)
    }

    fun get(keys: Iterable<Key>): MutableMap<Key, Entity> {
        check(keys)
        return datastore.get(keys)
    }

    fun find(key: Key): Entity? {
        check(key)
        return try {
            datastore.get(key)
        } catch (e: EntityNotFoundException) {
            null
        }
    }

    fun delete(key: Key) {
        check(key)
        datastore.delete(key)
    }

    fun delete(vararg keys: Key) {
        check(*keys)
        datastore.delete(keys.asIterable())
    }

    fun delete(keys: Iterable<Key>) {
        check(keys)
        datastore.delete(keys)
    }

    fun isKindOf(key: Key) = kind == key.kind
    fun isKindOf(data: Entity) = kind == data.kind

    fun check(key: Key) {
        if (!isKindOf(key))
            throw IllegalArgumentException("${key} doesn't represent a ${javaClass.canonicalName}")
    }

    fun check(vararg keys: Key) {
        val illegalKeys = keys.filterNot { isKindOf(it) }
        if (illegalKeys.isNotEmpty())
            throw IllegalArgumentException("${illegalKeys} doesn't represent a ${javaClass.canonicalName}")
    }

    fun check(keys: Iterable<Key>) {
        val illegalKeys = keys.filterNot { isKindOf(it) }
        if (illegalKeys.isNotEmpty())
            throw IllegalArgumentException("${illegalKeys} doesn't represent a ${javaClass.canonicalName}")
    }

    fun findAll(): Query = Query(kind)

    fun findAll(property: Indexed<Any>): Query {
        return findAll().addProjection(property.projection)
    }

    fun findAll(a: Indexed<Any>, b: Indexed<Any>): Query {
        return findAll(a).addProjection(b.projection)
    }

    fun findAll(a: Indexed<Any>, b: Indexed<Any>, c: Indexed<Any>): Query {
        return findAll(a, b).addProjection(c.projection)
    }

    fun findAll(a: Indexed<Any>, b: Indexed<Any>, c: Indexed<Any>, vararg properties: Indexed<Any>): Query {
        val query = findAll(a, b, c)
        properties.forEach { query.addProjection(it.projection) }
        return query
    }

    fun findAll(properties: Iterable<Indexed<Any>>): Query {
        val query = findAll()
        properties.forEach { query.addProjection(it.projection) }
        return query
    }
}

abstract class RootModel(kind: String) : Model {
    override final val kind = kind
}

abstract class ChildModel(kind: String) : Model {
    abstract val parent: Parent
    override final val kind = kind
}

interface WithId : Model {
    val modelId: Id

    fun create(id: Long) = Entity(kind, id)

    fun key(id: Long) = KeyFactory.createKey(kind, id)
    fun asKeys(vararg ids: Long): List<Key> = ids.map { key(it) }.toList()
    fun asKeys(ids: Iterable<Long>): List<Key> = ids.map { key(it) }.toList()

    @Throws(EntityNotFoundException::class)

    fun getBy(id: Long) = datastore.get(key(id))
    fun getBy(vararg ids: Long) = datastore.get(asKeys(*ids))
    fun getBy(ids: Iterable<Long>) = datastore.get(asKeys(ids))

    fun findBy(id: Long): Entity? {
        return try {
            datastore.get(key(id))
        } catch (e: EntityNotFoundException) {
            null
        }
    }

    fun deleteBy(id: Long) = datastore.delete(key(id))
    fun deleteBy(vararg ids: Long) = datastore.delete(asKeys(*ids))
    fun deleteBy(ids: Iterable<Long>) = datastore.delete(asKeys(ids))
}

abstract class RootModelWithId(kind: String) : RootModel(kind), WithId

abstract class ChildModelWithId(kind: String)    : ChildModel(kind), WithId {
    fun create(parent: Key) = Entity(kind, parent)

    fun create(id: Long, parent: Key) = Entity(kind, id, parent)

    fun key(id: Long, parent: Key) = KeyFactory.createKey(parent, kind, id)
}

interface WithName: Model {
    val modelName: Name

    fun create(name: String) = Entity(kind, name)

    fun key(name: String) = KeyFactory.createKey(kind, name)
    fun asKeys(vararg names: String): List<Key> = names.map { key(it) }.toList()
    fun asKeys(names: Iterable<String>): List<Key> = names.map { key(it) }.toList()

    @Throws(EntityNotFoundException::class)
    fun getBy(name: String) = datastore.get(key(name))

    fun getBy(vararg names: String) = datastore.get(asKeys(*names))
    fun getBy(names: Iterable<String>) = datastore.get(asKeys(names))

    fun findBy(name: String): Entity? {
        return try {
            datastore.get(key(name))
        } catch (e: EntityNotFoundException) {
            null
        }
    }

    fun deleteBy(name: String) = datastore.delete(key(name))
    fun deleteBy(vararg names: String) = datastore.delete(asKeys(*names))
    fun deleteBy(names: Iterable<String>) = datastore.delete(asKeys(names))
}

abstract class RootModelWithName(kind: String) : RootModel(kind), WithName

abstract class ChildModelWithName(kind: String) : ChildModel(kind), WithName {
    fun create(name: String, parent: Key) = Entity(kind, name, parent)

    fun key(name: String, parent: Key) = KeyFactory.createKey(parent, kind, name)
}