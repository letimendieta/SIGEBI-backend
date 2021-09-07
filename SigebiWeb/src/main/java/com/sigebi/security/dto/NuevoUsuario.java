package com.sigebi.security.dto;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sigebi.security.entity.Rol;
import com.sigebi.security.entity.Usuario;

public class NuevoUsuario {
          
    private Usuario usuario;
	
    private Set<String> roles = new HashSet<>();

    private List<Rol> rolesList = new ArrayList<Rol>();
    
    private boolean esCambioContrasenha;
    
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
