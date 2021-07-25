package kayak.validation

import kayak.json.AsJson

interface Failure : AsJson {
  fun message(): String = "failure"

  fun details(): Map<String, Failure> = emptyMap()
}
