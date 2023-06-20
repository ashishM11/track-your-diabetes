package com.tyd.user.module.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Collection;

@Entity
@Table(name = "tblUserRolePrivilege")
@Data
public class UserRolePrivilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRolePrivilegeId;

    @Column(nullable = false,unique = true,name = "userRolePrivilegeName")
    private String userRolePrivilegeName;

    @ToString.Exclude
    @ManyToMany(mappedBy = "userRolePrivileges")
    @JsonBackReference
    private Collection<UserRole> userRoles;
}
