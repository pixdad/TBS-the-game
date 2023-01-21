package fr.pixdad.Game.fight.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import fr.pixdad.Game.TBSGame;
import fr.pixdad.Game.data.entities.StatsValue;
import fr.pixdad.Game.fight.Fighter;

public class StatsUI extends Group {

    Fighter selected;
    Fighter targeted;
    private boolean _isFighing = false;
    Skin skin;

    Window selectWin;
    Window targetWin;

    public StatsUI(float width, float height, Skin skin) {
        super();
        this.setHeight(height);
        this.setWidth(width);
        this.skin = skin;

        selectWin = new Window("Selected", skin);
        targetWin = new Window("Action Result", skin);

        selectWin.setMovable(false);
        selectWin.setHeight(height);
        selectWin.setWidth(width/2);
        selectWin.setDebug(TBSGame.IS_GUI_DEBUG);
        targetWin.setMovable(false);
        targetWin.setHeight(height);
        targetWin.setWidth(width/2);
        targetWin.setDebug(TBSGame.IS_GUI_DEBUG);


        selectWin.setPosition(0, 0);
        targetWin.setPosition(width/2, 0);

        /*this.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                System.out.println("Hover");
            }
        });*/


        this.addActor(selectWin);
        this.addActor(targetWin);
    }




    public void showSelected(Fighter ft) {
        this.selected = ft;
        showStats(selectWin, ft, 0, StatsValue.ZERO);
    }
    public void showTargeted(Fighter ft, int hpMofif, StatsValue modif) {
        this.targeted = ft;
        showStats(targetWin, ft, hpMofif, modif);
    }
    public void emptyTargeted() {
        this.targeted = null;
        emptyWindow(targetWin);
    }

    private void emptyWindow(Window window) {
        window.clear();
    }
    public void empty() {
        emptyWindow(selectWin);
        emptyWindow(targetWin);
    }

    private void showStats(Window win, Fighter ft, int hpModif, StatsValue statsModif) {
        emptyWindow(win);
        Label name = new Label("Ragnarok Le Terrible"/*left.character.getName()*/, skin);
        Image fImage = new Image(ft.character.getTextureRegion());
        fImage.setScaling(Scaling.fit);
        StatsValue statsCurrent = ft.getCurrentStats();
        Label hpLabel = new Label(String.format("HP : %s / %s", formatStat(ft.hp, hpModif), formatStat(statsCurrent.hpMax, statsModif.hpMax)) ,skin);
        Label atkLabel = new Label(String.format("ATK : %s",formatStat(statsCurrent.atk, statsModif.atk)),skin);
        Label defLabel = new Label(String.format("DEF : %s",formatStat(statsCurrent.atk, statsModif.atk)),skin);
        Label strLabel = new Label(String.format("STR : %s",formatStat(statsCurrent.atk, statsModif.atk)),skin);
        Label moveLabel = new Label(ft._hasMoved?"Can't move for this turn":"Ready to move",skin);

        VerticalGroup stats = new VerticalGroup().align(Align.topLeft).fill();
        stats.addActor(hpLabel);
        stats.addActor(atkLabel);
        stats.addActor(defLabel);
        stats.addActor(strLabel);
        stats.addActor(moveLabel);

        win.add(name).colspan(2).align(Align.left);
        win.row();
        win.add(fImage).fill().expand(1, 1);
        win.add(stats).align(Align.topLeft).expand(2, 1);//.width(Value.percentWidth(70));
    }

    private String formatStat(int current, int modif) {
        return current + (modif!=0 ? " [RED](" + Math.max(current - modif, 0) + ")[]" : "");
    }


}
