package hu.futureofmedia.task.contactsapi.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "company")
    @EqualsAndHashCode.Exclude
    @Singular
    private Set<Contact> contacts;

}
