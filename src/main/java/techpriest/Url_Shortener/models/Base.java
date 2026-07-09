package techpriest.Url_Shortener.models;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;


@MappedSuperclass
public class Base {
    public Base() {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    final private Instant createdAt = Instant.now();
    final private Instant updatedAt = Instant.now();


    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }


}