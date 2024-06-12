package project.bayaraja.application.services.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import project.bayaraja.application.exceptions.BadRequestException;
import project.bayaraja.application.exceptions.DataNotFoundException;
import project.bayaraja.application.services.auth.interfaces.AuthService;
import project.bayaraja.application.services.auth.request.LoginRequest;
import project.bayaraja.application.services.auth.request.RegisterRequest;
import project.bayaraja.application.services.lookups.interfaces.LookupRepository;
import project.bayaraja.application.services.user.UserEntity;
import project.bayaraja.application.services.user.interfaces.UserRepository;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LookupRepository lookupRepository;
    private final ModelMapper modelMapper;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final AuthenticationManager authenticationManager;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @Override @Transactional
    public UserEntity registerUser(RegisterRequest bodyDto) {
        Optional<UserEntity> userPhoneNumber = this.userRepository.findByUsername(bodyDto.getUsername());

        this.checkUsername(userPhoneNumber);

        if(bodyDto.getStudent() != null){
            this.lookupRepository
                    .findLookupByTypeAndKey("MAJOR_CLASS", bodyDto.getStudent().getMajor())
                    .orElseThrow(() -> new BadRequestException("Type or Key is not define in database"));
        }

        bodyDto.setPassword(this.passwordEncoder.encode(bodyDto.getPassword()));

        UserEntity savingData = this.modelMapper.map(bodyDto, UserEntity.class);

        return this.userRepository.save(savingData);
    }

    @Override
    public void login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken
                .unauthenticated(
                        loginRequest.getUsername(), loginRequest.getPassword()
                );

        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();

        context.setAuthentication(authentication); //set context application from authentication
        securityContextHolderStrategy.setContext(context);

        System.out.println(securityContextHolderStrategy.getContext());

        securityContextRepository.saveContext(context, request, response); //save the auth context
    }

    @Override @Transactional
    public UserEntity verifiedUser(Integer userId) {
        UserEntity userData = this.userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User id not found"));

        this.acceptUser(userData);

        return userData;
    }

    @Override
    public UserEntity decodeSession() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getPrincipal().toString();

        return userRepository.findByUsername(username).orElseThrow(
                () -> new BadCredentialsException("Username is not found.")
        );
    }

    //----------- UTILS
    private void acceptUser(UserEntity dataUser){
        if(dataUser.getIs_verified()) throw new BadRequestException(String.format("User has verified at %s", dataUser.getVerified_at()));

        dataUser.setIs_verified(true);
        dataUser.setVerified_at(new Date());
    }

    private void checkUsername(Optional<UserEntity> dataUser){
        if(dataUser.isPresent()) throw new IllegalArgumentException("Phone number already in used");
    }
}
