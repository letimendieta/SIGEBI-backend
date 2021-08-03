package com.sigebi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.sigebi.clases.FileInfo;
import com.sigebi.clases.ResponseMessage;
import com.sigebi.service.FilesStorageService;
import com.sigebi.util.exceptions.SigebiException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/archivos")
public class FilesController {
  @Autowired
  FilesStorageService storageService;

  @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file, 
		  @RequestParam("tipo") String tipo) throws SigebiException {
		String message = "";
		if (file.isEmpty()) {
		    message = "Seleccionar un archivo";
		    return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		}
		storageService.save(file, tipo);
		message = "Archivo subido exitosamente: " + file.getOriginalFilename();
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

  }

  @GetMapping("/filesFolder/{tipo}")
  public ResponseEntity<List<FileInfo>> getListFiles(@PathVariable("tipo") String tipo) throws Exception {
    List<FileInfo> fileInfos = storageService.loadAll(tipo).map(path -> {
      String filename = path.getFileName().toString();
      String url = MvcUriComponentsBuilder
          .fromMethodName(FilesController.class, "getFile", path.getFileName().toString(), tipo).build().toString();

      return new FileInfo(filename, url);
    }).collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
  }
  
  @GetMapping(value = "/filesName/{name}/{tipo}")
  public ResponseEntity<List<FileInfo>> getListFilesName(@PathVariable("name") String name, 
		  @PathVariable("tipo") String tipo) throws Exception {
	  
    List<FileInfo> fileInfos = storageService.loadAll(tipo).map(path -> {
      String filename = path.getFileName().toString();
      if(filename.contains(name)){
	      String url = MvcUriComponentsBuilder
	          .fromMethodName(FilesController.class, "getFile", path.getFileName().toString(), tipo).build().toString();
	
	      return new FileInfo(filename, url);
      }
      return null;
    }).collect(Collectors.toList());
    
    List<FileInfo> filesList = new ArrayList<FileInfo>(fileInfos);
    for(FileInfo file : filesList) {
    	if(file == null) {
    		fileInfos.remove(file);
    	}
    }

    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
  }

  @GetMapping("/files/{filename:.+}")
  public ResponseEntity<Resource> getFile(@PathVariable String filename,
		  @PathVariable("tipo") String tipo) throws Exception {
    Resource file = storageService.load(filename, tipo);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }
}
