package kayak.datastore

import com.google.appengine.api.datastore.Blob
import com.google.appengine.api.blobstore.BlobKey
import com.google.appengine.api.datastore.Category
import com.google.appengine.api.datastore.Email
import com.google.appengine.api.datastore.EmbeddedEntity
import com.google.appengine.api.datastore.GeoPt
import com.google.appengine.api.datastore.IMHandle
import com.google.appengine.api.datastore.Key
import com.google.appengine.api.datastore.Link
import com.google.appengine.api.datastore.PhoneNumber
import com.google.appengine.api.datastore.PostalAddress
import com.google.appengine.api.datastore.PropertyContainer
import com.google.appengine.api.datastore.PropertyProjection
import com.google.appengine.api.datastore.Query
import com.google.appengine.api.datastore.Rating
import com.google.appengine.api.datastore.ShortBlob
import com.google.appengine.api.datastore.Text

import com.google.appengine.api.users.User
import java.io.Serializable
import java.util.Date

interface Property<T> : Serializable {
    val name: String
    val property: String
    val type: Class<T>

    fun read(data: PropertyContainer): T? = toModel(data.getProperty(property))

    fun toModel(value: Any?): T? = value as T?

    fun write(data: PropertyContainer, values: T?) = Unit

    fun toDatastore(value: T?): Any? = value
}

operator fun PropertyContainer.contains(propertyName: String): Boolean = hasProperty(propertyName)
operator fun <T> PropertyContainer.contains(property: Property<T>): Boolean = hasProperty(property.property)
operator fun <T> PropertyContainer.get(property: Property<T>): T? = property.read(this)
operator fun <T> PropertyContainer.set(property: Property<T>, value: T?): Unit = property.write(this, value)

interface Indexed<T> : Serializable {
    val asc: Query.SortPredicate
    val desc: Query.SortPredicate
    val isNull: Query.Filter
    val isNotNull: Query.Filter
    val projection: PropertyProjection

    infix fun EQ(value: T?): Query.Filter = equal(value)
    infix fun LT(value: T): Query.Filter = lessThan(value)
    infix fun LE(value: T): Query.Filter = lessThanOrEqual(value)
    infix fun GT(value: T): Query.Filter = greaterThan(value)
    infix fun GE(value: T): Query.Filter = greaterThanOrEqual(value)
    infix fun NE(value: T?): Query.Filter = notEqual(value)
    infix fun IN(values: Iterable<T?>): Query.Filter = memberOf(values)

    fun equal(value: T?): Query.Filter
    fun lessThan(value: T): Query.Filter
    fun lessThanOrEqual(value: T): Query.Filter
    fun greaterThan(value: T): Query.Filter
    fun greaterThanOrEqual(value: T): Query.Filter
    fun notEqual(value: T?): Query.Filter
    fun memberOf(values: Iterable<T?>): Query.Filter
}

/*
 * Scalar Properties
 */
abstract class ScalarProperty<T>(override val name: String, override val property: String, override val type: Class<T>) : Property<T> {
    fun toModel(vararg values: Any?): List<T?> = values.map { toModel(it) }.toList()
    fun toModel(values: Iterable<Any>): List<T?> = values.map { toModel(it) }.toList()
    fun toDatastore(vararg values: T?): List<Any?> = values.map { toDatastore(it) }.toList()
    fun toDatastore(values: Iterable<T?>): List<Any?> = values.map { toDatastore(it) }.toList()
}

abstract class UnindexedScalar<T>(name: String, property: String, type: Class<T>) : ScalarProperty<T>(name, property, type) {
    override fun write(data: PropertyContainer, values: T?) = data.setUnindexedProperty(property, toDatastore(values))
}

abstract class IndexedScalar<T>(name: String, property: String, type: Class<T>)
    : ScalarProperty<T>(name, property, type), Indexed<T> {
    override val asc = Query.SortPredicate(property, Query.SortDirection.ASCENDING)
    override val desc = Query.SortPredicate(property, Query.SortDirection.DESCENDING)
    override val isNull = equal(null)
    override val isNotNull = notEqual(null)
    override val projection = PropertyProjection(property, type)

    override fun equal(value: T?): Query.Filter =
            Query.FilterPredicate(property, Query.FilterOperator.EQUAL, toDatastore(value))

    override fun lessThan(value: T): Query.Filter =
            Query.FilterPredicate(property, Query.FilterOperator.LESS_THAN, toDatastore(value))

    override fun lessThanOrEqual(value: T): Query.Filter =
            Query.FilterPredicate(property, Query.FilterOperator.LESS_THAN_OR_EQUAL, toDatastore(value))

    override fun greaterThan(value: T): Query.Filter =
            Query.FilterPredicate(property, Query.FilterOperator.GREATER_THAN, toDatastore(value))

    override fun greaterThanOrEqual(value: T): Query.Filter =
            Query.FilterPredicate(property, Query.FilterOperator.GREATER_THAN_OR_EQUAL, toDatastore(value))

    override fun notEqual(value: T?): Query.Filter =
            Query.FilterPredicate(property, Query.FilterOperator.NOT_EQUAL, toDatastore(value))

    override fun memberOf(values: Iterable<T?>): Query.Filter =
            Query.FilterPredicate(property, Query.FilterOperator.EQUAL, toDatastore(values))
}

