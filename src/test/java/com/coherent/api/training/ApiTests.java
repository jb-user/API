package com.coherent.api.training;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ApiTests {
    @Test
    public void readScopeTokenIsNotEmptyTest() {
        String readToken = AuthTokenManager.getInstance().getReadToken();
        Assertions.assertFalse(readToken.isEmpty(), "Token for READ scope is empty.");
    }

    @Test
    public void writeScopeTokenIsNotEmptyTest() {
        String writeToken = AuthTokenManager.getInstance().getWriteToken();
        Assertions.assertFalse(writeToken.isEmpty(), "Token for WRITE scope is empty.");
    }
}
