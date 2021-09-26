package hu.futureofmedia.task.contactsapi.entities.contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactNewDTO {

    @NotNull(message = "First name cannot be null")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    private String lastName;

    @NotNull(message = "Company cannot be null")
    private Long companyId;

    @Column(nullable = false, unique = true)
    @Email()
    private String email;

    private String phoneNumber;

    private String comment;

}