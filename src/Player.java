public class Player {
    int x;
    int y;
    Stack backpack;
    int score;
    int teleportRight;
    int tpRight;
    Stack tempStack = new Stack(8);

    public Player(int x,int y) {
        this.x = x;
        this.y = y;
        backpack = new Stack(8);
        score = 0;
        tpRight = 3;
    }


    public void addNewItem(Object item) {

        if(item == "1") {score += 10;}
        else if(item == "2") {score += 40;}
        else{score += 90;tpRight ++;}

        if(backpack.isFull()) {
            while(!backpack.isEmpty()) {
                tempStack.push(backpack.pop());
            }
            if(tempStack.peek() == "1") {score-=10;}
            else if(tempStack.peek() == "2") {score-=40;}
            else {score -= 90;}
            tempStack.pop();

            while(!tempStack.isEmpty()) {
                backpack.push(tempStack.pop());
            }
            backpack.push(item);

        }
        else {backpack.push(item);}
    }



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
