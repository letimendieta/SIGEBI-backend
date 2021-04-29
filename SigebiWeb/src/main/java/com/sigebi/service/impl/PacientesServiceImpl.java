package com.sigebi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigebi.clases.FichaMedica;
import com.sigebi.clases.ProcesoPacienteHistorialClinico;
import com.sigebi.dao.IAlergiasDao;
import com.sigebi.dao.IAntecedentesDao;
import com.sigebi.dao.IHistorialClinicoDao;
import com.sigebi.dao.IPacientesDao;
import com.sigebi.dao.IPersonasDao;
import com.sigebi.dao.IPreguntasDao;
import com.sigebi.dao.IPreguntasHistorialDao;
import com.sigebi.dao.IVacunacionesDao;
import com.sigebi.dao.IVacunasDao;
import com.sigebi.entity.Alergenos;
import com.sigebi.entity.Alergias;
import com.sigebi.entity.Antecedentes;
import com.sigebi.entity.Carreras;
import com.sigebi.entity.Departamentos;
import com.sigebi.entity.Dependencias;
import com.sigebi.entity.Estamentos;
import com.sigebi.entity.HistorialClinico;
import com.sigebi.entity.Pacientes;
import com.sigebi.entity.PatologiasProcedimientos;
import com.sigebi.entity.Personas;
import com.sigebi.entity.Preguntas;
import com.sigebi.entity.PreguntasHistorial;
import com.sigebi.entity.Vacunaciones;
import com.sigebi.entity.Vacunas;
import com.sigebi.service.AlergenosService;
import com.sigebi.service.PacientesService;
import com.sigebi.service.PatologiasProcedimientosService;
import com.sigebi.service.PersonasService;
import com.sigebi.service.PreguntasHistorialService;
import com.sigebi.service.PreguntasService;
import com.sigebi.service.UtilesService;
import com.sigebi.service.VacunasService;
import com.sigebi.util.Globales;
import com.sigebi.util.exceptions.SigebiException;


@Service
public class PacientesServiceImpl implements PacientesService{

	@Autowired
	private IPacientesDao pacientesDao;	
	@Autowired
	private IPersonasDao personasDao;	
	@Autowired
	private PersonasService personasService;	
	@Autowired
	private AlergenosService alergenosService;
	@Autowired
	private IAlergiasDao alergiasDao;
	@Autowired
	private PatologiasProcedimientosService patologiasProcedimientosService;
	@Autowired
	private IAntecedentesDao antecedentesDao;
	@Autowired
	private VacunasService vacunasService;
	@Autowired
	private PreguntasService preguntasService;
	@Autowired
	private IVacunacionesDao vacunacionesDao;
	@Autowired
	private IHistorialClinicoDao historialClinicoDao;
	@Autowired
	private IPreguntasHistorialDao preguntasHistorialDao;
	@Autowired
	private UtilesService utiles;
	
	public PacientesServiceImpl(IPacientesDao pacientesDao) {
        this.pacientesDao = pacientesDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Pacientes> listar() throws SigebiException{ 
		
		List<Pacientes> pacientes = pacientesDao.findAll();
		
		if( pacientes.isEmpty()) {
			throw new SigebiException.DataNotFound("No se encontraron datos");
		}
		
		return pacientes;
	}

	@Override
	@Transactional(readOnly = true)
	public Pacientes obtener(int id) throws SigebiException{
		Pacientes paciente = pacientesDao.findById(id).orElse(null);
		
		if( paciente != null) {				
			
			if( paciente.getPersonas().getDepartamentos() == null ) {
				paciente.getPersonas().setDepartamentos(new Departamentos());
			}
			if( paciente.getPersonas().getDependencias() == null ) {
				paciente.getPersonas().setDependencias(new Dependencias());
			}
			if( paciente.getPersonas().getCarreras() == null ) {
				paciente.getPersonas().setCarreras(new Carreras());
			}
			if( paciente.getPersonas().getEstamentos() == null ) {
				paciente.getPersonas().setEstamentos(new Estamentos());
			}
		}
		
		if( paciente == null ) {
			String mensaje = "Error: no se pudo editar, el área ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!"));
			throw new SigebiException.DataNotFound(mensaje);
		}
		
