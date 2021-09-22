package hu.futureofmedia.task.contactsapi.repositories;

import hu.futureofmedia.task.contactsapi.entities.Contact;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ContactRepository extends Repository<Contact, Long> {
    List<Contact> findAll();
}

