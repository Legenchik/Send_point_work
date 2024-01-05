package com.Send_point.WebVersion_Control;

import com.Send_point.Gmail_And_Sheets_Points.BuildClass.Student;
import com.Send_point.Gmail_And_Sheets_Points.BuildClass.StudentsMore;
import com.Send_point.Gmail_And_Sheets_Points.Google_Gmail.SendConrol;
import com.Send_point.Gmail_And_Sheets_Points.Google_Sheets.GetDataFromPoint_Sheets_Web;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ControllerForWeb {
    List<StudentsMore> students = new ArrayList<>();
    @GetMapping("/greeting")
    public String greeting(Model model) {
        model.addAttribute("name", "namefdsfsdfsd");
        return "greeting";
    }

    private String AllInfoAboutStudents(String tableOfPoints,
                                        String tableOfEmail,
                                        int firstNumberToPoints,
                                        int startPointsNumber,
                                        int PIB,
                                        int rowForDontUsed,
                                        String oneOrMore,
                                        int RowWithName,
                                        int RowWithLastName,
                                        int RowWithEmail,
                                        String ChoiceForEmailTable){

        try {
            students= GetDataFromPoint_Sheets_Web.StudentSubjectAndPoint(tableOfPoints,
                                                                        tableOfEmail,
                                                                        firstNumberToPoints,
                                                                        startPointsNumber,
                                                                        PIB,
                                                                        rowForDontUsed,
                                                                        oneOrMore,
                                                                        RowWithName,
                                                                        RowWithLastName,
                                                                        RowWithEmail,
                                                                        ChoiceForEmailTable);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        String out="";
        for(StudentsMore s:students){
            out=out+s.forTest()+"\n";
        }
        return out;
    }

    @PostMapping("/greeting")
    public String greetingPost(@RequestParam(name = "TableOfPoints") String tableOfPoints,
                               @RequestParam(name = "TableOfEmail") String tableOfEmail,
                               @RequestParam(name = "FirsNumberToPoints") Integer firstNumberToPoints,
                               @RequestParam(name = "StartPointsNumber") Integer startPointsNumber,
                               @RequestParam(name = "RowWith_PIB") Integer PIB,
                               @RequestParam(name = "RowForDontUsed") Integer rowForDontUsed,
                               @RequestParam(name = "OneOrMore") String OneOrMore,
                               @RequestParam(name = "RowWithName") Integer RowWithName,
                               @RequestParam(name = "RowWithLastName") Integer RowWithLastName,
                               @RequestParam(name = "RowWithEmail") Integer RowWithEmail,
                               @RequestParam(name = "ChoiceForEmailTable") String ChoiceForEmailTable,Model model){
        model.addAttribute("name",AllInfoAboutStudents(tableOfPoints,
                                                tableOfEmail,
                                                firstNumberToPoints,
                                                startPointsNumber,
                                                PIB,
                                                rowForDontUsed,
                                                OneOrMore,
                                                RowWithName,
                                                RowWithLastName,
                                                RowWithEmail,
                                                ChoiceForEmailTable));
        return "CheckAndSend";
    }
    @PostMapping("/AllGood")
    public String allGood(Model model){
        String out;
        try {
            out=SendConrol.SendPointAll(students);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("result",out);
        return "GoodSend";
    }

}
