package com.tyd.user.module.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyd.user.module.config.JwtAuthFilter;
import com.tyd.user.module.dto.PasswordRequestDTO;
import com.tyd.user.module.dto.UserRequestDTO;
import com.tyd.user.module.dto.UserResponseDTO;
import com.tyd.user.module.dto.UserSignInRequestDTO;
import com.tyd.user.module.mapper.UserMapper;
import com.tyd.user.module.model.Password;
import com.tyd.user.module.model.User;
import com.tyd.user.module.model.UserRole;
import com.tyd.user.module.model.UserRolePrivilege;
import com.tyd.user.module.service.JwtService;
import com.tyd.user.module.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@AutoConfigureObservability
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private JwtAuthFilter jwtAuthFilter;
    private User user;
    private UserRequestDTO userRequestDTO;
    private UserSignInRequestDTO userSignInRequestDTO;
    private Password password;
    private PasswordRequestDTO passwordRequestDTO;
    private UserRole userRole;

    private UserRolePrivilege userRolePrivilege;

    Set<UserResponseDTO> userList = new HashSet<>();
    UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {

        userRolePrivilege = new UserRolePrivilege();
        userRolePrivilege.setUserRolePrivilegeId(1L);
        userRolePrivilege.setUserRolePrivilegeName("READ_PRIVILEGE");

        userRole = new UserRole();
        userRole.setUserRoleId(1L);
        userRole.setUserRoleName("USER_ROLE");
        userRole.setUserRolePrivileges(Collections.singletonList(userRolePrivilege));

        password = new Password();
        password.setPasswordId(1L);
        password.setEncryptedPassword("test");
        password.setPasswordCreationDT(LocalDate.now());
        user = new User();
        user.setUserId(1L);
        user.setUserCreationDT(LocalDate.now());
        user.setUserEmail("test@gmail.com");
        user.setUserFName("test");
        user.setUserLName("test");
        user.setUserMobile("1234567890");
        user.setUserGender("M");
        user.setUserDOB(LocalDate.now());
        user.setUserAccountEnabled(true);
        user.setUserAccountNonExpired(true);
        user.setUserAccountNonLocked(true);
        user.setUserCredentialsNonExpired(true);
        user.setUserRoles(Collections.singletonList(userRole));
        user.setPassword(password);

        passwordRequestDTO= new PasswordRequestDTO("test","test");
        userRequestDTO = new UserRequestDTO("test","test","1234567890","test@gmail.com","M",LocalDate.now(),passwordRequestDTO);
        userResponseDTO = UserMapper.INSTANCE.fromUserModelToResponseDTO(user);
        userList.add(userResponseDTO);

        userSignInRequestDTO=new UserSignInRequestDTO("test@gamil.com","test");
    }

    @Test
    void getUserEmailOrMobile() throws Exception {
        when(userService.getUserByEmailOrMobile("test@gmail.com")).thenReturn(userResponseDTO);
        this.mockMvc
                .perform(get("/api/v1/user/find").param("userEmailOrMobile","test@gmail.com"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void registerNewUser() throws Exception {
        when(userService.createUser(userRequestDTO)).thenReturn("");
        this.mockMvc
                .perform(post("/api/v1/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findUserByItsId() throws Exception {
        when(userService.findUserByItsId(1L)).thenReturn(userResponseDTO);
        this.mockMvc
                .perform(get("/api/v1/user/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void authenticateUser() throws Exception {
        when(userService.authenticate(userSignInRequestDTO)).thenReturn("");
        this.mockMvc
                .perform(post("/api/v1/user/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSignInRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findAllUsers() throws Exception {
        when(userService.findAllUsers()).thenReturn(userList);
        this.mockMvc
                .perform(get("/api/v1/user/all"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void forgetPassword() throws Exception {
        when(userService.forgetPassword("test@gmail.com")).thenReturn(true);
        this.mockMvc
                .perform(get("/api/v1/user/forgetPassword").param("userEmailOrMobile","test@gmail.com"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}