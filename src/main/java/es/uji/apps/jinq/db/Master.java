package es.uji.apps.jinq.db;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Master
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "master")
    private Set<Detail> details;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Set<Detail> getDetails()
    {
        return details;
    }

    public void setDetails(Set<Detail> detail)
    {
        this.details = detail;
    }

    @Override
    public String toString()
    {
        return "Master{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
