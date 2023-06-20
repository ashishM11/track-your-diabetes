package com.tyd.user.module.mapper;

import com.tyd.user.module.dto.UserRequestDTO;
import com.tyd.user.module.dto.UserResponseDTO;
import com.tyd.user.module.dto.UserRolePrivilegeRequestResponseDTO;
import com.tyd.user.module.dto.UserRoleRequestResponseDTO;
import com.tyd.user.module.model.User;
import com.tyd.user.module.model.UserRole;
import com.tyd.user.module.model.UserRolePrivilege;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Mapper
public interface UserMapper {


    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "password.password", target = "password.encryptedPassword")
    User fromUserRequestDTO(UserRequestDTO requestDTO);


    @Mapping(source = "user.password", target = "password")
    @Mapping(source = "user.userRoles", target = "userRoles", qualifiedByName = "setUserRole")
    UserResponseDTO fromUserModelToResponseDTO(User user);

    @Named("setUserRole")
    @Mapping(source = "userRole.userRolePrivileges",target = "privileges")
    default UserRoleRequestResponseDTO setUserRole(UserRole userRole) {
        Collection<UserRolePrivilegeRequestResponseDTO> userRolePrivilegeRequestResponseDTOS = new LinkedHashSet<>();
        for(UserRolePrivilege userRolePrivilege :userRole.getUserRolePrivileges()){
            userRolePrivilegeRequestResponseDTOS.add(setUserRolePrivilege(userRolePrivilege));
        }
        return new UserRoleRequestResponseDTO(userRole.getUserRoleId(), userRole.getUserRoleName(),userRolePrivilegeRequestResponseDTOS);
    }

    default UserRolePrivilegeRequestResponseDTO setUserRolePrivilege(UserRolePrivilege userRolePrivilege){
        return new UserRolePrivilegeRequestResponseDTO(userRolePrivilege.getUserRolePrivilegeId(), userRolePrivilege.getUserRolePrivilegeName());
    }

    Set<UserResponseDTO> fromUserModelsToUserResponseDTOs(Set<User> users);

}
