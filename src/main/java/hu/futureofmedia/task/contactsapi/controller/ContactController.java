package hu.futureofmedia.task.contactsapi.controller;

import hu.futureofmedia.task.contactsapi.entities.contact.Contact;
import hu.futureofmedia.task.contactsapi.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/contact")
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/all")
    public Set<Contact> getAllActiveContacts() {
        return contactService.findAllByActiveStatus();
    }

}
