package jp.co.sss.lms.ct.f03_report;

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
import org.openqa.selenium.support.ui.Select;

/**
 * 結合テスト レポート機能
 * ケース09
 * 
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

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
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA02");
		webDriver.findElement(By.id("password")).sendKeys("StudentAA00");
		webDriver.findElement(By.cssSelector("input[type='submit']")).click();

		// 待機処理
		visibilityTimeout(By.cssSelector("h2"), 5);

		// 画面タイトル確認
		assertEquals("コース詳細 | LMS", webDriver.getTitle());
		assertEquals("http://localhost:8080/lms/course/detail", webDriver.getCurrentUrl());
		assertTrue(webDriver.findElement(By.cssSelector("li.active")).isDisplayed());

	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {
		webDriver.findElement(By.linkText("ようこそ受講生ＡＡ２さん")).click();

		// 遷移確認
		visibilityTimeout(By.cssSelector("h2"), 5);
		assertEquals("ユーザー詳細", webDriver.getTitle());

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// 週報の修正ボタン
		WebElement row = webDriver.findElement(
				By.xpath("//tr[td[contains(text(),'2025年7月9日')]]"));
		WebElement detailButton = row.findElement(By.xpath(".//input[@type='submit' and @value='修正する']"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", detailButton);
		// 遷移確認
		visibilityTimeout(By.cssSelector("h2"), 5);
		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() {
		// <input type="text" id="intFieldName_0"
		visibilityTimeout(By.id("intFieldName_0"), 5);
		WebElement input = webDriver.findElement(By.id("intFieldName_0"));
		input.clear(); // 値を空にする

		WebElement submitButton = webDriver.findElement(By.cssSelector("button[type='submit']"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", submitButton);

		// 少し待つ（ページが再描画される場合）
		visibilityTimeout(By.id("intFieldName_0"), 3);

		// エラークラスが付与されたかを確認
		input = webDriver.findElement(By.id("intFieldName_0")); // 再取得
		String classes = input.getAttribute("class");
		assertTrue(classes.contains("errorInput"), "学習項目に errorInput クラスが付与されていること");

		input.sendKeys("test");
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() {
		visibilityTimeout(By.id("intFieldValue_0"), 5);

		WebElement selectElement = webDriver.findElement(By.id("intFieldValue_0"));
		Select select = new Select(selectElement);
		select.selectByValue(""); // 空欄を選択

		WebElement submitButton = webDriver.findElement(By.cssSelector("button[type='submit']"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", submitButton);

		// errorInput クラスが付与されているか確認
		selectElement = webDriver.findElement(By.id("intFieldValue_0")); // 再取得
		String classes = selectElement.getAttribute("class");
		assertTrue(classes.contains("errorInput"), "理解度の select に errorInput クラスが付与されていること");

		Select selectAfterError = new Select(selectElement);
		selectAfterError.selectByValue("1");
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() {
		// 目標の達成度 textarea
		WebElement targetInput = webDriver.findElement(By.id("content_0"));
		targetInput.clear();
		targetInput.sendKeys("abc"); // 数値以外

		// 提出ボタン押下
		WebElement submitButton = webDriver.findElement(By.cssSelector("button[type='submit']"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", submitButton);

		// 再取得して errorInput 確認
		targetInput = webDriver.findElement(By.id("content_0"));
		String classes = targetInput.getAttribute("class");
		assertTrue(classes.contains("errorInput"), "目標の達成度に errorInput クラスが付与されること");

		// 正しい数値を入力して再送信
		targetInput.clear();
		targetInput.sendKeys("5");

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() {
		WebElement targetInput = webDriver.findElement(By.id("content_0"));
		targetInput.clear();
		targetInput.sendKeys("20"); // 範囲外 (1-10)

		WebElement submitButton = webDriver.findElement(By.cssSelector("button[type='submit']"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", submitButton);

		targetInput = webDriver.findElement(By.id("content_0"));
		String classes = targetInput.getAttribute("class");
		assertTrue(classes.contains("errorInput"), "目標の達成度が範囲外の場合 errorInput が付与される");

		targetInput.clear();
		targetInput.sendKeys("7"); // 正常値

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {
		WebElement targetInput = webDriver.findElement(By.id("content_0"));
		targetInput.clear();

		// 所感
		WebElement commentInput = webDriver.findElement(By.id("content_1"));
		commentInput.clear();

		WebElement submitButton = webDriver.findElement(By.cssSelector("button[type='submit']"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", submitButton);

		// 再取得して errorInput 確認
		targetInput = webDriver.findElement(By.id("content_0"));
		commentInput = webDriver.findElement(By.id("content_1"));

		assertTrue(targetInput.getAttribute("class").contains("errorInput"), "目標の達成度に errorInput が付与");
		assertTrue(commentInput.getAttribute("class").contains("errorInput"), "所感に errorInput が付与");

		// 正しい値を入力
		targetInput.sendKeys("8");
		commentInput.sendKeys("今週は順調でした");

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {
		WebElement commentInput = webDriver.findElement(By.id("content_1"));
		WebElement weeklyInput = webDriver.findElement(By.id("content_2"));

		String longText = "a".repeat(2001); // 2001文字
		commentInput.clear();
		commentInput.sendKeys(longText);
		weeklyInput.clear();
		weeklyInput.sendKeys(longText);

		WebElement submitButton = webDriver.findElement(By.cssSelector("button[type='submit']"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", submitButton);

		commentInput = webDriver.findElement(By.id("content_1"));
		weeklyInput = webDriver.findElement(By.id("content_2"));

		assertTrue(commentInput.getAttribute("class").contains("errorInput"), "所感に errorInput が付与");
		assertTrue(weeklyInput.getAttribute("class").contains("errorInput"), "一週間の振り返りに errorInput が付与");

		// 文字数を減らして再送信
		commentInput.clear();
		commentInput.sendKeys("適切な文字数");
		weeklyInput.clear();
		weeklyInput.sendKeys("適切な文字数");

		getEvidence(new Object() {
		});
	}

}
