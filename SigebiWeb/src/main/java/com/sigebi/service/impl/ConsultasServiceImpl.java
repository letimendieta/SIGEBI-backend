package com.sigebi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sigebi.clases.ConsultasResult;
import com.sigebi.clases.DiagnosticosResult;
import com.sigebi.dao.IConsultasDao;
import com.sigebi.entity.Anamnesis;
import com.sigebi.entity.Areas;
import com.sigebi.entity.Consultas;
import com.sigebi.entity.EnfermedadesCie10;
import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.MotivosConsulta;
import com.sigebi.entity.Personas;
import com.sigebi.security.entity.Usuario;
import com.sigebi.security.service.UsuarioRolService;
import com.sigebi.service.ConsultasService;
import com.sigebi.service.EnfermedadesCie10Service;
import com.sigebi.service.FuncionariosService;
import com.sigebi.service.UsuariosService;
import com.sigebi.util.Globales;
import com.sigebi.util.exceptions.SigebiException;


@Service
public class ConsultasServiceImpl implements ConsultasService{

	@Autowired
	private IConsultasDao consultasDao;
	@Autowired
	private EnfermedadesCie10Service enfermedadesCie10Service;
	@Autowired
	private FuncionariosService funcionariosService;
	@Autowired
	private UsuariosService usuariosService;
	@Autowired
	private UsuarioRolService usuarioRolService;
	
