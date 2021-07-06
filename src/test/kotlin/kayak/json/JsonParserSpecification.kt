package kayak.json

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import java.io.StringReader

class JsonParserSpecification : ShouldSpec({
  context("creation") {
    should("reject ZERO buffer capacity") {
      val failure = shouldThrowExactly<IllegalArgumentException> {
        JsonParser.of("10", 0)
      }
      failure.message shouldBe "buffer capacity must be greater than zero"
    }
    should("reject NEGATIVE buffer capacity") {
      val failure = shouldThrowExactly<IllegalArgumentException> {
        JsonParser.of("10", -1)
      }
      failure.message shouldBe "buffer capacity must be greater than zero"
    }
    should("reject empty strings") {
      val failure = shouldThrowExactly<JsonParser.IllegalJsonSyntax> {
        JsonParser.of("", 1)
      }
      failure.message shouldBe "Location(offset=0, line=1, column=1): Unexpected end of input"
    }
    should("accept empty readers") {
      JsonParser.of(StringReader(""), 1) // parsing will fail
    }
  }

  context("parsing") {
    should("reject empty readers") {
      val failure = shouldThrowExactly<JsonParser.IllegalJsonSyntax> {
        Json.parse(StringReader(""))
      }
      failure.message shouldBe "Location(offset=0, line=1, column=1): Unexpected end of input"
    }
    should("recognize null json value") {
      val json = Json.parse("null")
      json.isDefined() shouldBe true
      json.isNull() shouldBe true
    }
    should("recognize true json value") {
      val json = Json.parse("true")
      json.isDefined() shouldBe true
      json.isNull() shouldBe false
      json.isBoolean() shouldBe true
      json.asBoolean() shouldBe true
      json.isNullableBoolean() shouldBe true
      json.asNullableBoolean() shouldBe true
      json.isTrue() shouldBe true
    }
    should("recognize false json value") {
      val json = Json.parse("false")
      json.isDefined() shouldBe true
      json.isNull() shouldBe false
      json.isBoolean() shouldBe true
      json.asBoolean() shouldBe false
      json.isNullableBoolean() shouldBe true
      json.asNullableBoolean() shouldBe false
      json.isFalse() shouldBe true
    }
    should("recognize simple string json value") {
      val json = Json.parse("\"kayak\"")
      json.isDefined() shouldBe true
      json.isNull() shouldBe false
      json.isString() shouldBe true
      json.asString() shouldBe "kayak"
      json.isNullableString() shouldBe true
      json.asNullableString() shouldBe "kayak"
    }
    should("recognize integer json value") {
      val json = Json.parse("111")
      json.isDefined() shouldBe true
      json.isNull() shouldBe false
      json.isNumber() shouldBe true
      json.asNumber() shouldBe "111"
      json.isNullableNumber() shouldBe true
      json.asNullableNumber() shouldBe "111"
    }
    should("recognize decimal json value") {
      val json = Json.parse("111.22")
      json.isDefined() shouldBe true
      json.isNull() shouldBe false
      json.isNumber() shouldBe true
      json.asNumber() shouldBe "111.22"
      json.isNullableNumber() shouldBe true
      json.asNullableNumber() shouldBe "111.22"
    }
    should("recognize array json value") {
      val json = Json.parse("""[111, 0.22, [true, false], {"sara":"catunga"}]""")
      json.isDefined() shouldBe true
      json.isNull() shouldBe false
      json.isArray() shouldBe true
      json.asArray() shouldBe listOf(
        JsonNumber.of("111"),
        JsonNumber.of("0.22"),
        JsonArray.of(JsonBoolean.TRUE, JsonBoolean.FALSE),
        JsonObject.of(JsonString.of("sara") to JsonString.of("catunga"))
      )
      json.isNullableArray() shouldBe true
      json.asNullableArray() shouldBe listOf(
        JsonNumber.of("111"),
        JsonNumber.of("0.22"),
        JsonArray.of(JsonBoolean.TRUE, JsonBoolean.FALSE),
        JsonObject.of(JsonString.of("sara") to JsonString.of("catunga"))
      )
    }
    should("recognize object json value") {
      val json = Json.parse("""{
        "sara": "catunga",
        "flag": true
      }""")
      json.isDefined() shouldBe true
      json.isNull() shouldBe false
      json.isObject() shouldBe true
      json.asObject() shouldBe mapOf(
        JsonString.of("sara") to JsonString.of("catunga"),
        JsonString.of("flag") to JsonBoolean.TRUE
      )
      json.isNullableObject() shouldBe true
      json.asNullableObject() shouldBe mapOf(
        JsonString.of("sara") to JsonString.of("catunga"),
        JsonString.of("flag") to JsonBoolean.TRUE
      )
    }
  }
})

