package fr.pixdad.Game.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import fr.pixdad.Game.fight.states.FightingScreen;
import fr.pixdad.Game.data.entities.GameLevel;
import fr.pixdad.Game.TBSGame;
import fr.pixdad.Game.data.DataManager;

public class MainMenuScreen implements Screen {
    private static String TAG = MainMenuScreen.class.getSimpleName();

    private Stage stage;
    private Skin skin;

    private final Table mainMenuTable;
    private Label titleLabel;

    private TBSGame game;


    public MainMenuScreen(final TBSGame game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("Skins/default/skin/uiskin.json"));

        titleLabel = new Label("TBS The Game", skin);
        titleLabel.setAlignment(Align.center);

        mainMenuTable = new Table();
        mainMenuTable.setFillParent(true);
        mainMenuTable.setDebug(TBSGame.IS_GUI_DEBUG); // This is optional, but enables debug lines for tables.
        mainMenuTable.defaults()
                .expandX()
                .height(80)
                .maxWidth(300)
                .pad(5, 5, 5, 5)
                .fill();

        createMainMenu();
        stage.addActor(mainMenuTable);

    }


    private void createMainMenu() {
        final TextButton newGameButton = new TextButton("Start a new Game", skin);
        final TextButton loadSaveButton = new TextButton("Load a save", skin);
        final TextButton exitButton = new TextButton("Exit the Game", skin);
        mainMenuTable.clear();
        mainMenuTable.add(titleLabel);
        mainMenuTable.row();
        mainMenuTable.add(newGameButton);
        mainMenuTable.row();
        mainMenuTable.add(loadSaveButton);
        mainMenuTable.row();
        mainMenuTable.add(exitButton);

        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(TAG, "New Game launched");
                createNewGameMenu();
            }
        });
        loadSaveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(TAG, "Load Save launched");
                createLoadSaveMenu();
            }
        });
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(TAG, "Exit button pressed");
                Gdx.app.exit();
            }
        });
    }


    private void createNewGameMenu() {
        final TextButton createButton = new TextButton("Create", skin);
        final TextButton backButton = new TextButton("Cancel", skin);
        final TextField nameField = new TextField("", skin);
        titleLabel.setText("Enter a name for the new game");
        mainMenuTable.clear();
        mainMenuTable.add(titleLabel).colspan(2);
        mainMenuTable.row();
        mainMenuTable.add(nameField).colspan(2);
        mainMenuTable.row();
        mainMenuTable.add(createButton).align(Align.right);
        mainMenuTable.add(backButton).align(Align.left);

        createButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String name = nameField.getText().trim();
                if(name.isEmpty()) {
                    Dialog dialog = new Dialog("Error", skin);
                    dialog.text("Name can not be blank");
                    dialog.button("Close");
                    dialog.setMovable(true);
                    dialog.show(stage);
                }
                else {
                    DataManager.get().createNewWorld(name);
                    createListLevelMenu();
                }
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(TAG, "Load Save launched");
                createMainMenu();
            }
        });
    }


    private void createLoadSaveMenu() {
        final TextButton backButton = new TextButton("Back", skin);
        titleLabel.setText("Load an existing save");
        mainMenuTable.clear();
        mainMenuTable.add(titleLabel);
        mainMenuTable.row();
        mainMenuTable.add(backButton);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(TAG, "Load Save launched");
                createMainMenu();
            }
        });
    }

    public void createListLevelMenu() {
        mainMenuTable.clear();

        titleLabel.setText("Select a level to play");
        mainMenuTable.add(titleLabel);
        mainMenuTable.row();

        final VerticalGroup vgroup = new VerticalGroup();
        final ScrollPane scrollPane = new ScrollPane(vgroup);
        mainMenuTable.add(scrollPane);

        for (GameLevel level: DataManager.get().getCurrentWorld().getAvailableLevels()) {
            final TextButton levelButton = new TextButton(level.getName(), skin);
            vgroup.addActor(levelButton);

            levelButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.addScreen(new FightingScreen(game, level, 1));
                }
            });
        }
    }




    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
