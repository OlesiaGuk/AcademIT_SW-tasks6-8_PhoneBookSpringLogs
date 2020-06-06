package ru.academits;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.academits.dao.ContactDao;
import ru.academits.model.Contact;
import ru.academits.model.ContactValidation;
import ru.academits.service.ContactService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddContactServiceTests {
    private ContactService contactService = new ContactService(new ContactDao());

    @BeforeClass
    public static void globalSetUp() {
        System.out.println("AddContact method from ContactService class tests starting...");
    }

    @Test
    public void testAddCorrectContact() {
        ContactValidation contactValidation = new ContactValidation();
        contactValidation.setValid(true);

        Contact contact = new Contact();
        contact.setFirstName("Петр");
        contact.setLastName("П");
        contact.setPhone("2");
        Assert.assertEquals(contactValidation, contactService.addContact(contact));
    }

    @Test
    public void testAddEmptyContact() {
        ContactValidation contactValidation = new ContactValidation();
        contactValidation.setValid(true);

        Contact contact = new Contact();
        contact.setFirstName("");
        contact.setLastName("");
        contact.setPhone("");
        Assert.assertNotEquals(contactValidation, contactService.addContact(contact));
    }

    @Test
    public void testAddContactWithAlreadyExistingPhone() {
        ContactValidation contactValidation = new ContactValidation();
        contactValidation.setValid(false);
        contactValidation.setError("Номер телефона не должен дублировать другие номера в телефонной книге.");

        Contact contact = new Contact();
        contact.setFirstName("Петр");
        contact.setLastName("П");
        contact.setPhone("9123456789");//phone number from ContactDao class constructor
        Assert.assertEquals(contactValidation, contactService.addContact(contact));
    }
}
