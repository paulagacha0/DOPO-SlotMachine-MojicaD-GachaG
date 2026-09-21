import java.awt.Color;
import java.util.Locale;

/**
 * [COL] Centraliza los colores admitidos por el catálogo y por Canvas
 */
public final class SymbolColors
{
    private static final String[] BASIC_NAMES = {
        "red", "blue", "green", "yellow", "magenta", "black"
    };
    private static final String[] BASIC_HEX = {
        "#ff0000", "#0000ff", "#00ff00", "#ffff00", "#ff00ff", "#000000"
    };

    /** [COL] Evita crear objetos de esta utilidad de métodos static */
    private SymbolColors()
    {
    }

    /**
     * [COL] Normaliza texto y unifica los alias de los colores básicos
     */
    public static String normalize(String color)
    {
        if (color == null) {
            return null;
        }
        String normalized = color.trim().toLowerCase(Locale.ROOT);
        for (int i = 0; i < BASIC_NAMES.length; i++) {
            if (BASIC_HEX[i].equals(normalized)) {
                return BASIC_NAMES[i];
            }
        }
        return normalized;
    }

    /**
     * [COL] Admite los seis nombres originales y colores #RRGGBB reserva blanco
     */
    public static boolean isSymbol(String color)
    {
        String normalized = normalize(color);
        if (normalized == null || "white".equals(normalized)
                || "#ffffff".equals(normalized)) {
            return false;
        }
        for (String basic : BASIC_NAMES) {
            if (basic.equals(normalized)) {
                return true;
            }
        }
        return normalized.matches("#[0-9a-f]{6}");
    }

    /**
     * [COL -> JAVA] Convierte un texto en el color usado para dibujar
     */
    public static Color forDrawing(String color)
    {
        String normalized = normalize(color);
        if ("white".equals(normalized) || "#ffffff".equals(normalized)) {
            return Color.WHITE;
        }
        for (int i = 0; i < BASIC_NAMES.length; i++) {
            if (BASIC_NAMES[i].equals(normalized)) {
                return Color.decode(BASIC_HEX[i]);
            }
        }
        if (normalized != null && normalized.matches("#[0-9a-f]{6}")) {
            return Color.decode(normalized);
        }
        return Color.BLACK;
    }

    /**
     * [COL] Genera hasta 50 colores diferentes para la máquina del concurso.
     */
    public static String[] createPalette(int count)
    {
        if (count < 1 || count > 50) {
            throw new IllegalArgumentException("The palette size must be between 1 and 50.");
        }
        String[] palette = new String[count];
        for (int i = 0; i < count; i++) {
            if (i < BASIC_NAMES.length) {
                palette[i] = BASIC_NAMES[i];
            } else {
                // [COL] Varía el tono y demse cosas
                int rgb = Color.HSBtoRGB((i - 6) / 44.0f, 0.70f, 0.85f);
                palette[i] = String.format(Locale.ROOT, "#%06x", rgb & 0xffffff);
            }
        }
        return palette;
    }
}
