import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gravity {
   public enigma.console.Console cn = Enigma.getConsole("Gravity",100,35,22,0);
   public TextMouseListener tmlis; 
   public KeyListener klis; 

   // ------ Standard variables for mouse and keyboard ------
   public int mousepr;          // mouse pressed?
   public int mousex, mousey;   // mouse text coords.
   public int keypr;   // key pressed?
   public int rkey;    // key   (for press/release)
   // ----------------------------------------------------

   
   Gravity() throws Exception {   // --- Contructor
                 
      // ------ Standard code for mouse and keyboard ------ Do not change
      tmlis=new TextMouseListener() {
         public void mouseClicked(TextMouseEvent arg0) {}
         public void mousePressed(TextMouseEvent arg0) {
            if(mousepr==0) {
               mousepr=1;
               mousex=arg0.getX();
               mousey=arg0.getY();
            }
         }
         public void mouseReleased(TextMouseEvent arg0) {}
      };
      cn.getTextWindow().addTextMouseListener(tmlis);
    
      klis=new KeyListener() {
         public void keyTyped(KeyEvent e) {}
         public void keyPressed(KeyEvent e) {
            if(keypr==0) {
               keypr=1;
               rkey=e.getKeyCode();
            }
         }
         public void keyReleased(KeyEvent e) {}
      };
      cn.getTextWindow().addKeyListener(klis);
      // ----------------------------------------------------

      int px=5,py=5;
      cn.getTextWindow().output(px,py,'P');

      char[][] map = new char[25][55];
      for (int i = 0; i < 25; i++) {
         for (int j = 0; j < 55; j++) {
            if (i == 0 || i == 24 || j == 0 || j == 54) {
               map[i][j] = '#';
            }
            else if (i == 8 && j < 50) {
               map[i][j] = '#';
            }
            else if (i == 16 && j > 4) {
               map[i][j] = '#';
            } else {
               map[i][j] = ' ';
            }
            cn.getTextWindow().output(j,i,map[i][j]);
         }
      }




      while(true) {
         if(mousepr==1) {  // if mouse button pressed
            cn.getTextWindow().output(mousex,mousey,'#');  // write a char to x,y position without changing cursor position
            px=mousex; py=mousey;
            
            mousepr=0;     // last action  
         }
         if(keypr==1) {    // if keyboard button pressed
            if(rkey==KeyEvent.VK_LEFT && map[py][px-1] != '#') {
               cn.getTextWindow().output(px, py, ' ');
               px--; }
            if(rkey==KeyEvent.VK_RIGHT && map[py][px+1] != '#'){
               cn.getTextWindow().output(px, py, ' ');
               px++;}
            if(rkey==KeyEvent.VK_UP && map[py-1][px] != '#'){
               cn.getTextWindow().output(px, py, ' ');
               py--;}
            if(rkey==KeyEvent.VK_DOWN&& map[py+1][px] != '#'){
               cn.getTextWindow().output(px, py, ' ');
               py++;}
            
            char rckey=(char)rkey;
            //        left          right          up            down
            if(rckey=='%' || rckey=='\'' || rckey=='&' || rckey=='(') cn.getTextWindow().output(px,py,'P'); // VK kullanmadan test teknigi
            else cn.getTextWindow().output(rckey);
            
            if(rkey==KeyEvent.VK_SPACE) {
               String str;         
               str=cn.readLine();     // keyboardlistener running and readline input by using enter 
               cn.getTextWindow().setCursorPosition(5, 20);
               cn.getTextWindow().output(str);
            }
            
            keypr=0;    // last action  
         }
         Thread.sleep(20);

      }




   }
}
