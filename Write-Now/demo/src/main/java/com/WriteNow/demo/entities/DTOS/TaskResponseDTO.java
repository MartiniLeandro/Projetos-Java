package com.WriteNow.demo.entities.DTOS;

import com.WriteNow.demo.entities.Task;

public class TaskResponseDTO {
    private Long id;
    private String title;
    private String content;

    public TaskResponseDTO(Task task){
        this.id = task.getId();
        this.title = task.getTitle();
        this.content = task.getContent();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
