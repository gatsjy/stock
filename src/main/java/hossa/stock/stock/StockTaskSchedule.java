package hossa.stock.stock;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import hossa.stock.stock.domain.Company;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Gatsjy
 * @since 2020-11-08
 * realize dreams myself
 * Blog : https://blog.naver.com/gkswndks123
 * Github : https://github.com/gatsjy
 */
@Component
public class StockTaskSchedule {

    // 매일 오후 13시00분 - 13시 59분 사이에 1분 간격 으로 실행
    //@Scheduled(cron = "0 0/1 13 * * *")
    @Scheduled(fixedRate = 60000)
    public void takeTarget(){
        // 1. 해당 시간마다 스케줄러 돌리기
        //String todayTime = LocalDateTime.now().minusDays(3).format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        //String yesterdayTime = LocalDateTime.now().minusDays(4).format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String todayTime = "202011060902";
        String yesterdayTime = "202011050902";

        System.out.println("====********============The current todayTime : " + todayTime);
        System.out.println("====********============The current yesterdayTime : " + yesterdayTime);

        // 2. 원하는 종목 체크
        // 원하는 종목에 대한 배열(원하는 종목 가져올 수 있도록 디비화 해야할 듯)...
        // 아직 디비화 하기 전까지는 그냥 원하는 종목 만큼 스트링 배열에 넣어서 for문으로 해결할 예정
        List<Company> stockEventes = new ArrayList<>();
        stockEventes.add(new Company("피에스엠씨","024850"));
        stockEventes.add(new Company("우리기술투자","041190"));
        stockEventes.add(new Company("소마젠","950200"));
        stockEventes.add(new Company("얼라인드","238120"));
        stockEventes.add(new Company("우리조명","037400"));
        stockEventes.add(new Company("오성첨단소재","052420"));
        stockEventes.add(new Company("동일철강","023790"));
        stockEventes.add(new Company("위지트","036090"));
        stockEventes.add(new Company("에이티넘인베스트","021080"));
        stockEventes.add(new Company("이퓨쳐","134060"));
        stockEventes.add(new Company("바이오스마트","038460"));

        // 원하는 종목 만큼 돌리기
        // 현재 시작을 표출
        // 하루 전의 데이터를 가져와서 체크 한다.
        // url을 만들어줘야 하는데 일단 page navigation 하는 로직도 짜야함.. -> page navigation 하는 로직은 굳이 필요 없음
        // String PrevUrl = "https://finance.naver.com/item/sise_time.nhn?code=051910&thistime=202011062217";
        for (Company stockEvent : stockEventes) {

            Document doc = null;
            String curMessage = "";
            String prevMessage = "";

            // 1. 현재 시간 관련 로직
            String CurUrl = String.format("https://finance.naver.com/item/sise_time.nhn?code=%s&thistime=%s", stockEvent.getStock_id() , todayTime);
            try {
                doc = Jsoup.connect(CurUrl).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 1.1 현재 테이블 파싱
            Elements tableList = doc.select("table.type2");
            for (Element table : tableList) {
                Elements rowList = table.select("tr:gt(0)");
                for (Element element : rowList) {
                    Elements cellList = rowList.select("td");
                    curMessage = " " + stockEvent.getName() + " 체결시간 = " + cellList.get(1).text() + " 체결가 = " + cellList.get(5).text() + " 거래량 = " +cellList.get(6).text();
                }
            }

            // 2. 어제 시간 관련 로직
            String PrevUrl = String.format("https://finance.naver.com/item/sise_time.nhn?code=%s&thistime=%s", stockEvent.getStock_id() , yesterdayTime);
            try {
                doc = Jsoup.connect(PrevUrl).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements tableList2 = doc.select("table.type2");
            for (Element table : tableList) {
                Elements rowList = table.select("tr:gt(0)");
                for (Element element : rowList) {
                    Elements cellList = rowList.select("td");
                    prevMessage = " " + stockEvent.getName() + " 체결시간 = " + cellList.get(1).text() + " 체결가 = " + cellList.get(2).text() + " 거래량 = " +cellList.get(6).text();
                }
            }

            // 3. 원하는 값을 추출해서 해당 값이 조건에 맞다면
            // 조건 1. 전일 9시 거래량이 오늘 9시 거래량보다 많다
            // 조건 2. 어제 마감된 가격이...
            // 아 모르겠고 일단 현재의 가격이 어제 거래량 보다 높을 때
            // 해당 사항을 만족한다면 슬랙으로 해당 내용을 푸시함


                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
                    .disableNotification(false);

            SendResponse sendResponse = bot.execute(request);
            boolean ok = sendResponse.isOk();
            Message message = sendResponse.message();
        }
    }
}
