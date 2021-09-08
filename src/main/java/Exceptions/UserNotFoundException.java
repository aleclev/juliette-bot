package Exceptions;

public class UserNotFoundException extends APIException {

    public UserNotFoundException(Long id) {
        super("Error! No user could be found with ID: " + id);
    }
}
