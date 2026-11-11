package pe.edu.utp.adaptive.core;

import java.util.Optional;
import pe.edu.utp.adaptive.api.Context;

@FunctionalInterface
public interface Rule {
    Optional<Decision> evaluate(Context context);
}