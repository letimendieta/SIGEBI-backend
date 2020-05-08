package com.sigebi.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sigebi.model.Roles;

public interface IRolesRepo extends JpaRepository<Roles, Integer> {

}
