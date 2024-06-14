package com.journalApp.service;
import com.journalApp.entity.User;
import com.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserDetailsServiceImplTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private UserRepository userRepository;

@Disabled
    @Test()
    void loadUserByUsernameTest() {
        User mockUser = User.builder()
                .userName("ram")
                .password("infifi")
                .roles(new ArrayList<>())
                .build();

        when(userRepository.findByUserName("A")).thenReturn(mockUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername("A");

        assertEquals("ram", userDetails.getUsername());
        assertEquals("infifi", userDetails.getPassword());
    }
}
