package hu.futureofmedia.task.contactsapi.service;

import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.entities.contact.Contact;
import hu.futureofmedia.task.contactsapi.entities.contact.ContactFindAllActiveDTO;
import hu.futureofmedia.task.contactsapi.entities.contact.ContactNewDTO;
import hu.futureofmedia.task.contactsapi.entities.contact.Status;
import hu.futureofmedia.task.contactsapi.repositories.CompanyRepository;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;

@Component
public class ContactService {

    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;


    @Autowired
    public ContactService(ContactRepository contactRepository, CompanyRepository companyRepository) {
        this.contactRepository = contactRepository;
        this.companyRepository = companyRepository;
    }

    public List<ContactFindAllActiveDTO> findAllByActiveStatus(int offSet) {
        List<Contact> activeContacts= contactRepository.findAllByActiveStatus(offSet);
        List<ContactFindAllActiveDTO> DTOs = new ArrayList<>();
        activeContacts.forEach(
                contact -> DTOs.add(new ContactFindAllActiveDTO(contact.getId(), contact.getFirstName(),
                        contact.getLastName(),
                        contact.getEmail(),
                        contact.getPhoneNumber(),
                        contact.getCompany())));
        return DTOs;
    }

    public Optional<Contact> findById(String contactId) {
        return contactRepository.findById(Long.parseLong(contactId));
    }

    public ResponseEntity<String> validateAndSave(@Valid @RequestBody ContactNewDTO contactNewDTO) {
        Optional<Company> contactCompany = companyRepository.findById(contactNewDTO.getCompanyId());
        if (contactCompany.isEmpty()) {
            return new ResponseEntity<>("Invalid company input", HttpStatus.BAD_REQUEST);
        }
        if (contactNewDTO.getPhoneNumber() != null){
            String regex = "^\\+?\\d{10,14}$";
            String text = contactNewDTO.getPhoneNumber();
            Pattern pt = Pattern.compile(regex);
            Matcher matcher = pt.matcher(text);
            if (!matcher.matches()) {
                return new ResponseEntity<>("Invalid phone number, try E-164 format", HttpStatus.BAD_REQUEST);
            }
        }
        Contact contactToAdd = Contact.builder()
                .firstName(contactNewDTO.getFirstName())
                .lastName(contactNewDTO.getLastName())
                .email(contactNewDTO.getEmail())
                .phoneNumber(contactNewDTO.getPhoneNumber())
                .company(contactCompany.get())
                .comment(Jsoup.parse(contactNewDTO.getComment()).text())
                .status(Status.ACTIVE)
                .creationDate(LocalDateTime.now())
                .lastUpdatedDate(LocalDateTime.now())
                .build();
        Contact newContact = contactRepository.save(contactToAdd);
        return ResponseEntity.ok("Valid contact");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
