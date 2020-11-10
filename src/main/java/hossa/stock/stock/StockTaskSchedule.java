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

    // 매일 오후 9시00분 - 9시 59분 사이에 10분 간격 으로 실행
    //@Scheduled(cron = "0 0/10 9 * * *")
    @Scheduled(fixedRate = 60000)
    public void takeTarget(){
        // 1. 해당 시간마다 스케줄러 돌리기
        String todayTime = LocalDateTime.now().minusMinutes(6450).format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String yesterdayTime = LocalDateTime.now().minusMinutes(7890).format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        //String todayTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        //String yesterdayTime = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        System.out.println("====********============The current todayTime : " + todayTime);
        System.out.println("====********============The current yesterdayTime : " + yesterdayTime);

        // 2. 원하는 종목 체크
        // 원하는 종목에 대한 배열(원하는 종목 가져올 수 있도록 디비화 해야할 듯)...
        // 아직 디비화 하기 전까지는 그냥 원하는 종목 만큼 스트링 배열에 넣어서 for문으로 해결할 예정
        /* */
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

        /*2020-11-10, 한주안, 부하 테스트용 데이터 추가*/
        stockEventes.add(new Company("미코바이오메드","214610"));
        stockEventes.add(new Company("피플바이오","304840"));
        stockEventes.add(new Company("빅히트","352820"));
        stockEventes.add(new Company("넥스틴","348210"));
        stockEventes.add(new Company("원방테크","053080"));

        // 원하는 종목 만큼 돌리기
        // 현재 시작을 표출
        // 하루 전의 데이터를 가져와서 체크 한다.
        // url을 만들어줘야 하는데 일단 page navigation 하는 로직도 짜야함.. -> page navigation 하는 로직은 굳이 필요 없음
        // String PrevUrl = "https://finance.naver.com/item/sise_time.nhn?code=051910&thistime=202011062217";
        for (Company stockEvent : stockEventes) {

            Document doc = null;
            String curMessage = "";
            String prevMessage = "";
            // 2020-11-10, 한주안, 마지막 메세지 추가
            String prevLastMessage = "";

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
                curMessage = rowList.get(1).text();
                //System.out.println("curMessage = " + curMessage);
            }

            // 1.2. 어제 시간 관련 로직
            String PrevUrl = String.format("https://finance.naver.com/item/sise_time.nhn?code=%s&thistime=%s", stockEvent.getStock_id() , yesterdayTime);
            try {
                doc = Jsoup.connect(PrevUrl).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements tableList2 = doc.select("table.type2");
            for (Element table : tableList2) {
                Elements rowList = table.select("tr:gt(0)");
                prevMessage = rowList.get(1).text();
                //System.out.println("prevMessage = " + prevMessage);
            }
            
            // 1.3. 어제 시간 중 종가 관련 로직
            String PrevLastUrl = String.format("https://finance.naver.com/item/sise_time.nhn?code=%s&thistime=%s", stockEvent.getStock_id() , yesterdayTime.substring(0,8)+"1700");
            try {
                doc = Jsoup.connect(PrevLastUrl).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements tableList3 = doc.select("table.type2");
            for (Element table : tableList3) {
                Elements rowList = table.select("tr:gt(0)");
                prevLastMessage = rowList.get(1).text();
            }
            
            // 3. 어제와 오늘의 거래량 비교
            // 0 : 체결시간
            // 1 : 체결가
            // 2 : 전일비
            // 3 : 매도
            // 4 : 매수
            // 5 : 거래량
            // 6 : 변동량
            String[] curMessages = curMessage.split(" ");
            String[] prevMessages = prevMessage.split(" ");
            String[] prevLastMessages = prevLastMessage.split(" ");

            if (prevMessages.length > 0 && curMessages.length > 0 && prevLastMessages.length > 0){
                // 1. 조건 1. 전일 9시00분 거리랭 < 금일 9시 00분 거래량
                if (prevMessages[5].length() > 1 && curMessages[5].length() > 1){
                    int tradeVolume = Integer.parseInt(curMessages[5].replace(",","")) - Integer.parseInt(prevMessages[5].replace(",",""));
                    if (tradeVolume > 0 ){

                        // 2. 조건 2. 전일 종가(15시30분) < 금일 9시 00분 종가
                        if(curMessages[1].length() > 1 && prevLastMessages[5].length() > 1){
                            int priceVolume = Integer.parseInt(curMessages[1].replace(",","")) - Integer.parseInt(prevLastMessages[1].replace(",",""));
                            if(priceVolume > 0){

                                        "*====="+ stockEvent.getName()+"(" + stockEvent.getStock_id() + ") =====" + "\n" +
                                                "*                체결시간 / 체결가 / 전일비 / 매도 / 매수 / 거래량 / 변동량  " + "\n" +
                                                "* 오늘     : " + " "+curMessage + "\n" +
                                                "* 하루 전 : " + " "+prevMessage + "\n" +
                                                "* 하루 전 종가 : "+ " " +prevLastMessage + "\n" +
                                                "* 조건 1. 전일 9시00분 거리랭 < 금일 9시 00분 거래량 : +" + Integer.toString(tradeVolume) + "\n" +
                                                "* 조건 2. 전일 종가(15시30분) < 금일 9시 00분 종가 : +" + Integer.toString(priceVolume))
                                        .parseMode(ParseMode.HTML)
                                        .disableWebPagePreview(true)
                                        .disableNotification(false);

                                SendResponse sendResponse = bot.execute(request);
                                boolean ok = sendResponse.isOk();
                                Message message = sendResponse.message();
                            }
                        }
                    }
                }
            }
        }
    }
}
