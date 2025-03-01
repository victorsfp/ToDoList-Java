package br.com.victorfeitosa.todolist.Filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.victorfeitosa.todolist.user.IUserRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
// public class FilterTaskAuth implements Filter {
public class FilterTaskAuth extends OncePerRequestFilter {

     @Autowired //Gerencia todo ciclo do spring 
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();
        if(servletPath.startsWith("/tasks/")) {
            //Pegar autenticação - Usuário e Senha
            var authorization = request.getHeader("Authorization");
            var user_password = authorization.substring("Basic".length()).trim();
            byte[] authDecoded = Base64.getDecoder().decode(user_password);
            var authString = new String(authDecoded);
            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            System.out.println("Authorization");
            System.out.println(username);
            System.out.println(password);

            //Validar usuário 
            var user = this.userRepository.findByUsername(username);
            if(user == null) {
                response.sendError(401);
            }else{
                //Validar a Senha
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if(passwordVerify.verified){
                    //Segue
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                }else{
                    response.sendError(401);
                }
                
            }
        }else {
            filterChain.doFilter(request, response);
        }
        
    }

}
