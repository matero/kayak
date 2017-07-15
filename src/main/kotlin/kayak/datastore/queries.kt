package kayak.datastore

import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.FetchOptions
import com.google.appengine.api.datastore.Query
import com.google.appengine.api.datastore.QueryResultIterable
import com.google.appengine.api.datastore.QueryResultIterator
import com.google.appengine.api.datastore.QueryResultList
import com.google.appengine.api.datastore.Transaction

val fetchOptions: FetchOptions
    get() = FetchOptions.Builder.withDefaults()

fun Query.asList(withFetchOptions: FetchOptions): List<Entity> {
    return datastore.prepare(this).asList(withFetchOptions)
}

fun Query.asList(tx: Transaction, withFetchOptions: FetchOptions): List<Entity> {
    return datastore.prepare(tx, this).asList(withFetchOptions)
}

fun Query.asQueryResultList(withFetchOptions: FetchOptions = fetchOptions): QueryResultList<Entity> {
    return datastore.prepare(this).asQueryResultList(withFetchOptions)
}

fun Query.asQueryResultList(tx: Transaction, withFetchOptions: FetchOptions = fetchOptions): QueryResultList<Entity> {
    return datastore.prepare(tx, this).asQueryResultList(withFetchOptions)
}

fun Query.asIterable(withFetchOptions: FetchOptions = fetchOptions): Iterable<Entity> {
    return datastore.prepare(this).asIterable(withFetchOptions)
}

fun Query.asIterable(tx: Transaction, withFetchOptions: FetchOptions = fetchOptions): Iterable<Entity> {
    return datastore.prepare(tx, this).asIterable(withFetchOptions)
}

fun Query.asQueryResultIterable(withFetchOptions: FetchOptions = fetchOptions): QueryResultIterable<Entity> {
    return datastore.prepare(this).asQueryResultIterable(withFetchOptions)
}

fun Query.asQueryResultIterable(tx: Transaction, withFetchOptions: FetchOptions = fetchOptions): QueryResultIterable<Entity> {
    return datastore.prepare(tx, this).asQueryResultIterable(withFetchOptions)
}

fun Query.asIterator(withFetchOptions: FetchOptions = fetchOptions): Iterator<Entity> {
    return datastore.prepare(this).asIterator(withFetchOptions)
}

fun Query.asIterator(tx: Transaction, withFetchOptions: FetchOptions = fetchOptions): Iterator<Entity> {
    return datastore.prepare(tx, this).asIterator(withFetchOptions)
}

fun Query.asQueryResultIterator(withFetchOptions: FetchOptions = fetchOptions): QueryResultIterator<Entity> {
    return datastore.prepare(this).asQueryResultIterator(withFetchOptions)
}

fun Query.asQueryResultIterator(tx: Transaction, withFetchOptions: FetchOptions = fetchOptions): QueryResultIterator<Entity> {
    return datastore.prepare(tx, this).asQueryResultIterator(withFetchOptions)
}

fun Query.asSingleEntity(): Entity {
    return datastore.prepare(this).asSingleEntity()
}

fun Query.asSingleEntity(tx: Transaction): Entity {
    return datastore.prepare(tx, this).asSingleEntity()
}

fun Query.countEntities(withFetchOptions: FetchOptions = fetchOptions): Int {
    return datastore.prepare(this).countEntities(withFetchOptions)
}

fun Query.countEntities(tx: Transaction, withFetchOptions: FetchOptions = fetchOptions): Int {
    return datastore.prepare(tx, this).countEntities(withFetchOptions)
}