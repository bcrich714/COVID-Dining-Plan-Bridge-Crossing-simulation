public class LinkedList {
    
    //Private Attributes
    private Customer head, tail, current;
    private int queueSize;

    //Default Constructor
    public LinkedList()
    {
        queueSize = 0;
    }

    //Constructor with an initial customer
    public LinkedList (Customer cust)
    {
        head = cust;
        tail = cust;
        queueSize = 1;
        current = head;
    }

    //Gets/Sets the head of the list
    public Customer getHead()
    {
        return head;
    }

    public void setHead(Customer cust)
    {
        head = cust;
    }

    //Gets/Sets the tail of the list
    public Customer getTail()
    {
        return tail;
    }

    public void setTail(Customer cust)
    {
        tail = cust;
    }

    //Adds a customer to the end of the list (like a queue)
    public void addToList(Customer cust)
    {
        //If the list is empty, set the new customer as the head and tail. if not, just add it to the end
        if (queueSize == 0)
        {
            head = cust;
            tail = cust;
        }
        else
        {
            tail.setNextCustomer(cust);
            cust.setPrevCustomer(tail);
            tail = cust;
        }
        queueSize++;
        current = tail;
    }

    //Removes the front-most customer from the list (NOT USED)
    public void removeFromList()
    {
        head = head.getNextCustomer();
        head.setPrevCustomer(null);
        queueSize--;
    }

    //Sets the current customer to the front customer
    public void reset()
    {
        current = head;
    }

    //Sets the current customer to the next customer in line
    public void next()
    {
        current = current.getNextCustomer();
    }

    //Gets the amount of customers in the line
    public int getListSize()
    {
        return queueSize;
    }

    //Gets the currently selected customer
    public Customer getCurrentCust()
    {
        return current;
    }

    //Prints the details of each customer in the list
    public void print()
    {
        reset();
        System.out.println("Customer   Arrives     Seats    Leaves");
        while (current != null)
        {
            current.print();
            current = current.getNextCustomer();
        }
    }
}
