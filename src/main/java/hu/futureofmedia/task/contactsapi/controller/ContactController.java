package hu.futureofmedia.task.contactsapi.controller;

import hu.futureofmedia.task.contactsapi.entities.contact.Contact;
import hu.futureofmedia.task.contactsapi.entities.contact.ContactDetailsDTO;
import hu.futureofmedia.task.contactsapi.entities.contact.ContactFindAllActiveDTO;
import hu.futureofmedia.task.contactsapi.entities.contact.ContactNewDTO;
import hu.futureofmedia.task.contactsapi.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return contact == null ? new ResponseEntity<>(null, HttpStatus.OK) :
                new ResponseEntity<>(new ContactDetailsDTO(contact), HttpStatus.OK);
    }

    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<String> addNewContact(@RequestBody ContactNewDTO contactNewDTO) {
        return contactService.validateAndSave(contactNewDTO);
    }
}
