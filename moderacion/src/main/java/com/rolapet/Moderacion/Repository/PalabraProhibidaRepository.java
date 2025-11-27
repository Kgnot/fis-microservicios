package com.rolapet.Moderacion.Repository;

import com.rolapet.Moderacion.Domain.entity.PalabraProhibida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PalabraProhibidaRepository extends JpaRepository<PalabraProhibida, Long> {
    List<PalabraProhibida> findByActivaTrue();
}
