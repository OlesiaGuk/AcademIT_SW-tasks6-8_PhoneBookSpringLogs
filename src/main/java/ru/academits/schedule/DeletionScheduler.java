package ru.academits.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.academits.dao.ContactDao;
import ru.academits.model.Contact;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class DeletionScheduler {
    private final ContactDao contactDao;

    public DeletionScheduler(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    @Scheduled(fixedRate = 10000)
    public void deleteRandomContact() {
        if (contactDao.getAllContacts().size() > 0) {
            List<Integer> idsList = contactDao.getAllContacts().stream().map(Contact::getId).collect(Collectors.toList());
            Random rand = new Random();
            int randomContactId = idsList.get(rand.nextInt(idsList.size()));

            contactDao.delete(new int[]{randomContactId});

            System.out.println("Удален контакт с id = " + randomContactId);
        }
    }
}
