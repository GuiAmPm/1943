import java.awt.geom.Rectangle2D;

import com.jogamp.opengl.GL2;

public class Plane extends Actor {

    protected int cont = 0;
    public static int generated = -1;

    public Plane(float x, float z, Stage stage, GL2 gl) {
        super(x, 1.5f, z, stage, gl);
        generated = generate(gl);
    }

    public void draw(GL2 gl) {
        stage.fixCamera(gl);

        gl.glTranslatef(x, y, z);

        gl.glCallList(generated);

        cont += 100;
        if (cont == 360) {
            cont = 1;
        }

        gl.glRotatef(-cont, 0f, 0f, 1f);
        gl.glCallList(generated + 1);

        stage.fixCamera(gl);
    }

    protected int generate(GL2 gl) {
        if (generated != -1) {
            return generated;
        }

        int list = gl.glGenLists(2);

        gl.glNewList(list, GL2.GL_COMPILE);

        int detail = 4 * Globals.detailLevel;

        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glNormal3d(0D, 0D, -1D);
        gl.glVertex3d(0, 0, -.7f);
        for (int i = 0; i < detail; i++) {
            gl.glNormal3d(.4 * Math.cos(Math.PI * 2 / detail * -i), .4 * Math.sin(Math.PI * 2 / detail * -i), -.6D);
            gl.glVertex3d(.3 * Math.cos(Math.PI * 2 / detail * -i), .3 * Math.sin(Math.PI * 2 / detail * -i), -.5D);
        }
        gl.glNormal3d(.4, 0D, -.6D);
        gl.glVertex3f(.3f, 0f, -.5f);
        gl.glEnd();

        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (int i = 0; i < detail; i++) {
            gl.glNormal3d(Math.cos(Math.PI * 2 / detail * -i), Math.sin(Math.PI * 2 / detail * -i), 0);
            gl.glVertex3d(.3 * Math.cos(Math.PI * 2 / detail * -i), .3 * Math.sin(Math.PI * 2 / detail * -i), -.5D);
            gl.glVertex3d(.5 * Math.cos(Math.PI * 2 / detail * -i), .5 * Math.sin(Math.PI * 2 / detail * -i), 0D);
        }
        gl.glVertex3f(.3f, 0f, -.5f);
        gl.glVertex3f(.5f, 0f, 0f);
        gl.glEnd();

        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (int i = 0; i < detail; i++) {
            gl.glNormal3d(Math.cos(Math.PI * 2 / detail * -i), Math.sin(Math.PI * 2 / detail * -i), 0);
            gl.glVertex3d(.5 * Math.cos(Math.PI * 2 / detail * -i), .5 * Math.sin(Math.PI * 2 / detail * -i), 0D);
            gl.glVertex3d(.5 * Math.cos(Math.PI * 2 / detail * -i), .3 * Math.sin(Math.PI * 2 / detail * -i), 2D);
        }
        gl.glVertex3f(.5f, 0f, 0f);
        gl.glVertex3f(.5f, 0f, 2f);
        gl.glEnd();

        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (int i = 0; i < detail; i++) {
            gl.glNormal3d(Math.cos(Math.PI * 2 / detail * -i), Math.sin(Math.PI * 2 / detail * -i) * .7,
                    Math.sin(Math.PI * 2 / detail * -i) * .3);
            gl.glVertex3d(.5 * Math.cos(Math.PI * 2 / detail * -i), .3 * Math.sin(Math.PI * 2 / detail * -i), 2D);
            gl.glVertex3d(.2 * Math.cos(Math.PI * 2 / detail * -i), 1D, 2.5);
        }
        gl.glVertex3d(.5, 0D, 2D);
        gl.glVertex3d(0D, 1D, 2.5);
        gl.glEnd();

        gl.glBegin(GL2.GL_QUAD_STRIP);
        gl.glNormal3d(0, 1, 0);
        gl.glVertex3f(-2f, .1f, 0f);
        gl.glVertex3f(-2f, .1f, 1f);
        gl.glVertex3f(2f, .1f, 0f);
        gl.glVertex3f(2f, .1f, 1f);
        gl.glNormal3d(1, 0, 0);
        gl.glVertex3f(2f, -.1f, 0f);
        gl.glVertex3f(2f, -.1f, 1f);
        gl.glNormal3d(0, -1, 0);
        gl.glVertex3f(-2f, -.1f, 0f);
        gl.glVertex3f(-2f, -.1f, 1f);
        gl.glNormal3d(-1, 0, 0);
        gl.glVertex3f(-2f, .1f, 0f);
        gl.glVertex3f(-2f, .1f, 1f);
        gl.glEnd();

        gl.glBegin(GL2.GL_QUAD_STRIP);
        gl.glNormal3d(0, 1, 0);
        gl.glVertex3f(-2f, 1.1f, 0f);
        gl.glVertex3f(-2f, 1.1f, 1f);
        gl.glVertex3f(2f, 1.1f, 0f);
        gl.glVertex3f(2f, 1.1f, 1f);
        gl.glNormal3d(1, 0, 0);
        gl.glVertex3f(2f, 0.9f, 0f);
        gl.glVertex3f(2f, 0.9f, 1f);
        gl.glNormal3d(0, -1, 0);
        gl.glVertex3f(-2f, 0.9f, 0f);
        gl.glVertex3f(-2f, 0.9f, 1f);
        gl.glNormal3d(-1, 0, 0);
        gl.glVertex3f(-2f, 1.1f, 0f);
        gl.glVertex3f(-2f, 1.1f, 1f);
        gl.glEnd();

        gl.glBegin(GL2.GL_QUAD_STRIP);
        gl.glNormal3d(0, 1, 0);
        gl.glVertex3f(-1f, 0.2f, 1.5f);
        gl.glVertex3f(-1f, 0.2f, 2f);
        gl.glVertex3f(1f, 0.2f, 1.5f);
        gl.glVertex3f(1f, 0.2f, 2f);
        gl.glNormal3d(1, 0, 0);
        gl.glVertex3f(1f, 0f, 1.5f);
        gl.glVertex3f(1f, 0f, 2f);
        gl.glNormal3d(0, -1, 0);
        gl.glVertex3f(-1f, 0f, 1.5f);
        gl.glVertex3f(-1f, 0f, 2f);
        gl.glNormal3d(-1, 0, 0);
        gl.glVertex3f(-1f, 0.2f, 1.5f);
        gl.glVertex3f(-1f, 0.2f, 2f);
        gl.glEnd();

        gl.glBegin(GL2.GL_QUADS);

        gl.glNormal3d(0, 0, 1);
        gl.glVertex3f(2f, -.1f, 1f);
        gl.glVertex3f(2f, .1f, 1f);
        gl.glVertex3f(-2f, .1f, 1f);
        gl.glVertex3f(-2f, -.1f, 1f);

        gl.glNormal3d(0, 0, -1);
        gl.glVertex3f(-2f, -.1f, 0f);
        gl.glVertex3f(-2f, 0.1f, 0f);
        gl.glVertex3f(2f, 0.1f, 0f);
        gl.glVertex3f(2f, -.1f, 0f);

        gl.glNormal3d(0, 0, 1);
        gl.glVertex3f(2f, 0.9f, 1f);
        gl.glVertex3f(2f, 1.1f, 1f);
        gl.glVertex3f(-2f, 1.1f, 1f);
        gl.glVertex3f(-2f, 0.9f, 1f);

        gl.glNormal3d(0, 0, -1);
        gl.glVertex3f(-2f, 0.9f, 0f);
        gl.glVertex3f(-2f, 1.1f, 0f);
        gl.glVertex3f(2f, 1.1f, 0f);
        gl.glVertex3f(2f, 0.9f, 0f);

        gl.glNormal3d(0, 0, -1);
        gl.glVertex3f(1.4f, 0f, .5f);
        gl.glVertex3f(1.4f, 1f, .5f);
        gl.glVertex3f(1.6f, 1f, .5f);
        gl.glVertex3f(1.6f, 0f, .5f);

        gl.glNormal3d(0, 0, 1);
        gl.glVertex3f(1.6f, 0f, .5f);
        gl.glVertex3f(1.6f, 1f, .5f);
        gl.glVertex3f(1.4f, 1f, .5f);
        gl.glVertex3f(1.4f, 0f, .5f);

        gl.glNormal3d(0, 0, -1);
        gl.glVertex3f(-1.6f, 0f, .5f);
        gl.glVertex3f(-1.6f, 1f, .5f);
        gl.glVertex3f(-1.4f, 1f, .5f);
        gl.glVertex3f(-1.4f, 0f, .5f);

        gl.glNormal3d(0, 0, 1);
        gl.glVertex3f(-1.4f, 0f, .5f);
        gl.glVertex3f(-1.4f, 1f, .5f);
        gl.glVertex3f(-1.6f, 1f, .5f);
        gl.glVertex3f(-1.6f, 0f, .5f);

        gl.glNormal3d(0, 0, 1);
        gl.glVertex3f(1f, 0f, 2f);
        gl.glVertex3f(1f, .2f, 2f);
        gl.glVertex3f(-1f, .2f, 2f);
        gl.glVertex3f(-1f, 0f, 2f);

        gl.glNormal3d(0, 0, -1);
        gl.glVertex3f(1f, .2f, 1.5f);
        gl.glVertex3f(1f, 0f, 1.5f);
        gl.glVertex3f(-1f, 0f, 1.5f);
        gl.glVertex3f(-1f, .2f, 1.5f);

        gl.glEnd();

        gl.glEndList();

        int propeller = list + 1;

        gl.glNewList(propeller, GL2.GL_COMPILE);

        gl.glBegin(GL2.GL_QUADS);

        gl.glNormal3d(0, 0, 1);
        gl.glVertex3d(.5f, -.1f, -.6f);
        gl.glVertex3d(.5f, .1f, -.6f);
        gl.glVertex3d(-.5f, .1f, -.6f);
        gl.glVertex3d(-.5f, -.1f, -.6f);

        gl.glNormal3d(0, 0, -1);
        gl.glVertex3f(.5f, .1f, -.6f);
        gl.glVertex3f(.5f, -.1f, -.6f);
        gl.glVertex3f(-.5f, -.1f, -.6f);
        gl.glVertex3f(-.5f, .1f, -.6f);

        gl.glEnd();

        gl.glEndList();

        return list;
    }

    public void forceRebuild(GL2 gl) {
        gl.glDeleteLists(generated, 2);
        generated = -1;
        generated = generate(gl);
    }

    public Rectangle2D getTopViewBounds() {
        return new Rectangle2D.Double(-2d + x, -.7 + z, 4d, 3.2);
    }
}