	public ConsultasServiceImpl(IConsultasDao consultasDao) {
        this.consultasDao = consultasDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Consultas> listar() {
		return (List<Consultas>) consultasDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) consultasDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public Consultas obtener(int id) {
		return consultasDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Consultas guardar(Consultas cliente) {
		return consultasDao.save(cliente);
	}
	
	@Override
	@Transactional
	public Consultas actualizar(Consultas cliente) {
		return consultasDao.save(cliente);
	}

	@Override
	@Transactional
	public void eliminar(int id) {
		consultasDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Consultas> buscar(Date fromDate, Date toDate, Consultas consulta, 
			String orderBy, String orderDir, Pageable pageable) throws DataAccessException{
		List<Consultas> consultasList;
		
		Specification<Consultas> consultasSpec = (Specification<Consultas>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if ( consulta.getFecha() != null ) {
                p = cb.and(p, cb.equal( root.get("fecha"), consulta.getFecha()) );
            }
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( consulta.getDiagnosticos() != null && consulta.getDiagnosticos().getDiagnosticoId() != null ) {
                p = cb.and(p, cb.equal(root.get("diagnosticoId"), consulta.getDiagnosticos().getDiagnosticoId()) );
            }
            if ( consulta.getTratamientos() != null && consulta.getTratamientos().getTratamientoId() != null ) {
                p = cb.and(p, cb.equal(root.get("tratamientoId"), consulta.getTratamientos().getTratamientoId()) );
            }
            if ( consulta.getConsultaId() != null ) {
                p = cb.and(p, cb.equal(root.get("consultaId"), consulta.getConsultaId()) );
            }
            if( consulta.getPacienteId() != null ){
            	p = cb.and(p, cb.equal(root.get("pacienteId"), consulta.getPacienteId()) );
            }
                        
            String orden = "consultaId";
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
			consultasList = consultasDao.findAll(consultasSpec, pageable).getContent();			
		}else {
			consultasList = consultasDao.findAll(consultasSpec);
		}
        
        return consultasList;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<ConsultasResult> buscarConsultas(Date fromDate, Date toDate, Consultas consulta, String orderBy, String orderDir, Pageable pageable) throws SigebiException {
		List<Consultas> consultasList = new ArrayList<Consultas>();
		ConsultasResult consultaResult = null;
		List<ConsultasResult> consultasListResult = new ArrayList<ConsultasResult>();
		
		try {
			if(consulta.getFuncionarios().getFuncionarioId() == null) {
				throw new SigebiException.BusinessException("El funcionario es requerido ");
			}
			
			Funcionarios funcionarioConsulta = funcionariosService.findById(consulta.getFuncionarios().getFuncionarioId());
			
			Usuario usuario = usuariosService.findByFuncionario(funcionarioConsulta);
			
			List<String> usuarioRol = usuarioRolService.listarRolesUsuario(usuario);
					
			if( !usuarioRol.contains( Globales.ROL_ADMIN) ) {
				Areas area = new Areas();
				area = funcionarioConsulta.getAreas();				
				consulta.setAreas(area);
			}
			consulta.setFuncionarios(null);
			
			consultasList = buscar(fromDate, toDate, consulta, orderBy, orderDir, pageable);
			
			for(Consultas consultaBusqueda : consultasList) {
				consultaResult = new ConsultasResult();
				
				consultaResult.setConsultaId(consultaBusqueda.getConsultaId());
				consultaResult.setFecha(consultaBusqueda.getFecha());
				consultaResult.setPacienteId(consultaBusqueda.getPacienteId());
				consultaResult.setFechaCreacion(consultaBusqueda.getFechaCreacion());
				consultaResult.setUsuarioCreacion(consultaBusqueda.getUsuarioCreacion());
				consultaResult.setFechaModificacion(consultaBusqueda.getFechaModificacion());
				consultaResult.setUsuarioModificacion(consultaBusqueda.getUsuarioModificacion());
				consultaResult.setAreas(consultaBusqueda.getAreas());
				consultaResult.setTratamientos(consultaBusqueda.getTratamientos());
				consultaResult.setFuncionarios(consultaBusqueda.getFuncionarios());
				consultaResult.setAnamnesis(consultaBusqueda.getAnamnesis());
				
				if ( consultaBusqueda.getDiagnosticos() != null ) {
					DiagnosticosResult diagnosticoResult = new DiagnosticosResult();
					
					diagnosticoResult.setDiagnosticoId(consultaBusqueda.getDiagnosticos().getDiagnosticoId());	
					diagnosticoResult.setDiagnosticoPrincipal(consultaBusqueda.getDiagnosticos().getDiagnosticoPrincipal());
					diagnosticoResult.setDiagnosticoSecundario(consultaBusqueda.getDiagnosticos().getDiagnosticoSecundario());					
					diagnosticoResult.setFechaCreacion(consultaBusqueda.getDiagnosticos().getFechaCreacion());
					diagnosticoResult.setUsuarioCreacion(consultaBusqueda.getDiagnosticos().getUsuarioCreacion());
					diagnosticoResult.setFechaModificacion(consultaBusqueda.getDiagnosticos().getFechaModificacion());
					diagnosticoResult.setUsuarioModificacion(consultaBusqueda.getDiagnosticos().getUsuarioModificacion());
					
					consultaResult.setDiagnosticos(diagnosticoResult);
				}				
				
				if( consultaBusqueda.getDiagnosticos() != null
						&& consultaBusqueda.getDiagnosticos().getEnfermedadCie10PrimariaId() == null ) {
					consultaResult.getDiagnosticos().setEnfermedadCie10Primaria(new EnfermedadesCie10());
				}else if( consultaBusqueda.getDiagnosticos() != null
						&& consultaBusqueda.getDiagnosticos().getEnfermedadCie10PrimariaId() != null ) {
					EnfermedadesCie10 cie10 = enfermedadesCie10Service.findById(consultaBusqueda.getDiagnosticos().getEnfermedadCie10PrimariaId());
					
					consultaResult.getDiagnosticos().setEnfermedadCie10Primaria(cie10);
				}
				
				if( consultaBusqueda.getDiagnosticos() != null
						&& consultaBusqueda.getDiagnosticos().getEnfermedadCie10SecundariaId() == null ) {
					consultaResult.getDiagnosticos().setEnfermedadCie10Secundaria(new EnfermedadesCie10());
				}else if( consultaBusqueda.getDiagnosticos() != null
						&& consultaBusqueda.getDiagnosticos().getEnfermedadCie10SecundariaId() != null ) {
					EnfermedadesCie10 cie10 = enfermedadesCie10Service.findById(consultaBusqueda.getDiagnosticos().getEnfermedadCie10SecundariaId());
					
					consultaResult.getDiagnosticos().setEnfermedadCie10Secundaria(cie10);
				}
				
				if( consultaResult.getAnamnesis() == null ) {
					consultaResult.setAnamnesis(new Anamnesis());
				}
				if( consultaResult.getAnamnesis() != null
						&& consultaResult.getAnamnesis().getMotivoConsulta() == null ) {
					consultaResult.getAnamnesis().setMotivoConsulta(new MotivosConsulta());
				}
				
				if( consultaResult.getFuncionarios() == null ) {
					consultaResult.setFuncionarios(new Funcionarios());
					consultaResult.getFuncionarios().setPersonas(new Personas());
				}
				
				consultasListResult.add(consultaResult);
			}
		} catch (Exception e) {
			throw new SigebiException("Error al buscar las consultas " + e.getMessage());
		}
		
		
		return consultasListResult;
	}

}
