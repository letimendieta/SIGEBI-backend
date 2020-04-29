package com.sigebi.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sigebi.model.Citas;

public interface ICitasRepo extends JpaRepository<Citas, Integer> {

}
