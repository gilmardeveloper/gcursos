package com.gilmarcarlos.developer.gcursos.model.eventos.presencial;

import java.beans.Transient;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

@Entity
public class PermissoesEventoPresencial implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ElementCollection
	private List<String> unidades;

	@ElementCollection
	private List<String> sexos;

	@ElementCollection
	private List<String> cargos;
	
	@ElementCollection
	private List<String> usuariosComCodigo;

	private String codigo;

	private String destinado;

	@OneToOne
	private EventoPresencial eventoPresencial;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<String> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<String> unidades) {
		this.unidades = unidades;
	}

	public List<String> getSexos() {
		return sexos;
	}

	public void setSexos(List<String> sexos) {
		this.sexos = sexos;
	}

	public List<String> getCargos() {
		return cargos;
	}

	public void setCargos(List<String> cargos) {
		this.cargos = cargos;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(Boolean codigo) {
		this.codigo = (codigo ? gerarToken() : null);
	}

	public String getDestinado() {
		return destinado;
	}

	public void setDestinado(String destinado) {
		this.destinado = destinado;
	}

	public EventoPresencial getEventoPresencial() {
		return eventoPresencial;
	}

	public void setEventoPresencial(EventoPresencial eventoPresencial) {
		this.eventoPresencial = eventoPresencial;
	}
		
	public List<String> getUsuariosComCodigo() {
		return usuariosComCodigo;
	}

	public void setUsuariosComCodigo(List<String> usuariosComCodigo) {
		this.usuariosComCodigo = usuariosComCodigo;
	}

	@Transient
	public Boolean precisaDeCodigo() {
		return (this.codigo != null ? (this.codigo.length() > 0) : false); 
	}
	
	@Transient
	public Boolean valida(Usuario usuario) {
		return temCargo(usuario) && temUnidade(usuario) && temSexo(usuario) && alvo(usuario);
	}

	@Transient
	private String gerarToken() {
		return UUID.randomUUID().toString().toUpperCase();
	}
	
	@Transient
	private Boolean temCargo(Usuario usuario) {
		return this.cargos.contains(usuario.getCodigoFuncional().getCargo().getNome()) || this.cargos.contains("todos");
	}
	
	@Transient
	private Boolean temUnidade(Usuario usuario) {
		return this.unidades.contains(usuario.getCodigoFuncional().getUnidadeTrabalho().getNome()) || this.unidades.contains("todos");
	}
	
	@Transient
	private Boolean temSexo(Usuario usuario) {
		return this.sexos.contains(usuario.getDadosPessoais().getSexo()) || this.sexos.contains("todos");
	}
	
	@Transient
	public Boolean temCodigo(Usuario usuario) {
		return this.usuariosComCodigo.contains(usuario.getEmail());
	}
	
	@Transient
	private Boolean alvo(Usuario usuario) {
		return this.destinado.equalsIgnoreCase(usuario.getCodigoFuncional().getTipo()) || this.destinado.equalsIgnoreCase("todos");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PermissoesEventoPresencial other = (PermissoesEventoPresencial) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
