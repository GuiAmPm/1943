
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.awt.geom.Rectangle2D;
import java.io.File;

public class Sign extends Actor {

    float width = 0f;
    float height = 0f;
    boolean ortho;
    Texture texture;
    Actor parent;
    int generated = -1;

    public Sign(float x, float y, float z, float width, float height, String textureFile, boolean ortho, Stage stage,
            GL2 gl) {
        super(x, y, z, stage, gl);
        this.width = width;
        this.height = -height;
        this.ortho = ortho;

        try {
            texture = TextureIO.newTexture(new File(textureFile), true);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        generated = generate(gl);
    }

    public Sign(float x, float y, float z, float width, float height, String textureFile, boolean ortho, Stage stage,
            GL2 gl, Actor parent) {
        this(x, y, z, width, height, textureFile, ortho, stage, gl);
        this.parent = parent;
    }

    public void draw(GL2 gl) {
        if (ortho) {
            gl.glLoadIdentity();
        } else {
            stage.fixCamera(gl);
        }

        if (parent != null) {
            gl.glTranslatef(parent.x, parent.y, parent.z);
        }
        gl.glTranslatef(x, y, z);

        gl.glCallList(generated);
        stage.fixCamera(gl);

    }

    protected int generate(GL2 gl) {
        if (generated != -1) {
            return generated;
        }

        int list = gl.glGenLists(1);

        gl.glNewList(list, GL2.GL_COMPILE);

        gl.glColor3f(1f, 1f, 1f);

        gl.glEnable(GL2.GL_BLEND);
        gl.glDisable(GL2.GL_DEPTH_TEST);
        if (!Globals.textures)
            gl.glEnable(GL2.GL_TEXTURE_2D);

        texture.bind(gl);
        texture.enable(gl);
        gl.glBegin(GL2.GL_QUADS);

        gl.glTexCoord2f(1f, 1f);
        gl.glVertex2f(width, 0);
        gl.glTexCoord2f(0f, 1f);
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(0f, 0f);
        gl.glVertex2f(0, height);
        gl.glTexCoord2f(1f, 0f);
        gl.glVertex2f(width, height);

        gl.glEnd();
        texture.disable(gl);
        gl.glDisable(GL2.GL_BLEND);
        gl.glEnable(GL2.GL_DEPTH_TEST);

        if (!Globals.textures)
            gl.glDisable(GL2.GL_TEXTURE_2D);

        gl.glEndList();

        return list;
    }

    public void forceRebuild(GL2 gl) {
        gl.glDeleteLists(generated, 1);
        generated = -1;
        generated = generate(gl);
    }

    public String toString() {
        return "list: " + generated + " x: " + (parent != null ? x + parent.x : x) + " y: "
                + (parent != null ? y + parent.y : y) + " z: " + (parent != null ? z + parent.z : z);
    }

    public Rectangle2D getTopViewBounds() {
        return null;
    }
}
