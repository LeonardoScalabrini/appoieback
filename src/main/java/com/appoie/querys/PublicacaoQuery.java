package com.appoie.querys;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appoie.commands.FiltroCommand;
import com.appoie.dto.PublicacaoDetalhadaDTO;
import com.appoie.dto.PublicacaoMarcacaoDTO;
import com.appoie.dto.PublicacaoPreviaDTO;
import com.appoie.exceptions.FiltroCategoriaPublicacaoException;
import com.appoie.exceptions.FiltroStatusException;
import com.appoie.exceptions.FiltroTipoPublicacaoException;
import com.appoie.ids.CidadeId;
import com.appoie.ids.PublicacaoId;
import com.appoie.ids.UsuarioId;
import com.appoie.models.Categoria;
import com.appoie.models.Status;
import com.appoie.models.TipoImagem;
import com.appoie.utils.FotoRepository;

@Component
public class PublicacaoQuery extends BasicQuery {

	@Autowired
	private FotoPublicacaoQuery fotoPublicacaoQuery;

	private FotoRepository fotoRepository = new FotoRepository(TipoImagem.JPG);

	@SuppressWarnings("unchecked")
	public List<PublicacaoMarcacaoDTO> getMarcadores(CidadeId cidadeId) {

		Query query = em.createNativeQuery("select p.id, p.latitude, p.longitude, p.categoria, "
				+ "p.qtd_apoiadores from publicacao p where " + "p.cidade_Id = :cidadeId");

		query.setParameter("cidadeId", cidadeId.getValue());
		List<Object[]> publicacoes = query.getResultList();

		List<PublicacaoMarcacaoDTO> commands = new ArrayList<>();
		for (Object[] publicacao : publicacoes) {

			Categoria categoria = Categoria.valueOf(publicacao[3].toString().toUpperCase());
			BigInteger qtdCurtidas = (BigInteger) publicacao[4];

			commands.add(new PublicacaoMarcacaoDTO(publicacao[0].toString(), (Double) publicacao[1],
					(Double) publicacao[2], categoria, qtdCurtidas.longValue()));
		}
		return commands;
	}

	public PublicacaoPreviaDTO getPreviaPublicacao(PublicacaoId id, UsuarioId usuarioId) {
		Query query = em.createNativeQuery(
				"select  p.id, p.titulo, p.qtd_apoiadores, p.status, f.endereco, CASE WHEN a.usuario_id= :idUsuario THEN 'S' ELSE 'N' END, a.id as idApoiador "
						+ "from publicacao p left join apoiador a on p.id = a.publicacao_id inner join foto_publicacao f on (p.id = f.publicacao_id)"
						+ " where p.id = :id and p.id = f.publicacao_id limit 1");
		query.setParameter("id", id.getValue());
		query.setParameter("idUsuario", usuarioId.getValue());

		Object[] publicacao = (Object[]) query.getSingleResult();

		BigInteger qtdApoiadores = (BigInteger) publicacao[2];

		return new PublicacaoPreviaDTO(publicacao[0].toString(), publicacao[1].toString(), qtdApoiadores.longValue(),
				Status.valueOf(publicacao[3].toString()), fotoRepository.getBase64(publicacao[4].toString()),
				publicacao[5].toString(), publicacao[6]);

	}

	public PublicacaoDetalhadaDTO getDetalhesPublicacao(PublicacaoId id) {
		Query query = em
				.createNativeQuery("select id, titulo, descricao, categoria, data_Publicacao, qtd_apoiadores, status"
						+ " from publicacao where id = :id");

		query.setParameter("id", id.getValue());

		Object[] publicacao = (Object[]) query.getSingleResult();

		return new PublicacaoDetalhadaDTO(publicacao[0].toString(), publicacao[1].toString(), publicacao[2].toString(),
				publicacao[3].toString(), publicacao[4].toString(), Integer.parseInt(publicacao[5].toString()),
				Status.valueOf(publicacao[6].toString()), fotoPublicacaoQuery.getFotosPublicacaoCommand(id));
	}

