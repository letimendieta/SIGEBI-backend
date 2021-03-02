package com.sigebi.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigebi.clases.FichaMedica;
import com.sigebi.clases.Globales;
import com.sigebi.clases.ProcesoDiagnosticoTratamiento;
import com.sigebi.dao.IAlergenosDao;
import com.sigebi.dao.IAlergiasDao;
import com.sigebi.dao.IAnamnesisDao;
import com.sigebi.dao.IAntecedentesDao;
import com.sigebi.dao.IConsultasDao;
import com.sigebi.dao.IDiagnosticosDao;
import com.sigebi.dao.IMotivosConsultaDao;
import com.sigebi.dao.IPatologiasProcedimientosDao;
import com.sigebi.dao.IStockDao;
import com.sigebi.dao.ITratamientosDao;
import com.sigebi.dao.ITratamientosInsumosDao;
import com.sigebi.entity.Alergenos;
import com.sigebi.entity.Alergias;
import com.sigebi.entity.Anamnesis;
import com.sigebi.entity.Antecedentes;
import com.sigebi.entity.Consultas;
import com.sigebi.entity.Diagnosticos;
import com.sigebi.entity.Insumos;
import com.sigebi.entity.MotivosConsulta;
import com.sigebi.entity.PatologiasProcedimientos;
import com.sigebi.entity.Stock;
import com.sigebi.entity.Tratamientos;
import com.sigebi.entity.TratamientosInsumos;
import com.sigebi.service.AlergenosService;
import com.sigebi.service.MotivosConsultaService;
import com.sigebi.service.PatologiasProcedimientosService;
import com.sigebi.service.ProcesoDiagnosticoTratamientoService;


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
	private IAlergenosDao alergenosDao;
	@Autowired
	private AlergenosService alergenosService;
	@Autowired
	private IAlergiasDao alergiasDao;
	@Autowired
	private IPatologiasProcedimientosDao patologiasProcedimientosDao;
	@Autowired
	private PatologiasProcedimientosService patologiasProcedimientosService;
	@Autowired
	private IAntecedentesDao antecedentesDao;
	@Autowired
	private IMotivosConsultaDao motivosConsultaDao;
	@Autowired
	private MotivosConsultaService motivosConsultaService;
	
	

	@Override
	@Transactional
	public Consultas save(ProcesoDiagnosticoTratamiento procesoDiagnosticoTratamiento) throws Exception {
		
		//Guardar el Anamnesis
		Anamnesis anamnesis = null;
		try {
			MotivosConsulta motivoConsulta = 
					motivosConsultaService.findById(procesoDiagnosticoTratamiento.getAnamnesis().getMotivoConsulta().getMotivoConsultaId());
			
			procesoDiagnosticoTratamiento.getAnamnesis().setMotivoConsulta(motivoConsulta);
			anamnesis = anamnesisDao.save(procesoDiagnosticoTratamiento.getAnamnesis());
		} catch (Exception e) {
			throw new Exception("Error al guardar el anamnesis " + e.getMessage());
		}
		
		//Guardar diagnostico	
		Diagnosticos diagnostico = null;
		try {
			diagnostico = diagnosticoDao.save(procesoDiagnosticoTratamiento.getDiagnostico());
		} catch (Exception e) {
			throw new Exception("Error al guardar el diagnostico " + e.getMessage());
		}
		
		//Actualizar la ficha medica	
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
		} catch (Exception e) {
			throw new Exception("Error al actualizar la ficha del paciente " + e.getMessage());
		}
		
		//Guardar el tratamiento
		Tratamientos tratamiento = null;
		try {
			tratamiento = tratamientoDao.save(procesoDiagnosticoTratamiento.getTratamiento());
		} catch (Exception e) {
			throw new Exception("Error al guardar el tratamiento " + e.getMessage());
		}
		
		//Guardar el tratamiento insumo
		try {
			for(TratamientosInsumos tratamientoInsumo : procesoDiagnosticoTratamiento.getTratamientoInsumoList()) {
				tratamientoInsumo.setTratamientos(tratamiento);
				tratamientoInsumoDao.save(tratamientoInsumo);				
			}			
		} catch (Exception e) {
			throw new Exception("Error al guardar los medicamentos del tratamiento " + e.getMessage());
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
			throw new Exception("Error al guardar la consulta " + e.getMessage());
		}
		
		return consulta;
	}
}
