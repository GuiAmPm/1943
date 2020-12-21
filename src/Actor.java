
import java.awt.geom.Rectangle2D;
import com.jogamp.opengl.GL2;

public abstract class Actor {

    float x, y, z;
    Stage stage;

    public Actor(float x, float y, float z, Stage stage, GL2 gl) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.stage = stage;
    }

    public abstract void draw(GL2 gl);

    protected abstract int generate(GL2 gl);

    public abstract void forceRebuild(GL2 gl);

    public abstract Rectangle2D getTopViewBounds();

    public String toString() {
        return "x: " + x + " y: " + y + "z: " + z;
    }

    public boolean collides(Actor b) {
        return b.getTopViewBounds().intersects(getTopViewBounds());
    }

}
