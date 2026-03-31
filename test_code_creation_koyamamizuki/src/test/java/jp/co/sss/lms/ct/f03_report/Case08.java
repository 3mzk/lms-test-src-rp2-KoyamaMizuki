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

/**
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

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
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA02");
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
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		WebElement row = webDriver
				.findElement(
						By.xpath("//tr[.//td[contains(text(),'2025年7月10日(木)')] and .//td[contains(text(),'Java概要')]]"

						));
		// 該当箇所にスクロール
		((JavascriptExecutor) webDriver)
				.executeScript("arguments[0].scrollIntoView({block:'center'});", row);

		// 詳細ボタンをクリック
		row.findElement(By.cssSelector("input[value='詳細']")).click();
		visibilityTimeout(By.cssSelector("h2"), 5);
		// 遷移先の検証
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());
		assertTrue(webDriver.getCurrentUrl().contains("/section/detail"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// 確認ボタンをクリック
		webDriver.findElement(By.cssSelector("input[type='submit']")).click();

		// 遷移先の検証
		visibilityTimeout(By.cssSelector("h2"), 5);
		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		assertTrue(webDriver.getCurrentUrl().contains("/report/regist"));

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {

		WebElement textarea = webDriver.findElement(By.cssSelector("textarea.form-control"));

		textarea.clear();
		textarea.sendKeys("修正後のテスト内容");

		webDriver.findElement(By.cssSelector("button[type='submit']")).click();

		//遷移先の検証
		visibilityTimeout(By.cssSelector("h2"), 5);
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {
		// 「ようこそ○○さん」リンクをクリック
		webDriver.findElement(By.linkText("ようこそ受講生ＡＡ２さん")).click();

		// 遷移確認
		visibilityTimeout(By.cssSelector("h2"), 5);
		assertEquals("ユーザー詳細", webDriver.getTitle());

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {

		WebElement row = webDriver.findElement(
				By.xpath("//tr[td[contains(text(),'2025年7月10日')]]"));

		//jsでクリック

		WebElement detailButton = row.findElement(By.xpath(".//input[@type='submit' and @value='詳細']"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", detailButton);
		// 遷移確認
		visibilityTimeout(By.cssSelector("h2"), 5);

		WebElement content = webDriver.findElement(By.id("contents"));

		// ★ デバッグ出力
		System.out.println("↓↓↓ 画面内容 ↓↓↓");
		System.out.println(content.getText());

		// 検証
		assertTrue(content.getText().contains("修正後のテスト内容"));
	}

}
