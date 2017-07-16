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
