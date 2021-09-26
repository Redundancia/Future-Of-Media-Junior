package hu.futureofmedia.task.contactsapi.entities.contact;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hu.futureofmedia.task.contactsapi.entities.Company;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber;

    @ManyToOne
    @JsonIgnoreProperties("contacts")
    private Company company;

    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private LocalDateTime creationDate;

    private LocalDateTime lastUpdatedDate;
}
