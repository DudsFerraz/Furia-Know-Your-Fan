package com.knowYourfan.backEnd;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

//CEP validator

@Getter
@Embeddable
public class Endereco implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String pais;
    private String estado;
    private String cidade;
    private String CEP;

    public Endereco() {}
    public Endereco(String pais, String estado, String cidade, String CEP) {
        setPais(pais);
        setEstado(estado);
        setCidade(cidade);
        setCEP(CEP);
    }

    public void setPais(String pais) {
        if (pais == null) throw new IllegalArgumentException("Pais invalido");
        this.pais = pais;
    }
    public void setEstado(String estado) {
        if (estado == null) throw new IllegalArgumentException("estado invalido");
        this.estado = estado;
    }
    public void setCEP(String CEP) {
        if (CEP == null) throw new IllegalArgumentException("CEP invalido");
        this.CEP = CEP;
    }
    public void setCidade(String cidade) {
        if (cidade == null) throw new IllegalArgumentException("Cidade invalida");
        this.cidade = cidade;
    }


    @Override
    public String toString() {
        return String.format("""
                        País: %s | Estado: %s
                        Cidade: %s | CEP: %s |
                        """,
                this.pais, this.estado, this.cidade, this.CEP);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Endereco endereco = (Endereco) o;
        return Objects.equals(pais, endereco.pais) && Objects.equals(estado, endereco.estado) && Objects.equals(getCidade(), endereco.getCidade()) && Objects.equals(getCEP(), endereco.getCEP());
    }

    @Override
    public int hashCode() {
        return Objects.hash(pais, estado, getCidade(), getCEP());
    }
}
