package org.example.Component;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ZodiacComponent {

    public ArrayList<Object> getTodayZodiacRankings() {
        ArrayList<Object> zodiacList = new ArrayList<>();

        // WebDriver 경로 설정 (환경에 맞게 수정)
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");

        WebDriver driver = new ChromeDriver();
        try {
            String url = "https://www.asahi.co.jp/ohaasa/week/horoscope/";
            driver.get(url);

            // ul.oa_horoscope_list 요소 찾기
            WebElement ulElement = driver.findElement(By.className("oa_horoscope_list"));

            // 하위 li 요소들 찾기
            List<WebElement> liElements = ulElement.findElements(By.tagName("li"));

            for (WebElement li : liElements) {
                String text = li.getText();
                System.out.println(text);  // 확인용 출력
                zodiacList.add(text);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit(); // 리소스 정리
        }

        return zodiacList;
    }
}
