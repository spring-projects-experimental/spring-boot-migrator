package org.springframework.sbm.spring.xml.example;

public class AppleService {
    private TestBean country;

    public void setCountry(TestBean country) {
        this.country = country;
    }

    public TestBean getCountry() {
        return country;
    }
}
