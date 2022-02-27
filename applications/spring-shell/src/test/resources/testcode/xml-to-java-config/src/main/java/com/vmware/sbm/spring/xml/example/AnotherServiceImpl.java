package org.springframework.sbm.spring.xml.example;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnotherServiceImpl {
    List<String> theList;
    private AppleService appleService;

    public AnotherServiceImpl(AppleService appleService) {
        this.appleService = appleService;
    }

}
