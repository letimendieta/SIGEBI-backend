package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.HorariosDisponibles;

public interface IHorariosDisponiblesDao extends JpaRepository<HorariosDisponibles, Integer>, JpaSpecificationExecutor<HorariosDisponibles>  {

}
