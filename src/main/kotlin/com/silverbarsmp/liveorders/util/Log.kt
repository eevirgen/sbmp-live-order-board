package silverbarsmp.liveorderboard.util

import org.apache.log4j.*
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

const val LOG_CONSOLE_LAYOUT = "%m%n"
const val LOG_FILE_LAYOUT = "%d %-5p : %m%n"
const val LOG_FILE = "log/out.log"

fun initLog() {
    Logger.getRootLogger().apply {
        allAppenders.iterator().forEach { removeAppender(it as Appender) }
        addAppender(ConsoleAppender(PatternLayout(LOG_CONSOLE_LAYOUT)))
        addAppender(
            RollingFileAppender(PatternLayout(LOG_FILE_LAYOUT), LOG_FILE).apply {
                setMaxFileSize("10MB")
                maxBackupIndex = 10
            })
        level = Level.INFO
    }
}

fun log(clazz: KClass<*> = Any::class) = LoggerFactory.getLogger(clazz.java)!!
