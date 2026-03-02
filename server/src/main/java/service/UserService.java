package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {

    private final DataAccess dao;

    public UserService(DataAccess dao) {
        this.dao = dao;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult registerUser(RegisterRequest registerRequest) throws DataAccessException {
        if (registerRequest.username() == null || registerRequest.username().isEmpty() ||
            registerRequest.password() == null || registerRequest.password().isEmpty() ||
            registerRequest.email() == null || registerRequest.email().isEmpty()) {
            throw new DataAccessException("Invalid input");
        }
        if (dao.getUser(registerRequest.username()) != null) {
            throw new DataAccessException("User already exists");
        }
        UserData newUser = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        dao.createUser(newUser);

        String token = generateToken();
        dao.createAuth(new AuthData(token, registerRequest.username()));

        return new RegisterResult(registerRequest.username(), token);
    }

    public LoginResult login(LoginResult loginResult) {

    }
}
