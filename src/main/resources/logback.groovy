import ch.qos.logback.core.joran.spi.ConsoleTarget
import org.thehellnet.utility.PathUtility

def logDir = PathUtility.join(System.getProperty("user.home"), "log", "lanparty")
def logName = "manager"
def logDateTimePattern = "%d{yyyyMMdd}"
def logRetentionDays = 30
def commonPattern = "%date{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{0} %class{0}.%method:%line - %msg%n"


appender("STDOUT", ConsoleAppender) {
    target = ConsoleTarget.SystemOut
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{5} - %msg%n"
    }
}

appender("FILE_GENERIC", RollingFileAppender) {
    append = true
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = PathUtility.join(logDir, sprintf("%s-%s-%s.log", logDateTimePattern, logName, "generic"))
        maxHistory = logRetentionDays
    }
    encoder(PatternLayoutEncoder) {
        pattern = commonPattern
    }
}

root(WARN, ["STDOUT"])

logger("org.thehellnet.lanparty.manager.init", WARN, ["FILE_GENERIC"])
logger("org.thehellnet.lanparty.manager", DEBUG, ["FILE_GENERIC"])

logger("org.hibernate", ERROR)
