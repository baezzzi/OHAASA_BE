package org.example.Component;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SeleniumCrawler {

    public List<Map<String, String>> crawl() throws IOException {
        File driverFile = new ClassPathResource("driver/chromedriver").getFile();
        boolean success = driverFile.setExecutable(true);
        if(!success) {
            System.out.println("설정 실패" + driverFile.getAbsolutePath());
        }
        System.setProperty("webdriver.chrome.driver", driverFile.getAbsolutePath());

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get("https://www.asahi.co.jp/ohaasa/week/horoscope/");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement ulElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.className("oa_horoscope_list"))
            );

            List<WebElement> liElement = ulElement.findElements(By.tagName("li"));
            List<Map<String, String>> result = new ArrayList<>();

            // li rank 속성 분리
            for (WebElement li : liElement) {
                String classAttr = li.getAttribute("class");
                String[] classes = classAttr.split(" ");
                String sign = classes[classes.length - 1];

                String horoTxt = "";
                try {
                    String horoElement = li.findElement(By.className("horo_txt")).getText();
                    String[] horoText = horoElement.split(" ");
                    horoTxt = String.join("\n", horoText);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                Map<String, String> item = new HashMap<>();
                item.put("horoTxt", horoTxt);
                item.put("name", sign);
                result.add(item);
            }

            return result;
        } finally {
            driver.quit();
        }
    }

}
