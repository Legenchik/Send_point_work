package com.Send_point.Gmail_And_Sheets_Points.BuildClass;

import java.util.ArrayList;
import java.util.List;

public class StudentsMore extends Student{
    private List<String> SubjectList = new ArrayList<>();

    public List<String> getSubjectList() {
        return SubjectList;
    }

    public void setSubjectList(List<String> subjectList) {
        SubjectList = subjectList;
    }
    public String forSendEmail(){
        String out="";
        for(String s:SubjectList){
            out=out+s+"\n";
        }
        return out;
    }

    public String forErrorSend(){
        return getNumberOfGroup()+" "+getName();
    }

    public String forTest(){
        String out="";
        for(String s:SubjectList){
            out=out+s+"\n";
        }

        return getNumberOfGroup()+" "+getName()+" "+getEmail()+"\n"+out;
    }
}