	public boolean verificaListaSituacoes(List<String> lista) {
		for(int i = 0; i < lista.size(); i++) {
			if(lista.get(i).equalsIgnoreCase("aberto") || lista.get(i).equalsIgnoreCase("fechado"))
				return true;
		}
		return false;
		
	}

	@SuppressWarnings("unchecked")
	public List<PublicacaoMarcacaoDTO> getMarcadoresFiltrados(CidadeId cidadeId, UsuarioId usuarioId,
			FiltroCommand command) throws FiltroCategoriaPublicacaoException, FiltroTipoPublicacaoException, FiltroStatusException {

		Query query = null;
		boolean filtroPorData = true;
		boolean minhasPublicacoes = true;
		if (command.dataInicio == null || command.dataFim == null) {
			filtroPorData = false;
		}
		if (!command.filtrarMinhasPublicacoes) {
			minhasPublicacoes = false;
		}
		if(verificaListaSituacoes(command.situacoes)) {
			throw new FiltroStatusException();
		}

		if (command.categorias.size() == 0) {
			throw new FiltroCategoriaPublicacaoException();
		} else if (command.situacoes.size() == 0) {
			throw new FiltroTipoPublicacaoException();
		}
		if (minhasPublicacoes && filtroPorData) {
			query = em.createNativeQuery("select p.id, p.latitude, p.longitude, p.categoria, "
					+ "p.qtd_apoiadores from publicacao p where " + "("
							+ "(p.cidade_Id = :cidadeId)"
					+ " and (p.data_publicacao between :dataInicio and :dataFim)"
					+ " and (p.status in (:status))"
					+ " and (p.usuario_id = :idUsuario)"
					+ " and (p.categoria in (:valoresCategorias))"
					+ ")");

			query.setParameter("cidadeId", cidadeId.getValue());
			query.setParameter("dataInicio", command.dataInicio);
			query.setParameter("dataFim", command.dataFim);
			query.setParameter("status", command.situacoes);
			query.setParameter("idUsuario", usuarioId.getValue());
			query.setParameter("valoresCategorias", command.categorias);
		} else if (minhasPublicacoes && !filtroPorData) {
			query = em.createNativeQuery("select p.id, p.latitude, p.longitude, p.categoria, "
					+ "p.qtd_apoiadores from publicacao p where " + "("
							+ "(p.cidade_Id = :cidadeId)"
					+ " and (p.status in (:status)) "
					+ "and (p.usuario_id = :idUsuario) "
					+ "and (p.categoria in (:valoresCategorias))"
					+ ")");

			query.setParameter("cidadeId", cidadeId.getValue());
			query.setParameter("status", command.situacoes);
			query.setParameter("idUsuario", usuarioId.getValue());
			query.setParameter("valoresCategorias", command.categorias);
		}

		else {
			query = em.createNativeQuery("select p.id, p.latitude, p.longitude, p.categoria, "
					+ "p.qtd_apoiadores from publicacao p where " + "("
							+ "(p.cidade_Id = :cidadeId)"
					+ "and (p.status in (:status)) "
					+ "and (p.categoria in (:valoresCategorias))"
					+ ")");

			query.setParameter("cidadeId", cidadeId.getValue());
			query.setParameter("status", command.situacoes);
			query.setParameter("valoresCategorias", command.categorias);
		}
		List<Object[]> publicacoes = query.getResultList();

		List<PublicacaoMarcacaoDTO> commands = new ArrayList<>();
		for (Object[] publicacao : publicacoes) {

			Categoria categoria = Categoria.valueOf(publicacao[3].toString().toUpperCase());
			BigInteger qtdCurtidas = (BigInteger) publicacao[4];

			commands.add(new PublicacaoMarcacaoDTO(publicacao[0].toString(), (Double) publicacao[1],
					(Double) publicacao[2], categoria, qtdCurtidas.longValue()));
		}
		return commands;
	}

}
