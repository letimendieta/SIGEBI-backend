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

import com.sigebi.dao.IMedicamentosDao;
import com.sigebi.entity.Medicamentos;
import com.sigebi.service.MedicamentosService;
import com.sigebi.util.exceptions.SigebiException;


@Service
public class MedicamentosServiceImpl implements MedicamentosService{

	@Autowired
	private IMedicamentosDao medicamentosDao;
	
	public MedicamentosServiceImpl(IMedicamentosDao medicamentosDao) {
        this.medicamentosDao = medicamentosDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Medicamentos> listar() {
		return (List<Medicamentos>) medicamentosDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) medicamentosDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public Medicamentos obtener(int id) {
		return medicamentosDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Medicamentos guardar(Medicamentos insumo) {
		return medicamentosDao.save(insumo);
	}
	
	@Override
	@Transactional
	public Medicamentos actualizar(Medicamentos medicamento) throws SigebiException {
		
		if ( medicamento.getMedicamentoId() == null ) {
			throw new SigebiException.BusinessException("medicamento id es requerido ");
		}
		
		Medicamentos medicamentoActual = obtener(medicamento.getMedicamentoId());		
		
		if ( medicamentoActual == null ) {
			String mensaje = "Error: no se pudo editar, el medicamento ID: "
					.concat(String.valueOf(medicamento.getMedicamentoId()).concat(" no existe en la base de datos!"));
			throw new SigebiException.DataNotFound(mensaje);
		}
		
		return medicamentosDao.save(medicamento);
	}

	@Override
	@Transactional
	public void eliminar(int id) {
		medicamentosDao.deleteById(id);
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public List<Medicamentos> buscar(Date fromDate, Date toDate, Medicamentos medicamento, String orderBy, String orderDir, Pageable pageable){
		List<Medicamentos> medicamentosList;
		
		Specification<Medicamentos> medicamentoSpec = (Specification<Medicamentos>) (root, cq, cb) -> {
			
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( medicamento.getMedicamentoId() != null ) {
                p = cb.and(p, cb.equal(root.get("medicamentoId"), medicamento.getMedicamentoId()) );
            }
            if (!StringUtils.isEmpty(medicamento.getCodigo())) {
                p = cb.and(p, cb.like(cb.lower(root.get("codigo")), "%" + medicamento.getCodigo().toLowerCase() + "%"));
            }
            if (!StringUtils.isEmpty(medicamento.getMedicamento())) {
                p = cb.and(p, cb.like(cb.lower(root.get("medicamento")), "%" + medicamento.getMedicamento().toLowerCase() + "%"));
            }
           
            String orden = "medicamentoId";
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
        	medicamentosList = medicamentosDao.findAll(medicamentoSpec, pageable).getContent();			
		}else {
			medicamentosList = medicamentosDao.findAll(medicamentoSpec);
		}
        return medicamentosList;
    }

	@Override
	@Transactional(readOnly = true)
	public List<Medicamentos> buscarNoPaginable(Date fromDate, Date toDate, Medicamentos medicamento) {
		List<Medicamentos> medicamentosList = medicamentosDao.findAll((Specification<Medicamentos>) (root, cq, cb) -> {
            
			Predicate p = cb.conjunction();
			if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( medicamento.getMedicamentoId() != null ) {
                p = cb.and(p, cb.equal(root.get("medicamentoId"), medicamento.getMedicamentoId()) );
            }
            if (!StringUtils.isEmpty(medicamento.getCodigo())) {
                p = cb.and(p, cb.like(cb.lower(root.get("codigo")), "%" + medicamento.getCodigo().toLowerCase() + "%"));
            }
            if (!StringUtils.isEmpty(medicamento.getMedicamento())) {
                p = cb.and(p, cb.like(cb.lower(root.get("medicamento")), "%" + medicamento.getMedicamento().toLowerCase() + "%"));
            }
         
            cq.orderBy(cb.desc(root.get("medicamentoId")));
            return p;
        });
        return medicamentosList;
    }
}
