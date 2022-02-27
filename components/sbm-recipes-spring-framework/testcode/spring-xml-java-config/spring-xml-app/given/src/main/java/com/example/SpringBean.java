package com.example;
import javax.sql.DataSource;

public class SpringBean {
    private DataSource dataSource;
    private int timeout;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setTimeout(int timeout) {
        this.timeout = this.timeout;
    }
}