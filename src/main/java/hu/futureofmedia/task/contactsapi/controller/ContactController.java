package hu.futureofmedia.task.contactsapi.controller;

import hu.futureofmedia.task.contactsapi.entities.contact.Contact;
import hu.futureofmedia.task.contactsapi.entities.contact.ContactDetailsDTO;
import hu.futureofmedia.task.contactsapi.entities.contact.ContactFindAllActiveDTO;
import hu.futureofmedia.task.contactsapi.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/contact")
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService ) {
        this.contactService = contactService;
    }

    @GetMapping("/all/{offSet}")
    public List<ContactFindAllActiveDTO> getAllActiveContacts(@PathVariable String offSet) {
        return contactService.findAllByActiveStatus(Integer.parseInt(offSet)-1);
    }

    @GetMapping("/{contactId}")
    public ResponseEntity<ContactDetailsDTO> findById(@PathVariable String contactId) {
        Contact contact =contactService.findById(contactId).orElse(null);
        return contact == null ? new ResponseEntity<>(null, HttpStatus.BAD_REQUEST) :
                new ResponseEntity<>(new ContactDetailsDTO(contact), HttpStatus.OK);
    }
}
