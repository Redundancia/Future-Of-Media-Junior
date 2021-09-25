package hu.futureofmedia.task.contactsapi.service;

import hu.futureofmedia.task.contactsapi.entities.contact.Contact;
import hu.futureofmedia.task.contactsapi.entities.contact.ContactDTO;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public List<ContactDTO> findAllByActiveStatus(int offSet) {
        List<Contact> activeContacts= contactRepository.findAllByActiveStatus(offSet);
        List<ContactDTO> DTOs = new ArrayList<>();
        activeContacts.forEach(
                contact -> DTOs.add(new ContactDTO(contact.getId(), contact.getFirstName(),
                        contact.getLastName(),
                        contact.getEmail(),
                        contact.getPhoneNumber(),
                        contact.getCompany())));
        return DTOs;
    }
}
