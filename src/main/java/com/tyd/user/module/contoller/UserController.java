package com.tyd.user.module.contoller;

import com.tyd.user.module.dto.ChangePasswordRequestDTO;
import com.tyd.user.module.dto.UserRequestDTO;
import com.tyd.user.module.dto.UserResponseDTO;
import com.tyd.user.module.dto.UserSignInRequestDTO;
import com.tyd.user.module.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @GetMapping("/find")
    public ResponseEntity<UserResponseDTO> getUserEmailOrMobile(@RequestParam(name = "userEmailOrMobile")String userEmailOrMobile){
        return new ResponseEntity<>(userService.getUserByEmailOrMobile(userEmailOrMobile), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerNewUser(@Valid @RequestBody UserRequestDTO userRequestDTO) throws MessagingException {
        return new ResponseEntity<>(userService.createUser(userRequestDTO),HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<UserResponseDTO> findUserByItsId(@PathVariable(name = "userId")  Long userId){
        return new ResponseEntity<>(userService.findUserByItsId(userId),HttpStatus.OK);
    }

    @PostMapping("/auth")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody UserSignInRequestDTO userSignInRequestDTO){
        return new ResponseEntity<>(userService.authenticate(userSignInRequestDTO),HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Set<UserResponseDTO>> findAllUsers(){
        return new ResponseEntity<>(userService.findAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/forgetPassword")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Boolean> forgetPassword(@Valid @RequestParam(name = "userEmailOrMobile") String  userEmailOrMobile){
        return new ResponseEntity<>(userService.forgetPassword(userEmailOrMobile),HttpStatus.OK);
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO){
        return new ResponseEntity<>(userService.changePasswordRequest(changePasswordRequestDTO),HttpStatus.OK);
    }

}
