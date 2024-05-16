package flappybirdinjava;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class Frame extends JFrame {
    private BackgroundPanel pnlGame = new BackgroundPanel();
    private Timer timer;
    private TimerTask timerTask;
    private Timer pipeSpawnTimer;

    private static Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    private static int taskBarHeight = (int)( scrnSize.getHeight() - winSize.getHeight() );

    //Components
    private Bird bird = new Bird();
    private ScoreText scoreText = new ScoreText();
    private StartScreen startScreen = new StartScreen(); //7주차
    private GameOverScreen gameOverScreen = new GameOverScreen(); //7주차
    private ResetButton resetButton = new ResetButton(); //7주차

    //Variable
    private float sizeMultiply = 1.0f;
    private final int ORIGIN_SIZE = 512;
    private boolean flagGameOver = false;
    private boolean flagGameStart = false; //7주차

    public Frame() {
        //Initialize
        setTitle("Flappy Bird In Java");
        setSize(513, 512);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setMinimumSize( new Dimension(256, 256) );
        setLayout( new CardLayout() );

        //Game Screen
        pnlGame.setLayout(null);

        //7주차
        startScreen.setLocation(164, 123);
        startScreen.setSize(0, 0);
        pnlGame.add(startScreen);

        gameOverScreen.setLocation(160, 145);
        gameOverScreen.setSize(0, 0);
        pnlGame.add(gameOverScreen);

        resetButton.setLocation(204, 276);
        resetButton.setSize(0, 0);
        pnlGame.add(resetButton);
        //

        scoreText.setLocation(0, 0);
        scoreText.setSize(0, 0);
        pnlGame.add(scoreText);

        bird.setLocation(100, 224); //7주차
        bird.setSize(0, 0);
        pnlGame.add(bird);

        add(pnlGame, "Game");
        pnlGame.addMouseListener( new MyMouseListener() );

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                pnlGame.update();
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 10);
        //7주차
    } //Constructor

    public float getSizeMultiply() {
        return sizeMultiply;
    }

    public int getTaskBarHeight() {
        return taskBarHeight;
    }

    public Bird getBird() {
        return bird;
    }

    public void gameOver() {
        //7주차
        if (flagGameOver) {
            return;
        }
        //
        flagGameOver = true;
        pipeSpawnTimer.cancel();

        //7주차
        gameOverScreen.setVisible(true);
        resetButton.setVisible(true);
        //
    }

    public boolean isGameOver() {
        return flagGameOver;
    }

    public void addScore() {
        scoreText.addScore(1);
    }

    //7주차
    public void startGame() {
        if (flagGameStart == false) {
            flagGameStart = true;
            flagGameOver = false;
            startScreen.setVisible(false);

            scoreText.resetScore();

            pipeSpawnTimer = new Timer();
            TimerTask pipeSpawnTimerTask = new TimerTask() {
                @Override
                public void run() {
                    int randY = (int)(Math.random() * 472);
                    int clampY = Main.clamp(randY, PipeSpawner.GAP + Pipe.MIN_HEIGHT, 472 - PipeSpawner.GAP - Pipe.MIN_HEIGHT);
                    PipeSpawner.spawnPipe(pnlGame, clampY);
                }
            };
            pipeSpawnTimer.scheduleAtFixedRate(pipeSpawnTimerTask, 0, PipeSpawner.SPAWN_DELAY);
        }
    }

    public void resetGame() {
        if (flagGameOver) {
            flagGameStart = false;

            pipeSpawnTimer.cancel();
            pipeSpawnTimer.purge();

            startScreen.setVisible(true);
            gameOverScreen.setVisible(false);
            resetButton.setVisible(false);

            bird.setLocation(100, 224); //7주차
            for ( Component k : pnlGame.getComponents() ) {
                try {
                    Pipe p = (Pipe)k;
                    pnlGame.remove(p);
                }
                catch (Exception e) {}
            }
            repaint();
            revalidate();
        }
    }

    public void initGmae() {
        pnlGame.update();
    }

    public boolean isGameStart() {
        return flagGameStart;
    }
    //

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int width = getWidth();
        int height = getHeight();

        if (width > height) {
            setSize(height, height);
        }
        else {
            setSize(width, width);
        }
        sizeMultiply = (float)(getHeight() - taskBarHeight) / (float)(ORIGIN_SIZE - taskBarHeight);
    }

    //Listeners
    private class MyMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            bird.jump();
        }
    }
    
} //Frame class
