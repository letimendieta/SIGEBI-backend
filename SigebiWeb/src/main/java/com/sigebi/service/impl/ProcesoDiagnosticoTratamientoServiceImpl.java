package com.sigebi.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigebi.clases.FichaMedica;
import com.sigebi.clases.ProcesoDiagnosticoTratamiento;
import com.sigebi.dao.IAlergiasDao;
import com.sigebi.dao.IAnamnesisDao;
import com.sigebi.dao.IAntecedentesDao;
import com.sigebi.dao.IConsultasDao;
import com.sigebi.dao.IDiagnosticosDao;
import com.sigebi.dao.IHistorialClinicoDao;
import com.sigebi.dao.IPacientesDao;
import com.sigebi.dao.IProcedimientosDao;
import com.sigebi.dao.IProcedimientosInsumosDao;
import com.sigebi.dao.ITratamientosDao;
import com.sigebi.dao.ITratamientosInsumosDao;
import com.sigebi.entity.Alergenos;
import com.sigebi.entity.Alergias;
import com.sigebi.entity.Anamnesis;
import com.sigebi.entity.Antecedentes;
import com.sigebi.entity.Consultas;
import com.sigebi.entity.Diagnosticos;
import com.sigebi.entity.HistorialClinico;
import com.sigebi.entity.MotivosConsulta;
import com.sigebi.entity.Pacientes;
import com.sigebi.entity.PatologiasProcedimientos;
import com.sigebi.entity.Procedimientos;
import com.sigebi.entity.ProcedimientosInsumos;
import com.sigebi.entity.Tratamientos;
import com.sigebi.entity.TratamientosInsumos;
import com.sigebi.service.AlergenosService;
import com.sigebi.service.HistorialesClinicosService;
import com.sigebi.service.MotivosConsultaService;
import com.sigebi.service.PatologiasProcedimientosService;
import com.sigebi.service.ProcesoDiagnosticoTratamientoService;
import com.sigebi.util.Globales;
import com.sigebi.util.exceptions.SigebiException;


@Service
public class ProcesoDiagnosticoTratamientoServiceImpl implements ProcesoDiagnosticoTratamientoService{

	@Autowired
	private IDiagnosticosDao diagnosticoDao;	
	@Autowired
	private ITratamientosDao tratamientoDao;
	@Autowired
	private IAnamnesisDao anamnesisDao;
	@Autowired
	private ITratamientosInsumosDao tratamientoInsumoDao;
	@Autowired
	private IConsultasDao consultaDao;
	@Autowired
	private AlergenosService alergenosService;
	@Autowired
	private IAlergiasDao alergiasDao;
	@Autowired
	private IPacientesDao pacientesDao;
	@Autowired
	private PatologiasProcedimientosService patologiasProcedimientosService;
	@Autowired
	private IAntecedentesDao antecedentesDao;
	@Autowired
	private MotivosConsultaService motivosConsultaService;
	@Autowired
	private IHistorialClinicoDao historialClinicoDao;
	@Autowired
	private IProcedimientosDao procedimientosDao;
	@Autowired
	private IProcedimientosInsumosDao procedimientoInsumoDao;
	@Autowired
	private HistorialesClinicosService historialesClinicosService;	

