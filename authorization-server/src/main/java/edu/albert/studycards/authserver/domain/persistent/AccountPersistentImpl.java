package edu.albert.studycards.authserver.domain.persistent;

import edu.albert.studycards.authserver.domain.Role;
import edu.albert.studycards.authserver.domain.Status;
import edu.albert.studycards.authserver.domain.dto.AccountRegistrationDto;
import edu.albert.studycards.authserver.domain.dto.UserAccountDto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "UserAccount")
@Table(name = "userAccount")
public class AccountPersistentImpl implements AccountPersistent, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column
    @Size(max = 32)
    private String firstName;

    @NotNull
    @Column
    @Size(max = 32)
    private String lastName;

    @Column
    @Size(max = 128)
    private String email;

    @NotNull
    @Column
    @Size(max = 64)
    private String password;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date created;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(name = "jwt_token")
    private String token;

    public AccountPersistentImpl(UserAccountDto userAcc) {
        this.email = userAcc.email();
        this.firstName = userAcc.firstName();
        this.lastName = userAcc.lastName();
        this.password = userAcc.password();
        this.created = new Date();
        this.role = Role.USER;
        this.status = Status.ACTIVE;
    }

    public AccountPersistentImpl(AccountRegistrationDto regRequest) {
        this.email = regRequest.email();
        this.firstName = regRequest.firstName();
        this.lastName = regRequest.lastName();
        this.password = regRequest.password();
        this.created = new Date();
        this.role = Role.USER;
        this.status = Status.ACTIVE;
    }

    public AccountPersistentImpl() {

    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }
}
