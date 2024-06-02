package com.journalApp.JournalApp.service;
import com.journalApp.JournalApp.entity.JournalEntry;
import com.journalApp.JournalApp.entity.User;
import com.journalApp.JournalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {
    @Autowired
 private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;

@Transactional
    public void saveEntity(JournalEntry journalEntry, String userName){
    try {
        User user = userService.findByUserName(userName);
        JournalEntry saved=journalEntryRepository.save(journalEntry);
        user.getJournalEntries().add(saved);
        userService.saveUser(user);
    }catch (Exception e){
        System.out.println(e);
        throw  new RuntimeException("AN error occurred while saving the entry");
    }
    }
    public void saveEntity(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }
   public List<JournalEntry> getAll(){
         return journalEntryRepository.findAll();
   }
    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepository.findById(id);
    }
    @Transactional
    public boolean deleteById(ObjectId id, String userName){
    boolean removed=false;
    try {
        User user = userService.findByUserName(userName);
         removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
         if (removed) {
             userService.saveUser(user);
             journalEntryRepository.deleteById(id);
         }

    }catch (Exception e){
        System.out.println(e);
        throw  new RuntimeException("An error occurred While deleting the entry.", e);
    }
    return removed;
}

}
