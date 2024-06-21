package com.journalApp.Scheduler;

import com.journalApp.entity.JournalEntry;
import com.journalApp.entity.User;
import com.journalApp.enums.Sentiment;
import com.journalApp.repository.UserRepositoryImpl;
import com.journalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoPeriod;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
    public class UserScheduler {
        @Autowired
        private EmailService emailService;
        @Autowired
        private UserRepositoryImpl userRepository;
        @Autowired
        private SentimentAnalysisService sentimentAnalysisService;


        @Scheduled
        public void featchUsersAndSendSaMail(){
            List<User> users = userRepository.getUserForSA();
            for (User user:users){
                List<JournalEntry> journalEntries = user.getJournalEntries();
                List<Sentiment> sentiments = journalEntries.stream().filter(x-> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoPeriod.DAYS))).map(x->x.getDescription()).collect(Collectors.toList());
                Map<Sentiment,Integer>sentimentCounts = new HashMap<>();
                for (Sentiment sentiment : sentiments){
                    if (sentiment != null)
                        sentimentCounts.put(sentiment,sentimentCounts.getOrDefault(sentiment,0)+1);
                }
                Sentiment mostFrequentSentiment = null;
                int maxCount = 0;
                for (Map.Entry<Sentiment , Integer>entry:sentimentCounts.entrySet()){
                    if (entry.getValue() > maxCount){
                        maxCount = entry.getValue();
                        mostFrequentSentiment = entry.getKey();
                    }
                }
                if (mostFrequentSentiment != null){
                    emailService.sendEmail(user.getEmail(), "sentiment for last 7 days" ,mostFrequentSentiment.toString());
                }



            }
        }


    }
