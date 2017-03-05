package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen2 implements Screen {
    final Drop game;

    Texture dropImage;
    Texture ballImage;
    Texture batImage;
    Texture brickImage, brickImage1, brickImage2, brickImage3;
    Texture background;
    Sound dropSound;
    Music rainMusic;
    OrthographicCamera camera;
    Rectangle bat;
    Ball ball;
    Circle bola;
    Array<Block> blocks;
    Array<Rectangle> raindrops;
    long lastDropTime;
    int dropsGathered;
    int batWidth = 128, batHeight = 32;
    int ballWidth = 32, ballHeight = 32;
    int cameraWidth = 800, cameraHeight = 480;
    int contRotos;

    public GameScreen2(final Drop gam) {
        this.game = gam;
        /*
        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = new Texture(Gdx.files.internal("star.png"));
        ballImage = new Texture(Gdx.files.internal("ball_silver2.png"));
        batImage = new Texture(Gdx.files.internal("bat_black.png"));
        brickImage1 = new Texture(Gdx.files.internal("Walls/brick.png"));
        brickImage2 = new Texture(Gdx.files.internal("Walls/brick_blue.png"));
        brickImage3 = new Texture(Gdx.files.internal("brick_red_big.png"));
        brickImage = new Texture(Gdx.files.internal("brick_blue.png"));
        background = new Texture(Gdx.files.internal("background.jpg"));

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);
        rainMusic.play();
        */
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, cameraWidth, cameraHeight);

        // create a Rectangle to logically represent the bucket
        bat = new Rectangle();
        bat.x = cameraWidth / 2 - batWidth / 2; // center the bucket horizontally
        bat.y = 20; // bottom left corner of the bucket is 20 pixels above
        // the bottom screen edge
        bat.width = batWidth;
        bat.height = batHeight;

        ball = new Ball(cameraWidth / 2 - ballWidth / 2, 50 + batHeight, ballWidth, cameraWidth, cameraHeight);

        // create the raindrops array and spawn the first raindrop
        raindrops = new Array<Rectangle>();
        blocks = new Array<Block>();

        contRotos=0;

        generaBloques();
        spawnRaindrop();

    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, cameraWidth - 64);
        raindrop.y = cameraHeight;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    private void generaBloques(){
        for(int i=1; i<10; i++){
            blocks.add(new Block(i*64, cameraHeight-100, 64, 32, 1));
        }
        for(int i=1; i<10; i++){
            blocks.add(new Block(i*64, cameraHeight-170, 64, 32, 1));
        }
    }

    private void derrota(){
        System.out.println("DERROTA");
    }

    private void victoria(){
        System.out.println("VICTORIA");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops
        game.batch.begin();
        game.batch.draw(background, 0, 0, 800, 480);
        game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, cameraHeight);
        game.batch.draw(ballImage, ball.x, ball.y);

        //-----bate----
        game.batch.draw(batImage, bat.x, bat.y, bat.width, bat.height);
        //game.batch.draw(brickImage3, bat.getX(), bat.getY(), bat.getWidth(), bat.getHeight());
        //game.batch.draw(brickImage3, bat.getX(), bat.getY(), bat.getWidth()*0.2f, bat.getHeight());
        //game.batch.draw(brickImage3, bat.getX() + bat.getWidth()*0.8f, bat.getY(), bat.getWidth()*0.2f, bat.getHeight());
        //game.batch.draw(brickImage3, bat.getX(), bat.getY(), 1, bat.getHeight()-5);
        //game.batch.draw(brickImage3, bat.getX()+bat.getWidth()-1, bat.getY(), 1, bat.getHeight()-5);
        //----bate----

        for (Rectangle raindrop : raindrops) {
            game.batch.draw(dropImage, raindrop.x, raindrop.y, raindrop.width, raindrop.height);
        }
        for (Block block : blocks) {
            game.batch.draw(brickImage, block.x, block.y, block.width, block.height);
        }
        game.batch.end();

        // process user input
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bat.x = touchPos.x - batWidth / 2;
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT))
            bat.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            bat.x += 200 * Gdx.graphics.getDeltaTime();

        // make sure the bucket stays within the screen bounds
        if (bat.x < 0)
            bat.x = 0;
        if (bat.x > cameraWidth - batWidth)
            bat.x = cameraWidth - batWidth;

        if(ball.getY() < 0-ball.getRadius()){
            derrota();
        }

        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 2140000000)
            spawnRaindrop();

        // move the raindrops, remove any that are beneath the bottom edge of
        // the screen or that hit the bucket. In the later case we increase the
        // value our drops counter and add a sound effect.
        Iterator<Rectangle> iter = raindrops.iterator();
        while (iter.hasNext()) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 64 < 0)
                iter.remove();
            if (raindrop.overlaps(bat)) {
                dropsGathered++;
                dropSound.play();
                iter.remove();
            }
        }


        if(blocks.size <= 0)
            victoria();
        Iterator<Block> iterb = blocks.iterator();
        while (iterb.hasNext()) {
            Block block = iterb.next();
            if (Intersector.overlaps(ball, block)) {
                dropSound.play();
                block.restaVida();
                if (Intersector.overlaps(ball, new Rectangle(block.getX(), block.getY(), block.getWidth(), 1)) || Intersector.overlaps(ball, new Rectangle(block.getX(), block.getY()+block.getHeight(), block.getWidth(), 1))){
                    ball.bounceY();
                }else{
                    ball.bounceX();
                }
            }
            if (block.getVida() == 0) {
                iterb.remove();
            }
        }

        if (Intersector.overlaps(ball, bat)){
            dropSound.play();
            boolean side=false; //si la tabla ha sido tocada, pero no con la parte de arriba

            contRotos++;
            if (contRotos>4){
                ball.increaseSpeedX();
                ball.increaseSpeedY();
                contRotos=0;
            }

            if(Intersector.overlaps(ball, new Rectangle(bat.getX(), bat.getY(), 1, bat.getHeight()-5)) && ball.getSpeedx() > 0) {
                //si ha tocado con el borde izquierdo y se mueve hacia la derecha
                ball.bounceX();
                side=true;
            }
            if(Intersector.overlaps(ball, new Rectangle(bat.getX()+bat.getWidth()-1, bat.getY(), 1, bat.getHeight()-5)) && ball.getSpeedx() < 0) {
                //si ha tocado con el borde derecho y se mueve hacia la izquierda
                ball.bounceX();
                side=true;
            }

            if(!side){
                if(Intersector.overlaps(ball, new Rectangle(bat.getX(), bat.getY(), bat.getWidth()*0.2f, bat.getHeight()))){
                    System.out.println("1/3 TABLA");
                    ball.bounceY();
                    ball.addPositiveXSpeed(false);
                }else{
                    if(Intersector.overlaps(ball, new Rectangle(bat.getX() + bat.getWidth()*0.8f, bat.getY(), bat.getWidth()*0.2f, bat.getHeight()))){
                        System.out.println("3/3 TABLA");
                        ball.bounceY();
                        ball.addPositiveXSpeed(true);
                    }else{
                        ball.bounceY();
                        System.out.println("2/3 TABLA");
                    }
                }
            }

        }

        ball.move();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}