package com.sigebi.security.dto;


import javax.validation.constraints.NotBlank;

import com.sigebi.security.entity.Rol;
import com.sigebi.security.entity.Usuario;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NuevoUsuario {
    
    /*@NotBlank
    private String nombreUsuario;
    
    @NotBlank
    private String password;*/
    
    private Usuario usuario;
	
    private Set<String> roles = new HashSet<>();

    private List<Rol> rolesList = new ArrayList<Rol>();
    
    private boolean esCambioContrasenha;
    
    /*public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }*/

    public Set<String> getRoles() {
        return roles;
    }

    public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

	public List<Rol> getRolesList() {
		return rolesList;
	}

	public void setRolesList(List<Rol> rolesList) {
		this.rolesList = rolesList;
	}

	public boolean isEsCambioContrasenha() {
		return esCambioContrasenha;
	}

	public void setEsCambioContrasenha(boolean esCambioContrasenha) {
		this.esCambioContrasenha = esCambioContrasenha;
	}
	
}
