import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Gravity {
    public enigma.console.Console cn = Enigma.getConsole("Gravity", 100, 26, 22, 0);
    public TextMouseListener tmlis;
    public KeyListener klis;

    // ------ Standard variables for mouse and keyboard ------
    public int mousepr;          // mouse pressed?
    public int mousex, mousey;   // mouse text coords.
    public int keypr;   // key pressed?
    public int rkey;    // key   (for press/release)
    // ----------------------------------------------------


    Stack displayStack = new Stack(8);
    Player player;


    Gravity() throws Exception {

        // --- Contructor
        // ------ Standard code for mouse and keyboard ------ Do not change
        tmlis = new TextMouseListener() {
            public void mouseClicked(TextMouseEvent arg0) {
            }

            public void mousePressed(TextMouseEvent arg0) {
                if (mousepr == 0) {
                    mousepr = 1;
                    mousex = arg0.getX();
                    mousey = arg0.getY();
                }
            }

            public void mouseReleased(TextMouseEvent arg0) {
            }
        };

        cn.getTextWindow().addTextMouseListener(tmlis);

        klis = new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (keypr == 0) {
                    keypr = 1;
                    rkey = e.getKeyCode();
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        };
        cn.getTextWindow().addKeyListener(klis);
        // ----------------------------------------------------
        player = new Player(0,0);
        Random random = new Random();
        Queue inputQueue = new Queue(1000);
        char[][] map = new char[25][55];


        player.x = 0;
        player.y = 0;


        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 55; j++) {
                if (i == 0 || i == 24 || j == 0 || j == 54) {
                    map[i][j] = '#';
                } else if (i == 8 && j < 50) {
                    map[i][j] = '#';
                } else if (i == 16 && j > 4) {
                    map[i][j] = '#';
                } else {
                    map[i][j] = ':';
                }
            }
        }

        // random 160 eartsquare   ->  bound
        int boundCount = 0;
        while (boundCount < 160) {
            int row = random.nextInt(25);
            int column = random.nextInt(55);
            if (isEarthSquare(map, row, column)) {
                map[row][column] = 'O';
                boundCount++;
            }
        }

        // random 30 earthsquare  ->  1-2-3 (equal probability)
        int treasureCount = 0;
        while (treasureCount < 30) {
            int row = random.nextInt(25);
            int column = random.nextInt(55);
            int treasure = random.nextInt(3) + 1; // (1 - 3)
            if (isEarthSquare(map, row, column)) {
                map[row][column] = Integer.toString(treasure).charAt(0);
                treasureCount++;
            }
        }

        int emtySquare = 0;

        while (emtySquare < 200) {
            int row = random.nextInt(25);
            int column = random.nextInt(55);
            if (isEarthSquare(map, row, column)) {
                map[row][column] = ' ';
                emtySquare++;
            }
        }


        while (!isEarthSquare(map, player.y, player.x)) {
            player.y = random.nextInt(25);
            player.x = random.nextInt(55);
        }
        map[player.y][player.x] = 'P';
        cn.getTextWindow().output(player.x, player.y, 'P');

        // print map
        printMap(map);
        int time = 0;

        for(int i = 0;i < 18;i++) {
            if(i <9) {cn.getTextWindow().output(64,8+i,'|');}
            else {cn.getTextWindow().output(68,i-1,'|');}
        }
        for(int i = 0;i < 5;i++) {
            if(i == 0 || i == 4) {
                cn.getTextWindow().output(64+i,17,'+');}
            else {
                cn.getTextWindow().output(64+i,17,'-');}
        }


        while (true) {


            if (mousepr == 1) {  // if mouse button pressed
                cn.getTextWindow().output(mousex, mousey, '#');  // write a char to x,y position without changing cursor position
                player.x = mousex;
                player.y = mousey;

                mousepr = 0;     // last action
            }
            if (keypr == 1) {    // if keyboard button pressed
                if (rkey == KeyEvent.VK_LEFT && map[player.y][player.x - 1] != '#') {
                    cn.getTextWindow().output(player.x, player.y, ' ');
                    player.x--;
                }
                if (rkey == KeyEvent.VK_RIGHT && map[player.y][player.x + 1] != '#') {
                    cn.getTextWindow().output(player.x, player.y, ' ');
                    player.x++;
                }
                if (rkey == KeyEvent.VK_UP && map[player.y - 1][player.x] != '#') {
                    cn.getTextWindow().output(player.x, player.y, ' ');
                    player.y--;
                }
                if (rkey == KeyEvent.VK_DOWN && map[player.y + 1][player.x] != '#') {
                    cn.getTextWindow().output(player.x, player.y, ' ');
                    player.y++;
                }

                char rckey = (char) rkey;
                //        left          right          up            down
                if (rckey == '%' || rckey == '\'' || rckey == '&' || rckey == '(')
                    cn.getTextWindow().output(player.x, player.y, 'P'); // VK kullanmadan test teknigi
                else cn.getTextWindow().output(rckey);

                if (rkey == KeyEvent.VK_SPACE) {
                    String str;
                    str = cn.readLine();     // keyboardlistener running and readline input by using enter
                    cn.getTextWindow().setCursorPosition(5, 20);
                    cn.getTextWindow().output(str);
                }

                keypr = 0;    // last action
            }


            if(map[player.y][player.x] == '1' || map[player.y][player.x] == '2' || map[player.y][player.x] == '3') {
                if(map[player.y][player.x] == '1') {player.addNewItem("1");}
                else if(map[player.y][player.x] == '2') {player.addNewItem("2");}
                else if (map[player.y][player.x] == '3') {player.addNewItem("3");}

                displayBackpack();


            }
            map[player.y][player.x] = 'P';
            cn.getTextWindow().output(player.x, player.y, 'P');

            char rckey = (char) rkey;
            //        left          right          up            down
            if (rckey == '%' || rckey == '\'' || rckey == '&' || rckey == '(')
                cn.getTextWindow().output(player.x, player.y, 'P'); // VK kullanmadan test teknigi
            else cn.getTextWindow().output(rckey);

            if (rkey == KeyEvent.VK_SPACE) {
                String str;
                str = cn.readLine();     // keyboardlistener running and readline input by using enter
                cn.getTextWindow().setCursorPosition(5, 20);
                cn.getTextWindow().output(str);
            }

            if (time % 80 == 0) {
                randomQueueAdd(inputQueue, map);
                printQueue(inputQueue);
            }

            cn.getTextWindow().setCursorPosition(60, 19);
            cn.getTextWindow().output("Teleport : " + player.getTpRights());


            cn.getTextWindow().setCursorPosition(60, 21);
            cn.getTextWindow().output("Score    : " + player.getScore());

            cn.getTextWindow().setCursorPosition(60, 23);
            cn.getTextWindow().output("Time     : " + time/40);//getTime()


            time++;

            Thread.sleep(20);

        }


    }

    boolean emptyOrEarth(char[][] map, int row, int column) {
        return map[row][column] == ' ' || map[row][column] == ':';
    }

    boolean isEmptySquare(char[][] map, int row, int column) {
        return map[row][column] == ' ';
    }


    boolean isEarthSquare(char[][] map, int row, int column) { // ora henüz bir şeye dönüşmediyse
        return map[row][column] == ':';
    }

    boolean isBoundSquare(char[][] map, int row, int column) {
        return map[row][column] == 'O';
    }


    void printMap(char[][] map) {
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 55; j++) {
                cn.getTextWindow().output(j, i, map[i][j]);
            }
        }
    }

    void randomQueueAdd(Queue queue, char[][] map) {
        Random rnd = new Random();
        while (queue.size()<13) {
            int randomIndex = rnd.nextInt(40);
            if (randomIndex < 6) { // 6/40 probability
                queue.enqueue('1');
                // 6 / 40 probability
            } else if (5 < randomIndex && randomIndex < 11) {
                queue.enqueue('2');
                // 5/40 probability
            } else if (10 < randomIndex && randomIndex < 15) {
                queue.enqueue('3');
                //4/40 probability
            } else if (14 < randomIndex && randomIndex < 16) {
                queue.enqueue('X');
                //1/40 probability -> enemy
            } else if (16 < randomIndex && randomIndex < 27) {
                queue.enqueue('O');
                //10/40 probability
            } else if (25 < randomIndex && randomIndex < 35) {
                queue.enqueue(':');
                // 9/40 probability
            } else {
                queue.enqueue('e');
                // 5/40 probability
            }
        }

        queueToMap(queue, map);
    }

    void printQueue(Queue queue) {
        Queue tempQueue = new Queue(queue.size());
        String str = "";
        while (!queue.isEmpty()) {
            Object value = queue.dequeue();
            tempQueue.enqueue(value);
            str += value;
        }
        while (!tempQueue.isEmpty()) {
            queue.enqueue(tempQueue.dequeue());
        }
        cn.getTextWindow().setCursorPosition(60, 3);
        cn.getTextWindow().output("<<<<<<<<<<<<");
        cn.getTextWindow().setCursorPosition(60, 4);
        cn.getTextWindow().output(str);
        cn.getTextWindow().setCursorPosition(60, 5);
        cn.getTextWindow().output("<<<<<<<<<<<<");
    }

    void queueToMap(Queue queue, char[][] map) {
        Random rnd = new Random();
        char element = (char) queue.dequeue();
        if (element == '1' || element == '2' || element == '3' || element == 'X') {

            // select random row and column, if not empty or ground then select random again
            int row = rnd.nextInt(25);
            int column = rnd.nextInt(55);
            while (!emptyOrEarth(map, row, column)) {
                row = rnd.nextInt(25);
                column = rnd.nextInt(55);
            }
            map[row][column] = element;
            cn.getTextWindow().output(column, row, element);
        }
        else if (element == 'O') {
            // select random row and column, if not empty or ground then select random again
            int row = rnd.nextInt(25);
            int column = rnd.nextInt(55);
            while (!emptyOrEarth(map, row, column)) {
                row = rnd.nextInt(25);
                column = rnd.nextInt(55);
            }

            // random bound coordinates
            int row2 = rnd.nextInt(25);
            int column2 = rnd.nextInt(55);
            while (!isBoundSquare(map, row2, column2)) {
                row2 = rnd.nextInt(25);
                column2 = rnd.nextInt(55);
            }

            map[row][column] = element;
            map[row2][column2] = ' ';
            cn.getTextWindow().output(column, row, element);
            cn.getTextWindow().output(column2, row2, ' ');
        }
        else if (element == ':') {
            // select random row and column, if not empty square then select random again
            int row = rnd.nextInt(25);
            int column = rnd.nextInt(55);
            while (!isEmptySquare(map, row, column)) {
                row = rnd.nextInt(25);
                column = rnd.nextInt(55);
            }
            map[row][column] = element;
            cn.getTextWindow().output(column, row, element);
        }
        else if (element == 'e') {
            // select random row and column, if not earth square then select random again
            int row = rnd.nextInt(25);
            int column = rnd.nextInt(55);
            while (!isEarthSquare(map, row, column)) {
                row = rnd.nextInt(25);
                column = rnd.nextInt(55);
            }
            map[row][column] = element;
            cn.getTextWindow().output(column, row, ' ');
        }

    }

    void displayBackpack() {
        while(!player.getBackpack().isEmpty()) {displayStack.push(player.getBackpack().pop());}
        int a = 0;
        while(!displayStack.isEmpty()) {
            char c = ((String) displayStack.peek()).charAt(0);
            player.getBackpack().push(displayStack.pop());
            cn.getTextWindow().output(66,16-a,c);
            a++;
        }
    }


}



