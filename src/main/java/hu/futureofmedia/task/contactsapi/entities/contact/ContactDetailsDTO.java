package hu.futureofmedia.task.contactsapi.entities.contact;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public
class ContactDetailsDTO {

    private String firstName;

    private String lastName;

    private String companyName;

    private String email;

    private String phoneNumber;

    private String comment;

    private Long contactId;

    private Long companyId;

    private LocalDateTime creationDate;

    private LocalDateTime lastUpdatedDate;

    public ContactDetailsDTO(Contact contact) {
        this.firstName = contact.getFirstName();
        this.lastName = contact.getLastName();
        this.email = contact.getEmail();
        this.phoneNumber = contact.getPhoneNumber();
        this.companyName = contact.getCompany().getName();
        this.contactId = contact.getId();
        this.companyId = contact.getCompany().getId();
        this.comment = contact.getComment();
        this.creationDate = contact.getCreationDate();
        this.lastUpdatedDate = contact.getLastUpdatedDate();
    }
}
