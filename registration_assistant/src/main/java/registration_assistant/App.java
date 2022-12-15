package registration_assistant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class App extends Application {
    private static ArrayList<Section> sections;

    public static ArrayList<Section> getSections() {
        return sections;
    }
    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {
        ArrayList<Course> courses = ReadCSV.readDegreePlan(
                getClass().getResource("/data/DegreePlan.csv").getFile());
        ArrayList<FinishedCourse> finishedCourses = ReadCSV.readFinishedCourse(
                getClass().getResource("/data/FinishedCourses.csv").getFile());
        sections = ReadCSV.readCourseOffering(
                getClass().getResource("/data/CourseOffering.csv").getFile());
        Schedule Mainschedule ;

        File studentFolder = Paths.get("src", "main", "resources", "student").toFile();
        if (studentFolder.exists()) {
            FileInputStream fileInputStream = new FileInputStream(
                    getClass().getResource("/student/schedule.ser").getFile());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Schedule schedule = (Schedule) objectInputStream.readObject();
            Mainschedule=schedule;
        } else {
            studentFolder.mkdir();
            Schedule schedule = new Schedule("202220", new ArrayList<Section>());
            Mainschedule=schedule;

            FileOutputStream fileOutputStream = new FileOutputStream(new File(studentFolder, "schedule.ser"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(schedule);
        }
        
        Student student = new Student(finishedCourses, Mainschedule);
        for(int i=0;i<sections.size();i++){
            sections.get(i).setSchedule(Mainschedule);
        }
        
        student.remainCourses(courses, finishedCourses, sections);
        ScrollPane root = FXMLLoader.load(getClass().getClassLoader().getResource("views/index.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setMinWidth(1563.05);
        stage.setMaxWidth(1563.05);
        stage.setTitle("Registration Assistant");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
