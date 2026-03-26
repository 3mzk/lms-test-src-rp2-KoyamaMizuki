package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト よくある質問機能
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		goTo("http://localhost:8080/lms");
		assertEquals("ログイン | LMS", webDriver.getTitle());
		assertEquals("http://localhost:8080/lms/", webDriver.getCurrentUrl());

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA01");
		webDriver.findElement(By.id("password")).sendKeys("StudentAA00");
		webDriver.findElement(By.cssSelector("input[type='submit']")).click();

		// 待機処理
		visibilityTimeout(By.cssSelector("h2"), 5);

		// 画面タイトル確認
		assertEquals("コース詳細 | LMS", webDriver.getTitle());
		assertEquals("http://localhost:8080/lms/course/detail", webDriver.getCurrentUrl());
		assertTrue(webDriver.findElement(By.cssSelector("li.active")).isDisplayed());

		// 遷移後の画面でメッセージを確認
		WebElement msg = webDriver.findElement(By.cssSelector("small"));
		assertTrue(msg.getText().contains("ようこそ"));

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		// 上部メニュー開く
		webDriver.findElement(By.cssSelector("a.dropdown-toggle")).click();
		webDriver.findElement(By.cssSelector("a[href='/lms/help']")).click();

		// ヘルプ画面に遷移するまでの待機処理
		visibilityTimeout(By.cssSelector("h2"), 5);

		// 画面タイトル検証
		assertEquals("ヘルプ | LMS", webDriver.getTitle());
		assertEquals("http://localhost:8080/lms/help", webDriver.getCurrentUrl());

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		// 画面操作
		String originalWindow = webDriver.getWindowHandle();
		webDriver.findElement(By.cssSelector("a[href='/lms/faq']")).click();

		// 別タブを開く
		for (String window : webDriver.getWindowHandles()) {
			if (!window.equals(originalWindow)) {
				webDriver.switchTo().window(window);
				break;
			}
		}

		// 別タブ表示の待機処理
		visibilityTimeout(By.cssSelector("h2"), 5);

		// 画面タイトル検証
		assertEquals("よくある質問 | LMS", webDriver.getTitle());
		assertEquals("http://localhost:8080/lms/faq", webDriver.getCurrentUrl());

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {
		// 研修関係でカテゴリ検索
		webDriver.findElement(By.cssSelector("a[href='/lms/faq?frequentlyAskedQuestionCategoryId=1']"))
				.click();
		WebElement result = webDriver.findElement(By.cssSelector("dt.mb10"));

		// 該当箇所にスクロール
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", result);

		// 検索結果を検証
		assertTrue(webDriver.getCurrentUrl().contains("frequentlyAskedQuestionCategoryId=1"));

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {
		WebElement question = webDriver.findElement(By.cssSelector("dt.mb10"));
		// 該当箇所にスクロール
		((JavascriptExecutor) webDriver)
				.executeScript("arguments[0].scrollIntoView(true);", question);

		// 検索結果の質問をクリック
		question.click();

		// 回答を検証
		assertTrue(webDriver.findElement(By.cssSelector("dd[id^='answer-h']")).isDisplayed());

		getEvidence(new Object() {
		});
	}

}
