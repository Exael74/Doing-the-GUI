package poobkemon.presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Componente especializado para mostrar Pokémon
 */
public class PokemonViewer extends JComponent {

    private String pokemonName;
    private ImageIcon originalIcon;
    private Image scaledImage;
    private JLabel dummyLabel; // Para mantener referencia al GIF
    private Timer animationTimer;
    private int frame = 0;

    /**
     * Constructor
     */
    public PokemonViewer(String pokemonName) {
        this.pokemonName = pokemonName;
        setOpaque(false);
        setDoubleBuffered(true);

        // Cargar el GIF
        loadGif();

        // Crear un timer para la animación manual
        animationTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame++;
                repaint();
            }
        });
        animationTimer.start();
    }

    /**
     * Carga el archivo GIF
     */
    private void loadGif() {
        try {
            // Ruta al GIF
            String gifPath = "C:\\Users\\stive\\Desktop\\Poobkemon\\Recursos\\PokemonsPokedex\\" +
                    pokemonName + "_pokedex.gif";

            // Verificar archivo
            File file = new File(gifPath);
            if (!file.exists()) {
                System.err.println("No se encontró el archivo: " + gifPath);
                return;
            }

            // Cargar el GIF
            originalIcon = new ImageIcon(gifPath);
            System.out.println("GIF cargado: " + pokemonName + " - Tamaño: " +
                    originalIcon.getIconWidth() + "x" + originalIcon.getIconHeight());

            // Para mantener la referencia al GIF (evita garbage collection)
            dummyLabel = new JLabel(originalIcon);

            // Escalar la imagen
            updateScaledImage();

        } catch (Exception e) {
            System.err.println("Error al cargar GIF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Actualiza la imagen escalada cuando cambia el tamaño
     */
    private void updateScaledImage() {
        if (originalIcon == null || originalIcon.getIconWidth() <= 0) return;

        int origWidth = originalIcon.getIconWidth();
        int origHeight = originalIcon.getIconHeight();

        // Escalar a 3x
        double scale = 3.0;
        int newWidth = (int)(origWidth * scale);
        int newHeight = (int)(origHeight * scale);

        // Escalar la imagen
        scaledImage = originalIcon.getImage().getScaledInstance(
                newWidth, newHeight, Image.SCALE_DEFAULT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // No dibujamos fondo aquí, dejamos que el componente padre lo haga

        // Dibujar la imagen si está disponible
        if (scaledImage != null) {
            // Centrar la imagen
            int x = (getWidth() - scaledImage.getWidth(null)) / 2;
            int y = (getHeight() - scaledImage.getHeight(null)) / 2;

            // Dibujar la imagen
            g.drawImage(scaledImage, x, y, null);
        }
    }

    /**
     * Detiene la animación
     */
    public void stopAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

    /**
     * Libera recursos
     */
    public void dispose() {
        stopAnimation();
        if (dummyLabel != null) {
            dummyLabel.setIcon(null);
        }
        originalIcon = null;
        scaledImage = null;
    }
}