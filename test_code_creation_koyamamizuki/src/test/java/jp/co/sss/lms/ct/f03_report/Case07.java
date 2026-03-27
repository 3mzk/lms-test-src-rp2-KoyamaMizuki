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
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {

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
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {

		WebElement row = webDriver.findElement(
				By.xpath("//tr[.//span[contains(text(), '未提出')]]"));
		// 該当箇所にスクロール
		((JavascriptExecutor) webDriver)
			.executeScript("arguments[0].scrollIntoView({block:'center'});", row);

		// 詳細ボタンをクリック
		row.findElement(By.cssSelector("input[value='詳細']")).click();
		// 遷移先の検証
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());
		assertTrue(webDriver.getCurrentUrl().contains("/section/detail"));
		
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// ボタン名の検証
		// 提出ボタンをクリック
		webDriver.findElement(By.cssSelector("input[type='submit']")).click();

		// 遷移先の検証
		visibilityTimeout(By.cssSelector("h2"), 5);
		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		assertTrue(webDriver.getCurrentUrl().contains("/report/regist"));
		
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {
		// 報告内容を入力
		webDriver.findElement(By.cssSelector("textarea.form-control")).sendKeys("テスト");
		
		// 提出ボタンをクリック
		webDriver.findElement(By.cssSelector("button[type='submit']")).click();
		//遷移先の検証
		visibilityTimeout(By.cssSelector("h2"), 5);
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());
		
		WebElement report =webDriver.findElement(
				By.cssSelector("input[value='提出済み日報【デモ】を確認する']"));
		assertTrue(report.isDisplayed());
		
		getEvidence(new Object() {
		});
	}

}
