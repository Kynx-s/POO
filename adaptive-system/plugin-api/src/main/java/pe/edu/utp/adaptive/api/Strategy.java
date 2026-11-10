package pe.edu.utp.adaptive.api;

public interface Strategy<I, O> {

    String id();

    boolean supports(Context context);

    Result<O> execute(I input);
}