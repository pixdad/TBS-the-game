package fr.pixdad.Game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.pixdad.Game.data.DataManager;
import fr.pixdad.Game.data.DataManagerOld;
import fr.pixdad.Game.menus.MainMenuScreen;

import java.util.ArrayList;

public class TBSGame extends Game {

    public static Boolean IS_GUI_DEBUG = false;

    private final ArrayList<Screen> screens = new ArrayList<Screen>();
    public SpriteBatch batch;


    public void addScreen(Screen newScreen) {
        Screen current = this.getScreen();
        screens.add(current);
        super.setScreen(newScreen);
    }

    public Boolean closeScreen() {
        if (!screens.isEmpty()) {
            Screen lastScreen = screens.get(screens.size()-1);
            setScreen(lastScreen);
            screens.remove(lastScreen);
            return true;
        }
        return false;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        //TODO: Find a way to merge Datamanger & DatamanagerOld
        DataManagerOld.loadData();
        DataManager.get().init();

        addScreen(new MainMenuScreen(this));
    }


    /*public void tests(TBSGame game) {

        ArrayList<Rectangle> rects = new ArrayList<Rectangle>();

        //Sur le point
        rects.add(new Rectangle(-1, -1, 1, 1));
        rects.add(new Rectangle(0, -1, 0, 1));
        rects.add(new Rectangle(1, -1, -1, 1));
        rects.add(new Rectangle(-1, 0, 1, 0));

        //A droite
        rects.add(new Rectangle(1, 0, 2, 0));
        rects.add(new Rectangle(1, -1, 2, 1));
        rects.add(new Rectangle(1, 1, 2, -1));
        rects.add(new Rectangle(1, 0, 1, 1));
        rects.add(new Rectangle(1, -1, 1, 0));

        //A gauche
        rects.add(new Rectangle(-1, 0, -2, 0));
        rects.add(new Rectangle(-1, -1, -2, 1));
        rects.add(new Rectangle(-1, 1, -2, -1));
        rects.add(new Rectangle(-1, 0, -1, 1));
        rects.add(new Rectangle(-1, -1, -1, 0));

        Polygon polygon = new Polygon();
        float[] vertices = {
          0,0,
          0,2,
          1,2,
          1,3,
          2,3,
          2,1,
          1,1,
          1,0,
        };
        polygon.setVertices(vertices);


        float x = 0;
        float y = 0;

        for (Rectangle rect : rects) {

            float x1 = rect.x;
            float y1 = rect.y;
            float x2 = rect.width;
            float y2 = rect.height;

            boolean bool = ((y1 <= y && y < y2) || (y2 <= y && y < y1)) && x < ((x2 - x1) / (y2 - y1) * (y - y1) + x1);
            System.out.println(rect.toString()+ " : " + bool);
        }

        System.out.println("Polygon : "+polygon.contains(0,0));

        for (int i = 0;i<=2;i++) {
            for (int j = 0; j <= 3; j++) {
                System.out.println("["+i+";"+j+"] : "  +polygon.contains(i+0.5f,j+0.5f));
            }
        }



    }*/
}
