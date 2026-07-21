package com.BarberHub.demo.services.barbeiro;

import com.BarberHub.demo.entities.Barbearia;
import com.BarberHub.demo.entities.Barbeiro;
import com.BarberHub.demo.entities.DTOS.barbeiro.BarbeiroRequestDTO;
import com.BarberHub.demo.entities.DTOS.barbeiro.BarbeiroResponseDTO;
import com.BarberHub.demo.entities.DTOS.user.RegisterUserDTO;
import com.BarberHub.demo.entities.ENUMS.RoleUser;
import com.BarberHub.demo.entities.ENUMS.StatusUsers;
import com.BarberHub.demo.entities.User;
import com.BarberHub.demo.exceptions.InvalidRoleException;
import com.BarberHub.demo.exceptions.NotFoundException;
import com.BarberHub.demo.repositories.BarbeariaRepository;
import com.BarberHub.demo.repositories.BarbeiroRepository;
import com.BarberHub.demo.repositories.UserRepository;
import com.BarberHub.demo.services.authentication.CreateUserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BarbeiroService {

    private final BarbeiroRepository barbeiroRepository;
    private final BarbeariaRepository barbeariaRepository;
    private final CreateUserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public BarbeiroService(BarbeiroRepository barbeiroRepository, BarbeariaRepository barbeariaRepository, CreateUserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.barbeiroRepository = barbeiroRepository;
        this.barbeariaRepository = barbeariaRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //ADMIN
    public List<BarbeiroResponseDTO> findAllBarbeiros(String token){
        User user = userService.findUserByToken(token);
        if(user.getRole() != RoleUser.ADMIN) throw new InvalidRoleException("Você não tem permissão para fazer esta ação");

        List<Barbeiro> barbeiros = barbeiroRepository.findAll();
        return barbeiros.stream().map(BarbeiroResponseDTO::new).toList();
    }

    //CLIENTE, BARBEIRO, BARBEARIA
    public List<BarbeiroResponseDTO> findAllBarbeirosByBarbeariaId(Long id, String token){
        userService.findUserByToken(token);
        Barbearia barbearia = barbeariaRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe barbearia com este ID"));
        List<Barbeiro> barbeiros = barbearia.getBarbeiros();
        return  barbeiros.stream().map(BarbeiroResponseDTO::new).toList();
    }

    //CLIENTE, BARBEIRO, BARBEARIA
    public BarbeiroResponseDTO findBarbeiroById(Long id, String token){
        userService.findUserByToken(token);
        Barbeiro barbeiro = barbeiroRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe barbeiro com este ID"));
        return  new BarbeiroResponseDTO(barbeiro);
    }

    //BARBEARIA
    @Transactional
    public BarbeiroResponseDTO createBarbeiro(RegisterUserDTO data, String token){
        User user = userService.findUserByToken(token);
        if(user.getBarbearia() == null){
            throw new InvalidRoleException("É necessário estar logado como como uma barbearia para criar um barbeiro");
        }
        if(userRepository.existsByEmail(data.email())){
            throw new ArithmeticException("Este email já está sendo utilizado");
        }
        User newUser = User.builder().email(data.email()).password(passwordEncoder.encode(data.password())).role(RoleUser.BARBEIRO).build();
        User savedUser = userRepository.save(newUser);
        Barbeiro newBarbeiro = Barbeiro.builder().nome(data.nome()).telefone(data.telefone()).user(savedUser).barbearia(user.getBarbearia()).status(StatusUsers.ATIVO).build();
        Barbeiro savedBarbeiro = barbeiroRepository.save(newBarbeiro);
        return new BarbeiroResponseDTO(savedBarbeiro);
    }

    //BARBEIRO
    @Transactional
    public BarbeiroResponseDTO updateBarbeiro(Long id, BarbeiroRequestDTO data, String token){
        User user = userService.findUserByToken(token);
        if(user.getBarbeiro() == null) throw new InvalidRoleException("Você não é um barbeiro");
        Barbeiro barbeiro = barbeiroRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe barbeiro com este ID"));
        if(!Objects.equals(user.getBarbeiro().getId(), barbeiro.getId())) throw new InvalidRoleException("Você não tem permissão para esta ação");
        barbeiro.setNome(data.nome());
        barbeiro.setTelefone(data.telefone());
        barbeiro.setBarbearia(barbeariaRepository.findById(data.barbeariaId()).orElseThrow(() -> new NotFoundException("Não existe barbearia com este ID")));
        barbeiro.setStatus(data.status());
        barbeiroRepository.save(barbeiro);
        return new BarbeiroResponseDTO(barbeiro);
    }

    //ADMIN
    @Transactional
    public void deleteBarbeiro(Long id, String token){
        User user = userService.findUserByToken(token);
        if(user.getRole() != RoleUser.ADMIN) throw new InvalidRoleException("Você não possui permissão para esta ação");
        try{
            barbeiroRepository.deleteById(id);
        }catch (NotFoundException e){
            throw new NotFoundException("Não existe barbeiro com este ID");
        }
    }


}
