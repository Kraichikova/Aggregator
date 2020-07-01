package com.javarush.task.task28.task2810;

import com.javarush.task.task28.task2810.model.HHStrategy;
import com.javarush.task.task28.task2810.model.Model;
import com.javarush.task.task28.task2810.model.MoikrugStrategy;
import com.javarush.task.task28.task2810.model.Provider;
import com.javarush.task.task28.task2810.view.HtmlView;
import com.javarush.task.task28.task2810.view.View;

import java.util.ArrayList;

public class Aggregator {
    public static void main(String[] args) {
        HtmlView htmlView = new HtmlView();
        Model model = new Model(htmlView, new Provider(new HHStrategy()), new Provider(new MoikrugStrategy()));
        Controller controller = new Controller(model);

        htmlView.setController(controller);
        htmlView.userCitySelectEmulationMethod();

    }
}