abstract class UnindexedLong(name: String, property: String = name) : UnindexedScalar<Long>(name, property, Long::class.java)

abstract class IndexedLong(name: String, property: String = name) : IndexedScalar<Long>(name, property, Long::class.java)

abstract class UnindexedDouble(name: String, property: String = name) : UnindexedScalar<Double>(name, property, Double::class.java)

abstract class IndexedDouble(name: String, property: String = name) : IndexedScalar<Double>(name, property, Double::class.java)

abstract class UnindexedBoolean(name: String, property: String = name) : UnindexedScalar<Boolean>(name, property, Boolean::class.java)

abstract class IndexedBoolean(name: String, property: String = name) : IndexedScalar<Boolean>(name, property, Boolean::class.java) {
    internal val isTrue = Query.FilterPredicate(property, Query.FilterOperator.EQUAL, true)
    internal val isFalse = Query.FilterPredicate(property, Query.FilterOperator.EQUAL, false)

    operator fun not(): Query.Filter = isFalse

    infix fun and(filter: Query.Filter): Query.Filter = Query.CompositeFilterOperator.and(isTrue, filter)
    infix fun or(filter: Query.Filter): Query.Filter = Query.CompositeFilterOperator.or(isTrue, filter)
}

infix fun Query.Filter.and(flag: IndexedBoolean) = Query.CompositeFilterOperator.and(flag.isTrue, this)
infix fun Query.Filter.or(flag: IndexedBoolean) = Query.CompositeFilterOperator.or(flag.isTrue, this)

abstract class UnindexedString(name: String, property: String = name) : UnindexedScalar<String>(name, property, String::class.java)

abstract class IndexedString(name: String, property: String = name) : IndexedScalar<String>(name, property, String::class.java)

abstract class UnindexedText(name: String, property: String = name) : UnindexedScalar<Text>(name, property, Text::class.java)

abstract class UnindexedShortBlob(name: String, property: String = name) : UnindexedScalar<ShortBlob>(name, property, ShortBlob::class.java)

abstract class IndexedShortBlob(name: String, property: String = name) : IndexedScalar<ShortBlob>(name, property, ShortBlob::class.java)

abstract class UnindexedBlob(name: String, property: String = name) : UnindexedScalar<Blob>(name, property, Blob::class.java)

abstract class UnindexedDate(name: String, property: String = name) : UnindexedScalar<Date>(name, property, Date::class.java)

abstract class IndexedDate(name: String, property: String = name) : IndexedScalar<Date>(name, property, Date::class.java)

abstract class UnindexedGeoPt(name: String, property: String = name) : UnindexedScalar<GeoPt>(name, property, GeoPt::class.java)

abstract class IndexedGeoPt(name: String, property: String = name) : IndexedScalar<GeoPt>(name, property, GeoPt::class.java)

abstract class UnindexedPostalAddress(name: String, property: String = name) : UnindexedScalar<PostalAddress>(name, property, PostalAddress::class.java)

abstract class IndexedPostalAddress(name: String, property: String = name) : IndexedScalar<PostalAddress>(name, property, PostalAddress::class.java)

abstract class UnindexedPhoneNumber(name: String, property: String = name) : UnindexedScalar<PhoneNumber>(name, property, PhoneNumber::class.java)

abstract class IndexedPhoneNumber(name: String, property: String = name) : IndexedScalar<PhoneNumber>(name, property, PhoneNumber::class.java)

abstract class UnindexedEmail(name: String, property: String = name) : UnindexedScalar<Email>(name, property, Email::class.java)

abstract class IndexedEmail(name: String, property: String = name) : IndexedScalar<Email>(name, property, Email::class.java)

abstract class UnindexedUser(name: String, property: String = name) : UnindexedScalar<User>(name, property, User::class.java)

abstract class IndexedUser(name: String, property: String = name) : IndexedScalar<User>(name, property, User::class.java)

abstract class UnindexedIMHandle(name: String, property: String = name) : UnindexedScalar<IMHandle>(name, property, IMHandle::class.java)

abstract class IndexedIMHandle(name: String, property: String = name) : IndexedScalar<IMHandle>(name, property, IMHandle::class.java)

abstract class UnindexedLink(name: String, property: String = name) : UnindexedScalar<Link>(name, property, Link::class.java)

