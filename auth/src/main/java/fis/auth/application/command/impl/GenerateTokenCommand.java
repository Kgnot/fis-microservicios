package fis.auth.application.command.impl;

import fis.auth.application.command.TokenCommand;
import fis.auth.application.service.TokenService;
import fis.auth.domain.model.Token;
import fis.auth.domain.model.TokenRequest;

public class GenerateTokenCommand extends TokenCommand {

    private final TokenRequest tokenRequest;

    public GenerateTokenCommand(TokenService tokenService, TokenRequest tokenRequest) {
        super(tokenService);
        this.tokenRequest = tokenRequest;
    }

    @Override
    public Token execute() {
        return tokenService.generateToken(tokenRequest);
    }
}
