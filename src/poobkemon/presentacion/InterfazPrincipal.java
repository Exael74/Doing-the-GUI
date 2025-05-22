package poobkemon.presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

/**
 * Interfaz gráfica principal del juego Poobkemon con soporte de pantalla completa
 * @author Exael74
 */
public class InterfazPrincipal extends JFrame {

    private JLabel logoLabel;
    private JPanel panelPrincipal;
    private JPanel panelBotones;
    private PantallaCompleta controladorPantallaCompleta;

    // Botones del menú principal
    private JButton botonJugar;
    private JButton botonSalir;

    // Botones del menú de juego
    private JButton botonNormal;
    private JButton botonSupervivencia;
    private JButton botonPoobkedex;
    private JButton botonVolver;

    // Botones del menú de modo normal
    private JButton botonJugadorVsJugador;
    private JButton botonMaquinaVsJugador;
    private JButton botonMaquinaVsMaquina;
    private JButton botonVolverModoNormal;

    // Estados del menú como constantes enteras en lugar de enum
    public static final int MENU_PRINCIPAL = 0;
    public static final int MENU_JUEGO = 1;
    public static final int MENU_MODO_NORMAL = 2;
    public static final int MENU_POOBKEDEX = 3; // Nuevo estado para Poobkedex

    // Estado actual del menú
    private int estadoMenuActual = MENU_PRINCIPAL;

    // Panel para contener la Poobkedex y los controles principales
    private JPanel contenedorPrincipal; // Nuevo panel para manejar el CardLayout
    private JPanel poobkedexContainer;
    private PoobkedexPanel poobkedexPanel;

