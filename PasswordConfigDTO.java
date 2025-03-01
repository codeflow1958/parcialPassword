package parcial;

import java.security.SecureRandom; // Importa SecureRandom para generar valores aleatorios seguros
import java.util.ArrayList; // Importa ArrayList para manejar listas dinámicas
import java.util.Collections; // Importa Collections para mezclar listas
import java.util.List; // Importa List para usar listas de caracteres

// Clase DTO para definir las configuraciones de la contraseña
class PasswordConfigDTO {
    // Indica si se debe restringir la cantidad mínima de dígitos en la contraseña
    public boolean restrictMinDigits;
    
    // Cantidad mínima de dígitos requeridos
    public int minDigits;
    
    // Indica si se debe restringir la cantidad mínima de mayúsculas
    public boolean restrictMinUpperCaseLetters;
    
    // Cantidad mínima de letras mayúsculas requeridas
    public int minUpperCaseLetters;
    
    // Indica si se debe restringir la cantidad mínima de minúsculas
    public boolean restrictMinLowerCaseLetters;
    
    // Cantidad mínima de letras minúsculas requeridas
    public int minLowerCaseLetters;
    
    // Indica si se debe restringir la cantidad mínima de caracteres especiales
    public boolean restrictMinNonAlphanumericCharacters;
    
    // Cantidad mínima de caracteres especiales requeridos
    public int minNonAlphanumericCharacters;
    
    // Longitud mínima de la contraseña
    public int minLength;

    // Constructor con valores predeterminados
    public PasswordConfigDTO() {
        this.restrictMinDigits = false;
        this.minDigits = 1;
        this.restrictMinUpperCaseLetters = false;
        this.minUpperCaseLetters = 1;
        this.restrictMinLowerCaseLetters = false;
        this.minLowerCaseLetters = 1;
        this.restrictMinNonAlphanumericCharacters = false;
        this.minNonAlphanumericCharacters = 1;
        this.minLength = 8;
    }
}

public class PasswordGenerator {
    // Conjunto de caracteres en minúsculas
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    
    // Conjunto de caracteres en mayúsculas
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    // Conjunto de dígitos numéricos
    private static final String DIGITS = "0123456789";
    
    // Conjunto de caracteres especiales
    private static final String SPECIALS = "!@#$%^&*()-_=+<>?";
    
    // Instancia de SecureRandom para generar valores aleatorios de manera segura
    private static final SecureRandom RANDOM = new SecureRandom();

    // Método para generar una contraseña aleatoria con una longitud específica
    public static String generatePassword(int length) {
        // Asegura que la longitud mínima sea de al menos 8 caracteres
        if (length < 8) length = 8;

        // Lista para almacenar los caracteres generados
        List<Character> passwordChars = new ArrayList<>();
        
        // Agrega al menos un carácter de cada tipo para cumplir con requisitos básicos
        passwordChars.add(getRandomChar(LOWERCASE)); // Agrega una letra minúscula
        passwordChars.add(getRandomChar(UPPERCASE)); // Agrega una letra mayúscula
        passwordChars.add(getRandomChar(DIGITS)); // Agrega un número
        passwordChars.add(getRandomChar(SPECIALS)); // Agrega un carácter especial

        // Conjunto de todos los caracteres disponibles
        String allChars = LOWERCASE + UPPERCASE + DIGITS + SPECIALS;

        // Completa la contraseña hasta alcanzar la longitud requerida
        while (passwordChars.size() < length) {
            passwordChars.add(getRandomChar(allChars)); // Agrega caracteres aleatorios
        }

        // Mezcla los caracteres para evitar patrones predecibles
        Collections.shuffle(passwordChars);

        // Convierte la lista de caracteres en una cadena y la devuelve
        return listToString(passwordChars);
    }

