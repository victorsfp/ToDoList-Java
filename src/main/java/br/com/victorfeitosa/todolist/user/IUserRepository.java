package br.com.victorfeitosa.todolist.user;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

//Primeiro Parametro - Tipo Do Model do Repository
//Segundo Parametro - Tipo do ID da entidade
public interface IUserRepository extends JpaRepository<UserModel, UUID> {
    UserModel findByUsername(String username); //finBy<Attribute> Spring ja entende que fazer o select com essa propriedade
}
