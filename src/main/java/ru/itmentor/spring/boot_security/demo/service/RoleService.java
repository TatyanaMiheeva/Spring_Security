package ru.itmentor.spring.boot_security.demo.service;

import ru.itmentor.spring.boot_security.demo.entity.Role;

import java.util.Set;

public interface RoleService {
    Set<Role> getAllRoles();

    Set<Role> getRoleByName(String[] roleName);
}
