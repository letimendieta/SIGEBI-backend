package com.sigebi.security.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sigebi.entity.Areas;
import com.sigebi.security.entity.Rol;
import com.sigebi.security.enums.RolNombre;
import com.sigebi.security.repository.RolRepository;

@Service
@Transactional
public class RolService {

    @Autowired
    RolRepository rolRepository;

    public Optional<Rol> getByRolNombre(RolNombre rolNombre){
        return rolRepository.findByRolNombre(rolNombre);
    }
    
    public List<Rol> listar(){
        return rolRepository.findAll();
    }

    public void save(Rol rol){
        rolRepository.save(rol);
    }
    
	/*@Transactional(readOnly = true)
	public List<Rol> buscar(Rol rol, 
			String orderBy, String orderDir, Pageable pageable) throws DataAccessException{
		List<Rol> rolList;
		
		Specification<Rol> rolSpec = (Specification<Rol>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            
            if (!StringUtils.isEmpty(rol.getEstado())) {
                p = cb.and(p, cb.like(root.get("estado"), "%" + rol.getEstado() + "%"));
            }
                        
            String orden = "rolNombre";
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
			rolList = rolRepository.findAll(rolSpec, pageable).getContent();			
		}else {
			rolList = rolRepository.findAll(rolSpec);
		}
        
        return rolList;
    }*/
}
