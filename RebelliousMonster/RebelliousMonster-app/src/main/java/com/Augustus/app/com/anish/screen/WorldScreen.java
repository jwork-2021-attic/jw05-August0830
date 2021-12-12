package com.Augustus.app.com.anish.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.Augustus.app.asciiPanel.AsciiPanel;
import com.Augustus.app.com.anish.calabashbros.Goblin;
import com.Augustus.app.com.anish.calabashbros.Monster;
import com.Augustus.app.com.anish.calabashbros.Stone;
import com.Augustus.app.com.anish.calabashbros.World;

public class WorldScreen implements Screen {

    private World world;
    BlockingQueue<KeyEvent> keyMessage;
    public static final int GOBCNT = 10;
    String[] sortSteps;
    ArrayList<Thread> gobThreads;

    // ArrayList<Thread>
    public WorldScreen() {
        world = new World();
        int width = 40;
        int height = 20;
        MapGenerator mapgen = new MapGenerator(width, height);
        System.out.println(WorldScreen.class.getClassLoader().getResource("com/Augustus/app/resources"));
        int[][] data = mapgen
                .getData(WorldScreen.class.getClassLoader().getResource("com/Augustus/app/resources/NJUCS.bmp"));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (data[j][i] == -1) {// 有颜色输出
                    // System.out.print("*");
                    world.put(new Stone(world, j, i), j, i);
                } else { // 无颜色输出

                }
            }
        }

        gobThreads = new ArrayList<Thread>();
        keyMessage = new LinkedBlockingQueue<KeyEvent>();
        Random r = new Random();
        for (int i = 0; i < GOBCNT; ++i) {
            int red = (r.nextInt(255) + 255) / 2;
            int blue = (r.nextInt(255) + 255) / 2;
            int green = (r.nextInt(255) + 255) / 2;
            Goblin gob = new Goblin(new Color(red, green, blue), world, 50);
            // hero = new Calabash(new Color(red,green,blue),1,world);
            // world.put(hero,0,startList.get(r.nextInt(startList.size())));
            gobThreads.add(new Thread(gob));
            gobThreads.get(i).start();
        }

        Monster monster = new Monster(world, 50);
        monster.setReceiver(keyMessage);
        Thread LocalMonster = new Thread(monster);
        LocalMonster.start();

    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());

            }
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        try {
            keyMessage.put(key);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this;
    }

}
