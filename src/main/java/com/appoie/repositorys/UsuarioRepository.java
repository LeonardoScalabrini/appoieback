package com.appoie.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appoie.ids.UsuarioId;
import com.appoie.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, UsuarioId>{

}
