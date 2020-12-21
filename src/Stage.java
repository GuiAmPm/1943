import com.jogamp.opengl.GL2;

public interface Stage {
    public void paint(GL2 gl);

    public void logicLoop(Entry frame);

    public boolean isLoaded();

    public void load(GL2 gl);

    public void unload(GL2 gl);

    public void keyPressed(int key);

    public void keyReleased(int key);

    public void fixCamera(GL2 gl);
}