abstract class IndexedLink(name: String, property: String = name) : IndexedScalar<Link>(name, property, Link::class.java)

abstract class UnindexedCategory(name: String, property: String = name) : UnindexedScalar<Category>(name, property, Category::class.java)

abstract class IndexedCategory(name: String, property: String = name) : IndexedScalar<Category>(name, property, Category::class.java)

abstract class UnindexedRating(name: String, property: String = name) : UnindexedScalar<Rating>(name, property, Rating::class.java)

abstract class IndexedRating(name: String, property: String = name) : IndexedScalar<Rating>(name, property, Rating::class.java)

abstract class UnindexedKey(name: String, property: String = name) : UnindexedScalar<Key>(name, property, Key::class.java)

abstract class IndexedKey(name: String, property: String = name) : IndexedScalar<Key>(name, property, Key::class.java)

abstract class UnindexedBlobKey(name: String, property: String = name) : UnindexedScalar<BlobKey>(name, property, BlobKey::class.java)

abstract class IndexedBlobKey(name: String, property: String = name) : IndexedScalar<BlobKey>(name, property, BlobKey::class.java)

abstract class UnindexedEmbeddedEntity(name: String, property: String = name) : UnindexedScalar<EmbeddedEntity>(name, property, EmbeddedEntity::class.java)

/*
 * List Properties
 */
abstract class ListProperty<E>(override val name: String,
                               override val property: String,
                               val elementType: Class<E>) : Property<List<E>> {
    override val type = java.util.List::class.java as Class<List<E>>

    fun elementToModel(value: Any?): E? = value as E?
    fun elementToDatastore(value: E?): Any? = value

    fun toModelList(vararg values: Any?): List<E?>? = values?.map { elementToModel(it) }?.toList()
    fun toModelList(values: Iterable<Any>?): List<E?>? = values?.map { elementToModel(it) }?.toList()
    fun toDatastoreList(vararg values: E?): List<Any?>? = values?.map { elementToDatastore(it) }?.toList()
    fun toDatastoreList(values: Iterable<E?>?): List<Any?>? = values?.map { elementToDatastore(it) }?.toList()
}

abstract class UnindexedList<E>(name: String, property: String, elementType: Class<E>)
    : ListProperty<E>(name, property, elementType) {
    override fun write(data: PropertyContainer, values: List<E>?) =
            data.setUnindexedProperty(property, toDatastoreList(values))
}

abstract class IndexedList<E>(name: String, property: String, elementType: Class<E>)
    : ListProperty<E>(name, property, elementType), Indexed<E> {
    override val asc = Query.SortPredicate(property, Query.SortDirection.ASCENDING)
    override val desc = Query.SortPredicate(property, Query.SortDirection.DESCENDING)
    override val isNull = equal(null)
    override val isNotNull = notEqual(null)
    override val projection = PropertyProjection(property, elementType)

    override fun equal(value: E?): Query.Filter =
            Query.FilterPredicate(property, Query.FilterOperator.EQUAL, elementToDatastore(value))

    override fun lessThan(value: E): Query.Filter =
            Query.FilterPredicate(property, Query.FilterOperator.LESS_THAN, elementToDatastore(value))

    override fun lessThanOrEqual(value: E): Query.Filter =
            Query.FilterPredicate(property, Query.FilterOperator.LESS_THAN_OR_EQUAL, elementToDatastore(value))

    override fun greaterThan(value: E): Query.Filter =
            Query.FilterPredicate(property, Query.FilterOperator.GREATER_THAN, elementToDatastore(value))

    override fun greaterThanOrEqual(value: E): Query.Filter =
            Query.FilterPredicate(property, Query.FilterOperator.GREATER_THAN_OR_EQUAL, elementToDatastore(value))

    override fun notEqual(value: E?): Query.Filter =
            Query.FilterPredicate(property, Query.FilterOperator.NOT_EQUAL, elementToDatastore(value))

    override fun memberOf(values: Iterable<E?>): Query.Filter =
            Query.FilterPredicate(property, Query.FilterOperator.EQUAL, toDatastoreList(values))
}

abstract class UnindexedLongList(name: String, property: String = name) : UnindexedList<Long>(name, property, Long::class.java)

abstract class IndexedLongList(name: String, property: String = name) : IndexedList<Long>(name, property, Long::class.java)

abstract class UnindexedDoubleList(name: String, property: String = name) : UnindexedList<Double>(name, property, Double::class.java)

abstract class IndexedDoubleList(name: String, property: String = name) : IndexedList<Double>(name, property, Double::class.java)

abstract class UnindexedBooleanList(name: String, property: String = name) : UnindexedList<Boolean>(name, property, Boolean::class.java)

