package com.gilmarcarlos.developer.gcursos.security.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.auth.Autorizacao;
import com.gilmarcarlos.developer.gcursos.model.auth.Privilegio;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.repository.usuarios.UsuarioRepository;
import com.gilmarcarlos.developer.gcursos.security.brute.force.AutenticacaoBlockForcaBruta;
import com.gilmarcarlos.developer.gcursos.security.exception.BruteForceException;
import com.gilmarcarlos.developer.gcursos.security.exception.RegistroNotFoundException;

/**
 * Classe com configurações de segurança do spring security para usuário
 * 
 * @author Gilmar Carlos
 *
 */
@Service("userDetailsService")
@Transactional
public class UsuarioService implements UserDetailsService {
 
	@Autowired
    private HttpServletRequest request;
	
    @Autowired
    private UsuarioRepository usuarioRepository;
  
    @Autowired
    private MessageSource messages;
  
    @Autowired
	private AutenticacaoBlockForcaBruta blockForcaBruta;
    
    @Override
    public UserDetails loadUserByUsername(String email)
      throws UsernameNotFoundException {
    	
    	String ip = getClientIP();
        if (blockForcaBruta.isBlocked(ip)) {
            throw new BruteForceException();
        }
    	    	
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
        	throw new RegistroNotFoundException();
        }
        
                            
        return new org.springframework.security.core.userdetails.User(
          usuario.getEmail(), usuario.getSenha(), usuario.isHabilitado(), true, true, 
          true, getAuthorities(usuario.getAutorizacoes()));
    }
     
    
   
    private String getClientIP() {
    	String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
	}


	public MessageSource getMessage() {
    	return messages;
    }
    
    
    private Collection<? extends GrantedAuthority> getAuthorities(
      Collection<Autorizacao> autorizacoes) {
  
        return getGrantedAuthorities(getPrivilegios(autorizacoes));
    }
 
    private List<String> getPrivilegios(Collection<Autorizacao> autorizacoes) {
  
        List<String> privilegios = new ArrayList<>();
        List<Privilegio> collection = new ArrayList<>();
        for (Autorizacao autorizacao : autorizacoes) {
            collection.addAll(autorizacao.getPrivilegios());
        }
        for (Privilegio item : collection) {
            privilegios.add(item.getNome());
        }
        return privilegios;
    }
    
   
    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
