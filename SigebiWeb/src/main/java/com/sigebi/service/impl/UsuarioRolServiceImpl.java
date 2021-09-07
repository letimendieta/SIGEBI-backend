package com.sigebi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigebi.dao.IUsuarioRolDao;
import com.sigebi.security.entity.Rol;
import com.sigebi.security.entity.Usuario;
import com.sigebi.security.entity.UsuarioRol;
import com.sigebi.security.service.UsuarioRolService;

@Service
public class UsuarioRolServiceImpl implements UsuarioRolService{
	
	@Autowired
	private IUsuarioRolDao usuarioRolDao;
		
	public UsuarioRolServiceImpl(IUsuarioRolDao usuarioRolDao) {
        this.usuarioRolDao = usuarioRolDao;
    }
	
	
	@Override
	@Transactional(readOnly = true)
	public List<UsuarioRol> findByUsuario(Usuario usuario) {
		return usuarioRolDao.findByUsuario(usuario);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> listarRolesUsuario(Usuario usuario) {
		List<UsuarioRol> usuariosRoles = usuarioRolDao.findByUsuario(usuario);
		List<String> listaRoles = new ArrayList<String>();
		
		for(UsuarioRol usuRol : usuariosRoles  ) {
			listaRoles.add(usuRol.getRol().getRolNombre().toString());
		}
		return listaRoles;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<UsuarioRol> findByRol(Rol rol) {
		return usuarioRolDao.findByRol(rol);
	}
		
	@Override
	@Transactional
	public void delete(int id) {
		usuarioRolDao.deleteById(id);
	}
}
