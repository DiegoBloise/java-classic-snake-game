package io.github.diegobloise.snake;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main implements ApplicationListener {

    private final int CELL_SIZE = 20;
    private final int LINES = 25;
    private final int COLUMNS = 25;
    private final int SCREEN_WIDTH = COLUMNS * CELL_SIZE;
    private final int SCREEN_HEIGHT = LINES * CELL_SIZE;

    private Vector2 apple = new Vector2();
    private Vector2 snakeHead;
    private List<Vector2> snakeBody = new ArrayList<>();

    private int gameSpeed;

    private enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    };

    Direction currentDirection;

    private ShapeRenderer shape;

    private void newApple() {
        apple = null;
        while (apple == null) {
            Vector2 newApplePos = new Vector2(MathUtils.random(0, COLUMNS - 1), MathUtils.random(0, LINES - 1));
            if (!snakeBody.contains(newApplePos)) {
                apple = newApplePos;
                break;
            }
        }
    }

    void setupGame() {
        gameSpeed = 10;

        Gdx.graphics.setForegroundFPS(gameSpeed);

        currentDirection = Direction.RIGHT;

        snakeBody.clear();
        snakeHead = new Vector2((int) (COLUMNS / 2f), (int) (LINES / 2f));
        for (int i = 1; i <= 3; i++) {
            snakeBody.add(new Vector2(snakeHead.x - i, snakeHead.y));
        }

        newApple();
    }

    void checkCollisions() {
        // Verifica se a cabeça da cobra está fora dos limites
        if (snakeHead.x < 0 || snakeHead.x >= LINES || snakeHead.y < 0 ||
                snakeHead.y >= COLUMNS) {
            setupGame();
        }

        // Verifica se a cabeça da cobra colidiu contra o corpo
        if (snakeBody.contains(snakeHead)) {
            setupGame();
        }

        if (snakeHead.equals(apple)) {
            snakeBody.add(new Vector2(snakeBody.get(snakeBody.size() - 1)));
            if (snakeBody.size() % 2 == 0) {
                gameSpeed++;
                Gdx.graphics.setForegroundFPS(gameSpeed);
            }
            newApple();
        }
    }

    void updateSnake() {
        snakeBody.get(0).set(snakeHead.x, snakeHead.y);
        // Atualiza a posição do corpo
        for (int i = snakeBody.size() - 1; i > 0; i--) {
            snakeBody.get(i).set(snakeBody.get(i - 1));
        }

        // Atualiza a cabeça da cobra
        switch (currentDirection) {
            case UP:
                snakeHead.y++;
                break;
            case DOWN:
                snakeHead.y--;
                break;
            case LEFT:
                snakeHead.x--;
                break;
            case RIGHT:
                snakeHead.x++;
                break;
        }

        checkCollisions();
    }

    private void input() {
        getInput();
    }

    private void getInput() {
        if ((Gdx.input.isKeyPressed(Input.Keys.W) ||
                Gdx.input.isKeyPressed(Input.Keys.UP))
                && currentDirection != Direction.DOWN) {

            currentDirection = Direction.UP;
        } else if ((Gdx.input.isKeyPressed(Input.Keys.S) ||
                Gdx.input.isKeyPressed(Input.Keys.DOWN))
                && currentDirection != Direction.UP) {

            currentDirection = Direction.DOWN;
        } else if ((Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) &&
                currentDirection != Direction.RIGHT) {

            currentDirection = Direction.LEFT;
        } else if ((Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) &&
                currentDirection != Direction.LEFT) {

            currentDirection = Direction.RIGHT;
        }
    }

    private void logic() {
        updateSnake();
    }

    private void draw() {
        drawSnake();
        drawApple();
        drawGrid();
    }

    private void drawSnake() {
        shape.begin(ShapeRenderer.ShapeType.Filled);

        // Draw Snake Head
        shape.setColor(new Color(0 / 255f, 228 / 255f, 48 / 255f, 255 / 255f));
        shape.rect(snakeHead.x * CELL_SIZE, snakeHead.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        // Draw Snake Body
        for (Vector2 segment : snakeBody) {
            shape.setColor(new Color(0 / 255f, 117 / 255f, 44 / 255f, 255 / 255f));
            shape.rect(segment.x * CELL_SIZE, segment.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        shape.end();
    }

    private void drawApple() {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.RED);
        shape.rect(apple.x * CELL_SIZE, apple.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        shape.end();
    }

    private void drawGrid() {
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(new Color(25 / 255f, 25 / 255f, 25 / 255f, 255 / 255f));
        for (int line = 0; line <= SCREEN_WIDTH; line += CELL_SIZE) {
            shape.line(line, 0, line, SCREEN_HEIGHT);
            shape.line(0, line, SCREEN_WIDTH, line);
        }
        shape.end();
    }

    @Override
    public void create() {
        Gdx.graphics.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        shape = new ShapeRenderer();
        setupGame();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        input();
        logic();
        draw();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        shape.dispose();
    }
}
