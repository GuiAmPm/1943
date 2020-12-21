
import java.awt.geom.Rectangle2D;

import com.jogamp.opengl.GL2;

public class Tree extends Actor {

    public static int generated = -1;
    boolean first, shows = true;
    boolean setColor = true;

    public Tree(GL2 gl, Stage stage) {
        super(0f, 0f, 0f, stage, gl);
        first = true;
        generated = generate(gl);
    }

    float offset = 0;

    public void draw(GL2 gl) {
        if (!shows)
            return;
        stage.fixCamera(gl);

        gl.glTranslatef(x, y, z + offset);
        if (setColor) {
            gl.glColor3f(.3f, .7f, .1f);
        }
        gl.glCallList(generated);
        if (setColor) {
            gl.glColor3f(.5f, .1f, 0f);
        }
        gl.glCallList(generated + 1);
        if (setColor) {
            gl.glColor3f(1f, 1f, 1f);
        }
        setColor = true;
        stage.fixCamera(gl);
    }

    public void draw(boolean setColor, GL2 gl) {
        this.setColor = setColor;
        draw(gl);
    }

    public void draw(float off, float obsI, float obsF, GL2 gl) {
        this.offset = off;
        if (z + offset >= .6f) {
            x = (float) (Math.random() * 50 - 25);
            z = -(float) (Math.random() * 60 + (first ? 0 : 60 + offset));
            shows = true;
        }
        if (-z + 1 >= obsI + offset && -z + 1 <= obsF + offset + 1) {
            shows = false;
        }

        draw(gl);
        first = false;
    }

    protected int generate(GL2 gl) {
        if (generated != -1) {
            return generated;
        }

        int list = gl.glGenLists(2);

        gl.glNewList(list, GL2.GL_COMPILE);

        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex3f(0, .7f, 0);
        for (int i = 0; i < 7; i++) {
            gl.glNormal3d(.5 * Math.cos(Math.PI * 2 / 7 * -i), .5, .5 * Math.sin(Math.PI * 2 / 7 * -i));
            gl.glVertex3d(.15 * Math.cos(Math.PI * 2 / 7 * -i), .6f, .15 * Math.sin(Math.PI * 2 / 7 * -i));
        }
        gl.glNormal3d(.5, .5, 0);
        gl.glVertex3d(.15f, .6f, 0f);
        gl.glEnd();

        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (int i = 0; i < 7; i++) {
            gl.glNormal3d(Math.cos(Math.PI * 2 / 7 * -i), 1D, Math.sin(Math.PI * 2 / 7 * -i));
            gl.glVertex3d(.15 * Math.cos(Math.PI * 2 / 7 * -i), .6f, .15 * Math.sin(Math.PI * 2 / 7 * -i));
            gl.glVertex3d(.15 * Math.cos(Math.PI * 2 / 7 * -i), .4f, .15 * Math.sin(Math.PI * 2 / 7 * -i));
        }
        gl.glNormal3d(1D, 1D, 0D);
        gl.glVertex3d(.15f, .6f, 0f);
        gl.glVertex3d(.15f, .4f, 0f);
        gl.glEnd();

        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex3f(0, .3f, 0);
        for (int i = 0; i < 7; i++) {
            gl.glNormal3d(.5 * Math.cos(Math.PI * 2 / 7 * i), -.5, .5 * Math.sin(Math.PI * 2 / 7 * i));
            gl.glVertex3d(.15 * Math.cos(Math.PI * 2 / 7 * i), .4f, .15 * Math.sin(Math.PI * 2 / 7 * i));
        }
        gl.glNormal3d(.5, -.5, 0D);
        gl.glVertex3d(.15f, .4f, 0f);
        gl.glEnd();

        gl.glEndList();

        gl.glNewList(list + 1, GL2.GL_COMPILE);
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (int i = 0; i < 4; i++) {
            gl.glNormal3d(Math.cos(Math.PI * 2 / 4 * -i), 1D, Math.sin(Math.PI * 2 / 4 * -i));
            gl.glVertex3d(.05 * Math.cos(Math.PI * 2 / 4 * -i), .4f, .05 * Math.sin(Math.PI * 2 / 4 * -i));
            gl.glVertex3d(.05 * Math.cos(Math.PI * 2 / 4 * -i), .0f, .05 * Math.sin(Math.PI * 2 / 4 * -i));
        }
        gl.glNormal3d(1D, 1D, 0D);
        gl.glVertex3d(.05f, .4f, 0f);
        gl.glVertex3d(.05f, .0f, 0f);
        gl.glEnd();
        gl.glEndList();

        return list;
    }

    public void forceRebuild(GL2 gl) {
        gl.glDeleteLists(generated, 1);
        generated = -1;
        generated = generate(gl);
    }

    public Rectangle2D getTopViewBounds() {
        return null;
    }
}
