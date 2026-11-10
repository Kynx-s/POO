package pe.edu.utp.adaptive.core;

import pe.edu.utp.adaptive.api.Context;
import pe.edu.utp.adaptive.api.Result;

public record Outcome(Context context, Decision decision, Result<String> result) {
}