package com.IdentityRegistry.IdentityRegistry.controller;

import com.IdentityRegistry.IdentityRegistry.entity.Roles;
import com.IdentityRegistry.IdentityRegistry.repository.RoleRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/role")
public class RoleController {
    RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostMapping
    public String addRole(@RequestParam String roleName){
        Roles roles = Roles.builder()
                .roleName(roleName)
                .build();
        roleRepository.save(roles);
        return "Role successfully added";
    }
}
