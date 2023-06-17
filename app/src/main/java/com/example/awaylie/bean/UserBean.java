package com.example.awaylie.bean;

public class UserBean {
    private String number;//电话号，用户唯一识别id
    private String name;//用户设置的昵称
    private String signature;//用户的个性签名
    private String city;//用户的城市
    private String birth;//用户的出生年月日
    private int sex;//用户的性别，0表示男，1表示女
    private String password;//用户密码，存储时需要处理


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
