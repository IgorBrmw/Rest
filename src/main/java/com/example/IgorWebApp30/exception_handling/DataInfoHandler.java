package com.example.IgorWebApp30.exception_handling;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class DataInfoHandler {

    private String msg;

    public DataInfoHandler(String msg) {
        this.msg = msg;
    }

    public DataInfoHandler() {
    }

    public String getMsg() {
        return msg;
    }

    public DataInfoHandler getInstanceWithInfo(String msg) {
        return new DataInfoHandler(msg);
    }
}
