package model;

import lombok.Data;

import java.time.LocalDate;
@Data

public class Task {
    private String id;
    private String taskDesc;
    private LocalDate date;
    private boolean state;

    public Task(String id, String desc, LocalDate date, boolean state){
        this.id = id;
        this.taskDesc = desc;
        this.date = date;
        this.state = state;
    }
}
