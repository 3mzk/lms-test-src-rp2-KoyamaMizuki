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
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

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
		// ログイン画面の検証
		assertEquals("ログイン | LMS", webDriver.getTitle());
	    assertEquals("http://localhost:8080/lms/", webDriver.getCurrentUrl());
	    // エビデンス取得	  
	    getEvidence(new Object() {
	    });
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		// ログイン情報の入力
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA01");
		webDriver.findElement(By.id("password")).sendKeys("StudentAA00");
		webDriver.findElement(By.cssSelector("input[type='submit']")).click();

		// 待機処理
		visibilityTimeout(By.cssSelector("h2"), 5);

		// 画面タイトル確認
		assertEquals("コース詳細 | LMS", webDriver.getTitle());
		assertEquals("http://localhost:8080/lms/course/detail", webDriver.getCurrentUrl());
		assertTrue(webDriver.findElement(By.cssSelector("li.active")).isDisplayed());

		// ログイン後のメッセージを検証
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

		// ヘルプ画面の検証
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

		// よくある質問画面の検証
		assertEquals("よくある質問 | LMS", webDriver.getTitle());
		assertEquals("http://localhost:8080/lms/faq", webDriver.getCurrentUrl());

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() {
		// キーワード検索
		webDriver.findElement(By.id("form")).sendKeys("キャンセル料");
		webDriver.findElement(By.cssSelector("input[type='submit']")).click();

		// 検索結果の表示まで待機
		visibilityTimeout(By.cssSelector("dt.mb10"), 5);
		
		WebElement result = webDriver.findElement(By.cssSelector("dt.mb10"));
		// 検索結果までスクロール
		((JavascriptExecutor) webDriver)
		.executeScript("arguments[0].scrollIntoView(true);", result);
		
		// 検索結果を検証
		assertTrue(result.getText().contains("Q.キャンセル料・途中退校について"));

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() {
		// 画面操作
		WebElement keyword = webDriver.findElement(By.id("form"));
		webDriver.findElement(By.cssSelector("input[type='button']")).click();
		
		// テキストボックスが空になっているか確認
		assertTrue(keyword.getAttribute("value").isEmpty());

		getEvidence(new Object() {
		});
	}

}
