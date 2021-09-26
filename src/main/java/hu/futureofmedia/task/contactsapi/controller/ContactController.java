package hu.futureofmedia.task.contactsapi.controller;

import hu.futureofmedia.task.contactsapi.entities.contact.Contact;
import hu.futureofmedia.task.contactsapi.entities.contact.ContactDetailsDTO;
import hu.futureofmedia.task.contactsapi.entities.contact.ContactFindAllActiveDTO;
import hu.futureofmedia.task.contactsapi.entities.contact.ContactNewDTO;
import hu.futureofmedia.task.contactsapi.service.ContactService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/contact")
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService ) {
        this.contactService = contactService;
    }

    @GetMapping("/all/{page}")
    @ApiOperation(value= "Get 10 contacts per page",
            notes = "Gets first 10 contacts ordered by name, offset by provided page number*10.")
    public List<ContactFindAllActiveDTO> getAllActiveContacts(@PathVariable String page) {
        return contactService.findAllByActiveStatus(Integer.parseInt(page)-1);
    }

    @GetMapping("/{contactId}")
    @ApiOperation(value= "Find contact by contact ID",
            notes = "Finds contact if ID is valid")
    public ResponseEntity<ContactDetailsDTO> findById(@PathVariable String contactId) {
        Contact contact =contactService.findById(contactId).orElse(null);
        return contact == null ? new ResponseEntity<>(null, HttpStatus.OK) :
                new ResponseEntity<>(new ContactDetailsDTO(contact), HttpStatus.OK);
    }

    @PostMapping("/new")
    @ResponseBody
    @ApiOperation(value= "Add new contact",
            notes = "Adds new contact if required fields are filled, phone number is validated, " +
                    "email is validated and unique")
    public ResponseEntity<String> addNewContact(@Valid @RequestBody ContactNewDTO contactNewDTO) {
        return contactService.validateAndSave(contactNewDTO);
    }

    @PutMapping("/{contactId}")
    @ApiOperation(value= "Updates contact",
            notes = "Updates given contact if contact ID is valid, required fields are filled, " +
                    "phone number is validated, email is validated and unique")
    public ResponseEntity<String> updateContact(@PathVariable Long contactId,@Valid @RequestBody ContactNewDTO contactNewDTO) {
        return contactService.validateAndUpdate(contactId, contactNewDTO);
    }

    @DeleteMapping("/{contactId}")
    @ApiOperation(value= "Deletes contact",
            notes = "Deletes given contact if contact ID is valid")
    public ResponseEntity<String> deleteContact(@PathVariable Long contactId) {
        return contactService.deleteContact(contactId);
    }
}
