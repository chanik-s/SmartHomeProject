package com.example.myapplication;

import java.io.Serializable;

// php에서 json 파싱한 값 저장할 클래스(sensor) 만듦
public class Sensor implements Serializable { //데이터 클래스 직렬화?
    private static final long serialVersionUID=1L; //, 객체를 전달측과 수신하는 측에서 사용하는 클래스 파일이 동일한지 체크
    private String humidity;
    private String temperature;
    private String time;
    private String PM;

    public String getHumidity(){
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPM() {
        return PM;
    }

    public void setPM(String PM) {
        this.PM = PM;
    }
}
