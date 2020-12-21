
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import java.awt.event.KeyEvent;

public class Menu implements Stage {

    int menuPos;
    int menu;
    boolean isLoaded = false;

    public void paint(GL2 gl) {
        if (!isLoaded) {
            return;
        }

        GLUT glut = new GLUT();

        fixCamera(gl);

        gl.glRasterPos2f(.4f, -.5f - .1f * (menuPos));
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, ">>");
        if (menu == 0) {
            gl.glRasterPos2f(.5f, -.5f);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Play: 1 Player");
            gl.glRasterPos2f(.5f, -.6f);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Play: 2 Players");
            gl.glRasterPos2f(.5f, -.7f);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Options");
            gl.glRasterPos2f(.5f, -.8f);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Exit");
        }
        if (menu == 1) {
            gl.glRasterPos2f(.5f, -.5f);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Airplane Detail level: " + Globals.detailLevel);
            gl.glRasterPos2f(.5f, -.6f);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Tree count: " + Globals.treeCount);
            gl.glRasterPos2f(.5f, -.7f);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Textures: " + Globals.textures);
            gl.glRasterPos2f(.5f, -.8f);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Cel-Shading: " + Globals.celShaded);
            gl.glRasterPos2f(.5f, -.9f);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Wire Frame: " + Globals.wireFrame);
            gl.glRasterPos2f(.5f, -1f);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Back");
        }
    }

    public void logicLoop(Entry frame) {
        if (!isLoaded) {
            return;
        }
        if (up)
            menuPos--;
        if (down)
            menuPos++;

        if (menu == 0) {
            if (menuPos == -1)
                menuPos = 3;
            if (menuPos > 3)
                menuPos = 0;
            if (pressed) {
                if (menuPos == 0) {
                    Globals.playerCount = 1;
                    frame.startStage(1);
                }
                if (menuPos == 1) {
                    Globals.playerCount = 2;
                    frame.startStage(1);
                }
                if (menuPos == 2) {
                    menuPos = 0;
                    menu = 1;
                }
                if (menuPos == 3) {
                    System.exit(0);
                }
            }
        } else {
            if (menuPos == -1)
                menuPos = 5;
            if (menuPos > 5)
                menuPos = 0;
            if (pressed) {
                if (menuPos == 5) {
                    menuPos = 0;
                    menu = 0;
                }
            }
            if (left) {
                if (menuPos == 0) {
                    if (Globals.detailLevel > 1)
                        Globals.detailLevel--;
                }
                if (menuPos == 1) {
                    if (Globals.treeCount > 0)
                        Globals.treeCount -= 10;
                }
                if (menuPos == 2) {
                    Globals.textures = !Globals.textures;
                }
                if (menuPos == 3) {
                    Globals.celShaded = !Globals.celShaded;
                    if (Globals.celShaded == true)
                        Globals.wireFrame = false;
                }
                if (menuPos == 4) {
                    Globals.wireFrame = !Globals.wireFrame;
                    if (Globals.celShaded == true)
                        Globals.celShaded = false;
                }
            }
            if (right) {
                if (menuPos == 0) {
                    Globals.detailLevel++;
                }
                if (menuPos == 1) {
                    if (Globals.treeCount < 1500)
                        Globals.treeCount += 10;
                }
                if (menuPos == 2) {
                    Globals.textures = !Globals.textures;
                }
                if (menuPos == 3) {
                    Globals.celShaded = !Globals.celShaded;
                    if (Globals.celShaded == true)
                        Globals.wireFrame = false;
                }
                if (menuPos == 4) {
                    Globals.wireFrame = !Globals.wireFrame;
                    if (Globals.celShaded == true)
                        Globals.celShaded = false;
                }
            }
        }
        up = down = left = right = pressed = false;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void load(GL2 gl) {
        if (isLoaded) {
            return;
        }
        gl.glClearColor(0f, 0f, 0f, 1f);
        up = down = left = right = pressed = false;
        menu = menuPos = 0;
        Globals.score = 0;
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
        switch (key) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                up = true;
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                down = true;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                left = true;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                right = true;
                break;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_NUMPAD0:
                pressed = true;
                break;
        }
    }

    boolean up, down, left, right, pressed;

    public void fixCamera(GL2 gl) {
        gl.glLoadIdentity();
        gl.glTranslatef(-1, .6f, -1.8f);
    }
}
