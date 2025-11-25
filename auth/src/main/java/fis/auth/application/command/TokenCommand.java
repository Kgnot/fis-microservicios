package fis.auth.application.command;

import fis.auth.application.service.TokenService;
import fis.auth.domain.model.Token;

public abstract class TokenCommand {

    protected TokenService tokenService;

    public TokenCommand(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public abstract Token execute();
}
