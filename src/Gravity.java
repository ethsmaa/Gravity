import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.awt.Color;
import enigma.console.TextAttributes;

// !! TODO: taş düşmanın üstüne düştüğü zaman player puan kazancak



public class Gravity {
    public static TextAttributes cyan = new TextAttributes(Color.cyan);
    public static TextAttributes black = new TextAttributes(Color.BLACK);
    public static TextAttributes white = new TextAttributes(Color.white);
    public static TextAttributes blue = new TextAttributes(Color.BLUE);
    public static TextAttributes gray = new TextAttributes(Color.GRAY);
    public static TextAttributes green = new TextAttributes(Color.GREEN);
    public static TextAttributes magenta = new TextAttributes(Color.MAGENTA);
    public static TextAttributes orange = new TextAttributes(Color.orange);
    public static TextAttributes red = new TextAttributes(Color.red);
    public static TextAttributes yellow = new TextAttributes(Color.yellow);
    public static TextAttributes pink = new TextAttributes(Color.PINK);



    public enigma.console.Console cn = Enigma.getConsole("Gravity", 100, 26, 20, 0);
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
    int [][] enemies = new int[100][2];
    int enemiesCount = 0;

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
        CircularQueue inputQueue = new CircularQueue(12);
        char[][] map = new char[25][55];





        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 55; j++) {
                if ((i == 0 || i == 24) || (j == 0 || j == 54)) {
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

        // random 160 eartsquare   ->  boulder
        int boulderCount = 0;
        while (boulderCount < 160) {
            int row = random.nextInt(25);
            int column = random.nextInt(55);
            if (isEarthSquare(map, row, column)) {
                map[row][column] = 'O';
                boulderCount++;
            }
        }

        // random 30 earthsquare  ->  1-2-3 (equal probability)
        int treasureCount = 0;
        while (treasureCount < 30) {
            int row = random.nextInt(24) + 1;
            int column = random.nextInt(54) + 1;
            //random treasure 1 to 3
            int treasure = random.nextInt(3) + 1;
            if (isEarthSquare(map, row, column)) {
                map[row][column] = Integer.toString(treasure).charAt(0);
                treasureCount++;
            }
        }

        int emtySquare = 0;

        while (emtySquare < 200) {
            int row = random.nextInt(24) + 1;
            int column = random.nextInt(54) + 1;
            if (isEarthSquare(map, row, column)) {
                map[row][column] = ' ';
                emtySquare++;
            }
        }

        // random 160 eartsquare   ->  enemy
        int enemyCount = 0;
        while (enemyCount < 7) {
            int row = random.nextInt(25);
            int column = random.nextInt(55);
            if (isEarthSquare(map, row, column)) {
                map[row][column] = 'X';
                enemies[enemiesCount][0] = row;
                enemies[enemiesCount][1] = column;
                enemiesCount++;
                enemyCount++;
            }
        }


        while (!isEarthSquare(map, player.y, player.x)) {
            player.y = random.nextInt(24) + 1;
            player.x = random.nextInt(54) + 1;
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
        boolean boulder_flag = false; 
        boolean game_over = false;

        while (true) {

            if (keypr == 1) {    // if keyboard button pressed
                if (rkey == KeyEvent.VK_LEFT && map[player.y][player.x - 1] != '#') {

                    if(map[player.y][player.x-1] == 'O' && map[player.y][player.x-2] == ' ') {
                        map[player.y][player.x] = ' ';
                        cn.getTextWindow().output(player.x, player.y, ' ');
                        map[player.y][player.x-2] = 'O';cn.getTextWindow().output(player.x-2,player.y,'O');
                        player.x--;}
                    else if(map[player.y][player.x-1] == 'O') {}
                    else {
                        map[player.y][player.x] = ' ';
                        cn.getTextWindow().output(player.x, player.y, ' ');
                        player.x--;}
                }
                if (rkey == KeyEvent.VK_RIGHT && map[player.y][player.x + 1] != '#') {
                    if(map[player.y][player.x+1] == 'O' && map[player.y][player.x+2] == ' ') {
                        map[player.y][player.x] = ' ';
                        cn.getTextWindow().output(player.x, player.y, ' ');
                        map[player.y][player.x+2] = 'O';cn.getTextWindow().output(player.x+2,player.y,'O');
                        player.x++;}
                    else if(map[player.y][player.x+1] == 'O') {}
                    else {
                        map[player.y][player.x] = ' ';
                        cn.getTextWindow().output(player.x, player.y, ' ');
                        player.x++;}
                }
                if (rkey == KeyEvent.VK_UP && map[player.y - 1][player.x] != '#'  && map[player.y - 1][player.x] != 'O') {
                    map[player.y][player.x] = ' ';
                    cn.getTextWindow().output(player.x, player.y, ' ');
                    player.y--;
                }
                if (rkey == KeyEvent.VK_DOWN && map[player.y + 1][player.x] != '#'  && map[player.y + 1][player.x] != 'O') {
                    map[player.y][player.x] = ' ';
                    cn.getTextWindow().output(player.x, player.y, ' ');
                    player.y++;
                    if(boulder_flag == true) {
                    	game_over = true;
                    }
                }

                char rckey = (char) rkey;
                //        left          right          up            down
                if (rckey == '%' || rckey == '\'' || rckey == '&' || rckey == '(')
                    cn.getTextWindow().output(player.x, player.y, 'P' ,green); // VK kullanmadan test teknigi
                else cn.getTextWindow().output(rckey);

                if (rkey == KeyEvent.VK_SPACE && player.getTpRights() > 0) {
                    map[player.y][player.x] = ' ';
                    cn.getTextWindow().output(player.x, player.y,' ');
                    player.x = 0;
                    player.y = 0;
                    while (!isEmptySquare(map, player.y, player.x)) {
                        player.y = random.nextInt(24) + 1;
                        player.x = random.nextInt(54) + 1;
                    }
                    player.teleport();
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
            cn.getTextWindow().output(player.x, player.y, 'P',green);

            char rckey = (char) rkey;
            //        left          right          up            down
            if (rckey == '%' || rckey == '\'' || rckey == '&' || rckey == '(')
                cn.getTextWindow().output(player.x, player.y, 'P' ,green); // VK kullanmadan test teknigi
            else cn.getTextWindow().output(rckey);
            int endOfTheGameRow = 0;
            int endOfTheGameColumn = 0;


            for (int i = 0; i < enemiesCount; i++) {
                int Xx = enemies[i][1];
                int Xy = enemies[i][0];
                if (enemies[i][0] != 0) {
                    int randomDirection = random.nextInt(4);
                    if (randomDirection == 0 && map[Xy - 1][Xx] != 'O' && map[Xy - 1][Xx] != '#' && map[Xy - 1][Xx] != 'X') {

                        cn.getTextWindow().setCursorPosition(enemies[i][1], enemies[i][0]);
                        cn.getTextWindow().output(' ');
                        cn.getTextWindow().setCursorPosition(enemies[i][1], enemies[i][0] - 1);
                        cn.getTextWindow().output('X', blue);
                        enemies[i][0]--;
                        if (map[Xy][Xx] == 'P') {
                            endOfTheGameRow = Xy;
                            endOfTheGameColumn = Xx;
                            break;
                        }
                    } else if (randomDirection == 1 && map[Xy + 1][Xx] != 'O' && map[Xy + 1][Xx] != '#' && map[Xy + 1][Xx] != 'X') {
                        if (map[Xy - 1][Xx] == 'O') {
                            player.killEnemy();
                            map[Xy][Xx] = ' ';
                            cn.getTextWindow().setCursorPosition(enemies[i][1], enemies[i][0]);
                            cn.getTextWindow().output(' ');
                            enemies[i][0] = 0;
                            enemies[i][1] = 0;


                            // enemiesCount--;   bu satır olmalı mı?
                        } else {

                            cn.getTextWindow().setCursorPosition(enemies[i][1], enemies[i][0]);
                            cn.getTextWindow().output(' ');
                            cn.getTextWindow().setCursorPosition(enemies[i][1], enemies[i][0] + 1);
                            cn.getTextWindow().output('X', blue);
                            enemies[i][0]++;
                            if (map[Xy][Xx] == 'P') {
                                endOfTheGameRow = Xy;
                                endOfTheGameColumn = Xx;
                                break;
                            }
                        }


                    } else if (randomDirection == 2 && map[Xy][Xx - 1] != 'O' && map[Xy][Xx - 1] != '#' && map[Xy][Xx - 1] != 'X') {

                        cn.getTextWindow().setCursorPosition(enemies[i][1], enemies[i][0]);
                        cn.getTextWindow().output(' ');
                        cn.getTextWindow().setCursorPosition(enemies[i][1] - 1, enemies[i][0]);
                        cn.getTextWindow().output('X', blue);
                        enemies[i][1]--;
                        if (map[Xy][Xx] == 'P') {
                            endOfTheGameRow = Xy;
                            endOfTheGameColumn = Xx;
                            break;
                        }
                    } else if (randomDirection == 3 && map[Xy][Xx + 1] != 'O' && map[Xy][Xx + 1] != '#' && map[Xy][Xx + 1] != 'X') {

                        cn.getTextWindow().setCursorPosition(enemies[i][1], enemies[i][0]);
                        cn.getTextWindow().output(' ');
                        cn.getTextWindow().setCursorPosition(enemies[i][1] + 1, enemies[i][0]);
                        cn.getTextWindow().output('X', blue);
                        enemies[i][1]++;
                        if (map[Xy][Xx] == 'P') {
                            endOfTheGameRow = Xy;
                            endOfTheGameColumn = Xx;
                            break;
                        }
                    }

                    map[Xy][Xx] = ' '; // old positions

                }
            }
            
            
            
            boulder_flag = false;
            for(int i = 0; i<25 ;i++) {
         	   for (int j = 0;j<55;j++) {
         		   
         		   if(map[i][j] == 'O') {
         			  // System.out.print("okayy");
         			   //if(isEmptySquare(map,i+1,j)) {
         			   if(map[i+1][j] == ' ') { 
         					map[i][j] = ' ';
         					map[i+1][j] = 'O';
         					
                             cn.getTextWindow().output(j, i, ' ');
                           	cn.getTextWindow().output(j,i+1,'O');
         			   }
         			   else if(isEarthSquare(map,i+1,j)) {}
         			 
         			   else if(isboulderSquare(map,i+1,j)) {
         				   if(isEmptySquare(map,i+1,j+1) && isEmptySquare(map,i+1,j-1)) {
         					   int rnd_number = random.nextInt(2);
         					   if(rnd_number ==0) {
         						   map[i][j] = ' ';
                 				   map[i+1][j-1] = 'O';
                 				   cn.getTextWindow().output(j, i, ' ');
                                	   cn.getTextWindow().output(j-1,i+1,'O');
         					   }
         					   else if(rnd_number == 1) {
         						   map[i][j] = ' ';
                 				   map[i+1][j+1] = 'O';
                 				   cn.getTextWindow().output(j, i, ' ');
                                	   cn.getTextWindow().output(j+1,i+1,'O');
         					   }
         				   }
         			   }
         			   
         			   else if (map[i+1][j] == 'P') {
         				   boulder_flag =true;
         			   }
                        else if (map[i+1][j] == 'X') {
                           map[i][j] = ' ';
                           map[i+1][j] = 'O';
                           enemyCount--;

                           player.killEnemy();

                       }


                   }
         		   
         		   
         	   }
            }

            if (map[endOfTheGameRow][endOfTheGameColumn] == 'P'||game_over){
                cn.getTextWindow().setCursorPosition(0, 27);
                cn.getTextWindow().output("GAME OVER. Your score is " , red);
                cn.getTextWindow().setCursorPosition(30, 27);
                cn.getTextWindow().output(" " + player.getScore());
                break;

            }
            if (time % 20 == 0) {
                randomQueueAdd(inputQueue, map);
                printQueue(inputQueue);
            }

            cn.getTextWindow().setCursorPosition(60, 19);
            cn.getTextWindow().output("Teleport : " , magenta);
            cn.getTextWindow().setCursorPosition(70, 19);
            cn.getTextWindow().output("  " +player.getTpRights());


            cn.getTextWindow().setCursorPosition(60, 21);
            cn.getTextWindow().output("Score    : " ,pink);
            cn.getTextWindow().setCursorPosition(70, 21);
            cn.getTextWindow().output("  " +player.getScore());


            cn.getTextWindow().setCursorPosition(60, 23);
            cn.getTextWindow().output("Time     : " , cyan);//getTime()
            cn.getTextWindow().setCursorPosition(70, 23);
            cn.getTextWindow().output("  " +time / 5);

            time++;

            Thread.sleep(100);

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

    boolean isboulderSquare(char[][] map, int row, int column) {
        return map[row][column] == 'O';
    }

    void printMap(char[][] map) {
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 55; j++) {
                cn.getTextWindow().output(j, i, map[i][j]);
            }
        }
    }

    void randomQueueAdd(CircularQueue queue, char[][] map) {
        Random rnd = new Random();
        while (!queue.isFull()) {
            int randomIndex = rnd.nextInt(  40);
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

    void printQueue(CircularQueue queue) {
        CircularQueue tempQueue = new CircularQueue(queue.size());
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
        cn.getTextWindow().output("<<<<<<<<<<<<" ,red);
        cn.getTextWindow().setCursorPosition(60, 4);
        cn.getTextWindow().output(str);
        cn.getTextWindow().setCursorPosition(60, 5);
        cn.getTextWindow().output("<<<<<<<<<<<<" ,red);
    }

    void queueToMap(CircularQueue queue, char[][] map) {
        Random rnd = new Random();
        char element = (char) queue.dequeue();
        if (element == '1' || element == '2' || element == '3' || element == 'X') {

            // select random row and column, if not empty or earth then select random again
            int row = rnd.nextInt(24) + 1;
            int column = rnd.nextInt(54) + 1;
            while (!emptyOrEarth(map, row, column)) {
                row = rnd.nextInt(24) + 1;
                column = rnd.nextInt(54) + 1;
            }
            if (element == 'X'){
                enemies[enemiesCount][0] = row;
                enemies[enemiesCount][1] = column;
                enemiesCount++;
            }
            map[row][column] = element;
            cn.getTextWindow().output(column, row, element);
        }
        else if (element == 'O') {
            // select random row and column, if not empty or earth then select random again
            int row = rnd.nextInt(24) + 1;
            int column = rnd.nextInt(54) + 1;
            while (!emptyOrEarth(map, row, column)) {
                row = rnd.nextInt(24) + 1;
                column = rnd.nextInt(54) + 1;
            }

            // random boulder coordinates
            int row2 = rnd.nextInt(24) + 1;
            int column2 = rnd.nextInt(54) + 1;
            while (!isboulderSquare(map, row2, column2)) {
                row2 = rnd.nextInt(24) + 1;
                column2 = rnd.nextInt(54) + 1;
            }

            map[row][column] = element;
            map[row2][column2] = ' ';
            cn.getTextWindow().output(column, row, element);
            cn.getTextWindow().output(column2, row2, ' ');
        }
        else if (element == ':') {
            // select random row and column, if not empty square then select random again
            int row = rnd.nextInt(24) + 1;
            int column = rnd.nextInt(54) + 1;
            while (!isEmptySquare(map, row, column)) {
                row = rnd.nextInt(24) + 1;
                column = rnd.nextInt(54) + 1;
            }
            map[row][column] = element;
            cn.getTextWindow().output(column, row, element);
        }
        else if (element == 'e') {
            // select random row and column, if not earth square then select random again
            int row = rnd.nextInt(24) + 1;
            int column = rnd.nextInt(54) + 1;
            while (!isEarthSquare(map, row, column)) {
                row = rnd.nextInt(24) + 1;
                column = rnd.nextInt(54) + 1;
            }
            map[row][column] = element;
            cn.getTextWindow().output(column, row, ' ');
        }

    }

    void displayBackpack() {
        while(!player.getBackpack().isEmpty()) {
            displayStack.push(player.getBackpack().pop());
        }
        int a = 0;
        while(!displayStack.isEmpty()) {
            char c = ((String) displayStack.peek()).charAt(0);
            player.getBackpack().push(displayStack.pop());
            cn.getTextWindow().output(66,16-a,c);
            a++;
        }
        cn.getTextWindow().output(66,16-a,' ');
        cn.getTextWindow().output(66,15-a,' ');
    }
}
