package es.uji.apps.jinq.db;

import javax.persistence.*;

@Entity
public class Detail
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "MASTER_ID")
    private Master master;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Master getMaster()
    {
        return master;
    }

    public void setMaster(Master master)
    {
        this.master = master;
    }

    @Override
    public String toString()
    {
        return "Detail{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
