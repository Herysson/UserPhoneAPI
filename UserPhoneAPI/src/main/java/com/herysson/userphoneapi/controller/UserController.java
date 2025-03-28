package com.herysson.userphoneapi.controller;

import com.herysson.userphoneapi.model.User;
import com.herysson.userphoneapi.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        user.getPhones().forEach(phone -> phone.setUser(user));
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> atualizarUsuario(@PathVariable Long id, @RequestBody User dadosAtualizados) {
    
        // 1. Procura o usuário pelo ID
        Optional<User> usuarioExistente = userRepository.findById(id);
    
        if (usuarioExistente.isPresent()) {
            User usuario = usuarioExistente.get();
    
            // 2. Atualiza os dados básicos (nome e email)
            usuario.setName(dadosAtualizados.getName());
            usuario.setEmail(dadosAtualizados.getEmail());
    
            // 3. Atualiza a lista de telefones:
            // Primeiro, remove os telefones antigos
            usuario.getPhones().clear();
    
            // Depois, adiciona os novos telefones vindos da requisição
            for (Phone telefone : dadosAtualizados.getPhones()) {
                telefone.setUser(usuario); // define a referência correta do usuário
                usuario.getPhones().add(telefone);
            }
    
            // 4. Salva o usuário atualizado no banco de dados
            User usuarioAtualizado = userRepository.save(usuario);
    
            // 5. Retorna o usuário atualizado como resposta
            return ResponseEntity.ok(usuarioAtualizado);
        } else {
            // Se o usuário não for encontrado, retorna erro 404
            return ResponseEntity.notFound().build();
        }

}

