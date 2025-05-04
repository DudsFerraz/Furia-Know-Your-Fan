package com.knowYourfan.backEnd.DTOs;

import java.util.Set;

public record UserCreationDTO(String name, String nickname, String cpf, String email, String password, String pais,
                              String estado, String cidade, String cep, Set<String> interests) {
}
