package Exceptions;

public class NoArgumentsInCommand extends Exception {
    public NoArgumentsInCommand() {
        super("This command does not contain arguments.");
    }
}
