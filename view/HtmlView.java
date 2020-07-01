package com.javarush.task.task28.task2810.view;

import com.javarush.task.task28.task2810.Controller;
import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class HtmlView implements View {
    private Controller controller;
    private final String filePath = "./4.JavaCollections/src/"
            + this.getClass().getPackage().getName().replace('.', '/') + "/vacancies.html";


    @Override
    public void update(List<Vacancy> vacancies) {

        try {
            updateFile(getUpdatedFileContent(vacancies));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void userCitySelectEmulationMethod() {
        this.controller.onCitySelect("Odessa");
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies) {
        try {
            Document document = getDocument();
            /*
            2. Получи элемент, у которого есть класс template.
            Сделай копию этого объекта, удали из нее атрибут "style" и класс "template".
            Используй этот элемент в качестве шаблона для добавления новой строки в таблицу вакансий.
             */
            Element template = document.getElementsByClass("template").first();
            Element copyTemplate = template.clone();
            copyTemplate.removeClass("template").removeAttr("style");

            /*
            Удали все добавленные ранее вакансии. У них единственный класс "vacancy".
            В файле backup.html это одна вакансия - Junior Java Developer.
            Нужно удалить все теги tr, у которых class="vacancy".
            Но тег tr, у которого class="vacancy template", не удаляй.
            Используй метод remove.
             */
            Elements elements = document.getElementsByClass("vacancy");
            for (Element element : elements) {
                if (!element.hasClass("template")) element.remove();
            }

            for (Vacancy vacancy : vacancies) {
                //склонируй шаблон тега, полученного в п.2. Метод clone.
                Element vacancyElement = copyTemplate.clone();
                // получи элемент, у которого есть класс "city". Запиши в него название города из вакансии.
                vacancyElement.getElementsByClass("city").append(vacancy.getCity());
                vacancyElement.getElementsByClass("companyName").append(vacancy.getCompanyName());
                vacancyElement.getElementsByClass("salary").append(vacancy.getSalary());
                //получи элемент-ссылку с тегом a. Запиши в него название вакансии(title).
                //Установи реальную ссылку на вакансию вместо href="url".
                vacancyElement.getElementsByTag("a").append(vacancy.getTitle()).attr("href", vacancy.getUrl());
                // добавь outerHtml элемента, в который ты записывал данные вакансии,
                //непосредственно перед шаблоном <tr class="vacancy template" style="display: none">
                template.before(vacancyElement.outerHtml());
            }
            // Верни html код всего документа в качестве результата работы метода.
            return document.html();
        //В случае возникновения исключения, выведи его стек-трейс и верни строку "Some exception occurred"
        } catch (IOException e) {
            e.printStackTrace();
            return "Some exception occurred";
        }
    }

    private void updateFile(String content) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Document getDocument() throws IOException {
        return Jsoup.parse(new File(filePath), "UTF-8");
    }
}
