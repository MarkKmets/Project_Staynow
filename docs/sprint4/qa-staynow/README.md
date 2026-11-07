# Автотести модуля бронювання StayNow

**Команда:** Backrooms. **Модуль:** staynow-module v1.0.0. **Пакет:** ua.kpi.tzyapz.staynow. **Вимоги:** SRS 1.1, BL-10…BL-70.

## Середовище і збірка

Потрібні JDK 17, Maven 3.9, Chrome та доступ до інтернету. Selenium Manager отримує драйвер автоматично.

```powershell
git clone https://github.com/MarkKmets/Project_Staynow.git
cd Project_Staynow/docs/sprint4/qa-staynow
mvn -v
mvn clean test
```

Клонування можна зробити через GitHub Desktop. В IntelliJ відкрити папку з pom.xml.

## Запуск тестів

```powershell
mvn clean test
mvn test "-DexcludedGroups=none" "-Dtest=*UiTest"
mvn test "-DexcludedGroups=none" "-Dheadless=false" "-Dtest=HomePageUiTest"
```

Перша команда запускає юніт-набір, друга — лише UI, третя — тести головної сторінки з видимим браузером. `mvn test "-DexcludedGroups=none"` запускає обидва набори; загальний BUILD FAILURE на v1 зумовлений дефектами юніт-модуля. Звіти зберігаються в target/surefire-reports/.

Початкова публікація містить основи трьох юніт-класів і два успішні ExampleTest. Після додавання всіх частин: **155 запусків, 21 failure, 1 error**. UI: **9 / 0 / 0**, повторено двічі. [Протокол](docs/surefire-summary.md) містить дати, команди та список невдалих тестів.

## Структура

| Шлях | Зміст |
|---|---|
| src/main/java/ua/kpi/tzyapz/staynow/ | Незмінений модуль v1: три класи й enum |
| src/test/java/ua/kpi/tzyapz/staynow/ | Юніт-тести за класами модуля та ExampleTest |
| src/test/java/ua/kpi/tzyapz/staynow/ui/ | Дев’ять WebDriver-тестів і спільна підготовка |
| docs/coverage-matrix.md | BL → правило → тести → рецензент; зауваження review |
| docs/ui-scenarios.md | TC → кроки, локатори, очікування, тест-метод |
| docs/surefire-summary.md | Протокол Surefire і failed → BUG |
| selenium-ide/staynow.side | Smoke-набір Selenium IDE |
| selenium-ide/export/ | Два Java JUnit-експорти; довідкові чернетки поза Maven-збіркою |

## Розподіл тестів

| Учасник | Роль | Частина | Підготовлено методів | PR із фактичним внеском |
|---|---|---|---|---|
| Кмець Марк | ТА | BL-10: дати й ночі | 9 | [tests/kmets](https://github.com/MarkKmets/Project_Staynow/pulls?q=is%3Apr%20head%3Atests%2Fkmets) |
| Тіньков Данііл | КТ | BL-20 і кількість гостей | 13 | [tests/tinkov](https://github.com/MarkKmets/Project_Staynow/pulls?q=is%3Apr%20head%3Atests%2Ftinkov) |
| Гатченко Ярослав | ІТ | BL-30, BL-40, BL-50: ціна ночі | 7 | [tests/hatchenko](https://github.com/MarkKmets/Project_Staynow/pulls?q=is%3Apr%20head%3Atests%2Fhatchenko) |
| Сирота Володимир | СМ | BL-50: загальна сума | 14 | [tests/syrota](https://github.com/MarkKmets/Project_Staynow/pulls?q=is%3Apr%20head%3Atests%2Fsyrota) |
| Сімутін Владислав | АО | BL-60 і BL-70 | 14 | [tests/simutin](https://github.com/MarkKmets/Project_Staynow/pulls?q=is%3Apr%20head%3Atests%2Fsimutin) |

Рецензент — Сирота Володимир; його частину перевіряє Тіньков Данііл. Тести, які відтворюють BUG-019…024, залишаються червоними на v1. Звіти реєструються в [GitHub Issues](https://github.com/MarkKmets/Project_Staynow/issues) з міткою module.

[Досьє спринта](../README.md), [аудит](../audit.md), [таблиця задач](../task-board.md), [ретро](../retro.md).
