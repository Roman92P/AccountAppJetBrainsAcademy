package account.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class SecurityEvent {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate date = LocalDate.now();
    @Enumerated(EnumType.STRING)
    private EventName action;
    private String subject;
    private String object;
    private String path;

    public SecurityEvent() {
    }

    public SecurityEvent(EventName action, String object, String path) {
        this.action = action;
        this.object = object;
        this.path = path;
    }

    public LocalDate getDate() {
        return date;
    }

    public EventName getAction() {
        return action;
    }

    public void setAction(EventName action) {
        this.action = action;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        String s = SecurityContextHolder.getContext().getAuthentication().getName() == null? "Anonymous" : SecurityContextHolder.getContext().getAuthentication().getName();
        this.subject = s;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
