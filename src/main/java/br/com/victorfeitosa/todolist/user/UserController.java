package br.com.victorfeitosa.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

// @Controller Quando precisamos enviar algum template, ex: HTML
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired //Gerencia todo ciclo do spring 
    private IUserRepository userRepository;
    
    /**
     * String (texto)
     * Integer (números inteiros)
     * Double (números decimais)
     * Boolean (true/false)
     * ResponseEntity (retorno mais completo)
     * Float (float)
     * char (A,B,C...)
     * Date (data)
     * void
     */
    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {
        var user = this.userRepository.findByUsername(userModel.getUsername());

        if(user != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário ja existe");
        }

        var passwordHashred = BCrypt.withDefaults()
        .hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashred);
        
        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

}
