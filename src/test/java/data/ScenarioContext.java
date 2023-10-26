package data;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {
    public enum Context {
        USER_NAME, PASSWORD;
    }

    @Getter
    private final Map<Context, Object> scenarioContext = new HashMap<>();

    public void setContext(Context key, Object value) {
        scenarioContext.put(key, value);
    }

    public Object getContext(Context key) {
        return scenarioContext.get(key);
    }

    public Boolean contains(Context key) {
        return scenarioContext.containsKey(key);
    }
}
