
package com.rangermerah.recyletor;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("resultscore")
    @Expose
    private float resultscore;
    @SerializedName("tag")
    @Expose
    private List<String> tag = null;
    @SerializedName("age")
    @Expose
    private List<Integer> age = null;
    @SerializedName("gender")
    @Expose
    private String gender;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public float getResultscore() {
        return resultscore;
    }

    public void setResultscore(float resultscore) {
        this.resultscore = resultscore;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public List<Integer> getAge() {
        return age;
    }

    public void setAge(List<Integer> age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
