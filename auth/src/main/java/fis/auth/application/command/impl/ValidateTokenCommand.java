package fis.auth.application.command.impl;

import fis.auth.application.command.TokenCommand;
import fis.auth.application.service.TokenService;
import fis.auth.domain.model.Token;

public class ValidateTokenCommand extends TokenCommand {

    private final String token;

    public ValidateTokenCommand(TokenService tokenService, String token) {
        super(tokenService);
        this.token = token;
    }

    @Override
    public Token execute() {
        return tokenService.validate(token);
    }
}
