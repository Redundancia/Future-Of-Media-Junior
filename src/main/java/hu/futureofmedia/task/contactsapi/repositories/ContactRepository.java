package hu.futureofmedia.task.contactsapi.repositories;

import hu.futureofmedia.task.contactsapi.entities.contact.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query(
            value = "SELECT * FROM contact WHERE contact.status = 'ACTIVE' ORDER BY contact.first_name, contact.last_name LIMIT 10 OFFSET :offSet*10",
            nativeQuery = true)
    List<Contact> findAllByActiveStatus(int offSet);
}

