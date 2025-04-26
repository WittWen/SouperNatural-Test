package com.badlogic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class HUD extends Table {
    private Array<BaseActor> hungerIcons;
    private Texture emptyHungerTexture;
    private Texture fullHungerTexture;
    private Texture halfHungerTexture;
    private Stage stage;
    private float leftPadding = 15f;  // 增加左边距
    private float topPadding = 50f;   // 增加上边距

    private Table currencyTable;
    private BaseActor silverIcon;
    private Label balanceLabel;
    private BitmapFont font;
    private LabelStyle labelStyle;

    public HUD(Stage s) {
        super();
        this.stage = s;
        hungerIcons = new Array<>();

        // 加载纹理
        emptyHungerTexture = new Texture("empty-hunger.png");
        fullHungerTexture = new Texture("full-hunger.png");
        halfHungerTexture = new Texture("half-hunger.png");

        // 创建货币显示
        currencyTable = new Table();
        silverIcon = new BaseActor(0, 0, s);
        silverIcon.loadTexture("silver.png");

        silverIcon.setSize(24,27);

        font = new BitmapFont();
        font.getData().setScale(2.0f);
        labelStyle = new LabelStyle(font, Color.WHITE);
        balanceLabel = new Label("0", labelStyle);
        balanceLabel.setFontScale(2.0f);

        // 将货币图标和数量添加到货币表格中
        currencyTable.add(silverIcon).padRight(10f);
        currencyTable.add(balanceLabel).padLeft(5f);

        // 设置主表格布局
        setPosition(leftPadding, stage.getHeight() - topPadding);
        add(currencyTable).padTop(10f).row();

        stage.addActor(this);

        // 初始更新货币显示
        updateCurrencyDisplay();
    }

    public void updateHungerDisplay(float currentHungerPoint, float maxHungerPoint) {
        // 清除现有的图标
        clearChildren();
        hungerIcons.clear();

        // 计算需要显示的心的数量
        // 每2点血对应1颗心，所以最大心数是maxHungerPoint/2
        int totalHearts = (int)Math.ceil(maxHungerPoint / 2.0f);

        // 创建一个新的表格来存放心形图标，并设置左对齐
        Table heartsTable = new Table();
        heartsTable.left();  // 设置左对齐

        // 创建并添加心形图标
        for (int i = 0; i < totalHearts; i++) {
            BaseActor heartIcon = new BaseActor(0, 0, stage);

            // 计算当前心的状态
            float currentHeartBlood = currentHungerPoint - (i * 2);

            if (currentHeartBlood >= 2) {
                heartIcon.loadTexture("full-hunger.png");
            } else if (currentHeartBlood >= 1) {
                heartIcon.loadTexture("half-hunger.png");
            } else {
                heartIcon.loadTexture("empty-hunger.png");
            }

            heartsTable.add(heartIcon).padRight(5f).left();  // 添加到表格时也指定左对齐
            hungerIcons.add(heartIcon);
        }

        // 将心形图标表格添加到主表格，并指定左对齐和填充整个宽度
        add(heartsTable).left().expandX().row();
        // 添加货币表格
        add(currencyTable).padTop(10f).left();

        // 设置整个表格左对齐
        left();
    }

    public void updateCurrencyDisplay() {
        int currentBalance = CurrencyManager.getInstance().getCurrentBalance();
        balanceLabel.setText(String.valueOf(currentBalance));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // 确保HUD始终在左上角
        setPosition(leftPadding, stage.getHeight() - getHeight() - topPadding);
        // 更新货币显示
        updateCurrencyDisplay();
    }

    public void dispose() {
        emptyHungerTexture.dispose();
        fullHungerTexture.dispose();
        halfHungerTexture.dispose();
        font.dispose();
    }
}
