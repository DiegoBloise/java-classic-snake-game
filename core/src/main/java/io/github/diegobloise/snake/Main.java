package io.github.diegobloise.snake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    private final int CELL_SIZE = 20;
    private final int LINES = 25;
    private final int COLUMNS = 25;
    private final int SCREEN_WIDTH = COLUMNS * CELL_SIZE;
    private final int SCREEN_HEIGHT = LINES * CELL_SIZE;

    private Vector2 apple = new Vector2();
    private List<Vector2> snake = new ArrayList<>();

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
        apple.set(MathUtils.random(0, COLUMNS - 1), MathUtils.random(0, LINES - 1));
    }

    void setupGame() {
        gameSpeed = 8;

        Gdx.graphics.setForegroundFPS(gameSpeed);

        currentDirection = Direction.RIGHT;

        newApple();

        snake.clear();
        snake.add(new Vector2((int) (COLUMNS / 2f), (int) (LINES / 2f)));
        for (int i = 1; i < 3; i++) {
            snake.add(new Vector2(snake.get(0).x - i, snake.get(0).y));
        }
    }

    void checkCollisions() {
        // Verifica se a cabeça da cobra está fora dos limites
        if (snake.get(0).x < 0 || snake.get(0).x >= LINES || snake.get(0).y < 0 ||
            snake.get(0).y >= COLUMNS) {
            setupGame();
        }

        if (snake.get(0).x == apple.x && snake.get(0).y == apple.y) {
            snake.add(new Vector2());
            if (snake.size() % 2 == 0) {
                gameSpeed++;
                Gdx.graphics.setForegroundFPS(gameSpeed);
            }
            newApple();
        }
    }

    void updateSnake() {
        // Atualiza a posição do corpo
        for (int i = snake.size() - 1; i > 0; i--) {
            snake.get(i).set(snake.get(i - 1));
        }

        // for (int i = 1; i < snakeLength; i++) {
        //     snake[i] = snake[];
        // }

        // Atualiza a cabeça da cobra
        switch (currentDirection) {
            case UP:
                snake.get(0).y++;
                break;
            case DOWN:
                snake.get(0).y--;
                break;
            case LEFT:
                snake.get(0).x--;
                break;
            case RIGHT:
                snake.get(0).x++;
                break;
        }

        checkCollisions();
    }

    private void input() {
        getInput();
    }

    private void getInput() {
        if ((Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) && currentDirection != Direction.DOWN)
            currentDirection = Direction.UP;

        if ((Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) && currentDirection != Direction.UP)
            currentDirection = Direction.DOWN;

        if ((Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) &&
            currentDirection != Direction.RIGHT) {
            currentDirection = Direction.LEFT;
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) &&
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
        for (int i = 0; i < snake.size() - 1; i++) {
            shape.setColor(i == 0 ? new Color(0 / 255f, 228 / 255f, 48 / 255f, 255 / 255f) : new Color(0 / 255f, 117 / 255f, 44 / 255f, 255 / 255f));
            shape.rect(snake.get(i).x * CELL_SIZE, snake.get(i).y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
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
        shape.setColor(new Color(25 / 255f,25 / 255f,25 / 255f,255 / 255f));
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
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        input();
        logic();
        draw();
    }

    @Override
    public void dispose() {
        shape.dispose();
    }
}
