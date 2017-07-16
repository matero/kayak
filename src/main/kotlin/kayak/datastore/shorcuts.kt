/*
MIT License

Copyright (c) 2017 Juan José Gil

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package kayak.datastore

import com.google.appengine.api.datastore.AsyncDatastoreService
import com.google.appengine.api.datastore.DatastoreService
import com.google.appengine.api.datastore.DatastoreServiceFactory
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.EntityNotFoundException
import com.google.appengine.api.datastore.Key
import com.google.appengine.api.datastore.PropertyContainer
import com.google.appengine.api.datastore.Query
import com.google.appengine.api.datastore.Transaction

import java.util.concurrent.Future

val datastore: DatastoreService
        get() = DatastoreServiceFactory.getDatastoreService()

val asyncDatastore: AsyncDatastoreService
    get() = DatastoreServiceFactory.getAsyncDatastoreService()

operator fun PropertyContainer.get(propertyName: String): Any? = getProperty(propertyName)

operator fun PropertyContainer.set(propertyName: String, value: Any?) {
    if (isUnindexedProperty(propertyName))
        setUnindexedProperty(propertyName, value)
    else
        setProperty(propertyName, value)
}

fun Entity.save(): Key = datastore.put(this)
fun Entity.delete(): Unit = datastore.delete(key)

@Throws(EntityNotFoundException::class)
fun Key.get(): Entity = datastore.get(this)
fun Key.delete(): Unit = datastore.delete(this)

fun Entity.asyncSave(): Future<Key> = asyncDatastore.put(this)
fun Entity.asyncDelete(): Future<Void> = asyncDatastore.delete(key)

@Throws(EntityNotFoundException::class)
fun Key.asyncGet(): Future<Entity> = asyncDatastore.get(this)
fun Key.asyncDelete(): Future<Void> = asyncDatastore.delete(this)

fun Entity.save(tx: Transaction): Key = datastore.put(tx, this)
fun Entity.delete(tx: Transaction): Unit = datastore.delete(tx, key)

@Throws(EntityNotFoundException::class)
fun Key.get(tx: Transaction): Entity = datastore.get(tx, this)
fun Key.delete(tx: Transaction): Unit = datastore.delete(tx, this)

fun Entity.asyncSave(tx: Transaction): Future<Key> = asyncDatastore.put(tx, this)
fun Entity.asyncDelete(tx: Transaction): Future<Void> = asyncDatastore.delete(tx, key)

@Throws(EntityNotFoundException::class)
fun Key.asyncGet(tx: Transaction): Future<Entity> = asyncDatastore.get(tx, this)
fun Key.asyncDelete(tx: Transaction): Future<Void> = asyncDatastore.delete(tx, this)

infix fun Query.Filter.and(rhs: Query.Filter) = Query.CompositeFilterOperator.and(this, rhs)
infix fun Query.Filter.or(rhs: Query.Filter) = Query.CompositeFilterOperator.or(this, rhs)