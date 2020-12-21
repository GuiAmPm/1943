
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class Entry extends JFrame implements KeyListener, GLEventListener, Runnable {

    private static final long serialVersionUID = 1L;

    public static void main(String[] x) {
        System.out.println("Initializing components...");
        new Entry(640, 480, new Menu(), new Level(), new GameOver());
    }

    Stage[] stage;
    volatile int currentStage = 0;
    volatile boolean running = true;
    int refreshRate;
    GLCanvas canvas;
    int prevStage;

    public Entry(int width, int height, Stage... stage) {

        setAlwaysOnTop(true);

        refreshRate = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode()
                .getRefreshRate();

        this.stage = stage;
        setTitle("1943");
        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        System.out.println("Initializing component OpenGL...");

        canvas = new GLCanvas(new GLCapabilities(GLProfile.get(GLProfile.GL2)));
        canvas.addGLEventListener(this);
        canvas.addKeyListener(this);
        add(canvas);

        FPSAnimator fps = new FPSAnimator(canvas, 100); // There's an issue here?
        fps.start();
        new Thread(this).start();
        new Thread(Globals.fpsCount).start();

        setVisible(true);
        canvas.requestFocus();

        System.out.println("Initialized!");
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (stage != null) {
            stage[currentStage].keyPressed(e.getKeyCode());
        }
    }

    public void keyReleased(KeyEvent e) {
        if (stage != null) {
            stage[currentStage].keyReleased(e.getKeyCode());
        }
    }

    public void init(GLAutoDrawable glad) {
        System.out.println("Initialized OpenGl...");
        GL2 gl = (GL2) glad.getGL();
        gl.glShadeModel(Globals.faceDetail);
        gl.glClearColor(0.06f, 0.3f, 1f, 1f);
        gl.glClearDepth(1D);
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LESS);
        gl.glCullFace(GL2.GL_BACK);
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        if (stage != null && stage.length > 0) {
            stage[0].load(gl);
        }
        System.out.println("Success OpenGl!!");
        System.out.println("Starting game...");
        System.out.println();
    }

    public void dispose(GLAutoDrawable glad) {
        System.out.println("Finalizing OpenGl.");
    }

    public void display(GLAutoDrawable glad) {
        GL2 gl = (GL2) glad.getGL();
        GLUT glut = new GLUT();
        if (stage != null) {
            if (!stage[currentStage].isLoaded()) {
                running = false;
                stage[currentStage].load(gl);
                running = true;
            }
            gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
            gl.glLoadIdentity();
            stage[currentStage].paint(gl);
        }
        Globals.fps++;
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -1.0f);
        gl.glRasterPos2f(0.4f, -0.4f);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "FPS: " + Globals.lastFPS);
    }

    public void logicLoop() {
        if (running && stage != null) {
            stage[currentStage].logicLoop(this);
        }
    }

    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
        if (height == 0) {
            height = 1;
        }
        GL2 gl = (GL2) glad.getGL();
        GLU glu = new GLU();

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45f, (float) width / height, .1f, 100f);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void startStage(int nextStage) {
        if (nextStage < stage.length && nextStage >= 0) {
            prevStage = currentStage;
            currentStage = nextStage;
        }
    }

    public void run() {
        while (true) {
            try {
                logicLoop();
                Thread.sleep(10);
            } catch (Exception e) {
            }
        }
    }

}
