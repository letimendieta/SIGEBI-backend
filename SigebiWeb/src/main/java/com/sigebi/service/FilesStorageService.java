package com.sigebi.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
  public void init(String tipo) throws Exception;

  public void save(MultipartFile file, String tipo) throws Exception;

  public Resource load(String filename, String tipo) throws Exception;

  public void deleteAll(String tipo) throws Exception;
  
  public void delete(String name, String tipo) throws Exception;

  public Stream<Path> loadAll(String tipo) throws Exception;
}
