package uni.fis.usuario.service;

import org.springframework.stereotype.Service;
import uni.fis.usuario.entity.PasswordEntity;
import uni.fis.usuario.repository.PasswordRepository;

import java.util.Optional;

@Service
public class PasswordServiceImpl implements PasswordService {

    private final PasswordRepository repository;

    public PasswordServiceImpl(PasswordRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean validate(int idUser, String password) {
        //primero encontramos todas y filtramos la ultima
        Optional<PasswordEntity> ultima = repository.findMasRecienteByIdUsuario(idUser);
        return ultima
                .map(passwordEntity -> passwordEntity.getChars().equals(password))
                .orElse(false);

    }
}
