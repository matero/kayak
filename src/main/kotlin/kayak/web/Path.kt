package kayak.web

@JvmInline
value class Path(private val value: String) {
    companion object {
        val INDEX = Path("")
    }
}
