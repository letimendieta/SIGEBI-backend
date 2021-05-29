package com.sigebi.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigebi.dao.ICitasDao;
import com.sigebi.dao.IProcedimientosDao;
import com.sigebi.entity.Citas;
import com.sigebi.entity.Procedimientos;
import com.sigebi.service.CitasService;
import com.sigebi.util.Globales;
import com.sigebi.util.exceptions.SigebiException;


@Service
public class CitasServiceImpl implements CitasService{

	@Autowired
	private ICitasDao citasDao;
	@Autowired
	private IProcedimientosDao procedimientosDao;
	
	public CitasServiceImpl(ICitasDao citasDao) {
        this.citasDao = citasDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Citas> findAll() {
		return (List<Citas>) citasDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Citas findById(int id) {
		return citasDao.findById(id).orElse(null);
	}

	@Transactional
	public Citas guardar(Citas cita) throws Exception {
		Citas citaSave = citasDao.save(cita);
		
		//Guardar procedimiento
		/*try {			
						
			Procedimientos procedimientoEntity = new Procedimientos();
			procedimientoEntity.setFuncionarios(citaSave.getFuncionarios());			
			procedimientoEntity.setPacientes(citaSave.getPacientes());
			procedimientoEntity.setUsuarioCreacion(citaSave.getUsuarioCreacion());
			procedimientoEntity.setCita(citaSave);
			//procedimientoEntity.setFecha(LocalDateTime.now());
			procedimientoEntity.setAreas(citaSave.getAreas());
			procedimientoEntity.setMotivoConsulta(citaSave.getMotivoConsulta());
			
			procedimientoEntity.setEstado(Globales.Estados.PENDIENTE);
			
			Procedimientos procedimiento = procedimientosDao.save(procedimientoEntity);
			
		} catch (Exception e) {
			throw new SigebiException.InternalServerError("Error al guardar el procedimiento " + e.getMessage());
		}*/
		
		return citaSave;
	}
	
	@Transactional
	public Citas actualizar(Citas cita) throws Exception {
						
		return citasDao.save(cita);
	}

	@Override
	@Transactional
	public void delete(int id) {
		citasDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Citas> buscar(Date fromDate, Date toDate, 
										Citas cita, 
										List<Integer> funcionariosId,
										List<Integer> pacientesId,
										Pageable pageable) {
		List<Citas> CitasList = citasDao.findAll((Specification<Citas>) (root, cq, cb) -> {
            
			Predicate p = cb.conjunction();
            if( funcionariosId != null && !funcionariosId.isEmpty() ){
            	p = cb.and(root.get("funcionarios").in(funcionariosId));
            }     
            if( pacientesId != null && !pacientesId.isEmpty() ){
            	p = cb.and(root.get("pacientes").in(pacientesId));
            } 
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( cita.getCitaId() != null ) {
                p = cb.and(p, cb.equal( root.get("citaId"), cita.getCitaId()) );
            }
            if ( cita.getAreas() != null && cita.getAreas().getAreaId() != null ) {
                p = cb.and(p, cb.equal( root.get("areas"), cita.getAreas().getAreaId()) );
            }
            if ( cita.getFecha() != null ) {
                p = cb.and(p, cb.equal( root.get("fecha"), cita.getFecha()) );
            }
            if ( cita.getHora() != null ) {
                p = cb.and(p, cb.equal( root.get("hora"), cita.getHora()) );
            }
            if ( cita.getEstado() != null ) {
                p = cb.and(p, cb.equal( root.get("estado"), cita.getEstado()) );
            }
            cq.orderBy(cb.desc( root.get("citaId")) );
            return p;
        }, pageable).getContent();
        return CitasList;
    }
	
}
