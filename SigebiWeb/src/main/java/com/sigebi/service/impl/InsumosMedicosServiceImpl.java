package com.sigebi.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sigebi.dao.IInsumosMedicosDao;
import com.sigebi.dao.IMedicamentosDao;
import com.sigebi.entity.EnfermedadesCie10;
import com.sigebi.entity.InsumosMedicos;
import com.sigebi.entity.Personas;
import com.sigebi.service.InsumosMedicosService;
import com.sigebi.util.exceptions.SigebiException;


@Service
public class InsumosMedicosServiceImpl implements InsumosMedicosService{


	@Autowired
	private IInsumosMedicosDao insumosMedicosDao;
	
	public InsumosMedicosServiceImpl(IInsumosMedicosDao insumosMedicosDao) {
        this.insumosMedicosDao = insumosMedicosDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<InsumosMedicos> listar() {
		return (List<InsumosMedicos>) insumosMedicosDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) insumosMedicosDao.count();
	}
	
	@Override
	@Transactional(readOnly = true)
	public InsumosMedicos obtener(int id) {
		return insumosMedicosDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public InsumosMedicos guardar(InsumosMedicos insumoMedico) {
		return insumosMedicosDao.save(insumoMedico);
	}
	
	@Override
	@Transactional
	public InsumosMedicos actualizar(InsumosMedicos insumoMedico) throws SigebiException {
		
		if ( insumoMedico.getInsumoMedicoId() == null ) {
			throw new SigebiException.BusinessException("insumo medico id es requerido ");
		}
		
		InsumosMedicos insumoActual = obtener(insumoMedico.getInsumoMedicoId());		
		
		if ( insumoActual == null ) {
			String mensaje = "Error: no se pudo editar, el insumo ID: "
					.concat(String.valueOf(insumoMedico.getInsumoMedicoId()).concat(" no existe en la base de datos!"));
			throw new SigebiException.DataNotFound(mensaje);
		}
		
		return insumosMedicosDao.save(insumoMedico);
	}

	@Override
	@Transactional
	public void eliminar(int id) {
		insumosMedicosDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<InsumosMedicos> buscar(Date fromDate, Date toDate, InsumosMedicos insumo, String orderBy, String orderDir, Pageable pageable){
		
		List<InsumosMedicos> insumosList;
		
		Specification<InsumosMedicos> insumoSpec = (Specification<InsumosMedicos>) (root, cq, cb) -> {		
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( insumo.getInsumoMedicoId() != null ) {
                p = cb.and(p, cb.equal(root.get("insumoMedicoId"), insumo.getInsumoMedicoId()) );
            }
            if (!StringUtils.isEmpty(insumo.getCodigo())) {
                p = cb.and(p, cb.like(cb.lower(root.get("codigo")), "%" + insumo.getCodigo().toLowerCase() + "%"));
            }
            if (!StringUtils.isEmpty(insumo.getNombre())) {
                p = cb.and(p, cb.like(cb.lower(root.get("nombre")), "%" + insumo.getNombre().toLowerCase() + "%"));
            }
            if (!StringUtils.isEmpty(insumo.getCaracteristicas())) {
                p = cb.and(p, cb.like(cb.lower(root.get("caracteristicas")), "%" + insumo.getCaracteristicas().toLowerCase() + "%"));
            }
            String orden = "insumoMedicoId";
            if (!StringUtils.isEmpty(orderBy)) {
            	orden = orderBy;
            }
            if("asc".equalsIgnoreCase(orderDir)){
            	cq.orderBy(cb.asc(root.get(orden)));
            }else {
            	cq.orderBy(cb.desc(root.get(orden)));
            }
            return p;
        };
        if(pageable != null) {
        	insumosList = insumosMedicosDao.findAll(insumoSpec, pageable).getContent();			
		}else {
			insumosList = insumosMedicosDao.findAll(insumoSpec);
		}
        return insumosList;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<InsumosMedicos> buscarNoPaginable(Date fromDate, Date toDate, InsumosMedicos insumo) {
		List<InsumosMedicos> insumosList = insumosMedicosDao.findAll((Specification<InsumosMedicos>) (root, cq, cb) -> {
            
			Predicate p = cb.conjunction();
			if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( insumo.getInsumoMedicoId() != null ) {
                p = cb.and(p, cb.equal(root.get("insumoMedicoId"), insumo.getInsumoMedicoId()) );
            }
            if (!StringUtils.isEmpty(insumo.getCodigo())) {
                p = cb.and(p, cb.like(cb.lower(root.get("codigo")), "%" + insumo.getCodigo().toLowerCase() + "%"));
            }
            if (!StringUtils.isEmpty(insumo.getNombre())) {
                p = cb.and(p, cb.like(cb.lower(root.get("nombre")), "%" + insumo.getNombre().toLowerCase() + "%"));
            }
            if (!StringUtils.isEmpty(insumo.getCaracteristicas())) {
                p = cb.and(p, cb.like(cb.lower(root.get("caracteristicas")), "%" + insumo.getCaracteristicas().toLowerCase() + "%"));
            }
         
            cq.orderBy(cb.desc(root.get("insumoMedicoId")));
            return p;
        });
        return insumosList;
    }

}
