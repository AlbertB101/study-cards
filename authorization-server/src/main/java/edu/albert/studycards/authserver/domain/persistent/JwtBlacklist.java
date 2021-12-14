package edu.albert.studycards.authserver.domain.persistent;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;

@Entity(name = "JWT_Blacklist")
@Table(name = "jwt_blacklist")
public class JwtBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String token;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date created;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expired_at")
    private Date expired;

    public JwtBlacklist() {
    }

    public JwtBlacklist(String token) {
        this.token = token;
        this.created = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(created);
        c.add(Calendar.DATE, 1);
        this.expired = c.getTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }
}
