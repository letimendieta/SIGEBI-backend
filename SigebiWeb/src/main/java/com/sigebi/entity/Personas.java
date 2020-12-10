package com.sigebi.entity;

import java.time.LocalDate;
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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "personas")
public class Personas {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_personas")
	@SequenceGenerator(name="seq_personas",sequenceName="seq_personas",allocationSize=1)
	private Integer personaId;
	
	@Column(name = "cedula", length = 20, unique = true, nullable = false)	
	@NotEmpty(message ="no puede estar vacio")
	@Size(max=20, message="maximo 20 caracteres")
	private String cedula;
	
	@Column(name = "nombres", length = 50, nullable = false)
	@NotEmpty(message ="no puede estar vacio")
	@Size(max=50, message="maximo 50 caracteres")
	private String nombres;	
	
	@Column(name = "apellidos", length = 50, nullable = false)
	@NotEmpty(message ="no puede estar vacio")
	@Size(max=50, message="maximo 50 caracteres")
	private String apellidos;
	
	@Column(name = "edad")
	private Integer edad;
	
	@Column(name = "fecha_nacimiento")
	private LocalDate fechaNacimiento;
	
	@Column(name = "direccion", length = 100)
	@Size(max=100, message="maximo 100 caracteres")
	private String direccion;
	
	@Column(name = "email", length = 50)	
	@Email(message="no es una dirección de correo bien formada")
	@Size(max=50, message="maximo 50 caracteres")
	private String email;
	
	@Column(name = "celular")
	@Size(max=20, message="maximo 20 caracteres")
	private String celular;
	
	@OneToOne
    @JoinColumn(name = "estamento_id", referencedColumnName = "estamentoId")
    private Estamentos estamentos;
	
	@OneToOne
    @JoinColumn(name = "carrera_id", referencedColumnName = "carreraId")
    private Carreras carreras;
	
	@OneToOne
    @JoinColumn(name = "departamento_id", referencedColumnName = "departamentoId")
    private Departamentos departamentos;
	
	@OneToOne
    @JoinColumn(name = "dependencia_id", referencedColumnName = "dependenciaId")
    private Dependencias dependencias;
		
	@Column(name = "sexo", length = 1)
	@Size(max=1, message="maximo 1 caracteres")
	private String sexo;
	
	@Column(name = "nacionalidad", length = 30)
	@Size(max=30, message="maximo 30 caracteres")
	private String nacionalidad;
	
	@Column(name = "telefono", length = 20)
	@Size(max=20, message="maximo 20 caracteres")
	private String telefono;
	
	@Column(name = "estado_civil", length = 15)
	@Size(max=15, message="maximo 15 caracteres")
	private String estadoCivil;
	
	@Column(name = "observacion", length = 200)
	@Size(max=200, message="maximo 200 caracteres")
	private String observacion;	
	
	@Column(name = "fecha_creacion")
	private LocalDateTime fechaCreacion;	
	
	@Column(name = "usuario_creacion", length = 15)
	@Size(max=15, message="maximo 15 caracteres")
	private String usuarioCreacion;
	
	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;
	
	@Column(name = "usuario_modificacion", length = 15)
	@Size(max=15, message="maximo 15 caracteres")
	private String usuarioModificacion;
	
	@OneToOne(mappedBy = "personas")
    private Pacientes pacientes;
	
	@OneToOne(mappedBy = "personas")
    private Funcionarios funcionarios;
	
	@OneToOne(mappedBy = "personas")
    private Usuarios usuarios;
	
	@PrePersist
	private void create() {
		this.fechaCreacion = LocalDateTime.now();
	}
	
	@PreUpdate
	private void update() {
		this.fechaModificacion = LocalDateTime.now();
	}

	public Integer getPersonaId() {
		return personaId;
	}

	public void setPersonaId(Integer personaId) {
		this.personaId = personaId;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public Estamentos getEstamentos() {
		return estamentos;
	}

	public void setEstamentos(Estamentos estamentos) {
		this.estamentos = estamentos;
	}

	public Carreras getCarreras() {
		return carreras;
	}

	public void setCarreras(Carreras carreras) {
		this.carreras = carreras;
	}

	public Departamentos getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(Departamentos departamentos) {
		this.departamentos = departamentos;
	}

	public Dependencias getDependencias() {
		return dependencias;
	}

	public void setDependencias(Dependencias dependencias) {
		this.dependencias = dependencias;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}
	
	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}

	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}

	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public LocalDateTime getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(LocalDateTime fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
		
}
