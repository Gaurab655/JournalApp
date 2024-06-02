package com.journalApp.JournalApp.Controller;
import com.journalApp.JournalApp.entity.JournalEntry;
import com.journalApp.JournalApp.entity.User;
import com.journalApp.JournalApp.service.JournalEntryService;
import com.journalApp.JournalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class journalEntryController {
    @Autowired
    public JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createEntity(@RequestBody JournalEntry myEntry) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryService.saveEntity(myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to save journal entry: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
       User user = userService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if (all!=null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No journal entries found", HttpStatus.NO_CONTENT);
        }
    }
    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);

        List<JournalEntry> filteredEntries = user.getJournalEntries().stream()
                .filter(entry -> entry.getId().equals(myId))
                .collect(Collectors.toList());

        if (!filteredEntries.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
      boolean removed =journalEntryService.deleteById(myId,userName);
      if (removed){
          return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateById(@PathVariable ObjectId myId ,
                                        @RequestBody JournalEntry newEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);
        List<JournalEntry> collect = user.getJournalEntries().stream()
                .filter(x -> x.getId().equals(myId))
                .collect(Collectors.toList());
        if (!collect.isEmpty()){
            Optional <JournalEntry> journalEntry = journalEntryService.findById(myId);
            if (journalEntry.isPresent()){
                JournalEntry old =journalEntry.get();
                old.setTitle(newEntry.getTitle()!=null && !newEntry.getTitle().equals("")? newEntry.getTitle() : old.getTitle());
                old.setDescription(newEntry.getDescription()!=null && !newEntry.equals("")? newEntry.getDescription() : old.getDescription());
                journalEntryService.saveEntity(old);
                return new ResponseEntity<>(old,HttpStatus.OK);

            }
        }
return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
