
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.FloatBuffer;
import java.util.Vector;

public class Level implements Stage {

    public float[] cameraPos = { 0f, 4.2f, 10f };
    public float[] cameraDir = { -25f, 0f, 0f };
    Texture textures[];
    float offset;
    int waterRelativePosition;
    int terraPosRel;
    int combo;
    int kill;
    int cont;
    boolean blinking;
    boolean access;
    float speed;
    Tree[] trees;
    Plane player1, player2;
    int lives;
    Vector<Bullet> bullets;
    Vector<Bullet> enemyBullets;
    Vector<Sign> signs;
    Vector<Enemy> enemies;
    Sign levelUp;
    float levelUpX;
    Sign livesSign;
    float ambient[] = { 1f, 1f, 1f, 1f };
    float diffuse[] = { 1f, 1f, 1f, 1f };
    float lPosition[] = { 0f, 0f, 2f, 1f };

    public void paint(GL2 gl) {
        gl.glColor3f(0f, 0f, 0f);
        if (!isLoaded) {
            gl.glLoadIdentity();
            gl.glTranslatef(0, 0, -1f);
            GLUT glut = new GLUT();
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Loaded...");
            return;
        }

        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -1f);
        gl.glRasterPos2f(-.05f, .37f);
        GLUT glut = new GLUT();
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "$ " + Globals.score + ",00");
        gl.glLoadIdentity();

        gl.glColor3f(1f, 1f, 1f);

        if (Globals.wireFrame) {
            gl.glDisable(GL2.GL_CULL_FACE);
        }

        float offset = this.offset;
        int waterPosition = waterRelativePosition - (int) offset;
        int terraPos = terraPosRel - (int) offset;

        if (Globals.illumination) {
            gl.glEnable(GL2.GL_LIGHTING);
        } else {
            gl.glDisable(GL2.GL_LIGHTING);
        }
        if (Globals.textures) {
            gl.glEnable(GL2.GL_TEXTURE_2D);
        } else {
            gl.glDisable(GL2.GL_TEXTURE_2D);
        }

        fixCamera(gl);

        int ultimaTex = -1;

        for (int y = 0; y < 60; y++) {
            gl.glColor3f(1f, 1f, 1f);
            if (y >= waterPosition && y <= terraPos) {
                if ((y == waterPosition || y == terraPos) && ultimaTex != 2) {
                    if (ultimaTex != -1) {
                        textures[ultimaTex].disable(gl);
                    }
                    if (Globals.textures) {
                        ultimaTex = 2;
                        textures[ultimaTex].enable(gl);
                        textures[ultimaTex].bind(gl);
                    } else {
                        gl.glColor3f(.8f, .8f, .01f);
                    }
                } else if (ultimaTex != 1) {
                    if (ultimaTex != -1) {
                        textures[ultimaTex].disable(gl);
                    }
                    if (Globals.textures) {
                        ultimaTex = 1;
                        textures[ultimaTex].enable(gl);
                        textures[ultimaTex].bind(gl);
                    } else {
                        gl.glColor3f(.01f, .2f, .8f);
                    }
                }
            } else if (ultimaTex != 0) {
                if (ultimaTex != -1) {
                    textures[ultimaTex].disable(gl);
                }
                if (Globals.textures) {
                    ultimaTex = 0;
                    textures[ultimaTex].enable(gl);
                    textures[ultimaTex].bind(gl);
                } else {
                    gl.glColor3f(.2f, .4f, .01f);
                }
            }
            gl.glBegin(GL2.GL_QUADS);
            gl.glNormal3f(0f, 1f, 0f);
            for (int x = -30; x < 30; x++) {
                float low = 0f, high = 0f;
                if (y >= waterPosition && y <= terraPos) {
                    if (y == waterPosition) {
                        high = -.3f;
                    } else if (y == terraPos) {
                        low = -.3f;
                    } else {
                        high = -.3f;
                        low = -.3f;
                    }
                }
                if (y == terraPos) {
                    gl.glTexCoord2f(0f, 1f);
                    gl.glVertex3f(x - 1f, low, offset % 1 - y + 1f);
                    gl.glTexCoord2f(1f, 1f);
                    gl.glVertex3f(x + 1f, low, offset % 1 - y + 1f);
                    gl.glTexCoord2f(1f, 0f);
                    gl.glVertex3f(x + 1f, high, offset % 1 - y);
                    gl.glTexCoord2f(0f, 0f);
                    gl.glVertex3f(x - 1f, high, offset % 1 - y);
                } else {
                    gl.glTexCoord2f(0f, 0f);
                    gl.glVertex3f(x - 1f, low, offset % 1 - y + 1f);
                    gl.glTexCoord2f(1f, 0f);
                    gl.glVertex3f(x + 1f, low, offset % 1 - y + 1f);
                    gl.glTexCoord2f(1f, 1f);
                    gl.glVertex3f(x + 1f, high, offset % 1 - y);
                    gl.glTexCoord2f(0f, 1f);
                    gl.glVertex3f(x - 1f, high, offset % 1 - y);
                }
            }
            gl.glEnd();
        }

        if (ultimaTex != -1) {
            textures[ultimaTex].disable(gl);
        }

        for (int i = 0; i < trees.length; i++) {
            trees[i].draw(offset, waterPosition, terraPos, gl);
        }

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(gl);
        }

        for (int i = 0; i < enemyBullets.size(); i++) {
            enemyBullets.get(i).draw(gl);
        }

        gl.glColor4f(.4f, 0f, 0f, 1f);
        if ((blinking && access) || !blinking) {
            player1.draw(gl);
            if (Globals.playerCount == 2) {
                gl.glColor4f(0f, .4f, 0f, 1f);
                player2.draw(gl);
            }
        }
        if (blinking) {
            access = !access;
        }

        gl.glColor4f(0f, 0f, .4f, 1f);
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(gl);
        }

        if (Globals.celShaded) {
            gl.glDepthFunc(GL2.GL_LEQUAL);
            gl.glCullFace(GL2.GL_FRONT);

            gl.glColor3f(0f, 0f, 0f);

            gl.glPolygonMode(GL2.GL_BACK, GL2.GL_LINE);
            gl.glLineWidth(1.5f);

            for (int i = 0; i < trees.length; i++) {
                trees[i].draw(false, gl);
            }
            player1.draw(gl);
            if (Globals.playerCount == 2)
                player2.draw(gl);
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).draw(gl);
            }

            gl.glPolygonMode(GL2.GL_BACK, GL2.GL_FILL);

            gl.glDepthFunc(GL2.GL_LESS);
            gl.glCullFace(GL2.GL_BACK);
        }

        if (Globals.wireFrame) {
            gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
        }
        for (int i = 0; i < signs.size(); i++) {
            signs.get(i).draw(gl);
        }
        if (levelUpX > -.5f) {
            levelUp.x = levelUpX - .05f;
            levelUp.draw(gl);
            if (levelUpX > -.03 && levelUpX < .03) {
                levelUpX -= .001;
            } else {
                levelUpX -= .01;
            }
        }

        for (int i = 0; i < lives; i++) {
            livesSign.x = -.55f + i * .11f;
            livesSign.y = .4f;
            livesSign.z = -1f;
            livesSign.draw(gl);
        }
        if (Globals.wireFrame) {
            gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_LINE);
        }
    }

    public void fixCamera(GL2 gl) {
        gl.glLoadIdentity();
        gl.glTranslatef(-cameraPos[0], -cameraPos[1], -cameraPos[2]);
        gl.glRotatef(-cameraDir[0], 1f, 0f, 0f);
        gl.glRotatef(-cameraDir[1], 0f, 1f, 0f);
        gl.glRotatef(-cameraDir[2], 0f, 0f, 1f);
    }

    boolean first = true;

    public void logicLoop(Entry frame) {
        if (!isLoaded) {
            return;
        }

        if (blinking) {
            cont++;
        }
        if (cont > 100) {
            blinking = false;
            cont = 0;
        }

        outer: for (int i = 0; i < enemies.size(); i++) {
            for (int j = 0; j < bullets.size(); j++) {
                if (bullets.get(j).collides(enemies.get(i))) {
                    bullets.remove(j);
                    enemies.remove(i);
                    Globals.score += 100 + 10 * (Globals.level - 1) + (-player1.z / 10) + 10 * combo;
                    kill++;
                    combo++;
                    break outer;
                }
            }
            if ((!blinking) && enemies.get(i).collides(player1)) {
                lives--;
                blinking = true;
                player1.x = 0;
                player1.z = -10;
                enemies.remove(i);
            }
            if ((!blinking) && Globals.playerCount == 2 && enemies.get(i).collides(player2)) {
                lives--;
                blinking = true;
                player2.x = 0;
                player2.z = -10;
                enemies.remove(i);
            }
        }

        if (!blinking) {
            for (int i = 0; i < enemyBullets.size(); i++) {
                if (enemyBullets.get(i).collides(player1)) {
                    lives--;
                    blinking = true;
                    player1.x = 0;
                    player1.z = -10;
                    enemyBullets.remove(i);
                }
                if (Globals.playerCount == 2 && enemyBullets.get(i).collides(player2)) {
                    lives--;
                    blinking = true;
                    player2.x = 0;
                    player2.z = -10;
                    enemyBullets.remove(i);
                }
            }
        }

        if (lives == -1) {
            frame.startStage(2);
            isLoaded = false;
            return;
        }

        if (kill / ((Globals.level) + 2 * (Globals.level - 1)) >= Globals.level) {
            Globals.level++;
            levelUpX = .5f;
        }
        speed = (float) (.025 + (Globals.level - 1) / 25d);
        offset += speed;

        if (terraPosRel - offset < 0) {
            waterRelativePosition = (int) (Math.random() * 500 + 60 + offset);
            terraPosRel = (int) (Math.random() * 200 + waterRelativePosition + 2);
        }
        refreshPlayer();
        for (int i = 0; i < bullets.size(); i++) {
            Bullet tiro = bullets.get(i);
            tiro.move();
            if (tiro.z < -50) {
                bullets.remove(tiro);
                combo = 1;
            }
        }
        for (int i = 0; i < enemyBullets.size(); i++) {
            Bullet tiro = enemyBullets.get(i);
            tiro.move();
            if (tiro.z > 5) {
                enemyBullets.remove(tiro);
                combo = 1;
            }

        }
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.move();
            if (enemy.z > 5) {
                enemies.remove(enemy);
                combo = 1;
            }
        }

        double joker = Math.random();
        double joker2 = Math.random();

        if (joker < .005 + (Globals.level - 1) * 0.0005) {
            enemies.add(new Enemy((float) (Math.random() * 20 - 10), -60, this, null));
        }
        if (enemies.size() > 0 && joker2 < .00001 + (Globals.level - 1) * 0.00001) {
            Enemy ini = enemies.get((int) (enemies.size() * Math.random()));
            enemyBullets.add(new Bullet(ini.x, ini.z, this, null, 180, .5f));
        }
    }

    public synchronized void load(GL2 gl) {
        if (isLoaded) {
            return;
        }

        speed = (float) (.025 + (1d * (Globals.level - 1) / 100d));
        levelUpX = -.5f;
        lives = 3 * Globals.playerCount;
        cont = 0;
        kill = 0;
        combo = 1;
        terraPosRel = 60;
        waterRelativePosition = 0;
        offset = 0f;
        access = false;
        blinking = true;

        if (Globals.wireFrame && Globals.celShaded) {
            Globals.celShaded = false;
        }
        if (Globals.wireFrame) {
            gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_LINE);
            gl.glPolygonMode(GL2.GL_BACK, GL2.GL_LINE);
        }

        gl.glClearColor(.8f, .8f, 1f, 1f);

        gl.glFogi(GL2.GL_FOG_MODE, GL2.GL_LINEAR);
        FloatBuffer fb = FloatBuffer.allocate(4);
        fb.put(new float[] { .8f, .8f, 1f, 1f });
        fb.rewind();
        gl.glFogfv(GL2.GL_FOG_COLOR, fb);
        gl.glFogf(GL2.GL_FOG_DENSITY, .35f);
        gl.glHint(GL2.GL_FOG_HINT, GL2.GL_DONT_CARE);
        gl.glFogf(GL2.GL_FOG_START, 40f);
        gl.glFogf(GL2.GL_FOG_END, 50f);
        gl.glEnable(GL2.GL_FOG);

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lPosition, 0);

        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_LIGHTING);

        gl.glEnable(GL2.GL_NORMALIZE);

        gl.glEnable(GL2.GL_COLOR_MATERIAL);

        textures = new Texture[3];
        try {
            textures[0] = TextureIO.newTexture(new File("tex" + File.separator + "g00.jpg"), true);
            textures[1] = TextureIO.newTexture(new File("tex" + File.separator + "w00.jpg"), true);
            textures[2] = TextureIO.newTexture(new File("tex" + File.separator + "s00.jpg"), true);
        } catch (Exception e) {
        }

        trees = new Tree[Globals.treeCount];

        for (int i = 0; i < trees.length; i++) {
            trees[i] = new Tree(gl, this);
        }

        player1 = new Plane(0f, -8f, this, gl);
        if (Globals.playerCount == 2) {
            player2 = new Plane(0f, -8f, this, gl);
        }

        bullets = new Vector<Bullet>();
        enemyBullets = new Vector<Bullet>();
        signs = new Vector<Sign>();
        enemies = new Vector<Enemy>();

        new Bullet(0, 0, this, gl, 0, 0);
        new Enemy(0, 0, this, gl);

        signs.add(
                new Sign(-.5f, 2.5f, 1.5f, 1f, 1f, "tex" + File.separator + "1p_trans.png", false, this, gl, player1));
        livesSign = new Sign(0, 0, 0, .1f, .1f, "tex" + File.separator + "plane.png", true, this, gl);
        levelUp = new Sign(levelUpX, .05f, -.5f, .1f, .1f, "tex" + File.separator + "levelUp.png", true, this, gl);
        isLoaded = true;
    }

    public synchronized void unload(GL2 gl) {
        if (!isLoaded) {
            return;
        }

        gl.glDisable(GL2.GL_FOG);
        gl.glDisable(GL2.GL_LIGHT0);
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glClearColor(0f, 0f, 0f, 1f);

        isLoaded = false;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    private boolean isLoaded = false;

    public void keyPressed(int key) {
        switch (key) {
            case KeyEvent.VK_W:
                cUp = true;
                break;
            case KeyEvent.VK_S:
                cDown = true;
                break;
            case KeyEvent.VK_A:
                cLeft = true;
                break;
            case KeyEvent.VK_D:
                cRight = true;
                break;
            case KeyEvent.VK_SPACE:
                cTiro = true;
                break;

            case KeyEvent.VK_UP:
                cUp2 = true;
                break;
            case KeyEvent.VK_DOWN:
                cDown2 = true;
                break;
            case KeyEvent.VK_LEFT:
                cLeft2 = true;
                break;
            case KeyEvent.VK_RIGHT:
                cRight2 = true;
                break;
            case KeyEvent.VK_NUMPAD0:
                cTiro2 = true;
                break;
        }

    }

    public void keyReleased(int key) {
        switch (key) {
            case KeyEvent.VK_W:
                cUp = false;
                break;
            case KeyEvent.VK_S:
                cDown = false;
                break;
            case KeyEvent.VK_A:
                cLeft = false;
                break;
            case KeyEvent.VK_D:
                cRight = false;
                break;
            case KeyEvent.VK_SPACE:
                // cTiro = false;
                lock = false;
                break;

            case KeyEvent.VK_UP:
                cUp2 = false;
                break;
            case KeyEvent.VK_DOWN:
                cDown2 = false;
                break;
            case KeyEvent.VK_LEFT:
                cLeft2 = false;
                break;
            case KeyEvent.VK_RIGHT:
                cRight2 = false;
                break;
            case KeyEvent.VK_NUMPAD0:
                // cTiro2 = false;
                lock2 = false;
                break;
        }

    }

    boolean cTiro = false;
    boolean cUp, cDown, cLeft, cRight;
    boolean lock = false;
    boolean cTiro2 = false;
    boolean cUp2, cDown2, cLeft2, cRight2;
    boolean lock2 = false;

    private void refreshPlayer() {
        if (cUp && player1.z > -30) {
            player1.z -= .1 + .02 * (Globals.level - 1);
        }
        if (cDown && player1.z < -8) {
            player1.z += .1 + .02 * (Globals.level - 1);
        }
        if (cLeft && player1.x > -10) {
            player1.x -= .1 + .02 * (Globals.level - 1);
        }
        if (cRight && player1.x < 10) {
            player1.x += .1 + .02 * (Globals.level - 1);
        }
        if (cTiro && !lock) {
            bullets.add(new Bullet(player1.x, player1.z, this, null, 0, .5f));
            cTiro = false;
            lock = true;
        }
        if (Globals.playerCount == 2) {
            if (cUp2 && player2.z > -30) {
                player2.z -= .1 + .02 * (Globals.level - 1);
            }
            if (cDown2 && player2.z < -8) {
                player2.z += .1 + .02 * (Globals.level - 1);
            }
            if (cLeft2 && player2.x > -10) {
                player2.x -= .1 + .02 * (Globals.level - 1);
            }
            if (cRight2 && player2.x < 10) {
                player2.x += .1 + .02 * (Globals.level - 1);
            }
            if (cTiro2 && !lock2) {
                bullets.add(new Bullet(player2.x, player2.z, this, null, 0, .5f));
                cTiro2 = false;
                lock2 = true;
            }
        }
    }
}
