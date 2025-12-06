package fis.auth.domain.usecase;

import fis.auth.domain.model.SignIn;

import java.time.LocalDate;
import java.time.Period;

public class MenorDeEdadUseCase {

    public static boolean execute(SignIn signIn) {
        // la fecha es yyyy-mm-dd
        // primero vamos a usar el año el mes y el día para evaluar el dia.
        LocalDate fechaNacimiento = LocalDate.parse(signIn.fechaNacimiento());
        LocalDate hoy = LocalDate.now();
        int edad = Period.between(fechaNacimiento, hoy).getYears();

        return edad < 18;
    }

}
