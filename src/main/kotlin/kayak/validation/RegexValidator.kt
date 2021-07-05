package kayak.validation

import java.util.regex.Pattern

/**
 * Construct a validator that matches any one of the set of regular
 * expressions with the specified case sensitivity.
 *
 * @param regexes The set of regular expressions this validator will
 * validate against
 * @param caseSensitive when `true` matching is *case
 * sensitive*, otherwise matching is *case in-sensitive*
 */
class RegexValidator @JvmOverloads constructor(caseSensitive: Boolean = true, vararg regexes: String) {
  private val patterns: Array<Pattern>

  init {
    require(regexes.isNotEmpty()) { "Regular expressions are missing" }
    val flags = if (caseSensitive) 0 else Pattern.CASE_INSENSITIVE
    patterns = Array<Pattern>(regexes.size) {
      require(regexes[it].isNotEmpty()) { "Regular expression[$it] is missing" }
      Pattern.compile(regexes[it], flags)
    }
  }

  /**
   * Validate a value against the set of regular expressions.
   *
   * @param value The value to validate.
   * @return `true` if the value is valid
   * otherwise `false`.
   */
  fun isValid(value: String?): Boolean {
    if (value == null) {
      return false
    }
    for (pattern in patterns) {
      if (pattern.matcher(value).matches()) {
        return true
      }
    }
    return false
  }

  /**
   * Validate a value against the set of regular expressions
   * returning the array of matched groups.
   *
   * @param value The value to validate.
   * @return String array of the *groups* matched if
   * valid or `null` if invalid
   */
  fun match(value: String?): Array<String> {
    if (value == null) {
      return emptyArray()
    }
    for (pattern in patterns) {
      val matcher = pattern.matcher(value)
      if (matcher.matches()) {
        return Array(matcher.groupCount()) { matcher.group(it + 1) }
      }
    }
    return emptyArray()
  }

  /**
   * Validate a value against the set of regular expressions
   * returning a String value of the aggregated groups.
   *
   * @param value The value to validate.
   * @return Aggregated String value comprised of the
   * *groups* matched if valid or `null` if invalid
   */
  fun validate(value: String?): String? {
    if (value == null) {
      return null
    }
    for (pattern in patterns) {
      val matcher = pattern.matcher(value)
      if (matcher.matches()) {
        val count: Int = matcher.groupCount()
        if (count == 1) {
          return matcher.group(1)
        }
        val buffer = StringBuilder()
        for (j in 0 until count) {
          val component = matcher.group(j + 1)
          if (component != null) {
            buffer.append(component)
          }
        }
        return buffer.toString()
      }
    }
    return null
  }

  /**
   * Provide a String representation of this validator.
   * @return A String representation of this validator
   */
  override fun toString(): String {
    val buffer = StringBuilder()
    buffer.append("RegexValidator{")
    for (i in patterns.indices) {
      if (i > 0) {
        buffer.append(",")
      }
      buffer.append(patterns[i].pattern())
    }
    buffer.append("}")
    return buffer.toString()
  }
}
