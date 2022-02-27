package com.example.jee.app.ejb.local;

import javax.ejb.Local;

@Local
public interface ABusinessInterface {
    String businessMethod();
}