package com.sigebi.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Personas {
	
	@Id
	private int personaId;
	
	@Column(name = "cedula", length = 20)
	private String cedula;
	
	@Column(name = "nombres", length = 50)
	private String nombres;	
	
	@Column(name = "apellidos", length = 50)
	private String apellidos;
	
	@Column(name = "edad")
	private Integer edad;
	
	@Column(name = "fecha_nacimiento")
	private Date fechaNacimiento;
	
	@Column(name = "direccion", length = 100)
	private String direccion;
	
	@Column(name = "email", length = 50)
	private String email;
	
	@Column(name = "celular", length = 20)
	private String celular;
	
	@Column(name = "estamento_id")
	private Integer estamentoId;
	
	@Column(name = "carrera_id")
	private Integer carreraId;
	
	@Column(name = "departamento_id")
	private Integer departamentoId;
	
	@Column(name = "dependencia_id")
	private Integer dependenciaId;
	
	@Column(name = "sexo", length = 1)
	private String sexo;
	
	@Column(name = "nacionalidad", length = 30)
	private String nacionalidad;
	
	@Column(name = "telefono", length = 20)
	private String telefono;
	
	@Column(name = "estado_civil", length = 15)
	private String estadoCivil;

	public int getPersonaId() {
		return personaId;
	}

	public void setPersonaId(int personaId) {
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

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
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

	public Integer getEstamentoId() {
		return estamentoId;
	}

	public void setEstamentoId(Integer estamentoId) {
		this.estamentoId = estamentoId;
	}

	public Integer getCarreraId() {
		return carreraId;
	}

	public void setCarreraId(Integer carreraId) {
		this.carreraId = carreraId;
	}

	public Integer getDepartamentoId() {
		return departamentoId;
	}

	public void setDepartamentoId(Integer departamentoId) {
		this.departamentoId = departamentoId;
	}

	public Integer getDependenciaId() {
		return dependenciaId;
	}

	public void setDependenciaId(Integer dependenciaId) {
		this.dependenciaId = dependenciaId;
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

		
}
