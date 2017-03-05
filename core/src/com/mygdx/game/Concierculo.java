package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class Concierculo implements Screen {
    final Drop game;

    ShapeRenderer shapeRenderer;

    Texture buttImage, poopImage, stainImage;
    Texture background;
    Texture play, stop, next, last;

    Sound fart1, fart2, fart3, fart4, fart5, fart6;

    Array<Rectangle> butts;
    Array<Rectangle> rainpoop, poopstain;
    Rectangle playButton, nextButton, lastButton;
    Array<String> musicName;
    Array<Music> musics;

    OrthographicCamera camera;

    int cameraWidth = 800, cameraHeight = 480;
    int cancion;
    int cacometro;
    long cacatime;
    String strCacometro, cancionStr;
    boolean isPlaying;
    long lastTouched, lastPoop;

    public Concierculo(final Drop gam) {
        this.game = gam;

        shapeRenderer = new ShapeRenderer();

        buttImage = new Texture(Gdx.files.internal("butt.png"));
        poopImage = new Texture(Gdx.files.internal("poop.png"));
        stainImage = new Texture(Gdx.files.internal("poopstain.png"));


        play = new Texture(Gdx.files.internal("play.png"));
        stop = new Texture(Gdx.files.internal("stop.png"));
        next = new Texture(Gdx.files.internal("next.png"));
        last = new Texture(Gdx.files.internal("last.png"));
        background = new Texture(Gdx.files.internal("background.jpg"));

        playButton = new Rectangle(cameraWidth - 70 - 130, cameraHeight - 64 - 30, 64, 64);
        nextButton = new Rectangle(cameraWidth/2 + 111 + 30 - 100, cameraHeight-64 - 30, 64, 64);
        lastButton = new Rectangle(cameraWidth/2 - 111 - 94 - 100, cameraHeight-64 - 30, 64, 64);

        musicName = new Array<String>();
        musics = new Array<Music>();
        cancion=0;
        iniciaMusica();
        cancionStr=musicName.get(0);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, cameraWidth, cameraHeight);


        butts = new Array<Rectangle>();
        rainpoop = new Array<Rectangle>();
        poopstain = new Array<Rectangle>();
        cacometro=0;
        cacatime=2140000000;
        strCacometro = "Cacometro: " + cacometro;

        rellenaCulos();
        isPlaying=false;

    }


    private void actualizaCacaTime(){
        cacatime=(2140000000-(cacometro*10000000)); //200 pedos para lograr la maxima lluvia de cacas
        if (cacometro > 200)
            cacometro = 200;

        strCacometro = "Cacometro: " + cacometro;

    }

    private void spawnPoop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, cameraWidth - 32);
        raindrop.y = cameraHeight;
        raindrop.width = 32;
        raindrop.height = 32;
        rainpoop.add(raindrop);
        lastPoop = TimeUtils.nanoTime();
    }

    public void iniciaMusica(){
        fart1 = Gdx.audio.newSound(Gdx.files.internal("songs/fart1.wav"));
        fart2 = Gdx.audio.newSound(Gdx.files.internal("songs/fart2.wav"));
        fart3 = Gdx.audio.newSound(Gdx.files.internal("songs/fart3.wav"));
        fart4 = Gdx.audio.newSound(Gdx.files.internal("songs/fart5.wav"));
        fart5 = Gdx.audio.newSound(Gdx.files.internal("songs/fart9.wav"));
        fart6 = Gdx.audio.newSound(Gdx.files.internal("songs/fart10.wav"));

        musicName.add("THUNDERSTRUCK");
        musicName.add("TNT");
        musicName.add("HIGHWAY");
        musicName.add("TRAIN");
        musicName.add("ROCKY");
        musicName.add("EYE OF TIGER");
        musicName.add("COUNTDOWN");
        musicName.add("PRESSURE");

        musics.add(Gdx.audio.newMusic(Gdx.files.internal("songs/thunderstruck.wav")));
        musics.add(Gdx.audio.newMusic(Gdx.files.internal("songs/tnt.mp3")));
        musics.add(Gdx.audio.newMusic(Gdx.files.internal("songs/highway.mp3")));
        musics.add(Gdx.audio.newMusic(Gdx.files.internal("songs/train.mp3")));
        musics.add(Gdx.audio.newMusic(Gdx.files.internal("songs/rocky.mp3")));
        musics.add(Gdx.audio.newMusic(Gdx.files.internal("songs/tiger.mp3")));
        musics.add(Gdx.audio.newMusic(Gdx.files.internal("songs/countdown.wav")));
        musics.add(Gdx.audio.newMusic(Gdx.files.internal("songs/pressure.wav")));
    }

    public void rellenaCulos(){
        butts.add(new Rectangle(90, 60, 128, 128));
        butts.add(new Rectangle(320, 60, 128, 128));
        butts.add(new Rectangle(550, 60, 128, 128));
        butts.add(new Rectangle(90, 220, 128, 128));
        butts.add(new Rectangle(320, 220, 128, 128));
        butts.add(new Rectangle(550, 220, 128, 128));
    }

    public void paraCancion(){
        if (musics.get(cancion).isLooping())
            musics.get(cancion).stop();
        isPlaying=false;

        poopstain = new Array<Rectangle>();
        cacometro=0;
    }

    public void cambiaCancion(){
        if (cancion>musicName.size-1)
            cancion=0;
        if (cancion<0)
            cancion=musicName.size-1;

        musics.get(cancion).setLooping(true);
        musics.get(cancion).play();

        cancionStr=musicName.get(cancion);

        isPlaying=true;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        //----------BEGIN BATCH-------------
        game.batch.begin();

        game.batch.draw(background, 0, 0, cameraWidth, cameraHeight);

        for (Rectangle stain : poopstain) {
            game.batch.draw(stainImage, stain.x, stain.y, stain.width, stain.height);
        }
        for (Rectangle raindrop : rainpoop) {
            game.batch.draw(poopImage, raindrop.x, raindrop.y, raindrop.width, raindrop.height);
        }

        //------------interfaz---------------
        game.font.getData().setScale(2);
        GlyphLayout layout = new GlyphLayout(game.font, cancionStr);
        game.font.draw(game.batch, cancionStr, cameraWidth/2 - layout.width/2 - 100, cameraHeight-layout.height - 30);
        game.batch.draw(next, nextButton.x, nextButton.y, nextButton.width, nextButton.height);
        game.batch.draw(last, lastButton.x, lastButton.y, lastButton.width, lastButton.height);
        if(isPlaying){
            game.batch.draw(stop, playButton.x, playButton.y, playButton.width, playButton.height);
        }else{
            game.batch.draw(play, playButton.x, playButton.y, playButton.width, playButton.height);
        }
        for(Rectangle butt: butts)
            game.batch.draw(buttImage, butt.x, butt.y, butt.width, butt.height);

        //------------cacometro---------------
        game.font.getData().setScale(1.3f);
        GlyphLayout layout2 = new GlyphLayout(game.font, strCacometro);
        game.font.draw(game.batch, strCacometro, cameraWidth/2 + layout2.width/2 + 140, layout2.height+30);
        //game.batch.draw(cacometroImage, cameraWidth-50, cameraHeight-475, 32, cacometro);

        game.batch.end();
        //-------------------END BATCH--------------------

        shapeRenderer.setProjectionMatrix(game.batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BROWN);
        shapeRenderer.rect(cameraWidth-50, 27, 16, cacometro);
        shapeRenderer.end();


        if (Gdx.input.isTouched()) {
            if (TimeUtils.nanoTime() - lastTouched >  180000000){
                lastTouched = TimeUtils.nanoTime();
                Vector3 touchPos3 = new Vector3();
                touchPos3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos3);

                if(playButton.contains(touchPos3.x, touchPos3.y)){
                    if (isPlaying)
                        paraCancion();
                    else
                        cambiaCancion();
                }
                if(nextButton.contains(touchPos3.x, touchPos3.y)){
                    paraCancion();
                    cancion++;
                    cambiaCancion();
                }
                if(lastButton.contains(touchPos3.x, touchPos3.y)){
                    paraCancion();
                    cancion--;
                    cambiaCancion();
                }

                //----------CULOS---------
                if(butts.get(0).contains(touchPos3.x, touchPos3.y)){
                    //System.out.println("CULO1");
                    fart1.play();
                    cacometro++;
                    actualizaCacaTime();
                }
                if(butts.get(1).contains(touchPos3.x, touchPos3.y)){
                    //System.out.println("CULO2");
                    fart2.play();
                    cacometro++;
                    actualizaCacaTime();
                }
                if(butts.get(2).contains(touchPos3.x, touchPos3.y)){
                    //System.out.println("CULO3");
                    fart3.play();
                    cacometro++;
                    actualizaCacaTime();
                }
                if(butts.get(3).contains(touchPos3.x, touchPos3.y)){
                    //System.out.println("CULO4");
                    fart4.play();
                    cacometro++;
                    actualizaCacaTime();
                }
                if(butts.get(4).contains(touchPos3.x, touchPos3.y)){
                    //System.out.println("CULO5");
                    fart5.play();
                    cacometro++;
                    actualizaCacaTime();
                }
                if(butts.get(5).contains(touchPos3.x, touchPos3.y)){
                    //System.out.println("CULO6");
                    fart6.play();
                    cacometro++;
                    actualizaCacaTime();
                }
            }
        }

        if (TimeUtils.nanoTime() - lastPoop > cacatime && isPlaying)
            spawnPoop();

        Iterator<Rectangle> iter = rainpoop.iterator();
        while (iter.hasNext()) {
            Rectangle rainpoop = iter.next();
            rainpoop.y -= 100 * Gdx.graphics.getDeltaTime();
            if (rainpoop.y + 64 < 0)
                iter.remove();
            else
                if (MathUtils.random(0, 150) == 1){
                    poopstain.add(rainpoop);
                    iter.remove();
                }
        }


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