package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String DB_UserName = "postgres";
    private static final String DB_Password = "12345678";
    private static final String DB_URl = "jdbc:postgresql://localhost:5432/lalala";

    private static void getFiles(File rootFile, List<File> fileList) {
        if (rootFile.isDirectory()) {
            //System.out.println("searching: " + rootFile.getAbsolutePath());
            File[] directoryFiles = rootFile.listFiles();
            if (directoryFiles != null) {
                for (File file : directoryFiles) {
                    if (file.isDirectory()) {
                        getFiles(file, fileList);
                    } else {
                        if (file.getName().toLowerCase().endsWith(".xml")) {
                            fileList.add(file);
                        }
                    }
                }
            }
        }
    }

    private static void makeTables(File file) throws ParserConfigurationException, IOException, SAXException, SQLException {
        if (file.exists()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            String info[];
            ArrayList<Zapis> zapisiPervoyTablici = Parser.getSpisok(document);
            info = Parser.getInfo(document);
            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.M.yyyy");
            LocalDate date = LocalDate.parse(info[1], df);
            Connection connection = DriverManager.getConnection(DB_URl, DB_UserName, DB_Password);
            String nov_d_cat = "insert into d_cat_catalog (delivery_date, company, uuid) values (?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(nov_d_cat);
            statement.setString(2, info[2]);
            statement.setString(3, info[0]);
            statement.setDate(1, Date.valueOf(date));
            statement.executeUpdate();

            for (Zapis i : zapisiPervoyTablici) {
                String nov_f_cat = "insert into f_cat_plants (common, botanical, zone, light, price, availability, catalog_id) values (?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement statement2 = connection.prepareStatement(nov_f_cat);
                statement2.setString(1, i.getCommon());
                statement2.setString(2, i.getBotanical());
                statement2.setInt(3, i.getZone());
                statement2.setString(4, i.getLight());
                statement2.setDouble(5, i.getPrice());
                statement2.setInt(6, i.getAvailability());
                Statement statement3 = connection.createStatement();
                String giveMeMyID = "Select id from d_cat_catalog where uuid='" + i.getUuid() + "';";
                ResultSet id = statement3.executeQuery(giveMeMyID);
                id.next();
                statement2.setInt(7, id.getInt("id"));
                statement2.executeUpdate();
            }
        } else {
            System.err.println("файл по пути " + file.getAbsolutePath() + " не существует");
        }
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, SQLException {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> files = new ArrayList<>();
        ArrayList<File> fileList = new ArrayList<>();
        String folderAdress = "";

        int flag = 0;
        System.out.println("добавлять пути к файлу можно сколько угодно(пока не кончится оперативная память)");
        System.out.println("добавлять одновременно пути к файлу и путь к дирректории возможно, причем дубликаты стираются)");
        while (true) {
            System.out.println("1. добавить путь к файлу");
            System.out.println("2. ввести путь к папке с файлами");
            System.out.println("3. выполнить парсинг файлов");
            System.out.println("4. выйти");

            int command;
            try {
                command = scanner.nextInt();
            } catch (Exception e) {
                System.err.println("извините, но вы ввели ерунду, программа закрывается");
                return;
            }
            switch (command) {

                case (1):
                    System.out.println("путь к файлу:");
                    scanner.nextLine();
                    try {
                        fileList.add(new File(scanner.nextLine()));
                    } catch (Exception e) {
                    }
                    break;
                case (2):
                    System.out.println("путь к папке с файлами:");
                    scanner.nextLine();
                    folderAdress = scanner.nextLine();
                    fileList.clear();
                    getFiles(new File(folderAdress), fileList);
                    break;
                case (3):
                    if (fileList.isEmpty()) {
                        System.out.println("невозможно выполнить запрос, не было передано ни одного пути");
                    }
                    //поиск по папке
                    else {
                        List<File> listWithoutDuplicates = new ArrayList<>(new HashSet<>(fileList));
                        for (File i : listWithoutDuplicates) {
                            makeTables(i);
                        }
                    }
                    break;
                case (4):
                    flag = 1;
                    break;
            }
            if (flag == 1) {
                break;
            }
        }

    }
}