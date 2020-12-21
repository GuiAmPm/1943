import com.jogamp.opengl.GL2;

public class Globals {

    public static final int SMOOTH = GL2.GL_SMOOTH, FLAT = GL2.GL_FLAT;

    public static int score = 0;
    public static int level = 1;
    public static int playerCount = 1;

    public static int detailLevel = 5;
    public static boolean textures = true;
    public static boolean illumination = true;
    public static int treeCount = 200;
    public static int faceDetail = SMOOTH;
    public static boolean celShaded = true;
    public static boolean wireFrame = false;

    public static volatile int fps;
    public static volatile int lastFPS;

    public static Runnable fpsCount = new Runnable() {

        public void run() {
            while (true) {
                lastFPS = fps;
                fps = 0;
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        }
    };

}
