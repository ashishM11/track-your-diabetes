package com.tyd.user.module.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Collection;

@Entity
@Table(name = "tblUserRole")
@Data
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRoleId;

    @Column(unique = true,name = "userRoleName", nullable = false)
    private String userRoleName;

    @ToString.Exclude
    @JsonBackReference
    @ManyToMany(mappedBy = "userRoles")
    private Collection<User> users;

    @ToString.Exclude
    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tblUserRoleWithPrivileges",
            joinColumns = @JoinColumn( name = "userRoleId" , referencedColumnName = "userRoleId"),
            inverseJoinColumns = @JoinColumn(name = "userRolePrivilegeId", referencedColumnName = "userRolePrivilegeId")
    )
    private Collection<UserRolePrivilege> userRolePrivileges;

}
