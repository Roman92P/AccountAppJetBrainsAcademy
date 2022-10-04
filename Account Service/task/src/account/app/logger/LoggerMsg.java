package account.app.logger;

import org.springframework.boot.logging.LogLevel;

import java.time.LocalDateTime;

public class LoggerMsg {
    //[date time][log level][message]
    private LocalDateTime time;
    private LogLevel level;
    private String message;

    public LoggerMsg() {
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("[%s][%s]%s", time, level, message);
    }
}
