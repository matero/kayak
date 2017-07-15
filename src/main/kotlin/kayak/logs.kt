package kayak

import java.util.logging.Level
import java.util.logging.Logger
import kotlin.reflect.KClass

inline fun <T : Any> makeLogger(klass: KClass<T>) = Logger.getLogger(klass.qualifiedName)
fun <T : Any> makeLogger(clazz: Class<T>) = Logger.getLogger(clazz.canonicalName)
fun <T : Any> makeLogger(loggerName: String) = Logger.getLogger(loggerName)

/**
 * Log a SEVERE message.
 *
 *
 * If the logger is currently enabled for the SEVERE message
 * level then the given message is forwarded to all the
 * registered output Handler objects.
 *
 *
 * @param   msg     The string message (or a key in the message catalog)
 */
inline fun Logger.severe(msg: () -> String) {
    if (isLoggable(Level.SEVERE))
        log(Level.SEVERE, msg())
}

/**
 * Log a WARNING message.
 *
 *
 * If the logger is currently enabled for the WARNING message
 * level then the given message is forwarded to all the
 * registered output Handler objects.
 *
 *
 * @param   msg     The string message (or a key in the message catalog)
 */
inline fun Logger.warning(msg: () -> String) {
    if (isLoggable(Level.WARNING))
        log(Level.WARNING, msg())
}

/**
 * Log an INFO message.
 *
 *
 * If the logger is currently enabled for the INFO message
 * level then the given message is forwarded to all the
 * registered output Handler objects.
 *
 *
 * @param   msg     The string message (or a key in the message catalog)
 */
inline fun Logger.info(msg: () -> String) {
    if (isLoggable(Level.INFO))
        log(Level.INFO, msg())
}

/**
 * Log a CONFIG message.
 *
 *
 * If the logger is currently enabled for the CONFIG message
 * level then the given message is forwarded to all the
 * registered output Handler objects.
 *
 *
 * @param   msg     The string message (or a key in the message catalog)
 */
inline fun Logger.config(msg: () -> String) {
    if (isLoggable(Level.CONFIG))
        log(Level.CONFIG, msg())
}

/**
 * Log a FINE message.
 *
 *
 * If the logger is currently enabled for the FINE message
 * level then the given message is forwarded to all the
 * registered output Handler objects.
 *
 *
 * @param   msg     The string message (or a key in the message catalog)
 */
inline fun Logger.fine(msg: () -> String) {
    if (isLoggable(Level.FINE))
        log(Level.FINE, msg())
}

/**
 * Log a FINER message.
 *
 *
 * If the logger is currently enabled for the FINER message
 * level then the given message is forwarded to all the
 * registered output Handler objects.
 *
 *
 * @param   msg     The string message (or a key in the message catalog)
 */
inline fun Logger.finer(msg: () -> String) {
    if (isLoggable(Level.FINER))
        log(Level.FINER, msg())
}

/**
 * Log a FINEST message.
 *
 *
 * If the logger is currently enabled for the FINEST message
 * level then the given message is forwarded to all the
 * registered output Handler objects.
 *
 *
 * @param   msg     The string message (or a key in the message catalog)
 */
inline fun Logger.finest(msg: () -> String) {
    if (isLoggable(Level.FINEST))
        log(Level.FINEST, msg())
}
