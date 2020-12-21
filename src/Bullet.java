
import java.awt.geom.Rectangle2D;
import com.jogamp.opengl.GL2;

public class Bullet extends Actor {

    float angle, speed;

    public static int generated = -1;

    public Bullet(float x, float z, Stage stage, GL2 gl, float angle, float speed) {
        super(x, 1.5f, z, stage, gl);
        this.angle = angle;
        this.speed = speed;

        generated = generate(gl);
    }

    public void draw(GL2 gl) {
        stage.fixCamera(gl);

        gl.glTranslatef(x, y, z);

        gl.glRotatef(-angle, 0, 1f, 0);
        gl.glCallList(generated);

        stage.fixCamera(gl);
    }

    protected int generate(GL2 gl) {
        if (generated != -1 || gl == null)
            return generated;
        int list = gl.glGenLists(1);

        gl.glNewList(list, GL2.GL_COMPILE);

        gl.glColor3f(.7f, .5f, 0f);

        gl.glBegin(GL2.GL_QUADS);

        gl.glVertex3f(.3f, 0, 0);
        gl.glVertex3f(-.3f, 0, 0);

        gl.glColor3f(1f, 1f, 0f);
        gl.glVertex3f(-.3f, 0f, 3f);
        gl.glVertex3f(.3f, 0f, 3f);

        gl.glEnd();

        gl.glColor3f(1f, 1f, 1f);
        gl.glEndList();

        generated = list;

        return list;
    }

    public void forceRebuild(GL2 gl) {
        gl.glDeleteLists(generated, 1);
        generated = -1;
        generated = generate(gl);
    }

    public void move() {
        x += (float) (speed * Math.cos(Math.toRadians(angle - 90)));
        z += (float) (speed * Math.sin(Math.toRadians(angle - 90)));
    }

    public Rectangle2D getTopViewBounds() {
        return new Rectangle2D.Double(-.3 + x, z, .6, 3d);
    }

}
