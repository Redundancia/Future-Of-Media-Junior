package hu.futureofmedia.task.contactsapi.entities;

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

    //regex if you want different restriction
    @Email
    @Column(nullable = false)
    private String email;

    //TODO regex the phone number ^\+?\d{10,14}$
    private String phoneNumber;

    @ManyToOne
    private Company company;

    //TODO strip html tags in controller/service Jsoup.parse(html).text();
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private LocalDateTime creationDate;

    private LocalDateTime lastUpdatedDate;
}
