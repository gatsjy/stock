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
/*
		TelegramBot bot = new TelegramBot("");

		SendMessage request = new SendMessage("", "wtf!!! hello bot!!")
				.parseMode(ParseMode.HTML)
				.disableWebPagePreview(true)
				.disableNotification(false);

		SendResponse sendResponse = bot.execute(request);
		boolean ok = sendResponse.isOk();
		Message message = sendResponse.message();
*/

	}

}
