package com.example.awaylie.bean;

public class TruthBean {
    private int id;
    private int verifyId;
    private String name;
    private String time;
    private String content;

    public TruthBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVerifyId() {
        return verifyId;
    }

    public void setVerifyId(int verifyId) {
        this.verifyId = verifyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "TruthBean{" +
                "verifyId=" + verifyId +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
