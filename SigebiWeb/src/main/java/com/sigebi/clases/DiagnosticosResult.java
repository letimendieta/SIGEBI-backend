package com.sigebi.clases;

import java.time.LocalDateTime;

import com.sigebi.entity.EnfermedadesCie10;

public class DiagnosticosResult {	
	
	private Integer diagnosticoId;	
	private String diagnosticoPrincipal;
	private String diagnosticoSecundario;
	private EnfermedadesCie10 enfermedadCie10Primaria;
	private EnfermedadesCie10 enfermedadCie10Secundaria;
	private LocalDateTime fechaCreacion;
	private String usuarioCreacion;
	private LocalDateTime fechaModificacion;
	private String usuarioModificacion;
	
	public Integer getDiagnosticoId() {
		return diagnosticoId;
	}
	public void setDiagnosticoId(Integer diagnosticoId) {
		this.diagnosticoId = diagnosticoId;
	}
	public String getDiagnosticoPrincipal() {
		return diagnosticoPrincipal;
	}
	public void setDiagnosticoPrincipal(String diagnosticoPrincipal) {
		this.diagnosticoPrincipal = diagnosticoPrincipal;
	}
	public String getDiagnosticoSecundario() {
		return diagnosticoSecundario;
	}
	public void setDiagnosticoSecundario(String diagnosticoSecundario) {
		this.diagnosticoSecundario = diagnosticoSecundario;
	}
	public EnfermedadesCie10 getEnfermedadCie10Primaria() {
		return enfermedadCie10Primaria;
	}
	public void setEnfermedadCie10Primaria(EnfermedadesCie10 enfermedadCie10Primaria) {
		this.enfermedadCie10Primaria = enfermedadCie10Primaria;
	}
	public EnfermedadesCie10 getEnfermedadCie10Secundaria() {
		return enfermedadCie10Secundaria;
	}
	public void setEnfermedadCie10Secundaria(EnfermedadesCie10 enfermedadCie10Secundaria) {
		this.enfermedadCie10Secundaria = enfermedadCie10Secundaria;
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
	
	
}
