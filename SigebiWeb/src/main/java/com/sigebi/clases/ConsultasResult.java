package com.sigebi.clases;

import java.time.LocalDateTime;

import com.sigebi.entity.Anamnesis;
import com.sigebi.entity.Areas;
import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.Tratamientos;

public class ConsultasResult {
		
	private Integer consultaId;
	private LocalDateTime fecha;
	private Integer pacienteId;
	private LocalDateTime fechaCreacion;
	private String usuarioCreacion;
	private LocalDateTime fechaModificacion;
	private String usuarioModificacion;
    private Areas areas;
    private DiagnosticosResult diagnosticos;
    private Tratamientos tratamientos;
    private Funcionarios funcionarios;
    private Anamnesis anamnesis;
    
	public Integer getConsultaId() {
		return consultaId;
	}
	public void setConsultaId(Integer consultaId) {
		this.consultaId = consultaId;
	}
	public LocalDateTime getFecha() {
		return fecha;
	}
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
	public Integer getPacienteId() {
		return pacienteId;
	}
	public void setPacienteId(Integer pacienteId) {
		this.pacienteId = pacienteId;
	}
	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}
	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	public LocalDateTime getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(LocalDateTime fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}
	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}
	public Areas getAreas() {
		return areas;
	}
	public void setAreas(Areas areas) {
		this.areas = areas;
	}
	public DiagnosticosResult getDiagnosticos() {
		return diagnosticos;
	}
	public void setDiagnosticos(DiagnosticosResult diagnosticos) {
		this.diagnosticos = diagnosticos;
	}
	public Tratamientos getTratamientos() {
		return tratamientos;
	}
	public void setTratamientos(Tratamientos tratamientos) {
		this.tratamientos = tratamientos;
	}
	public Funcionarios getFuncionarios() {
		return funcionarios;
	}
	public void setFuncionarios(Funcionarios funcionarios) {
		this.funcionarios = funcionarios;
	}
	public Anamnesis getAnamnesis() {
		return anamnesis;
	}
	public void setAnamnesis(Anamnesis anamnesis) {
		this.anamnesis = anamnesis;
	}
	
    
	
}
