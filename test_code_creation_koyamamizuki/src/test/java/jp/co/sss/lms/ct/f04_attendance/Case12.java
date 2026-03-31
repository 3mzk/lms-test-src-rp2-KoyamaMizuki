package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * 結合テスト 勤怠管理機能
 * ケース12
 * 
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース12 受講生 勤怠直接編集 入力チェック")
public class Case12 {

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
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() {
		webDriver.findElement(By.linkText("勤怠")).click();
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
	@DisplayName("テスト05 不適切な内容で修正してエラー表示：出退勤の（時）と（分）のいずれかが空白")
	void test05() {
		Select startHour = new Select(
				webDriver.findElement(
						By.name("attendanceList[0].trainingStartTimeHour")));

		startHour.selectByValue("9");

		Select startMinute = new Select(
				webDriver.findElement(
						By.name("attendanceList[0].trainingStartTimeMinute")));

		startMinute.selectByValue(""); // 分を空白

		Select endHour = new Select(
				webDriver.findElement(
						By.name("attendanceList[0].trainingEndTimeHour")));
		endHour.selectByValue("18");
		Select endMinute = new Select(
				webDriver.findElement(
						By.name("attendanceList[0].trainingEndTimeMinute")));
		endMinute.selectByValue("0");

		// 更新ボタン
		WebElement submitButton = webDriver.findElement(By.name("complete"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", submitButton);
		// ダイアログでOK押す
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();
		// エラー文検証
		assertTrue(webDriver.getPageSource().contains("出勤時間が正しく入力されていません"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正してエラー表示：出勤が空白で退勤に入力あり")
	void test06() {
		// 出勤を空白
		Select startHour = new Select(
				webDriver.findElement(
						By.name("attendanceList[0].trainingStartTimeHour")));

		startHour.selectByValue("");

		Select startMinute = new Select(
				webDriver.findElement(
						By.name("attendanceList[0].trainingStartTimeMinute")));

		startMinute.selectByValue("");

		// 退勤を18:00にプルダウン選択
		Select endHour = new Select(
				webDriver.findElement(
						By.name("attendanceList[0].trainingEndTimeHour")));

		endHour.selectByValue("18");
		Select endMinute = new Select(
				webDriver.findElement(
						By.name("attendanceList[0].trainingEndTimeMinute")));
		endMinute.selectByValue("0");


		// 更新クリック
		WebElement submitButton = webDriver.findElement(By.name("complete"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", submitButton);
	
		// ダイアログでOK押す
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();
		// エラー文検証
		assertTrue(webDriver.getPageSource().contains("出勤情報がないため退勤情報を入力出来ません"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正してエラー表示：出勤が退勤よりも遅い時間")
	void test07() {
		new Select(webDriver.findElement(
				By.name("attendanceList[0].trainingStartTimeHour")))
				.selectByValue("18");

		new Select(webDriver.findElement(
				By.name("attendanceList[0].trainingStartTimeMinute")))
				.selectByValue("0");

		new Select(webDriver.findElement(
				By.name("attendanceList[0].trainingEndTimeHour")))
				.selectByValue("9");

		new Select(webDriver.findElement(
				By.name("attendanceList[0].trainingEndTimeMinute")))
				.selectByValue("0");

		// スクロールして更新
		WebElement complete = webDriver.findElement(By.name("complete"));
		((JavascriptExecutor) webDriver)
				.executeScript("arguments[0].scrollIntoView(true);", complete);
		complete.click();
		// ダイアログでOK押す
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();
		// エラー文検証
		assertTrue(webDriver.getPageSource().contains("退勤時刻[0]は出勤時刻[0]より後でなければいけません。"));
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正してエラー表示：出退勤時間を超える中抜け時間")
	void test08() {
		new Select(webDriver.findElement(
				By.name("attendanceList[0].trainingStartTimeHour")))
				.selectByValue("9");

		new Select(webDriver.findElement(
				By.name("attendanceList[0].trainingStartTimeMinute")))
				.selectByValue("0");
		new Select(webDriver.findElement(
				By.name("attendanceList[0].trainingEndTimeHour")))
				.selectByValue("15");

		new Select(webDriver.findElement(
				By.name("attendanceList[0].trainingEndTimeMinute")))
				.selectByValue("0");

		new Select(webDriver.findElement(
				By.name("attendanceList[0].blankTime")))
				.selectByValue("465"); // 中抜け時間を7時間45分に設定

		// スクロールして更新
		WebElement complete = webDriver.findElement(By.name("complete"));
		((JavascriptExecutor) webDriver)
				.executeScript("arguments[0].scrollIntoView(true);", complete);
		complete.click();
		// ダイアログでOK押す
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();
		// エラー文検証
		assertTrue(webDriver.getPageSource().contains("中抜け時間が勤務時間を超えています。"));

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正してエラー表示：備考が100文字超")
	void test09() {
		WebElement note = webDriver.findElement(By.name("attendanceList[0].note"));

		note.clear();
		String longNote = "a".repeat(101);
		note.sendKeys(longNote);


		// スクロールして更新
		WebElement complete = webDriver.findElement(By.name("complete"));
		((JavascriptExecutor) webDriver)
				.executeScript("arguments[0].scrollIntoView(true);", complete);
		complete.click();

		// ダイアログでOK押す
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();
		// エラー文検証
		assertTrue(webDriver.getPageSource().contains("備考の長さが最大値(100)を超えています。"));
		getEvidence(new Object() {
		});

	}

}
