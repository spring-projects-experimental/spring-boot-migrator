package com.example.jee.app.ejb.local;

import javax.ejb.Stateless;

@Stateless
public class ABean implements ABusinessInterface {

     @Override
     public String businessMethod() {
          return "A";
     }
}