
import java.awt.geom.Rectangle2D;

import com.jogamp.opengl.GL2;

public class Enemy extends Plane {

    public float angleY;
    public boolean alive = true;
    boolean strange = false;
    float offset;

    public Enemy(float x, float z, Stage stage, GL2 gl) {
        super(x, z, stage, gl);
        if (Math.random() < .5) {
            strange = true;
            offset = (float) (Math.random() * 360);
        }
    }

    public void draw(GL2 gl) {
        stage.fixCamera(gl);
        gl.glTranslatef(x, y, z);
        gl.glRotatef(180, 0, 1f, 0);

        gl.glCallList(generated);

        cont += 100;
        if (cont == 360)
            cont = 1;

        gl.glRotatef(-cont, 0f, 0f, 1f);
        gl.glCallList(generated + 1);

        stage.fixCamera(gl);
    }

    public void move() {
        z += (.025 + (1d * (Globals.level - 1) / 10d));
        if (strange)
            x = (float) (Math.sin(Math.toRadians(z * Globals.level + offset)) * 10);
    }

    public Rectangle2D getTopViewBounds() {
        return new Rectangle2D.Double(-2d + x, -2.5 + z, 4d, 3.2);
    }

}
