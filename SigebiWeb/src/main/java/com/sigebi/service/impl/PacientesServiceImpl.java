package com.sigebi.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigebi.dao.IPacientesDao;
import com.sigebi.entity.Pacientes;
import com.sigebi.service.PacientesService;


@Service
public class PacientesServiceImpl implements PacientesService{

	@Autowired
	private IPacientesDao pacientesDao;
	
	public PacientesServiceImpl(IPacientesDao pacientesDao) {
        this.pacientesDao = pacientesDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Pacientes> findAll() {
		return (List<Pacientes>) pacientesDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Pacientes findById(int id) {
		return pacientesDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Pacientes save(Pacientes paciente) {
		return pacientesDao.save(paciente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		pacientesDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Pacientes> buscar(Date fromDate, Date toDate, Pacientes paciente, Pageable pageable) {
		
        List<Pacientes> pacientesList = null;/*pacientesDao.findAll((Specification<Pacientes>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if (!StringUtils.isEmpty(paciente.getCedula())) {
                p = cb.and(p, cb.like(root.get("cedula"), "%" + paciente.getCedula() + "%"));
            }
            if (!StringUtils.isEmpty(paciente.getNombres())) {
                p = cb.and(p, cb.like(root.get("nombres"), "%" + paciente.getNombres() + "%"));
            }
            if (!StringUtils.isEmpty(paciente.getApellidos())) {
                p = cb.and(p, cb.like(root.get("apellidos"), "%" + paciente.getApellidos() + "%"));
            }
            cq.orderBy(cb.desc(root.get("pacienteId")));
            return p;
        }, pageable).getContent();*/
        return pacientesList;
    }

}
