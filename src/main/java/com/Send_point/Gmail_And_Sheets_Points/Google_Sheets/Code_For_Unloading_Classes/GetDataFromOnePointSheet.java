package com.Send_point.Gmail_And_Sheets_Points.Google_Sheets.Code_For_Unloading_Classes;


import com.Send_point.Gmail_And_Sheets_Points.BuildClass.StudentsMore;

import java.util.ArrayList;
import java.util.List;

public class GetDataFromOnePointSheet {

    public List<StudentsMore> sheet_Processing(List<List<Object>> values,
                                                    String nameOfSheets,
                                               int behindUp,
                                               int rowForDontUsed,
                                               int StarPointForSubject,
                                               int rowWith_PIB){
        List<StudentsMore> students = new ArrayList<>();
        if(values.isEmpty()){
            System.out.println("Нема данних "+nameOfSheets);
        }else{
            boolean checkForOneTime=true; //перевірка щоб записать предмети
            boolean forSubjestWithWords=true;
            int rowCount=0;
            int countSubject=0;
            List<String> subject = new ArrayList<>();
            List<String> subjectWithPoints =new ArrayList<>();  //TODO clear the mass
            String name="";
            for(List row:values){
                StudentsMore std = new StudentsMore();
                List<String> subjectWithPointAnd_Words = new ArrayList<>();
                if(behindUp<=rowCount && rowCount!=rowForDontUsed){
                    if(checkForOneTime){   //Предмети
                        for (int range = StarPointForSubject; range < row.size(); range++) {
                            subject.add(row.get(range).toString());
                            countSubject++;
                        }
                        checkForOneTime=false;
                    }else{    //Оцінки

                        if(forSubjestWithWords) {
                            for (int range = StarPointForSubject; range < row.size(); range++) {
                                int forThis = range - StarPointForSubject;
                                subjectWithPoints.add(forThis, subject.get(forThis).toString() + "- " + row.get(range));
                            }
                            name=row.get(rowWith_PIB).toString();
                            forSubjestWithWords=false;
                        }else{
                            for (int range = StarPointForSubject; range < row.size(); range++) {
                                int forThis = range - StarPointForSubject;
                                subjectWithPointAnd_Words.add(forThis, subjectWithPoints.get(forThis).toString() + "- " + row.get(range));
                            }
                            if(subjectWithPointAnd_Words.size()<countSubject){
                                for(int range = subjectWithPointAnd_Words.size();range<countSubject;range++){
                                    subjectWithPointAnd_Words.add(range, subjectWithPoints.get(range).toString() + " ");
                                }
                            }
                            std.setNumberOfGroup(nameOfSheets);
                            std.setName(name);
                            std.setSubjectList(subjectWithPointAnd_Words);
                            students.add(std);
                            subjectWithPoints.clear();
                            name="";
                            forSubjestWithWords=true;
                        }

                    }
                }
                rowCount++;
            }
        }

        return students;
    }
}
