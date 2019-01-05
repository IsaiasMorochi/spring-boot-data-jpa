package com.springboot.app.models.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {

	Resource load(String filename) throws MalformedURLException;
	String copy(MultipartFile file) throws IOException;
	boolean delete(String filename);
	void deleteAll();
	void init() throws IOException;
}