package pl.bak.businessallocationapp.registration.token.model;

import pl.bak.businessallocationapp.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "ConfirmationToken")
@Table(
        name = "confirmation_token",
        uniqueConstraints = {
                @UniqueConstraint(name = "confirmation_token_unique", columnNames = "token"),
        }
)
public class ConfirmationToken {

    @Id
    @SequenceGenerator(
            name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "token",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String token;

    @Column(
            name = "create_at",
            nullable = false
    )
    private LocalDateTime createAt;

    @Column(
            name = "expired_at",
            nullable = false
    )
    private LocalDateTime expiredAt;

    @Column(
            name = "confirm_at"
    )
    private LocalDateTime confirmAt;

    @ManyToOne
    @JoinColumn(
            name = "app_user_id",
            nullable = false
    )
    private User user;

    public ConfirmationToken() {
    }

    public ConfirmationToken(String token, LocalDateTime createAt,
                             LocalDateTime expiredAt, User user) {
        this.token = token;
        this.createAt = createAt;
        this.expiredAt = expiredAt;
        this.user = user;
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

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public LocalDateTime getConfirmAt() {
        return confirmAt;
    }

    public void setConfirmAt(LocalDateTime confirmAt) {
        this.confirmAt = confirmAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
