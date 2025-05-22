package poobkemon.presentacion;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Versión simplificada del panel para GIFs
 */
public class AnimatedGifPanel extends JPanel {

    private String pokemonName;
    private JLabel gifLabel;
    private boolean isLarge;

    /**
     * Constructor para un panel de GIF animado
     */
    public AnimatedGifPanel(String pokemonName, int width, int height, boolean isLarge) {
        this.pokemonName = pokemonName;
        this.isLarge = isLarge;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.WHITE));

        loadGif();
    }

    /**
     * Carga un GIF directamente desde el archivo
     */
    private void loadGif() {
        try {
            // Ruta directa al archivo GIF
            String gifPath = "C:\\Users\\stive\\Desktop\\Poobkemon\\Recursos\\PokemonsPokedex\\" +
                    pokemonName + "_pokedex.gif";

            // Verificar que el archivo exista
            File gifFile = new File(gifPath);
            if (!gifFile.exists()) {
                System.err.println("Archivo no encontrado: " + gifPath);
                showNameOnly();
                return;
            }

            // Cargar GIF directamente
            ImageIcon icon = new ImageIcon(gifPath);

            // Si es para panel grande, hacer el Pokémon más grande
            if (isLarge) {
                // Factor de escala fijo para GIF grande
                int origWidth = icon.getIconWidth();
                int origHeight = icon.getIconHeight();

                if (origWidth > 0 && origHeight > 0) {
                    // Escalar a un tamaño fijo de 200px (mantiene proporción)
                    double scale = 3.0; // Factor de escala fijo

                    int newWidth = (int)(origWidth * scale);
                    int newHeight = (int)(origHeight * scale);

                    Image scaled = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
                    icon = new ImageIcon(scaled);

                    System.out.println("Escalando " + pokemonName + " a " + newWidth + "x" + newHeight);
                }
            }

            // Crear etiqueta para mostrar el GIF
            gifLabel = new JLabel(icon);
            gifLabel.setHorizontalAlignment(JLabel.CENTER);

            // Usar un JScrollPane sin barras para evitar los rastros
            JScrollPane scroller = new JScrollPane(gifLabel);
            scroller.setBorder(null);
            scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scroller.getViewport().setBackground(Color.BLACK);

            // Añadir al panel
            add(scroller, BorderLayout.CENTER);

            // Si no es panel grande, mostrar el nombre debajo
            if (!isLarge) {
                JLabel nameLabel = new JLabel(pokemonName);
                nameLabel.setHorizontalAlignment(JLabel.CENTER);
                nameLabel.setForeground(Color.WHITE);
                add(nameLabel, BorderLayout.SOUTH);
            }

        } catch (Exception e) {
            System.err.println("Error al cargar GIF: " + e.getMessage());
            e.printStackTrace();
            showNameOnly();
        }
    }

    /**
     * Si no se puede cargar el GIF, al menos mostrar el nombre
     */
    private void showNameOnly() {
        JLabel nameLabel = new JLabel(pokemonName);
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        nameLabel.setForeground(Color.WHITE);
        add(nameLabel, BorderLayout.CENTER);
    }

    /**
     * Marca el panel como seleccionado
     */
    public void setSelected(boolean selected) {
        if (selected) {
            setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        } else {
            setBorder(BorderFactory.createLineBorder(Color.WHITE));
        }
    }

    /**
     * Libera recursos
     */
    public void dispose() {
        if (gifLabel != null) {
            gifLabel.setIcon(null);
        }
        removeAll();
    }

    /**
     * Método vacío para mantener la interfaz
     */
    public void stopAnimation() {
        // No hace nada
    }
}