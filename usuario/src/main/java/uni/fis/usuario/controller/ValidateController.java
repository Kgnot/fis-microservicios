package uni.fis.usuario.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.fis.usuario.dto.request.TokenRequest;
import uni.fis.usuario.dto.request.UserValidateRequest;
import uni.fis.usuario.service.PasswordService;
import uni.fis.usuario.service.UserService;

@RestController
@RequestMapping("/api/v1/validate")
@RequiredArgsConstructor
public class ValidateController {

    private final UserService userService;
    private final PasswordService passwordService;

    @PostMapping("/auth/verify")
    public ResponseEntity<?> validateUser(@RequestBody UserValidateRequest userValidateRequest) {
        var validate = passwordService.validate(userValidateRequest.id(), userValidateRequest.password());
        if (validate) {
            var user = userService.findById(userValidateRequest.id());
            if (user.isEmpty()) {
                return ResponseEntity
                        .of(ProblemDetail.forStatus(403))
                        .build();
            }
            return ResponseEntity.ok(
                    new TokenRequest(
                            user.get().id(),
                            user.get().idRol()
                    )
            );
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(403));
    }

}
