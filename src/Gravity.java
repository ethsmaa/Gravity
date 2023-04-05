import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Gravity {
    public enigma.console.Console cn = Enigma.getConsole("Gravity", 100, 35, 22, 0);
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
        Random random = new Random();
        char[][] map = new char[25][55];


        int px = 0, py = 0;

// find a random empty location for the player


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
            if (isAlreadyChosen(map, row, column)) {
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
            if (isAlreadyChosen(map, row, column)) {
                map[row][column] = Integer.toString(treasure).charAt(0);
                treasureCount++;
            }
        }

        int emtySquare = 0;

        while (emtySquare < 200) {
            int row = random.nextInt(25);
            int column = random.nextInt(55);
            if (isAlreadyChosen(map, row, column)) {
                map[row][column] = ' ';
                emtySquare++;
            }
        }


        while (!isAlreadyChosen(map,py,px)) {
            py = random.nextInt(25);
            px = random.nextInt(55);
        }
        map[py][px] = 'P';
        cn.getTextWindow().output(px, py, 'P');
        player = new Player(px,py);

        // print map
        printMap(map);
        
        for(int i = 0;i < 18;i++) {
      	  if(i <9) {cn.getTextWindow().output(72,6+i,'|');}
      	  else {cn.getTextWindow().output(76,i-3,'|');}
        }
        for(int i = 0;i < 5;i++) {
      	  if(i == 0 || i == 4) {cn.getTextWindow().output(72+i,14,'+');}
      	  else {cn.getTextWindow().output(72+i,14,'-');}
        }
        
        cn.getTextWindow().setCursorPosition(71, 15);
  		cn.getTextWindow().output("Backpack");
  		
  		cn.getTextWindow().setCursorPosition(68, 18);
  		cn.getTextWindow().output("Teleport :   3");

  	
  		cn.getTextWindow().setCursorPosition(68, 20);
  		cn.getTextWindow().output("Score    :   0");
  	
  		cn.getTextWindow().setCursorPosition(68, 22);
  		cn.getTextWindow().output("Time     :   0");
        

        while (true) {
        	
            if (mousepr == 1) {  // if mouse button pressed
                cn.getTextWindow().output(mousex, mousey, '#');  // write a char to x,y position without changing cursor position
                px = mousex;
                py = mousey;

                mousepr = 0;     // last action
            }
            if (keypr == 1) {    // if keyboard button pressed
                if (rkey == KeyEvent.VK_LEFT && map[py][px - 1] != '#') {
                    cn.getTextWindow().output(px, py, ' ');
                    px--;
                }
                if (rkey == KeyEvent.VK_RIGHT && map[py][px + 1] != '#') {
                    cn.getTextWindow().output(px, py, ' ');
                    px++;
                }
                if (rkey == KeyEvent.VK_UP && map[py - 1][px] != '#') {
                    cn.getTextWindow().output(px, py, ' ');
                    py--;
                }
                if (rkey == KeyEvent.VK_DOWN && map[py + 1][px] != '#') {
                    cn.getTextWindow().output(px, py, ' ');
                    py++;
                }
                

                
                
            if(map[py][px] == '1' || map[py][px] == '2' || map[py][px] == '3') {
            	if(map[py][px] == '1') {player.addNewItem("1");}
                else if(map[py][px] == '2') {player.addNewItem("2");}
                else if (map[py][px] == '3') {player.addNewItem("3");}
                
          		displayBackpack();
          		
                cn.getTextWindow().setCursorPosition(68, 8);
          		cn.getTextWindow().output("Teleport :   "+player.getTpRights());

          	
          		cn.getTextWindow().setCursorPosition(68, 10);
          		cn.getTextWindow().output("Score    :   "+player.getScore());
          	
          		cn.getTextWindow().setCursorPosition(68, 12);
          		cn.getTextWindow().output("Time     :   0");//getTime()
            }
            map[py][px] = 'P';
            cn.getTextWindow().output(px, py, 'P');
            
            char rckey = (char) rkey;
            //        left          right          up            down
            if (rckey == '%' || rckey == '\'' || rckey == '&' || rckey == '(')
                cn.getTextWindow().output(px, py, 'P'); // VK kullanmadan test teknigi
            else cn.getTextWindow().output(rckey);

            if (rkey == KeyEvent.VK_SPACE) {
                String str;
                str = cn.readLine();     // keyboardlistener running and readline input by using enter
                cn.getTextWindow().setCursorPosition(5, 20);
                cn.getTextWindow().output(str);
            }


                keypr = 0;    // last action
            }
            Thread.sleep(20);

        }


    }


    boolean isAlreadyChosen(char[][] map, int row, int column) { // ora henüz bir şeye dönüşmediyse
        return map[row][column] == ':';
    }

    void printMap(char[][] map) {
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 55; j++) {
                cn.getTextWindow().output(j, i, map[i][j]);
            }
        }
    }
    
    void displayBackpack() {
 	   while(!player.getBackpack().isEmpty()) {displayStack.push(player.getBackpack().pop());}
 	   int a = 0;
 	   while(!displayStack.isEmpty()) {
 		   char c = ((String) displayStack.peek()).charAt(0);
 		   player.getBackpack().push(displayStack.pop());
 		   cn.getTextWindow().output(74,13-a,c);
 		   a++;
 	   }
    }

}
