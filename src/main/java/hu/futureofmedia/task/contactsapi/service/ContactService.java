package hu.futureofmedia.task.contactsapi.service;

import hu.futureofmedia.task.contactsapi.entities.contact.Contact;
import hu.futureofmedia.task.contactsapi.entities.contact.ContactDTO;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Set<ContactDTO> findAllByActiveStatus() {
        Set<Contact> activeContacts= contactRepository.findAllByActiveStatus();
        Set<ContactDTO> DTOs = new HashSet<>();
        activeContacts.forEach(
                contact -> DTOs.add(new ContactDTO(contact.getId(), contact.getFirstName(),
                        contact.getLastName(),
                        contact.getEmail(),
                        contact.getPhoneNumber(),
                        contact.getCompany())));
        return DTOs;
    }
}
