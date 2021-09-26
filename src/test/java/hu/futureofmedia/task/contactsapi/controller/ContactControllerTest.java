package hu.futureofmedia.task.contactsapi.controller;

import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.entities.contact.Contact;
import hu.futureofmedia.task.contactsapi.entities.contact.Status;
import hu.futureofmedia.task.contactsapi.repositories.CompanyRepository;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ContactControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ContactRepository mockContactRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Test
    void getFirstTenContactRoute_shouldReturnJsonWithListOfContacts_whenMultipleContactsArePresent() throws Exception {
        Company company1 = Company.builder().name("company1").build();
        Contact contact1 = Contact.builder()
                .firstName("Contact")
                .lastName("One")
                .lastUpdatedDate(LocalDateTime.now())
                .creationDate(LocalDateTime.now())
                .email("contact@one.hu")
                .status(Status.ACTIVE)
                .build();
        Contact contact2 = Contact.builder()
                .firstName("Contact")
                .lastName("Two")
                .lastUpdatedDate(LocalDateTime.now())
                .creationDate(LocalDateTime.now())
                .email("contact@two.hu")
                .status(Status.ACTIVE)
                .build();
        Contact contact3 = Contact.builder()
                .firstName("Contact")
                .lastName("Three")
                .lastUpdatedDate(LocalDateTime.now())
                .creationDate(LocalDateTime.now())
                .email("contact@three.hu")
                .status(Status.DELETED)
                .build();
        company1.setContacts(Set.of(contact1, contact2, contact3));
        companyRepository.save(company1);
        contact1.setCompany(company1);
        contact2.setCompany(company1);
        contact3.setCompany(company1);
        mockContactRepository.saveAll(List.of(contact1, contact2, contact3));


        Mockito.when(mockContactRepository.findAllByActiveStatus(0)).thenReturn(List.of(contact1, contact2));

        mockMvc.perform(MockMvcRequestBuilders.get("/contact/all/{id}", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Contact One"))
                .andExpect(jsonPath("$[1].fullName").value("Contact Two"));
    }

    @Test
    void findById_returnsContactWhenValidId() throws Exception {
        Company company1 = Company.builder().name("company1").build();
        Contact contact1 = Contact.builder()
                .firstName("Contact")
                .lastName("One")
                .lastUpdatedDate(LocalDateTime.now())
                .creationDate(LocalDateTime.now())
                .company(company1)
                .email("contact@one.hu")
                .status(Status.ACTIVE)
                .build();
        mockContactRepository.save(contact1);

        Mockito.when(mockContactRepository.findById(1L)).thenReturn(Optional.of(contact1));

        mockMvc.perform(MockMvcRequestBuilders.get("/contact/{id}", "1"))
                .andDo(print())
                .andExpect(jsonPath("email").value("contact@one.hu"));
    }

    @Test
    void findById_returnsNullWhenInValidId() throws Exception {
        Company company2 = Company.builder().name("company2").build();
        Contact contact2 = Contact.builder()
                .firstName("Contact")
                .lastName("Two")
                .lastUpdatedDate(LocalDateTime.now())
                .creationDate(LocalDateTime.now())
                .company(company2)
                .email("contact@two.hu")
                .status(Status.ACTIVE)
                .build();
        mockContactRepository.save(contact2);

        Mockito.when(mockContactRepository.findById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/contact/{id}", "2"))
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist());

    }

    @Test
    void newContact_InvalidNameThrowsError() throws Exception {
        Company company2 = Company.builder().name("company2").build();

        Contact contact2 = Contact.builder()
                .firstName("Contact")
                .lastName("Two")
                .lastUpdatedDate(LocalDateTime.now())
                .creationDate(LocalDateTime.now())
                .company(company2)
                .email("contact@two.hu")
                .status(Status.ACTIVE)
                .build();
        Mockito.when(mockContactRepository.save(any())).thenReturn(contact2);
        mockMvc.perform(MockMvcRequestBuilders.post("/contact/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"lastName\": \"two\", \"companyId\": \"1\", " +
                                "\"phoneNumber\": \"00333333101\", \"email\": \"1000asfasf@afaf.hu\", \"comment\": \"Lori\"}"))
                .andExpect(status().isBadRequest());


    }

    @Test
    void newContact_InvalidPhoneNumberThrowsError() throws Exception {
        Company company2 = Company.builder().name("company2").build();
        Contact contact1 = Contact.builder()
                .firstName("Contact")
                .lastName("Two")
                .lastUpdatedDate(LocalDateTime.now())
                .creationDate(LocalDateTime.now())
                .company(company2)
                .email("contact@two.hu")
                .status(Status.ACTIVE)
                .build();
        mockContactRepository.save(contact1);
        mockMvc.perform(MockMvcRequestBuilders.post("/contact/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"lol\", \"lastName\": \"two\", \"companyId\": \"1\", " +
                                "\"phoneNumber\": \"00333333100000000000001\", \"email\": \"contacttwhu\", \"comment\": \"Lori\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());


    }
}