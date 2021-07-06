package kayak.json

import java.math.BigDecimal
import java.math.BigInteger

interface AsJson {
  fun asJson(): Json
}

fun Iterator<Json>.asJson() = JsonArray.of(this)
fun Iterable<Json>.asJson() = JsonArray.of(this)
fun List<Json>.asJson() = JsonArray.of(this)

fun Pair<JsonString, Json>.asJson() = JsonObject.of(this)
fun Collection<Pair<JsonString, Json>>.asJson() = JsonObject.of(this)
fun Map<JsonString, Json>.asJson() = JsonObject.of(this)

fun CharSequence.asJson() = JsonString.of(this)
fun Boolean.asJson() = JsonBoolean.of(this)

fun Byte.asJson() = JsonNumber.of(this)
fun Short.asJson() = JsonNumber.of(this)
fun Int.asJson() = JsonNumber.of(this)
fun Long.asJson() = JsonNumber.of(this)
fun BigInteger.asJson() = JsonNumber.of(this)
fun Float.asJson() = JsonNumber.of(this)
fun Double.asJson() = JsonNumber.of(this)
fun BigDecimal.asJson() = JsonNumber.of(this)
