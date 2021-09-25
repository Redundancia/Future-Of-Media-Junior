package hu.futureofmedia.task.contactsapi.repositories;

import hu.futureofmedia.task.contactsapi.entities.contact.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query(
            value = "SELECT * FROM contact WHERE contact.status = 'ACTIVE'",
            nativeQuery = true)
    Set<Contact> findAllByActiveStatus();
}

