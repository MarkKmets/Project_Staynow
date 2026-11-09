# Протокол запуску Surefire

**Команда:** Backrooms. **Продукт:** StayNow. **Модуль:** staynow-module v1.0.0.  
**Виконавець запуску:** Гатченко Ярослав (ІТ). **Дата:** 09.11.2026.  
**Версія проєкту:** завантажена копія main після злиття внесків учасників.

## Середовище

| Параметр | Значення |
|---|---|
| ОС | Windows 11, amd64 |
| Java | 17.0.21, Eclipse Adoptium |
| Maven | Apache Maven 3.9.16 |
| JUnit | Jupiter 5.10.2 |
| Surefire | 3.2.5 |
| Selenium | 4.21.0 |
| Браузер UI | Chrome 157.0.8103.38 |

Команди виконано з папки docs/sprint4/qa-staynow. Версії Java та Maven перевірено командою `mvn -v`.

## Юніт тести

Команда: `mvn clean test`. Завершення: **09.11.2026, 11:53:42 +02:00**. Тривалість: **6.129 с**.

```text
Tests run: 155, Failures: 21, Errors: 1, Skipped: 0
BUILD FAILURE
```

| Клас | Запусків | Failures | Errors | Skipped |
|---|---|---|---|---|
| BookingValidatorTest | 50 | 3 | 0 | 0 |
| PriceCalculatorTest | 66 | 14 | 0 | 0 |
| AvailabilityTest | 37 | 4 | 1 | 0 |
| ExampleTest | 2 | 0 | 0 | 0 |
| **Разом** | **155** | **21** | **1** | **0** |

## Невдалі тести і дефекти

Індекси в квадратних дужках позначають виконання параметризованих методів.

| Дефект | Тести, що не пройшли | Результат |
|---|---|---|
| [BUG-019](https://github.com/MarkKmets/Project_Staynow/issues/7), BL-10 | BookingValidatorTest.allowedNightsAreAccepted[5] — 30 ночей | 1 failure |
| [BUG-020](https://github.com/MarkKmets/Project_Staynow/issues/8), BL-10 | BookingValidatorTest.checkInYesterdayCheckOutTomorrow; severalViolationsReportedTogether | 2 failures |
| [BUG-021](https://github.com/MarkKmets/Project_Staynow/issues/9), BL-30/50 | PriceCalculatorTest.weekendNight[5], [7]; nightPrice[4], [5], [6], [7]; totalFridayNightOnly; totalSundayNightOnly; totalSixNightsNoDiscount; totalAcrossSeasonBoundary; totalSaturdayAndSundayInHighSeasonStart | 11 failures |
| [BUG-022](https://github.com/MarkKmets/Project_Staynow/issues/10), BL-50 | PriceCalculatorTest.nightPriceRoundsHalfUpAboveHalf; nightPriceRoundsHalfUpAtExactHalf; nightPriceRoundsHalfUpSmallValue | 3 failures |
| [BUG-023](https://github.com/MarkKmets/Project_Staynow/issues/11), BL-60 | AvailabilityTest.cancellationFee[3]; refund[1] — скасування за 7 днів | 2 failures |
| [BUG-024](https://github.com/MarkKmets/Project_Staynow/issues/12), BL-70 | AvailabilityTest.intervalAgainstExistingStay[8], [9]; backToBackBookingsAllowed | 2 failures, 1 error |

backToBackBookingsAllowed завершується **Error** через IllegalStateException під час другого суміжного бронювання. Решта 21 невдале виконання — **Failure** через розбіжності очікуваного й фактичного результату.

Разом успішні 133 виконання; 22 невдалі виконання відтворюють шість відомих дефектів v1. Очікування тестів залишено за SRS для регресійної перевірки виправленої версії. Шість звітів зареєстровані в [GitHub Issues](https://github.com/MarkKmets/Project_Staynow/issues) з міткою module.

## UI тести

Для кожного з двох прогонів виконано:

```powershell
mvn test "-DexcludedGroups=none" "-Dtest=*UiTest"
```

| Прогін | Завершення 09.11.2026 | Тривалість | Tests run | Failures | Errors | Skipped | Результат |
|---|---|---|---|---|---|---|---|
| 1 | 12:01:31 +02:00 | 30.214 с | 9 | 0 | 0 | 0 | BUILD SUCCESS |
| 2 | 12:02:27 +02:00 | 24.794 с | 9 | 0 | 0 | 0 | BUILD SUCCESS |

В обох прогонах: HomePageUiTest — 2 виконання, AdminLoginUiTest — 3, ContactFormUiTest — 4. Пропущених тестів немає.

Попередження Selenium щодо CDP 157 не перешкодили виконанню цих WebDriver-сценаріїв: обидва прогони завершилися BUILD SUCCESS. У терміналі зазначено platform encoding Cp1252; частина українських повідомлень відображена знаками питання. Числові результати, назви методів і типи невдалих перевірок читаються у вихідному журналі.

## Висновок

Повний набір зі злитої версії проєкту відтворив очікувані шість дефектів модуля v1: **155 / 21 / 1 / 0**. UI-набір двічі пройшов без невдач: **9 / 0 / 0 / 0**. Показники наведено як Tests run / Failures / Errors / Skipped.
