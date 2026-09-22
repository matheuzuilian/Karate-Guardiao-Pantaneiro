package com.ifms.lp3spring.Repository;

import com.ifms.lp3spring.model.ResponsavelModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ResponsavelRepository extends JpaRepository<ResponsavelModel, Long> {

    // Busca responsável pelo CPF para validações de duplicidade
    Optional<ResponsavelModel> findByCpf(String cpf);

    // Permite buscar responsáveis pelo nome no sistema
    List<ResponsavelModel> findByNomeContainingIgnoreCase(String nome);
}