package com.sigebi.security.repository;

import com.sigebi.security.entity.Rol;
import com.sigebi.security.enums.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByRolNombre(RolNombre rolNombre);
    List<Rol> findByEstado(String estado);
}
