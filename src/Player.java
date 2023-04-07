public class Player {
    int x;
    int y;
    Stack backpack;
    int score;
    int tpRight;
    
    public Player(int x,int y) {
    	this.x = x;
    	this.y = y;
    	backpack = new Stack(8);
    	score = 0;
    	tpRight = 3;
    }
    
    
    public void addNewItem(Object item) {
    	if(backpack.isFull()) {
    		backpack.pop();
    		gainScore(item);}
    	else {
    		if(backpack.isEmpty()) {backpack.push(item);}
    		else {gainScore(item);}}
    	
    
    }
    
    void gainScore(Object item) {
    	if(backpack.peek().equals(item)) {
    		if(item.equals("1")) {score+=10;}
    		else if (item.equals("2")) {score+=40;}
    		else {score+=90;tpRight++;}
    		backpack.pop();}
    	else {backpack.push(item);}}
    
    
    
    public Stack getBackpack() {
    	return backpack;
    }
    
    
    public int getScore() {
    	return score;
    }
    
    public int getTpRights() {
    	return tpRight;
    }
    
}
