package ru.academits;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.academits.dao.ContactDao;
import ru.academits.model.Contact;

import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PhonebookSpringApplicationTests {
    private ContactDao contactDao = new ContactDao();

    @BeforeClass
    public static void globalSetUp() {
        System.out.println("Delete method tests starting...");
    }

    @Before
    public void setUp() {
        Contact contact = new Contact();
        contact.setFirstName("Петр");
        contact.setLastName("П");
        contact.setPhone("2");
        contactDao.add(contact);

        Contact contact2 = new Contact();
        contact2.setFirstName("Алексей");
        contact2.setLastName("А");
        contact2.setPhone("3");
        contactDao.add(contact2);
    }

    @Test
    public void testDeleteOneContact() {
        Assert.assertThat(contactDao.getAllContacts().size(), is(3));
        contactDao.delete(new int[]{1});
        Assert.assertThat(contactDao.getAllContacts().size(), is(2));
    }

    @Test
    public void testDeleteSomeContacts() {
        Assert.assertThat(contactDao.getAllContacts().size(), is(3));
        contactDao.delete(new int[]{1, 2});
        Assert.assertThat(contactDao.getAllContacts().size(), is(1));
    }

    @Test
    public void testDeleteWithNonexistentContactId() {
        Assert.assertThat(contactDao.getAllContacts().size(), is(3));
        contactDao.delete(new int[]{25});
        Assert.assertThat(contactDao.getAllContacts().size(), is(3));
    }

    @AfterClass
    public static void completeTests() {
        System.out.println("Tests finished");
    }
}




