abstract class IndexedBooleanList(name: String, property: String = name) : IndexedList<Boolean>(name, property, Boolean::class.java) {
    internal val isTrue = Query.FilterPredicate(property, Query.FilterOperator.EQUAL, true)
    internal val isFalse = Query.FilterPredicate(property, Query.FilterOperator.EQUAL, false)

    operator fun not(): Query.Filter = isFalse

    infix fun and(filter: Query.Filter) = Query.CompositeFilterOperator.and(isTrue, filter)
    infix fun or(filter: Query.Filter) = Query.CompositeFilterOperator.or(isTrue, filter)
}

infix fun Query.Filter.and(flag: IndexedBooleanList) = Query.CompositeFilterOperator.and(flag.isTrue, this)
infix fun Query.Filter.or(flag: IndexedBooleanList) = Query.CompositeFilterOperator.or(flag.isTrue, this)

abstract class UnindexedStringList(name: String, property: String = name) : UnindexedList<String>(name, property, String::class.java)

abstract class IndexedStringList(name: String, property: String = name) : IndexedList<String>(name, property, String::class.java)

abstract class UnindexedTextList(name: String, property: String = name) : UnindexedList<Text>(name, property, Text::class.java)

abstract class UnindexedShortBlobList(name: String, property: String = name) : UnindexedList<ShortBlob>(name, property, ShortBlob::class.java)

abstract class IndexedShortBlobList(name: String, property: String = name) : IndexedList<ShortBlob>(name, property, ShortBlob::class.java)

abstract class UnindexedBlobList(name: String, property: String = name) : UnindexedList<Blob>(name, property, Blob::class.java)

abstract class UnindexedDateList(name: String, property: String = name) : UnindexedList<Date>(name, property, Date::class.java)

abstract class IndexedDateList(name: String, property: String = name) : IndexedList<Date>(name, property, Date::class.java)

abstract class UnindexedGeoPtList(name: String, property: String = name) : UnindexedList<GeoPt>(name, property, GeoPt::class.java)

abstract class IndexedGeoPtList(name: String, property: String = name) : IndexedList<GeoPt>(name, property, GeoPt::class.java)

abstract class UnindexedPostalAddressList(name: String, property: String = name) : UnindexedList<PostalAddress>(name, property, PostalAddress::class.java)

abstract class IndexedPostalAddressList(name: String, property: String = name) : IndexedList<PostalAddress>(name, property, PostalAddress::class.java)

abstract class UnindexedPhoneNumberList(name: String, property: String = name) : UnindexedList<PhoneNumber>(name, property, PhoneNumber::class.java)

abstract class IndexedPhoneNumberList(name: String, property: String = name) : IndexedList<PhoneNumber>(name, property, PhoneNumber::class.java)

abstract class UnindexedEmailList(name: String, property: String = name) : UnindexedList<Email>(name, property, Email::class.java)

abstract class IndexedEmailList(name: String, property: String = name) : IndexedList<Email>(name, property, Email::class.java)

abstract class UnindexedUserList(name: String, property: String = name) : UnindexedList<User>(name, property, User::class.java)

abstract class IndexedUserList(name: String, property: String = name) : IndexedList<User>(name, property, User::class.java)

abstract class UnindexedIMHandleList(name: String, property: String = name) : UnindexedList<IMHandle>(name, property, IMHandle::class.java)

abstract class IndexedIMHandleList(name: String, property: String = name) : IndexedList<IMHandle>(name, property, IMHandle::class.java)

abstract class UnindexedLinkList(name: String, property: String = name) : UnindexedList<Link>(name, property, Link::class.java)

abstract class IndexedLinkList(name: String, property: String = name) : IndexedList<Link>(name, property, Link::class.java)

abstract class UnindexedCategoryList(name: String, property: String = name) : UnindexedList<Category>(name, property, Category::class.java)

abstract class IndexedCategoryList(name: String, property: String = name) : IndexedList<Category>(name, property, Category::class.java)

abstract class UnindexedRatingList(name: String, property: String = name) : UnindexedList<Rating>(name, property, Rating::class.java)

abstract class IndexedRatingList(name: String, property: String = name) : IndexedList<Rating>(name, property, Rating::class.java)

abstract class UnindexedKeyList(name: String, property: String = name) : UnindexedList<Key>(name, property, Key::class.java)

abstract class IndexedKeyList(name: String, property: String = name) : IndexedList<Key>(name, property, Key::class.java)

abstract class UnindexedBlobKeyList(name: String, property: String = name) : UnindexedList<BlobKey>(name, property, BlobKey::class.java)

abstract class IndexedBlobKeyList(name: String, property: String = name) : IndexedList<BlobKey>(name, property, BlobKey::class.java)

abstract class UnindexedEmbeddedEntityList(name: String, property: String = name) : UnindexedList<EmbeddedEntity>(name, property, EmbeddedEntity::class.java)