    // Método para generar una contraseña basada en una configuración personalizada
    public static String generatePassword(PasswordConfigDTO config) {
        // Verifica que la configuración sea válida (que la suma de restricciones no supere la longitud mínima)
        if (!isConfigValid(config)) {
            throw new IllegalArgumentException("Configuración inválida: la suma de restricciones excede la longitud mínima.");
        }

        // Lista para almacenar los caracteres generados
        List<Character> passwordChars = new ArrayList<>();
        
        // Agrega los caracteres requeridos según la configuración
        if (config.restrictMinDigits) 
            appendRandomChars(passwordChars, DIGITS, config.minDigits); // Agrega números
        if (config.restrictMinUpperCaseLetters) 
            appendRandomChars(passwordChars, UPPERCASE, config.minUpperCaseLetters); // Agrega mayúsculas
        if (config.restrictMinLowerCaseLetters) 
            appendRandomChars(passwordChars, LOWERCASE, config.minLowerCaseLetters); // Agrega minúsculas
        if (config.restrictMinNonAlphanumericCharacters) 
            appendRandomChars(passwordChars, SPECIALS, config.minNonAlphanumericCharacters); // Agrega caracteres especiales

        // Conjunto de todos los caracteres disponibles
        String allChars = LOWERCASE + UPPERCASE + DIGITS + SPECIALS;

        // Completa la contraseña hasta alcanzar la longitud requerida
        while (passwordChars.size() < config.minLength) {
            passwordChars.add(getRandomChar(allChars)); // Agrega caracteres aleatorios
        }

        // Mezcla los caracteres para evitar patrones predecibles
        Collections.shuffle(passwordChars);

        // Convierte la lista de caracteres en una cadena y la devuelve
        return listToString(passwordChars);
    }

    // Método auxiliar para obtener un carácter aleatorio de un conjunto dado
    private static char getRandomChar(String characters) {
        return characters.charAt(RANDOM.nextInt(characters.length()));
    }

    // Método auxiliar para agregar caracteres aleatorios a la contraseña
    private static void appendRandomChars(List<Character> passwordChars, String chars, int count) {
        for (int i = 0; i < count; i++) {
            passwordChars.add(getRandomChar(chars)); // Agrega caracteres de un conjunto dado
        }
    }

    // Método para validar que la configuración sea válida
    private static boolean isConfigValid(PasswordConfigDTO config) {
        int requiredChars = 0;
        
        // Suma el total de caracteres mínimos requeridos por la configuración
        if (config.restrictMinDigits) requiredChars += config.minDigits;
        if (config.restrictMinUpperCaseLetters) requiredChars += config.minUpperCaseLetters;
        if (config.restrictMinLowerCaseLetters) requiredChars += config.minLowerCaseLetters;
        if (config.restrictMinNonAlphanumericCharacters) requiredChars += config.minNonAlphanumericCharacters;

        // Retorna verdadero si la suma de caracteres obligatorios no supera la longitud mínima
        return requiredChars <= config.minLength;
    }

    // Método auxiliar para convertir una lista de caracteres en una cadena
    private static String listToString(List<Character> list) {
        StringBuilder sb = new StringBuilder();
        for (Character ch : list) {
            sb.append(ch);
        }
        return sb.toString();
    }

    // Método principal para probar la generación de contraseñas
    public static void main(String[] args) {
        // Genera una contraseña básica con 12 caracteres
        System.out.println("Password (Versión 1): " + generatePassword(12));

        // Crea una configuración personalizada
        PasswordConfigDTO config = new PasswordConfigDTO();
        config.restrictMinDigits = true;
        config.minDigits = 3;
        config.restrictMinUpperCaseLetters = true;
        config.minUpperCaseLetters = 2;
        config.restrictMinLowerCaseLetters = true;
        config.minLowerCaseLetters = 2;
        config.restrictMinNonAlphanumericCharacters = true;
        config.minNonAlphanumericCharacters = 2;
        config.minLength = 12;

        // Genera una contraseña con la configuración personalizada
        System.out.println("Password (Versión 2): " + generatePassword(config));
    }
}
