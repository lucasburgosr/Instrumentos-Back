package com.example.Repository;

import com.example.Entity.Instrumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstrumentoRepository extends JpaRepository<Instrumento,Long> {
    List<Instrumento> findByCategoriaId(Long id);
}

