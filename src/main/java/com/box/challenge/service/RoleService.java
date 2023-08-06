package com.box.challenge.service;

import com.box.challenge.model.Role;
import com.box.challenge.repository.RoleRepository;
import org.springframework.stereotype.Service;

/**
 * = RoleServiceImpl TODO Auto-generated class documentation
 */
@Service
public class RoleService extends GenericService<Role> {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository;
    }


}
