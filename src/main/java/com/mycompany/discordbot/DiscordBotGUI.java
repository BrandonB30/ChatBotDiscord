package com.mycompany.discordbot;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

/**
 * ChatBot de Discord
 * 
 * Este es un bot de Discord desarrollado en Java que proporciona una interfaz gráfica
 * para la gestión y control de un bot de Discord.
 * 
 * @author Brandon
 */
public class DiscordBotGUI extends javax.swing.JFrame {
    
    private static final Logger LOGGER = Logger.getLogger(DiscordBotGUI.class.getName());
    private final String[] conversaciones = new String[1000];
    private int cantConversaciones = 0;
    private String botToken = "";
    private JDA discordClient;
    private Map<String, String> respuestas;
    private ExecutorService executorService;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private static final int CORE_POOL_SIZE = 4;
    private static final int MAX_POOL_SIZE = 8;
    private static final long KEEP_ALIVE_TIME = 60L;

    /**
     * Constructor de la interfaz
     */
    public DiscordBotGUI() {
        initComponents();
        inicializarRespuestas();
        initializeExecutorService();
        personalizarInterfaz();
    }

    /**
     * Personaliza la apariencia de la interfaz gráfica
     */
    private void personalizarInterfaz() {
        // Establecer un aspecto más moderno
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "No se pudo establecer el look and feel", e);
        }

        // Personalizar colores y fuentes
        Color colorPrimario = new Color(114, 137, 218); // Color de Discord
        Color colorFondo = new Color(54, 57, 63); // Fondo estilo Discord
        Color colorTexto = new Color(255, 255, 255);
        Font fuenteTitulo = new Font("Segoe UI", Font.BOLD, 14);
        Font fuenteNormal = new Font("Segoe UI", Font.PLAIN, 12);

        // Aplicar estilos
        this.getContentPane().setBackground(colorFondo);
        jPanel1.setBackground(colorFondo);
        jLabel1.setFont(fuenteTitulo);
        jLabel1.setForeground(colorTexto);
        jTextField1.setFont(fuenteNormal);
        jCheckBox1.setFont(fuenteNormal);
        jCheckBox1.setBackground(colorFondo);
        jCheckBox1.setForeground(colorTexto);
        jButton1.setFont(fuenteNormal);
        jButton1.setBackground(colorPrimario);
        jButton1.setForeground(Color.WHITE);
        jButton1.setFocusPainted(false);
        jButton1.setBorderPainted(false);

        // Cargar el icono de Discord
        try {
            // Debes proporcionar un icono de Discord
            File iconFile = new File("./src/img/discord_icon.png");
            if (iconFile.exists()) {
                ImageIcon iconoDiscord = new ImageIcon(iconFile.getAbsolutePath());
                // Redimensionar el icono si es necesario
                Image img = iconoDiscord.getImage();
                if (img != null) {
                    Image imgResized = img.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                    iconoDiscord = new ImageIcon(imgResized);
                    setIconImage(iconoDiscord.getImage());
                    LOGGER.log(Level.INFO, "Icono de Discord cargado correctamente");
                } else {
                    LOGGER.log(Level.WARNING, "No se pudo cargar la imagen del icono");
                }
            } else {
                LOGGER.log(Level.WARNING, "No se encontró el archivo de icono en: " + iconFile.getAbsolutePath());
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error al cargar el icono de Discord", e);
        }
    }

    /**
     * Inicializa el pool de hilos para procesamiento asíncrono
     */
    private void initializeExecutorService() {
        ThreadFactory threadFactory = r -> {
            Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setName("BotWorker-" + thread.getId());
            thread.setUncaughtExceptionHandler((t, e) -> 
                LOGGER.log(Level.SEVERE, "Error no manejado en thread " + t.getName(), e));
            return thread;
        };

        executorService = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            threadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Bot de Discord");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 0);

        jLabel1.setText("Token del Bot:");
        jPanel1.add(jLabel1, gbc);

        gbc.gridy = 1;
        jTextField1.setToolTipText("Ingresa el token del bot");
        jPanel1.add(jTextField1, gbc);

        gbc.gridy = 2;
        jSeparator1.setForeground(new Color(200, 200, 200));
        jPanel1.add(jSeparator1, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 1;
        jCheckBox1.setText("Activar Bot");
        jPanel1.add(jCheckBox1, gbc);

        gbc.gridx = 1;
        jButton1.setText("Ayuda");
        jPanel1.add(jButton1, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        jLabel2 = new javax.swing.JLabel("Estado del Bot:");
        jPanel1.add(jLabel2, gbc);
        
        gbc.gridy = 5;
        jTextArea1 = new javax.swing.JTextArea(8, 30);
        jTextArea1.setEditable(false);
        jScrollPane1 = new javax.swing.JScrollPane(jTextArea1);
        jPanel1.add(jScrollPane1, gbc);

        getContentPane().add(jPanel1);
        pack();
        setLocationRelativeTo(null);

        // Agregar listeners
        jCheckBox1.addActionListener(evt -> jCheckBox1ActionPerformed(evt));
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Maneja el evento de activación/desactivación del bot
     */
    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jCheckBox1.isSelected()) {
            try {
                botToken = jTextField1.getText().trim();

                if (botToken.isEmpty()) {
                    throw new IllegalArgumentException("Por favor ingresa el token del bot");
                }

                CompletableFuture.runAsync(() -> {
                    try {
                        addLogMessage("Iniciando bot de Discord...");
                        // Construir cliente Discord JDA
                        discordClient = JDABuilder.createDefault(botToken)
                            .setActivity(Activity.watching("mensajes"))
                            .enableIntents(GatewayIntent.MESSAGE_CONTENT, 
                                          GatewayIntent.GUILD_MESSAGES,
                                          GatewayIntent.DIRECT_MESSAGES)
                            .setMemberCachePolicy(MemberCachePolicy.ONLINE)
                            .addEventListeners(new DiscordMessageListener())
                            .build();
                        
                        addLogMessage("Conectando con servidores de Discord...");
                        discordClient.awaitReady(); // Esperar a que se conecte
                        isRunning.set(true);
                        
                        addLogMessage("Bot conectado correctamente");
                        SwingUtilities.invokeLater(() -> 
                            JOptionPane.showMessageDialog(this, "Bot de Discord activado correctamente"));
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Error al inicializar el bot", e);
                        addLogMessage("Error: " + e.getMessage());
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(this, 
                                "Error al activar el bot: " + e.getMessage());
                            jCheckBox1.setSelected(false);
                        });
                    }
                }, executorService);

            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "Error de validación", e);
                addLogMessage("Error: " + e.getMessage());
                JOptionPane.showMessageDialog(this, e.getMessage());
                jCheckBox1.setSelected(false);
            }
        } else {
            try {
                isRunning.set(false);
                if (discordClient != null) {
                    CompletableFuture.runAsync(() -> {
                        try {
                            addLogMessage("Desactivando bot...");
                            // Cerrar conexión con Discord
                            discordClient.shutdown();
                            discordClient = null;
                            addLogMessage("Bot desactivado");
                            SwingUtilities.invokeLater(() -> 
                                JOptionPane.showMessageDialog(this, "Bot desactivado correctamente"));
                        } catch (Exception e) {
                            LOGGER.log(Level.SEVERE, "Error al desactivar el bot", e);
                            addLogMessage("Error al desactivar: " + e.getMessage());
                            SwingUtilities.invokeLater(() -> 
                                JOptionPane.showMessageDialog(this, 
                                    "Error al desactivar el bot: " + e.getMessage()));
                        }
                    }, executorService);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error al desactivar el bot", e);
                addLogMessage("Error: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Error al desactivar el bot: " + e.getMessage());
            }
        }
    }

    /**
     * Muestra la información de ayuda
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String ayuda = "Para configurar el bot de Discord:\n\n" +
                      "1. Ve al Portal de Desarrolladores de Discord (discord.com/developers)\n" +
                      "2. Crea una nueva aplicación\n" +
                      "3. Ve a la sección 'Bot' y haz clic en 'Add Bot'\n" +
                      "4. Copia el token que se muestra allí\n" +
                      "5. Activa los intents privilegiados:\n" +
                      "   - MESSAGE_CONTENT\n" +
                      "   - GUILD_MEMBERS\n" +
                      "   - GUILD_PRESENCES\n" +
                      "6. Genera un link de invitación en 'OAuth2' > 'URL Generator'\n" +
                      "   - Selecciona los scopes 'bot' y 'applications.commands'\n" +
                      "   - Selecciona permisos necesarios (al menos Send Messages y Read Messages)\n" +
                      "7. Pega el token en esta aplicación y activa el bot";
        JOptionPane.showMessageDialog(this, ayuda, "Ayuda", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Agrega un mensaje al registro de eventos del bot
     */
    private void addLogMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            jTextArea1.append("[" + timestamp + "] " + message + "\n");
            // Scroll al final
            jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DiscordBotGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DiscordBotGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DiscordBotGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DiscordBotGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new DiscordBotGUI().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    /**
     * Genera una lista de las conversaciones activas
     * @return String con la lista de conversaciones
     */
    private String listarConversaciones() {
        if (cantConversaciones == 0) {
            return "No hay conversaciones activas.";
        }
        
        StringBuilder lista = new StringBuilder("Conversaciones activas:\n");
        for (int i = 0; i < cantConversaciones; i++) {
            lista.append((i + 1)).append(". ").append(conversaciones[i]).append("\n");
        }
        return lista.toString();
    }

    /**
     * Inicializa las respuestas automáticas del bot
     */
    private void inicializarRespuestas() {
        respuestas = new HashMap<>();
        // Agregamos respuestas básicas
        respuestas.put("hola", "¡Hola! ¿En qué puedo ayudarte?");
        respuestas.put("buenos días", "¡Buenos días! ¿Cómo puedo ayudarte hoy?");
        respuestas.put("buenas tardes", "¡Buenas tardes! ¿En qué puedo servirte?");
        respuestas.put("buenas noches", "¡Buenas noches! ¿Necesitas ayuda con algo?");
        respuestas.put("ayuda", "Puedo ayudarte con:\n- Información general\n- Horarios\n- Contacto\n¿Qué necesitas?");
        respuestas.put("gracias", "¡De nada! Estoy aquí para ayudarte.");
        respuestas.put("adiós", "¡Hasta luego! Que tengas un excelente día.");
        respuestas.put("chao", "¡Hasta pronto! Si necesitas algo más, aquí estaré.");
        
        // Respuestas sobre el creador
        respuestas.put("quien es tu creador", "Mi creador es Juan Sebastian Quinto Hinestroza");
        respuestas.put("quien te creo", "Fui creado por Juan Sebastian Quinto Hinestroza");
        respuestas.put("quien te hizo", "Juan Sebastian Quinto Hinestroza es mi creador");
        respuestas.put("quien te programo", "Fui programado por Juan Sebastian Quinto Hinestroza");
        respuestas.put("quien te desarrollo", "Mi desarrollador es Juan Sebastian Quinto Hinestroza");
        respuestas.put("en que semestre esta tu creador", "Mi creador está en 5to semestre de ingeniería en sistemas");
        respuestas.put("que semestre cursa tu creador", "Mi creador está cursando el 5to semestre de ingeniería en sistemas");
        respuestas.put("en que semestre estudia tu creador", "Mi creador estudia en 5to semestre de ingeniería en sistemas");
        
        // Comando para listar conversaciones
        respuestas.put("lista de conversaciones", listarConversaciones());
        respuestas.put("mostrar conversaciones", listarConversaciones());
        respuestas.put("ver conversaciones", listarConversaciones());
    }

    /**
     * Libera recursos al cerrar la aplicación
     */
    @Override
    public void dispose() {
        try {
            if (executorService != null) {
                executorService.shutdown();
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            }
            if (discordClient != null) {
                discordClient.shutdown();
                discordClient = null;
            }
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, "Error al cerrar el ExecutorService", e);
            Thread.currentThread().interrupt();
        } finally {
            super.dispose();
        }
    }

    /**
     * Procesa un mensaje recibido y genera una respuesta
     * @param messageText Texto del mensaje recibido
     * @return Respuesta generada
     */
    protected String processMessage(String messageText) {
        if (messageText == null) return "Lo siento, no puedo procesar un mensaje vacío.";
        
        try {
            messageText = messageText.toLowerCase().trim();
            if (messageText.startsWith("!")) { // Cambiamos / por ! para los comandos de Discord
                return processCommand(messageText);
            }
            return respuestas.getOrDefault(messageText,
                "Lo siento, no entiendo ese mensaje. ¿Podrías reformularlo o usar !help para ver los comandos disponibles?");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error procesando mensaje: " + messageText, e);
            return "Lo siento, ocurrió un error al procesar tu mensaje.";
        }
    }

    /**
     * Procesa un comando y genera una respuesta
     * @param command Comando recibido
     * @return Respuesta al comando
     */
    protected String processCommand(String command) {
        if (command == null) return "Comando no válido";
        
        try {
            switch (command.toLowerCase().trim()) {
                case "!start":
                    return "¡Bienvenido! Soy tu asistente virtual. ¿En qué puedo ayudarte?";
                case "!help":
                    return "Comandos disponibles:\n" +
                           "!start - Iniciar conversación\n" +
                           "!help - Ver comandos disponibles\n" +
                           "!hora - Ver hora actual\n" +
                           "!info - Información general\n" +
                           "!conversaciones - Ver lista de conversaciones";
                case "!hora":
                    return "La hora actual es: " + 
                           LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                case "!info":
                    return "Soy un bot de asistencia creado para ayudarte.\n" +
                           "Puedes preguntarme sobre diversos temas o usar los comandos disponibles.";
                case "!conversaciones":
                    return listarConversaciones();
                default:
                    return "Comando no reconocido. Usa !help para ver los comandos disponibles.";
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error procesando comando: " + command, e);
            return "Lo siento, ocurrió un error al procesar el comando.";
        }
    }
    
    /**
     * Listener para procesar mensajes de Discord
     */
    private class DiscordMessageListener extends ListenerAdapter {
        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            if (!isRunning.get()) return;
            
            // Ignorar mensajes propios para evitar bucles
            if (event.getAuthor().isBot()) return;
            
            CompletableFuture.runAsync(() -> {
                try {
                    String messageText = event.getMessage().getContentRaw();
                    String username = event.getAuthor().getName();
                    String channelName = event.getChannel().getName();
                    String guildName = "";
                    
                    // Si el mensaje viene de un servidor, registrar el nombre del servidor
                    if (event.isFromGuild()) {
                        guildName = event.getGuild().getName();
                    }
                    
                    // Registrar conversación
                    boolean conversacionEncontrada = false;
                    String conversationId = guildName.isEmpty() ? 
                            "DM: " + username : guildName + " #" + channelName;
                    
                    for (int i = 0; i < cantConversaciones; i++) {
                        if (conversationId.equals(conversaciones[i])) {
                            conversacionEncontrada = true;
                            break;
                        }
                    }
                    
                    if (!conversacionEncontrada && cantConversaciones < conversaciones.length) {
                        conversaciones[cantConversaciones++] = conversationId;
                        addLogMessage("Nueva conversación: " + conversationId);
                    }
                    
                    // Registrar mensaje recibido
                    addLogMessage("Mensaje de " + username + ": " + messageText);
                    
                    // Procesar el mensaje y enviar respuesta
                    String response = DiscordBotGUI.this.processMessage(messageText);
                    
                    // Enviar respuesta con mecanismo de escritura
                    TextChannel textChannel = event.getChannel().asTextChannel();
                    textChannel.sendTyping().queue(); // "El bot está escribiendo..."
                    
                    // Simular tiempo de escritura basado en la longitud del mensaje
                    // (Entre 1 y 3 segundos)
                    long typingTime = Math.min(1000 + (response.length() * 10), 3000);
                    Thread.sleep(typingTime);
                    
                    // Enviar mensaje
                    textChannel.sendMessage(response).queue(
                        // Callback de éxito
                        sentMessage -> addLogMessage("Respuesta enviada a " + username),
                        // Callback de error
                        error -> addLogMessage("Error al enviar respuesta: " + error.getMessage())
                    );
                    
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error procesando mensaje de Discord", e);
                    addLogMessage("Error: " + e.getMessage());
                }
            }, executorService);
        }
    }
}