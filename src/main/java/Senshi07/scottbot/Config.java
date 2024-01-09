package Senshi07.scottbot;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Loads the variables stored in the .env file into memory.
 * @author Senshi
 * @since 0.1.1-alpha
 */
public class Config
{

    // Dotenv logic
    private static final Dotenv config = Dotenv.load();
    public static String get(String key)
    {
        return config.get(key);
    }
}
