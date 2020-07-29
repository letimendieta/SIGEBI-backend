package com.sigebi.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.sigebi.service.FilesStorageService;
import com.sigebi.service.UtilesService;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {
	@Autowired
	private UtilesService utiles;

  private final Path rootHistorialClinico = Paths.get("historial");
  private final Path rootImages = Paths.get("imagenes");

  @Override
  public void init(String tipo) throws Exception {
    try {
	 Path path = obtenerPath(tipo);
	
	 if(path == null) {
	 	throw new Exception("No se ha encontrado el path de la carpeta para guardar el archivo");
	 }
      Files.createDirectory(path);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for upload!");
    }
  }

  @Override
  public void save(MultipartFile file, String tipo) {
    try {
    	if(utiles.isNullOrBlank(tipo)) {
    		throw new Exception("Se requiere tipo para guardar el archivo");
    	}
    	Path path = obtenerPath(tipo);
    	
    	if(path == null) {
    		throw new Exception("No se ha encontrado el path de la carpeta para guardar el archivo");
    	}
    	
      Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception e) {
        throw new RuntimeException("El archivo ya existe. Error: " + e.getMessage());
    }
  }

  @Override
  public Resource load(String filename, String tipo) throws Exception {
    try {
      
      Path path = obtenerPath(tipo);
  	
	  if(path == null) {
	  	throw new Exception("No se ha encontrado el path de la carpeta para guardar el archivo");
	  }
	  
	  Path file = path.resolve(filename);
	  	
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("No se encontraron archivos!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public void deleteAll(String tipo) throws Exception {
	Path path = obtenerPath(tipo);
	  	
	if(path == null) {
	  throw new Exception("No se ha encontrado el path de la carpeta para guardar el archivo");
	}
    FileSystemUtils.deleteRecursively(path.toFile());
  }
  
  @Override
  public void delete(String name, String tipo) throws Exception {
	Path path = obtenerPath(tipo);
	  	
	if(path == null) {
	  throw new Exception("No se ha encontrado el path de la carpeta para guardar el archivo");
	}
	
	Path file = path.resolve(name);
	
	Resource resource = new UrlResource(file.toUri());

    if (resource.exists() || resource.isReadable()) {
    	FileSystemUtils.deleteRecursively(file);
    } else {
      throw new RuntimeException("No se encontraron archivos!");
    }
    
  }

  @Override
  public Stream<Path> loadAll(String tipo) throws Exception {
    try {
      Path pathLocal = obtenerPath(tipo);
      	
  	  if(pathLocal == null) {
  	  	throw new Exception("No se ha encontrado el path de la carpeta para guardar el archivo");
  	  }
      return Files.walk(pathLocal, 1).filter(path -> !path.equals(pathLocal)).map(pathLocal::relativize);
    } catch (IOException e) {
      throw new RuntimeException("Could not load the files!");
    }
  }
  
  private Path obtenerPath(String tipo) {
	 Path path = null;
	 
	 if("H".equals(tipo)) {
  		path = this.rootHistorialClinico;
  	}else if("I".equals(tipo)) {
  		path = this.rootImages;
  	}
	  
	return path;	  
  }

}
