package hu.futureofmedia.task.contactsapi.entities.contact;

import hu.futureofmedia.task.contactsapi.entities.Company;
import lombok.Data;


@Data
public class ContactDTO {

    private String fullName;

    private String companyName;

    private String email;

    private String phoneNumber;

    private Long contactId;

    private Long companyId;

    public ContactDTO(Long contactId, String firstName, String lastName, String email, String phoneNumber, Company company) {
        this.fullName = firstName + " " + lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.companyName = company.getName();
        this.contactId = contactId;
        this.companyId = company.getId();
    }
}
