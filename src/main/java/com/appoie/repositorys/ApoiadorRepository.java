package com.appoie.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.appoie.ids.ApoiadorId;
import com.appoie.models.Apoiador;

public interface ApoiadorRepository extends JpaRepository<Apoiador, ApoiadorId>{

}
