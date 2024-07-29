package sg.nus.iss.javaspring.adprojrct.Models;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private double budget;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    /*@JsonBackReference*/
    private User user;

    private int type;
    //0 -> system; 1 -> user defined
    //0 -> info can not be changed
    //1 -> everything can be changed

    @OneToMany(mappedBy = "category")
    private List<Transaction> transactions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double amount) {
        this.budget = amount;
    }

    public Category() {}

}
