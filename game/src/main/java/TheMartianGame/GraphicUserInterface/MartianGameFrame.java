package TheMartianGame.GraphicUserInterface;

import TheMartianGame.MartianGame;
import TheMartianGame.DefaultCommandNames;
import TheMartianGame.DefaultObjectConfig;
import TheMartianGame.DefaultCharacterConfig;
import TheMartianGame.DefaultEnemyConfig;
import Game.Type.GameObject;

import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MartianGameFrame extends javax.swing.JFrame {

    private CardLayout cardLayout;
    private JPanel mainContainer;

    // Componenti Schermata Menu
    private JTextField txtPlayerName;
    private JLabel lblErrorMsg;
    private static final String PLACEHOLDER_NAME = "Inserisci nome...";

    // Dati Giocatore
    private String playerName = "";

    // Componenti Schermata Gioco
    private MartianGame game;
    private JTextArea jTextArea1;
    private JTextField jTextFieldInput;
    private JLabel lblCurrentRoom;
    private JProgressBar oxygenBar;
    private JList<String> jList1;
    private MapPanel mapPanel;

    // Gestione Audio Globale
    private Clip currentAudioClip;
    private boolean isAlarmActive = false;
    private boolean isGameOverPlayed = false;
    private boolean isVictoryPlayed = false;
    
    // Controlli Volume
    private float currentVolume = 0.8f; 
    private boolean isMuted = false;
    private JSlider volumeSlider;
    private JToggleButton muteButton;

    // Animazione Lampeggio O2
    private Timer blinkTimer;
    private boolean blinkState = false;

    // Animazioni Visive
    private Timer menuParticleTimer;
    private final List<DustParticle> particles = new ArrayList<>();

    // Effetto Macchina da Scrivere per i testi di gioco
    private Timer typewriterTimer;

    public MartianGameFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("ARES MISSION - MARS SURVIVAL HARDCORE");
        setSize(1150, 800);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // Inizializza le due schermate nel CardLayout
        mainContainer.add(createStartMenuPanel(), "MENU");
        mainContainer.add(createGamePanel(), "GAME");

        // Crea un Root Panel per mantenere fissi i controlli audio in basso
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.add(mainContainer, BorderLayout.CENTER);
        rootPanel.add(createAudioControlPanel(), BorderLayout.SOUTH);

        setContentPane(rootPanel);

        initBlinkAnimation();
        initMenuParticlesAnimation();

        // L'audio parte all'avvio
        playMusic("/audio/bg_ambient.wav", true);
    }

    // --- PANNELLO CONTROLLI AUDIO (FISSO IN BASSO) ---

    private JPanel createAudioControlPanel() {
        JPanel audioPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        audioPanel.setBackground(new Color(20, 20, 20));
        audioPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(60, 60, 60)));

        muteButton = new JToggleButton("🔊");
        muteButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        muteButton.setBackground(new Color(40, 40, 40));
        muteButton.setForeground(Color.WHITE);
        muteButton.setFocusPainted(false);
        muteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        muteButton.addActionListener(e -> {
            isMuted = muteButton.isSelected();
            muteButton.setText(isMuted ? "🔇" : "🔊");
            updateVolume();
        });

        JLabel volLabel = new JLabel("VOLUME");
        volLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        volLabel.setForeground(Color.GRAY);

        volumeSlider = new JSlider(0, 100, 80);
        volumeSlider.setBackground(new Color(20, 20, 20));
        volumeSlider.setFocusable(false);
        
        volumeSlider.addChangeListener(e -> {
            currentVolume = volumeSlider.getValue() / 100f;
            if (currentVolume == 0f) {
                if (!isMuted) {
                    isMuted = true;
                    muteButton.setSelected(true);
                    muteButton.setText("🔇");
                }
            } else {
                if (isMuted) {
                    isMuted = false;
                    muteButton.setSelected(false);
                    muteButton.setText("🔊");
                }
            }
            updateVolume();
        });

        audioPanel.add(muteButton);
        audioPanel.add(volLabel);
        audioPanel.add(volumeSlider);
        
        return audioPanel;
    }

    // --- LOGICA VOLUME ---

    private void updateVolume() {
        setClipVolume(currentAudioClip);
    }

    private void setClipVolume(Clip clip) {
        if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            if (isMuted || currentVolume == 0f) {
                gainControl.setValue(gainControl.getMinimum());
            } else {
                float dB = (float) (Math.log10(currentVolume) * 20.0);
                dB = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), dB));
                gainControl.setValue(dB);
            }
        }
    }

    // --- EFFETTO MACCHINA DA SCRIVERE ---

    private void typeWriterEffect(String text) {
        if (typewriterTimer != null && typewriterTimer.isRunning()) {
            typewriterTimer.stop();
        }

        final int[] index = {0};
        
        typewriterTimer = new Timer(5, e -> {
            if (index[0] < text.length()) {
                jTextArea1.append(String.valueOf(text.charAt(index[0])));
                index[0]++;
                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
            } else {
                ((Timer) e.getSource()).stop();
                jTextArea1.append("\n\n"); 
            }
        });
        typewriterTimer.start();
    }

    // --- SCHERMATA INIZIALE (MENU ARANCIONE / NERO) ---

    private JPanel createStartMenuPanel() {
        JPanel menuPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                synchronized (particles) {
                    for (DustParticle p : particles) {
                        g2d.setColor(p.color);
                        g2d.fillOval((int) p.x, (int) p.y, p.size, p.size);
                    }
                }
            }
        };
        menuPanel.setBackground(new Color(15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel titleLabel = new JLabel("ARES MISSION: MARS", SwingConstants.CENTER);
        titleLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 36));
        titleLabel.setForeground(new Color(255, 110, 0));

        JLabel subtitleLabel = new JLabel("INSERISCI LE CREDENZIALI DELL'ASTRONAUTA", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        subtitleLabel.setForeground(new Color(200, 80, 0));

        txtPlayerName = new JTextField(PLACEHOLDER_NAME, 15);
        txtPlayerName.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        txtPlayerName.setBackground(Color.BLACK);
        txtPlayerName.setForeground(Color.GRAY);
        txtPlayerName.setCaretColor(new Color(255, 140, 0));
        txtPlayerName.setHorizontalAlignment(JTextField.CENTER);
        txtPlayerName.setBorder(BorderFactory.createLineBorder(new Color(255, 110, 0), 2));

        txtPlayerName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtPlayerName.getText().equals(PLACEHOLDER_NAME)) {
                    txtPlayerName.setText("");
                    txtPlayerName.setForeground(new Color(255, 140, 0));
                }
                lblErrorMsg.setText(" "); 
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtPlayerName.getText().trim().isEmpty()) {
                    txtPlayerName.setText(PLACEHOLDER_NAME);
                    txtPlayerName.setForeground(Color.GRAY);
                }
            }
        });

        txtPlayerName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    playEffect("/audio/click.wav");
                    startGame();
                }
            }
        });

        lblErrorMsg = new JLabel(" ", SwingConstants.CENTER);
        lblErrorMsg.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
        lblErrorMsg.setForeground(new Color(255, 50, 50));

        JButton btnStart = createBtn("► AVVIA MISSIONE", e -> startGame());
        btnStart.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        btnStart.setBackground(new Color(255, 110, 0));
        btnStart.setForeground(Color.BLACK);

        gbc.gridy = 0; menuPanel.add(titleLabel, gbc);
        gbc.gridy = 1; menuPanel.add(subtitleLabel, gbc);
        gbc.gridy = 2; menuPanel.add(txtPlayerName, gbc);
        gbc.gridy = 3; menuPanel.add(lblErrorMsg, gbc); 
        gbc.gridy = 4; menuPanel.add(btnStart, gbc);

        return menuPanel;
    }

    private void startGame() {
        String inputName = txtPlayerName.getText().trim();

        if (inputName.isEmpty() || inputName.equals(PLACEHOLDER_NAME)) {
            lblErrorMsg.setText("⚠️ ERRORE: NOME ASTRONAUTA OBBLIGATORIO PER AVVIARE LA MISSIONE!");
            playEffect("/audio/error.wav"); 
            triggerScreenShake();
            return; 
        }

        playerName = inputName;
        
        if (menuParticleTimer != null && menuParticleTimer.isRunning()) {
            menuParticleTimer.stop();
        }
        
        cardLayout.show(mainContainer, "GAME");
        initGame();
    }

    private JPanel createGamePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(15, 15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(10, 5));
        topPanel.setBackground(new Color(28, 28, 28));
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        lblCurrentRoom = new JLabel("📍 STANZA ATTUALE: -");
        lblCurrentRoom.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        lblCurrentRoom.setForeground(Color.WHITE);

        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        topRightPanel.setOpaque(false);

        JLabel lblO2 = new JLabel("🫁 O2 TUTA:");
        lblO2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        lblO2.setForeground(Color.CYAN);

        oxygenBar = new JProgressBar(0, 100);
        oxygenBar.setValue(100);
        oxygenBar.setStringPainted(true);
        oxygenBar.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
        oxygenBar.setForeground(Color.GREEN);
        oxygenBar.setBackground(Color.DARK_GRAY);
        oxygenBar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
            @Override
            protected Color getSelectionForeground() { return Color.BLACK; }
            @Override
            protected Color getSelectionBackground() { return Color.BLACK; }
        });
        oxygenBar.setPreferredSize(new Dimension(160, 26));

        topRightPanel.add(lblO2);
        topRightPanel.add(oxygenBar);

        topPanel.add(lblCurrentRoom, BorderLayout.CENTER);
        topPanel.add(topRightPanel, BorderLayout.EAST);

        jTextArea1 = new JTextArea();
        jTextArea1.setEditable(false);
        jTextArea1.setBackground(Color.BLACK);
        jTextArea1.setForeground(new Color(0, 255, 120));
        jTextArea1.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        jTextArea1.setMargin(new Insets(12, 12, 12, 12));
        JScrollPane scroll = new JScrollPane(jTextArea1);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(380, 0));
        rightPanel.setOpaque(false);

        mapPanel = new MapPanel();
        mapPanel.setPreferredSize(new Dimension(380, 210));
        mapPanel.setMaximumSize(new Dimension(380, 210));

        JPanel dPadPanel = new JPanel(new GridLayout(3, 3, 4, 4));
        dPadPanel.setBackground(new Color(28, 28, 28));
        dPadPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60)), 
            "SPOSTAMENTO TATTICO", 
            0, 0, new Font(Font.MONOSPACED, Font.BOLD, 13), Color.WHITE
        ));
        dPadPanel.setPreferredSize(new Dimension(380, 110));
        dPadPanel.setMaximumSize(new Dimension(380, 110));

        dPadPanel.add(new JLabel("")); 
        dPadPanel.add(createBtn("▲ NORD", e -> doMove("NORD"))); 
        dPadPanel.add(new JLabel(""));
        dPadPanel.add(createBtn("◄ OVEST", e -> doMove("OVEST"))); 
        dPadPanel.add(new JLabel("")); 
        dPadPanel.add(createBtn("► EST", e -> doMove("EST")));
        dPadPanel.add(new JLabel("")); 
        dPadPanel.add(createBtn("▼ SUD", e -> doMove("SUD"))); 
        dPadPanel.add(new JLabel(""));

        JPanel actPanel = new JPanel(new GridLayout(2, 2, 6, 6));
        actPanel.setBackground(new Color(28, 28, 28));
        actPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 255, 120)), 
            "AZIONI", 
            0, 0, new Font(Font.MONOSPACED, Font.BOLD, 13), new Color(0, 255, 120)
        ));
        actPanel.setPreferredSize(new Dimension(380, 90));
        actPanel.setMaximumSize(new Dimension(380, 90));

        actPanel.add(createBtn("🔍 ESAMINA", e -> doAction("EXAMINE")));
        actPanel.add(createBtn("✋ PRENDI", e -> doAction("PICK")));
        actPanel.add(createBtn("⚡ RIPARA", e -> doAction("REPAIR")));
        actPanel.add(createBtn("⚙️ USA", e -> doAction("USE")));

        jList1 = new JList<>();
        jList1.setBackground(Color.BLACK);
        jList1.setForeground(new Color(0, 255, 120));
        jList1.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
        
        JScrollPane invScroll = new JScrollPane(jList1);
        invScroll.getViewport().setBackground(Color.BLACK);
        invScroll.setBackground(Color.BLACK);
        invScroll.setViewportBorder(null);
        
        invScroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 255, 120), 1), 
            "INVENTARIO", 
            0, 0, new Font(Font.MONOSPACED, Font.BOLD, 13), new Color(0, 255, 120)
        ));
        
        if (invScroll.getVerticalScrollBar() != null) {
            invScroll.getVerticalScrollBar().setBackground(Color.BLACK);
        }
        if (invScroll.getHorizontalScrollBar() != null) {
            invScroll.getHorizontalScrollBar().setBackground(Color.BLACK);
        }
        invScroll.setPreferredSize(new Dimension(380, 110));

        rightPanel.add(mapPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        rightPanel.add(dPadPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        rightPanel.add(actPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        rightPanel.add(invScroll);

        JPanel bottomPanel = new JPanel(new BorderLayout(8, 5));
        bottomPanel.setBackground(new Color(28, 28, 28));
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 255, 120), 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        JLabel lblInputPrompt = new JLabel("💻 INPUT TERMINALE (Stanza 5):");
        lblInputPrompt.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
        lblInputPrompt.setForeground(new Color(0, 255, 120));

        jTextFieldInput = new JTextField();
        jTextFieldInput.setBackground(Color.BLACK);
        jTextFieldInput.setForeground(new Color(0, 255, 120));
        jTextFieldInput.setCaretColor(new Color(0, 255, 120));
        jTextFieldInput.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        jTextFieldInput.addActionListener(e -> {
            playEffect("/audio/click.wav");
            String txt = jTextFieldInput.getText();
            jTextFieldInput.setText("");
            if (game.enterPin(txt)) {
                typeWriterEffect("> INPUT: " + txt + "\n🔓 PIN CORRETTO! Porta sbloccata a OVEST.");
                updateUI();
            } else {
                typeWriterEffect("> INPUT: " + txt + "\n❌ PIN ERRATO o non ti trovi nel Laboratorio.");
                playEffect("/audio/error.wav");
                triggerScreenShake();
            }
        });

        bottomPanel.add(lblInputPrompt, BorderLayout.WEST);
        bottomPanel.add(jTextFieldInput, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scroll, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JButton createBtn(String label, java.awt.event.ActionListener al) {
        JButton b = new JButton(label);
        b.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        b.setMargin(new Insets(3, 3, 3, 3));
        b.setBackground(new Color(40, 40, 40));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        b.addActionListener(e -> {
            playEffect("/audio/click.wav");
            if (al != null) {
                al.actionPerformed(e);
            }
        });

        b.addMouseListener(new MouseAdapter() {
            private Color originalBg;
            @Override
            public void mouseEntered(MouseEvent e) {
                originalBg = b.getBackground();
                b.setBackground(originalBg.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (originalBg != null) {
                    b.setBackground(originalBg);
                }
            }
        });

        return b;
    }

    // --- FINESTRA IN SOVRAIMPRESSIONE (FINE PARTITA) ---
    
    private void showEndGameDialog(boolean isVictory) {
        JDialog dialog = new JDialog(this, "Esito Missione", true);
        dialog.setSize(380, 260);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true); 
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(isVictory ? new Color(0, 255, 120) : new Color(255, 50, 50), 3));

        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBackground(new Color(20, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel(isVictory ? "🏆 VITTORIA!" : "💀 GAME OVER", SwingConstants.CENTER);
        title.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
        title.setForeground(isVictory ? new Color(0, 255, 120) : new Color(255, 50, 50));
        panel.add(title, BorderLayout.NORTH);

        String subText = isVictory ? "Segnale inviato.\nSoccorsi in arrivo." : "Ossigeno esaurito.\nMissione fallita.";
        JTextArea subtitle = new JTextArea(subText);
        subtitle.setEditable(false);
        subtitle.setOpaque(false);
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        subtitle.setFocusable(false);
        subtitle.setLineWrap(true);
        subtitle.setWrapStyleWord(true);
        // Centrare il testo in una JTextArea
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setMargin(new Insets(10, 10, 10, 10));
        
        panel.add(subtitle, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBackground(new Color(20, 20, 20));

        JButton btnRestart = createBtn("🔄 RIAVVIA MISSIONE", e -> {
            dialog.dispose();
            restartGame();
        });
        
        JButton btnMenu = createBtn("🏠 TORNA AL MENU", e -> {
            dialog.dispose();
            returnToMenu();
        });
        
        JButton btnExit = createBtn("🚪 ESCI", e -> System.exit(0));

        buttonPanel.add(btnRestart);
        buttonPanel.add(btnMenu);
        buttonPanel.add(btnExit);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(panel);

        // Ritardo di 1.5 secondi per permettere allo screen shake e al typewriter di terminare
        Timer dialogTimer = new Timer(1500, e -> {
            dialog.setVisible(true);
        });
        dialogTimer.setRepeats(false);
        dialogTimer.start();
    }

    private void restartGame() {
        jTextFieldInput.setEnabled(true);
        isGameOverPlayed = false;
        isVictoryPlayed = false;
        isAlarmActive = false;
        
        if (blinkTimer != null && blinkTimer.isRunning()) blinkTimer.stop();
        if (typewriterTimer != null && typewriterTimer.isRunning()) typewriterTimer.stop();
        
        initGame();
        playMusic("/audio/bg_ambient.wav", true);
    }
    
    private void returnToMenu() {
        stopAudio();
        isGameOverPlayed = false;
        isVictoryPlayed = false;
        isAlarmActive = false;
        
        if (blinkTimer != null && blinkTimer.isRunning()) blinkTimer.stop();
        if (typewriterTimer != null && typewriterTimer.isRunning()) typewriterTimer.stop();

        jTextArea1.setText("");
        jTextFieldInput.setText("");
        jTextFieldInput.setEnabled(true);
        oxygenBar.setValue(100);
        oxygenBar.setForeground(Color.GREEN);
        
        playMusic("/audio/bg_ambient.wav", true);
        
        if (menuParticleTimer != null && !menuParticleTimer.isRunning()) {
            menuParticleTimer.start();
        }
        
        cardLayout.show(mainContainer, "MENU");
    }

    private void initBlinkAnimation() {
        blinkTimer = new Timer(500, e -> {
            blinkState = !blinkState;
            if (game != null && game.getOxygenLevel() <= 25) {
                oxygenBar.setForeground(blinkState ? Color.RED : Color.BLACK);
            }
        });
    }

    private void initMenuParticlesAnimation() {
        Random rand = new Random();
        for (int i = 0; i < 40; i++) {
            particles.add(new DustParticle(
                rand.nextInt(1150),
                rand.nextInt(800),
                rand.nextInt(3) + 2,
                (rand.nextFloat() * 0.8f) + 0.2f,
                new Color(255, rand.nextInt(100) + 80, 0, rand.nextInt(150) + 50)
            ));
        }

        menuParticleTimer = new Timer(30, e -> {
            synchronized (particles) {
                for (DustParticle p : particles) {
                    p.x -= p.speed;
                    p.y += (Math.sin(p.x * 0.05) * 0.5);
                    if (p.x < 0) {
                        p.x = 1150;
                        p.y = rand.nextInt(800);
                    }
                }
            }
            mainContainer.repaint();
        });
        menuParticleTimer.start();
    }

    private void triggerScreenShake() {
        Point originalLoc = getLocation();
        new Thread(() -> {
            try {
                for (int i = 0; i < 6; i++) {
                    int offsetX = (i % 2 == 0 ? 6 : -6);
                    int offsetY = (i % 4 < 2 ? 6 : -6);
                    setLocation(originalLoc.x + offsetX, originalLoc.y + offsetY);
                    Thread.sleep(30);
                }
                setLocation(originalLoc);
            } catch (InterruptedException e) {
                setLocation(originalLoc);
            }
        }).start();
    }

    private void initGame() {
        try {
            game = new MartianGame(new DefaultCommandNames(), new DefaultObjectConfig(), new DefaultCharacterConfig(), new DefaultEnemyConfig());
            game.init();
            
            String introText = "--- SISTEMA SUPPORTO VITALE ARES ---\n" +
                               "Benvenuto Astronauta " + playerName + ".\n\n" +
                               game.getSystemGuide() +
                               game.getCurrentRoom().getFirstDescription();
            
            jTextArea1.setText(""); // Pulisce lo schermo prima di iniziare la nuova scrittura
            typeWriterEffect(introText);
            updateUI();
        } catch (Exception e) {
            jTextArea1.setText("Errore inizializzazione gioco: " + e.getMessage());
        }
    }

    private void doMove(String dir) {
        if (game.isEnd()) return;
        String res = game.move(dir);
        typeWriterEffect(res);
        updateUI();
    }

    private void doAction(String type) {
        if (game.isEnd()) return;
        String res = "";
        if (type.equals("EXAMINE")) res = game.executeExamine();
        if (type.equals("PICK")) res = game.executePickUp();
        if (type.equals("REPAIR")) res = game.executeRepair();
        if (type.equals("USE")) res = game.executeUse();
        
        typeWriterEffect(res);
        updateUI();
    }

    private void updateUI() {
        lblCurrentRoom.setText("📍 ASTRONAUTA " + playerName.toUpperCase() + " | STANZA: " + game.getCurrentRoom().getName());
        int o2 = game.getOxygenLevel();
        oxygenBar.setValue(o2);
        oxygenBar.setString(o2 + "%");

        if (game.isEnd()) {
            jTextFieldInput.setEnabled(false);
            if (blinkTimer.isRunning()) blinkTimer.stop();
            
            if (o2 <= 0) {
                if (!isGameOverPlayed) {
                    isGameOverPlayed = true;
                    typeWriterEffect("\n💀 CRITICO: L'ossigeno è terminato. L'astronauta " + playerName + " è deceduto su Marte.");
                    playMusic("/audio/game_over.wav", false);
                    triggerScreenShake();
                    showEndGameDialog(false); // Avvia popup di Game Over
                }
            } else {
                if (!isVictoryPlayed) {
                    isVictoryPlayed = true;
                    playMusic("/audio/victory.wav", false);
                    showEndGameDialog(true); // Avvia popup di Vittoria
                }
            }
            return;
        }

        if (o2 <= 25) {
            if (!blinkTimer.isRunning()) blinkTimer.start();
            if (!isAlarmActive) {
                isAlarmActive = true;
                playMusic("/audio/alarm_loop.wav", true);
                triggerScreenShake();
            }
        } else {
            if (blinkTimer.isRunning()) blinkTimer.stop();
            if (isAlarmActive) {
                isAlarmActive = false;
                playMusic("/audio/bg_ambient.wav", true);
            }
            if (o2 > 50) oxygenBar.setForeground(Color.GREEN);
            else oxygenBar.setForeground(Color.YELLOW);
        }

        mapPanel.setCurrentRoomId(game.getCurrentRoom().getId());

        DefaultListModel<String> m = new DefaultListModel<>();
        for (GameObject o : game.getInventory()) {
            m.addElement("• " + o.getName());
        }
        jList1.setModel(m);

        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
    }

    // --- GESTIONE AUDIO E REGOLAZIONE VOLUME ---

    private void playMusic(String resourcePath, boolean loop) {
        stopAudio();
        currentAudioClip = loadClip(resourcePath);
        if (currentAudioClip != null) {
            setClipVolume(currentAudioClip); 
            if (loop) {
                currentAudioClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            currentAudioClip.start();
        }
    }

    private void stopAudio() {
        if (currentAudioClip != null) {
            if (currentAudioClip.isRunning()) {
                currentAudioClip.stop();
            }
            currentAudioClip.close();
            currentAudioClip = null;
        }
    }

    private void playEffect(String resourcePath) {
        new Thread(() -> {
            Clip effectClip = loadClip(resourcePath);
            if (effectClip != null) {
                setClipVolume(effectClip); 
                effectClip.start();
            }
        }).start();
    }

    private Clip loadClip(String resourcePath) {
        try {
            InputStream is = getClass().getResourceAsStream(resourcePath);
            if (is == null) {
                System.err.println("❌ File audio non trovato: " + resourcePath);
                return null;
            }
            BufferedInputStream bis = new BufferedInputStream(is);
            AudioInputStream ais = AudioSystem.getAudioInputStream(bis);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            return clip;
        } catch (Exception e) {
            System.err.println("❌ Errore caricamento audio: " + resourcePath);
            return null;
        }
    }

    // --- STRUTTURA DATI PARTICELLE ---

    private static class DustParticle {
        double x, y;
        int size;
        float speed;
        Color color;

        DustParticle(double x, double y, int size, float speed, Color color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.speed = speed;
            this.color = color;
        }
    }

    // --- PANNELLO MAPPA ---

    private static class MapPanel extends JPanel {
        private int currentRoomId = MartianGame.ID_ROOM_OUTSIDE;
        private int pulseRadius = 0;
        private Timer pulseTimer;

        public MapPanel() {
            setBackground(Color.BLACK);
            setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)), 
                "MAPPA HUB SUPPORTO VITALE", 
                0, 0, 
                new Font(Font.MONOSPACED, Font.BOLD, 12), Color.CYAN
            ));

            pulseTimer = new Timer(50, e -> {
                pulseRadius = (pulseRadius + 1) % 20;
                repaint();
            });
            pulseTimer.start();
        }

        public void setCurrentRoomId(int id) {
            this.currentRoomId = id;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0, 255, 120));
            g2.setStroke(new BasicStroke(2));

            g2.drawLine(45, 40, 270, 40);   
            g2.drawLine(270, 40, 270, 130); 
            g2.drawLine(270, 130, 45, 130); 

            Stroke dashed = new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{4.0f}, 0.0f);
            g2.setStroke(dashed);
            g2.setColor(new Color(255, 180, 0));

            g2.drawLine(195, 50, 195, 85);
            g2.drawLine(300, 130, 330, 130);

            drawNode(g2, MartianGame.ID_ROOM_OUTSIDE, 15, 30, 65, 24, "1.EST", false);
            drawNode(g2, MartianGame.ID_ROOM_AIRLOCK, 90, 30, 65, 24, "2.AIR", false);
            drawNode(g2, MartianGame.ID_ROOM_SUBSTATION, 165, 30, 65, 24, "3.CAB", false);
            drawNode(g2, MartianGame.ID_ROOM_GREENHOUSE, 240, 30, 65, 24, "4.SER", false);

            drawNode(g2, MartianGame.ID_ROOM_LAB, 240, 120, 65, 24, "5.LAB", false);
            drawNode(g2, MartianGame.ID_ROOM_SECURITY, 165, 120, 65, 24, "6.SIC", false);
            drawNode(g2, MartianGame.ID_ROOM_REACTORS, 90, 120, 65, 24, "7.REA", false);
            drawNode(g2, MartianGame.ID_ROOM_COMMUNICATIONS, 15, 120, 65, 24, "8.COM", false);

            drawNode(g2, MartianGame.ID_ROOM_SUPPLY_DEPOT, 165, 75, 65, 24, "3B[OPZ]", true);
            drawNode(g2, MartianGame.ID_ROOM_MEDBAY, 305, 120, 65, 24, "5B[OPZ]", true);

            g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 10));
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawString("■ Principale", 15, 175);
            g2.setColor(new Color(255, 180, 0));
            g2.drawString("■ [OPZ] Ricarica O2", 120, 175);
        }

        private void drawNode(Graphics2D g2, int roomId, int x, int y, int w, int h, String label, boolean isOptional) {
            boolean isCurrent = (roomId == currentRoomId);
            
            if (isCurrent) {
                g2.setColor(new Color(255, 215, 0));
            } else if (isOptional) {
                g2.setColor(new Color(60, 45, 0));
            } else {
                g2.setColor(new Color(30, 30, 30));
            }
            g2.fillRect(x, y, w, h);

            if (isCurrent) {
                int alpha = Math.max(0, 255 - (pulseRadius * 12));
                g2.setColor(new Color(255, 255, 0, alpha));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRect(x - pulseRadius / 2, y - pulseRadius / 2, w + pulseRadius, h + pulseRadius);
            }

            g2.setStroke(new BasicStroke(1));
            if (isCurrent) {
                g2.setColor(Color.YELLOW);
            } else if (isOptional) {
                g2.setColor(new Color(255, 180, 0));
            } else {
                g2.setColor(Color.GRAY);
            }
            g2.drawRect(x, y, w, h);

            g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 10));
            if (isCurrent) {
                g2.setColor(Color.BLACK);
            } else if (isOptional) {
                g2.setColor(new Color(255, 200, 80));
            } else {
                g2.setColor(Color.WHITE);
            }

            FontMetrics fm = g2.getFontMetrics();
            int tx = x + (w - fm.stringWidth(label)) / 2;
            int ty = y + ((h - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(label, tx, ty);
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new MartianGameFrame().setVisible(true));
    }
}