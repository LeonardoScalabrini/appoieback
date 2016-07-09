package com.appoie.models;

import javax.persistence.Entity;
import javax.persistence.Lob;

import com.appoie.ids.UsuarioId;
import static com.appoie.utils.ValidationObject.*;

import java.util.Arrays;

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

<<<<<<< HEAD
	public byte[] getFoto() {
		return foto;
=======
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(foto);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FotoPerfil other = (FotoPerfil) obj;
		if (!Arrays.equals(foto, other.foto))
			return false;
		return true;
>>>>>>> c72c8b950a938951549416e14a65af3c8d072e39
	}
	
	
}
