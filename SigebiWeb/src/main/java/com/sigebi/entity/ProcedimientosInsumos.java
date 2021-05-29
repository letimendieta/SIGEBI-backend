package com.sigebi.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "procedimientos_insumos")
public class ProcedimientosInsumos {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_procedimientos_insumos")
	@SequenceGenerator(name="seq_procedimientos_insumos",sequenceName="seq_procedimientos_insumos",allocationSize=1)
	private Integer procedimientoInsumoId;	
	
	@Column(name = "cantidad")
	private Integer cantidad;
		
	@Column(name = "estado", length = 10)
	@Size(max=10, message="maximo 10 caracteres")
	private String estado;
		
	@Column(name = "fecha_creacion")
	private LocalDateTime fechaCreacion;	
	
	@Column(name = "usuario_creacion", length = 15)
	private String usuarioCreacion;
	
	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;
	
	@Column(name = "usuario_modificacion", length = 15)
	private String usuarioModificacion;
	
	@OneToOne
    @JoinColumn(name = "procedimiento_id", referencedColumnName = "procedimientoId")
    private Procedimientos procedimientos;
	
	@OneToOne
    @JoinColumn(name = "insumo_medico_id", referencedColumnName = "insumoMedicoId")
    private InsumosMedicos insumosMedicos;
	
	@OneToOne
    @JoinColumn(name = "medicamento_id", referencedColumnName = "medicamentoId")
    private Medicamentos medicamentos;	
	
	@PrePersist
	private void create() {
		this.fechaCreacion = LocalDateTime.now();
	}
	
	@PreUpdate
	private void update() {
		this.fechaModificacion = LocalDateTime.now();
	}	
		
	public Integer getProcedimientoInsumoId() {
		return procedimientoInsumoId;
	}

	public void setProcedimientoInsumoId(Integer procedimientoInsumoId) {
		this.procedimientoInsumoId = procedimientoInsumoId;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public InsumosMedicos getInsumosMedicos() {
		return insumosMedicos;
	}

	public void setInsumosMedicos(InsumosMedicos insumosMedicos) {
		this.insumosMedicos = insumosMedicos;
	}

	public Medicamentos getMedicamentos() {
		return medicamentos;
	}

	public void setMedicamentos(Medicamentos medicamentos) {
		this.medicamentos = medicamentos;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Procedimientos getProcedimientos() {
		return procedimientos;
	}

	public void setProcedimientos(Procedimientos procedimientos) {
		this.procedimientos = procedimientos;
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
