package project.bayaraja.application.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.bayaraja.application.services.user.UserEntity;
import project.bayaraja.application.services.user.UserRepository;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByPhoneNumber(username).orElseThrow(
                () -> new UsernameNotFoundException("Username not found")
        );

        return new User(
                user.getPhone_number(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}
