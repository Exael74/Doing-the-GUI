package poobkemon.dominio;

import java.io.Serializable;

/**
 * Clase que representa un Pokémon en el juego
 */
public class Pokemon implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributos básicos
    private String nombre;
    private String tipo;
    private String descripcion;
    private int nivel;

    // Atributos de batalla
    private int hp;
    private int hpTotal;
    private int ataque;
    private int defensa;
    private int velocidad;
    private String[] ataques;

    /**
     * Constructor completo para crear un Pokémon
     */
    public Pokemon(String nombre, String tipo, String descripcion, int nivel,
                   int hpTotal, int ataque, int defensa, int velocidad, String[] ataques) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.nivel = nivel;
        this.hpTotal = hpTotal;
        this.hp = hpTotal; // Inicialmente con salud completa
        this.ataque = ataque;
        this.defensa = defensa;
        this.velocidad = velocidad;
        this.ataques = ataques;
    }

    /**
     * Constructor simplificado que genera estadísticas basadas en nivel y tipo
     */
    public Pokemon(String nombre, String tipo, String descripcion, int nivel) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.nivel = nivel;

        // Generar estadísticas basadas en nivel y un factor de aleatoriedad
        int base = 50 + (nivel * 5);
        double factorTipo = obtenerFactorTipo(tipo);

        this.hpTotal = (int)(base * 1.5 * factorTipo);
        this.hp = hpTotal;
        this.ataque = (int)(base * 1.0 * factorTipo);
        this.defensa = (int)(base * 0.8 * factorTipo);
        this.velocidad = (int)(base * 0.7 * factorTipo);

        // Ataques por defecto
        this.ataques = new String[]{"Placaje", "Gruñido"};
    }

    /**
     * Obtiene un factor de ajuste según el tipo de Pokémon
     */
    private double obtenerFactorTipo(String tipo) {
        switch (tipo.toLowerCase()) {
            case "fuego": return 1.1;
            case "agua": return 1.0;
            case "planta": return 0.9;
            case "eléctrico": return 1.05;
            case "normal": return 1.0;
            case "volador": return 0.95;
            case "roca": return 1.15;
            case "psíquico": return 1.05;
            case "fantasma": return 0.9;
            case "dragón": return 1.2;
            default: return 1.0;
        }
    }

    /**
     * Recibe daño reduciendo el HP actual
     * @param cantidad Cantidad de daño a recibir
     * @return true si el Pokémon sigue consciente, false si quedó debilitado
     */
    public boolean recibirDaño(int cantidad) {
        hp = Math.max(0, hp - cantidad);
        return hp > 0; // Retorna true si sigue consciente
    }

    /**
     * Cura al Pokémon recuperando HP
     * @param cantidad Cantidad de HP a recuperar
     * @return Cantidad efectiva recuperada
     */
    public int curar(int cantidad) {
        int hpAnterior = hp;
        hp = Math.min(hpTotal, hp + cantidad);
        return hp - hpAnterior; // Retorna la cantidad efectiva recuperada
    }

    /**
     * Revive al Pokémon si estaba debilitado
     * @param porcentaje Porcentaje del HP total a recuperar (0 a 100)
     * @return true si se revivió exitosamente, false si no estaba debilitado
     */
    public boolean revivir(int porcentaje) {
        if (hp > 0) return false; // No se puede revivir si no está debilitado

        hp = (hpTotal * porcentaje) / 100;
        return true;
    }

    /**
     * Verifica si el Pokémon está debilitado
     */
    public boolean estaDebilitado() {
        return hp <= 0;
    }

    // Getters y setters

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getNivel() {
        return nivel;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hpTotal, hp));
    }

    public int getHpTotal() {
        return hpTotal;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public String[] getAtaques() {
        return ataques;
    }

    /**
     * Establece los ataques disponibles para este Pokémon
     */
    public void setAtaques(String[] ataques) {
        this.ataques = ataques;
    }

    @Override
    public String toString() {
        return nombre + " (Nv. " + nivel + ")";
    }
}