package com.appoie.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appoie.commands.SalvarPublicacaoCommand;
import com.appoie.commands.EditarPublicacaoCommand;
import com.appoie.commands.FiltroCommand;
import com.appoie.dto.IconesDTO;
import com.appoie.dto.PublicacaoDetalhadaDTO;
import com.appoie.dto.PublicacaoMarcacaoDTO;
import com.appoie.dto.PublicacaoPreviaDTO;
import com.appoie.exceptions.QuantidadeFotosPublicacaoException;
import com.appoie.ids.PublicacaoId;
import com.appoie.services.PublicacaoService;
import com.appoie.utils.Sessao;

@CrossOrigin
@RestController
@RequestMapping("/publicacao")
public class PublicacaoController {

	@Autowired
	private PublicacaoService service;

	@RequestMapping(method = RequestMethod.GET, value = "/icones")
	public List<IconesDTO> icone() {
		return service.getIconesDTO();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/editar")
	public void editar(@RequestBody EditarPublicacaoCommand command) {
		service.editar(command);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/marcadores")
	public List<PublicacaoMarcacaoDTO> recuperarMarcadores(HttpSession session) {
		Sessao sessao = new Sessao(session);
		return service.getMarcadores(sessao.getCidadeId());
	}

	@RequestMapping(method = RequestMethod.GET, value = "detalhada/{id}")
	public PublicacaoDetalhadaDTO recuperarDetalhada(@PathVariable PublicacaoId id) {
		return service.getDetalhesPublicacao(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "previa/{id}")
	public PublicacaoPreviaDTO recuperarPrevia(@PathVariable PublicacaoId id) {
		return service.getPreviaPublicacao(id);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/salvar")
	public void salvar(@RequestBody SalvarPublicacaoCommand command, HttpSession session)
			throws QuantidadeFotosPublicacaoException {
		service.salvar(command, new Sessao(session));
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/excluir/{id}")
	public void deletar(@PathVariable PublicacaoId id) {
		service.excluir(id);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/marcadores/categoria")
	public List<PublicacaoMarcacaoDTO> recuperarMarcadoresPorCategoria(HttpSession session, @RequestBody FiltroCommand command) {
		Sessao sessao = new Sessao(session);
		return service.getMarcadoresPorCategoria(sessao.getCidadeId(), command);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/marcadores/data")
	public List<PublicacaoMarcacaoDTO> recuperarMarcadoresPorData(HttpSession session, @RequestBody FiltroCommand command) {
		Sessao sessao = new Sessao(session);
		return service.getMarcadoresPorData(sessao.getCidadeId(), command.dataInicio, command.dataFim);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/marcadores/tipo")
	public List<PublicacaoMarcacaoDTO> recuperarMarcadoresPorTipo(HttpSession session, @RequestBody FiltroCommand command) {
		Sessao sessao = new Sessao(session);
		return service.getMarcadoresPorTipo(sessao.getCidadeId(), command);
	}
	
	
}