	@Override
	@Transactional
	public Consultas guardar(ProcesoDiagnosticoTratamiento procesoDiagnosticoTratamiento) throws SigebiException {
		MotivosConsulta motivoConsulta = null;
		//Guardar el Anamnesis
		Anamnesis anamnesis = null;
		try {
			motivoConsulta = 
					motivosConsultaService.findById(procesoDiagnosticoTratamiento.getAnamnesis().getMotivoConsulta().getMotivoConsultaId());
			
			procesoDiagnosticoTratamiento.getAnamnesis().setMotivoConsulta(motivoConsulta);
			anamnesis = anamnesisDao.save(procesoDiagnosticoTratamiento.getAnamnesis());
		} catch (Exception e) {
			throw new SigebiException.InternalServerError("Error al guardar el anamnesis " + e.getMessage());
		}
		
		//Guardar diagnostico	
		Diagnosticos diagnostico = null;
		try {
			diagnostico = diagnosticoDao.save(procesoDiagnosticoTratamiento.getDiagnostico());
		} catch (Exception e) {
			throw new SigebiException.InternalServerError("Error al guardar el diagnostico " + e.getMessage());
		}
		
		//Actualizar la ficha clinica	
		try {
			for(FichaMedica fichaMedica : procesoDiagnosticoTratamiento.getFichaMedicaList()) {
				if( Globales.TipoDatoFichaMedica.ALERGIAS.equals( fichaMedica.getTipo()) ) {
					Alergias alergia = new Alergias();
					
					Alergenos alergeno = alergenosService.findById(fichaMedica.getId());
					
					alergia.setAlergenos(alergeno);
					alergia.setUsuarioCreacion(procesoDiagnosticoTratamiento.getDiagnostico().getUsuarioCreacion());
					alergia.setPacienteId(fichaMedica.getPacienteId());
					
					alergiasDao.save(alergia);
				}else if( Globales.TipoDatoFichaMedica.ANTECEDENTE_PATOLOGIAS_PROCEDIMIENTOS.equals( fichaMedica.getTipo()) ) {
					Antecedentes antecedentes = new Antecedentes();
					PatologiasProcedimientos patologiaProcedimiento = new PatologiasProcedimientos();
					
					patologiaProcedimiento = patologiasProcedimientosService.findById(fichaMedica.getId());
					
					antecedentes.setPatologiasProcedimientos(patologiaProcedimiento);
					antecedentes.setPacienteId(fichaMedica.getPacienteId());
					antecedentes.setUsuarioCreacion(procesoDiagnosticoTratamiento.getDiagnostico().getUsuarioCreacion());
					antecedentes.setTipo(Globales.TiposAntecedentes.PERSONAL);
					antecedentesDao.save(antecedentes);
				}				
			}
			
			HistorialClinico historialClinico = historialesClinicosService.findById(procesoDiagnosticoTratamiento.getHistorialClinico().getHistorialClinicoId());
			
			historialClinico.setPatologiaActual(procesoDiagnosticoTratamiento.getHistorialClinico().getPatologiaActual());
			historialClinico.setTratamientoActual(procesoDiagnosticoTratamiento.getHistorialClinico().getTratamientoActual());
			historialClinico.setUsuarioModificacion(procesoDiagnosticoTratamiento.getHistorialClinico().getUsuarioModificacion());
			
			historialClinicoDao.save(historialClinico);
			
		} catch (Exception e) {
			throw new SigebiException.InternalServerError("Error al actualizar la ficha del paciente " + e.getMessage());
		}
		
		//Guardar el tratamiento
		Tratamientos tratamiento = null;
		try {
			tratamiento = tratamientoDao.save(procesoDiagnosticoTratamiento.getTratamiento());
		} catch (Exception e) {
			throw new SigebiException.InternalServerError("Error al guardar el tratamiento " + e.getMessage());
		}
		
		//Guardar el tratamiento insumo
		try {
			for(TratamientosInsumos tratamientoInsumo : procesoDiagnosticoTratamiento.getTratamientoInsumoList()) {
				tratamientoInsumo.setTratamientos(tratamiento);
				tratamientoInsumoDao.save(tratamientoInsumo);				
			}			
		} catch (Exception e) {
			throw new SigebiException.InternalServerError("Error al guardar los medicamentos del tratamiento " + e.getMessage());
		}
		
		//Guardar la consulta
		Consultas consulta;
		try {
			procesoDiagnosticoTratamiento.getConsulta().setFecha(LocalDateTime.now());
			procesoDiagnosticoTratamiento.getConsulta().setDiagnosticos(diagnostico);
			procesoDiagnosticoTratamiento.getConsulta().setTratamientos(tratamiento);
			procesoDiagnosticoTratamiento.getConsulta().setAnamnesis(anamnesis);
			
			consulta = consultaDao.save(procesoDiagnosticoTratamiento.getConsulta());
		} catch (Exception e) {
			throw new SigebiException.InternalServerError("Error al guardar la consulta " + e.getMessage());
		}
		
		//Guardar procedimiento y procedimiento insumo
		try {			
			
			Pacientes paciente = pacientesDao.findByPacienteId(procesoDiagnosticoTratamiento.getConsulta().getPacienteId());
			
			Procedimientos procedimientoEntity = new Procedimientos();
			procedimientoEntity.setFuncionarios(procesoDiagnosticoTratamiento.getConsulta().getFuncionarios());			
			procedimientoEntity.setPacientes(paciente);
			procedimientoEntity.setUsuarioCreacion(procesoDiagnosticoTratamiento.getDiagnostico().getUsuarioCreacion());
			procedimientoEntity.setConsultaId(consulta.getConsultaId());
			procedimientoEntity.setFecha(LocalDateTime.now());
			procedimientoEntity.setAreas(procesoDiagnosticoTratamiento.getConsulta().getAreas());
			procedimientoEntity.setMotivoConsulta(motivoConsulta);
			
			if( procesoDiagnosticoTratamiento.getTratamientoInsumoList().size() > 0) {
				procedimientoEntity.setEstado(Globales.Estados.PENDIENTE);
			}else {
				procedimientoEntity.setEstado(Globales.Estados.FINALIZADO);
			}
			
			Procedimientos procedimiento = procedimientosDao.save(procedimientoEntity);
			
			
			for(TratamientosInsumos tratamientoInsumo : procesoDiagnosticoTratamiento.getTratamientoInsumoList()) {
				ProcedimientosInsumos procedimientoInsumoEntity = new ProcedimientosInsumos();
				
				procedimientoInsumoEntity.setEstado(Globales.EstadosEntregaInsumos.PENDIENTE);
				procedimientoInsumoEntity.setInsumos(tratamientoInsumo.getInsumos());
				procedimientoInsumoEntity.setUsuarioCreacion(procesoDiagnosticoTratamiento.getDiagnostico().getUsuarioCreacion());
				procedimientoInsumoEntity.setProcedimientos(procedimiento);
				procedimientoInsumoEntity.setMedida(tratamientoInsumo.getMedida());
				procedimientoInsumoEntity.setCantidad(tratamientoInsumo.getCantidad());
				
				procedimientoInsumoDao.save(procedimientoInsumoEntity);
			}			
			
		} catch (Exception e) {
			throw new SigebiException.InternalServerError("Error al guardar el procedimiento " + e.getMessage());
		}
		
		return consulta;
	}
}
