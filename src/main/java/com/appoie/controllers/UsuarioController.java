package com.appoie.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.appoie.commands.CadastrarCommand;
import com.appoie.commands.AlterarEmailCommand;
import com.appoie.commands.AlterarPerfilCommand;
import com.appoie.commands.AlterarSenhaCommand;
import com.appoie.commands.AutenticarCommand;
import com.appoie.commands.RecuperarSenhaCommand;
import com.appoie.dto.InformacoesUsuarioDTO;
import com.appoie.services.UsuarioService;
import com.appoie.utils.Sessao;

@CrossOrigin
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@RequestMapping(method = RequestMethod.POST)
	public void cadastrar(@RequestBody CadastrarCommand command) throws Exception {
		usuarioService.cadastrar(command);
	}
	
	@RequestMapping(value="/perfil", method=RequestMethod.GET)
	public @ResponseBody AlterarPerfilCommand buscarPerfil() throws Exception{
		return usuarioService.getPerfil();
	}
	
	@RequestMapping(value="/perfil", method=RequestMethod.PUT)
	public void alterarPerfil(@RequestBody AlterarPerfilCommand perfilCommand) throws Exception{
		usuarioService.alterarPerfil(perfilCommand);
	}

	@RequestMapping(value="/email", method=RequestMethod.PUT)
	public void alterarEmail(@RequestBody AlterarEmailCommand emailCommand) throws Exception{
		usuarioService.alterarEmail(emailCommand, Sessao.getUsuarioId());
	}
	
	@RequestMapping(value="/senha", method=RequestMethod.PUT)
	public void alterarSenha(@RequestBody AlterarSenhaCommand senhaCommand) throws Exception{
		usuarioService.alterarSenha(senhaCommand, Sessao.getUsuarioId());	
	}
	
	@RequestMapping(value="/auth", method=RequestMethod.POST)
	public InformacoesUsuarioDTO logar(@RequestBody AutenticarCommand autenticarCommand) throws Exception{
		return usuarioService.logar(autenticarCommand);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/recuperarSenha")
	public void recuperarSenha(@RequestBody RecuperarSenhaCommand command) throws Exception {
		usuarioService.recuperarSenha(command);
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/estado/{estado}")
	public InformacoesUsuarioDTO pegarEstado(@PathVariable String estado) throws Exception {
		return usuarioService.atribuirEstadoUsuarioLogado(estado);
	}
}