    /**
     * Constructor de la clase
     */
    public InterfazPrincipal() {
        setTitle("Poobkemon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controladorPantallaCompleta = new PantallaCompleta(this);

        // Crear un panel contenedor principal con CardLayout
        contenedorPrincipal = new JPanel(new CardLayout());
        getContentPane().add(contenedorPrincipal, BorderLayout.CENTER);

        inicializarComponentes();

        // Establecer tamaño inicial
        setSize(600, 500);
        setMinimumSize(new Dimension(400, 350));
        setLocationRelativeTo(null);

        // Agregar KeyListener para alternar pantalla completa con F11
        KeyStroke f11 = KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0);
        getRootPane().registerKeyboardAction(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        controladorPantallaCompleta.alternarPantallaCompleta();
                        redimensionarComponentes();
                    }
                },
                f11,
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    /**
     * Inicializa y configura los componentes de la interfaz
     */
    private void inicializarComponentes() {
        // Panel principal con GridBagLayout para mejor control del redimensionamiento
        panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(new Color(50, 120, 240)); // Azul similar al de la imagen

        GridBagConstraints gbc = new GridBagConstraints();

        // Cargar y configurar el logo
        ImageIcon logoIcon = cargarImagen("Recursos/Logo/poobkemon_title.png");
        logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Configurar panel de botones
        panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(false); // Transparente para mostrar el fondo azul

        // Inicializar todos los botones
        inicializarBotones();

        // Configurar el menú principal inicialmente
        configurarMenuPrincipal();

        // Añadir logo al panel principal
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.4;
        gbc.fill = GridBagConstraints.BOTH;
        panelPrincipal.add(logoLabel, gbc);

        // Añadir panel de botones al panel principal
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.6;
        panelPrincipal.add(panelBotones, gbc);

        // Inicializar el contenedor para la Poobkedex
        poobkedexContainer = new JPanel(new BorderLayout());

        // Añadir decoración de bordes como en la imagen
        agregarDecoracionBordes(panelPrincipal);

        // Añadir los paneles al contenedor principal con CardLayout
        contenedorPrincipal.add(panelPrincipal, "menu");
        contenedorPrincipal.add(poobkedexContainer, "poobkedex");

        // Mostrar el menú principal primero
        CardLayout cl = (CardLayout)(contenedorPrincipal.getLayout());
        cl.show(contenedorPrincipal, "menu");

        // Añadir listener para redimensionar los componentes cuando cambia el tamaño de la ventana
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                redimensionarComponentes();
            }
        });
    }

    /**
     * Inicializa todos los botones de la aplicación
     */
    private void inicializarBotones() {
        // Botones del menú principal
        botonJugar = crearBotonRetro("JUGAR");
        botonSalir = crearBotonRetro("SALIR");

        // Botones del menú de juego
        botonNormal = crearBotonRetro("Normal");
        botonSupervivencia = crearBotonRetro("Supervivencia");
        botonPoobkedex = crearBotonRetro("Poobkedex");
        botonVolver = crearBotonRetro("Volver");

        // Botones del menú de modo normal
        botonJugadorVsJugador = crearBotonRetro("Jugador vs Jugador");
        botonMaquinaVsJugador = crearBotonRetro("Maquina vs Jugador");
        botonMaquinaVsMaquina = crearBotonRetro("Maquina vs Maquina");
        botonVolverModoNormal = crearBotonRetro("Volver");

        // Configurar acciones de los botones
        configurarAcciones();
    }

    /**
     * Configura las acciones para todos los botones
     */
    private void configurarAcciones() {
        // Acciones para menú principal
        botonJugar.addActionListener(e -> mostrarMenuJuego());
        botonSalir.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(
                    InterfazPrincipal.this,
                    "¿Estás seguro que deseas salir?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Acciones para menú de juego
        botonNormal.addActionListener(e -> mostrarMenuModoNormal());
        botonSupervivencia.addActionListener(e -> JOptionPane.showMessageDialog(
                this, "Iniciando modo Supervivencia...", "Poobkemon", JOptionPane.INFORMATION_MESSAGE));

        // Modificar para abrir la Poobkedex en el panel principal
        botonPoobkedex.addActionListener(e -> mostrarPoobkedex());

        botonVolver.addActionListener(e -> mostrarMenuPrincipal());

        // Acciones para menú de modo normal
        botonJugadorVsJugador.addActionListener(e -> JOptionPane.showMessageDialog(
                this, "Iniciando Jugador vs Jugador...", "Poobkemon", JOptionPane.INFORMATION_MESSAGE));
        botonMaquinaVsJugador.addActionListener(e -> JOptionPane.showMessageDialog(
                this, "Iniciando Máquina vs Jugador...", "Poobkemon", JOptionPane.INFORMATION_MESSAGE));
        botonMaquinaVsMaquina.addActionListener(e -> JOptionPane.showMessageDialog(
                this, "Iniciando Máquina vs Máquina...", "Poobkemon", JOptionPane.INFORMATION_MESSAGE));
        botonVolverModoNormal.addActionListener(e -> mostrarMenuJuego());
    }

    /**
     * Muestra la Poobkedex en pantalla completa dentro del panel principal
     */
    private void mostrarPoobkedex() {
        // Guardar el estado actual antes de cambiar
        estadoMenuActual = MENU_POOBKEDEX;

        // Si no existe el panel de Poobkedex, lo creamos
        if (poobkedexPanel == null) {
            poobkedexPanel = new PoobkedexPanel(this);
            poobkedexContainer.add(poobkedexPanel, BorderLayout.CENTER);
        }

        // Cambiar a la tarjeta de Poobkedex
        CardLayout cl = (CardLayout)(contenedorPrincipal.getLayout());
        cl.show(contenedorPrincipal, "poobkedex");

        // Forzar actualización del contenido
        revalidate();
        repaint();
    }

    /**
     * Vuelve al menú anterior desde la Poobkedex
     */
    public void volverDesdePoobkedex() {
        // Cambiar a la tarjeta del menú principal
        CardLayout cl = (CardLayout)(contenedorPrincipal.getLayout());
        cl.show(contenedorPrincipal, "menu");

        // Actualizar el estado
        estadoMenuActual = MENU_JUEGO;

        // Refrescar la interfaz
        revalidate();
        repaint();
    }

    /**
     * Crea un botón con estilo retro
     */
    private JButton crearBotonRetro(String texto) {
        // Crear un botón personalizado con estilo retro
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

                boolean hover = getModel().isRollover();
                boolean pressed = getModel().isPressed();

                // Colores retro
                Color colorPrincipal = new Color(200, 30, 30); // Rojo más oscuro
                Color colorClaro = new Color(240, 100, 100); // Rojo claro para destacado
                Color colorOscuro = new Color(120, 0, 0); // Rojo oscuro para sombra

                int width = getWidth();
                int height = getHeight();

                // Efecto 3D retro
                if (pressed) {
                    // Botón presionado: solo borde y fondo plano
                    g2.setColor(colorOscuro);
                    g2.fillRect(0, 0, width, height);
                    g2.setColor(colorPrincipal);
                    g2.fillRect(2, 2, width - 4, height - 4);
                } else {
                    // Efecto 3D cuando no está presionado
                    g2.setColor(hover ? colorClaro : colorPrincipal);
                    g2.fillRect(0, 0, width, height);

                    // Borde superior e izquierdo claro
                    g2.setColor(Color.WHITE);
                    g2.drawLine(0, 0, width - 1, 0); // Borde superior
                    g2.drawLine(0, 0, 0, height - 1); // Borde izquierdo

                    // Borde inferior y derecho oscuro
                    g2.setColor(colorOscuro);
                    g2.drawLine(0, height - 1, width - 1, height - 1); // Borde inferior
                    g2.drawLine(width - 1, 0, width - 1, height - 1); // Borde derecho

                    // Segunda capa de bordes para efecto 3D más pronunciado
                    g2.setColor(Color.WHITE.darker());
                    g2.drawLine(1, 1, width - 2, 1); // Borde superior interior
                    g2.drawLine(1, 1, 1, height - 2); // Borde izquierdo interior

                    g2.setColor(colorOscuro.darker());
                    g2.drawLine(1, height - 2, width - 2, height - 2); // Borde inferior interior
                    g2.drawLine(width - 2, 1, width - 2, height - 2); // Borde derecho interior
                }

                // Texto con "sombra" para efecto retro
                FontMetrics fm = g2.getFontMetrics();
                Rectangle2D r = fm.getStringBounds(texto, g2);
                int x = (width - (int) r.getWidth()) / 2;
                int y = (height - (int) r.getHeight()) / 2 + fm.getAscent();

                if (!pressed) {
                    // Sombra del texto (solo si no está presionado)
                    g2.setColor(Color.BLACK);
                    g2.drawString(texto, x + 1, y + 1);
                }

                g2.setColor(pressed ? Color.WHITE : Color.BLACK);
                g2.drawString(texto, x, y);

                g2.dispose();
            }

            // Sobrescribir para que los botones tengan el mismo tamaño
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.width = Math.max(d.width, 200);
                d.height = Math.max(d.height, 50);
                return d;
            }
        };

        // Configurar aspecto del botón
        boton.setOpaque(false);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Courier New", Font.BOLD, 18)); // Fuente estilo retro
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Efecto hover para el cursor
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return boton;
    }

    /**
     * Configura el panel para mostrar el menú principal
     */
    private void configurarMenuPrincipal() {
        panelBotones.removeAll();
        panelBotones.add(Box.createVerticalStrut(20));
        panelBotones.add(botonJugar);
        panelBotones.add(Box.createVerticalStrut(15));
        panelBotones.add(botonSalir);
        estadoMenuActual = MENU_PRINCIPAL;
        actualizarInterfaz();
    }

    /**
     * Muestra el menú de juego
     */
    private void mostrarMenuPrincipal() {
        configurarMenuPrincipal();
    }

    /**
     * Muestra el menú de juego con las opciones de modos
     */
    private void mostrarMenuJuego() {
        panelBotones.removeAll();
        panelBotones.add(Box.createVerticalStrut(10));
        panelBotones.add(botonNormal);
        panelBotones.add(Box.createVerticalStrut(10));
        panelBotones.add(botonSupervivencia);
        panelBotones.add(Box.createVerticalStrut(10));
        panelBotones.add(botonPoobkedex);
        panelBotones.add(Box.createVerticalStrut(10));
        panelBotones.add(botonVolver);
        estadoMenuActual = MENU_JUEGO;
        actualizarInterfaz();
    }

    /**
     * Muestra el menú de modo normal con sus opciones
     */
    private void mostrarMenuModoNormal() {
        panelBotones.removeAll();
        panelBotones.add(Box.createVerticalStrut(10));
        panelBotones.add(botonJugadorVsJugador);
        panelBotones.add(Box.createVerticalStrut(10));
        panelBotones.add(botonMaquinaVsJugador);
        panelBotones.add(Box.createVerticalStrut(10));
        panelBotones.add(botonMaquinaVsMaquina);
        panelBotones.add(Box.createVerticalStrut(10));
        panelBotones.add(botonVolverModoNormal);
        estadoMenuActual = MENU_MODO_NORMAL;
        actualizarInterfaz();
    }

    /**
     * Actualiza la interfaz después de cambiar de menú
     */
    private void actualizarInterfaz() {
        redimensionarComponentes();
        panelBotones.revalidate();
        panelBotones.repaint();
    }

    /**
     * Carga una imagen desde la ruta especificada
     */
    private ImageIcon cargarImagen(String ruta) {
        ImageIcon icono = new ImageIcon(ruta);
        return icono;
    }

    /**
     * Redimensiona los componentes cuando cambia el tamaño de la ventana
     */
    private void redimensionarComponentes() {
        int anchoVentana = getWidth();

        // Redimensionar botones según el menú actual
        Dimension tamanoBoton = new Dimension(anchoVentana / 3, 50);

        // Actualizar tamaño de todos los botones visibles según el menú actual
        switch (estadoMenuActual) {
            case MENU_PRINCIPAL:
                botonJugar.setMaximumSize(tamanoBoton);
                botonSalir.setMaximumSize(tamanoBoton);
                break;

            case MENU_JUEGO:
                botonNormal.setMaximumSize(tamanoBoton);
                botonSupervivencia.setMaximumSize(tamanoBoton);
                botonPoobkedex.setMaximumSize(tamanoBoton);
                botonVolver.setMaximumSize(tamanoBoton);
                break;

            case MENU_MODO_NORMAL:
                botonJugadorVsJugador.setMaximumSize(tamanoBoton);
                botonMaquinaVsJugador.setMaximumSize(tamanoBoton);
                botonMaquinaVsMaquina.setMaximumSize(tamanoBoton);
                botonVolverModoNormal.setMaximumSize(tamanoBoton);
                break;

            case MENU_POOBKEDEX:
                // Redimensionar el panel de Poobkedex si está activo
                if (poobkedexPanel != null) {
                    poobkedexPanel.setSize(contenedorPrincipal.getSize());
                }
                break;
        }

        // Redimensionar logo proporcionalmente
        ImageIcon logoIcon = (ImageIcon) logoLabel.getIcon();
        if (logoIcon != null) {
            Image img = logoIcon.getImage();
            int nuevoAncho = Math.min(anchoVentana - 40, img.getWidth(null) * 2);
            if (nuevoAncho > 0) {
                double ratio = (double) img.getHeight(null) / img.getWidth(null);
                int nuevoAlto = (int) (nuevoAncho * ratio);
                logoLabel.setIcon(new ImageIcon(img.getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH)));
            }
        }

        revalidate();
        repaint();
    }

    /**
     * Agrega decoración de bordes como se muestra en la imagen
     */
    private void agregarDecoracionBordes(JPanel panel) {
        panel.setBorder(BorderFactory.createCompoundBorder(
                // Borde exterior negro
                BorderFactory.createLineBorder(Color.BLACK, 2),
                // Borde interior con decoración
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
    }

    /**
     * Método principal para ejecutar la aplicación
     */
    public static void main(String[] args) {
        try {
            // Usamos Look and Feel básico para mejor control de la apariencia retro
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                InterfazPrincipal ventana = new InterfazPrincipal();
                ventana.setVisible(true);
            }
        });
    }
}