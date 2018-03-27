package mini.com.shoppinglist;

import java.io.Serializable;

public class Product implements Serializable {
    public String Name;
    public Product(String name)
    {
        Name = name;
    }
}
