public class Queue {
    private int rear, front;
    private Object[] elements;

    Queue(int capacity) {
        elements = new Object[capacity];
        rear=-1;
        front=0;
    }

    public void enqueue(Object object){
        if(isFull())
            System.out.println("Queue Overflow");
        else {
            rear++;
            elements[rear] = object;
        }


    }

    public Object dequeue() {
        if(isEmpty()){
            System.out.println("Queue is empty");
            return null;
        }
        else {
            Object object = elements[front];
            elements[front] = null;
            front++;
            return object;
        }
    }

    public Object peek() {
        if(isEmpty()){
            System.out.println("Queue is empty");
            return null;
        }
        else {

            return elements[front];
        }
    }

    public boolean isFull() {
        return (rear+1 == elements.length);
    }

    public boolean isEmpty() {
        return rear<front;
    }

    public int size() {
        return rear- front+1;
    }
}

