package com.example.spring2key.spring2key.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity

public class TodoTask {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Date date;
    private Boolean checked;
    private String username;

    public TodoTask() {
    }

    public TodoTask(Long id, String name,Date date, Boolean checked, String username) {
        this.id = id;
        this.name = name;
        this.date=date;
        this.checked = checked;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", checked=" + checked +
                ", username='" + username + '\'' +
                '}';
    }
}
