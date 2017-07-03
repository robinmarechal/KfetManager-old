package fr.polytech.marechal.models;

import fr.polytech.marechal.libs.api.UrlParametersMap;
import fr.polytech.marechal.libs.mvc.models.Model;
import fr.polytech.marechal.libs.mvc.models.ModelManager;
import fr.polytech.marechal.models.managers.CustomersManager;
import fr.polytech.marechal.models.managers.OrdersManager;
import fr.polytech.marechal.models.managers.StaffsManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Robin
 * @date 25/06/2017
 */
public class Customer extends Model<Customer>
{
    private String firstname;
    private String lastname;
    private int staffId;
    private double balance;

    private Staff staff;
    private ArrayList<Order> orders;


    public String getFirstname ()
    {
        return firstname;
    }

    public void setFirstname (String firstname)
    {
        this.firstname = firstname;
    }

    public String getLastname ()
    {
        return lastname;
    }

    public void setLastname (String lastname)
    {
        this.lastname = lastname;
    }

    public int getStaffId ()
    {
        return staffId;
    }

    public void setStaffId (int staffId)
    {
        this.staffId = staffId;
    }

    public double getBalance ()
    {
        return balance;
    }

    public void setBalance (double balance)
    {
        this.balance = balance;
    }

    public Staff getStaff ()
    {
        return staff;
    }

    public void setStaff (Staff staff)
    {
        this.staff = staff;
    }

    public ArrayList<Order> getOrders ()
    {
        return orders;
    }

    public void setOrders (ArrayList<Order> orders)
    {
        this.orders = orders;
    }

    public void addOrder (Order order)
    {
        this.orders.add(order);
    }

    public Customer loadStaff ()
    {
        return loadStaff(new UrlParametersMap());
    }

    public Customer loadStaff (UrlParametersMap parameters)
    {
        staff = new StaffsManager().find(staffId, parameters);
        return this;
    }

    public Customer loadOders ()
    {
        return loadOders(new UrlParametersMap());
    }

    public Customer loadOders (UrlParametersMap parameters)
    {
        orders = new OrdersManager().ofUrl("customers/" + getId() + "/orders", parameters);
        return this;
    }

    @Override

    protected void recopy (Customer obj)
    {
        firstname = obj.firstname;
        lastname = obj.lastname;
        staffId = obj.staffId;
        balance = obj.balance;
        staff = obj.staff;
        orders = obj.orders;
    }

    @Override
    public boolean existsInDatabase ()
    {
        return false;
    }

    @Override
    public boolean save ()
    {
        return false;
    }

    @Override
    public Customer loadAll ()
    {
        Customer tmp = new CustomersManager().find(getId(), new UrlParametersMap().withAllRelations());
        recopy(tmp);
        return this;
    }

    @Override
    public Customer loadAll (UrlParametersMap parameters)
    {
        loadStaff(parameters);
        loadOders(parameters);
        return this;
    }

    @Override
    public ModelManager<Customer> getManagerInstance ()
    {
        return new CustomersManager();
    }

    @Override
    public HashMap<String, Object> toHashMap ()
    {
        return null;
    }

    @Override
    public String toString ()
    {
        return "Customer{" + "id=" + getId() + ", firstname='" + firstname + '\'' + ", lastname='" + lastname + '\'' + ", staffId=" +
                staffId + ", balance=" + balance + ", staff=" + staff + ", orders=" + orders + '}';
    }
}
