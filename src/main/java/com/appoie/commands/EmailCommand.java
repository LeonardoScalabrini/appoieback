package com.appoie.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailCommand {

	public final String emailAtual;
	public final String novoEmail;
	public final String confirmarNovoEmail;
	
	@JsonCreator
	public EmailCommand(@JsonProperty("emailAtual") String emailAtual,
						@JsonProperty("novoEmail") String novoEmail,
						@JsonProperty("confirmarNovoEmail") String confirmarNovoEmail){
		this.emailAtual = emailAtual;
		this.novoEmail = novoEmail;
		this.confirmarNovoEmail = confirmarNovoEmail;
	}
}
