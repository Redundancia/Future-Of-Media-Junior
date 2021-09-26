package hu.futureofmedia.task.contactsapi.service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
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

import java.time.LocalDateTime;
import java.util.*;
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

    public ResponseEntity<String> validateAndSave(ContactNewDTO contactNewDTO)  {
        Optional<Company> contactCompany = companyRepository.findById(contactNewDTO.getCompanyId());

        ResponseEntity<String> returnResponse= isContactDetailValid(contactNewDTO, contactCompany);

        if (returnResponse.getStatusCodeValue() != 200) return returnResponse;

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
        contactRepository.save(contactToAdd);
        return returnResponse;
    }
    //TODO maybe move this to UTIL?
    private boolean isContactPhoneNumberValid(String phoneNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phone = phoneNumberUtil.parse(phoneNumber,
                    Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name());
            return phoneNumberUtil.isValidNumber(phone);
        } catch (NumberParseException numberParseException) {
            return false;
        }
    }

    //TODO maybe move this to UTIL?
    private boolean isContactCompanyValid(Optional<Company> contactCompany) {
        return contactCompany.isEmpty();
    }

    private ResponseEntity<String> isContactDetailValid(ContactNewDTO contactNewDTO, Optional<Company> contactCompany) {

        if (isContactCompanyValid(contactCompany)) {
            return new ResponseEntity<>("Invalid company input", HttpStatus.BAD_REQUEST);
        }

        if (contactNewDTO.getPhoneNumber() != null){
            if (!isContactPhoneNumberValid(contactNewDTO.getPhoneNumber())) {
                return new ResponseEntity<>("Invalid phone number, try E-164 format", HttpStatus.BAD_REQUEST);
            }
        }

        if (contactNewDTO.getFirstName() == null) {
            return new ResponseEntity<>("Invalid name", HttpStatus.BAD_REQUEST);
        }
        if (contactNewDTO.getLastName() == null) {
            return new ResponseEntity<>("Invalid name", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Valid contact");
    }

    public ResponseEntity<String> validateAndUpdate(Long contactId, ContactNewDTO contactNewDTO) {
        Optional<Company> contactCompany = companyRepository.findById(contactNewDTO.getCompanyId());

        Optional<Contact> contactToUpdate = contactRepository.findById(contactId);

        if(contactRepository.findById(contactId).isEmpty()) return new ResponseEntity<>("Contact to update doesn't exist", HttpStatus.BAD_REQUEST);

        ResponseEntity<String> returnResponse= isContactDetailValid(contactNewDTO, contactCompany);

        if (returnResponse.getStatusCodeValue() != 200) return returnResponse;

        Contact contact = contactToUpdate.get();

        contact.setFirstName(contactNewDTO.getFirstName());
        contact.setLastName(contactNewDTO.getLastName());
        contact.setEmail(contactNewDTO.getEmail());
        contact.setPhoneNumber(contactNewDTO.getPhoneNumber());
        contact.setCompany(contactCompany.get());
        contact.setComment(Jsoup.parse(contactNewDTO.getComment()).text());
        contact.setLastUpdatedDate(LocalDateTime.now());

        contactRepository.save(contact);
        return returnResponse;
    }
}
