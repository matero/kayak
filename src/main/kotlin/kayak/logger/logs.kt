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
package kayak.logger

import java.util.logging.Level
import java.util.logging.Logger

inline fun <T : Any> makeLogger(klass: kotlin.reflect.KClass<T>) = Logger.getLogger(klass.qualifiedName)
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