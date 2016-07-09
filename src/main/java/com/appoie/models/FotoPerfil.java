package com.appoie.models;

import javax.persistence.Entity;
import javax.persistence.Lob;

import com.appoie.ids.UsuarioId;
import static com.appoie.utils.ValidationObject.*;

@Entity
public class FotoPerfil extends BasicEntity<UsuarioId>{
	
	public byte[] foto;
	
	private FotoPerfil(){
		super(new UsuarioId());
	}
	
	public FotoPerfil(UsuarioId id, byte[] foto) {
		super(id);
		isNull(id);
		this.foto = foto;
	}

	public byte[] getFoto() {
		return foto;
	}
	
	
}
