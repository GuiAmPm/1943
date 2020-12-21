
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class GameOver implements Stage {

    boolean isLoaded = false;

    public void paint(GL2 gl) {
        if (!isLoaded) {
            return;
        }

        GLUT glut = new GLUT();

        fixCamera(gl);

        gl.glRasterPos2f(0f, -1.3f);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Press any key to go back...");

        gl.glRasterPos2f(0f, 0f);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "You are a great war hero, Red Baron!");
        if (Globals.playerCount == 2) {
            gl.glRasterPos2f(0f, -.1f);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Although you, Green Baron, were only an extra...");
            gl.glRasterPos2f(0f, -.2f);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Red Baron got all your money:");
            gl.glRasterPos2f(0f, -.3f);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "$ " + Globals.score + ",00");
        } else {
            gl.glRasterPos2f(0f, -.1f);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Mr. Baron, you account has:");
            gl.glRasterPos2f(0f, -.2f);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "$ " + Globals.score + ",00");
        }
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -1.8f);
        gl.glRasterPos2f(0f, 0f);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "- Congratulations,");
        gl.glRasterPos2f(0f, -.1f);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Cpt. Guilherme Amorim");
        gl.glRasterPos2f(0f, -.2f);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "04h55 - 27/07/2010");
    }

    public void logicLoop(Entry frame) {
        if (!isLoaded) {
            return;
        }
        if (pressed)
            frame.startStage(0);
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void load(GL2 gl) {
        if (isLoaded) {
            return;
        }
        gl.glClearColor(0f, 0f, 0f, 1f);
        pressed = false;
        isLoaded = true;
    }

    public void unload(GL2 gl) {
        if (!isLoaded) {
            return;
        }
    }

    public void keyPressed(int key) {
    }

    public void keyReleased(int key) {
        pressed = true;
    }

    boolean pressed;

    public void fixCamera(GL2 gl) {
        gl.glLoadIdentity();
        gl.glTranslatef(-1, .6f, -1.8f);
    }
}