		return paciente;
	}

	@Transactional
	public Pacientes guardar(Pacientes paciente) throws SigebiException {
		
		if ( paciente.getPersonas() == null ) {
			throw new SigebiException.BusinessException("Datos de la persona es requerido ");
		}
		
		if( paciente.getPersonas() != null ) {
			//Buscar si la persona ya es paciente
			
			List<Personas> personaDb = personasDao.findByCedula(paciente.getPersonas().getCedula());
			if( personaDb != null && !personaDb.isEmpty() ) {
				List<Integer> pacienteIds = new ArrayList<Integer>();
				pacienteIds.add(personaDb.get(0).getPersonaId());
				
				List<Pacientes> pacienteDb = buscarNoPaginable(null, null, new Pacientes(), pacienteIds);
				
				if( !pacienteDb.isEmpty() ) {
					throw new SigebiException.DataAlreadyExist("La persona ya existe como paciente");
				}
			}
			
			if( paciente.getPersonas().getPersonaId() != null ) {
				//Busca a la persona y si existe actualizar sus datos
				Personas persona = personasService.obtener(paciente.getPersonas().getPersonaId());
				
				if(persona == null) {
					throw new SigebiException.DataNotFound("No se encontró persona con id: " + paciente.getPersonas().getPersonaId());
				}
								
				persona.setNombres(paciente.getPersonas().getNombres());
				persona.setApellidos(paciente.getPersonas().getApellidos());
				persona.setCarreras(paciente.getPersonas().getCarreras());
				persona.setCedula(paciente.getPersonas().getCedula());
				persona.setCelular(paciente.getPersonas().getCelular());
				persona.setDepartamentos(paciente.getPersonas().getDepartamentos());
				persona.setDependencias(paciente.getPersonas().getDependencias());
				persona.setDireccion(paciente.getPersonas().getDireccion());
				persona.setEmail(paciente.getPersonas().getEmail());
				persona.setEstadoCivil(paciente.getPersonas().getEstadoCivil());
				persona.setEstamentos(paciente.getPersonas().getEstamentos());
				persona.setFechaNacimiento(paciente.getPersonas().getFechaNacimiento());
				persona.setNacionalidad(paciente.getPersonas().getNacionalidad());
				persona.setSexo(paciente.getPersonas().getSexo());
				persona.setTelefono(paciente.getPersonas().getTelefono());
				persona.setObservacion(paciente.getPersonas().getObservacion());
				persona.setUsuarioModificacion(paciente.getPersonas().getUsuarioModificacion());
				
				paciente.setPersonas(persona);
				personasService.guardar(persona);
			}else {
				//Crear nueva persona
				Personas personaNew = personasDao.save(paciente.getPersonas());
				paciente.setPersonas(personaNew);
			}
		}
		
		return pacientesDao.save(paciente);
	}
	
	@Transactional
	public Pacientes guardarPacienteHistorialClinico(ProcesoPacienteHistorialClinico pacienteHistorialClinico) throws SigebiException {
		
		if ( pacienteHistorialClinico.getPaciente().getPersonas() == null ) {
			throw new SigebiException.BusinessException("Datos de la persona es requerido ");
		}
		
		Pacientes paciente = guardar(pacienteHistorialClinico.getPaciente());
		
		//Insertar el historial clinico
		try {
			pacienteHistorialClinico.getHistorialClinico().setPacienteId(paciente.getPacienteId());			
			historialClinicoDao.save(pacienteHistorialClinico.getHistorialClinico());
			
			for(Integer alergenoId : pacienteHistorialClinico.getAlergenosIdList()) {
				Alergias alergia = new Alergias();
				
				Alergenos alergeno = alergenosService.findById(alergenoId);
				
				alergia.setAlergenos(alergeno);
				alergia.setUsuarioCreacion(pacienteHistorialClinico.getPaciente().getUsuarioCreacion());
				alergia.setPacienteId(paciente.getPacienteId());
				
				alergiasDao.save(alergia);				
			}
			
			for(Integer patologiaProcedimientoId : pacienteHistorialClinico.getPatologiasProcedimientosIdList()) {
				Antecedentes antecedentes = new Antecedentes();
				PatologiasProcedimientos patologiaProcedimiento = new PatologiasProcedimientos();
				
				patologiaProcedimiento = patologiasProcedimientosService.findById(patologiaProcedimientoId);
				
				antecedentes.setPatologiasProcedimientos(patologiaProcedimiento);
				antecedentes.setPacienteId(paciente.getPacienteId());
				antecedentes.setUsuarioCreacion(pacienteHistorialClinico.getPaciente().getUsuarioCreacion());
				antecedentes.setTipo(Globales.TiposAntecedentes.PERSONAL);
				antecedentesDao.save(antecedentes);				
			}
			
			for(Integer patologiaFamiliarId : pacienteHistorialClinico.getPatologiasFamiliaresIdList()) {
				Antecedentes antecedentes = new Antecedentes();
				PatologiasProcedimientos patologiaProcedimiento = new PatologiasProcedimientos();
				
				patologiaProcedimiento = patologiasProcedimientosService.findById(patologiaFamiliarId);
				
				antecedentes.setPatologiasProcedimientos(patologiaProcedimiento);
				antecedentes.setPacienteId(paciente.getPacienteId());
				antecedentes.setUsuarioCreacion(pacienteHistorialClinico.getPaciente().getUsuarioCreacion());
				antecedentes.setTipo(Globales.TiposAntecedentes.FAMILIAR);
				antecedentesDao.save(antecedentes);				
			}
			
			for(Integer vacunaId : pacienteHistorialClinico.getVacunasIdList()) {
				Vacunaciones vacunaciones = new Vacunaciones();
				Vacunas vacuna = new Vacunas();
				
				vacuna = vacunasService.findById(vacunaId);
				
				vacunaciones.setVacunas(vacuna);
				vacunaciones.setPacienteId(paciente.getPacienteId());
				vacunaciones.setUsuarioCreacion(pacienteHistorialClinico.getPaciente().getUsuarioCreacion());
				vacunacionesDao.save(vacunaciones);				
			}
			
			for(Preguntas preguntaParam : pacienteHistorialClinico.getPreguntasList()) {
				PreguntasHistorial preguntasHistorial = new PreguntasHistorial();
				Preguntas pregunta = new Preguntas();
				
				pregunta = preguntasService.findById(preguntaParam.getPreguntaId());
				
				preguntasHistorial.setPreguntas(pregunta);
				preguntasHistorial.setRespuesta(preguntaParam.getValor());
				preguntasHistorial.setHistorialClinicoId(pacienteHistorialClinico.getHistorialClinico().getHistorialClinicoId());
				preguntasHistorial.setUsuarioCreacion(pacienteHistorialClinico.getPaciente().getUsuarioCreacion());
				
				preguntasHistorialDao.save(preguntasHistorial);
			}
			
		} catch (Exception e) {
			//throw new Exception("Error al insertar el historial clinico " + e.getMessage());
		}
		
		return paciente;
	}
	
	@Transactional
	public Pacientes actualizarPacienteHistorialClinico(ProcesoPacienteHistorialClinico pacienteHistorialClinico) throws SigebiException {
				
		if ( pacienteHistorialClinico.getPaciente().getPacienteId() == null ) {
			throw new SigebiException.BusinessException("Paciente id es requerido ");
		}
		
		Pacientes pacienteActual = obtener(pacienteHistorialClinico.getPaciente().getPacienteId());
		
		if ( pacienteActual == null ) {						
			String mensaje = "Error: no se pudo editar, el paciente ID: "
					.concat(String.valueOf(pacienteHistorialClinico.getPaciente().getPacienteId()).concat(" no existe en la base de datos!"));
			throw new SigebiException.DataNotFound(mensaje);
		}
		
		Pacientes paciente = actualizar(pacienteHistorialClinico.getPaciente());
		
		//Actualizar el historial clinico
		try {
			HistorialClinico historialClinico = historialClinicoDao.save(pacienteHistorialClinico.getHistorialClinico());			
			
			/*
			 * Alergias
			 * */
			List<Alergias> alergiasListDb = alergiasDao.findByPacienteId(paciente.getPacienteId());
			
			boolean existeId = false;
			for(Alergias alergiaDb : alergiasListDb) {
				for(Integer alergenoId : pacienteHistorialClinico.getAlergenosIdList()) {
					if(alergiaDb.getAlergenos().getAlergenoId().equals(alergenoId)) {
						existeId = true;
						break;
					}
				}
				//Eliminar alergeno no chequeado de las alergias
				if(!existeId) {
					alergiasDao.deleteById(alergiaDb.getAlergiaId());
				}
				
				existeId = false;
			}
			
			existeId = false;
			for(Integer alergenoId : pacienteHistorialClinico.getAlergenosIdList()) {
				
				for(Alergias alergiaDb : alergiasListDb) {
					if(alergenoId.equals(alergiaDb.getAlergenos().getAlergenoId())) {
						existeId = true;
						break;
					}
				}
				//Agregar nuevo alergeno como alergia
				if(!existeId) {
					Alergias alergia = new Alergias();
					
					Alergenos alergeno = alergenosService.findById(alergenoId);
					
					alergia.setAlergenos(alergeno);
					alergia.setUsuarioCreacion(pacienteHistorialClinico.getPaciente().getUsuarioCreacion());
					alergia.setPacienteId(paciente.getPacienteId());
					
					alergiasDao.save(alergia);	
				}
				existeId = false;
			}
			
			/*
			 * Antecedentes
			 * */
			List<Antecedentes> antecedentesPersonalesListDb = antecedentesDao.findByPacienteId(paciente.getPacienteId());
			
			List<Antecedentes> antecedentesListDb = new ArrayList<Antecedentes>();
			for(Antecedentes antecedenteDb : antecedentesPersonalesListDb) {
				if(Globales.TiposAntecedentes.PERSONAL.equals(antecedenteDb.getTipo())) {
					antecedentesListDb.add(antecedenteDb);
				}
			}
			
			existeId = false;
			for(Antecedentes antecedenteDb : antecedentesListDb) {
				for(Integer patologiaProcedimientoId : pacienteHistorialClinico.getPatologiasProcedimientosIdList()) {
					if(antecedenteDb.getPatologiasProcedimientos().getPatologiaProcedimientoId().equals(patologiaProcedimientoId)) {
						existeId = true;
						break;
					}
				}
				//Eliminar alergeno no chequeado de las alergias
				if(!existeId) {
					antecedentesDao.deleteById(antecedenteDb.getAntecedenteId());
				}
				
				existeId = false;
			}
			
			existeId = false;
			for(Integer patologiaProcedimientoId : pacienteHistorialClinico.getPatologiasProcedimientosIdList()) {
				for(Antecedentes antecedenteDb : antecedentesListDb) {
					if(patologiaProcedimientoId.equals(antecedenteDb.getPatologiasProcedimientos().getPatologiaProcedimientoId())) {
						existeId = true;
						break;
					}
				}
				//Agregar nueva patologia/procedimiento como antecedente
				if(!existeId) {
					Antecedentes antecedentes = new Antecedentes();
					PatologiasProcedimientos patologiaProcedimiento = new PatologiasProcedimientos();
					
					patologiaProcedimiento = patologiasProcedimientosService.findById(patologiaProcedimientoId);
					
					antecedentes.setPatologiasProcedimientos(patologiaProcedimiento);
					antecedentes.setPacienteId(paciente.getPacienteId());
					antecedentes.setUsuarioCreacion(pacienteHistorialClinico.getPaciente().getUsuarioCreacion());
					antecedentes.setTipo(Globales.TiposAntecedentes.PERSONAL);
					antecedentesDao.save(antecedentes);				
				}
				existeId = false;
			}
			
			/*
			 * Antecedentes familiares
			 * */
			List<Antecedentes> antecedentesFamiliarListDb = antecedentesDao.findByPacienteId(paciente.getPacienteId());
			
			List<Antecedentes> antecedentesFamiliaresListDb = new ArrayList<Antecedentes>();
			for(Antecedentes antecedenteDb : antecedentesFamiliarListDb) {
				if(Globales.TiposAntecedentes.FAMILIAR.equals(antecedenteDb.getTipo())) {
					antecedentesFamiliaresListDb.add(antecedenteDb);
				}
			}
			
			existeId = false;
			for(Antecedentes antecedenteFamiliarDb : antecedentesFamiliaresListDb) {
				for(Integer patologiaId : pacienteHistorialClinico.getPatologiasFamiliaresIdList()) {
					if(antecedenteFamiliarDb.getPatologiasProcedimientos().getPatologiaProcedimientoId().equals(patologiaId)) {
						existeId = true;
						break;
					}
				}
				//Eliminar patologia no chequeado de los antecedentes familiares
				if(!existeId) {
					antecedentesDao.deleteById(antecedenteFamiliarDb.getAntecedenteId());
				}
				
				existeId = false;
			}
			
			existeId = false;
			for(Integer patologiaFamiliarId : pacienteHistorialClinico.getPatologiasFamiliaresIdList()) {
				for(Antecedentes antecedenteFamiliarDb : antecedentesFamiliaresListDb) {
					if(patologiaFamiliarId.equals(antecedenteFamiliarDb.getPatologiasProcedimientos().getPatologiaProcedimientoId())) {
						existeId = true;
						break;
					}
				}
				//Agregar nueva patologia como antecedente familiar
				if(!existeId) {
					Antecedentes antecedentes = new Antecedentes();
					PatologiasProcedimientos patologiaProcedimiento = new PatologiasProcedimientos();
					
					patologiaProcedimiento = patologiasProcedimientosService.findById(patologiaFamiliarId);
					
					antecedentes.setPatologiasProcedimientos(patologiaProcedimiento);
					antecedentes.setPacienteId(paciente.getPacienteId());
					antecedentes.setUsuarioCreacion(pacienteHistorialClinico.getPaciente().getUsuarioCreacion());
					antecedentes.setTipo(Globales.TiposAntecedentes.FAMILIAR);
					antecedentesDao.save(antecedentes);	
				}
				existeId = false;
			}
						
			/*
			 * Vacunaciones
			 * */
			List<Vacunaciones> vacunacionesListDb = vacunacionesDao.findByPacienteId(paciente.getPacienteId());
			
			existeId = false;
			for(Vacunaciones vacunacionesDb : vacunacionesListDb) {
				for(Integer vacunaId : pacienteHistorialClinico.getVacunasIdList()) {
					if(vacunacionesDb.getVacunas().getVacunaId().equals(vacunaId)) {
						existeId = true;
						break;
					}
				}
				//Eliminar patologia no chequeado de los antecedentes familiares
				if(!existeId) {
					vacunacionesDao.deleteById(vacunacionesDb.getVacunacionId());
				}
				
				existeId = false;
			}
			
			existeId = false;
			for(Integer vacunaId : pacienteHistorialClinico.getVacunasIdList()) {
				for(Vacunaciones vacunacionesDb : vacunacionesListDb) {
					if(vacunaId.equals(vacunacionesDb.getVacunas().getVacunaId())) {
						existeId = true;
						break;
					}
				}
				//Agregar nueva vacuna en las vacunaciones
				if(!existeId) {
					Vacunaciones vacunaciones = new Vacunaciones();
					Vacunas vacuna = new Vacunas();
					
					vacuna = vacunasService.findById(vacunaId);
					
					vacunaciones.setVacunas(vacuna);
					vacunaciones.setPacienteId(paciente.getPacienteId());
					vacunaciones.setUsuarioCreacion(pacienteHistorialClinico.getPaciente().getUsuarioCreacion());
					vacunacionesDao.save(vacunaciones);	
				}
				existeId = false;
			}
			
			/*
			 * Preguntas
			 * */
			List<PreguntasHistorial> preguntasHistorialListDb = preguntasHistorialDao.findByHistorialClinicoId(pacienteHistorialClinico.getHistorialClinico().getHistorialClinicoId());
						
			existeId = false;
			for(PreguntasHistorial preguntasHistorialDb : preguntasHistorialListDb) {
				for(Preguntas pregunta : pacienteHistorialClinico.getPreguntasList()) {
					if(preguntasHistorialDb.getPreguntas().getPreguntaId().equals(pregunta.getPreguntaId())) {
						existeId = true;
						break;
					}
				}
				//Eliminar pregunta no chequeado
				if(!existeId) {
					preguntasHistorialDao.deleteById(preguntasHistorialDb.getPreguntaHistorialId());
				}
				
				existeId = false;
			}
			
			existeId = false;
			for(Preguntas pregunta : pacienteHistorialClinico.getPreguntasList()) {
				for(PreguntasHistorial preguntaHistorialDb : preguntasHistorialListDb) {
					if(pregunta.getPreguntaId().equals(preguntaHistorialDb.getPreguntas().getPreguntaId())) {
						existeId = true;
						break;
					}
				}
				//Agregar nueva pregunta
				if(!existeId) {
					PreguntasHistorial preguntaHistorial = new PreguntasHistorial();
					Preguntas preguntaDb = new Preguntas();
					
					preguntaDb = preguntasService.findById(pregunta.getPreguntaId());
					
					preguntaHistorial.setHistorialClinicoId(historialClinico.getHistorialClinicoId());
					preguntaHistorial.setPreguntas(preguntaDb);
					preguntaHistorial.setUsuarioCreacion(pacienteHistorialClinico.getPaciente().getUsuarioCreacion());
					preguntaHistorial.setRespuesta(pregunta.getValor());
					preguntasHistorialDao.save(preguntaHistorial);				
				}
				existeId = false;
			}
			
		} catch (Exception e) {
			//throw new Exception("Error al insertar el historial clinico " + e.getMessage());
		}
		
		return paciente;
	}
	
	@Transactional
	public Pacientes actualizar(Pacientes paciente) throws SigebiException {
		
		if ( paciente.getPacienteId() == null ) {
			throw new SigebiException.BusinessException("Paciente id es requerido ");
		}
		
		Pacientes pacienteActual = obtener(paciente.getPacienteId());
		
		if ( pacienteActual == null ) {
			String mensaje = "Error: no se pudo editar, el paciente ID: "
					.concat(String.valueOf(paciente.getPacienteId()).concat(" no existe en la base de datos!"));
			throw new SigebiException.DataNotFound(mensaje);
		}
		
		//Busca a la persona y actualizar sus datos
		Personas persona = personasService.obtener(paciente.getPersonas().getPersonaId());
		
		if(persona == null) {
			throw new SigebiException.DataNotFound("No se encontro persona con id: " + paciente.getPersonas().getPersonaId());
		}
				
		persona.setNombres(paciente.getPersonas().getNombres());
		persona.setApellidos(paciente.getPersonas().getApellidos());
		persona.setCarreras(paciente.getPersonas().getCarreras());
		persona.setCedula(paciente.getPersonas().getCedula());
		persona.setCelular(paciente.getPersonas().getCelular());
		persona.setDepartamentos(paciente.getPersonas().getDepartamentos());
		persona.setDependencias(paciente.getPersonas().getDependencias());
		persona.setDireccion(paciente.getPersonas().getDireccion());
		persona.setEmail(paciente.getPersonas().getEmail());
		persona.setEstadoCivil(paciente.getPersonas().getEstadoCivil());
		persona.setEstamentos(paciente.getPersonas().getEstamentos());
		persona.setFechaNacimiento(paciente.getPersonas().getFechaNacimiento());
		persona.setNacionalidad(paciente.getPersonas().getNacionalidad());
		persona.setSexo(paciente.getPersonas().getSexo());
		persona.setTelefono(paciente.getPersonas().getTelefono());
		persona.setObservacion(paciente.getPersonas().getObservacion());
		persona.setUsuarioModificacion(paciente.getPersonas().getUsuarioModificacion());
		persona.setFoto(paciente.getPersonas().getFoto());
		
		paciente.setPersonas(persona);
				
		return pacientesDao.save(paciente);
	}

	@Override
	@Transactional
	public void eliminar(int id) throws SigebiException {
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			throw new SigebiException.BusinessException("Paciente id es requerido ");
		}
		
		Pacientes pacienteActual = obtener(id);
		
		if ( pacienteActual == null ) {
			String mensaje = "Error: no se pudo editar, el paciente ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!"));
			throw new SigebiException.DataNotFound(mensaje);
		}
		pacientesDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Pacientes> buscar(Date fromDate, Date toDate, Pacientes paciente, List<Integer> personasId, Pageable pageable) throws DataAccessException {
		List<Pacientes> pacientesList = pacientesDao.findAll((Specification<Pacientes>) (root, cq, cb) -> {
            
			Predicate p = cb.conjunction();
            if( personasId != null && !personasId.isEmpty() ){
            	p = cb.and(root.get("personas").in(personasId));
            }            
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( paciente.getPacienteId() != null ) {
                p = cb.and(p, cb.equal(root.get("pacienteId"), paciente.getPacienteId()) );
            }
   
            cq.orderBy(cb.desc(root.get("pacienteId")));
            return p;
        }, pageable).getContent();
						
        return pacientesList;
        
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Pacientes> buscarNoPaginable(Date fromDate, Date toDate, Pacientes paciente, List<Integer> personasId) throws DataAccessException {
		List<Pacientes> pacientesList = pacientesDao.findAll((Specification<Pacientes>) (root, cq, cb) -> {
            
			Predicate p = cb.conjunction();
            if( personasId != null && !personasId.isEmpty() ){
            	p = cb.and(root.get("personas").in(personasId));
            }            
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( paciente.getPacienteId() != null ) {
                p = cb.and(p, cb.equal(root.get("pacienteId"), paciente.getPacienteId()) );
            }
         
            cq.orderBy(cb.desc(root.get("pacienteId")));
            return p;
        });
        return pacientesList;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Pacientes> buscarPacientes(Date fromDate, Date toDate, Pacientes paciente, Pageable pageable) throws DataAccessException {
		
		List<Pacientes> pacientesList = new ArrayList<Pacientes>();
		
		if ( paciente == null ) {
			paciente = new Pacientes();
		}
		List<Personas> personasList = new ArrayList<Personas>();
		List<Integer> personasIds = new ArrayList<Integer>();
		
		if( paciente.getPersonas() != null) {
			personasList = personasService.buscar(fromDate, toDate, paciente.getPersonas(), pageable);
				
			for( Personas persona : personasList ){
				personasIds.add(persona.getPersonaId());
			}
			if( personasList.isEmpty()) {
				return pacientesList;
			}
		}
					
		pacientesList = buscar(fromDate, toDate, paciente, personasIds, pageable);
		
		for( Pacientes pacienteFor : pacientesList) {
			
			if( pacienteFor.getPersonas().getDepartamentos() == null ) {
				pacienteFor.getPersonas().setDepartamentos(new Departamentos());
			}
			if( pacienteFor.getPersonas().getDependencias() == null ) {
				pacienteFor.getPersonas().setDependencias(new Dependencias());
			}
			if( pacienteFor.getPersonas().getCarreras() == null ) {
				pacienteFor.getPersonas().setCarreras(new Carreras());
			}
			if( pacienteFor.getPersonas().getEstamentos() == null ) {
				pacienteFor.getPersonas().setEstamentos(new Estamentos());
			}
		}
		
		return pacientesList;
	}
	
}
