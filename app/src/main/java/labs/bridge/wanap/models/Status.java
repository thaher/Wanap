package labs.bridge.wanap.models;

import java.io.Serializable;

public class Status implements Serializable {

    String id;
    String name;
    String status;

    public Status() {
    }

    public Status(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getId() {
        if(id==null) return "0";
        else
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
