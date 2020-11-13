package hossa.stock.stock;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class StockApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void sendMsgTest(){
		TelegramBot bot = new TelegramBot("1308465026:AAHOrMFyULrupxEnhkPIsNjGJ0o-4uF0q7U");
		SendMessage request = new SendMessage("729845849", "wtf!!! hello bot!!")
				.parseMode(ParseMode.HTML)
				.disableWebPagePreview(true)
				.disableNotification(false);

		SendResponse sendResponse = bot.execute(request);
		boolean ok = sendResponse.isOk();
		Message message = sendResponse.message();

	}

}
