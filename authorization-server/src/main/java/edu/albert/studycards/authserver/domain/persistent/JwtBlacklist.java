package edu.albert.studycards.authserver.domain.persistent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;

@Entity(name = "JWT_Blacklist")
@Table(name = "jwt_blacklist")
@Getter
@Setter
@NoArgsConstructor
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
    
    public JwtBlacklist(String token) {
        this.token = token;
        this.created = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(created);
        c.add(Calendar.DATE, 1);
        this.expired = c.getTime();
    }
}
