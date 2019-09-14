package com.controllers;


import com.models.service.IUploadFileService;
import com.models.entity.Cliente;
import com.models.service.IClienteService;
import com.util.paginator.PageRender;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@SessionAttributes("cliente")
public class ClienteController {

	private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

	@Autowired private IClienteService clienteService;

	@Autowired private IUploadFileService uploadFileService;

	@Autowired private MessageSource messageSource;

	@Secured({"ROLE_USER","ROLE_ADMIN"})
	@GetMapping(value="/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			logger.error("ERROR vert foto ", e);
			e.printStackTrace();
		}
		//Recuperamos la imagen de manera programatica atraves de un resource
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +  recurso.getFilename() +"\"")
				.body(recurso);
	}

//	@Secured({"ROLE_USER"})
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash, Locale locale) {
		Cliente cliente = clienteService.fetchByIdWithFacturas(id);
		if (cliente == null) {
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
			return "redirect:/listar";
		}

		model.put("cliente", cliente);
		model.put("titulo", messageSource.getMessage("text.cliente.detalle.titulo", null, locale).concat(": ").concat(cliente.getNombre()));
		return "ver";
	}

	@RequestMapping(value = {"/listar","/"}, method = RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue="0") int page, Model model, Authentication authentication, HttpServletRequest request, Locale locale) {

		if (authentication != null){
			logger.info("Hola usuario autenticado, tu username es: ".concat(authentication.getName()));
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if(auth != null) {
			logger.info("Utilizando forma estática SecurityContextHolder.getContext().getAuthentication(): Usuario autenticado: ".concat(auth.getName()));
		}

		// 1er forma
		if(hasRole("ROLE_ADMIN")) {
			logger.info("Hola ".concat(auth.getName()).concat(" tienes acceso!"));
		} else {
			logger.info("Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
		}

		//2da forma
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "");

		if(securityContext.isUserInRole("ROLE_ADMIN")) {
			logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName()).concat(" tienes acceso!"));
		} else {
			logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
		}

		//3era forma
		if(request.isUserInRole("ROLE_ADMIN")) {
			logger.info("Forma usando HttpServletRequest: Hola ".concat(auth.getName()).concat(" tienes acceso!"));
		} else {
			logger.info("Forma usando HttpServletRequest: Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
		}

		Pageable pageRequest = PageRequest.of(page, 4); //spring 5
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);

		model.addAttribute("titulo",messageSource.getMessage("text.cliente.listar.titulo",null,locale));
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);

		return "listar";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model, Locale locale) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", messageSource.getMessage("text.cliente.form.titulo.crear", null, locale));
		return "form";
	}

//	@Secured("ROLE_ADMIN")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash, Locale locale) {
		
//		Optional<Cliente> cliente = null;
		Cliente cliente = clienteService.findById(id);
		if(id > 0) {

			if(cliente == null) {
				flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", messageSource.getMessage("text.cliente.flash.id.error", null, locale));
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", messageSource.getMessage("text.cliente.form.titulo.editar", null, locale));
		return "form";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status, Locale locale) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", messageSource.getMessage("text.cliente.form.titulo", null, locale));
			return "form";
		}

		if (!foto.isEmpty()) {

			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) {

				uploadFileService.delete(cliente.getFoto());
			}

			String uniqueFilename = null;
			try {
				uniqueFilename = uploadFileService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			flash.addFlashAttribute("info", messageSource.getMessage("text.cliente.flash.foto.subir.success", null, locale) + "'" + uniqueFilename + "'");
			cliente.setFoto(uniqueFilename);

		}

		String mensajeFlash = (cliente.getId() != null) ? messageSource.getMessage("text.cliente.flash.editar.success", null, locale) : messageSource.getMessage("text.cliente.flash.crear.success", null, locale);

		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash, Locale locale) {
		Cliente cliente = clienteService.findById(id);

		if (id > 0) {
			clienteService.delete(id);
			flash.addFlashAttribute("success", messageSource.getMessage("text.cliente.flash.eliminar.success", null, locale));

			if (uploadFileService.delete(cliente.getFoto())) {
				String mensajeFotoEliminar = String.format(messageSource.getMessage("text.cliente.flash.foto.eliminar.success", null, locale), cliente.getFoto());
				flash.addFlashAttribute("info", mensajeFotoEliminar);
			}
		}
		return "redirect:/listar";
	}

	private boolean hasRole(String role) {
		SecurityContext context = SecurityContextHolder.getContext();
		if(context == null) {
			return false;
		}
		Authentication auth = context.getAuthentication();
		if(auth == null) {
			return false;
		}
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		return authorities.contains(new SimpleGrantedAuthority(role));
		/* // nos permite obtener el ROLE_
		   for(GrantedAuthority authority: authorities) {
			if(role.equals(authority.getAuthority())) {
				logger.info("Hola usuario ".concat(auth.getName()).concat(" tu role es: ".concat(authority.getAuthority())));
				return true;
			}
		}
		return false; */
	}
}
