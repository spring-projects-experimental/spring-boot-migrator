package org.springboot.example.upgrade;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Student {
    @Id
    private long id;
    private String name;
}
