package com.gml.cursomc.services;

import com.gml.cursomc.domain.Cliente;
import com.gml.cursomc.repositories.ClienteRepository;
import com.gml.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService emailService;

    private Random random = new Random();

    public void sendNewPassword(String email) {


        Cliente cliente = clienteRepository.findByEmail(email);

        if (cliente == null) {
            throw new ObjectNotFoundException("Email não encontrado");
        }

        String newPass = newPassword();
        cliente.setPassword(bCryptPasswordEncoder.encode(newPass));

        clienteRepository.save(cliente);
        emailService.sendNewPassordEmail(cliente, newPass);

    }

    private String newPassword(){
        char[] vet = new char[10];

        for (int i=0; i< 10; i++){
            vet[i] = randomChar();
        }

        return new String(vet);
    }

    private char randomChar(){
        int opt = random.nextInt(3);
        if (opt == 0){ //gera um digito
            return (char) (random.nextInt(10) + 48);
        } else if (opt == 1) { //gera letra minuscula
            return (char) (random.nextInt(26) + 65);
        } else { //gera letra minuscula
            return (char) (random.nextInt(26) + 97);
        }

    }

}
