package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト 勤怠管理機能
 * ケース11
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース11 受講生 勤怠直接編集 正常系")
public class Case11 {

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
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() {
		webDriver.findElement(By.linkText("勤怠")).click();
		// アラートでOK選択
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();
		// 待機処理
		visibilityTimeout(By.cssSelector("h2"), 5);
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「勤怠情報を直接編集する」リンクから勤怠情報直接変更画面に遷移")
	void test04() {
		webDriver.findElement(By.linkText("勤怠情報を直接編集する")).click();

		visibilityTimeout(By.cssSelector("h2"), 5);
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 すべての研修日程の勤怠情報を正しく更新し勤怠管理画面に遷移")
	void test05() {
		// 空文字””をプルダウン選択
		WebElement selectElement = webDriver.findElement(By.name("attendanceList[1].trainingStartTimeHour"));
		Select select = new Select(selectElement);
		// 出勤時間を9:00にプルダウン選択
		select.selectByValue("9");
		 selectElement = webDriver.findElement(By.name("attendanceList[1].trainingStartTimeMinute"));
		 select = new Select(selectElement);
		select.selectByValue("0");
		// 退勤時間を18:00にプルダウン選択
		 
		selectElement = webDriver.findElement(By.name("attendanceList[1].trainingEndTimeHour"));
		select = new Select(selectElement);
		select.selectByValue("18");
		 selectElement = webDriver.findElement(By.name("attendanceList[1].trainingEndTimeMinute"));
		 select = new Select(selectElement);
		 select.selectByValue("0");
		// 更新ボタンをクリック
		webDriver.findElement(By.cssSelector("input[type='submit']")).click();
		// ダイアログでOK押す
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();
		
		visibilityTimeout(By.cssSelector("h2"), 5);
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());

		getEvidence(new Object() {
		});
	}

}
