package com.Send_point.getData;

import java.util.ArrayList;
import java.util.List;

public class Student {

    private String numberOfGroup;
    private String name;
    private String  subject;
    private String email;
    private String subjectWords;

    public String getSubjectWords() {
        return subjectWords;
    }

    public void setSubjectWords(String subjectWords) {
        this.subjectWords = subjectWords;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumberOfGroup() {
        return numberOfGroup;
    }

    public void setNumberOfGroup(String numberOfGroup) {
        this.numberOfGroup = numberOfGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String allInfo(){
        return numberOfGroup+"; " +name + "; " +subject +email+"\n"+subjectWords+"\n";
    }
}
